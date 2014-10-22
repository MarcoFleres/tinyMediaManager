package org.tinymediamanager.ui.plaf.light;

import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;

import com.jtattoo.plaf.AbstractToolBarUI;

public class TmmLightToolBarUI extends AbstractToolBarUI {
  public static ComponentUI createUI(JComponent c) {
    return new TmmLightToolBarUI();
  }

  @Override
  public Border getRolloverBorder() {
    return TmmLightBorders.getRolloverToolButtonBorder();
  }

  @Override
  public Border getNonRolloverBorder() {
    return null;
  }

  @Override
  public boolean isButtonOpaque() {
    return false;
  }

  @Override
  public void paint(Graphics g, JComponent c) {
  }
}
