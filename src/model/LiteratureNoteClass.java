package model;

import enums.NoteType;

import java.time.LocalDate;
import java.util.ArrayList;

public class LiteratureNoteClass extends ContentNoteClass implements LiteratureNote {

    String title;
    String author;
    LocalDate publicationDate;
    String URL;
    String quote;

    public LiteratureNoteClass(NoteType type, LocalDate date, String name, String content, String title, String author, LocalDate publicationDate, String URL, String quote, int ID, ArrayList<String> links) {
        super(type, date, name, content, ID, links);
        this.title = title;
        this.author = author;
        this.publicationDate = publicationDate;
        this.URL = URL;
        this.quote = quote;
    }
}
