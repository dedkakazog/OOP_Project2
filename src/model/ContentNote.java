package model;
import enums.NoteType;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public abstract class ContentNote extends Note {
    private LocalDate lastUpdate;
    private String content;
    private ArrayList<String> links;
    private HashSet<String> tags;
    private int ID;

    public ContentNote(NoteType type, LocalDate date, String name, String content, int ID, ArrayList<String> links) {
        super(type, name);
        this.lastUpdate = date;
        this.content = content;
        this.links = links;
        this.tags = new HashSet<>();
        this.ID = ID;
    }

    public LocalDate getLastUpdate() {
        return lastUpdate;
    }

    public String getContent() {
        return content;
    }

    public Iterator<String> getLinks() {
        return links.iterator();
    }

    public int getNumLinks() {
        return links.size();
    }

    public void updateDate(LocalDate date, int ID) {
        this.lastUpdate = date;
        this.ID = ID;
    }

    public boolean hasLink(String link) {
        return links.contains(link);
    }

    public void removeLink(String link) {
        links.remove(link);
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void clearLinks() {
        links.clear();
    }

    public void loadLinks(ArrayList<String> links) {
        this.links = links;
    }

    public void addTag(String tag) {
        tags.add(tag);
    }

    public void removeTag(String tag) {
        tags.remove(tag);
    }

    public int getNumberOfTags() {
        return tags.size();
    }

    public Iterator<String> getTagsIterator () {
        return tags.iterator();
    }

    public int getID(){
        return ID;
    }
}
