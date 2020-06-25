package it.logistics.hr.domain.model.employee;

public interface OauthService {

    public OauthId oauthIdFor(Employee employee);

    public void removeOauthId(Long employeeId);

}
