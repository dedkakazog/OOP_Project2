package model;

import enums.NoteType;

import java.util.HashSet;
import java.util.Iterator;

public class ReferenceNote extends Note {
    private HashSet<String> notes;
    private int ID;

    public ReferenceNote(NoteType type, String name) {
        super(type, name);
        notes = new HashSet<>();
    }

    public boolean hasTaggedNote(String note) {
        return notes.contains(note);
    }


    public void addNoteToTag(String note, int ID) {
        notes.add(note);
        this.ID = ID;
    }

    public void removeNoteFromTag(String note) {
        notes.remove(note);
    }

    public Iterator<String> getNotesIterator() {
        return notes.iterator();
    }

    public int getNumberOfNotes() {
        return notes.size();
    }

    public int getID() {
        return ID;
    }
}
