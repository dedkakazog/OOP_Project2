package model;

import enums.NoteType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public abstract class Note {

    private NoteType type;
    private String name;

    public Note(NoteType type, String name) {
        this.type = type;
        this.name = name;
    }


    public NoteType getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
