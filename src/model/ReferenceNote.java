package model;

import enums.NoteType;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;

public class ReferenceNote extends Note {
    private HashSet<String> notes;
    public ReferenceNote(NoteType type, String name, String taggedNote) {
        super(type, name);
        notes = new HashSet<>();
        notes.add(taggedNote);
    }

    public boolean alreadyTagged(String note) {
        return notes.contains(note);
    }

    public void addTag(String note) {
        notes.add(note);
    }
}
