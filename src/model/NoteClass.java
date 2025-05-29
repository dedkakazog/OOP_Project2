package model;

import enums.NoteType;


public abstract class NoteClass implements Note{

    private NoteType type;
    private String name;

    public NoteClass(NoteType type, String name) {
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
