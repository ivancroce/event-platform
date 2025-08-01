package ivancroce.event_platform.security;

import ivancroce.event_platform.entities.User;
import ivancroce.event_platform.services.UserService;
import ivancroce.event_platform.tools.JWTTools;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.UnavailableException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class JWTCheckerFilter extends OncePerRequestFilter {
    @Autowired
    private JWTTools jwtTools;

    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // ================================================================ AUTHENTICATION ================================================================

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer "))
            throw new UnavailableException("Please enter the token in the Authorization Header in the correct format.");
        String token = authHeader.replace("Bearer ", "");
        jwtTools.verifyToken(token);

        // ================================================================ AUTHORIZATIOON ================================================================

        String userId = jwtTools.extractIdFromToken(token);
        User currentUser = this.userService.findById(UUID.fromString(userId));
        Authentication authentication = new UsernamePasswordAuthenticationToken(currentUser, null, currentUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    // We disable this filter for certain endpoints like /auth/login and /auth/register
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return new AntPathMatcher().match("/auth/**", request.getServletPath());
        // return new AntPathMatcher().match("/users/register", request.getServletPath()); // TODO: need to be changed later
    }
}
