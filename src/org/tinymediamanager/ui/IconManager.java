/*
 * Copyright 2012 - 2014 Manuel Laggner
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.tinymediamanager.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.net.URL;

public class IconManager {
  private final static ImageIcon EMPTY_IMAGE       = new ImageIcon();

  public final static ImageIcon  APPLY             = loadImage("apply.png");
  public final static ImageIcon  ARROW_UP          = loadImage("arrow-up.png");
  public final static ImageIcon  ARROW_DOWN        = loadImage("arrow-down.png");
  public final static ImageIcon  BUG               = loadImage("bug.png");
  public final static ImageIcon  CANCEL            = loadImage("cancel.png");
  public final static ImageIcon  CHECK_ALL         = loadImage("check-all.png");
  public final static ImageIcon  CHECKMARK         = loadImage("checkmark.png");
  public final static ImageIcon  CLAPBOARD         = loadImage("icn_trailer.png");
  public final static ImageIcon  COPY              = loadImage("copy.png");
  public final static ImageIcon  CROSS             = loadImage("cross.png");
  public final static ImageIcon  DELETE            = loadImage("delete.png");
  public final static ImageIcon  DOT_AVAILABLE     = loadImage("dot_available.png");
  public final static ImageIcon  DOT_UNAVAILABLE   = loadImage("dot_unavailable.png");
  public final static ImageIcon  DOWNLOAD          = loadImage("download.png");
  public final static ImageIcon  DOWNLOAD_DISABLED = loadImage("download-disabled.png");
  public final static ImageIcon  EDIT              = loadImage("edit.png");
  public final static ImageIcon  ERROR             = loadImage("error.png");
  public final static ImageIcon  EXIT              = loadImage("exit.png");
  public final static ImageIcon  EXPORT            = loadImage("export.png");
  public final static ImageIcon  FEEDBACK          = loadImage("feedback.png");
  public final static ImageIcon  FILE_OPEN         = loadImage("file-open.png");
  public final static ImageIcon  FILTER            = loadImage("filter.png");
  public final static ImageIcon  HINT              = loadImage("hint.png");
  public final static ImageIcon  IMAGE             = loadImage("icn_images.png");
  public final static ImageIcon  INFO              = loadImage("icn_nfo.png");
  public final static ImageIcon  LIST_ADD          = drawAddIcon();                     // loadImage("list-add.png");
  public final static ImageIcon  LIST_REMOVE       = drawRemoveIcon();                  // loadImage("list-remove.png");
  public final static ImageIcon  LOADING           = loadImage("loading.gif");
  public final static ImageIcon  PLAY_SMALL        = loadImage("play-small.png");
  public final static ImageIcon  PLAY              = loadImage("play.png");
  public final static ImageIcon  PROCESS_STOP      = loadImage("process-stop.png");
  public final static ImageIcon  REFRESH           = loadImage("refresh.png");
  public final static ImageIcon  REGISTER          = loadImage("register.png");
  public final static ImageIcon  SEARCH            = loadImage("search.png");
  public final static ImageIcon  SETTINGS_SMALL    = loadImage("settings-small.png");
  public final static ImageIcon  STAR_FILLED       = loadImage("star-filled.png");
  public final static ImageIcon  STAR_EMPTY        = loadImage("star-empty.png");
  public final static ImageIcon  SUBTITLE          = loadImage("icn_subs.png");
  public final static ImageIcon  SYNC              = loadImage("sync.png");
  public final static ImageIcon  UNCHECK_ALL       = loadImage("uncheck-all.png");
  public final static ImageIcon  UNWATCHED         = loadImage("unwatched.png");

  private static ImageIcon loadImage(String name) {
    URL file = IconManager.class.getResource("/images/ui/" + name);
    if (file != null) {
      return new ImageIcon(file);
    }

    return EMPTY_IMAGE;
  }

  // code generated icons
  private static BufferedImage createEmptyImage(int width, int height, int transparency) {
    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    GraphicsDevice gd = ge.getDefaultScreenDevice();
    GraphicsConfiguration gc = gd.getDefaultConfiguration();
    return gc.createCompatibleImage(width, height, transparency);
  }

  private static ImageIcon drawAddIcon() {
    int width = 16;
    int height = 16;

    BufferedImage image = createEmptyImage(width, height, Transparency.TRANSLUCENT);
    Graphics2D g2D = image.createGraphics();
    g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2D.setStroke(new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));

    double offsetX = width / 8;
    double offsetY = height / 8;
    double shadowOffsetX = width / 16;
    double shadowOffsetY = height / 16;

    // shadow
    g2D.setColor(new Color(155, 155, 155));
    g2D.draw(new Line2D.Double(offsetX + shadowOffsetX, height / 2d + shadowOffsetY, width - offsetX + shadowOffsetX, height / 2d + shadowOffsetY));
    g2D.draw(new Line2D.Double(width / 2d + shadowOffsetX, offsetY + shadowOffsetY, width / 2d + shadowOffsetX, height - offsetY + shadowOffsetY));

    // cross
    g2D.setColor(Color.white);
    g2D.draw(new Line2D.Double(offsetX, height / 2d, width - offsetX, height / 2d));
    g2D.draw(new Line2D.Double(width / 2d, offsetY, width / 2d, height - offsetY));

    return new ImageIcon(image);
  }

  private static ImageIcon drawRemoveIcon() {
    int width = 16;
    int height = 16;

    BufferedImage image = createEmptyImage(width, height, Transparency.TRANSLUCENT);
    Graphics2D g2D = image.createGraphics();
    g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2D.setStroke(new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));

    double offsetX = width / 8;
    double offsetY = height / 8;
    double shadowOffsetX = width / 16;
    double shadowOffsetY = height / 16;

    // shadow
    g2D.setColor(new Color(155, 155, 155));
    g2D.draw(new Line2D.Double(width / 2d - (width / 2d - offsetX) / 1.4142 + shadowOffsetX, height / 2d - (height / 2d - offsetY) / 1.4142
        + shadowOffsetY, width / 2d + (width / 2d - offsetX + shadowOffsetX) / 1.4142 + shadowOffsetX, height / 2d
        + (height / 2d - offsetY + shadowOffsetY) / 1.4142 + shadowOffsetY));
    g2D.draw(new Line2D.Double(width / 2d - (width / 2d - offsetX) / 1.4142 + shadowOffsetX, height / 2d + (height / 2d - offsetY + shadowOffsetY)
        / 1.4142 + shadowOffsetY, width / 2d + (width / 2d - offsetX + shadowOffsetX) / 1.4142 + shadowOffsetX, height / 2d - (height / 2d - offsetY)
        / 1.4142 + shadowOffsetY));

    // cross
    g2D.setColor(Color.white);
    g2D.draw(new Line2D.Double(width / 2d - (width / 2d - offsetX) / 1.4142, height / 2d - (height / 2d - offsetY) / 1.4142, width / 2d
        + (width / 2d - offsetX) / 1.4142, height / 2d + (height / 2d - offsetY) / 1.4142));
    g2D.draw(new Line2D.Double(width / 2d - (width / 2d - offsetX) / 1.4142, height / 2d + (height / 2d - offsetY) / 1.4142, width / 2d
        + (width / 2d - offsetX) / 1.4142, height / 2d - (height / 2d - offsetY) / 1.4142));

    return new ImageIcon(image);
  }
}
