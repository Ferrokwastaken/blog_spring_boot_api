package blog_api.test.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import blog_api.test.User;
import blog_api.test.repository.UserRepository;
import jakarta.transaction.Transactional;

@Service
public class UserService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  
  // Spring will now automatically use the instances of UserRepository and PasswordEncoder
  @Autowired
  public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  // Basic registration method. Checks if the username or email is already in use.
  // Then hashes the introduced password.
  @Transactional
  public Optional<User> registerUser (User user) {
    if (userRepository.existsByUsername(user.getUsername())) {
      System.out.println("Username already exists: " + user.getUsername());
      return Optional.empty();
    }
    if (userRepository.existsByEmail(user.getEmail())) {
      System.out.println("Email already exists: " + user.getEmail());
      return Optional.empty();
    }

    String hashedPassword = passwordEncoder.encode(user.getPassword());
    user.setPassword(hashedPassword);

    User savedUser = userRepository.save(user);
    return Optional.of(savedUser);
  }

  // Basic login method. Checks if the user exists, and compares the introduced
  // password with the hashed one in the database. It returns an error if the
  // the user's not found or the password doesn't match.
  public Optional<User> authenticatedUser(String username, String rawPassword) {
    Optional<User> userOptional = userRepository.findByUsername(username);

    if (userOptional.isPresent()) {
      User user = userOptional.get();
      if (passwordEncoder.matches(rawPassword, user.getPassword())) {
        return Optional.of(user);
      }
    }
    return Optional.empty();
  }

  // Get all users
  public List<User> getAllUsers() {
    return userRepository.findAll();
  }

  // Get a user by ID
  public Optional<User> getUserById(UUID id) {
    return userRepository.findById(id);
  }

  // Update an existing user. It also only updates the password (and it's corresponding hash) if a new one
  // has been provided
  @Transactional
  public Optional<User> updateUser(UUID id, User userDetails) {
    return userRepository.findById(id).map(existingUser -> {
      existingUser.setUsername(userDetails.getUsername());
      existingUser.setEmail(userDetails.getEmail());

      if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
        existingUser.setPassword(passwordEncoder.encode(userDetails.getPassword()));
      }

      return userRepository.save(existingUser);
    });
  }

  // Deletes a user
  @Transactional
  public boolean deleteUser(UUID id) {
    if (userRepository.existsById(id)) {
      userRepository.deleteById(id);
      return true;
    }
    return false;
  }
}
