package org.ufo.sightings.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "sightings")
public class Sighting {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    @Column(name = "datetime")
    private LocalDateTime datetime;

    private String city;
    private String state;
    private String country;
    private String shape;

    @Column(name = "duration_seconds") // Match the column name
    //@JsonProperty("daft") - can customise the field name in the response json
    private int durationSeconds;

    @Column(name = "durationHoursMins") // Match the column name
    private String durationHoursMins;

    @Column(columnDefinition = "TEXT") // Explicitly specify the type for comments
    private String comments;

    @Column(name = "datePosted") // Match the column name
    private LocalDateTime datePosted;
    private float latitude;
    private float longitude;
    private String agent;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getDatetime() {
        return datetime;
    }

    public void setDatetime(LocalDateTime datetime) {
        this.datetime = datetime;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getShape() {
        return shape;
    }

    public void setShape(String shape) {
        this.shape = shape;
    }

    public int getDurationSeconds() {
        return durationSeconds;
    }

    public void setDurationSeconds(int durationSeconds) {
        this.durationSeconds = durationSeconds;
    }

    public String getDurationHoursMins() {
        return durationHoursMins;
    }

    public void setDurationHoursMins(String durationHoursMins) {
        this.durationHoursMins = durationHoursMins;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public LocalDateTime getDatePosted() {
        return datePosted;
    }

    public void setDatePosted(LocalDateTime datePosted) {
        this.datePosted = datePosted;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }
}
