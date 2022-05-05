package com.lms.librarymanagementsystem;

public class MediaSearchModel {
    Integer mediaid, available;
    String title, format, category, description, publisher, author, isbn, director, actor, country, rating;

    public MediaSearchModel(Integer mediaid, String title, String format, String category,
                            String description, String publisher, String author, String isbn, String director,
                            String actor, String country, String rating, Integer available) {
        this.mediaid = mediaid;
        this.title = title;
        this.format = format;
        this.category = category;
        this.description = description;
        this.publisher = publisher;
        this.author = author;
        this.isbn = isbn;
        this.director = director;
        this.actor = actor;
        this.country = country;
        this.rating = rating;
        this.available = available;
    }
    public Integer getMediaid() {
        return mediaid;
    }
    public Integer getAvailable() {
        return available;
    }
    public String getTitle() {
        return title;
    }
    public String getFormat() {
        return format;
    }
    public String getCategory() {
        return category;
    }
    public String getDescription() {
        return description;
    }
    public String getPublisher() {
        return publisher;
    }
    public String getAuthor() {
        return author;
    }
    public String getIsbn() {
        return isbn;
    }
    public String getDirector() {
        return director;
    }
    public String getActor() {
        return actor;
    }
    public String getCountry() {
        return country;
    }
    public String getRating() {
        return rating;
    }
    public void setMediaid(Integer mediaid) {
        this.mediaid = mediaid;
    }
    public void setAvailable(Integer available) {
        this.available = available;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setFormat(String format) {
        this.format = format;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
    public void setDirector(String director) {
        this.director = director;
    }
    public void setActor(String actor) {
        this.actor = actor;
    }
    public void setCountry(String country) {
        this.country = country;
    }
    public void setRating(String rating) {
        this.rating = rating;
    }
}
