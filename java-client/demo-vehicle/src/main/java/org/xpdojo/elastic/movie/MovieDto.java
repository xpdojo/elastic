package org.xpdojo.elastic.movie;

/**
 * <ol>
 *     <li>ObjectMapper에서 Getter를 사용한다.</li>
 *     <li><code>@RequestBody</code>에서 Constructor를 사용한다.</li>
 * </ol>
 */
public class MovieDto {
    private String title;
    private String year;
    private String director;

    public MovieDto() {
    }

    public MovieDto(String title, String year, String director) {
        this.title = title;
        this.year = year;
        this.director = director;
    }

    public String getTitle() {
        return title;
    }

    public String getYear() {
        return year;
    }

    public String getDirector() {
        return director;
    }
}
