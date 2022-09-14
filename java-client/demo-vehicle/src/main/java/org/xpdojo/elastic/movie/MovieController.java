package org.xpdojo.elastic.movie;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/movies")
@RestController
public class MovieController {

    private final ObjectMapper objectMapper;
    private final MovieService movieService;

    public MovieController(ObjectMapper objectMapper, MovieService movieService) {
        this.objectMapper = objectMapper;
        this.movieService = movieService;
    }

    @PostMapping
    public MovieDto save(@RequestBody MovieDto movieDto) {
        Movie movie = objectMapper.convertValue(movieDto, Movie.class);
        movieService.save(movie);
        return movieDto;
    }

    @GetMapping
    public Iterable<Movie> listMovies() {
        return movieService.findAll();
    }

    @GetMapping("/search/{keyword}")
    public Iterable<Movie> findMoviesByTitle(@PathVariable("keyword") String keyword) {
        return movieService.findByTitle(keyword);
    }

}
