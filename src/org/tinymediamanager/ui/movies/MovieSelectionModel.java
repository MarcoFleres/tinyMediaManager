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
package org.tinymediamanager.ui.movies;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.tinymediamanager.core.AbstractModelObject;
import org.tinymediamanager.core.movie.entities.Movie;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.swing.DefaultEventSelectionModel;
import ca.odell.glazedlists.swing.TableComparatorChooser;

/**
 * The Class MovieSelectionModel.
 * 
 * @author Manuel Laggner
 */
public class MovieSelectionModel extends AbstractModelObject implements ListSelectionListener {

  /** The Constant SELECTED_MOVIE. */
  private static final String               SELECTED_MOVIE = "selectedMovie";

  /** The selected movies. */
  private List<Movie>                       selectedMovies;

  /** The selected movie. */
  private Movie                             selectedMovie;

  /** The initial movie. */
  private Movie                             initialMovie   = new Movie();

  /** The selection model. */
  private DefaultEventSelectionModel<Movie> selectionModel;

  /** The matcher editor. */
  private MovieMatcherEditor                matcherEditor;

  /** The table comparator chooser. */
  private TableComparatorChooser<Movie>     tableComparatorChooser;

  /** The sorted list. */
  private SortedList<Movie>                 sortedList;

  /** The property change listener. */
  private PropertyChangeListener            propertyChangeListener;

  public MovieSelectionModel(SortedList<Movie> sortedList, EventList<Movie> source, MovieMatcherEditor matcher) {
    this.sortedList = sortedList;
    this.selectionModel = new DefaultEventSelectionModel<Movie>(source);
    this.selectionModel.addListSelectionListener(this);
    this.matcherEditor = matcher;
    this.selectedMovies = selectionModel.getSelected();

    propertyChangeListener = new PropertyChangeListener() {
      @Override
      public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getSource() == selectedMovie) {
          firePropertyChange(evt);
        }
      }
    };
  }

  public MovieSelectionModel() {

  }

  public void setSelectedMovie(Movie movie) {
    Movie oldValue = this.selectedMovie;
    if (movie == null) {
      this.selectedMovie = initialMovie;
    }
    else {
      this.selectedMovie = movie;
    }

    if (oldValue != null) {
      oldValue.removePropertyChangeListener(propertyChangeListener);
    }

    if (selectedMovie != null) {
      selectedMovie.addPropertyChangeListener(propertyChangeListener);
    }

    firePropertyChange(SELECTED_MOVIE, oldValue, selectedMovie);
  }

  public MovieMatcherEditor getMatcherEditor() {
    return matcherEditor;
  }

  public DefaultEventSelectionModel<Movie> getSelectionModel() {
    return selectionModel;
  }

  @Override
  public void valueChanged(ListSelectionEvent e) {
    if (e.getValueIsAdjusting()) {
      return;
    }

    // display first selected movie
    if (selectedMovies.size() > 0 && selectedMovie != selectedMovies.get(0)) {
      Movie oldValue = selectedMovie;
      selectedMovie = selectedMovies.get(0);

      // unregister propertychangelistener
      if (oldValue != null && oldValue != initialMovie) {
        oldValue.removePropertyChangeListener(propertyChangeListener);
      }
      if (selectedMovie != null && selectedMovie != initialMovie) {
        selectedMovie.addPropertyChangeListener(propertyChangeListener);
      }
      firePropertyChange(SELECTED_MOVIE, oldValue, selectedMovie);
    }

    // display empty movie (i.e. when all movies are removed from the list)
    if (selectedMovies.size() == 0) {
      Movie oldValue = selectedMovie;
      selectedMovie = initialMovie;
      // unregister propertychangelistener
      if (oldValue != null && oldValue != initialMovie) {
        oldValue.removePropertyChangeListener(propertyChangeListener);
      }
      firePropertyChange(SELECTED_MOVIE, oldValue, selectedMovie);
    }
  }

  public Movie getSelectedMovie() {
    return selectedMovie;
  }

  public List<Movie> getSelectedMovies() {
    return selectedMovies;
  }

  public void setSelectedMovies(List<Movie> selectedMovies) {
    this.selectedMovies = selectedMovies;
  }

  public void filterMovies(HashMap<MoviesExtendedMatcher.SearchOptions, Object> filter) {
    matcherEditor.filterMovies(filter);
  }

  public TableComparatorChooser<Movie> getTableComparatorChooser() {
    return tableComparatorChooser;
  }

  public void setTableComparatorChooser(TableComparatorChooser<Movie> tableComparatorChooser) {
    this.tableComparatorChooser = tableComparatorChooser;
  }

  public void sortMovies(MovieExtendedComparator.SortColumn column, boolean ascending) {
    Comparator<Movie> comparator = new MovieExtendedComparator(column, ascending);
    sortedList.setComparator(comparator);
  }
}