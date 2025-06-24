package blog_api.test.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import blog_api.test.User;

public interface UserRepository extends JpaRepository<User, UUID> {
  // With the JpaRepository the basic CRUD operations such as:
  // save(), findById(), findAll(), deleteById(), and a couple more are already included

  // --- Custom query methods ---
  Optional<User> findByUsername(String username); // Optional because it might not exist in the database

  Optional<User> findByEmail(String email);

  // These next two methods are to check if the username or email
  // already exists.
  boolean existsByUsername(String username);
  boolean existsByEmail(String email);
}
