package com.example.ApiGateway.port.config;


import org.keycloak.adapters.springsecurity.KeycloakConfiguration;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;



@EnableWebSecurity
public class SecurityConfig {
    @KeycloakConfiguration
    public static class KeycloakSecurityConfig extends KeycloakWebSecurityConfigurerAdapter{
        private static final String[] applicationRoutes = new String[] {
                "/allProducts", "/product/**", "/products/**"
        };

        private static final String[] applicationRoles = new String[] {
                "admin",
                "user"
        };

        @Override
        protected void configure(final HttpSecurity http) throws Exception {
            super.configure(http);
            http
                    .headers()
                    .httpStrictTransportSecurity().disable()
                    .and()
                    .authorizeRequests()
                    .antMatchers(applicationRoutes).hasAnyRole(applicationRoles)
                    .antMatchers("/").hasAnyRole(applicationRoles)
                    .and()
                    .logout()
                    .logoutSuccessUrl("/")
                    .logoutUrl("/logout")
            ;
        }

        @Override
        @Autowired
        protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
            final var keycloakAuthenticationProvider = keycloakAuthenticationProvider();
            keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(new SimpleAuthorityMapper());
            auth.authenticationProvider(keycloakAuthenticationProvider);
        }

        @Bean
        @Override
        protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
            return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
        }

    }
}
