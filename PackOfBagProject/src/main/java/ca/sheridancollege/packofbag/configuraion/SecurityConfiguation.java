package ca.sheridancollege.packofbag.configuraion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import ca.sheridancollege.packofbag.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfiguation {
    
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    
    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (request, response, authentication) -> {
            if (authentication != null && authentication.isAuthenticated()) {
                response.sendRedirect("/home"); // Redirect to home page if already authenticated
            }
        };
    }

    @Autowired
    public void configuredGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .authorizeRequests()
                .requestMatchers("/register").permitAll()
                .requestMatchers("/").permitAll()
                .requestMatchers("/**").permitAll()
                .requestMatchers("/index").permitAll()
                .requestMatchers("/donations").permitAll()
                .requestMatchers("/volunteer-register").permitAll()
                .requestMatchers("/whoWeAre").permitAll()
                .requestMatchers("/history").permitAll()
                .requestMatchers("/thanksto").permitAll()
                .requestMatchers("/volunteerSuccess").permitAll()
                .requestMatchers("/editVolunteer").permitAll()
                .requestMatchers("/event-register").permitAll()
                .requestMatchers("/eventView").permitAll()
                .requestMatchers("/editEvents").permitAll()
                .requestMatchers("/edit-events").permitAll()
                .requestMatchers("/eventEdit").permitAll()
                .requestMatchers("/userEventRegistration").permitAll()
                .requestMatchers("/photoGallery").permitAll()
                .requestMatchers("/photoGallery/*").permitAll()
                .requestMatchers("/contact").permitAll()
                .requestMatchers("/filterImages").permitAll()
                .requestMatchers("/filterImages/*").permitAll()
                .requestMatchers("/userEventRegistration/*").permitAll()
                .requestMatchers("/imageUpload/*").permitAll()
                .requestMatchers("/imagePostUpload").permitAll()
                .requestMatchers("/imagePostUpload/*").permitAll()
                .requestMatchers("/imageUpload/*").permitAll()
                .requestMatchers("/userEventView").permitAll()
                .requestMatchers("/images/**").permitAll()
                .requestMatchers("/css/**").permitAll()
                .requestMatchers("/scripts/**").permitAll()
                .requestMatchers("eventRegistrationSuccess").permitAll()
                .requestMatchers("/delete-image/**").authenticated()
                .anyRequest().authenticated()
            .and()
            .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .successHandler(authenticationSuccessHandler())
                .permitAll()
            .and()
            .logout()
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login?logout").permitAll();
        
      
        return http.build();
    }
    
}
