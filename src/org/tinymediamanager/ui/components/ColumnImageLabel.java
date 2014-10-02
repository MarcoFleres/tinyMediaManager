package org.tinymediamanager.ui.components;

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
import java.awt.Dimension;

/**
 * The class ColumnImageLabel. To create same width ImageLabels for a column view
 * 
 * @author Manuel Laggner
 */
public class ColumnImageLabel extends ImageLabel {
  private static final long serialVersionUID   = -3477271720593555990L;

  private float             defaultAspectRatio = 1;

  public ColumnImageLabel() {
    super();
  }

  public ColumnImageLabel(boolean drawBorder) {
    super(drawBorder);
  }

  public ColumnImageLabel(boolean drawBorder, boolean drawFullWidth) {
    super(drawBorder, drawFullWidth);
  }

  public ColumnImageLabel(boolean drawBorder, boolean drawFullWidth, boolean drawShadow) {
    super(drawBorder, drawFullWidth, drawShadow);
  }

  /**
   * set the default aspect ratio, when no image has been loaded
   * 
   * @param aspectRatio
   *          the new default aspect ratio
   */
  public void setDefaultAspectRatio(float aspectRatio) {
    defaultAspectRatio = aspectRatio;
  }

  /**
   * get the default aspect ratio of this label
   * 
   * @return
   */
  public float getDefaultAspectRatio() {
    return defaultAspectRatio;
  }

  @Override
  public Dimension getPreferredSize() {
    if (originalImage != null) {
      return new Dimension(getParent().getWidth(),
          (int) (getParent().getWidth() / (float) originalImage.getWidth() * (float) originalImage.getHeight()));
    }
    return new Dimension(getParent().getWidth(), (int) (getParent().getWidth() * defaultAspectRatio) + 1);
  }
}
