package com.potaliadmin.domain.classified;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by shaktsin on 4/5/15.
 */
@Entity
@Table(name = "primary_category")
public class PrimaryCategory implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", unique = true, nullable = false)
  private Long id;

  @Column(name = "name", nullable = false, length = 45)
  private String name;

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
}
