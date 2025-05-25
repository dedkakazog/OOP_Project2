package model;

import enums.NoteType;
import exceptions.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;


public class SecondBrainController {

    private static final String NOTE_DETAILS = "%s: %s %d links. %d tags.";
    private static final int FIRST_ADDITION_TO_MAP = 1;
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy MM dd");

    private HashMap<String, Note> notes;
    private TreeMap<Integer, LinkedList<String>> tags;
    private LocalDate currentDate;
    private int ID;


    public SecondBrainController() {
        notes = new HashMap<>();
        tags = new TreeMap<>();
        ID = 0;
    }


    public void addPermNote(NoteType type, LocalDate date, String name, String content) throws NoteAlreadyExistsException, InvalidNoteKindException, NoTimeTravellingException {
        checkNoteConditions(name, date);
        ContentNote note = new PermanentNote(type, date, name, getModifiedContent(content), ID++, getLinks(content));
        changeDate(date);
        notes.put(name, note);
        addSubNotes(note, content, date);
    }

    public void addLitNote(NoteType type, LocalDate date, String name, String content, String title, String author, LocalDate publicationDate, String URL, String quote) throws NoteAlreadyExistsException, NoTimeTravellingException, NoTimeTravellingDocumentException {
        checkNoteConditions(name, date);
        if (publicationDate.isAfter(currentDate)) {
            throw new NoTimeTravellingDocumentException();
        }
        ContentNote note = new LiteratureNote(type, date, name, getModifiedContent(content), title, author, publicationDate, URL, quote, ID++, getLinks(content));
        notes.put(name, note);
        changeDate(date);
        addSubNotes(note, content, date);
    }

    private void addSubNotes(ContentNote note, String content, LocalDate date){
        if (note.getNumLinks() > 0) {
            ArrayList<String> linkNoteNames = getLinks(content);
            for (String linkNoteName : linkNoteNames) {
                if (!notes.containsKey(linkNoteName)) {
                    ContentNote linkNote = new PermanentNote(NoteType.PERMANENT, date, linkNoteName, linkNoteName + ".", ID++, new ArrayList<>());
                    notes.put(linkNoteName, linkNote);
                }
            }
        }
    }

    private void checkNoteConditions(String name, LocalDate date) throws NoTimeTravellingException, NoteAlreadyExistsException {
        if (notes.isEmpty()) {
            setCurrentDate(date);
        }
        if (date.isBefore(currentDate) && !date.isEqual(currentDate) ) {
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

    public int findLinksNum(String name){
        ContentNote note = (ContentNote) notes.get(name);
        return note.getNumLinks();
    }


    private void updateLinks(ContentNote note, String content) {
        note.clearLinks();
        ArrayList<String> links = getLinks(content);
        note.loadLinks(links);
    }



    public String getNoteDetails(String name) throws NoteNotFoundException{
        if (!notes.containsKey(name)){
            throw new NoteNotFoundException();
        }
        ContentNote note = (ContentNote) notes.get(name);
        return String.format(NOTE_DETAILS, name, note.getContent(), note.getNumLinks(), note.getNumberOfTags());
    }


    public void deleteNote(String name) throws NoteNotFoundException {
        if (!notes.containsKey(name)) {
            throw new NoteNotFoundException();
        }
        Note note = notes.get(name);
        if (note.getType() == NoteType.REFERENCE) {
            deleteReferenceNote((ReferenceNote) note);
        } else  {
            deleteContentNote((ContentNote) note);
        }
    }

    private void deleteReferenceNote(ReferenceNote tag){
        Iterator<String> it = tag.getNotesIterator();
        while(it.hasNext()){
            ContentNote taggedNote = (ContentNote) notes.get(it.next());
            taggedNote.removeTag(tag.getName());
        }
        notes.remove(tag.getName());
    }

    private void deleteContentNote(ContentNote note) {
        //Iterator<String> it =
    }

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
            if (!matches.contains(match)){
                matches.add(match);
            }
            startIndex = closeBracketIndex + 2;
        }

        return matches;
    }

    private String getModifiedContent(String content) {
        ArrayList<String> links = getLinks(content);
        for (String link : links) {
            content = content.replace("[[" + link + "]]", link);
        }
        return content;
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
            permanentNote.recordUpdate(date, ID++);
            permanentNote.updateContent(getModifiedContent(content));
        }else {
            note.updateContent(getModifiedContent(content));
            note.updateDate(date, ID++);
        }
        updateLinks(note, content);
    }

    public Iterator<String> getLinksIterator(String name) throws NoteNotFoundException{
        if (!notes.containsKey(name)){
            throw new NoteNotFoundException();
        }
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
        if(!notes.containsKey(tagName)){
            throw new TagNotFoundException();
        }
        ReferenceNote tag = (ReferenceNote) notes.get(tagName);
        ContentNote note = (ContentNote) notes.get(name);
        if(!tag.hasTaggedNote(name)){
            throw new TagNotFoundException();
        }
        removeTagFromMap(tag.getNumberOfNotes(), tagName);
        tag.removeNoteFromTag(name);
        note.removeTag(tagName);
        if(tag.getNumberOfNotes() == 0){
            notes.remove(tagName);
        }
    }

    public Iterator<String> getTags(String noteName) throws NoteNotFoundException {
        if(!notes.containsKey(noteName)) {
            throw new NoteNotFoundException();
        }
        ContentNote note = (ContentNote) notes.get(noteName);
        return note.getTagsIterator();
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

    public LocalDate validateStartDate(String dateStr) throws InvalidStartDateException{
        try {
            return LocalDate.parse(dateStr, DATE_FORMAT);
        } catch (DateTimeParseException e) {
            throw new InvalidStartDateException();
        }
    }

    public LocalDate validateEndDate(String dateEnd) throws InvalidEndDateException {
        try {
            return LocalDate.parse(dateEnd, DATE_FORMAT);
        } catch (DateTimeParseException e) {
            throw new InvalidEndDateException();
        }
    }

    public Iterator<String> getNotes(NoteType noteType, LocalDate startDate, LocalDate endDate) throws StartEndDateException {
        if (startDate.isAfter(endDate)) {
            throw new StartEndDateException();
        }
        ArrayList<ContentNote> filteredNotes = new ArrayList<>();
        for (Note note : notes.values()) {
            if (note instanceof ContentNote contentNote) {
                if (contentNote.getType() == noteType) {
                    LocalDate noteDate = contentNote.getLastUpdate();
                    if (!noteDate.isBefore(startDate) && !noteDate.isAfter(endDate)) {
                        filteredNotes.add(contentNote);
                    }
                }
            }
        }
        for (int i = 0; i < filteredNotes.size() - 1; i++) {
            for (int j = 0; j < filteredNotes.size() - i - 1; j++) {
                ContentNote note1 = filteredNotes.get(j);
                ContentNote note2 = filteredNotes.get(j + 1);
                LocalDate date1 = note1.getLastUpdate();
                LocalDate date2 = note2.getLastUpdate();
                boolean shouldSwap = false;
                if (date1.isBefore(date2)) {//или after
                    shouldSwap = true;
                } else if (date1.isEqual(date2)) {
                    if (note1.getID() < note2.getID()) {//или наоборот
                        shouldSwap = true;
                    }
                }
                if (shouldSwap) {
                    filteredNotes.set(j, note2);
                    filteredNotes.set(j + 1, note1);
                }
            }
        }
        ArrayList<String> noteNames = new ArrayList<>();
        for (ContentNote note : filteredNotes) {
            noteNames.add(note.getName());
        }
        return noteNames.iterator();
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