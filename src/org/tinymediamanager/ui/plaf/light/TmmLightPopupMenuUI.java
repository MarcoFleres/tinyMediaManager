package org.tinymediamanager.ui.plaf.light;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.JToolTip;
import javax.swing.JWindow;
import javax.swing.Popup;
import javax.swing.border.AbstractBorder;
import javax.swing.plaf.ComponentUI;

import com.jtattoo.plaf.BasePopupMenuUI;

public class TmmLightPopupMenuUI extends BasePopupMenuUI {
  public static ComponentUI createUI(JComponent c) {
    return new TmmLightPopupMenuUI();
  }

  @Override
  public Popup getPopup(JPopupMenu popupMenu, int x, int y) {
    return new TranslucentPopup(popupMenu.getInvoker(), popupMenu, x, y);
  }

  private class TranslucentPopup extends Popup {
    final JWindow popupWindow;

    TranslucentPopup(Component owner, Component contents, int ownerX, int ownerY) {
      // create a new heavyweight window
      this.popupWindow = new JWindow();
      // mark the popup with partial opacity
      com.sun.awt.AWTUtilities.setWindowOpacity(popupWindow, (contents instanceof JToolTip) ? 0.8f : 0.95f);
      // determine the popup location
      popupWindow.setLocation(ownerX, ownerY);
      // add the contents to the popup
      popupWindow.getContentPane().add(contents, BorderLayout.CENTER);
      contents.invalidate();
      JComponent parent = (JComponent) contents.getParent();
      // set the shadow border
      parent.setBorder(new ShadowPopupBorder());
    }

    @Override
    public void show() {
      this.popupWindow.setVisible(true);
      this.popupWindow.pack();
      // mark the window as non-opaque, so that the
      // shadow border pixels take on the per-pixel
      // translucency
      com.sun.awt.AWTUtilities.setWindowOpaque(this.popupWindow, false);
    }

    @Override
    public void hide() {
      this.popupWindow.setVisible(false);
      this.popupWindow.removeAll();
      this.popupWindow.dispose();
    }
  }

  static class ShadowPopupBorder extends AbstractBorder {
    private static final int         SHADOW_SIZE = 5;
    private static ShadowPopupBorder instance    = new ShadowPopupBorder();

    private static Image             shadow      = new ImageIcon(ShadowPopupBorder.class.getResource("shadow.png")).getImage();

    public static ShadowPopupBorder getInstance() {
      return instance;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
      // fake drop shadow effect in case of heavy weight popups
      JComponent popup = (JComponent) c;

      // draw drop shadow
      g.drawImage(shadow, x + 5, y + height - 5, x + 10, y + height, 0, 6, 5, 11, null, c);
      g.drawImage(shadow, x + 10, y + height - 5, x + width - 5, y + height, 5, 6, 6, 11, null, c);
      g.drawImage(shadow, x + width - 5, y + 5, x + width, y + 10, 6, 0, 11, 5, null, c);
      g.drawImage(shadow, x + width - 5, y + 10, x + width, y + height - 5, 6, 5, 11, 6, null, c);
      g.drawImage(shadow, x + width - 5, y + height - 5, x + width, y + height, 6, 6, 11, 11, null, c);
    }

    @Override
    public Insets getBorderInsets(Component c) {
      return new Insets(0, 0, SHADOW_SIZE, SHADOW_SIZE);
    }

    @Override
    public Insets getBorderInsets(Component c, Insets insets) {
      insets.left = insets.top = 0;
      insets.right = insets.bottom = SHADOW_SIZE;
      return insets;
    }

  }
}
