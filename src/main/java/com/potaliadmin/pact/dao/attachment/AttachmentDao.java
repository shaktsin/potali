package com.potaliadmin.pact.dao.attachment;

import com.potaliadmin.constants.attachment.EnumAttachmentType;
import com.potaliadmin.constants.image.EnumImageSize;
import com.potaliadmin.domain.attachment.Attachment;
import com.potaliadmin.pact.framework.BaseDao;

import java.util.List;

/**
 * Created by shakti on 28/1/15.
 */
public interface AttachmentDao extends BaseDao {

  Attachment createAttachment(EnumAttachmentType enumAttachmentType, String path, EnumImageSize enumImageSize, Long postId);

  List<Attachment> findByPostId(Long postId);
}
