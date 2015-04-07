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
package org.tinymediamanager.ui.converter;

import org.jdesktop.beansbinding.Converter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.net.URL;

/**
 * The class MediaInfoAspectRatioConverter.
 * 
 * @author Manuel Laggner
 */
public class MediaInfoAspectRatioConverter extends Converter<Float, Icon> {
  private static final Logger   LOGGER     = LoggerFactory.getLogger(MediaInfoAspectRatioConverter.class);
  public final static ImageIcon emptyImage = new ImageIcon();

  @Override
  public Icon convertForward(Float arg0) {
    // try to get the image file

    // a) return null if the aspect ratio is zero
    if (arg0 == 0) {
      return null;
    }

    try {
      String ratio = String.valueOf(arg0);
      // try to load image
      URL file = MediaInfoAspectRatioConverter.class.getResource("/images/mediainfo/aspect/" + ratio + ".png");

      // return image
      if (file != null) {
        return new ImageIcon(file);
      }
    }
    catch (Exception e) {
      LOGGER.warn(e.getMessage());
    }

    // we did not get any file: return the empty
    return emptyImage;
  }

  @Override
  public Float convertReverse(Icon arg0) {
    return null;
  }

}
