package org.xpdojo.elastic.movie;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/movie")
@RestController
public class MovieController {

    @GetMapping("/search")
    public String search() {
        return "movie";
    }

}
