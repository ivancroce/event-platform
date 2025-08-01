package ivancroce.event_platform.controllers;

import ivancroce.event_platform.entities.User;
import ivancroce.event_platform.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    // ========================================== /ME ENDPOINTS ==========================================

    @GetMapping("/me")
    public User getOwnProfile(@AuthenticationPrincipal User authenticatedUser) {
        return authenticatedUser;
    }
    
    // ========================================== ADMIN ENDPOINTS ==========================================

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public Page<User> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy
    ) {
        return this.userService.findAll(page, size, sortBy);
    }

  /*  @PostMapping("/register")
    // @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public NewUserRespDTO createUser(@RequestBody @Validated NewUserDTO payload, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            validationResult.getAllErrors().forEach(System.out::println);
            throw new ValidationException(validationResult.getFieldErrors().stream().map(fieldError -> fieldError.getDefaultMessage()).toList());
        } else {
            User newUser = this.userService.saveUser(payload);
            return new NewUserRespDTO(newUser.getId());
        }
    }*/
}
