package com.lms.librarymanagementsystem.models;
//  #011B3E blue
//  #F0F0F0 light gray
public class MediaModel {
    Integer mediaid;
    String  title, format, category, description,
            publisher, edition, author, isbn,
            director, actor, country, rating,
            available;
    public MediaModel(Integer mediaid,
                      String title, String format, String category, String description,
                      String publisher, String edition, String author, String isbn,
                      String director, String actor, String country, String rating,
                      String available) {
        this.mediaid = mediaid;
        this.title = title;
        this.format = format;
        this.category = category;
        this.description = description;
        this.publisher = publisher;
        this.edition = edition;
        this.author = author;
        this.isbn = isbn;
        this.director = director;
        this.actor = actor;
        this.country = country;
        this.rating = rating;
        this.available = available;
    }
    @SuppressWarnings("unused")
    public Integer getMediaid() {
        return mediaid;
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
    public String getEdition(){return edition;}
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
    public String getAvailable() {
        return available;
    }
    @SuppressWarnings("unused")
    public void setMediaid(Integer mediaid) {
        this.mediaid = mediaid;
    }
    @SuppressWarnings("unused")
    public void setTitle(String title) {
        this.title = title;
    }
    @SuppressWarnings("unused")
    public void setFormat(String format) {
        this.format = format;
    }
    @SuppressWarnings("unused")
    public void setCategory(String category) {
        this.category = category;
    }
    @SuppressWarnings("unused")
    public void setDescription(String description) {
        this.description = description;
    }
    @SuppressWarnings("unused")
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
    @SuppressWarnings("unused")
    public void setEdition(String edition){this.edition=edition;}
    @SuppressWarnings("unused")
    public void setAuthor(String author) {
        this.author = author;
    }
    @SuppressWarnings("unused")
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
    @SuppressWarnings("unused")
    public void setDirector(String director) {
        this.director = director;
    }
    @SuppressWarnings("unused")
    public void setActor(String actor) {
        this.actor = actor;
    }
    @SuppressWarnings("unused")
    public void setCountry(String country) {
        this.country = country;
    }
    @SuppressWarnings("unused")
    public void setRating(String rating) {
        this.rating = rating;
    }
    @SuppressWarnings("unused")
    public void setAvailable(String available) {
        this.available = available;
    }
}