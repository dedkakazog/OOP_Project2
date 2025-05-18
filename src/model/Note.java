package model;

import enums.NoteType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public abstract class Note {

    private NoteType type;
    private LocalDate lastUpdate;
    private String name;
    private String content;
    private HashSet<String> links;

    public Note(NoteType type, LocalDate date, String name, String content) {
        this.type = type;
        this.lastUpdate = date;
        this.name = name;
        this.content = content;
        this.links = new HashSet<>();
    }

    public NoteType getType() {
        return type;
    }
    public LocalDate getLastUpdate() {
        return lastUpdate;
    }
    public String getName() {
        return name;
    }
    public String getContent() {
        return content;
    }

    public void addLink(String note) {
        links.add(note);
    }

    public Iterator<String> getLinks() {
        return links.iterator();
    }

    public int getNumLinks() {
        return links.size();
    }

    public void updateDate(LocalDate date) {
        this.lastUpdate = date;
    }

    public void updateContent(String content) {
        this.content = content;
    }



    public void clearLinks() {
        links.clear();
    }
}
