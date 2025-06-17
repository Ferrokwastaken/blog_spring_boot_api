package blog_api.test.controller;

import java.util.List;
import java.util.Optional;

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

import blog_api.test.Post;
import blog_api.test.service.PostService;

@RestController // Marks the class as a RESTful controller
@RequestMapping("/api/posts") // The route for this controller
public class PostController {
  private final PostService postService;

  @Autowired
  public PostController(PostService postService) {
    this.postService = postService;
  }

  // --- API Routes ---

  // GET /api/posts - Get all posts
  @GetMapping
  public ResponseEntity<List<Post>> getAllPosts() {
    List<Post> posts = postService.getAllPosts();
    
    return new ResponseEntity<>(posts, HttpStatus.OK);
  }

  // GET /api/posts/{id} - Get a single post by ID
  @GetMapping("/{id}")
  public ResponseEntity<Post> getPostById(@PathVariable Long id) {
    Optional<Post> post = postService.getPostId(id);

    // Using map to return the post if present, or notFound if empty
    return post.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  // POST /api/posts - Create a new post
  @PostMapping
  public ResponseEntity<Post> createPost(@RequestBody Post post) {
    Post createdPost = postService.createPost(post);
    
    return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
  }

  // PUT /api/posts/{id} - Update an existing post
  @PutMapping("/{id}")
  public ResponseEntity<Post> updatePost(@PathVariable Long id, @RequestBody Post postDetails) {
    Optional<Post> updatedPost = postService.updatePost(id, postDetails);
    
    return updatedPost.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  // DELETE /api/posts/{id} - Delete a post
  @DeleteMapping("/{id}")
  public ResponseEntity<HttpStatus> deletePost(@PathVariable Long id) {
    boolean deleted = postService.deletePost(id);
    if (deleted) {
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }
}
