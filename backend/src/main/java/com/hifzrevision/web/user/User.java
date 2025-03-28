package com.hifzrevision.web.user;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "\"user\"")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;

    // No need to manually write getters, setters, etc. Lombok handles it with @Data
}
