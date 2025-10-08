package com.proyect.System_userAndLogin.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proyect.System_userAndLogin.Dto.LoginRequest;
import com.proyect.System_userAndLogin.ServicesImpl.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")

public class AuthController {

  @Autowired
  private final AuthService authService;

  public AuthController (AuthService authService) {
	  this.authService = authService;
	  
  }
  @PostMapping("/login")
  public ResponseEntity<?> login(@Valid @RequestBody LoginRequest req) {
    return ResponseEntity.ok(authService.login(req.getLogin(), req.getPassword()));
  }
}