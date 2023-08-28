package pe.isil.luna_2618.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import pe.isil.luna_2618.security.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //Habilitacion del inicio de sesion
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()

                //definir los permisos como rutas especificas
                .authorizeRequests()

                .antMatchers("/admin/**") //admin/cursos, admin/usuarios, admin/cursos/editar/1
                .hasAnyRole("ADMIN")

                .antMatchers("/cursos/**","/usuario/**")
                .authenticated()

                .anyRequest()//Para el resto de rutas son publicas, acceso total
                .permitAll()

                .and()
                .rememberMe().key("rememberMeKey").tokenValiditySeconds(3600)
                .userDetailsService(userDetailsServiceImpl)

                .and()

                .exceptionHandling().accessDeniedPage("/403")

                .and()

                .logout(logout -> logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET")).logoutSuccessUrl("/"));
    }
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
