package tech.nilanjan.cloud.google.spring.SpannerWithSpringApplication.ui.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.nilanjan.cloud.google.spring.SpannerWithSpringApplication.shared.dto.UserDto;
import tech.nilanjan.cloud.google.spring.SpannerWithSpringApplication.ui.model.response.UserProfileRest;

@RestController
@RequestMapping("/profile")
public class UserProfileController {

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<UserProfileRest> getMyProfile(Authentication authResult) {
        UserDto userDetails = (UserDto) authResult.getCredentials();

        UserProfileRest returnValue = new UserProfileRest();
        BeanUtils.copyProperties(userDetails, returnValue);

        return ResponseEntity.ok().body(returnValue);
    }
}
