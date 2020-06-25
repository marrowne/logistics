package it.logistics.common.domain.model;

import java.util.Date;

public interface DomainEvent {

    public int getEventVersion();

    public Date getOccurDate();

}
