package com.diviso.graeshoppe.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

import com.diviso.graeshoppe.security.AuthoritiesConstants;

@Configuration
public class WebsocketSecurityConfiguration extends AbstractSecurityWebSocketMessageBrokerConfigurer {

	@Override
	protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
		//messages.simpDestMatchers("/user/queue/**").authenticated();
		messages.simpSubscribeDestMatchers("/user/queue/**").authenticated();
		  //messages.nullDestMatcher().authenticated()
		  messages.simpDestMatchers("/topic/tracker")
		 .hasAuthority(AuthoritiesConstants.USER) // matches any destination tha* starts with /topic/ 
		 // (i.e. cannot send messages directly to /topic/) (i.e. cannot subscribe to /topic/messages/* to get messages sent to 
		 // /topic/messages-user<id>)
		 .simpDestMatchers("/topic/**").authenticated(); // message types other than MESSAGE and SUBSCRIBE
		  //.simpTypeMatchers(SimpMessageType.MESSAGE,
		// SimpMessageType.SUBSCRIBE).denyAll() ;// catch al .anyMessage().denyAll();
		 
	}

	/**
	 * Disables CSRF for Websockets.
	 */
	@Override
	protected boolean sameOriginDisabled() {
		return true;
	}
}
