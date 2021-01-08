package org.openmrs.eip.component.model;

import java.time.LocalDateTime;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class GaacFamilyModel extends BaseChangeableDataModel {
	private String familyIdentifier;
	protected LocalDateTime startDate;
	protected LocalDateTime endDate;
	private String focalPatientUuid;
	private Integer affinityType;
	private String locationUuid;
	private Integer crumbled;
	private String reasonCrumbled;
	protected LocalDateTime dateCrumbled;
	
	public String getFamilyIdentifier() {
		return familyIdentifier;
	}
	public void setFamilyIdentifier(String familyIdentifier) {
		this.familyIdentifier = familyIdentifier;
	}
	public LocalDateTime getStartDate() {
		return startDate;
	}
	public void setStartDate(LocalDateTime startDate) {
		this.startDate = startDate;
	}
	public LocalDateTime getEndDate() {
		return endDate;
	}
	public void setEndDate(LocalDateTime endDate) {
		this.endDate = endDate;
	}
	public String getFocalPatientUuid() {
		return focalPatientUuid;
	}
	public void setFocalPatientUuid(String focalPatientUuid) {
		this.focalPatientUuid = focalPatientUuid;
	}
	public Integer getAffinityType() {
		return affinityType;
	}
	public void setAffinityType(Integer affinityType) {
		this.affinityType = affinityType;
	}
	public String getLocationUuid() {
		return locationUuid;
	}
	public void setLocationUuid(String locationUuid) {
		this.locationUuid = locationUuid;
	}
	public Integer getCrumbled() {
		return crumbled;
	}
	public void setCrumbled(Integer crumbled) {
		this.crumbled = crumbled;
	}
	public String getReasonCrumbled() {
		return reasonCrumbled;
	}
	public void setReasonCrumbled(String reasonCrumbled) {
		this.reasonCrumbled = reasonCrumbled;
	}
	public LocalDateTime getDateCrumbled() {
		return dateCrumbled;
	}
	public void setDateCrumbled(LocalDateTime dateCrumbled) {
		this.dateCrumbled = dateCrumbled;
	}	
}