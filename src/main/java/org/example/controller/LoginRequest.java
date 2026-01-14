package org.example.controller;

import lombok.Data;
import org.example.model.Role;

@Data
public class LoginRequest {
    private String email;
    private String passwordhash;
    private Role role;

}
