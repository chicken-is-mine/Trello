package com.sparta.trello.domain.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@Builder
@ToString(exclude = "password")
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_USER")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column
    private String profile;

    public User(String email, String password, String username, String profile) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.profile = profile;
    }

    public User(Long id, String email, String username, String profile) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.profile = profile;
    }


    public void updateUser(String username, String profile) {
        this.username = username;
        this.profile = profile;
    }
}
