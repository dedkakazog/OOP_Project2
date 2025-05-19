package model;

import enums.NoteType;

import java.util.HashSet;

public class ReferenceNote extends Note {
    private HashSet<String> notes;
    public ReferenceNote(NoteType type, String name, String taggedNote) {
        super(type, name);
        notes = new HashSet<>();
        notes.add(taggedNote);
    }

    public boolean hasTag(String note) {
        return notes.contains(note);
    }

    public void addTag(String note) {
        notes.add(note);
    }
    public void removeTag(String note) {
        notes.remove(note);
    }
}
