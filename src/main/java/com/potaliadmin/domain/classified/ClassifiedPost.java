package com.potaliadmin.domain.classified;

import com.potaliadmin.domain.post.Post;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * Created by shaktsin on 4/5/15.
 */
@Entity
@Table(name = "classified_post")
@PrimaryKeyJoinColumn(name = "id")
public class ClassifiedPost extends Post {
}
