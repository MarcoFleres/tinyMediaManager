package org.tinymediamanager.ui.components;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import com.jhlabs.image.GaussianFilter;

public class TmmFilterPanel extends JPanel implements ComponentListener {
  private static final long serialVersionUID = -6741479097779634748L;
  private int               arcRadius        = 15;
  private int               shadowSize       = 13;

  private BufferedImage     shadowImage      = null;

  public TmmFilterPanel() {
    // This is very important, as part of the panel is going to be transparent
    setOpaque(false);
  }

  @Override
  public Insets getInsets() {
    // return new Insets(0, 2 * shadowSize, 2 * shadowSize, 2 * shadowSize);
    return new Insets(0, shadowSize, shadowSize, shadowSize);
  }

  @Override
  protected void paintComponent(Graphics g) {
    int width = getWidth() - 1;
    int height = getHeight() - 1;

    Graphics2D g2d = (Graphics2D) g.create();
    applyQualityProperties(g2d);
    Insets insets = getInsets();
    Rectangle panelBounds = getBounds();
    panelBounds.x = insets.left;
    panelBounds.y = insets.top;
    panelBounds.width = width - (insets.left + insets.right);
    panelBounds.height = height - (insets.top + insets.bottom);

    RoundRectangle2D shape = new RoundRectangle2D.Float(panelBounds.x, panelBounds.y, panelBounds.width, panelBounds.height, arcRadius, arcRadius);
    Rectangle2D shape2 = new Rectangle2D.Float(panelBounds.x, panelBounds.y, panelBounds.width, arcRadius);
    Area areaPanel = new Area(shape);
    areaPanel.add(new Area(shape2));

    if (shadowImage == null) {
      Rectangle shadowBounds = getBounds();
      shadowBounds.x = insets.left / 2;
      shadowBounds.y = insets.top / 2;
      shadowBounds.width = width - (insets.left + insets.right) / 2;
      shadowBounds.height = height - (insets.top + insets.bottom) / 2;
      shape = new RoundRectangle2D.Float(shadowBounds.x, shadowBounds.y, shadowBounds.width, shadowBounds.height, arcRadius, arcRadius);
      shape2 = new Rectangle2D.Float(shadowBounds.x, shadowBounds.y, shadowBounds.width, arcRadius);
      Area areaShadow = new Area(shape);
      areaShadow.add(new Area(shape2));
      BufferedImage img = createCompatibleImage(width, height);
      Graphics2D tg2d = img.createGraphics();
      applyQualityProperties(g2d);
      tg2d.setColor(Color.BLACK);
      tg2d.fill(areaShadow);
      tg2d.dispose();
      shadowImage = generateShadow(img, shadowSize, Color.BLACK, 0.5f);
    }

    g2d.drawImage(shadowImage, 0, 0, this);

    g2d.setColor(getBackground());
    g2d.fill(areaPanel);

    /**
     * THIS ONE OF THE ONLY OCCASIONS THAT I WOULDN'T CALL super.paintComponent *
     */
    getUI().paint(g2d, this);

    g2d.setColor(Color.GRAY);
    g2d.draw(areaPanel);
    g2d.dispose();
  }

  public GraphicsConfiguration getGraphicsConfiguration() {
    return GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
  }

  protected void applyQualityProperties(Graphics2D g2) {
    g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
    g2.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
    g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
  }

  protected BufferedImage createCompatibleImage(int width, int height) {
    return createCompatibleImage(width, height, Transparency.TRANSLUCENT);
  }

  protected BufferedImage createCompatibleImage(int width, int height, int transparency) {
    BufferedImage image = getGraphicsConfiguration().createCompatibleImage(width, height, transparency);
    image.coerceData(true);
    return image;
  }

  protected BufferedImage generateShadow(BufferedImage imgSource, int size, Color color, float alpha) {
    int imgWidth = imgSource.getWidth() + (size * 2);
    int imgHeight = imgSource.getHeight() + (size * 2);

    BufferedImage imgMask = createCompatibleImage(imgWidth, imgHeight);
    Graphics2D g2 = imgMask.createGraphics();
    applyQualityProperties(g2);

    g2.drawImage(imgSource, 0, 0, null);
    g2.dispose();

    // ---- Blur here ---
    BufferedImage imgShadow = generateBlur(imgMask, size, color, alpha);

    return imgShadow;
  }

  protected BufferedImage generateBlur(BufferedImage imgSource, int size, Color color, float alpha) {
    GaussianFilter filter = new GaussianFilter(size);

    int imgWidth = imgSource.getWidth();
    int imgHeight = imgSource.getHeight();

    BufferedImage imgBlur = createCompatibleImage(imgWidth, imgHeight);
    Graphics2D g2d = imgBlur.createGraphics();
    applyQualityProperties(g2d);

    g2d.drawImage(imgSource, 0, 0, null);
    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_IN, alpha));
    g2d.setColor(color);

    g2d.fillRect(0, 0, imgSource.getWidth(), imgSource.getHeight());
    g2d.dispose();

    imgBlur = filter.filter(imgBlur, null);

    return imgBlur;
  }

  @Override
  public void componentResized(ComponentEvent e) {
    // invalidate shadow image
    shadowImage = null;
  }

  @Override
  public void componentMoved(ComponentEvent e) {
  }

  @Override
  public void componentShown(ComponentEvent e) {
  }

  @Override
  public void componentHidden(ComponentEvent e) {
  }
}
