package tech.nilanjan.cloud.google.spring.SpannerWithSpringApplication.service;

import tech.nilanjan.cloud.google.spring.SpannerWithSpringApplication.shared.dto.UserDto;

import java.util.Optional;

public interface UserService {
    UserDto createUser(UserDto userDetails);
    Optional<UserDto> getUserByUserId(String userId);
}
