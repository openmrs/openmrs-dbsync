package org.openmrs.eip.web.receiver;

import java.util.Map;

import org.openmrs.eip.app.management.entity.ReceiverRetryQueueItem;
import org.openmrs.eip.web.BaseRestController;
import org.openmrs.eip.web.RestConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(RestConstants.API_PATH + "/dbsync/receiver/error")
public class ReceiverErrorController extends BaseRestController {
	
	private static final Logger log = LoggerFactory.getLogger(ReceiverErrorController.class);
	
	@Override
	public Class<?> getClazz() {
		return ReceiverRetryQueueItem.class;
	}
	
	@GetMapping
	public Map<String, Object> getAll() {
		if (log.isDebugEnabled()) {
			log.debug("Fetching receiver retry items");
		}
		
		return doGetAll();
	}
	
	@GetMapping("/{id}")
	public Object get(@PathVariable("id") Integer id) {
		if (log.isDebugEnabled()) {
			log.debug("Fetching receiver retry item with id: " + id);
		}
		
		return doGet(id);
	}
	
	@DeleteMapping("/{id}")
	public void delete(@PathVariable("id") Integer id) {
		if (log.isDebugEnabled()) {
			log.debug("Deleting receiver retry item with id: " + id);
		}
		
		doDelete(id);
	}
	
}
