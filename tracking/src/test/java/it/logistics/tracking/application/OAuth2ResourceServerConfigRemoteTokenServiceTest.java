package it.logistics.tracking.application;

import it.logistics.tracking.port.adapter.service.HttpEmployeeAdapter;
import org.junit.Test;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

public class OAuth2ResourceServerConfigRemoteTokenServiceTest {

    private String HR_URL_TEMPLATE="/hr/employee/{employeeId}";
    private String HR_HOST="localhost";
    private String HR_PROTOCOL="http";
    private String HR_PORT="8081";

    @Test
    public void testTokenServices() {
        // given
        OAuth2ResourceServerConfigRemoteTokenService oAuth2ResourceServerConfigRemoteTokenService = new OAuth2ResourceServerConfigRemoteTokenService();

        // when
        RemoteTokenServices remoteTokenServices = oAuth2ResourceServerConfigRemoteTokenService.tokenServices();

        // then
        assertNotNull(remoteTokenServices);
    }

    @Test
    public void testBuildURLFor() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // given
        Method method = OAuth2ResourceServerConfigRemoteTokenService.class.getDeclaredMethod("buildURLFor", String.class, String.class, String.class, String.class);
        method.setAccessible(true);

        // when
        Object url = (String) method.invoke(null, HR_URL_TEMPLATE, HR_HOST, HR_PROTOCOL, HR_PORT);

        // then
        assertEquals("http://localhost:8081/hr/employee/{employeeId}", url);
    }

}
