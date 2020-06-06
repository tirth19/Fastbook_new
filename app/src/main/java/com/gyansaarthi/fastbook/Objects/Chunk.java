package com.gyansaarthi.fastbook.Objects;

public class Chunk {
    private String heading, contentText;

    public Chunk(String heading, String contentText) {
        this.heading = heading;
        this.contentText = contentText;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getContentText() {
        return contentText;
    }

    public void setContentText(String contentText) {
        this.contentText = contentText;
    }
}
