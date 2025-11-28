package hr.ja.st.security;

import hr.ja.st.user.domain.Roles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.saml2.provider.service.registration.RelyingPartyRegistrationRepository;
import org.springframework.security.saml2.provider.service.web.DefaultRelyingPartyRegistrationResolver;
import org.springframework.security.saml2.provider.service.web.Saml2MetadataFilter;
import org.springframework.security.saml2.provider.service.web.Saml2WebSsoAuthenticationRequestFilter;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
public class SecurityConfig {


    @Bean
    SecurityFilterChain securitySAML(HttpSecurity http,
                                     RelyingPartyRegistrationRepository rpr) throws Exception {

        // 1) Metadata filter
        var resolver = new DefaultRelyingPartyRegistrationResolver(rpr);
        var metadataFilter = new Saml2MetadataFilter(
              resolver,
              new org.springframework.security.saml2.provider.service.metadata.OpenSamlMetadataResolver()
        );

        http.addFilterBefore(
              metadataFilter,
              Saml2WebSsoAuthenticationRequestFilter.class
        );

        // 2) Security
        http.authorizeHttpRequests(auth -> auth
                    .requestMatchers(
                          "/", "/health",
                          "/saml2/service-provider-metadata/**",
                          "/saml2/authenticate/**",     // permit i za init endpoint
                          "/login", "/error"            // da ne upadne u petlju
                    ).permitAll()
                    .anyRequest().authenticated()
              )
              .saml2Login(Customizer.withDefaults())
              .logout(l -> l.logoutSuccessUrl("/"));

        return http.build();
    }


    //@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
              .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/login", "/static/**", "/about", "/__dev/**").permitAll()
                    .requestMatchers("/users/**").hasAuthority(Roles.ADMIN)
                    .anyRequest().authenticated()
              )
              .formLogin(form -> form
                    .loginPage("/login").permitAll()
                    .defaultSuccessUrl("/", true)
                    .failureUrl("/login?error")
              )
              .logout(logout -> logout
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/login?logout")
                    .deleteCookies("JSESSIONID")
                    .invalidateHttpSession(true)
                    .permitAll()
              )
              .csrf(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
