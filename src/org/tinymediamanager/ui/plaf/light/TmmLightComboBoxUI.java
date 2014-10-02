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
import java.awt.Graphics;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;

import com.jtattoo.plaf.BaseComboBoxUI;
import com.jtattoo.plaf.NoFocusButton;

/**
 * @author Manuel Laggner
 * 
 */
public class TmmLightComboBoxUI extends BaseComboBoxUI {
  private static final Color FOREGROUND_COLOR = new Color(208, 208, 208);

  public static ComponentUI createUI(JComponent c) {
    return new TmmLightComboBoxUI();
  }

  @Override
  public JButton createArrowButton() {
    return new ArrowButton();
  }

  @Override
  public void installUI(JComponent c) {
    super.installUI(c);
    this.comboBox.setRequestFocusEnabled(false);
  }

  @Override
  protected void setButtonBorder() {
  }

  public static class ArrowButton extends NoFocusButton {
    private static final long serialVersionUID = -2765755741007665606L;

    @Override
    public void paint(Graphics g) {
      int w = getWidth();
      int h = getHeight();

      g.setColor(FOREGROUND_COLOR);

      int[] xPoints = { w / 2 + 5, w / 2 - 5, w / 2 };
      int[] yPoints = { h / 2 - 1, h / 2 - 1, h / 2 + 4 };
      g.fillPolygon(xPoints, yPoints, xPoints.length);
    }
  }
}
