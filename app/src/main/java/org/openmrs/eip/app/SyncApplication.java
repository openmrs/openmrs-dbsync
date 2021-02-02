package org.openmrs.eip.app;

import java.security.Security;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.DeadLetterChannelBuilder;
import org.apache.camel.builder.NoErrorHandlerBuilder;
import org.apache.camel.processor.idempotent.jpa.JpaMessageIdRepository;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.openmrs.eip.component.SyncProfiles;
import org.openmrs.eip.component.camel.StringToLocalDateTimeConverter;
import org.openmrs.eip.component.service.TableToSyncEnum;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;

import liquibase.integration.spring.SpringLiquibase;

@SpringBootApplication(scanBasePackages = {
        "org.openmrs.eip.app",
        "org.openmrs.eip.component",
        "org.openmrs.utils.odoo"
})
public class SyncApplication {

    private CamelContext camelContext;

    private final static Set<TableToSyncEnum> IGNORE_TABLES;

    static {
        IGNORE_TABLES = new HashSet();
        IGNORE_TABLES.add(TableToSyncEnum.CONCEPT_ATTRIBUTE);
        IGNORE_TABLES.add(TableToSyncEnum.LOCATION_ATTRIBUTE);
        IGNORE_TABLES.add(TableToSyncEnum.PROVIDER_ATTRIBUTE);
        IGNORE_TABLES.add(TableToSyncEnum.CONCEPT);
        IGNORE_TABLES.add(TableToSyncEnum.LOCATION);
    }

    public SyncApplication(final CamelContext camelContext) {
        this.camelContext = camelContext;
    }

    public static void main(final String[] args) {
        SpringApplication.run(SyncApplication.class, args);
    }

    @PostConstruct
    private void addTypeConverter() {
        camelContext.getTypeConverterRegistry().addTypeConverter(LocalDateTime.class, String.class, new StringToLocalDateTimeConverter());
    }

    @PostConstruct
    private void addBCProvider() {
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * Bean to handle messages in error and re-route them to another route
     *
     * @return deadLetterChannelBuilder
     */
    @Bean
    public DeadLetterChannelBuilder deadLetterChannelBuilder() {
        DeadLetterChannelBuilder builder = new DeadLetterChannelBuilder("direct:dlc");
        builder.setUseOriginalMessage(true);
        return builder;
    }

    @Bean("outBoundErrorHandler")
    public DeadLetterChannelBuilder getOutBoundErrorHandler() {
        DeadLetterChannelBuilder builder = new DeadLetterChannelBuilder("direct:outbound-error-handler");
        builder.setUseOriginalMessage(true);
        return builder;
    }

    @Bean("inBoundErrorHandler")
    public DeadLetterChannelBuilder getInBoundErrorHandler() {
        DeadLetterChannelBuilder builder = new DeadLetterChannelBuilder("direct:inbound-error-handler");
        builder.setUseOriginalMessage(true);
        return builder;
    }

    @Bean("noErrorHandler")
    public NoErrorHandlerBuilder getNoErrorHandler() {
        return new NoErrorHandlerBuilder();
    }

    @Bean("jpaIdempotentRepository")
    @Profile(SyncProfiles.SENDER)
    public JpaMessageIdRepository getJpaIdempotentRepository(@Qualifier("mngtEntityManager") EntityManagerFactory emf) {
        return new JpaMessageIdRepository(emf, "complexObsProcessor");
    }

    @Bean
    @Profile(SyncProfiles.SENDER)
    public PropertySource getCustomPropertySource(ConfigurableEnvironment env) {
        //Custom PropertySource that we can dynamically populate with generated property values which
        //is not possible via the properties file e.g. to specify names of tables to sync.
        final String dbName = env.getProperty("openmrs.db.name");
        Set<String> tables = new HashSet(TableToSyncEnum.values().length);
        for (TableToSyncEnum tableToSyncEnum : TableToSyncEnum.values()) {
            //TODO Remove the enum values instead including services
            if (IGNORE_TABLES.contains(tableToSyncEnum)) {
                continue;
            }

            tables.add(dbName + "." + tableToSyncEnum.name());
        }

        Map<String, Object> props = Collections.singletonMap("debezium.tablesToSync", StringUtils.join(tables, ","));
        PropertySource customPropSource = new MapPropertySource("custom", props);
        env.getPropertySources().addLast(customPropSource);

        return customPropSource;
    }

    @Bean(name = "liquibase")
    public SpringLiquibase getSpringLiquibaseForMgtDB(@Qualifier("mngtDataSource") DataSource dataSource, Environment env) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setContexts(env.getActiveProfiles()[0]);
        liquibase.setDataSource(dataSource);
        liquibase.setChangeLog("classpath:liquibase-master.xml");
        liquibase.setDatabaseChangeLogTable("liquibasechangelog");
        liquibase.setDatabaseChangeLogLockTable("liquibasechangeloglock");
        liquibase.setShouldRun(false);

        return liquibase;
    }

}