package com.potaliadmin.domain.post;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Shakti Singh on 12/16/14.
 */
@Entity
@Table(name = "post_blob")
public class PostBlob implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", unique = true, nullable = false)
  private Long id;

  @Column(name = "content", nullable = true, length = 16777215)
  private String content;

  @Column(name = "post_id", nullable = false)
  private Long postId;


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public Long getPostId() {
    return postId;
  }

  public void setPostId(Long postId) {
    this.postId = postId;
  }
}
