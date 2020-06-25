package it.logistics.hr.domain.model.employee;

import it.logistics.common.domain.model.DomainEvent;

import java.util.Date;

public class EmployeeRemoved implements DomainEvent {

    private int eventVersion;
    private Date occurDate;
    private long employeeId;
    private Job position;

    public EmployeeRemoved(long employeeId, Job position) {
        super();
        this.eventVersion = 1;
        this.occurDate = new Date();
        this.employeeId = employeeId;
        this.position = position;
    }

    @Override
    public int getEventVersion() {
        return this.eventVersion;
    }

    @Override
    public Date getOccurDate() {
        return this.occurDate;
    }

    public long getEmployeeId() {
        return this.employeeId;
    }

    public Job getPosition() {
        return this.position;
    }

}
