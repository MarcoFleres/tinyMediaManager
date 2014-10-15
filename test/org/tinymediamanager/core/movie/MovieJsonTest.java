package org.tinymediamanager.core.movie;

import org.junit.Assert;
import org.junit.Test;
import org.tinymediamanager.core.movie.entities.Movie;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;

public class MovieJsonTest {

  @Test
  public void TestMovieToJSON() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(MapperFeature.AUTO_DETECT_GETTERS, false);
    mapper.configure(MapperFeature.AUTO_DETECT_IS_GETTERS, false);
    mapper.configure(MapperFeature.AUTO_DETECT_SETTERS, false);
    mapper.configure(MapperFeature.AUTO_DETECT_FIELDS, false);
    mapper.setSerializationInclusion(Include.NON_NULL);
    mapper.setSerializationInclusion(Include.NON_EMPTY);
    ObjectWriter writer = mapper.writerWithType(Movie.class);
    try {
      Movie movie = new Movie();

      movie.setTitle("Moviename");
      movie.setOriginalTitle("Original Moviename");
      movie.setYear("2012");
      movie.setRating(8.3f);
      movie.setPath("/media/movies/Moviename");

      long start = System.currentTimeMillis();
      String json = writer.writeValueAsString(movie);
      System.out.println(json);

      ObjectReader reader = mapper.reader(Movie.class);
      Movie movie2 = reader.readValue(json);

      long end = System.currentTimeMillis();
      System.out.println(end - start);

      Assert.assertEquals(movie.getTitle(), movie2.getTitle());
      Assert.assertEquals(movie.getOriginalTitle(), movie2.getOriginalTitle());
      Assert.assertEquals(movie.getRating(), movie2.getRating(), 0f);
      Assert.assertEquals(movie.getPath(), movie2.getPath());

    }
    catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      Assert.fail();
    }
  }

}
