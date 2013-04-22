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
package org.tinymediamanager.core.tvshow.tasks;

import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinymediamanager.core.tvshow.TvShowEpisode;
import org.tinymediamanager.core.tvshow.TvShowList;
import org.tinymediamanager.scraper.IMediaMetadataProvider;
import org.tinymediamanager.scraper.MediaMetadata;
import org.tinymediamanager.scraper.MediaScrapeOptions;
import org.tinymediamanager.scraper.MediaType;

/**
 * @author Manuel Laggner
 * 
 */
public class TvShowEpisodeScrapeTask implements Runnable {

  private static final Logger          LOGGER           = LoggerFactory.getLogger(TvShowEpisodeScrapeTask.class);

  private final List<TvShowEpisode>    episodes;

  private final IMediaMetadataProvider metadataProvider = TvShowList.getInstance().getMetadataProvider();

  public TvShowEpisodeScrapeTask(List<TvShowEpisode> episodes) {
    this.episodes = episodes;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Runnable#run()
   */
  @Override
  public void run() {
    for (TvShowEpisode episode : episodes) {
      MediaScrapeOptions options = new MediaScrapeOptions();
      for (Entry<String, Object> entry : episode.getTvShow().getIds().entrySet()) {
        options.setId(entry.getKey(), entry.getValue().toString());
      }

      options.setType(MediaType.TV_EPISODE);
      options.setId("seasonNr", String.valueOf(episode.getSeason()));
      options.setId("episodeNr", String.valueOf(episode.getEpisode()));

      try {
        MediaMetadata metadata = metadataProvider.getMetadata(options);
        if (StringUtils.isNotBlank(metadata.getTitle())) {
          episode.setMetadata(metadata);
        }
      }
      catch (Exception e) {
        LOGGER.warn("Error getting metadata " + e.getMessage());
      }
    }
  }

}