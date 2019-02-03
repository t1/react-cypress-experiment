package com.example.reactcypressexperiment;

public class Book {
    private String author;
    private String title;

    public static Book by(String author) { return new Book().setAuthor(author); }

    public String getAuthor() { return author; }

    public Book setAuthor(String author) { this.author = author; return this; }

    public String getTitle() { return title; }

    public Book setTitle(String title) { this.title = title; return this; }
}
