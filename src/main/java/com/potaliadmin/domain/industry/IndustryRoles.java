package com.potaliadmin.domain.industry;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Shakti Singh on 12/16/14.
 */
@Entity
@Table(name = "industry_roles")
public class IndustryRoles implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", unique = true, nullable = false)
  private Long id;

  @Column(name = "name", nullable = false, length = 45)
  private String name;

  @Column(name = "industry_id", nullable = false)
  private Long industryId;



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

  public Long getIndustryId() {
    return industryId;
  }

  public void setIndustryId(Long industryId) {
    this.industryId = industryId;
  }
}
