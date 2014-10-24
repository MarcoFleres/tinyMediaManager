package org.tinymediamanager.ui.plaf;

import java.awt.Graphics;
import java.awt.Window;

import javax.swing.JWindow;

public class HeavyWeightWindow extends JWindow {
  private static final long serialVersionUID = -722416364265735038L;

  public HeavyWeightWindow(Window parent) {
    super(parent);
    setFocusableWindowState(false);
    // setType(Window.Type.POPUP);

    // Try to set "always-on-top" for the popup window.
    // Applets usually don't have sufficient permissions to do it.
    // In this case simply ignore the exception.
    try {
      setAlwaysOnTop(true);
    }
    catch (SecurityException se) {
      // setAlwaysOnTop is restricted,
      // the exception is ignored
    }
  }

  @Override
  public void update(Graphics g) {
    paint(g);
  }

  @SuppressWarnings("deprecation")
  @Override
  public void show() {
    this.pack();
    if (getWidth() > 0 && getHeight() > 0) {
      super.show();
    }
  }
}
