package ivancroce.event_platform.services;

import ivancroce.event_platform.entities.User;
import ivancroce.event_platform.exceptions.BadRequestException;
import ivancroce.event_platform.exceptions.NotFoundException;
import ivancroce.event_platform.payloads.UserDTO;
import ivancroce.event_platform.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private PasswordEncoder bcrypt;

    public User saveUser(UserDTO payload) {
        this.userRepository.findByUsername(payload.username()).ifPresent(employee -> {
            throw new BadRequestException("The username '" + payload.username() + "' is already in use.");
        });

        User newUser = new User(payload.username(), bcrypt.encode(payload.password()), payload.fullName());

        User savedUser = this.userRepository.save(newUser);

        log.info("The User with ID '" + newUser.getId() + "' was created.");

        return newUser;
    }

    public Page<User> findAll(int page, int size, String sortBy) {
        if (size > 50) size = 50;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.userRepository.findAll(pageable);
    }

    public User findById(UUID userId) {
        return this.userRepository.findById(userId).orElseThrow(() -> new NotFoundException(userId));
    }

    public User findByUsername(String username) {
        return this.userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("The employee with username '" + username + "' was not found."));
    }
}
