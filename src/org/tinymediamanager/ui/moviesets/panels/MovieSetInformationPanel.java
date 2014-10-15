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
package org.tinymediamanager.ui.moviesets.panels;

import static org.tinymediamanager.core.Constants.*;

import java.awt.Dimension;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Comparator;
import java.util.ResourceBundle;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextPane;

import org.jdesktop.beansbinding.AutoBinding;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.Bindings;
import org.tinymediamanager.core.MediaFileType;
import org.tinymediamanager.core.movie.entities.Movie;
import org.tinymediamanager.core.movie.entities.MovieSet;
import org.tinymediamanager.ui.ColumnLayout;
import org.tinymediamanager.ui.TmmFontHelper;
import org.tinymediamanager.ui.UTF8Control;
import org.tinymediamanager.ui.components.ColumnImageLabel;
import org.tinymediamanager.ui.components.TmmTable;
import org.tinymediamanager.ui.moviesets.MovieSetSelectionModel;

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
 * The Class MovieSetInformationPanel.
 * 
 * @author Manuel Laggner
 */
public class MovieSetInformationPanel extends JPanel {
  private static final ResourceBundle   BUNDLE           = ResourceBundle.getBundle("messages", new UTF8Control()); //$NON-NLS-1$
  private static final long             serialVersionUID = -8166784589262658147L;

  private MovieSetSelectionModel        selectionModel;
  private EventList<Movie>              movieEventList;
  private DefaultEventTableModel<Movie> movieTableModel  = null;

  private JLabel                        lblMovieSetName;
  private JTable                        tableAssignedMovies;
  private ColumnImageLabel              lblMovieSetPoster;
  private JLabel                        lblFanartSize;
  private JLabel                        lblPosterSize;
  private JPanel                        panelLeft;
  private ColumnImageLabel              lblMovieSetFanart;
  private JScrollPane                   scrollPaneOverview;
  private JTextPane                     tpOverview;
  private JPanel                        panelRight;
  private JSplitPane                    splitPane;
  private JPanel                        panelTop;
  private JPanel                        panelBottom;

  /**
   * Instantiates a new movie set information panel.
   * 
   * @param model
   *          the model
   */
  public MovieSetInformationPanel(MovieSetSelectionModel model) {
    this.selectionModel = model;
    movieEventList = new ObservableElementList<Movie>(GlazedLists.threadSafeList(new BasicEventList<Movie>()), GlazedLists.beanConnector(Movie.class));
    movieTableModel = new DefaultEventTableModel<Movie>(GlazedListsSwing.swingThreadProxyList(movieEventList), new MovieInMovieSetTableFormat());

    setLayout(new FormLayout(new ColumnSpec[] { FormFactory.UNRELATED_GAP_COLSPEC, ColumnSpec.decode("200px:grow"),
        FormFactory.UNRELATED_GAP_COLSPEC, ColumnSpec.decode("500px:grow(3)"), FormFactory.UNRELATED_GAP_COLSPEC, }, new RowSpec[] {
        FormFactory.PARAGRAPH_GAP_ROWSPEC, RowSpec.decode("90px:grow"), FormFactory.PARAGRAPH_GAP_ROWSPEC, }));

    panelLeft = new JPanel();
    panelLeft.setLayout(new ColumnLayout());

    lblMovieSetPoster = new ColumnImageLabel(false);
    lblMovieSetPoster.setDefaultAspectRatio(2 / 3f);
    panelLeft.add(lblMovieSetPoster);
    lblMovieSetPoster.setAlternativeText(BUNDLE.getString("image.notfound.poster")); //$NON-NLS-1$
    lblMovieSetPoster.enableLightbox();
    lblPosterSize = new JLabel(BUNDLE.getString("mediafiletype.poster")); //$NON-NLS-1$
    panelLeft.add(lblPosterSize);
    panelLeft.add(Box.createVerticalStrut(20));

    lblMovieSetFanart = new ColumnImageLabel(false);
    lblMovieSetFanart.setDefaultAspectRatio(9 / 16f);
    panelLeft.add(lblMovieSetFanart);
    lblMovieSetFanart.setAlternativeText(BUNDLE.getString("image.notfound.fanart"));
    lblMovieSetFanart.enableLightbox();
    lblFanartSize = new JLabel(BUNDLE.getString("mediafiletype.fanart")); //$NON-NLS-1$
    panelLeft.add(lblFanartSize);

    JScrollPane scrollPaneLeft = new JScrollPane(panelLeft);
    scrollPaneLeft.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    scrollPaneLeft.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    add(scrollPaneLeft, "2, 2, fill, fill");

    panelRight = new JPanel();
    add(panelRight, "4, 2, fill, fill");
    panelRight
        .setLayout(new FormLayout(new ColumnSpec[] { ColumnSpec.decode("200px:grow"), }, new RowSpec[] { RowSpec.decode("fill:default:grow"), }));

    splitPane = new JSplitPane();
    splitPane.setResizeWeight(0.5);
    splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
    panelRight.add(splitPane, "1, 1, fill, fill");

    panelTop = new JPanel();
    splitPane.setLeftComponent(panelTop);
    panelTop.setLayout(new FormLayout(new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("200px:grow"),
        FormFactory.RELATED_GAP_COLSPEC, }, new RowSpec[] { FormFactory.DEFAULT_ROWSPEC, FormFactory.NARROW_LINE_GAP_ROWSPEC,
        FormFactory.DEFAULT_ROWSPEC, FormFactory.NARROW_LINE_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.NARROW_LINE_GAP_ROWSPEC,
        RowSpec.decode("top:50px:grow(2)"), }));

    lblMovieSetName = new JLabel("");
    panelTop.add(lblMovieSetName, "2, 1");
    TmmFontHelper.changeFont(lblMovieSetName, 1.5, Font.BOLD);

    JSeparator separator = new JSeparator();
    panelTop.add(separator, "2, 3");

    JLabel lblOverview = new JLabel(BUNDLE.getString("metatag.plot"));
    TmmFontHelper.changeFont(lblOverview, Font.BOLD);
    panelTop.add(lblOverview, "2, 5");

    scrollPaneOverview = new JScrollPane();
    panelTop.add(scrollPaneOverview, "2, 7, fill, fill");

    tpOverview = new JTextPane();
    tpOverview.setEditable(false);
    tpOverview.setOpaque(false);
    scrollPaneOverview.setViewportView(tpOverview);

    panelBottom = new JPanel();
    splitPane.setRightComponent(panelBottom);
    panelBottom.setLayout(new FormLayout(new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("200px:grow"),
        FormFactory.RELATED_GAP_COLSPEC, }, new RowSpec[] { FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("min:grow"),
        FormFactory.RELATED_GAP_ROWSPEC, }));
    tableAssignedMovies = new TmmTable(movieTableModel);
    JScrollPane scrollPaneMovies = TmmTable.createJScrollPane(tableAssignedMovies, new int[] {});
    tableAssignedMovies.setPreferredScrollableViewportSize(new Dimension(450, 200));
    scrollPaneMovies.setViewportView(tableAssignedMovies);
    panelBottom.add(scrollPaneMovies, "2, 2, fill, fill");

    // adjust table columns
    // year column
    int width = tableAssignedMovies.getFontMetrics(tableAssignedMovies.getFont()).stringWidth(" 2000");
    int titleWidth = tableAssignedMovies.getFontMetrics(tableAssignedMovies.getFont()).stringWidth(BUNDLE.getString("metatag.year")); //$NON-NLS-1$
    if (titleWidth > width) {
      width = titleWidth;
    }
    tableAssignedMovies.getTableHeader().getColumnModel().getColumn(1).setPreferredWidth(width);
    tableAssignedMovies.getTableHeader().getColumnModel().getColumn(1).setMinWidth(width);
    tableAssignedMovies.getTableHeader().getColumnModel().getColumn(1).setMaxWidth((int) (width * 1.5));

    // watched column
    tableAssignedMovies.getTableHeader().getColumnModel().getColumn(2).setPreferredWidth(70);
    tableAssignedMovies.getTableHeader().getColumnModel().getColumn(2).setMinWidth(70);
    tableAssignedMovies.getTableHeader().getColumnModel().getColumn(2).setMaxWidth(85);

    initDataBindings();

    // install the propertychangelistener
    PropertyChangeListener propertyChangeListener = new PropertyChangeListener() {
      @Override
      public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        String property = propertyChangeEvent.getPropertyName();
        Object source = propertyChangeEvent.getSource();
        // react on selection of a movie and change of media files
        if ((source.getClass() == MovieSetSelectionModel.class && "selectedMovieSet".equals(property))
            || (source.getClass() == MovieSet.class && "movies".equals(property))) {
          movieEventList.clear();
          movieEventList.addAll(selectionModel.getSelectedMovieSet().getMovies());
          lblMovieSetFanart.setImagePath(selectionModel.getSelectedMovieSet().getArtworkFilename(MediaFileType.FANART));
          lblMovieSetPoster.setImagePath(selectionModel.getSelectedMovieSet().getArtworkFilename(MediaFileType.POSTER));
        }

        // react on changes of the images
        if ((source.getClass() == MovieSet.class && FANART.equals(property))) {
          MovieSet movieSet = (MovieSet) source;
          lblMovieSetFanart.clearImage();
          lblMovieSetFanart.setImagePath(movieSet.getArtworkFilename(MediaFileType.FANART));
        }
        if ((source.getClass() == MovieSet.class && POSTER.equals(property))) {
          MovieSet movieSet = (MovieSet) source;
          lblMovieSetPoster.clearImage();
          lblMovieSetPoster.setImagePath(movieSet.getArtworkFilename(MediaFileType.POSTER));
        }
      }
    };

    selectionModel.addPropertyChangeListener(propertyChangeListener);
  }

  private static class MovieInMovieSetTableFormat implements AdvancedTableFormat<Movie> {
    @Override
    public int getColumnCount() {
      return 3;
    }

    @Override
    public String getColumnName(int column) {
      switch (column) {
        case 0:
          return BUNDLE.getString("movieset.parts"); //$NON-NLS-1$

        case 1:
          return BUNDLE.getString("metatag.year"); //$NON-NLS-1$

        case 2:
          return BUNDLE.getString("metatag.watched"); //$NON-NLS-1$
      }

      throw new IllegalStateException();
    }

    @Override
    public Object getColumnValue(Movie movie, int column) {
      switch (column) {
        case 0:
          return movie.getTitle();

        case 1:
          return movie.getYear();

        case 2:
          return movie.isWatched();
      }
      throw new IllegalStateException();
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Class getColumnClass(int column) {
      switch (column) {
        case 0:
        case 1:
          return String.class;

        case 2:
          return Boolean.class;
      }
      throw new IllegalStateException();
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Comparator getColumnComparator(int arg0) {
      return null;
    }

  }

  protected void initDataBindings() {
    BeanProperty<MovieSetSelectionModel, String> movieSetSelectionModelBeanProperty = BeanProperty.create("selectedMovieSet.title");
    BeanProperty<JLabel, String> jLabelBeanProperty = BeanProperty.create("text");
    AutoBinding<MovieSetSelectionModel, String, JLabel, String> autoBinding = Bindings.createAutoBinding(UpdateStrategy.READ, selectionModel,
        movieSetSelectionModelBeanProperty, lblMovieSetName, jLabelBeanProperty);
    autoBinding.bind();
    //
    BeanProperty<MovieSetSelectionModel, String> movieSetSelectionModelBeanProperty_4 = BeanProperty.create("selectedMovieSet.plot");
    BeanProperty<JTextPane, String> jTextPaneBeanProperty = BeanProperty.create("text");
    AutoBinding<MovieSetSelectionModel, String, JTextPane, String> autoBinding_3 = Bindings.createAutoBinding(UpdateStrategy.READ, selectionModel,
        movieSetSelectionModelBeanProperty_4, tpOverview, jTextPaneBeanProperty);
    autoBinding_3.bind();
  }
}
