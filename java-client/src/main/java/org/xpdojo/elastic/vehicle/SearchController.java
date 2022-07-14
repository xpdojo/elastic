package org.xpdojo.elastic.vehicle;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xpdojo.elastic.vehicle.model.CodeSet;

import java.io.IOException;

@RequestMapping("/vehicle")
@RestController
public class SearchController {

    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping("/search")
    public CodeSet search() throws IOException {
        return searchService.search();
    }

}
