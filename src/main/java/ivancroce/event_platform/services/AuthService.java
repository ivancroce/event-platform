package ivancroce.event_platform.services;

import ivancroce.event_platform.entities.User;
import ivancroce.event_platform.exceptions.UnauthorizedException;
import ivancroce.event_platform.payloads.LoginDTO;
import ivancroce.event_platform.tools.JWTTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UserService userService;

    @Autowired
    private JWTTools jwtTools;

    @Autowired
    private PasswordEncoder bcrypt;

    public String authenticateUserAndGenerateToken(LoginDTO payload) {

        User user = this.userService.findByUsername(payload.username());

        if (bcrypt.matches(payload.password(), user.getPassword())) {
            return jwtTools.createToken(user);
        } else {
            throw new UnauthorizedException("Invalid authorization. Check your username and password.");
        }
    }
}
