package com.gyansaarthi.fastbook.Objects;

public class Achievement {
    private String title, description, achieved, target, thumbnail;

    public Achievement(String title, String description, String achieved, String target, String thumbnail) {
        this.title = title;
        this.description = description;
        this.achieved = achieved;
        this.target = target;
        this.thumbnail = thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAchieved() {
        return achieved;
    }

    public void setAchieved(String achieved) {
        this.achieved = achieved;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
