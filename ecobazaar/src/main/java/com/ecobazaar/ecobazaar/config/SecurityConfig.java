/*package com.ecobazaar.ecobazaar.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterConfig(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/register", "/api/auth/login").permitAll()
                .anyRequest().authenticated()
            );

        return http.build();
    }
    
     @Bean
     public InMemoryUserDetailsManager userDetailsService() {
   	UserDetails admin = User.withUsername("admin")
  			.password("{noop}admin123")
   			.roles("ADMIN")
   			.build();
    	
  	return new InMemoryUserDetailsManager(admin);
   }
}
*/
package com.ecobazaar.ecobazaar.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer; // Needed for httpBasic
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity; // Good practice to include
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // Include this annotation
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Disable CSRF for API usage
            .authorizeHttpRequests(auth -> auth
                // Allow registration and login without authentication
                .requestMatchers("/api/auth/register", "/api/auth/login").permitAll()
                
                // Set the specific rule for ADMIN to manage products (as requested previously)
                .requestMatchers("/products", "/products/**").hasRole("ADMIN")
                
                // All other requests require any user to be authenticated
                .anyRequest().authenticated()
            )
            // IMPORTANT: Add this to enable basic authentication (username/password in header)
            .httpBasic(Customizer.withDefaults()); 

        return http.build();
    }
    
    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        // Define the 'admin' user with the ADMIN role
        UserDetails admin = User.withUsername("admin")
            .password("{noop}admin123") // Password is 'admin123'
            .roles("ADMIN")
            .build();
        
        // Define additional users like 'john' if needed (based on your users table)
        // UserDetails customer = User.withUsername("john")
        //     .password("{noop}123456") // Example password
        //     .roles("CUSTOMER")
        //     .build();
        
        return new InMemoryUserDetailsManager(admin); // , customer);
    }
}