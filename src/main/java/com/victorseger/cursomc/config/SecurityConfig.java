package com.victorseger.cursomc.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{

    @Autowired
    private Environment env;


    //variável para organizar os matchers - acesso liberado sem autenticação
    public static final String[] PUBLIC_MATCHERS = {
            "/h2-console/**"
    };

    public static final String[] PUBLIC_MATCHERS_GET = {
            "/produtos/**",
            "/categorias/**"
    };


    //configura as permissões de acesso aos recursos da aplicação com limitação de operações também
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception{

        //se eu estiver com o profile "test" ativo, será liberado o acesso ao h2
        if(Arrays.asList(env.getActiveProfiles()).contains("test")) {
            httpSecurity.headers().frameOptions().disable();
        }

        httpSecurity.cors().and().csrf().disable(); // liberando acesso a configurações CORS
        httpSecurity.authorizeRequests().antMatchers(PUBLIC_MATCHERS).permitAll()// permite acesso não-autenticado a todas as pastas dos "antMatchers"
                .antMatchers(HttpMethod.GET,PUBLIC_MATCHERS_GET).permitAll() // permite apenas operações get nos caminhos determinados
                .anyRequest().authenticated();
        httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues()); // dando acesso básico a multiplas fontes
        return source;
    }

}
