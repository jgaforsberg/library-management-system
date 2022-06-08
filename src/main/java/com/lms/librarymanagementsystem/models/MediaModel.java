package com.lms.librarymanagementsystem.models;

import javafx.beans.property.SimpleStringProperty;
//  #011B3E blue
//  #F0F0F0 light gray
@SuppressWarnings("ALL")
public class MediaModel {
    private Integer mediaid;
    private SimpleStringProperty    title, format, category, description,
            publisher, edition, author, isbn,
            director, actor, country, rating,
            available;
    public MediaModel() {
    }
    public MediaModel(Integer mediaid,
                      String title, String format, String category, String description,
                      String publisher, String edition, String author, String isbn,
                      String director, String actor, String country, String rating,
                      String available) {
        this.mediaid = mediaid;
        this.title = new SimpleStringProperty(title);
        this.format = new SimpleStringProperty(format);
        this.category = new SimpleStringProperty(category);
        this.description = new SimpleStringProperty(description);
        this.publisher = new SimpleStringProperty(publisher);
        this.edition = new SimpleStringProperty(edition);
        this.author = new SimpleStringProperty(author);
        this.isbn = new SimpleStringProperty(isbn);
        this.director = new SimpleStringProperty(director);
        this.actor = new SimpleStringProperty(actor);
        this.country = new SimpleStringProperty(country);
        this.rating = new SimpleStringProperty(rating);
        this.available = new SimpleStringProperty(available);
    }
    public MediaModel(Integer mediaid, String title) {
        this.mediaid = mediaid;
        this.title = new SimpleStringProperty(title);
    }
    public Integer getMediaid() {
        return mediaid;
    }
    public SimpleStringProperty titleProperty() {
        return title;
    }
    public SimpleStringProperty formatProperty() {
        return format;
    }
    public SimpleStringProperty categoryProperty() {
        return category;
    }
    public SimpleStringProperty descriptionProperty() {
        return description;
    }
    public SimpleStringProperty publisherProperty() {
        return publisher;
    }
    public SimpleStringProperty editionProperty(){return edition;}
    public SimpleStringProperty authorProperty() {
        return author;
    }
    public SimpleStringProperty isbnProperty() {
        return isbn;
    }
    public SimpleStringProperty directorProperty() {
        return director;
    }
    public SimpleStringProperty actorProperty() {
        return actor;
    }
    public SimpleStringProperty countryProperty() {
        return country;
    }
    public SimpleStringProperty ratingProperty() {
        return rating;
    }
    public SimpleStringProperty availableProperty() {
        return available;
    }
    public void setMediaid(Integer mediaid) {
        this.mediaid = mediaid;
    }
    public void setTitle(SimpleStringProperty title) {
        this.title = title;
    }
    public void setFormat(SimpleStringProperty format) {
        this.format = format;
    }
    public void setCategory(SimpleStringProperty category) {
        this.category = category;
    }
    public void setDescription(SimpleStringProperty description) {
        this.description = description;
    }
    public void setPublisher(SimpleStringProperty publisher) {
        this.publisher = publisher;
    }
    public void setEdition(SimpleStringProperty edition){this.edition=edition;}
    public void setAuthor(SimpleStringProperty author) {
        this.author = author;
    }
    public void setIsbn(SimpleStringProperty isbn) {
        this.isbn = isbn;
    }
    public void setDirector(SimpleStringProperty director) {
        this.director = director;
    }
    public void setActor(SimpleStringProperty actor) {
        this.actor = actor;
    }
    public void setCountry(SimpleStringProperty country) {
        this.country = country;
    }
    public void setRating(SimpleStringProperty rating) {
        this.rating = rating;
    }
    public void setAvailable(SimpleStringProperty available) {
        this.available = available;
    }
}