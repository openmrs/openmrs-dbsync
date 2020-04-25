package org.openmrs.sync.app.management.entity;

import org.openmrs.sync.component.common.BaseStatefulEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "sync_attempt")
public class SyncAttempt extends BaseStatefulEntity {

    public static final long serialVersionUID = 1;

    @NotBlank
    @Column(name = "sync_record_uuid", length = 38, updatable = false)
    private String syncRecordUuid;

    @ManyToOne(optional = false)
    @JoinColumn(name = "openmrs_db_destination", updatable = false)
    private OpenmrsDatabase destination;

    /**
     * Gets the syncRecordUuid
     *
     * @return the syncRecordUuid
     */
    public String getSyncRecordUuid() {
        return syncRecordUuid;
    }

    /**
     * Sets the syncRecordUuid
     *
     * @param syncRecordUuid the syncRecordUuid to set
     */
    public void setSyncRecordUuid(String syncRecordUuid) {
        this.syncRecordUuid = syncRecordUuid;
    }

    /**
     * Gets the destination
     *
     * @return the destination
     */
    public OpenmrsDatabase getDestination() {
        return destination;
    }

    /**
     * Sets the destination
     *
     * @param destination the destination to set
     */
    public void setDestination(OpenmrsDatabase destination) {
        this.destination = destination;
    }

    @Override
    public String toString() {
        return "SyncAttempt {destination=" + destination + ", status=" + getStatus() + ", syncRecordUuid=" + syncRecordUuid + "}";
    }

}