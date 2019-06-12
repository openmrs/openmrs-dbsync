package org.openmrs.sync.core.service.light.impl;

import org.openmrs.sync.core.entity.light.FormLight;
import org.openmrs.sync.core.repository.OpenMrsRepository;
import org.openmrs.sync.core.service.light.AbstractLightService;
import org.openmrs.sync.core.service.attribute.AttributeUuid;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FormLightService extends AbstractLightService<FormLight> {

    public FormLightService(final OpenMrsRepository<FormLight> repository) {
        super(repository);
    }

    @Override
    protected FormLight getFakeEntity(final String uuid,
                                      final List<AttributeUuid> attributeUuids) {
        FormLight form = new FormLight();
        form.setUuid(uuid);
        form.setName(DEFAULT_STRING);
        form.setVersion(DEFAULT_STRING);
        form.setCreator(DEFAULT_USER_ID);
        form.setDateCreated(DEFAULT_DATE);
        return form;
    }
}
