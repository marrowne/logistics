package it.logistics.tracking.application;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;

@Configuration
@EnableResourceServer
public class OAuth2ResourceServerConfigRemoteTokenService extends ResourceServerConfigurerAdapter {

    @Value("${identity.endpoint.host}")
    private String AUTH_HOST;

    @Value("${identity.endpoint.check-token}")
    private String AUTH_CHECK_TOKEN_URL_TEMPLATE;

    @Value("${identity.endpoint.port}")
    private String AUTH_PORT;

    @Value("${identity.endpoint.protocol}")
    private String AUTH_PROTOCOL;

    @Value("${identity.endpoint.client.id}")
    private String AUTH_CLIENT_ID;

    @Value("${identity.endpoint.client.secret}")
    private String AUTH_CLIENT_SECRET;

    @Override
    public void configure(final HttpSecurity http) throws Exception {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
            .and()
            .authorizeRequests().anyRequest().permitAll();
    }

    @Override
    public void configure(final ResourceServerSecurityConfigurer config) {
        config.tokenServices(tokenServices());
    }

    @Primary
    @Bean
    public RemoteTokenServices tokenServices() {
        final RemoteTokenServices tokenService = new RemoteTokenServices();
        tokenService.setCheckTokenEndpointUrl(
                OAuth2ResourceServerConfigRemoteTokenService.buildURLFor(AUTH_CHECK_TOKEN_URL_TEMPLATE, AUTH_HOST, AUTH_PROTOCOL, AUTH_PORT)
        );
        tokenService.setClientId(AUTH_CLIENT_ID);
        tokenService.setClientSecret(AUTH_CLIENT_SECRET);
        return tokenService;
    }

    private static String buildURLFor(String aTemplate, String aHost, String aProtocol, String aPort) {
        String url =
                aProtocol
                        + "://"
                        + aHost + ":" + aPort
                        + aTemplate;

        return url;
    }

}
