package it.logistics.hr.domain.model.employee;

import it.logistics.common.domain.model.DomainEventPublisher;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Table(name="EMPLOYEE")
@Where(clause="EMPLOYEE_STATUS <> 'LEFT'")
@SQLDelete(sql="UPDATE EMPLOYEE SET " +
        "EMPLOYEE_STATUS='LEFT', " +
        "FIRST_NAME=NULL, " +
        "LAST_NAME=NULL, " +
        "MOBILE=NULL " +
        "WHERE ID=?")
public class Employee {

    public Employee() { }

    public Employee(FullName name, Mobile mobile, Job position) {
        this.fullName = name;
        this.mobile = mobile;
        this.position = position;

        DomainEventPublisher
                .getInstance()
                .publish(new EmployeeAdded(id,
                        name,
                        position));
    }

    public Employee(long id, FullName name, Mobile mobile, Job position) {
        this.id = id;
        this.fullName = name;
        this.mobile = mobile;
        this.position = position;

        DomainEventPublisher
                .getInstance()
                .publish(new EmployeeAdded(id,
                        name,
                        position));
    }

    @Id
    private long id;

    @Embedded
    private FullName fullName;

    @AttributeOverride(name="number",
            column = @Column(name = "MOBILE"))
    private Mobile mobile;

    @Column
    @Enumerated(EnumType.STRING)
    private EmployeeStatus employeeStatus;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Job position;

    @PrePersist
    private void addEmployee() {
        this.employeeStatus = EmployeeStatus.EMPLOYEED;
    }

    @PreRemove
    private void removeEmployee() {
        DomainEventPublisher
                .getInstance()
                .publish(new EmployeeRemoved(id, position));
    }

    public long getId() {
        return id;
    }

    public FullName getFullName() {
        return fullName;
    }

    public Mobile getMobile() {
        return mobile;
    }

    public EmployeeStatus getEmployeeStatus() {
        return employeeStatus;
    }

    public Job getPosition() {
        return position;
    }

}

