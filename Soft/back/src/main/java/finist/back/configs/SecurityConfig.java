package finist.back.configs;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

   private final UserDetailsService userDetailsService;

   @Autowired
    public SecurityConfig(@Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST,"/users", "/session").permitAll() // все могут зарегаться или залогиниться
                .antMatchers(HttpMethod.GET, "/free", "/users/{userId}").permitAll() // Разрешить доступ без аутентификации
                .antMatchers(HttpMethod.GET,  "/api/competitions/**").permitAll()
                .antMatchers(HttpMethod.POST, "api/competitions/**").permitAll()
                .antMatchers(HttpMethod.PATCH, "api/competitions/**").permitAll()


                .antMatchers(HttpMethod.PATCH,"/users/{userId}").hasAuthority("users:update") // Разрешить доступ к /free без аутентификации
                .antMatchers(HttpMethod.DELETE,"/users/{userId}").hasAuthority("users:delete") // Разрешить доступ к /free без аутентификации
                .antMatchers(HttpMethod.GET,"/users").hasAuthority("users:readAll") // Разрешить доступ к /free без аутентификации
                .antMatchers(HttpMethod.GET, "/users/{userId}/arrows").hasAuthority("users:read:detailed")
                .anyRequest().authenticated() // Все остальные запросы требуют аутентификации
                .and()
                .httpBasic(); // Использовать Basic Auth
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    protected PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    protected DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        return daoAuthenticationProvider;

    }
}
