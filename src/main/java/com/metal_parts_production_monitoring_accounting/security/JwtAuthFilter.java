package com.metal_parts_production_monitoring_accounting.security;


import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Value("${testing.app.secret}")
    private String secret;

    private final UserDetailsService userDetailsService;

    public JwtAuthFilter(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }


    @Override
    public void doFilterInternal(HttpServletRequest request,
                                 HttpServletResponse response,
                                 FilterChain filterChain)throws ServletException, IOException{
        // Извлечение jwt из заголовка Authorization("Bearer <token>")
        String header = request.getHeader("Authorization");
        String username = null;
        String token = null;
        if (header != null && header.startsWith("Bearer ")) {
            token = header.substring(7);
            try{
                //парсим токен для получения имени пользователя (subject)
                username = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
            }catch (Exception e){}
        }

        // если пользователь еще не аутентифицирован и имя из токена не получено
        if (username !=null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                // просрочен токен или нет
                if (Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getExpiration().after(new Date())) {
                    // создаем аутентификационный объект и сохраняем в контекст Spring Sequrity
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }catch(UsernameNotFoundException e){}

        }

        // продолжение обработки запроса(вызывается следующий контроллер или фильтр)
        filterChain.doFilter(request, response);
    }
}
