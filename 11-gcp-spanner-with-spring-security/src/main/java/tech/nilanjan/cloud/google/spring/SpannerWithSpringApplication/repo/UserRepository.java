package tech.nilanjan.cloud.google.spring.SpannerWithSpringApplication.repo;

import com.google.cloud.spanner.Key;
import com.google.cloud.spring.data.spanner.repository.SpannerRepository;
import com.google.cloud.spring.data.spanner.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import tech.nilanjan.cloud.google.spring.SpannerWithSpringApplication.io.entity.UserEntity;

import java.util.Optional;

@RepositoryRestResource(collectionResourceRel = "users", path = "users")
public interface UserRepository extends SpannerRepository<UserEntity, Key> {
    @Query("SELECT * FROM users WHERE email_address=@email_address LIMIT 1")
    Optional<UserEntity> getUserEntityByEmailAddress(@Param("email_address") String emailAddress);

    @Query("SELECT * FROM users WHERE user_id=@user_id LIMIT 1")
    Optional<UserEntity> getUserEntityByUserId(@Param("user_id") String userId);
}
