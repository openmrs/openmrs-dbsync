package org.openmrs.eip.component.entity.light;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;


@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "orders")
@AttributeOverride(name = "id", column = @Column(name = "order_id"))
public class OrderLight extends VoidableLightEntity {

    @NotNull
    @ManyToOne
    @JoinColumn(name = "order_type_id")
    private OrderTypeLight orderType;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "concept_id")
    private ConceptLight concept;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "orderer")
    private ProviderLight orderer;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "encounter_id")
    private EncounterLight encounter;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "patient_id")
    private PatientLight patient;

    @NotNull
    @Column(name = "order_number")
    private String orderNumber;

    @NotNull
    @Column(name = "order_action")
    private String action;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "care_setting")
    private CareSettingLight careSetting;

    @NotNull
    @Column(name = "date_activated")
    private LocalDateTime dateActivated;

    @NotNull
    private String urgency;

}
