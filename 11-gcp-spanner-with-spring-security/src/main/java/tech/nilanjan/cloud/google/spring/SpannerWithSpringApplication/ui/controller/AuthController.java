package tech.nilanjan.cloud.google.spring.SpannerWithSpringApplication.ui.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.nilanjan.cloud.google.spring.SpannerWithSpringApplication.security.jwt.JwtUtil;
import tech.nilanjan.cloud.google.spring.SpannerWithSpringApplication.service.UserService;
import tech.nilanjan.cloud.google.spring.SpannerWithSpringApplication.shared.dto.UserDto;
import tech.nilanjan.cloud.google.spring.SpannerWithSpringApplication.ui.model.request.UserRequestDetails;
import tech.nilanjan.cloud.google.spring.SpannerWithSpringApplication.ui.model.response.LoginRest;
import tech.nilanjan.cloud.google.spring.SpannerWithSpringApplication.ui.model.response.UserSignUpRest;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthController(
            UserService userService,
            AuthenticationManager authenticationManager,
            JwtUtil jwtUtil
    ) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping(
            path = "/sign-up",
            produces = {MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<UserSignUpRest> signUp(@RequestBody UserRequestDetails userRequestDetails) {
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userRequestDetails, userDto);

        UserDto createdUser = userService.createUser(userDto);

        UserSignUpRest returnValue = new UserSignUpRest();
        BeanUtils.copyProperties(createdUser, returnValue);

        return ResponseEntity.ok().body(returnValue);
    }

    @PostMapping(
            path = "/sign-in",
            produces = {MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<LoginRest> signIn(
            @RequestBody UserRequestDetails userRequestDetails,
            HttpServletRequest request
    ) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userRequestDetails.getEmailAddress(),
                userRequestDetails.getPassword()
        );

        Authentication authResult = authenticationManager.authenticate(authentication);

        String accessToken = jwtUtil.generateAccessToken(authResult, request);
        LoginRest returnValue = new LoginRest(accessToken);

        return ResponseEntity.ok().body(returnValue);
    }
}
