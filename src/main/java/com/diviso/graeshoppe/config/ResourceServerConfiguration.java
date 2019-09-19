 /*
 * Copyright 2002-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.diviso.graeshoppe.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher;
import org.springframework.web.filter.CorsFilter;

import com.diviso.graeshoppe.security.AuthoritiesConstants;

@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    private final CorsFilter corsFilter;

    public ResourceServerConfiguration(CorsFilter corsFilter) {
        this.corsFilter = corsFilter;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .addFilterBefore(corsFilter, CsrfFilter.class)
            .requestMatcher(requestHeaderRequestMatcher())
            .authorizeRequests()
            .antMatchers("/api/**").authenticated()
            .antMatchers("/management/**").hasAuthority(AuthoritiesConstants.ADMIN);
    }

    @Bean("authorizationHeaderRequestMatcher")
    RequestHeaderRequestMatcher requestHeaderRequestMatcher() {
        return new RequestHeaderRequestMatcher("Authorization");
    }
}