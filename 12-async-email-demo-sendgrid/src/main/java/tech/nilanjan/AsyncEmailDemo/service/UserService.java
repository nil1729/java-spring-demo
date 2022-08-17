package tech.nilanjan.AsyncEmailDemo.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.nilanjan.AsyncEmailDemo.shared.dto.UserDto;
import tech.nilanjan.AsyncEmailDemo.io.entity.UserEntity;
import tech.nilanjan.AsyncEmailDemo.repo.UserRepository;
import tech.nilanjan.AsyncEmailDemo.shared.utils.RandomTokenUtil;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RandomTokenUtil randomTokenUtil;

    @Autowired
    public UserService(
            UserRepository userRepository,
            RandomTokenUtil randomTokenUtil
    ) {
        this.userRepository = userRepository;
        this.randomTokenUtil = randomTokenUtil;
    }

    public UserDto createUser(UserDto userDetails) {
        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(userDetails, userEntity);

        userEntity.setEmailVerificationStatus(false);
        userEntity.setEmailVerificationToken(randomTokenUtil.generateEmailVerificationToken());

        UserEntity savedUser = userRepository.save(userEntity);

        UserDto returnValue = new UserDto();
        BeanUtils.copyProperties(savedUser, returnValue);
        return returnValue;
    }
}
