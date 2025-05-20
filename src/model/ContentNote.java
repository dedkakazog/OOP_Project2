package model;
import enums.NoteType;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Iterator;

public abstract class ContentNote extends Note {
    private LocalDate lastUpdate;
    private String content;
    private HashSet<String> links;
    private HashSet<String> tags;

    public ContentNote(NoteType type, LocalDate date, String name, String content) {
        super(type, name);
        this.lastUpdate = date;
        this.content = content;
        this.links = new HashSet<>();
        this.tags = new HashSet<>();
    }

    public LocalDate getLastUpdate() {
        return lastUpdate;
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

    public void addTag(String note) {
        tags.add(note);
    }

    public void removeTag(String note) {
        tags.remove(note);
    }

    public Iterator<String> getTags() {
        return tags.iterator();
    }
}
