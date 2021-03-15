package org.openmrs.eip.app;

import static org.apache.camel.impl.engine.DefaultFluentProducerTemplate.on;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.openmrs.eip.app.management.entity.ReceiverSyncStatus;
import org.openmrs.eip.app.management.entity.SiteInfo;
import org.openmrs.eip.component.model.SyncMetadata;
import org.openmrs.eip.component.model.SyncModel;
import org.openmrs.eip.component.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("syncStatusProcessor")
public class SyncStatusProcessor implements Processor {
	
	private static final Logger logger = LoggerFactory.getLogger(SyncStatusProcessor.class);
	
	@Override
	public void process(Exchange exchange) {
		SiteInfo siteInfo = null;
		try {
			SyncMetadata metadata;
			if (exchange.getProperty("is-file", Boolean.class)) {
				metadata = JsonUtils.unmarshal(exchange.getProperty("sync-metadata", String.class), SyncMetadata.class);
			} else {
				metadata = exchange.getIn().getBody(SyncModel.class).getMetadata();
			}
			
			String siteClass = SiteInfo.class.getSimpleName();
			final String siteQuery = "jpa:" + siteClass + " ?query=SELECT s FROM " + siteClass + " s WHERE s.identifier = '"
			        + metadata.getSourceIdentifier() + "'";
			
			List<SiteInfo> sites = on(exchange.getContext()).to(siteQuery).request(List.class);
			
			if (sites.size() == 1) {
				siteInfo = sites.get(0);
			}
			
			if (siteInfo == null) {
				logger.error("No site info found with identifier: " + metadata.getSourceIdentifier()
				        + ", please create one in order to track its last sync date");
				return;
			}
			
			String statusClass = ReceiverSyncStatus.class.getSimpleName();
			final String statusParamsProp = "status-params";
			exchange.setProperty(statusParamsProp, Collections.singletonMap("siteInfoId", siteInfo.getId()));
			final String statusQuery = "jpa:" + statusClass + " ?query=SELECT s FROM " + statusClass
			        + " s WHERE s.siteInfoId = " + siteInfo.getId();
			
			List<ReceiverSyncStatus> statuses = on(exchange.getContext()).to(statusQuery).request(List.class);
			
			ReceiverSyncStatus status;
			final Date date = new Date();
			if (statuses.size() == 0) {
				status = new ReceiverSyncStatus(siteInfo.getId(), date);
				status.setDateCreated(date);
				if (logger.isDebugEnabled()) {
					logger.debug("Inserting initial sync status for " + siteInfo + " as of " + status.getLastSyncDate());
				}
			} else {
				status = statuses.get(0);
				status.setLastSyncDate(date);
				if (logger.isDebugEnabled()) {
					logger.debug("Updating last sync date for " + siteInfo + " to " + status.getLastSyncDate());
				}
			}
			
			status = on(exchange.getContext()).withBody(status).to("jpa:" + statusClass).request(ReceiverSyncStatus.class);
			
			if (logger.isDebugEnabled()) {
				logger.debug("Successfully saved sync status for: " + siteInfo + " -> " + status);
			}
		}
		catch (Exception e) {
			logger.error("Failed to update sync status for: " + siteInfo, e);
		}
		
	}
	
}
