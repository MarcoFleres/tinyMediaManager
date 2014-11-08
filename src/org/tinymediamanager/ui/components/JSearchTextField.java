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
package org.tinymediamanager.ui.components;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.util.ResourceBundle;

import javax.swing.UIManager;
import javax.swing.border.AbstractBorder;
import javax.swing.plaf.UIResource;

import org.tinymediamanager.ui.UTF8Control;

import com.jtattoo.plaf.AbstractLookAndFeel;

/**
 * The Class JSearchTextField.
 * 
 * @author Georgios Migdos <cyberpython@gmail.com>
 */
public class JSearchTextField extends JIconTextField implements FocusListener {
  private static final long           serialVersionUID = 5684796522381134018L;
  private static final ResourceBundle BUNDLE           = ResourceBundle.getBundle("messages", new UTF8Control()); //$NON-NLS-1$

  private String                      textWhenNotFocused;

  public JSearchTextField() {
    super();
    this.textWhenNotFocused = BUNDLE.getString("tmm.searchfield"); //$NON-NLS-1$
    this.addFocusListener(this);
     this.setBorder(new SearchTextFieldBorder());
  }

  public String getTextWhenNotFocused() {
    return this.textWhenNotFocused;
  }

  public void setTextWhenNotFocused(String newText) {
    this.textWhenNotFocused = newText;
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    if (!this.hasFocus() && this.getText().equals("")) {
      int height = this.getHeight();
      Font prev = g.getFont();
      Font italic = prev.deriveFont(Font.ITALIC);
      Color prevColor = g.getColor();
      g.setFont(italic);
      g.setColor(UIManager.getColor("textInactiveText"));
      int h = g.getFontMetrics().getHeight();
      int textBottom = (height - h) / 2 + h - 3;
      int x = this.getInsets().left;
      Graphics2D g2d = (Graphics2D) g;
      RenderingHints hints = g2d.getRenderingHints();
      g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
      g2d.drawString(textWhenNotFocused, x, textBottom);
      g2d.setRenderingHints(hints);
      g.setFont(prev);
      g.setColor(prevColor);
    }

  }

  @Override
  public void focusGained(FocusEvent e) {
    this.repaint();
  }

  @Override
  public void focusLost(FocusEvent e) {
    this.repaint();
  }

  private static class SearchTextFieldBorder extends AbstractBorder implements UIResource {
    private static final long   serialVersionUID = -7328010784938968201L;
    private static final Insets insets           = new Insets(0, 26, 0, 10);
    private int focusWidth = 2;

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
      int r = height;
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
      return insets;
    }
  }
}
