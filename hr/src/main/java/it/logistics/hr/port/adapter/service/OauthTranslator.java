package it.logistics.hr.port.adapter.service;

import it.logistics.common.domain.model.AbstractId;
import it.logistics.hr.domain.model.employee.OauthId;

public class OauthTranslator extends AbstractId {

    public OauthTranslator() {
        super();
    }

    public OauthId toOauthIdFromRepresentation(String oauthIdRepresentation) {
        return new OauthId(oauthIdRepresentation);
    }
}
