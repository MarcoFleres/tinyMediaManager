package org.tinymediamanager.ui.plaf.light;

import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.Popup;
import javax.swing.plaf.ComponentUI;

import org.tinymediamanager.ui.plaf.ShadowBorderPopup;

import com.jtattoo.plaf.BasePopupMenuUI;

public class TmmLightPopupMenuUI extends BasePopupMenuUI {
  public static ComponentUI createUI(JComponent c) {
    return new TmmLightPopupMenuUI();
  }

  @Override
  public Popup getPopup(JPopupMenu popupMenu, int x, int y) {
    return new ShadowBorderPopup(popupMenu.getInvoker(), popupMenu, x, y);
  }
}
