package blog_api.test.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import blog_api.test.Post;
import blog_api.test.repository.PostRepository;
import jakarta.transaction.Transactional;

@Service // Makes the class a Spring Service component
public class PostService {
  private final PostRepository postRepository;

  // It allows Spring to automatically wire the required beans (dependencies) into your classes, 
  // eliminating the need for manual configuration.
  @Autowired 
  public PostService(PostRepository postRepository) {
    this.postRepository = postRepository;
  }

  // --- Business Logic Methods ---

  @Transactional // In case of error, the database rolls back
  public Post createPost(Post post) {
    post.setCreatedAt(LocalDateTime.now());
    post.setUpdatedAt(LocalDateTime.now());
    return postRepository.save(post);
  }

  // Retrieve all posts.
  public List<Post> getAllPosts() {
    return postRepository.findAll();
  }

  // Retrieve a post by its ID.
  public Optional<Post> getPostId(Long id) {
    return postRepository.findById(id);
  }

  // Update an existing post.
  @Transactional
  public Optional<Post> updatePost(Long id, Post postDetails) {
    return postRepository.findById(id).map(existingPost -> {
      existingPost.setTitle(postDetails.getTitle());
      existingPost.setContent(postDetails.getContent());
      existingPost.setAuthor(postDetails.getAuthor());
      existingPost.setUpdatedAt(LocalDateTime.now());

      return postRepository.save(existingPost);
    }); // 'map' will return an Optional containing the updated post, or empty if not found
  }

  // Delete a post by its ID
  @Transactional
  public boolean deletePost(Long id) {
    if (postRepository.existsById(id)) {
      postRepository.deleteById(id);
      return true;
    }
    return false;
  }
}
