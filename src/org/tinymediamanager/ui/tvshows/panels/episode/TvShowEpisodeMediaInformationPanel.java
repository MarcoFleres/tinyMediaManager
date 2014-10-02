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
package org.tinymediamanager.ui.tvshows.panels.episode;

import static org.tinymediamanager.core.Constants.*;

import java.awt.Font;
import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import org.jdesktop.beansbinding.AutoBinding;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.Bindings;
import org.tinymediamanager.core.MediaFileType;
import org.tinymediamanager.core.entities.MediaFile;
import org.tinymediamanager.core.entities.MediaFileAudioStream;
import org.tinymediamanager.core.entities.MediaFileSubtitle;
import org.tinymediamanager.core.tvshow.entities.TvShowEpisode;
import org.tinymediamanager.ui.TmmFontHelper;
import org.tinymediamanager.ui.UTF8Control;
import org.tinymediamanager.ui.components.MediaFilesPanel;
import org.tinymediamanager.ui.tvshows.TvShowEpisodeSelectionModel;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.ObservableElementList;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

/**
 * The Class TvShowEpisodeMediaInformationPanel.
 * 
 * @author Manuel Laggner
 */
public class TvShowEpisodeMediaInformationPanel extends JPanel {
  private static final ResourceBundle BUNDLE           = ResourceBundle.getBundle("messages", new UTF8Control()); //$NON-NLS-1$
  private static final long           serialVersionUID = 2513029074142934502L;

  private TvShowEpisodeSelectionModel selectionModel;
  private EventList<MediaFile>        mediaFileEventList;

  /**
   * UI components
   */
  private JLabel                      lblRuntime;
  private JCheckBox                   chckbxWatched;
  private JPanel                      panelVideoStreamDetails;
  private JLabel                      lblVideoCodec;
  private JLabel                      lblVideoResolution;
  private JLabel                      lblVideoBitrate;
  private JPanel                      panelAudioStreamT;
  private JPanel                      panelAudioStreamDetails;
  private JPanel                      panelSubtitleT;
  private JPanel                      panelSubtitleDetails;
  private JSeparator                  separator;
  private JLabel                      lblDateAdded;
  private JLabel                      lblEpisodePath;

  public TvShowEpisodeMediaInformationPanel(TvShowEpisodeSelectionModel model) {
    this.selectionModel = model;
    mediaFileEventList = new ObservableElementList<MediaFile>(GlazedLists.threadSafeList(new BasicEventList<MediaFile>()),
        GlazedLists.beanConnector(MediaFile.class));

    setLayout(new FormLayout(new ColumnSpec[] { FormFactory.UNRELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC,
        ColumnSpec.decode("10dlu"), FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC,
        ColumnSpec.decode("10dlu"), FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC,
        ColumnSpec.decode("10dlu"), FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC,
        ColumnSpec.decode("100px:grow"), FormFactory.UNRELATED_GAP_COLSPEC, }, new RowSpec[] { FormFactory.RELATED_GAP_ROWSPEC,
        FormFactory.DEFAULT_ROWSPEC, FormFactory.UNRELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.UNRELATED_GAP_ROWSPEC,
        FormFactory.DEFAULT_ROWSPEC, FormFactory.UNRELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
        FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
        FormFactory.DEFAULT_ROWSPEC, FormFactory.PARAGRAPH_GAP_ROWSPEC, RowSpec.decode("default:grow"), FormFactory.PARAGRAPH_GAP_ROWSPEC, }));

    JLabel lblRuntimeT = new JLabel(BUNDLE.getString("metatag.runtime")); //$NON-NLS-1$
    TmmFontHelper.changeFont(lblRuntimeT, Font.BOLD);
    add(lblRuntimeT, "2, 2");

    lblRuntime = new JLabel("");
    add(lblRuntime, "6, 2");

    JLabel lblWatchedT = new JLabel(BUNDLE.getString("metatag.watched")); //$NON-NLS-1$
    TmmFontHelper.changeFont(lblWatchedT, Font.BOLD);
    add(lblWatchedT, "10, 2");

    chckbxWatched = new JCheckBox("");
    add(chckbxWatched, "14, 2");

    JLabel lblVideoT = new JLabel(BUNDLE.getString("metatag.video")); //$NON-NLS-1$
    TmmFontHelper.changeFont(lblVideoT, Font.BOLD);
    add(lblVideoT, "2, 4");

    JLabel lblEpisode = new JLabel(BUNDLE.getString("metatag.episode")); //$NON-NLS-1$
    add(lblEpisode, "6, 4");

    panelVideoStreamDetails = new JPanel();
    panelVideoStreamDetails.setLayout(new GridLayout(1, 4, 0, 25));
    add(panelVideoStreamDetails, "10, 4, 7, 1, fill, top");

    lblVideoCodec = new JLabel("");
    panelVideoStreamDetails.add(lblVideoCodec);

    lblVideoResolution = new JLabel("");
    panelVideoStreamDetails.add(lblVideoResolution);

    lblVideoBitrate = new JLabel("");
    panelVideoStreamDetails.add(lblVideoBitrate);

    // to create the same spacing as in audio
    panelVideoStreamDetails.add(new JLabel(""));

    JLabel lblAudioT = new JLabel(BUNDLE.getString("metatag.audio")); //$NON-NLS-1$
    TmmFontHelper.changeFont(lblAudioT, Font.BOLD);
    add(lblAudioT, "2, 6, default, top");

    panelAudioStreamT = new JPanel();
    panelAudioStreamT.setLayout(new GridLayout(0, 1));
    add(panelAudioStreamT, "6, 6, left, top");

    panelAudioStreamDetails = new JPanel();
    panelAudioStreamDetails.setLayout(new GridLayout(0, 4));
    add(panelAudioStreamDetails, "10, 6, 7, 1, fill, top");

    JLabel lblSubtitleT = new JLabel(BUNDLE.getString("metatag.subtitles")); //$NON-NLS-1$
    TmmFontHelper.changeFont(lblSubtitleT, Font.BOLD);
    add(lblSubtitleT, "2, 8, default, top");

    panelSubtitleT = new JPanel();
    panelSubtitleT.setLayout(new GridLayout(0, 1));
    add(panelSubtitleT, "6, 8, left, top");

    panelSubtitleDetails = new JPanel();
    panelSubtitleDetails.setLayout(new GridLayout(0, 1));
    add(panelSubtitleDetails, "10, 8, 5, 1, left, top");

    separator = new JSeparator();
    add(separator, "2, 10, 15, 1");

    JLabel lblDateAddedT = new JLabel(BUNDLE.getString("metatag.dateadded")); //$NON-NLS-1$
    TmmFontHelper.changeFont(lblDateAddedT, Font.BOLD);
    add(lblDateAddedT, "2, 12");

    lblDateAdded = new JLabel("");
    add(lblDateAdded, "6, 12, 11, 1");

    JLabel lblEpisodePathT = new JLabel(BUNDLE.getString("metatag.path")); //$NON-NLS-1$
    TmmFontHelper.changeFont(lblEpisodePathT, Font.BOLD);
    add(lblEpisodePathT, "2, 14");

    lblEpisodePath = new JLabel("");
    add(lblEpisodePath, "6, 14, 11, 1");

    final MediaFilesPanel panelMediaFiles = new MediaFilesPanel(mediaFileEventList);
    add(panelMediaFiles, "2, 16, 15, 1, fill, fill");

    // install the propertychangelistener
    PropertyChangeListener propertyChangeListener = new PropertyChangeListener() {
      @Override
      public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        String property = propertyChangeEvent.getPropertyName();
        Object source = propertyChangeEvent.getSource();
        // react on selection of a movie and change of media files
        if ((source.getClass() == TvShowEpisodeSelectionModel.class && "selectedTvShowEpisode".equals(property))
            || MEDIA_INFORMATION.equals(property)) {
          fillVideoStreamDetails();
          buildAudioStreamDetails();
          buildSubtitleStreamDetails();
        }
        if ((source.getClass() == TvShowEpisodeSelectionModel.class && "selectedTvShowEpisode".equals(property))
            || (source.getClass() == TvShowEpisode.class && MEDIA_FILES.equals(property))) {
          // this does sometimes not work. simply wrap it
          try {
            mediaFileEventList.getReadWriteLock().writeLock().lock();
            mediaFileEventList.clear();
            mediaFileEventList.addAll(selectionModel.getSelectedTvShowEpisode().getMediaFiles());
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
    initDataBindings();
  }

  private void fillVideoStreamDetails() {
    List<MediaFile> mediaFiles = selectionModel.getSelectedTvShowEpisode().getMediaFiles(MediaFileType.VIDEO);

    if (mediaFiles.size() == 0) {
      return;
    }

    MediaFile mediaFile = mediaFiles.get(0);

    int runtime = 0;
    for (MediaFile mf : mediaFiles) {
      runtime += mf.getDuration();
    }

    if (runtime == 0) {
      lblRuntime.setText("");
    }
    else {
      int minutes = (int) (runtime / 60) % 60;
      int hours = (int) (runtime / (60 * 60)) % 24;
      lblRuntime.setText(hours + "h " + String.format("%02d", minutes) + "m");
    }

    chckbxWatched.setSelected(selectionModel.getSelectedTvShowEpisode().isWatched());

    lblVideoCodec.setText(mediaFile.getVideoCodec());
    lblVideoResolution.setText(mediaFile.getVideoResolution());
    lblVideoBitrate.setText(mediaFile.getBiteRateInKbps());
  }

  private void buildAudioStreamDetails() {
    panelAudioStreamT.removeAll();
    panelAudioStreamDetails.removeAll();

    List<MediaFile> mediaFiles = selectionModel.getSelectedTvShowEpisode().getMediaFilesContainingAudioStreams();

    for (MediaFile mediaFile : mediaFiles) {
      for (int i = 0; i < mediaFile.getAudioStreams().size(); i++) {
        MediaFileAudioStream audioStream = mediaFile.getAudioStreams().get(i);

        if (mediaFile.getType() == MediaFileType.VIDEO) {
          panelAudioStreamT.add(new JLabel(BUNDLE.getString("metatag.internal"))); //$NON-NLS-1$
        }
        else {
          panelAudioStreamT.add(new JLabel(BUNDLE.getString("metatag.external"))); //$NON-NLS-1$
        }

        panelAudioStreamDetails.add(new JLabel(audioStream.getCodec()));
        panelAudioStreamDetails.add(new JLabel(audioStream.getChannels()));
        panelAudioStreamDetails.add(new JLabel(audioStream.getBitrateInKbps()));
        panelAudioStreamDetails.add(new JLabel(audioStream.getLanguage()));
      }
    }
  }

  private void buildSubtitleStreamDetails() {
    panelSubtitleT.removeAll();
    panelSubtitleDetails.removeAll();

    List<MediaFile> mediaFiles = selectionModel.getSelectedTvShowEpisode().getMediaFilesContainingSubtitles();

    for (MediaFile mediaFile : mediaFiles) {
      for (int i = 0; i < mediaFile.getSubtitles().size(); i++) {
        MediaFileSubtitle subtitle = mediaFile.getSubtitles().get(i);

        if (mediaFile.getType() == MediaFileType.VIDEO) {
          panelSubtitleT.add(new JLabel(BUNDLE.getString("metatag.internal"))); //$NON-NLS-1$
          String info = subtitle.getLanguage() + (subtitle.isForced() ? " forced" : "") + " (" + subtitle.getCodec() + ")";
          panelSubtitleDetails.add(new JLabel(info));
        }
        else {
          panelSubtitleT.add(new JLabel(BUNDLE.getString("metatag.external"))); //$NON-NLS-1$
          panelSubtitleDetails.add(new JLabel(mediaFile.getFilename()));
        }
      }
    }
  }

  protected void initDataBindings() {
    BeanProperty<TvShowEpisodeSelectionModel, String> tvShowEpisodeSelectionModelBeanProperty = BeanProperty
        .create("selectedTvShowEpisode.dateAddedAsString");
    BeanProperty<JLabel, String> jLabelBeanProperty = BeanProperty.create("text");
    AutoBinding<TvShowEpisodeSelectionModel, String, JLabel, String> autoBinding = Bindings.createAutoBinding(UpdateStrategy.READ, selectionModel,
        tvShowEpisodeSelectionModelBeanProperty, lblDateAdded, jLabelBeanProperty);
    autoBinding.bind();
    //
    BeanProperty<TvShowEpisodeSelectionModel, String> tvShowEpisodeSelectionModelBeanProperty_1 = BeanProperty.create("selectedTvShowEpisode.path");
    AutoBinding<TvShowEpisodeSelectionModel, String, JLabel, String> autoBinding_1 = Bindings.createAutoBinding(UpdateStrategy.READ, selectionModel,
        tvShowEpisodeSelectionModelBeanProperty_1, lblEpisodePath, jLabelBeanProperty);
    autoBinding_1.bind();
  }
}
