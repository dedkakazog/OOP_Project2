package model;
import enums.NoteType;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

public abstract class ContentNoteClass extends NoteClass implements ContentNote{

    private String content;

    private ArrayList<String> links;
    private TreeSet<String> tags;

    private LocalDate lastUpdateDate;
    private int lastUpdateID;

    public ContentNoteClass(NoteType type, LocalDate date, String name, String content, int ID, ArrayList<String> links) {
        super(type, name);
        this.content = content;
        this.links = links;
        tags = new TreeSet<>();
        lastUpdateDate = date;
        lastUpdateID = ID;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void loadLinks(ArrayList<String> links) {
        this.links = links;
    }

    public void removeLink(String link) {
        links.remove(link);
    }

    public boolean hasLink(String link) {
        return links.contains(link);
    }

    public Iterator<String> getLinks() {
        return links.iterator();
    }

    public int getNumberOfLinks() {
        return links.size();
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

    public LocalDate getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void updateDate(LocalDate date, int ID) {
        this.lastUpdateDate = date;
        this.lastUpdateID = ID;
    }

    public int getLastUpdateID(){
        return lastUpdateID;
    }
}
