package model;

import enums.NoteType;

import java.util.HashSet;
import java.util.Iterator;

public class ReferenceNote extends Note {
    private HashSet<String> notes;

    public ReferenceNote(NoteType type, String name) {
        super(type, name);
        notes = new HashSet<>();
    }

    public boolean hasTaggedNote(String note) {
        return notes.contains(note);
    }


    public void addNoteToTag(String note) {
        notes.add(note);
    }

    public void removeNoteFromTag(String note) {
        notes.remove(note);
    }

    public Iterator<String> getNotesIterator() {
        return notes.iterator();
    }
}
