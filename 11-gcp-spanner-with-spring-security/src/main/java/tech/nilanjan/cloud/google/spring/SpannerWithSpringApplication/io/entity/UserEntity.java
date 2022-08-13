package tech.nilanjan.cloud.google.spring.SpannerWithSpringApplication.io.entity;

import com.google.cloud.spring.data.spanner.core.mapping.Column;
import com.google.cloud.spring.data.spanner.core.mapping.PrimaryKey;
import com.google.cloud.spring.data.spanner.core.mapping.Table;
import com.google.spanner.v1.TypeCode;

import java.time.LocalDate;
import java.util.Date;

@Table(name = "users")
public class UserEntity {
    @PrimaryKey
    @Column(
            name = "user_id",
            spannerType = TypeCode.STRING,
            nullable = false,
            spannerTypeMaxLength = 30L
    )
    private String userId;

    @Column(
            name = "first_name",
            spannerType = TypeCode.STRING,
            nullable = false,
            spannerTypeMaxLength = 50L
    )
    private String firstName;

    @Column(
            name = "last_name",
            spannerType = TypeCode.STRING,
            nullable = false,
            spannerTypeMaxLength = 50L
    )
    private String lastName;

    @Column(
            name = "email_address",
            nullable = false,
            spannerTypeMaxLength = 100L,
            spannerType = TypeCode.STRING
    )
    private String emailAddress;

    @Column(
            name = "password",
            nullable = false,
            spannerType = TypeCode.STRING
    )
    private String password;

    @Column(name = "registered_at", nullable = false, spannerType = TypeCode.DATE)
    private LocalDate registeredAt;

    @Column(name = "updated_at", spannerCommitTimestamp = true)
    private Date updatedAt;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getRegisteredAt() {
        return registeredAt;
    }

    public void setRegisteredAt(LocalDate registeredAt) {
        this.registeredAt = registeredAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public UserEntity(
            String userId,
            String firstName,
            String lastName,
            String emailAddress,
            String password,
            LocalDate registeredAt
    ) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.password = password;
        this.registeredAt = registeredAt;
    }

    public UserEntity() {
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "userId='" + userId + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", password='" + password + '\'' +
                ", registeredAt=" + registeredAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
