/*
 * Copyright 2012 - 2015 Manuel Laggner
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
package org.tinymediamanager.scraper.moviemeternl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinymediamanager.core.Constants;
import org.tinymediamanager.scraper.IMediaMetadataProvider;
import org.tinymediamanager.scraper.MediaCastMember;
import org.tinymediamanager.scraper.MediaCastMember.CastType;
import org.tinymediamanager.scraper.MediaGenres;
import org.tinymediamanager.scraper.MediaMetadata;
import org.tinymediamanager.scraper.MediaProviderInfo;
import org.tinymediamanager.scraper.MediaScrapeOptions;
import org.tinymediamanager.scraper.MediaSearchOptions;
import org.tinymediamanager.scraper.MediaSearchResult;
import org.tinymediamanager.scraper.MediaType;
import org.tinymediamanager.scraper.MetadataUtil;
import org.tinymediamanager.scraper.moviemeternl.model.Film;
import org.tinymediamanager.scraper.moviemeternl.model.FilmDetail;
import org.tinymediamanager.scraper.moviemeternl.model.FilmDetail.Actor;
import org.tinymediamanager.scraper.moviemeternl.model.FilmDetail.Date;
import org.tinymediamanager.scraper.moviemeternl.model.FilmDetail.Director;
import org.tinymediamanager.scraper.moviemeternl.model.FilmDetail.Genre;

/**
 * The Class MoviemeterMetadataProvider. A meta data provider for the site moviemeter.nl
 * 
 * @author Myron Boyle (myron0815@gmx.net)
 */
public class MoviemeterMetadataProvider implements IMediaMetadataProvider {
  private static final Logger      LOGGER       = LoggerFactory.getLogger(MoviemeterMetadataProvider.class);

  private static MoviemeterApi     mmapi;
  private static MediaProviderInfo providerInfo = new MediaProviderInfo(Constants.MOVIEMETERID, "moviemeter.nl",
                                                    "Scraper for moviemeter.nl which is able to scrape movie metadata");

  public MoviemeterMetadataProvider() throws Exception {
    initAPI();
  }

  private static synchronized void initAPI() throws Exception {
    if (mmapi == null) {
      try {
        mmapi = new MoviemeterApi();
      }
      catch (Exception e) {
        LOGGER.error("MoviemeterMetadataProvider", e);
        throw e;
      }
    }
  }

  @Override
  public MediaProviderInfo getProviderInfo() {
    return providerInfo;
  }

  @Override
  public MediaMetadata getMetadata(MediaScrapeOptions options) throws Exception {
    LOGGER.debug("getMetadata() " + options.toString());
    // check if there is a md in the result
    if (options.getResult() != null && options.getResult().getMediaMetadata() != null) {
      LOGGER.debug("MovieMeter: getMetadata from cache: " + options.getResult());
      return options.getResult().getMediaMetadata();
    }

    // get ids to scrape
    MediaMetadata md = new MediaMetadata(providerInfo.getId());

    int mmId = 0;

    // mmId from searchResult
    if (options.getResult() != null) {
      mmId = Integer.parseInt(options.getResult().getId());// Constants.MOVIEMETERID));
    }

    // imdbid
    String imdbId = options.getImdbId();

    if (StringUtils.isBlank(imdbId) && mmId == 0) {
      LOGGER.warn("not possible to scrape from Moviemeter.bl - no mmId/imdbId found");
      return md;
    }

    // scrape
    FilmDetail fd = null;
    synchronized (mmapi) {
      if (mmId != 0) {
        LOGGER.debug("MovieMeter: getMetadata(mmId): " + mmId);
        fd = mmapi.filmDetail(mmId);
      }
      else if (StringUtils.isNotBlank(imdbId)) {
        LOGGER.debug("MovieMeter: filmSearchImdb(imdbId): " + imdbId);
        fd = mmapi.filmSearchImdb(imdbId);
      }
    }

    if (fd == null) {
      return md;
    }

    md.setId(MediaMetadata.IMDBID, "tt" + fd.getImdb());
    md.storeMetadata(MediaMetadata.TITLE, fd.getTitle());
    md.storeMetadata(MediaMetadata.YEAR, fd.getYear());
    md.storeMetadata(MediaMetadata.PLOT, fd.getPlot());
    md.storeMetadata(MediaMetadata.TAGLINE, fd.getPlot().length() > 150 ? fd.getPlot().substring(0, 150) : fd.getPlot());
    // md.setOriginalTitle(fd.getAlternative_titles());
    try {
      md.storeMetadata(MediaMetadata.RATING, Double.parseDouble(fd.getAverage()));
    }
    catch (Exception e) {
      md.storeMetadata(MediaMetadata.RATING, 0);
    }
    md.setId("moviemeter", fd.getFilmId());
    try {
      md.storeMetadata(MediaMetadata.RUNTIME, Integer.valueOf(fd.getDurations().get(0).duration));
    }
    catch (Exception e) {
      md.storeMetadata(MediaMetadata.RUNTIME, 0);
    }
    md.storeMetadata(MediaMetadata.VOTE_COUNT, Integer.valueOf(fd.getVotes_count()));
    for (Genre g : fd.getGenres()) {
      md.addGenre(getTmmGenre(g.getName()));
    }
    md.storeMetadata(MediaMetadata.POSTER_URL, fd.getThumbnail().replace(".50.jpg", ".jpg")); // full res
    ArrayList<Date> dateList = fd.getDates_cinema();
    if (dateList != null && dateList.size() > 0) {
      md.storeMetadata(MediaMetadata.RELEASE_DATE, dateList.get(0).getDate());
    }
    md.storeMetadata(MediaMetadata.COUNTRY, fd.getCountries_text());
    for (Actor a : fd.getActors()) {
      MediaCastMember cm = new MediaCastMember();
      cm.setName(a.getName());
      cm.setType(CastType.ACTOR);
      md.addCastMember(cm);
    }
    for (Director d : fd.getDirectors()) {
      MediaCastMember cm = new MediaCastMember();
      cm.setName(d.getName());
      cm.setType(CastType.DIRECTOR);
      md.addCastMember(cm);
    }
    return md;
  }

  @Override
  public List<MediaSearchResult> search(MediaSearchOptions query) throws Exception {
    LOGGER.debug("search() " + query.toString());
    List<MediaSearchResult> resultList = new ArrayList<MediaSearchResult>();
    String imdb = query.get(MediaSearchOptions.SearchParam.IMDBID);
    String searchString = "";
    String myear = query.get(MediaSearchOptions.SearchParam.YEAR);

    // check type
    if (query.getMediaType() != MediaType.MOVIE) {
      throw new Exception("wrong media type for this scraper");
    }

    if (StringUtils.isEmpty(searchString) && StringUtils.isNotEmpty(query.get(MediaSearchOptions.SearchParam.QUERY))) {
      searchString = query.get(MediaSearchOptions.SearchParam.QUERY);
    }

    if (StringUtils.isEmpty(searchString) && StringUtils.isNotEmpty(query.get(MediaSearchOptions.SearchParam.TITLE))) {
      searchString = query.get(MediaSearchOptions.SearchParam.TITLE);
    }

    if (StringUtils.isEmpty(searchString)) {
      LOGGER.debug("Moviemeter Scraper: empty searchString");
      return resultList;
    }

    searchString = MetadataUtil.removeNonSearchCharacters(searchString);

    if (MetadataUtil.isValidImdbId(searchString)) {
      // hej, our entered value was an IMDBid :)
      imdb = searchString;
    }

    List<Film> moviesFound = new ArrayList<Film>();
    FilmDetail fd = null;

    synchronized (mmapi) {
      // 1. "search" with IMDBid (get details, well)
      if (StringUtils.isNotEmpty(imdb)) {
        fd = mmapi.filmSearchImdb(imdb);
        LOGGER.debug("found result with IMDB id");
      }

      // 2. try with searchString
      if (fd == null) {
        moviesFound = mmapi.filmSearch(searchString);
        LOGGER.debug("found " + moviesFound.size() + " results");
      }
    }

    if (fd != null) { // imdb film detail page
      MediaSearchResult sr = new MediaSearchResult(providerInfo.getId());
      sr.setId(Integer.toString(fd.getFilmId()));
      sr.setIMDBId(imdb);
      sr.setTitle(fd.getTitle());
      sr.setUrl(fd.getUrl());
      sr.setYear(fd.getYear());
      sr.setScore(1);
      resultList.add(sr);
    }
    for (Film film : moviesFound) {
      MediaSearchResult sr = new MediaSearchResult(providerInfo.getId());
      sr.setId(film.getFilmId());
      sr.setIMDBId(imdb);
      sr.setTitle(film.getTitle());
      sr.setUrl(film.getUrl());
      sr.setYear(film.getYear());

      // compare score based on names
      float score = MetadataUtil.calculateScore(searchString, film.getTitle());

      if (myear != null && !myear.isEmpty() && !myear.equals("0") && !myear.equals(sr.getYear())) {
        LOGGER.debug("parsed year does not match search result year - downgrading score by 0.01");
        score = score - 0.01f;
      }
      sr.setScore(score);

      resultList.add(sr);
    }
    Collections.sort(resultList);
    Collections.reverse(resultList);

    return resultList;
  }

  /*
   * Maps scraper Genres to internal TMM genres
   */
  private MediaGenres getTmmGenre(String genre) {
    MediaGenres g = null;
    if (genre.isEmpty()) {
      return g;
    }
    // @formatter:off
    else if (genre.equals("Actie"))            { g = MediaGenres.ACTION; }
    else if (genre.equals("Animatie"))         { g = MediaGenres.ANIMATION; }
    else if (genre.equals("Avontuur"))         { g = MediaGenres.ADVENTURE; }
    else if (genre.equals("Documentaire"))     { g = MediaGenres.DOCUMENTARY; }
    else if (genre.equals("Drama"))            { g = MediaGenres.DRAMA; }
    else if (genre.equals("Erotiek"))          { g = MediaGenres.EROTIC; }
    else if (genre.equals("Familie"))          { g = MediaGenres.FAMILY; }
    else if (genre.equals("Fantasy"))          { g = MediaGenres.FANTASY; }
    else if (genre.equals("Film noir"))        { g = MediaGenres.FILM_NOIR; }
    else if (genre.equals("Horror"))           { g = MediaGenres.HORROR; }
    else if (genre.equals("Komedie"))          { g = MediaGenres.COMEDY; }
    else if (genre.equals("Misdaad"))          { g = MediaGenres.CRIME; }
    else if (genre.equals("Muziek"))           { g = MediaGenres.MUSIC; }
    else if (genre.equals("Mystery"))          { g = MediaGenres.MYSTERY; }
    else if (genre.equals("Oorlog"))           { g = MediaGenres.WAR; }
    else if (genre.equals("Roadmovie"))        { g = MediaGenres.ROAD_MOVIE; }
    else if (genre.equals("Romantiek"))        { g = MediaGenres.ROMANCE; }
    else if (genre.equals("Sciencefiction"))   { g = MediaGenres.SCIENCE_FICTION; }
    else if (genre.equals("Thriller"))         { g = MediaGenres.THRILLER; }
    else if (genre.equals("Western"))          { g = MediaGenres.WESTERN; }
    // @formatter:on
    if (g == null) {
      g = MediaGenres.getGenre(genre);
    }
    return g;
  }
}
