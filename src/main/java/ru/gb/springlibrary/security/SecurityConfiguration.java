package ru.gb.springlibrary.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Конфигурация безопасности приложения
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		return httpSecurity.authorizeHttpRequests(conf -> conf
						.requestMatchers("/ui/reader/**", "/ui/book/**").authenticated()
						.requestMatchers("/ui/issue/**").hasAuthority("admin")
						.requestMatchers("/ui/issue/reader/**").hasAuthority("reader")
						.anyRequest().permitAll())
				.csrf(AbstractHttpConfigurer::disable)
				.oauth2ResourceServer(conf -> conf
						.jwt(Customizer.withDefaults()))
				.oauth2Login(Customizer.withDefaults())
				.build();
	}

	@Bean
	JwtAuthenticationConverter jwtAuthenticationConverter() {
		JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
		converter.setPrincipalClaimName("preferred_username");
		converter.setJwtGrantedAuthoritiesConverter(jwt -> {
			List<String> roles = jwt.getClaim("spring_roles");
			return roles.stream()
					.map(SimpleGrantedAuthority::new)
					.collect(Collectors.toList());
		});
		return converter;
	}

	@Bean
	public OAuth2UserService<OidcUserRequest, OidcUser> oAuth2UserService() {
		OidcUserService oidcUserService = new OidcUserService();
		return userRequest -> {
			OidcUser oidcUser = oidcUserService.loadUser(userRequest);
			List<String> roles = oidcUser.getClaim("spring_roles");
			List<SimpleGrantedAuthority> authorities = roles.stream()
					.map(SimpleGrantedAuthority::new)
					.toList();
			return new DefaultOidcUser(authorities, oidcUser.getIdToken(), oidcUser.getUserInfo());
		};
	}
}
