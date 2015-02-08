package com.potaliadmin.util.image;

import com.potaliadmin.constants.DefaultConstants;
import com.potaliadmin.constants.image.EnumImageSize;
import org.apache.commons.lang.StringUtils;
import org.imgscalr.Scalr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

/**
 * Created by Shakti Singh on 1/3/15.
 */
public class ImageProcessUtil {

  private static Logger logger = LoggerFactory.getLogger(ImageProcessUtil.class);

  public static final String finalFormat = "jpg";
  public static final String fileBreaker = ".";

  public static String reSize(String filePath, String fileName, EnumImageSize enumImageSize, String reNameFile) {
    String fullFileName = filePath + File.separator + fileName;
    String reSizedFileName = null;
    try {
      BufferedImage originalImage = ImageIO.read(new File(fullFileName));

      //BufferedImage resizeImageJpg = resizeImageWithHint(originalImage, type, enumImageSize);
      BufferedImage resizeImageJpg = Scalr.resize(originalImage,
          Scalr.Method.ULTRA_QUALITY, Scalr.Mode.AUTOMATIC,
          enumImageSize.getWidth(), enumImageSize.getHeight(), Scalr.OP_ANTIALIAS);


          //getScaledInstance(originalImage, enumImageSize.getWidth(),
          //    enumImageSize.getHeight(), RenderingHints.VALUE_INTERPOLATION_BILINEAR, true);

      if (StringUtils.isBlank(reNameFile)) {
        reNameFile = fileName;
      }

      reSizedFileName = reNameFile + DefaultConstants.NAME_SEPARATOR+enumImageSize.getPrefix()+
          fileBreaker +enumImageSize.getFormat();

      String reSizedCanonicalName  = filePath + File.separator + reSizedFileName;

      ImageIO.write(resizeImageJpg, enumImageSize.getFormat(), new File(reSizedCanonicalName));
      //writeJPG(resizeImageJpg, new FileOutputStream(reSizedCanonicalName), 0.85f, enumImageSize.getFormat());


    } catch (Exception e) {
      logger.error("Error while resizing file " + fullFileName + " into size "+enumImageSize.getPrefix());
    }
    return reSizedFileName;
  }

  private static BufferedImage resizeImageWithHint(BufferedImage originalImage, int type, EnumImageSize enumImageSize){

    BufferedImage resizedImage = new BufferedImage(enumImageSize.getWidth(), enumImageSize.getHeight(), type);
    Graphics2D g = resizedImage.createGraphics();
    g.drawImage(originalImage, 0, 0, enumImageSize.getWidth(), enumImageSize.getHeight(), null);
    g.dispose();
    g.setComposite(AlphaComposite.Src);

    g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    g.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);

    return resizedImage;
  }

  public static BufferedImage getScaledInstance( BufferedImage img, int targetWidth,int targetHeight, Object hint,boolean higherQuality)
  {
    int type = (img.getTransparency() == Transparency.OPAQUE)? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
    BufferedImage ret = (BufferedImage) img;
    int w, h;
    if (higherQuality)
    {
      // Use multi-step technique: start with original size, then
      // scale down in multiple passes with drawImage()
      // until the target size is reached
      w = img.getWidth();
      h = img.getHeight();
    }
    else
    {
      // Use one-step technique: scale directly from original
      // size to target size with a single drawImage() call
      w = targetWidth;
      h = targetHeight;
    }

    do
    {
      if (higherQuality && w > targetWidth)
      {
        w /= 2;
        if (w < targetWidth)
        {
          w = targetWidth;
        }
      }

      if (higherQuality && h > targetHeight)
      {
        h /= 2;
        if (h < targetHeight)
        {
          h = targetHeight;
        }
      }

      BufferedImage tmp = new BufferedImage(w, h, type);
      Graphics2D g2 = tmp.createGraphics();
      g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, hint);
      g2.drawImage(ret, 0, 0, w, h, null);
      g2.dispose();

      ret = tmp;
    } while (w != targetWidth || h != targetHeight);

    return ret;
  }

  public static void writeJPG(BufferedImage bufferedImage,OutputStream outputStream,float quality, String format) throws IOException
  {
    Iterator<ImageWriter> iterator =ImageIO.getImageWritersByFormatName(format);
    ImageWriter imageWriter = iterator.next();
    ImageWriteParam imageWriteParam = imageWriter.getDefaultWriteParam();
    imageWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
    imageWriteParam.setCompressionQuality(quality);
    ImageOutputStream imageOutputStream = new MemoryCacheImageOutputStream(outputStream);
    imageWriter.setOutput(imageOutputStream);
    IIOImage iioimage = new IIOImage(bufferedImage, null, null);
    imageWriter.write(null, iioimage, imageWriteParam);
    imageOutputStream.flush();
    outputStream.close();
    imageOutputStream.close();
  }

}
