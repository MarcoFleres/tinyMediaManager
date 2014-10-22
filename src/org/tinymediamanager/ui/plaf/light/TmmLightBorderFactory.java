/*
 * Copyright (c) 2002 and later by MH Software-Entwicklung. All Rights Reserved.
 *  
 * JTattoo is multiple licensed. If your are an open source developer you can use
 * it under the terms and conditions of the GNU General Public License version 2.0
 * or later as published by the Free Software Foundation.
 *  
 * see: gpl-2.0.txt
 * 
 * If you pay for a license you will become a registered user who could use the
 * software under the terms and conditions of the GNU Lesser General Public License
 * version 2.0 or later with classpath exception as published by the Free Software
 * Foundation.
 * 
 * see: lgpl-2.0.txt
 * see: classpath-exception.txt
 * 
 * Registered users could also use JTattoo under the terms and conditions of the 
 * Apache License, Version 2.0 as published by the Apache Software Foundation.
 *  
 * see: APACHE-LICENSE-2.0.txt
 */

package org.tinymediamanager.ui.plaf.light;

import javax.swing.border.Border;

import com.jtattoo.plaf.AbstractBorderFactory;

/**
 * @author Michael Hagen
 */
public class TmmLightBorderFactory implements AbstractBorderFactory {
  private static TmmLightBorderFactory instance = null;

  private TmmLightBorderFactory() {
  }

  public static synchronized TmmLightBorderFactory getInstance() {
    if (instance == null) {
      instance = new TmmLightBorderFactory();
    }
    return instance;
  }

  public Border getFocusFrameBorder() {
    return TmmLightBorders.getFocusFrameBorder();
  }

  public Border getButtonBorder() {
    return TmmLightBorders.getButtonBorder();
  }

  public Border getToggleButtonBorder() {
    return TmmLightBorders.getToggleButtonBorder();
  }

  public Border getTextBorder() {
    return TmmLightBorders.getTextBorder();
  }

  public Border getSpinnerBorder() {
    return TmmLightBorders.getSpinnerBorder();
  }

  public Border getTextFieldBorder() {
    return TmmLightBorders.getTextFieldBorder();
  }

  public Border getComboBoxBorder() {
    return TmmLightBorders.getComboBoxBorder();
  }

  public Border getTableHeaderBorder() {
    return TmmLightBorders.getTableHeaderBorder();
  }

  public Border getTableScrollPaneBorder() {
    return TmmLightBorders.getTableScrollPaneBorder();
  }

  public Border getScrollPaneBorder() {
    return TmmLightBorders.getScrollPaneBorder();
  }

  public Border getTabbedPaneBorder() {
    return TmmLightBorders.getTabbedPaneBorder();
  }

  public Border getMenuBarBorder() {
    return TmmLightBorders.getMenuBarBorder();
  }

  public Border getMenuItemBorder() {
    return TmmLightBorders.getMenuItemBorder();
  }

  public Border getPopupMenuBorder() {
    return TmmLightBorders.getPopupMenuBorder();
  }

  public Border getInternalFrameBorder() {
    return TmmLightBorders.getInternalFrameBorder();
  }

  public Border getPaletteBorder() {
    return TmmLightBorders.getPaletteBorder();
  }

  public Border getToolBarBorder() {
    return TmmLightBorders.getToolBarBorder();
  }

  public Border getProgressBarBorder() {
    return TmmLightBorders.getProgressBarBorder();
  }

  public Border getDesktopIconBorder() {
    return TmmLightBorders.getDesktopIconBorder();
  }
}
