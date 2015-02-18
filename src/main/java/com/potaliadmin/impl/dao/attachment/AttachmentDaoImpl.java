package com.potaliadmin.impl.dao.attachment;

import com.potaliadmin.constants.attachment.EnumAttachmentType;
import com.potaliadmin.constants.image.EnumImageSize;
import com.potaliadmin.domain.attachment.Attachment;
import com.potaliadmin.impl.framework.BaseDaoImpl;
import com.potaliadmin.pact.dao.attachment.AttachmentDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by shakti on 28/1/15.
 */
@Repository
public class AttachmentDaoImpl extends BaseDaoImpl implements AttachmentDao {

  @Override
  public Attachment createAttachment(EnumAttachmentType enumAttachmentType, String path, EnumImageSize enumImageSize,Long postId) {
    Attachment attachment = new Attachment();
    attachment.setAttachmentType(enumAttachmentType.getId());
    attachment.setSize(enumImageSize.getId());
    attachment.setPath(path);
    attachment.setPostId(postId);
    return (Attachment)save(attachment);
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<Attachment> findByPostId(Long postId) {
    return (List<Attachment>)findByNamedQueryAndNamedParam("findByPostId", new String[]{"postId"}, new Object[]{postId});

  }
}
