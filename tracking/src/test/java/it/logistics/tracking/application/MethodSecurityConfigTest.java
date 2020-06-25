package it.logistics.tracking.application;

import org.junit.Test;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;

import static org.junit.Assert.assertNotNull;


public class MethodSecurityConfigTest {

    @Test
    public void testCreateExpressionHandler() {
        // given
        MethodSecurityConfig methodSecurityConfig = new MethodSecurityConfig();

        // when
        MethodSecurityExpressionHandler expressionHandler = methodSecurityConfig.createExpressionHandler();

        // then
        assertNotNull(expressionHandler);
    }

}
