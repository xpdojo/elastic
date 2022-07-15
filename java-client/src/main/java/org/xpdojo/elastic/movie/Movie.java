package org.xpdojo.elastic.movie;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "movies", createIndex = true)
@ToString
@Getter
@Setter
public class Movie {

    @Id
    private String id;

    @Field(type = FieldType.Text, analyzer = "english")
    private String title;

    @Field(type = FieldType.Integer)
    private String year;

    @Field(type = FieldType.Text)
    private String director;
    // private String[] genres;
    // private String[] actors;

    public Movie() {
    }

    @Builder
    public Movie(String title, String year, String director) {
        this.title = title;
        this.year = year;
        this.director = director;
    }
}
