package it.logistics.hr.resource;

import it.logistics.hr.infrastructure.persistence.UserStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.sql.SQLException;

@RestController
public class UserResource {

    @Qualifier("dataSource")
    @Autowired
    private DataSource dataSource;

    @PostMapping(path = "/user")
    public Long addUser(@RequestParam String role) throws SQLException {
        return this.userStore().addUser(role);
    }

    @DeleteMapping(path = "/user/{userId}")
    public void removeUser(@PathVariable Long userId) throws SQLException {
        this.userStore().removeUser(userId);
    }

    @Bean
    public UserStore userStore() {
        return new UserStore(dataSource);
    }

}