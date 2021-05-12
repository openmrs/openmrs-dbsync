package org.openmrs.eip.mysql.watcher;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.component.debezium.DebeziumConstants;
import org.apache.kafka.connect.data.Struct;
import org.openmrs.eip.EIPException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * This processor creates an {@link Event} object from the debezium payload and sets it as a
 * property on the exchange, it also invokes the {@link IdentifierSettingProcessor}
 */
@Component(WatcherConstants.DBZM_MSG_PROCESSOR)
public class DebeziumMessageProcessor implements Processor {
	
	private static final Logger logger = LoggerFactory.getLogger(DebeziumMessageProcessor.class);
	
	@Override
	public void process(Exchange exchange) {
		Message message = exchange.getMessage();
		String op = message.getHeader(DebeziumConstants.HEADER_OPERATION, String.class);
		if (!"c".equals(op) && !"u".equals(op) && !"d".equals(op)) {
			throw new EIPException("Don't know how to handle DB event with operation: " + op);
		}
		
		Event event = new Event();
		Struct primaryKeyStruct = message.getHeader(DebeziumConstants.HEADER_KEY, Struct.class);
		//TODO Take care of situation where a table has a composite PK because fields length will be > 1
		event.setPrimaryKeyId(primaryKeyStruct.get(primaryKeyStruct.schema().fields().get(0)).toString());
		Map<String, Object> sourceMetadata = message.getHeader(DebeziumConstants.HEADER_SOURCE_METADATA, Map.class);
		event.setTableName(sourceMetadata.get(WatcherConstants.DEBEZIUM_FIELD_TABLE).toString());
		event.setOperation(op);
		
		Object snapshot = sourceMetadata.getOrDefault(WatcherConstants.DEBEZIUM_FIELD_SNAPSHOT, "");
		event.setSnapshot(!"false".equalsIgnoreCase(snapshot.toString()));
		
		exchange.setProperty(WatcherConstants.PROP_EVENT, event);
		
		logger.info("Received from debezium: " + event + ", Source Metadata: " + sourceMetadata);
		
		if (message.getBody() != null) {
			Map<String, Object> currentState = new HashMap();
			Struct bodyStruct = message.getBody(Struct.class);
			bodyStruct.schema().fields().forEach(field -> currentState.put(field.name(), bodyStruct.get(field)));
			event.setCurrentState(currentState);
		}
		
		Struct beforeStruct = message.getHeader(DebeziumConstants.HEADER_BEFORE, Struct.class);
		if (beforeStruct != null) {
			Map<String, Object> beforeState = new HashMap();
			beforeStruct.schema().fields().forEach(field -> beforeState.put(field.name(), beforeStruct.get(field)));
			event.setPreviousState(beforeState);
		}
	}
	
}
