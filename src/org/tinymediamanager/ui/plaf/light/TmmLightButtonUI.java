/*
 * Copyright 2012 - 2013 Manuel Laggner
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

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.JComponent;
import javax.swing.JMenuBar;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.ComponentUI;

import sun.swing.SwingUtilities2;

import com.jtattoo.plaf.AbstractLookAndFeel;
import com.jtattoo.plaf.BaseButtonUI;
import com.jtattoo.plaf.ColorHelper;
import com.jtattoo.plaf.JTattooUtilities;

/**
 * @author Manuel Laggner
 * 
 */
public class TmmLightButtonUI extends BaseButtonUI {
  private static Color BUTTON_DEFAULT_COLOR  = new Color(76, 76, 76);
  private static Color BUTTON_ROLLOVER_COLOR = new Color(42, 42, 42);
  private static Color BUTTON_PRESS_COLOR    = new Color(141, 165, 179);

  private boolean      isFlatButton          = false;

  public static ComponentUI createUI(JComponent c) {
    return new TmmLightButtonUI();
  }

  @Override
  public void installDefaults(AbstractButton b) {
    super.installDefaults(b);

    Object prop = b.getClientProperty("flatButton");
    if (prop != null && prop instanceof Boolean) {
      Boolean flat = (Boolean) prop;
      isFlatButton = flat;
    }

    b.setOpaque(false);
    b.setFocusPainted(false);
  }

  @Override
  protected void paintBackground(Graphics g, AbstractButton b) {
    if (isFlatButton || !b.isContentAreaFilled() || (b.getParent() instanceof JMenuBar)) {
      return;
    }
    Graphics2D g2D = (Graphics2D) g;

    Object savedRederingHint = g2D.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
    g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    int width = b.getWidth();
    int height = b.getHeight();
    int borderRadius = b.getHeight();

    Insets insets = b.getBorder().getBorderInsets(b);
    int x = insets.left > 0 ? 1 : 0;
    int y = insets.top > 0 ? 1 : 0;
    int w = insets.right > 0 ? width - 1 : width;
    int h = insets.bottom > 0 ? height - 1 : height;

    ButtonModel model = b.getModel();
    if ((model.isPressed()) && (model.isArmed())) {
      g2D.setColor(BUTTON_PRESS_COLOR);
    }
    else if ((b.isRolloverEnabled()) && (model.isRollover())) {
      g2D.setColor(BUTTON_ROLLOVER_COLOR);
    }
    else {
      g2D.setColor(BUTTON_DEFAULT_COLOR);
    }

    g2D.fillRoundRect(x, y, w - 1, h - 1, borderRadius, borderRadius);
    g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, savedRederingHint);
  }

  @SuppressWarnings("restriction")
  protected void paintText(Graphics g, AbstractButton b, Rectangle textRect, String text) {
    ButtonModel model = b.getModel();

    FontMetrics fm = SwingUtilities2.getFontMetrics(b, g, b.getFont());
    int mnemIndex;
    if (JTattooUtilities.getJavaVersion() >= 1.4) {
      mnemIndex = b.getDisplayedMnemonicIndex();
    }
    else {
      mnemIndex = JTattooUtilities.findDisplayedMnemonicIndex(b.getText(), model.getMnemonic());
    }

    if (model.isEnabled()) {
      Color foreground = b.getForeground();
      Color background = b.getBackground();
      int offs = 0;
      if (model.isArmed() && model.isPressed()) {
        offs = 0;
      }
      if (!(model.isPressed() && model.isArmed())) {
        Object sc = b.getClientProperty("shadowColor");
        if (sc instanceof Color) {
          g.setColor((Color) sc);
          JTattooUtilities.drawStringUnderlineCharAt(b, g, text, mnemIndex, textRect.x + 1, textRect.y + 1 + fm.getAscent());
        }
      }
      if (background instanceof ColorUIResource) {
        if (model.isPressed() && model.isArmed()) {
          g.setColor(AbstractLookAndFeel.getTheme().getPressedForegroundColor());
        }
        else if (model.isRollover()) {
          g.setColor(AbstractLookAndFeel.getTheme().getRolloverForegroundColor());
        }
        else {
          g.setColor(foreground);
        }
      }
      else {
        g.setColor(foreground);
      }
      JTattooUtilities.drawStringUnderlineCharAt(b, g, text, mnemIndex, textRect.x + offs, textRect.y + offs + fm.getAscent());
    }
    else {
      if (ColorHelper.getGrayValue(b.getForeground()) < 128) {
        g.setColor(Color.white);
        JTattooUtilities.drawStringUnderlineCharAt(b, g, text, mnemIndex, textRect.x + 1, textRect.y + 1 + fm.getAscent());
      }
      g.setColor(AbstractLookAndFeel.getDisabledForegroundColor());
      JTattooUtilities.drawStringUnderlineCharAt(b, g, text, mnemIndex, textRect.x, textRect.y + fm.getAscent());
    }
  }

  @Override
  protected void paintFocus(Graphics g, AbstractButton b, Rectangle viewRect, Rectangle textRect, Rectangle iconRect) {
  }
}
