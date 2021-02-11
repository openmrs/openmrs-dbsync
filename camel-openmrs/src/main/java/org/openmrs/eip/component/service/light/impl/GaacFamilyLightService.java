package org.openmrs.eip.component.service.light.impl;

import org.openmrs.eip.component.entity.light.GaacFamilyLight;
import org.openmrs.eip.component.repository.OpenmrsRepository;
import org.openmrs.eip.component.service.light.AbstractLightService;
import org.springframework.stereotype.Service;

@Service
public class GaacFamilyLightService extends AbstractLightService<GaacFamilyLight> {
	
    public GaacFamilyLightService(final OpenmrsRepository<GaacFamilyLight> repository) {
        super(repository);
    }

    @Override
    protected GaacFamilyLight createPlaceholderEntity(final String uuid) {
    	GaacFamilyLight gaac = new GaacFamilyLight();
        gaac.setFamilyIdentifier(DEFAULT_STRING);
    	gaac.setDateCreated(DEFAULT_DATE);
        gaac.setCreator(DEFAULT_USER_ID);
        gaac.setStartDate(DEFAULT_DATE);
        return gaac;
    }
}