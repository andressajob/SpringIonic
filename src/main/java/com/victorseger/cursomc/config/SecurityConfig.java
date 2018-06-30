package com.victorseger.cursomc.config;

import com.victorseger.cursomc.security.JWTAuthenticationFilter;
import com.victorseger.cursomc.security.JWTAuthorizationFilter;
import com.victorseger.cursomc.security.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)//anotação que permite a personalização das autorizações (pela anotação @PreAuthorize("hasAnyRole('ADMIN')")
public class SecurityConfig extends WebSecurityConfigurerAdapter{

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JWTUtil jwtUtil;

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

    public static final String[] PUBLIC_MATCHERS_POST = {
            "/clientes/**",
            "/auth/forgot/**"
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
                .antMatchers(HttpMethod.POST,PUBLIC_MATCHERS_POST).permitAll() // permite operações post nos caminhos do vetor
                .anyRequest().authenticated();
        httpSecurity.addFilter(new JWTAuthenticationFilter(authenticationManager(),jwtUtil));
        httpSecurity.addFilter(new JWTAuthorizationFilter(authenticationManager(), jwtUtil, userDetailsService));
        httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues()); // dando acesso básico a multiplas fontes
        return source;
    }

    //bean para configuração do codificador de password
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
         return new BCryptPasswordEncoder();
    }


    //CONFIGURE PARA AUTENTICAÇÃO - método para identificar o userDetailsService sendo usado e qual é o algoritmo de codificação que está sendo usado
    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
    }

}
