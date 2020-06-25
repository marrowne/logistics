package it.logistics.hr.domain.model.employee;

import it.logistics.common.domain.model.AbstractId;

import javax.persistence.Embeddable;

@Embeddable
public class OauthId extends AbstractId {

    public OauthId(String id) {
        super(id);
    }

    public OauthId() { }

}
