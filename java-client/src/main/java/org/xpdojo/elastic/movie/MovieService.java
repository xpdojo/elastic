package org.xpdojo.elastic.movie;

import org.springframework.stereotype.Service;

@Service
public class MovieService {

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public void save(Movie movie) {
        movieRepository.save(movie);
    }

    public Iterable<Movie> findAll() {
        return movieRepository.findAll();
    }

    public Iterable<Movie> findByTitle(String keyword) {
        return movieRepository.findByTitle(keyword);
    }
}
