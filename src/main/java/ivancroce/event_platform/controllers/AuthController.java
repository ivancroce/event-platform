package ivancroce.event_platform.controllers;

import ivancroce.event_platform.entities.User;
import ivancroce.event_platform.exceptions.ValidationException;
import ivancroce.event_platform.payloads.NewUserDTO;
import ivancroce.event_platform.payloads.NewUserRespDTO;
import ivancroce.event_platform.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
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
    }
}
