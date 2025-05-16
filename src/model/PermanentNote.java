package model;

import enums.NoteType;

import java.time.LocalDate;

public class PermanentNote extends Note {
    public PermanentNote(NoteType type, LocalDate date, String name, String content) {
        super(type, date, name, content);
    }
}
