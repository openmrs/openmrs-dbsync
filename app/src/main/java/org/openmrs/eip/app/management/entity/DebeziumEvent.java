package org.openmrs.eip.app.management.entity;

import java.util.Arrays;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.openmrs.eip.component.entity.Event;

@Entity
@Table(name = "debezium_event_queue")
public class DebeziumEvent extends AbstractEntity{
	private static final long serialVersionUID = -1884382844867650350L;
	
    /*@Embedded
    @AttributeOverride(name = "identifier", column = @Column(updatable = false))
    @AttributeOverride(name = "primaryKeyId", column = @Column(name = "primary_key_id", nullable = false, updatable = false))
    @AttributeOverride(name = "tableName", column = @Column(name = "table_name", nullable = false, updatable = false, length = 100))
    @AttributeOverride(name = "operation", column = @Column(nullable = false, updatable = false, length = 1))
    @AttributeOverride(name = "snapshot", column = @Column(nullable = false, updatable = false))
    private Event event;*/
	
	
	@AttributeOverride(name = "date_created", column = @Column(name = "date_created", insertable = false,  nullable = true))
	  
    private String identifier;
    private String primaryKeyId;

    private String tableName;

    private String operation;

    private Boolean snapshot = Boolean.FALSE;

    private String beforeState;

    private String afterState;

    private Long eventTimeStamp;

    @Transient
    private Event event;
    
	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getPrimaryKeyId() {
		return primaryKeyId;
	}

	public void setPrimaryKeyId(String primaryKeyId) {
		this.primaryKeyId = primaryKeyId;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public Boolean getSnapshot() {
		return snapshot;
	}

	public void setSnapshot(Boolean snapshot) {
		this.snapshot = snapshot;
	}

	public String getBeforeState() {
		return beforeState;
	}

	public void setBeforeState(String beforeState) {
		this.beforeState = beforeState;
	}

	public String getAfterState() {
		return afterState;
	}

	public void setAfterState(String afterState) {
		this.afterState = afterState;
	}

	public Long getEventTimeStamp() {
		return eventTimeStamp;
	}

	public void setEventTimeStamp(Long eventTimeStamp) {
		this.eventTimeStamp = eventTimeStamp;
	}
	
	public Event getEvent() {
		if (this.event == null) {
			synchronized (afterState) {
				this.event = new Event();
				
				this.event.setPrimaryKeyId(primaryKeyId);
			    this.event.setTableName(tableName);
			    this.event.setOperation(operation);
			    this.event.setSnapshot(snapshot);
			}
		}
		
		return event;
	}
	
	public boolean isDMLOperation() {
		return Arrays.asList("c", "u", "d").contains(this.operation);
	}
       
}
