/*
 * You can use the following import statements
 *
 * import org.springframework.beans.factory.annotation.Autowired;
 * import org.springframework.http.HttpStatus;
 * import org.springframework.jdbc.core.JdbcTemplate;
 * import org.springframework.stereotype.Service;
 * import org.springframework.web.server.ResponseStatusException;
 * import java.util.*;
 *
 */

// Write your code 

package com.example.movie.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.*;
import com.example.movie.model.Movie;
import com.example.movie.repository.MovieRepository;
import com.example.movie.model.MovieRowMapper;

@Service 
public class MovieH2Service implements MovieRepository {
    @Autowired 
    private JdbcTemplate db;
    @Override 
    public ArrayList <Movie> getMovies() {
        List <Movie> movieList=db.query("select * from movielist", new MovieRowMapper());
        ArrayList<Movie> movies=new ArrayList<>(movieList);
        return movies;
    }
    @Override 
    public Movie getMovieById(int movieId) {
        try{
            Movie movie=db.queryForObject("select * from movielist where movieId=?", new MovieRowMapper(),movieId);
            return movie;
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
    @Override 
    public Movie addMovie(Movie movie) {
        db.update("insert into movielist (movieName,leadActor) values(?,?)", movie.getMovieName(),movie.getLeadActor());
        Movie Savedmovie=db.queryForObject("select * From movielist Where movieName=? and leadActor=?", new MovieRowMapper(), movie.getMovieName(),movie.getLeadActor());
        return Savedmovie;
    }

    @Override
    public Movie updateMovie(int movieId,Movie movie) {
        if(movie.getMovieName()!=null) {
            db.update("update movielist set movieName=? where movieId=?",movie.getMovieName(),movieId);
        }
        if(movie.getLeadActor()!=null) {
            db.update("update movielist set leadActor=? where movieId=?",movie.getLeadActor(),movieId);
        }
        return getMovieById(movieId);
    }
    @Override
    public void deleteMovie(int movieId) {
        db.update("delete from movielist where movieId=?",movieId);
    }
}


