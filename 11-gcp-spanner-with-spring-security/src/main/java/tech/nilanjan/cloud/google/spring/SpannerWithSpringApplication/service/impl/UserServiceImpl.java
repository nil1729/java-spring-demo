package tech.nilanjan.cloud.google.spring.SpannerWithSpringApplication.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tech.nilanjan.cloud.google.spring.SpannerWithSpringApplication.io.entity.UserEntity;
import tech.nilanjan.cloud.google.spring.SpannerWithSpringApplication.repo.UserRepository;
import tech.nilanjan.cloud.google.spring.SpannerWithSpringApplication.service.UserService;
import tech.nilanjan.cloud.google.spring.SpannerWithSpringApplication.shared.dto.UserDto;
import tech.nilanjan.cloud.google.spring.SpannerWithSpringApplication.shared.utils.RandomId;

import java.time.LocalDate;
import java.util.Optional;


@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RandomId randomId;

    @Autowired
    public UserServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            RandomId randomId
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.randomId = randomId;
    }

    @Override
    public UserDto createUser(UserDto userDetails) {
        Optional<UserEntity> userEntityWithSameEmail =
                userRepository.getUserEntityByEmailAddress(userDetails.getEmailAddress());
        if(userEntityWithSameEmail.isPresent()) {
            throw new RuntimeException(String.format("Email address [%s] already registered", userDetails.getEmailAddress()));
        }

        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(userDetails, userEntity);

        userEntity.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        userEntity.setUserId(randomId.generateUserId(30));
        userEntity.setRegisteredAt(LocalDate.now());

        UserEntity createdUserEntity = userRepository.save(userEntity);

        UserDto returnValue = new UserDto();
        BeanUtils.copyProperties(createdUserEntity, returnValue);

        return returnValue;
    }

    @Override
    public Optional<UserDto> getUserByUserId(String userId) {
        Optional<UserEntity> userEntity = userRepository.getUserEntityByUserId(userId);

        if(userEntity.isPresent()) {
            UserDto returnValue = new UserDto();
            BeanUtils.copyProperties(userEntity.get(), returnValue);

            return Optional.of(returnValue);
        }

        return Optional.empty();
    }
}
