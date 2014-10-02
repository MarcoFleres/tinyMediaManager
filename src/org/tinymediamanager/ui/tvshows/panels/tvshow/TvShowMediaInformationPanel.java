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
package org.tinymediamanager.ui.tvshows.panels.tvshow;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ResourceBundle;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.commons.lang3.StringUtils;
import org.jdesktop.beansbinding.AutoBinding;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.Bindings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinymediamanager.core.Message;
import org.tinymediamanager.core.Message.MessageLevel;
import org.tinymediamanager.core.MessageManager;
import org.tinymediamanager.core.entities.MediaFile;
import org.tinymediamanager.core.tvshow.entities.TvShowEpisode;
import org.tinymediamanager.ui.TmmFontHelper;
import org.tinymediamanager.ui.TmmUIHelper;
import org.tinymediamanager.ui.UTF8Control;
import org.tinymediamanager.ui.components.LinkLabel;
import org.tinymediamanager.ui.components.MediaFilesPanel;
import org.tinymediamanager.ui.tvshows.TvShowSelectionModel;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.ObservableElementList;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

/**
 * The Class TvShowMediaInformationPanel. Used to display all TV show relevant media information
 * 
 * @author Manuel Laggner
 */
public class TvShowMediaInformationPanel extends JPanel {
  private static final long           serialVersionUID = 1610264727610254912L;
  private static final ResourceBundle BUNDLE           = ResourceBundle.getBundle("messages", new UTF8Control());   //$NON-NLS-1$
  private final static Logger         LOGGER           = LoggerFactory.getLogger(TvShowMediaInformationPanel.class);

  private TvShowSelectionModel        selectionModel;
  private EventList<MediaFile>        mediaFileEventList;

  private LinkLabel                   lblTvShowPath;
  private JLabel                      lblDateAdded;
  private JCheckBox                   cbWatched;
  private MediaFilesPanel             panelMediaFiles;

  public TvShowMediaInformationPanel(TvShowSelectionModel model) {
    this.selectionModel = model;
    mediaFileEventList = new ObservableElementList<MediaFile>(GlazedLists.threadSafeList(new BasicEventList<MediaFile>()),
        GlazedLists.beanConnector(MediaFile.class));

    setLayout(new FormLayout(new ColumnSpec[] { FormFactory.UNRELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.UNRELATED_GAP_COLSPEC,
        ColumnSpec.decode("default:grow"), FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC,
        ColumnSpec.decode("default:grow"), FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.UNRELATED_GAP_COLSPEC, },
        new RowSpec[] { FormFactory.PARAGRAPH_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
            FormFactory.PARAGRAPH_GAP_ROWSPEC, RowSpec.decode("default:grow"), FormFactory.PARAGRAPH_GAP_ROWSPEC, }));

    JLabel lblDateAddedT = new JLabel(BUNDLE.getString("metatag.dateadded")); //$NON-NLS-1$
    TmmFontHelper.changeFont(lblDateAddedT, Font.BOLD);
    add(lblDateAddedT, "2, 2, default, bottom");

    lblDateAdded = new JLabel("");
    add(lblDateAdded, "4, 2, default, bottom");

    JLabel lblWatchedT = new JLabel(BUNDLE.getString("metatag.watched")); //$NON-NLS-1$
    TmmFontHelper.changeFont(lblWatchedT, Font.BOLD);
    add(lblWatchedT, "6, 2");

    cbWatched = new JCheckBox("");
    cbWatched.setEnabled(false);
    add(cbWatched, "8, 2");

    JLabel lblTvShowPathT = new JLabel(BUNDLE.getString("metatag.path")); //$NON-NLS-1$
    TmmFontHelper.changeFont(lblTvShowPathT, Font.BOLD);
    add(lblTvShowPathT, "2, 4");

    lblTvShowPath = new LinkLabel("");
    lblTvShowPath.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent arg0) {
        if (!StringUtils.isEmpty(lblTvShowPath.getNormalText())) {
          // get the location from the label
          File path = new File(lblTvShowPath.getNormalText());
          try {
            // check whether this location exists
            if (path.exists()) {
              TmmUIHelper.openFile(path);
            }
          }
          catch (Exception ex) {
            LOGGER.error("open filemanager", ex);
            MessageManager.instance.pushMessage(new Message(MessageLevel.ERROR, path, "message.erroropenfolder", new String[] { ":",
                ex.getLocalizedMessage() }));
          }
        }
      }
    });
    lblTvShowPathT.setLabelFor(lblTvShowPath);
    lblTvShowPathT.setLabelFor(lblTvShowPath);
    add(lblTvShowPath, "4, 4, 5, 1");

    panelMediaFiles = new MediaFilesPanel(mediaFileEventList);
    add(panelMediaFiles, "2, 6, 9, 1, fill, fill");

    initDataBindings();

    // install the propertychangelistener
    PropertyChangeListener propertyChangeListener = new PropertyChangeListener() {
      public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        String property = propertyChangeEvent.getPropertyName();
        Object source = propertyChangeEvent.getSource();
        // react on selection of a tv show and change of media files
        if ((source.getClass() == TvShowSelectionModel.class && "selectedTvShow".equals(property))
            || (source.getClass() == TvShowEpisode.class && "mediaFiles".equals(property))) {
          try {
            mediaFileEventList.getReadWriteLock().writeLock().lock();
            mediaFileEventList.clear();
            mediaFileEventList.addAll(selectionModel.getSelectedTvShow().getMediaFiles());
          }
          catch (Exception e) {
          }
          finally {
            mediaFileEventList.getReadWriteLock().writeLock().unlock();
          }
          try {
            panelMediaFiles.adjustColumns();
          }
          catch (Exception e) {
          }
        }
      }
    };

    selectionModel.addPropertyChangeListener(propertyChangeListener);
  }

  protected void initDataBindings() {
    BeanProperty<TvShowSelectionModel, Integer> tvShowSelectionModelBeanProperty = BeanProperty.create("selectedTvShow.dateAdded.date");
    BeanProperty<JLabel, String> jLabelBeanProperty = BeanProperty.create("text");
    AutoBinding<TvShowSelectionModel, Integer, JLabel, String> autoBinding = Bindings.createAutoBinding(UpdateStrategy.READ, selectionModel,
        tvShowSelectionModelBeanProperty, lblDateAdded, jLabelBeanProperty);
    autoBinding.bind();
    //
    BeanProperty<TvShowSelectionModel, Boolean> tvShowSelectionModelBeanProperty_1 = BeanProperty.create("selectedTvShow.watched");
    BeanProperty<JCheckBox, Boolean> jCheckBoxBeanProperty = BeanProperty.create("selected");
    AutoBinding<TvShowSelectionModel, Boolean, JCheckBox, Boolean> autoBinding_1 = Bindings.createAutoBinding(UpdateStrategy.READ, selectionModel,
        tvShowSelectionModelBeanProperty_1, cbWatched, jCheckBoxBeanProperty);
    autoBinding_1.bind();
    //
    BeanProperty<TvShowSelectionModel, Integer> tvShowSelectionModelBeanProperty_2 = BeanProperty.create("selectedTvShow.dateAdded.day");
    AutoBinding<TvShowSelectionModel, Integer, JLabel, String> autoBinding_2 = Bindings.createAutoBinding(UpdateStrategy.READ, selectionModel,
        tvShowSelectionModelBeanProperty_2, lblDateAdded, jLabelBeanProperty);
    autoBinding_2.bind();
    //
    BeanProperty<TvShowSelectionModel, String> tvShowSelectionModelBeanProperty_3 = BeanProperty.create("selectedTvShow.dateAddedAsString");
    AutoBinding<TvShowSelectionModel, String, JLabel, String> autoBinding_3 = Bindings.createAutoBinding(UpdateStrategy.READ, selectionModel,
        tvShowSelectionModelBeanProperty_3, lblDateAdded, jLabelBeanProperty);
    autoBinding_3.bind();
    //
    BeanProperty<TvShowSelectionModel, String> tvShowSelectionModelBeanProperty_13 = BeanProperty.create("selectedTvShow.path");
    AutoBinding<TvShowSelectionModel, String, JLabel, String> autoBinding_19 = Bindings.createAutoBinding(UpdateStrategy.READ, selectionModel,
        tvShowSelectionModelBeanProperty_13, lblTvShowPath, jLabelBeanProperty);
    autoBinding_19.bind();
  }
}
