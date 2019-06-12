package org.openmrs.sync.core.entity.light;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "visit")
@AttributeOverride(name = "id", column = @Column(name = "visit_id"))
public class VisitLight extends LightEntity {

    @NotNull
    @ManyToOne
    @JoinColumn(name = "patient_id")
    private PatientLight patient;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "visit_type_id")
    private VisitTypeLight visitType;

    @NotNull
    @Column(name = "date_started")
    private LocalDateTime dateStarted;
}
