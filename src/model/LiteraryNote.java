package model;

import enums.NoteType;

import java.time.LocalDate;

public class LiteraryNote extends ContentNote {

    String title;
    String author;
    LocalDate publicationDate;
    String URL;
    String quote;
    public LiteraryNote(NoteType type, LocalDate date, String name, String content, String title, String author, LocalDate publicationDate, String URL, String quote, int ID) {
        super(type, date, name, content, ID);
        this.title = title;
        this.author = author;
        this.publicationDate = publicationDate;
        this.URL = URL;
        this.quote = quote;
    }
}

