package blog_api.test.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import blog_api.test.Post;

// The JpaRepository provides the basic CRUD methods.
public interface PostRepository extends JpaRepository<Post, Long> {
  
}
