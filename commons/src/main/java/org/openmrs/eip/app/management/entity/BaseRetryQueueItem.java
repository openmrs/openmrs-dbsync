package org.openmrs.eip.app.management.entity;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.util.Date;

@MappedSuperclass
public abstract class BaseRetryQueueItem extends AbstractEntity {
	
	@Column(length = 1024)
	private String message;
	
	@Column(name = "cause_message", length = 1024)
	private String causeMessage;
	
	@Column(name = "attempt_count", nullable = false)
	private Integer attemptCount = 1;
	
	@Column(name = "date_Changed")
	private Date dateChanged;
	
	/**
	 * Gets the message
	 *
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	
	/**
	 * Sets the message
	 *
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	
	/**
	 * Gets the causeMessage
	 *
	 * @return the causeMessage
	 */
	public String getCauseMessage() {
		return causeMessage;
	}
	
	/**
	 * Sets the causeMessage
	 *
	 * @param causeMessage the causeMessage to set
	 */
	public void setCauseMessage(String causeMessage) {
		this.causeMessage = causeMessage;
	}
	
	/**
	 * Gets the attemptCount
	 *
	 * @return the attemptCount
	 */
	public Integer getAttemptCount() {
		return attemptCount;
	}
	
	/**
	 * Sets the attemptCount
	 *
	 * @param attemptCount the attemptCount to set
	 */
	public void setAttemptCount(Integer attemptCount) {
		this.attemptCount = attemptCount;
	}
	
	/**
	 * Gets the dateChanged
	 *
	 * @return the dateChanged
	 */
	public Date getDateChanged() {
		return dateChanged;
	}
	
	/**
	 * Sets the dateChanged
	 *
	 * @param dateChanged the dateChanged to set
	 */
	public void setDateChanged(Date dateChanged) {
		this.dateChanged = dateChanged;
	}
	
}
