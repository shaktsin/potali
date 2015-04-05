package com.potaliadmin.domain.classified;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by shaktsin on 4/5/15.
 */
@Entity
@Table(name = "secondary_category")
public class SecondaryCategory implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", unique = true, nullable = false)
  private Long id;

  @Column(name = "name", nullable = false, length = 45)
  private String name;

  @Column(name = "primary_category_id", nullable = false)
  private Long primaryCategoryId;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Long getPrimaryCategoryId() {
    return primaryCategoryId;
  }

  public void setPrimaryCategoryId(Long primaryCategoryId) {
    this.primaryCategoryId = primaryCategoryId;
  }
}
