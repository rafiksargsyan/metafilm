package com.rsargsyan.metafilm.main_ctx.core;

import io.trbl.blurhash.BlurHash;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;

@Slf4j
public class BlurhashUtil {

  private static final int TARGET_WIDTH = 100;

  public static String compute(byte[] imageBytes) {
    try {
      BufferedImage original = ImageIO.read(new ByteArrayInputStream(imageBytes));
      if (original == null) return null;

      int targetWidth = Math.min(original.getWidth(), TARGET_WIDTH);
      int targetHeight = (int) Math.round(original.getHeight() * ((double) targetWidth / original.getWidth()));

      BufferedImage scaled = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
      Graphics2D g = scaled.createGraphics();
      g.drawImage(original.getScaledInstance(targetWidth, targetHeight, Image.SCALE_FAST), 0, 0, null);
      g.dispose();

      int[] pixels = scaled.getRGB(0, 0, targetWidth, targetHeight, null, 0, targetWidth);
      int cx = targetWidth >= targetHeight ? 4 : 3;
      int cy = targetWidth >= targetHeight ? 3 : 4;
      return BlurHash.encode(pixels, targetWidth, targetHeight, cx, cy);
    } catch (Exception e) {
      log.warn("Failed to compute blurhash", e);
      return null;
    }
  }

  private BlurhashUtil() {}
}
