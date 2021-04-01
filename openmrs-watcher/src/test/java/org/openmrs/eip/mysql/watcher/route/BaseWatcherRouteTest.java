package org.openmrs.eip.mysql.watcher.route;

import static org.openmrs.eip.mysql.watcher.WatcherConstants.PROP_URI_ERROR_HANDLER;
import static org.openmrs.eip.mysql.watcher.WatcherTestConstants.URI_MOCK_ERROR_HANDLER;

import org.apache.camel.EndpointInject;
import org.apache.camel.component.mock.MockEndpoint;
import org.openmrs.eip.BaseDbBackedCamelTest;
import org.openmrs.eip.mysql.watcher.Event;
import org.openmrs.eip.mysql.watcher.config.WatcherConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.test.context.TestPropertySource;

/**
 * Base class for tests for routes that wish to be notified of DB events in the backing OpenMRS
 * database, .
 */
@Import(WatcherConfig.class)
@TestPropertySource("classpath:watcher-application-test.properties")
@TestPropertySource(properties = PROP_URI_ERROR_HANDLER + "=" + URI_MOCK_ERROR_HANDLER)
public abstract class BaseWatcherRouteTest extends BaseDbBackedCamelTest {

	protected static final String PROP_RETRY_MAP = "route-retry-count-map";
	
	@EndpointInject(URI_MOCK_ERROR_HANDLER)
	protected MockEndpoint mockErrorHandlerEndpoint;
	
	@Autowired
	protected ConfigurableEnvironment env;
	
	protected Event createEvent(String table, String pkId, String identifier, String operation) {
		Event event = new Event();
		event.setTableName(table);
		event.setPrimaryKeyId(pkId);
		event.setIdentifier(identifier);
		event.setOperation(operation);
		event.setSnapshot(false);
		return event;
	}
	
}
