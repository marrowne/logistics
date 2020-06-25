package it.logistics.hr.domain.model.employee;

import it.logistics.common.domain.model.DomainEvent;

import java.util.Date;

public class EmployeeAdded implements DomainEvent {

    private int eventVersion;
    private FullName name;
    private Date occurDate;
    private long employeeId;
    private Job position;


    public EmployeeAdded(long employeeId, FullName name, Job position) {
        super();
        this.eventVersion = 1;
        this.name = name;
        this.occurDate = new Date();
        this.employeeId = employeeId;
        this.position = position;
    }

    @Override
    public int getEventVersion() {
        return this.eventVersion;
    }

    public FullName getName() {
        return this.name;
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
