/*
 * Copyright 2012 - 2015 Manuel Laggner
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

import com.jtattoo.plaf.AbstractLookAndFeel;
import com.jtattoo.plaf.BaseScrollPaneUI;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;

public class TmmLightScrollPaneUI extends BaseScrollPaneUI {

  public static ComponentUI createUI(JComponent c) {
    return new TmmLightScrollPaneUI();
  }

  @Override
  protected void installDefaults(JScrollPane scrollpane) {
    LookAndFeel.installColorsAndFont(scrollpane, "ScrollPane.background", "ScrollPane.foreground", "ScrollPane.font");

    Border vpBorder = scrollpane.getViewportBorder();
    if ((vpBorder == null) || (vpBorder instanceof UIResource)) {
      vpBorder = UIManager.getBorder("ScrollPane.viewportBorder");
      scrollpane.setViewportBorder(vpBorder);
    }
    LookAndFeel.installProperty(scrollpane, "opaque", Boolean.TRUE);

    Object roundPane = scrollpane.getClientProperty("roundScrollPane");
    if (roundPane != null && "true".equals(roundPane.toString())) {
      LookAndFeel.installColorsAndFont(scrollpane, "ScrollPane.foreground", "ScrollPane.background", "ScrollPane.font");
      LookAndFeel.installBorder(scrollpane, "ScrollPane.border");
      scrollpane.getViewport().setOpaque(false);
      scrollpane.setBackground(AbstractLookAndFeel.getControlColorLight());

      scrollpane.getHorizontalScrollBar().putClientProperty("swapColors", "true");
      scrollpane.getHorizontalScrollBar().updateUI();
      scrollpane.getVerticalScrollBar().putClientProperty("swapColors", "true");
      scrollpane.getVerticalScrollBar().updateUI();
    }

    scrollpane.getViewport().setOpaque(false);
    if (scrollpane.getViewport().getView() instanceof JComponent) {
      ((JComponent) scrollpane.getViewport().getView()).setOpaque(false);
    }

    if (scrollpane.getViewport().getView() instanceof JList) {
      if ((((JList) scrollpane.getViewport().getView()).getCellRenderer()) != null) {
        ((DefaultListCellRenderer) ((JList) scrollpane.getViewport().getView()).getCellRenderer()).setOpaque(false);
      }
    }
  }
}
