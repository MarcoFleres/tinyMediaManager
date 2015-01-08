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
package org.tinymediamanager.ui.plaf.light;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.JMenuBar;
import javax.swing.JPopupMenu;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.UIResource;

import com.jtattoo.plaf.AbstractLookAndFeel;
import com.jtattoo.plaf.BaseBorders;
import com.jtattoo.plaf.ColorHelper;
import com.jtattoo.plaf.luna.LunaBorders.RolloverToolButtonBorder;

/**
 * @author Manuel Laggner
 */
public class TmmLightBorders extends BaseBorders {
  // ------------------------------------------------------------------------------------
  // Lazy access methods
  // ------------------------------------------------------------------------------------
  public static Border getTextBorder() {
    if (textFieldBorder == null) {
      textFieldBorder = new TextFieldBorder();
    }
    return textFieldBorder;
  }

  public static Border getTextFieldBorder() {
    return getTextBorder();
  }

  public static Border getComboBoxBorder() {
    if (comboBoxBorder == null) {
      comboBoxBorder = new TextFieldBorder();
    }
    return comboBoxBorder;
  }

  public static Border getScrollPaneBorder() {
    if (scrollPaneBorder == null) {
      scrollPaneBorder = new ScrollPaneBorder(false);
    }
    return scrollPaneBorder;
  }

  public static Border getTableScrollPaneBorder() {
    if (tableScrollPaneBorder == null) {
      tableScrollPaneBorder = new ScrollPaneBorder(true);
    }
    return tableScrollPaneBorder;
  }

  public static Border getButtonBorder() {
    if (buttonBorder == null) {
      buttonBorder = new EmptyBorder(4, 15, 4, 15);
    }
    return buttonBorder;
  }

  public static Border getToggleButtonBorder() {
    return getButtonBorder();
  }

  public static Border getRolloverToolButtonBorder() {
    if (rolloverToolButtonBorder == null) {
      rolloverToolButtonBorder = new RolloverToolButtonBorder();
    }
    return rolloverToolButtonBorder;
  }

  public static Border getInternalFrameBorder() {
    if (internalFrameBorder == null) {
      internalFrameBorder = new InternalFrameBorder();
    }
    return internalFrameBorder;
  }

  public static Border getTableHeaderBorder() {
    if (tableHeaderBorder == null) {
      tableHeaderBorder = new TableHeaderBorder();
    }
    return tableHeaderBorder;
  }

  public static Border getPopupMenuBorder() {
    if (popupMenuBorder == null) {
      popupMenuBorder = new PopupMenuBorder();
    }
    return popupMenuBorder;
  }

  // ------------------------------------------------------------------------------------
  // Implementation of border classes
  // ------------------------------------------------------------------------------------
  public static class TextFieldBorder extends AbstractBorder implements UIResource {
    private static final long   serialVersionUID = -1476629322366320255L;
    private static final Insets insets           = new Insets(4, 6, 4, 6);

    private static int          focusWidth       = 2;

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
      int r = 10;
      RoundRectangle2D round = new RoundRectangle2D.Float(x + focusWidth, y + focusWidth, width - 2 * focusWidth, height - 2 * focusWidth, r, r);
      Container parent = c.getParent();
      if (parent != null) {
        GraphicsConfiguration gc = ((Graphics2D) g).getDeviceConfiguration();
        BufferedImage img = gc.createCompatibleImage(width, height, Transparency.TRANSLUCENT);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setComposite(AlphaComposite.Clear);
        g2.fillRect(0, 0, width, height);

        g2.setComposite(AlphaComposite.Src);
        g2.setColor(parent.getBackground());
        Area corner = new Area(new Rectangle2D.Float(x, y, width, height));
        corner.subtract(new Area(round));
        g2.fill(corner);

        if (c.hasFocus()) {
          x = focusWidth;
          y = focusWidth;
          int w = width - 2 * focusWidth;
          int h = height - 2 * focusWidth;
          g2.setColor(AbstractLookAndFeel.getFocusColor());
          for (int i = focusWidth; i > 0; i -= 1) {
            final float opacity = (float) (1 - (2.f * i * i / 10));
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, opacity));
            g2.fillRoundRect(x - i, y - i, w + 2 * i, h + 2 * i, r, r);
          }
        }
        g2.dispose();
        g.drawImage(img, 0, 0, null);
      }
    }

    @Override
    public Insets getBorderInsets(Component c) {
      return insets;
    }

    @Override
    public Insets getBorderInsets(Component c, Insets insets) {
      return TextFieldBorder.insets;
    }
  } // class TextFieldBorder

  public static class ScrollPaneBorder extends AbstractBorder implements UIResource {
    private static final long   serialVersionUID = -7118022577788519656L;
    private static final Color  fieldBorderColor = new Color(127, 157, 185);
    private static final Insets insets           = new Insets(2, 2, 2, 2);
    private static final Insets tableInsets      = new Insets(1, 1, 1, 1);
    private boolean             tableBorder      = false;

    public ScrollPaneBorder(boolean tableBorder) {
      this.tableBorder = tableBorder;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
      g.setColor(fieldBorderColor);
      g.drawRect(x, y, w - 1, h - 1);
      g.setColor(ColorHelper.brighter(AbstractLookAndFeel.getTheme().getBackgroundColor(), 50));
      g.drawRect(x + 1, y + 1, w - 3, h - 3);
    }

    @Override
    public Insets getBorderInsets(Component c) {
      if (tableBorder) {
        return new Insets(tableInsets.top, tableInsets.left, tableInsets.bottom, tableInsets.right);
      }
      else {
        return new Insets(insets.top, insets.left, insets.bottom, insets.right);
      }
    }

    @Override
    public Insets getBorderInsets(Component c, Insets borderInsets) {
      Insets ins = getBorderInsets(c);
      borderInsets.left = ins.left;
      borderInsets.top = ins.top;
      borderInsets.right = ins.right;
      borderInsets.bottom = ins.bottom;
      return borderInsets;
    }
  } // class ScrollPaneBorder

  public static class InternalFrameBorder extends BaseInternalFrameBorder {
    private static final long serialVersionUID = 1227394113801329301L;

    public InternalFrameBorder() {
      insets.top = 3;
      insets.left = 2;
      insets.right = 2;
      insets.bottom = 2;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
      Color borderColor = AbstractLookAndFeel.getWindowBorderColor();
      g.setColor(Color.BLACK);
      g.fillRect(x, y, w, h);
      g.setColor(borderColor);
      g.fillRect(x + 1, y + 1, w - 2, h - 2);
    }
  } // class InternalFrameBorder

  public static class TableHeaderBorder extends AbstractBorder implements UIResource {
    private static final long   serialVersionUID = -2182436739429673033L;
    private static final Insets insets           = new Insets(0, 1, 1, 1);

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
      g.setColor(ColorHelper.brighter(AbstractLookAndFeel.getControlBackgroundColor(), 40));
      g.drawLine(0, 0, 0, h - 1);
      g.setColor(ColorHelper.darker(AbstractLookAndFeel.getControlBackgroundColor(), 20));
      g.drawLine(w - 1, 0, w - 1, h - 1);
      g.setColor(ColorHelper.darker(AbstractLookAndFeel.getControlBackgroundColor(), 10));
      g.drawLine(0, h - 1, w - 1, h - 1);
    }

    @Override
    public Insets getBorderInsets(Component c) {
      return new Insets(insets.top, insets.left, insets.bottom, insets.right);
    }

    @Override
    public Insets getBorderInsets(Component c, Insets borderInsets) {
      borderInsets.left = insets.left;
      borderInsets.top = insets.top;
      borderInsets.right = insets.right;
      borderInsets.bottom = insets.bottom;
      return borderInsets;
    }
  } // class TableHeaderBorder

  public static class PopupMenuBorder extends AbstractBorder implements UIResource {
    private static final long serialVersionUID  = -2851747427345778378L;
    protected static Insets   insets;

    protected static int      TOP_BOTTOM_INSETS = 10;

    public PopupMenuBorder() {
      insets = new Insets(TOP_BOTTOM_INSETS, 1, 2 * TOP_BOTTOM_INSETS, 1);

    }

    public boolean isMenuBarPopup(Component c) {
      boolean menuBarPopup = false;
      if (c instanceof JPopupMenu) {
        JPopupMenu pm = (JPopupMenu) c;
        if (pm.getInvoker() != null) {
          menuBarPopup = (pm.getInvoker().getParent() instanceof JMenuBar);
        }
      }
      return menuBarPopup;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
      Color borderColorLo = AbstractLookAndFeel.getGridColor();// getFrameColor();

      Graphics2D g2D = (Graphics2D) g;
      Object savedRederingHint = g2D.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
      g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

      // - outer frame
      g.setColor(borderColorLo);
      if (isMenuBarPopup(c)) {
        // top
        g.drawLine(x - 1, y, x + w, y);
        // left
        g.drawLine(x, y, x, y + h - 1);
        // bottom
        g.drawLine(x, y + h - 1, x + w, y + h - 1);
        // right
        g.drawLine(x + w - 1, y + 1, x + w - 1, y + h - 1);
      }
      else {
        g.drawRect(x, y, w - 1, h - 1);
      }

      // paint the bottom border in the default panel color
      g.setColor(AbstractLookAndFeel.getBackgroundColor());
      g.fillRect(x + 1, y + h - insets.bottom, w - 2, insets.bottom - 1);

      g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, savedRederingHint);
    }

    @Override
    public Insets getBorderInsets(Component c) {
      return new Insets(insets.top, insets.left, insets.bottom, insets.right);
    }

    @Override
    public Insets getBorderInsets(Component c, Insets borderInsets) {
      Insets ins = getBorderInsets(c);
      borderInsets.left = ins.left;
      borderInsets.top = ins.top;
      borderInsets.right = ins.right;
      borderInsets.bottom = ins.bottom;
      return borderInsets;
    }

  } // class PopupMenuBorder

} // class TmmLightBorders