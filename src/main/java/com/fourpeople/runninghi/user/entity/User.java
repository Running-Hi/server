package com.fourpeople.runninghi.user.entity;

import com.fourpeople.runninghi.oauth.entity.Role;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="users")
public class User {
    public User(Role role, String providerId, String provider) {
        this.role = role;
        this.providerId = providerId;
        this.provider = provider;
    }

    @Id
    @Column(name = "id",unique=true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "provider_id", unique = false, nullable = false)
    private String providerId;

    @Column(name = "provider", unique = false, nullable = false)
    private String provider;
}
