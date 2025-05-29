package model;

import enums.NoteType;

/**
 * Base interface for all note types
 */
public interface Note {

    /**
     * Gets the type of the note
     * @return note type
     */
    NoteType getType();

    /**
     * Gets the name of the note
     * @return note name
     */
    String getName();
}