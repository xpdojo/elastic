package org.xpdojo.aggregation.dto;

public class Option {

    private String id;
    private String name;
    private long count;

    public Option(String id, String name, long count) {
        this.id = id;
        this.name = name;
        this.count = count;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getCount() {
        return count;
    }
}
