package model;

import enums.NoteType;

import java.time.LocalDate;

public abstract class Note {

    private NoteType type;
    private LocalDate date;
    private String name;
    private String content;

    public Note(NoteType type, LocalDate date, String name, String content) {
        this.type = type;
        this.date = date;
        this.name = name;
        this.content = content;
    }
}
