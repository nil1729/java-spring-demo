package tech.nilanjan.AsyncEmailDemo.io.entity;

import javax.persistence.*;

@Entity(
        name = "Users"
)
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "email_unique_constraint",
                        columnNames = {"email"}
                )
        }
)
public class UserEntity {
    @Id
    @SequenceGenerator(
            name = "user_id_generator",
            sequenceName = "user_id_generator",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.AUTO,
            generator = "user_id_generator"
    )
    @Column(name = "user_id")
    private Long userId;

    @Column(
            name = "name",
            nullable = false,
            length = 100
    )
    private String name;

    @Column(
            name = "email",
            nullable = false,
            length = 100,
            updatable = false
    )
    private String email;

    @Column(
            name = "email_verification_token",
            columnDefinition = "TEXT"
    )
    private String emailVerificationToken;

    @Column(
            name = "email_verification_status",
            nullable = false
    )
    private Boolean emailVerificationStatus;

    public Long getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmailVerificationToken() {
        return emailVerificationToken;
    }

    public void setEmailVerificationToken(String emailVerificationToken) {
        this.emailVerificationToken = emailVerificationToken;
    }

    public Boolean getEmailVerificationStatus() {
        return emailVerificationStatus;
    }

    public void setEmailVerificationStatus(Boolean emailVerificationStatus) {
        this.emailVerificationStatus = emailVerificationStatus;
    }
}
