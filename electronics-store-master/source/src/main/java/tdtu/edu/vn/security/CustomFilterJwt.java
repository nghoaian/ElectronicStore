package tdtu.edu.vn.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import tdtu.edu.vn.model.User.Role;
import tdtu.edu.vn.util.JwtUtilsHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class CustomFilterJwt extends OncePerRequestFilter{
    JwtUtilsHelper jwtUtilsHelper;


    @Override
    @SuppressWarnings("NullableProblems")
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        String token = getTokenFromHeader(request);

        System.out.println("Token: " + token);
        if (token != null && jwtUtilsHelper.verifyToken(token)) {
            System.out.println("Valid: " + token);

            String phoneNumber = jwtUtilsHelper.getPhoneNumberFromToken(token);
            String email = jwtUtilsHelper.getEmailFromToken(token);
            String role = jwtUtilsHelper.getRoleFromToken(token);
            String id = jwtUtilsHelper.getIdFromToken(token);

            if (id == null || id.isEmpty()){
                id = "default_id";
            }

            System.out.println("Phone number: " + phoneNumber);
            System.out.println("Email: " + email);
            System.out.println("Role: " + role);
            System.out.println("Id: " + id);

            List<GrantedAuthority> authorities = new ArrayList<>();
            if (role != null && !role.isEmpty()) {
                authorities.add(new SimpleGrantedAuthority(role));
            } else {
                System.out.println("Role is empty");
                authorities.add(new SimpleGrantedAuthority(Role.USER.toString()));
            }

            UserPrincipal userPrincipal = new UserPrincipal(phoneNumber, email, id);
            System.out.println("User principal: " + userPrincipal);

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userPrincipal, null, authorities);
            SecurityContext context = SecurityContextHolder.getContext();
            context.setAuthentication(authenticationToken);
        }

        filterChain.doFilter(request, response);
    }

    private String getTokenFromHeader(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }

    @Setter
    @Getter
    @ToString
    @AllArgsConstructor
    public static class UserPrincipal{
        private String phoneNumber;
        private String email;
        private String id;
    }
}
