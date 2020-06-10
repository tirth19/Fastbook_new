package com.gyansaarthi.fastbook.Objects;

public class BookCover {
    private String book_title, book_author, thumbnail;
    private int total_pages, pages_read;

    public BookCover(String book_title, String book_author, String thumbnail, int total_pages, int pages_read) {
        this.book_title = book_title;
        this.book_author = book_author;
        this.thumbnail = thumbnail;
        this.total_pages = total_pages;
        this.pages_read = pages_read;
    }

    public BookCover() {
    }

    public String getBook_title() {
        return book_title;
    }

    public void setBook_title(String book_title) {
        this.book_title = book_title;
    }

    public String getBook_author() {
        return book_author;
    }

    public void setBook_author(String book_author) {
        this.book_author = book_author;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

    public int getPages_read() {
        return pages_read;
    }

    public void setPages_read(int pages_read) {
        this.pages_read = pages_read;
    }
}
