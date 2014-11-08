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
package org.tinymediamanager.ui.plaf;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.font.TextAttribute;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.swing.plaf.FontUIResource;

import com.jtattoo.plaf.AbstractTheme;

public class TmmTheme extends AbstractTheme {

  public static final java.lang.String ROBOTO = "DejaVu Sans";

  static {
    try {
      // Font robotoRegular = Font.createFont(Font.PLAIN, TmmTheme.class.getResource("Roboto-Light.ttf").openStream());
      // InputStream fontStream = TmmTheme.class.getResource("Roboto-Regular.ttf").openStream();
      InputStream fontStream = TmmTheme.class.getResource("DejaVuSans.ttf").openStream();
      Font robotoRegular = Font.createFont(Font.TRUETYPE_FONT, fontStream);
      GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(robotoRegular);
      fontStream.close();
    }
    catch (FontFormatException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  @Override
  public FontUIResource getControlTextFont() {
    if (controlFont == null) {
      controlFont = new FontUIResource(ROBOTO, Font.PLAIN, 12);
    }
    return controlFont;
  }

  @Override
  public FontUIResource getSystemTextFont() {
    if (systemFont == null) {
      systemFont = new FontUIResource(ROBOTO, Font.PLAIN, 12);
    }
    return systemFont;
  }

  @Override
  public FontUIResource getUserTextFont() {
    if (userFont == null) {
      userFont = new FontUIResource(ROBOTO, Font.PLAIN, 12);
      // FIXME
      Map<TextAttribute, Object> attributes = new HashMap<TextAttribute, Object>();
      attributes.put(TextAttribute.KERNING, TextAttribute.KERNING_ON);
      userFont = new FontUIResource(userFont.deriveFont(attributes));
    }
    return userFont;
  }

  @Override
  public FontUIResource getMenuTextFont() {
    if (menuFont == null) {
      menuFont = new FontUIResource(ROBOTO, Font.PLAIN, 12);

    }
    return menuFont;
  }

  @Override
  public FontUIResource getWindowTitleFont() {
    if (windowTitleFont == null) {
      windowTitleFont = new FontUIResource(ROBOTO, Font.BOLD, 12);
    }
    return windowTitleFont;
  }

  @Override
  public FontUIResource getSubTextFont() {
    if (smallFont == null) {
      smallFont = new FontUIResource(ROBOTO, Font.PLAIN, 10);
    }
    return smallFont;
  }
}
