package model;

import enums.NoteType;

import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeSet;

public class ReferenceNote extends Note
{
    private TreeSet<String> noteNames;

    public ReferenceNote(NoteType type, String name) {
        super(type, name);
        noteNames = new TreeSet<>();
    }

    public boolean hasTaggedNote(String note) {
        return noteNames.contains(note);
    }

    public void addNoteToTag(String note) {
        noteNames.add(note);
    }

    public void removeNoteFromTag(String note) {
        noteNames.remove(note);
    }

    public Iterator<String> getNotesIterator() {
        return noteNames.iterator();
    }

    public int getNumberOfNotes() {
        return noteNames.size();
    }

}
