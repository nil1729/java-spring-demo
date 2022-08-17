package tech.nilanjan.AsyncEmailDemo.ui.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.nilanjan.AsyncEmailDemo.service.EmailService;
import tech.nilanjan.AsyncEmailDemo.service.UserService;
import tech.nilanjan.AsyncEmailDemo.shared.dto.UserDto;
import tech.nilanjan.AsyncEmailDemo.ui.model.request.UserRequestDetails;
import tech.nilanjan.AsyncEmailDemo.ui.model.response.UserRest;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final EmailService emailService;

    public UserController(UserService userService, EmailService emailService) {
        this.userService = userService;
        this.emailService = emailService;
    }

    @PostMapping(
            path = "/create",
            produces = {MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<UserRest> createUser(
            @RequestBody UserRequestDetails userRequestDetails
    ) {
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userRequestDetails, userDto);

        UserDto createdUser = userService.createUser(userDto);

        UserRest returnValue = new UserRest();
        BeanUtils.copyProperties(createdUser, returnValue);

        try {
            emailService.sendVerificationEmail(createdUser);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return ResponseEntity.ok().body(returnValue);
    }
}
