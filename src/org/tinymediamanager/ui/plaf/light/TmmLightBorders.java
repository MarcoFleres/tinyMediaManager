package org.tinymediamanager.ui.plaf.light;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

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
    return getTextBorder();
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
    private static final Insets insets           = new Insets(2, 4, 2, 4);

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
      Graphics2D g2 = (Graphics2D) g.create();
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      int r = 10;
      RoundRectangle2D round = new RoundRectangle2D.Float(x, y, width - 1, height - 1, r, r);
      Container parent = c.getParent();
      if (parent != null) {
        g2.setColor(parent.getBackground());
        Area corner = new Area(new Rectangle2D.Float(x, y, width, height));
        corner.subtract(new Area(round));
        g2.fill(corner);
      }

      g2.dispose();
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
      Color titleColor = AbstractLookAndFeel.getWindowTitleColorLight();
      Color borderColor = AbstractLookAndFeel.getWindowBorderColor();
      g.setColor(titleColor);
      g.fillRect(x, y + 1, w, insets.top - 1);
      g.setColor(borderColor);
      g.fillRect(x, y, w, h);
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
      insets = new Insets(TOP_BOTTOM_INSETS, 1, TOP_BOTTOM_INSETS, 1);

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