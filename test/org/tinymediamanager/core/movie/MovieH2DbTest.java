package org.tinymediamanager.core.movie;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;
import org.tinymediamanager.core.movie.entities.Movie;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class MovieH2DbTest {

  @Test
  public void TestMovieInsert() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(MapperFeature.AUTO_DETECT_GETTERS, false);
    mapper.configure(MapperFeature.AUTO_DETECT_IS_GETTERS, false);
    mapper.configure(MapperFeature.AUTO_DETECT_SETTERS, false);
    mapper.configure(MapperFeature.AUTO_DETECT_FIELDS, false);
    ObjectWriter writer = mapper.writerWithType(Movie.class);
    try {

      Class.forName("org.h2.Driver");
      Connection connection = DriverManager.getConnection("jdbc:h2:./testcase", "", "");

      long start = System.currentTimeMillis();
      int i = 0;
      while (i < 1000) {
        Movie movie = new Movie();
        UUID uuid = UUID.randomUUID();

        movie.setTitle("Moviename");
        movie.setOriginalTitle("Original Moviename");
        movie.setYear("2012");
        movie.setRating(8.3f);
        movie.setPath("/media/movies/Moviename");

        String json = writer.writeValueAsString(movie);

        Statement stat = connection.createStatement();
        stat.execute("create table if not exists movie(id uuid primary key, data clob)");

        PreparedStatement pstat = connection.prepareStatement("insert into movie values(?,?)");
        pstat.setString(1, uuid.toString());
        pstat.setString(2, json);
        pstat.executeUpdate();

        connection.commit();
        i++;
      }
      long end = System.currentTimeMillis();
      System.out.println(end - start);

      connection.close();

    }
    catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      Assert.fail();
    }
  }
}
