package com.diviso.graeshoppe.config;


import java.security.Principal;
import java.util.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.*;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationManager;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import com.diviso.graeshoppe.security.AuthoritiesConstants;

import io.github.jhipster.config.JHipsterDefaults.Security.Authentication;
import io.github.jhipster.config.JHipsterProperties;

@Configuration
@EnableWebSocketMessageBroker
public class WebsocketConfiguration implements WebSocketMessageBrokerConfigurer {

    public static final String IP_ADDRESS = "IP_ADDRESS";

    private final JHipsterProperties jHipsterProperties;

    public WebsocketConfiguration(JHipsterProperties jHipsterProperties) {
        this.jHipsterProperties = jHipsterProperties;
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic","/queue/notification");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        //String[] allowedOrigins = Optional.ofNullable(jHipsterProperties.getCors().getAllowedOrigins()).map(origins -> origins.toArray(new String[0])).orElse(new String[0]);
        registry.addEndpoint("/order-notifications")
            .setHandshakeHandler(defaultHandshakeHandler())
            .setAllowedOrigins("*","http://localhost:8100/*")
            .withSockJS()
            .setInterceptors(httpSessionHandshakeInterceptor());
    }

    @Bean
    public HandshakeInterceptor httpSessionHandshakeInterceptor() {
        return new HandshakeInterceptor() {

            @Override
            public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
                Principal principal = request.getPrincipal();
               // System.out.println("The user is ********************************** "+principal.getName());
            	if (request instanceof ServletServerHttpRequest) {
                    ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
                    attributes.put(IP_ADDRESS, servletRequest.getRemoteAddress());
                }
                return true;
            }

            @Override
            public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
                Principal principal = request.getPrincipal();
                System.out.println("The user is ********************************** "+principal.getName());
            }
        };
    }

    private DefaultHandshakeHandler defaultHandshakeHandler() {
        return new DefaultHandshakeHandler() {
            @Override
            protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
//              System.out.println("AUthorization header is %%%%%%%%%%%%%%%%%%%%%%%%%%%%5" +request.getURI());
//              
//              OAuth2AccessToken accessToken = new DefaultOAuth2AccessToken("");
//              TokenStore tokenStore=new InMemoryTokenStore();
//              Principal principal = request.getPrincipal();
//              OAuth2Authentication auth=tokenStore.readAuthentication(accessToken);
               // System.out.println("The user is ********************************** "+principal.getName());
            	Principal principal=request.getPrincipal();
                if (principal == null) {
                    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    authorities.add(new SimpleGrantedAuthority(AuthoritiesConstants.ANONYMOUS));
                    principal = new AnonymousAuthenticationToken("WebsocketConfiguration", "anonymous", authorities);
                }
                return principal;
            }
        };
    }
}
