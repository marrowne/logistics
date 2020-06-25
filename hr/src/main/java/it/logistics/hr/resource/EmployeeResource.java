package it.logistics.hr.resource;

import it.logistics.hr.application.EmployeeService;
import it.logistics.hr.domain.model.employee.*;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.springframework.http.HttpStatus.OK;

@RequestMapping("/hr")
@RestController
public class EmployeeResource {

    private final EmployeeService employeeService;

    private final OauthService oauthService;

    @Autowired
    public EmployeeResource(EmployeeService employeeService, OauthService oauthService) {
        this.employeeService = employeeService;
        this.oauthService = oauthService;
    }

    @PreAuthorize("hasAnyAuthority('ADMINISTRATOR')")
    @PostMapping(path = "/employee")
    public ResponseEntity<Employee> addEmployee(@RequestBody Employee employee) {
        OauthId oauthId = null;
        try {
            oauthId = oauthService.oauthIdFor(employee);
        } catch (IllegalStateException e) { }
        if (oauthId == null) {
            throw new IllegalArgumentException("Error on requesting new OAuth ID.");
        }
        FullName fullName = new FullName(employee.getFullName().getFirstName(), employee.getFullName().getLastName());
        Mobile mobile = new Mobile(employee.getMobile().getNumber());
        Employee newEmployee = new Employee(Long.parseLong(oauthId.getId()),
                fullName,
                mobile,
                employee.getPosition());
        employeeService.addEmployee(newEmployee);
        return new ResponseEntity<>(newEmployee, org.springframework.http.HttpStatus.OK);
    }


    @PreAuthorize("hasAnyAuthority('ADMINISTRATOR')")
    @RequestMapping(method = RequestMethod.GET, path = "/employee")
    @ResponseBody
    public ResponseEntity<Employee> getEmployees(
            @RequestParam(name = "sort", required = false, defaultValue = "[\"id\",\"ASC\"]") String sort,
            @RequestParam(name = "range", required = false, defaultValue = "[0,9]") String range,
            @RequestParam(name = "filter", required = false, defaultValue = "{}") String filter) {
        String[] sortArr = sort.replace ("[", "").replace ("]", "").replaceAll("\"", "").split(",");
        String[] rangeArr = range.replace ("[", "").replace ("]", "").replaceAll("\"", "").split(",");
        final int start = Integer.parseInt(rangeArr[0]);
        final int end = Integer.parseInt(rangeArr[1]);
        final int size = end - start + 1;
        final int page = start / size;
        PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.valueOf(sortArr[1]), sortArr[0]);
        Page<Employee> result = employeeService.allEmployees(pageRequest);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Range", "employee" + " " + start + " - " + end + " / " + result.getTotalElements());
        headers.add("Access-Control-Expose-Headers", "Content-Range");
        return new ResponseEntity(result.getContent(), headers, OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMINISTRATOR')")
    @GetMapping(path = "/employee/{id}")
    public Employee getEmployee(@PathVariable Long id, HttpServletResponse response) {
        Employee employee = employeeService.findEmployee(id);
        if(employee == null) {
            response.setStatus( HttpStatus.SC_NOT_FOUND );
        }
        return employee;
    }

    @PreAuthorize("hasAnyAuthority('COURIER', 'SORTING', 'ADMINISTRATOR')")
    @GetMapping(path = "/employee/me")
    public Employee getMyName(HttpServletRequest request, HttpServletResponse response) {
        final Long id = Long.parseLong(request.getUserPrincipal().getName());
        Employee employee = employeeService.findEmployee(id);
        if(employee == null) {
            response.setStatus( HttpStatus.SC_NOT_FOUND );
        }
        return employee;
    }

    @PreAuthorize("hasAnyAuthority('ADMINISTRATOR')")
    @DeleteMapping("/employee/{id}")
    public void deleteEmployee(@PathVariable Long id) {
        try {
            oauthService.removeOauthId(id);
        } catch (IllegalStateException e) { }
        employeeService.deleteEmployee(id);
    }

    private String buildURLFor(String aTemplate, String aHost, String aProtocol, String aPort) {
        String url =
                aProtocol
                        + "://"
                        + aHost + ":" + aPort
                        + aTemplate;

        return url;
    }

}
