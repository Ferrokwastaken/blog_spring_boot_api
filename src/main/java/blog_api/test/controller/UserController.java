package blog_api.test.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import blog_api.test.User;
import blog_api.test.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
  private final UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  // POST /api/users/register - Register a new user
  @PostMapping("/register")
  public ResponseEntity<User> registerUser(@RequestBody User user) {
    Optional<User> registeredUser = userService.registerUser(user);
    if (registeredUser.isPresent()) {
      return new ResponseEntity<>(registeredUser.get(), HttpStatus.CREATED);
    } else {
      return new ResponseEntity<>(HttpStatus.CONFLICT);
    }
  }

  // POST /api/users/login - Authenticate a user (Login)
  @PostMapping("/login")
  public ResponseEntity<User> loginUser(@RequestBody User loginRequest) {
    Optional<User> authenticatedUser = userService.authenticatedUser(loginRequest.getUsername(),
        loginRequest.getPassword());

    if (authenticatedUser.isPresent()) {
      return new ResponseEntity<>(authenticatedUser.get(), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
  }

  // GET /api/users - Get all users
  @GetMapping
  public ResponseEntity<List<User>> getAllUsers() {
    List<User> users = userService.getAllUsers();
    return new ResponseEntity<>(users, HttpStatus.OK);
  }

  // GET /api/users/{id} - Get a single user by ID
  @GetMapping("/{id}")
  public ResponseEntity<User> getUserById(@PathVariable UUID id) {
    Optional<User> user = userService.getUserById(id);
    return user.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  // PUT /api/users/{id} - Update an existing user
  @PutMapping("/{id}")
  public ResponseEntity<User> updateUser(@PathVariable UUID id, @RequestBody User userDetails) {
    Optional<User> updatedUser = userService.updateUser(id, userDetails);
    return updatedUser.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<HttpStatus> deleteUser(@PathVariable UUID id) {
    boolean deleted = userService.deleteUser(id);
    if (deleted) {
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }
}
