package org.tinymediamanager.ui.plaf;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Window;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JWindow;
import javax.swing.Popup;
import javax.swing.SwingUtilities;
import javax.swing.border.AbstractBorder;

public class ShadowBorderPopup extends Popup {
  final JWindow popupWindow;

  public ShadowBorderPopup(Component owner, Component contents, int ownerX, int ownerY) {
    // create a new heavyweight window
    this.popupWindow = new HeavyWeightWindow(getParentWindow(owner));
    // determine the popup location
    popupWindow.setLocation(ownerX, ownerY);
    // add the contents to the popup
    popupWindow.getContentPane().add(contents, BorderLayout.CENTER);
    contents.invalidate();
    JComponent parent = (JComponent) contents.getParent();
    // set the shadow border
    parent.setBorder(ShadowPopupBorder.getInstance());
  }

  private Window getParentWindow(Component owner) {
    Window window = null;

    if (owner instanceof Window) {
      window = (Window) owner;
    }
    else if (owner != null) {
      window = SwingUtilities.getWindowAncestor(owner);
    }
    if (window == null) {
      window = new DefaultFrame();
    }
    return window;
  }

  @SuppressWarnings("restriction")
  @Override
  public void show() {
    this.popupWindow.setVisible(true);
    this.popupWindow.pack();
    com.sun.awt.AWTUtilities.setWindowOpaque(this.popupWindow, false);
  }

  @Override
  public void hide() {
    this.popupWindow.setVisible(false);
  }

  private static class DefaultFrame extends Frame {
    private static final long serialVersionUID = 7200829432362582250L;
  }

  private static class ShadowPopupBorder extends AbstractBorder {
    private static final long        serialVersionUID = -4892202184116474708L;
    private static final int         SHADOW_SIZE      = 18;
    private static ShadowPopupBorder instance         = new ShadowPopupBorder();

    private static Image             shadow_right     = new ImageIcon(ShadowPopupBorder.class.getResource("shadow_right.png")).getImage();
    private static Image             shadow_left      = new ImageIcon(ShadowPopupBorder.class.getResource("shadow_left.png")).getImage();

    public static ShadowPopupBorder getInstance() {
      return instance;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
      /* draw drop shadow */
      // bottom
      g.drawImage(shadow_right, x + SHADOW_SIZE, y + height - SHADOW_SIZE, x + width - SHADOW_SIZE, y + height, SHADOW_SIZE, SHADOW_SIZE + 1,
          SHADOW_SIZE + 1, 2 * SHADOW_SIZE + 1, null, c);

      // top right
      g.drawImage(shadow_right, x + width - SHADOW_SIZE, y, x + width, y + 2 * SHADOW_SIZE, SHADOW_SIZE + 1, 0, 2 * SHADOW_SIZE + 1, SHADOW_SIZE,
          null, c);

      // right
      g.drawImage(shadow_right, x + width - SHADOW_SIZE, y + 2 * SHADOW_SIZE, x + width, y + height - SHADOW_SIZE, SHADOW_SIZE + 1, SHADOW_SIZE,
          2 * SHADOW_SIZE + 1, SHADOW_SIZE + 1, null, c);

      // bottom right
      g.drawImage(shadow_right, x + width - SHADOW_SIZE, y + height - SHADOW_SIZE, x + width, y + height, SHADOW_SIZE + 1, SHADOW_SIZE + 1,
          2 * SHADOW_SIZE + 1, 2 * SHADOW_SIZE + 1, null, c);

      // top left
      int width_shadow_left = shadow_left.getWidth(null);
      g.drawImage(shadow_left, x + 3, y, x + SHADOW_SIZE, y + SHADOW_SIZE, 0, 0, width_shadow_left - SHADOW_SIZE,
          width_shadow_left - SHADOW_SIZE - 1, null, c);

      // left
      g.drawImage(shadow_left, x + 3, y + SHADOW_SIZE, x + SHADOW_SIZE, y + height - SHADOW_SIZE, 0, SHADOW_SIZE, width_shadow_left - SHADOW_SIZE,
          SHADOW_SIZE + 1, null, c);

      // bottom left
      g.drawImage(shadow_left, x + 3, y + height - SHADOW_SIZE, x + SHADOW_SIZE, y + height, 0, SHADOW_SIZE + 1, width_shadow_left - SHADOW_SIZE,
          2 * SHADOW_SIZE + 1, null, c);
    }

    @Override
    public Insets getBorderInsets(Component c) {
      return new Insets(0, SHADOW_SIZE, SHADOW_SIZE, SHADOW_SIZE);
    }

    @Override
    public Insets getBorderInsets(Component c, Insets insets) {
      insets.top = 0;
      insets.left = insets.right = insets.bottom = SHADOW_SIZE;
      return insets;
    }
  }
}
