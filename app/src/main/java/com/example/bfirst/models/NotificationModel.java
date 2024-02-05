package com.example.bfirst.models;

public class NotificationModel{
    private String body;
    private String image;
    private boolean readed;

    public NotificationModel(String body, String image, boolean readed) {
        this.body = body;
        this.image = image;
        this.readed = readed;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isReaded() {
        return readed;
    }

    public void setReaded(boolean readed) {
        this.readed = readed;
    }

    @Override
    public String toString() {
        return "NotificationModel{" +
                "body='" + body + '\'' +
                ", image='" + image + '\'' +
                ", readed=" + readed +
                '}';
    }
}
