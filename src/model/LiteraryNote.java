package model;

import enums.NoteType;

import java.time.LocalDate;

public class LiteraryNote extends Note {

    String title;
    String author;
    LocalDate publicationDate;
    String URL;
    String quote;
    public LiteraryNote(NoteType type, LocalDate date, String name, String content, String title, String author, LocalDate publicationDate, String URL, String quote) {
        super(type, date, name, content);
        this.title = title;
        this.author = author;
        this.publicationDate = publicationDate;
        this.URL = URL;
        this.quote = quote;
    }
}
