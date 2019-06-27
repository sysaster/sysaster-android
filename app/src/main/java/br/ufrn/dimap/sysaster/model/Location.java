package br.ufrn.dimap.sysaster.model;

import java.util.Date;

public class Location {
    private String id;
    private String urlImage;
    private Double latitude;
    private Double longitude;
    private Date date;

    public Location(String id, String urlImage, Double latitude, Double longitude, Date date) {
        this.id = id;
        this.urlImage = urlImage;
        this.latitude = latitude;
        this.longitude = longitude;
        this.setDate(date);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
