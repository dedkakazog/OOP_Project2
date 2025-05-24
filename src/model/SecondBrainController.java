package model;

import enums.NoteType;
import exceptions.*;

import javax.swing.text.html.HTML;
import java.time.LocalDate;
import java.util.*;


public class SecondBrainController {

    private static final String NOTE_DETAILS = "%s: %s %d links. %d tags.";
    private static final int FIRST_ADDITION_TO_MAP = 1;

    private HashMap<String, Note> notes;
    private TreeMap<Integer, LinkedList<String>> tags;
    private LocalDate currentDate;

    public SecondBrainController() {
        notes = new HashMap<>();
        tags = new TreeMap<>();
    }


    public void addPermNote(NoteType type, LocalDate date, String name, String content) throws NoteAlreadyExistsException, InvalidNoteKindException, NoTimeTravellingException {
        checkNoteConditions(name, date);
        ContentNote note = new PermanentNote(type, date, name, content);
        changeDate(date);
        notes.put(name, note);
        addLinks(note);
    }

    public void addLitNote(NoteType type, LocalDate date, String name, String content, String title, String author, LocalDate publicationDate, String URL, String quote) throws NoteAlreadyExistsException, NoTimeTravellingException, NoTimeTravellingDocumentException {
        checkNoteConditions(name, date);
        if (publicationDate.isAfter(currentDate)) {
            throw new NoTimeTravellingDocumentException();
        }
        ContentNote note = new LiteraryNote(type, date, name, content, title, author, publicationDate, URL, quote);
        notes.put(name, note);
        changeDate(date);
        addLinks(note);
    }

    private void checkNoteConditions(String name, LocalDate date) throws NoTimeTravellingException, NoTimeTravellingDocumentException {
        if (notes.isEmpty()) {
            setCurrentDate(date);
        }
        if (date.isBefore(currentDate) && !date.isEqual(currentDate)) {
            throw new NoTimeTravellingException();
        }
        if (notes.containsKey(name)) {
            throw new NoteAlreadyExistsException(name);
        }
    }


    private void changeDate(LocalDate date) {
        if(date.isAfter(currentDate)) {
            setCurrentDate(date);
        }
    }


    private void addLinks(ContentNote note){
        if (getLinksAmount(note.getContent()) > 0){
            note.clearLinks();
            ArrayList<String> links = getLinks(note.getContent());
            for (int i = 0; i < getLinksAmount(note.getContent()); i++) {
                note.addLink(links.get(i));
            }
        }
    }


/// ////////////////////////////////////////////////////////////
    public String getNoteDetails(String name) throws NoteNotFoundException{
        if (!notes.containsKey(name)){
            throw new NoteNotFoundException();
        }
        ContentNote note = (ContentNote) notes.get(name);
        return String.format(NOTE_DETAILS, name, note.getContent(), note.getNumLinks(), 0);
    }
    ///////////////////////////////////////////////////////////////////////////



    private void setCurrentDate(LocalDate date) {
        currentDate = date;
    }


    private ArrayList<String> getLinks(String content) {
        ArrayList<String> matches = new ArrayList<>();
        int startIndex = 0;
        while (true) {
            int openBracketIndex = content.indexOf("[[", startIndex);
            if (openBracketIndex == -1) {
                break;
            }
            int closeBracketIndex = content.indexOf("]]", openBracketIndex);
            if (closeBracketIndex == -1) {
                break;
            }
            String match = content.substring(openBracketIndex + 2, closeBracketIndex);
            matches.add(match);
            startIndex = closeBracketIndex + 2;
        }
        return matches;
    }

    public int getLinksAmount(String content) {
        return getLinks(content).size();
    }

    public void updateNoteContent(String name, String content, LocalDate date) throws NoteNotFoundException, NoTimeTravellingException {
        if (!notes.containsKey(name)){
            throw new NoteNotFoundException();
        }
        if (date.isBefore(currentDate)) {
            throw new NoTimeTravellingException();
        }
        ContentNote note = (ContentNote) notes.get(name);
        if(note instanceof PermanentNote permanentNote){
            permanentNote.recordUpdate(date);
            permanentNote.updateContent(content);
        }else {
            note.updateContent(content);
            note.updateDate(date);
        }
        addLinks(note);
    }

    public Iterator<String> getLinksIterator(String name) {
        ContentNote note = (ContentNote) notes.get(name);
        return note.getLinks();
    }

    public void addTag(String noteName, String tagName) throws NoteNotFoundException, TagAlreadyExistsException {
        if (!notes.containsKey(noteName)){
            throw new NoteNotFoundException();
        }
        if(notes.containsKey(tagName)) {
            ReferenceNote tag = (ReferenceNote) notes.get(tagName);
            ContentNote note = (ContentNote) notes.get(noteName);
            if(tag.hasTaggedNote(noteName)){
                throw new TagAlreadyExistsException();
            } else {
                int oldCount = tag.getNumberOfNotes();
                removeTagFromMap(oldCount, tagName);
                addTagToMap(oldCount + 1, tagName);
                tag.addNoteToTag(noteName);
                note.addTag(tagName);
            }
        } else {
            ReferenceNote tag = new ReferenceNote(NoteType.REFERENCE, tagName);
            ContentNote note = (ContentNote) notes.get(noteName);
            notes.put(tagName, tag);
            addTagToMap(FIRST_ADDITION_TO_MAP, tagName);
            tag.addNoteToTag(noteName);
            note.addTag(tagName);
        }
    }

    private void addTagToMap(int count, String tag) {
        tags.putIfAbsent(count, new LinkedList<>());
        tags.get(count).add(tag);
    }

    private void removeTagFromMap(int count, String tag) {
        LinkedList<String> listOfTags = tags.get(count);
        if (listOfTags != null) {
            listOfTags.remove(tag);
            if (listOfTags.isEmpty()) {
                tags.remove(count); // удаляю ключ, если список пуст
            }
        }
    }

    /*public Iterator<String> getAllTagsDescending() {
        LinkedList<String> sortedTags = new LinkedList<>();
        for (Map.Entry<Integer, LinkedList<String>> entry : tags.descendingMap().entrySet()) {
            sortedTags.addAll(entry.getValue());
        }
        return sortedTags.iterator();
    }*/



    public void removeTag(String name, String tagName) throws NoteNotFoundException, TagNotFoundException {
        if (!notes.containsKey(name)){
            throw new NoteNotFoundException();
        }
        if(notes.containsKey(tagName)) {
            ReferenceNote tag = (ReferenceNote) notes.get(tagName);
            ContentNote note = (ContentNote) notes.get(name);
            if(tag.hasTaggedNote(name)){
                removeTagFromMap(tag.getNumberOfNotes(), tagName);
                tag.removeNoteFromTag(name);
                note.removeTag(tagName);
                if(tag.getNumberOfNotes() == 0){
                    notes.remove(tagName);
                }
            } else throw new TagNotFoundException();
        }else throw new TagNotFoundException();
    }

    public Iterator<String> getTags(String noteName) throws NoteNotFoundException {
        if(!notes.containsKey(noteName)) {
            throw new NoteNotFoundException();
        }
        ContentNote note = (ContentNote) notes.get(noteName);
        return note.getTags();
    }

    public Iterator<String> getTaggedNotes(String tagName) throws NoteNotFoundException {
        if(!notes.containsKey(tagName)) {
            throw new NoteNotFoundException();
        }
        ReferenceNote note = (ReferenceNote) notes.get(tagName);
        ArrayList<String> taggedNotes = new ArrayList<>();
        Iterator<String> it = note.getNotesIterator();
        while(it.hasNext()) {
            taggedNotes.add(it.next());
        }
        Collections.sort(taggedNotes);
        return taggedNotes.iterator();
    }

    public Iterator<String> getSortedTags() {//новый вариант
        LinkedList<String> sortedTags = new LinkedList<>();
        for (Integer count : tags.descendingKeySet()) {
            LinkedList<String> tagList = tags.get(count);
            sortedTags.addAll(tagList);
        }
        return sortedTags.iterator();
    }

    /*public Iterator<String> getSortedTags(){///////старый вариант
        ArrayList<ReferenceNote> tags = new ArrayList<>();
        ArrayList<String> tagNames = new ArrayList<>();
        for (int i = 0; i < notes.size(); i++) {
            if(notes.get(i) instanceof ReferenceNote) {
                ReferenceNote note = (ReferenceNote) notes.get(i);
                tags.add(note);
            }
        }
        for (int i = 0; i < tags.size() - 1; i++) {
            for (int j = 0; j < tags.size() - i - 1; j++) {
                if(tags.get(j).getNumberOfNotes() < tags.get(j + 1).getNumberOfNotes() || (tags.get(j).getNumberOfNotes() == tags.get(j + 1).getNumberOfNotes() && tags.get(j).getID() > tags.get(j + 1).getID())) {
                    ReferenceNote temp = tags.get(j);
                    tags.set(j, tags.get(j + 1));
                    tags.set(j + 1, temp);
                }
            }
        }
        for (ReferenceNote tag : tags) {
            tagNames.add(tag.getName());
        }
        return tagNames.iterator();
    }*/
}