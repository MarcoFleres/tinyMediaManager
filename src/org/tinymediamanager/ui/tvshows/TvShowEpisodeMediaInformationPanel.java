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
package org.tinymediamanager.ui.tvshows;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.Comparator;
import java.util.ResourceBundle;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.apache.commons.lang3.StringUtils;
import org.jdesktop.beansbinding.AutoBinding;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.Bindings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinymediamanager.core.MediaFile;
import org.tinymediamanager.core.tvshow.TvShowEpisode;
import org.tinymediamanager.ui.TableColumnAdjuster;
import org.tinymediamanager.ui.UTF8Control;
import org.tinymediamanager.ui.components.LinkLabel;
import org.tinymediamanager.ui.components.ZebraJTable;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.ObservableElementList;
import ca.odell.glazedlists.gui.AdvancedTableFormat;
import ca.odell.glazedlists.swing.DefaultEventTableModel;
import ca.odell.glazedlists.swing.GlazedListsSwing;

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

  /** The Constant BUNDLE. */
  private static final ResourceBundle       BUNDLE              = ResourceBundle.getBundle("messages", new UTF8Control());          //$NON-NLS-1$

  /** The Constant serialVersionUID. */
  private static final long                 serialVersionUID    = 1L;

  /** The logger. */
  private final static Logger               LOGGER              = LoggerFactory.getLogger(TvShowEpisodeMediaInformationPanel.class);

  /** The selection model. */
  private TvShowEpisodeSelectionModel       selectionModel;

  /** The lbl files t. */
  private JLabel                            lblFilesT;

  /** The scroll pane files. */
  private JScrollPane                       scrollPaneFiles;

  /** The table files. */
  private JTable                            tableFiles;

  /** The lbl path. */
  private LinkLabel                         lblEpisodePath;

  /** The lbl date added t. */
  private JLabel                            lblDateAddedT;

  /** The lbl date added. */
  private JLabel                            lblDateAdded;

  /** The cb watched. */
  private JCheckBox                         cbWatched;

  /** The lbl watched t. */
  private JLabel                            lblWatchedT;

  /** The lbl path t. */
  private JLabel                            lblEpisodePathT;

  // /** The btn play. */
  // private JButton btnPlay;

  /** The table column adjuster. */
  private TableColumnAdjuster               tableColumnAdjuster = null;

  /** The media file event list. */
  private EventList<MediaFile>              mediaFileEventList;

  /** The media file table model. */
  private DefaultEventTableModel<MediaFile> mediaFileTableModel = null;

  /**
   * Instantiates a new tv show media information panel.
   * 
   * @param model
   *          the model
   */
  public TvShowEpisodeMediaInformationPanel(TvShowEpisodeSelectionModel model) {
    this.selectionModel = model;
    mediaFileEventList = GlazedListsSwing.swingThreadProxyList(new ObservableElementList<MediaFile>(GlazedLists
        .threadSafeList(new BasicEventList<MediaFile>()), GlazedLists.beanConnector(MediaFile.class)));

    setLayout(new FormLayout(new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC,
        ColumnSpec.decode("default:grow"), FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC,
        ColumnSpec.decode("default:grow"), FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, },
        new RowSpec[] { FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.NARROW_LINE_GAP_ROWSPEC,
            FormFactory.DEFAULT_ROWSPEC, FormFactory.NARROW_LINE_GAP_ROWSPEC, RowSpec.decode("default:grow"), FormFactory.NARROW_LINE_GAP_ROWSPEC, }));

    lblDateAddedT = new JLabel(BUNDLE.getString("metatag.dateadded")); //$NON-NLS-1$
    add(lblDateAddedT, "2, 2");

    lblDateAdded = new JLabel("");
    add(lblDateAdded, "4, 2");

    lblWatchedT = new JLabel(BUNDLE.getString("metatag.watched")); //$NON-NLS-1$
    add(lblWatchedT, "6, 2");

    cbWatched = new JCheckBox("");
    cbWatched.setEnabled(false);
    add(cbWatched, "8, 2");

    // btnPlay = new JButton("Play");
    // btnPlay.addActionListener(new ActionListener() {
    // public void actionPerformed(ActionEvent arg0) {
    // if (!StringUtils.isEmpty(lblMoviePath.getNormalText())) {
    // // get the location from the label
    // StringBuilder movieFile = new
    // StringBuilder(lblMoviePath.getNormalText());
    // movieFile.append(File.separator);
    // movieFile.append(movieSelectionModel.getselectedTvShowEpisode().getMediaFiles().get(0).getFilename());
    // File f = new File(movieFile.toString());
    //
    // try {
    // if (f.exists()) {
    //
    // String vlcF = f.getAbsolutePath();
    // // FIXME: german umlauts do not decode correctly; Bug in
    // // libDvdNav? so workaround;
    // if (vlcF.matches(".*[äöüÄÖÜ].*")) {
    // LOGGER.debug("VLC: workaround: german umlauts found - use system player");
    // Desktop.getDesktop().open(f);
    // }
    // else {
    // try {
    //
    // if (!vlcF.startsWith("/")) {
    // // add the missing 3rd / if not start with one (eg windows)
    // vlcF = "/" + vlcF;
    // }
    // String mrl = new FileMrl().file(vlcF).value();
    //
    // final EmbeddedMediaPlayerComponent mediaPlayerComponent = new
    // EmbeddedMediaPlayerComponent();
    // JFrame frame = new JFrame("player");
    // frame.setLocation(100, 100);
    // frame.setSize(1050, 600);
    // frame.setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
    // frame.setVisible(true);
    // frame.setContentPane(mediaPlayerComponent);
    // // mrl = mrl.replace("file://", ""); // does not work either
    //
    // LOGGER.debug("VLC: playing " + mrl);
    // Boolean ok = mediaPlayerComponent.getMediaPlayer().playMedia(mrl);
    // if (!ok) {
    // LOGGER.error("VLC: couldn't create player window!");
    // }
    // }
    // catch (RuntimeException e) {
    // LOGGER.warn("VLC: has not been initialized on startup - use system player");
    // Desktop.getDesktop().open(f);
    // }
    // catch (NoClassDefFoundError e) {
    // LOGGER.warn("VLC: has not been initialized on startup - use system player");
    // Desktop.getDesktop().open(f);
    // }
    //
    // } // end else
    // } // end exists
    // } // end try
    // catch (IOException e) {
    // LOGGER.error("Error opening file", e);
    // }
    // } // end isEmpty
    // }
    // });
    // add(btnPlay, "10, 2");

    lblEpisodePathT = new JLabel(BUNDLE.getString("metatag.path")); //$NON-NLS-1$
    add(lblEpisodePathT, "2, 4");

    lblEpisodePath = new LinkLabel("");
    lblEpisodePath.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        if (!StringUtils.isEmpty(lblEpisodePath.getNormalText())) {
          try {
            // get the location from the label
            File path = new File(lblEpisodePath.getNormalText());
            // check whether this location exists
            if (path.exists()) {
              Desktop.getDesktop().open(path);
            }
          }
          catch (Exception ex) {
            LOGGER.error("open filemanager", ex);
          }
        }
      }
    });
    lblEpisodePathT.setLabelFor(lblEpisodePath);
    lblEpisodePathT.setLabelFor(lblEpisodePath);
    add(lblEpisodePath, "4, 4, 5, 1");

    lblFilesT = new JLabel(BUNDLE.getString("metatag.files")); //$NON-NLS-1$
    add(lblFilesT, "2, 6, default, top");

    mediaFileTableModel = new DefaultEventTableModel<MediaFile>(mediaFileEventList, new MediaTableFormat());
    // tableFiles = new JTable(mediaFileTableModel);
    tableFiles = new ZebraJTable(mediaFileTableModel);
    tableFiles.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

    // scrollPaneFiles = new JScrollPane();
    scrollPaneFiles = ZebraJTable.createStripedJScrollPane(tableFiles);
    add(scrollPaneFiles, "4, 6, 5, 1, fill, fill");

    lblFilesT.setLabelFor(tableFiles);
    scrollPaneFiles.setViewportView(tableFiles);

    initDataBindings();

    // adjust table
    tableColumnAdjuster = new TableColumnAdjuster(tableFiles);
    tableColumnAdjuster.setColumnDataIncluded(true);
    tableColumnAdjuster.setColumnHeaderIncluded(true);
    tableColumnAdjuster.setOnlyAdjustLarger(false);
    // tableColumnAdjuster.setDynamicAdjustment(true);

    // install the propertychangelistener
    PropertyChangeListener propertyChangeListener = new PropertyChangeListener() {
      public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        String property = propertyChangeEvent.getPropertyName();
        Object source = propertyChangeEvent.getSource();
        // react on selection of a movie and change of media files
        if ((source.getClass() == TvShowEpisodeSelectionModel.class && "selectedTvShowEpisode".equals(property))
            || (source.getClass() == TvShowEpisode.class && "mediaFiles".equals(property))) {
          mediaFileEventList.clear();
          mediaFileEventList.addAll(selectionModel.getSelectedTvShowEpisode().getMediaFiles());
          tableColumnAdjuster.adjustColumns();
        }

      }
    };

    selectionModel.addPropertyChangeListener(propertyChangeListener);
  }

  /**
   * Inits the data bindings.
   */
  protected void initDataBindings() {
    BeanProperty<TvShowEpisodeSelectionModel, Integer> tvShowEpisodeSelectionModelBeanProperty = BeanProperty
        .create("selectedTvShowEpisode.dateAdded.date");
    BeanProperty<JLabel, String> jLabelBeanProperty = BeanProperty.create("text");
    AutoBinding<TvShowEpisodeSelectionModel, Integer, JLabel, String> autoBinding = Bindings.createAutoBinding(UpdateStrategy.READ, selectionModel,
        tvShowEpisodeSelectionModelBeanProperty, lblDateAdded, jLabelBeanProperty);
    autoBinding.bind();
    //
    BeanProperty<TvShowEpisodeSelectionModel, Boolean> tvShowEpisodeSelectionModelBeanProperty_1 = BeanProperty
        .create("selectedTvShowEpisode.watched");
    BeanProperty<JCheckBox, Boolean> jCheckBoxBeanProperty = BeanProperty.create("selected");
    AutoBinding<TvShowEpisodeSelectionModel, Boolean, JCheckBox, Boolean> autoBinding_1 = Bindings.createAutoBinding(UpdateStrategy.READ,
        selectionModel, tvShowEpisodeSelectionModelBeanProperty_1, cbWatched, jCheckBoxBeanProperty);
    autoBinding_1.bind();
    //
    BeanProperty<TvShowEpisodeSelectionModel, Integer> tvShowEpisodeSelectionModelBeanProperty_2 = BeanProperty
        .create("selectedTvShowEpisode.dateAdded.day");
    AutoBinding<TvShowEpisodeSelectionModel, Integer, JLabel, String> autoBinding_2 = Bindings.createAutoBinding(UpdateStrategy.READ, selectionModel,
        tvShowEpisodeSelectionModelBeanProperty_2, lblDateAdded, jLabelBeanProperty);
    autoBinding_2.bind();
    //
    BeanProperty<TvShowEpisodeSelectionModel, String> tvShowEpisodeSelectionModelBeanProperty_3 = BeanProperty
        .create("selectedTvShowEpisode.dateAddedAsString");
    AutoBinding<TvShowEpisodeSelectionModel, String, JLabel, String> autoBinding_3 = Bindings.createAutoBinding(UpdateStrategy.READ, selectionModel,
        tvShowEpisodeSelectionModelBeanProperty_3, lblDateAdded, jLabelBeanProperty);
    autoBinding_3.bind();
    //
    BeanProperty<TvShowEpisodeSelectionModel, String> tvShowEpisodeSelectionModelBeanProperty_13 = BeanProperty.create("selectedTvShowEpisode.path");
    AutoBinding<TvShowEpisodeSelectionModel, String, JLabel, String> autoBinding_19 = Bindings.createAutoBinding(UpdateStrategy.READ, selectionModel,
        tvShowEpisodeSelectionModelBeanProperty_13, lblEpisodePath, jLabelBeanProperty);
    autoBinding_19.bind();
  }

  /**
   * The Class MediaTableFormat.
   * 
   * @author Manuel Laggner
   */
  private static class MediaTableFormat implements AdvancedTableFormat<MediaFile> {

    /*
     * (non-Javadoc)
     * 
     * @see ca.odell.glazedlists.gui.TableFormat#getColumnCount()
     */
    @Override
    public int getColumnCount() {
      return 8;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ca.odell.glazedlists.gui.TableFormat#getColumnName(int)
     */
    @Override
    public String getColumnName(int column) {
      switch (column) {
        case 0:
          return BUNDLE.getString("metatag.filename"); //$NON-NLS-1$

        case 1:
          return BUNDLE.getString("metatag.size"); //$NON-NLS-1$

        case 2:
          return BUNDLE.getString("metatag.runtime"); //$NON-NLS-1$

        case 3:
          return BUNDLE.getString("metatag.videocodec"); //$NON-NLS-1$

        case 4:
          return BUNDLE.getString("metatag.resolution"); //$NON-NLS-1$

        case 5:
          return BUNDLE.getString("metatag.videobitrate"); //$NON-NLS-1$

        case 6:
          return BUNDLE.getString("metatag.audiocodec"); //$NON-NLS-1$

        case 7:
          return BUNDLE.getString("metatag.audiochannels"); //$NON-NLS-1$

      }

      throw new IllegalStateException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see ca.odell.glazedlists.gui.TableFormat#getColumnValue(java.lang.Object, int)
     */
    @Override
    public Object getColumnValue(MediaFile mediaFile, int column) {
      switch (column) {
        case 0:
          return mediaFile.getFilename();

        case 1:
          return mediaFile.getFilesizeInMegabytes();

        case 2:
          return mediaFile.getDurationHM();

        case 3:
          return mediaFile.getVideoCodec();

        case 4:
          return mediaFile.getVideoResolution();

        case 5:
          return mediaFile.getBiteRateInKbps();

        case 6:
          return mediaFile.getAudioCodec();

        case 7:
          return mediaFile.getAudioChannels();
      }

      throw new IllegalStateException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see ca.odell.glazedlists.gui.AdvancedTableFormat#getColumnClass(int)
     */
    @SuppressWarnings("rawtypes")
    @Override
    public Class getColumnClass(int column) {
      switch (column) {
        case 0:
        case 1:
        case 2:
        case 3:
        case 4:
        case 5:
        case 6:
        case 7:
          return String.class;
      }

      throw new IllegalStateException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see ca.odell.glazedlists.gui.AdvancedTableFormat#getColumnComparator(int)
     */
    @SuppressWarnings("rawtypes")
    @Override
    public Comparator getColumnComparator(int arg0) {
      return null;
    }

  }
}