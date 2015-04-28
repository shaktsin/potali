package com.potaliadmin.domain.classified;

import com.potaliadmin.domain.address.City;
import com.potaliadmin.domain.industry.IndustryRoles;
import com.potaliadmin.domain.post.Post;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by shaktsin on 4/5/15.
 */
@Entity
@Table(name = "classified_post")
@PrimaryKeyJoinColumn(name = "id")
public class ClassifiedPost extends Post {

  @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinTable(name="classified_post_has_secondary_category", joinColumns = {
      @JoinColumn(name = "classified_post_id", nullable = false, updatable = false)} ,
      inverseJoinColumns = {@JoinColumn(name = "secondary_category_id", nullable = false, updatable = false)})
  private Set<SecondaryCategory> secondaryCategorySet;

  @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinTable(name="classified_post_has_city", joinColumns = {
      @JoinColumn(name = "classified_post_id", nullable = false, updatable = false)} ,
      inverseJoinColumns = {@JoinColumn(name = "city_id", nullable = false, updatable = false)})
  private Set<City> citySet;


  public Set<SecondaryCategory> getSecondaryCategorySet() {
    return secondaryCategorySet;
  }

  public void setSecondaryCategorySet(Set<SecondaryCategory> secondaryCategorySet) {
    this.secondaryCategorySet = secondaryCategorySet;
  }

  public Set<City> getCitySet() {
    return citySet;
  }

  public void setCitySet(Set<City> citySet) {
    this.citySet = citySet;
  }
}
