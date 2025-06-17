package blog_api.test;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity // Indicates the class as a JPA entity akin to a model in Laravel
public class Post {
  // Specifies that this is the primary key of the entity
  // It also configures it to auto increment
  // It uses the wrapper class 'Long'
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false) // Indicates that the contents of the filed cannot be null
  private String title;

  @Column(nullable = false, columnDefinition = "TEXT") // Allows to specify the type of data of that column
  private String content;

  private String author; // Author can be null

  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  // --- Constructors ---
  // JPA requires a basic constructor without arguments
  protected Post() {
  }

  // Overloaded constructor for easier creation
  public Post(String title, String content, String author) {
    this.title = title;
    this.content = content;
    this.author = author;
    this.createdAt = LocalDateTime.now();
    this.updatedAt = LocalDateTime.now();
  }

  // --- Getters and Setters ---
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  // For easier logging and debugging
  @Override
  public String toString() {
    return "Post{" +
        "id=" + id +
        ", title='" + title + '\'' +
        ", content='" + content + '\'' +
        ", author='" + author + '\'' +
        ", createdAt=" + createdAt +
        ", updatedAt=" + updatedAt +
        '}';
  }

}
