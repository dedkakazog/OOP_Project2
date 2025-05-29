package model;

import java.util.Iterator;

/**
 * Interface for working with reference notes (tags)
 */
public interface ReferenceNote extends Note {

    /**
     * Checks if a note is tagged with this reference
     * @param note note name to check
     * @return true if note is tagged, false otherwise
     */
    boolean hasTaggedNote(String note);

    /**
     * Adds info that given note was tagged with this tag
     * @param note note name to add
     */
    void addNoteToTag(String note);

    /**
     * Removes info that given note was tagged with this tag
     * @param note note name to remove
     */
    void removeNoteFromTag(String note);

    /**
     * Gets an iterator for traversing tagged notes
     * @return iterator of tagged note names
     */
    Iterator<String> getNotesIterator();

    /**
     * Gets the number of tagged notes
     * @return number of tagged notes
     */
    int getNumberOfNotes();
}