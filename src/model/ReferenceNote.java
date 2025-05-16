package model;

import enums.NoteType;

import java.time.LocalDate;

public class ReferenceNote extends Note {
    public ReferenceNote(NoteType type, LocalDate date, String name, String content) {
        super(type, date, name, content);
    }
}
