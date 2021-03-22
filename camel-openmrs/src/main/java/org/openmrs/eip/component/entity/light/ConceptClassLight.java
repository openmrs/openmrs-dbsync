package org.openmrs.eip.component.entity.light;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "concept_class")
@AttributeOverride(name = "id", column = @Column(name = "concept_class_id"))
public class ConceptClassLight extends RetireableLightEntity {

    @NotNull
    @Column(name = "name")
    private String name;
}
