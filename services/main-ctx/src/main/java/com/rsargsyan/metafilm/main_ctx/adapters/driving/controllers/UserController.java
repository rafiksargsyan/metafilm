package com.rsargsyan.metafilm.main_ctx.adapters.driving.controllers;

import com.rsargsyan.metafilm.main_ctx.core.app.UserService;
import com.rsargsyan.metafilm.main_ctx.core.app.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
  private final UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/signup-external")
  public ResponseEntity<UserDTO> signupExternal() {
    UserContext userContext = UserContextHolder.get();
    UserDTO user = userService.signUpWithExternal(userContext.getExternalId(), userContext.getFullName());
    return ResponseEntity.ok(user);
  }
}
