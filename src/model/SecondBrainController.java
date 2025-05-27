package model;

import enums.NoteType;
import exceptions.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;


public class SecondBrainController {

    private static final int FIRST_ADDITION_TO_MAP = 1;

    private static final String LINK_START = "[[";
    private static final String LINK_END = "]]";
    
    private static final String NOTE_DETAILS = "%s: %s %d links. %d tags.";
    
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy MM dd");

    private HashMap<String, Note> notes;

    private TreeMap<Integer, LinkedList<String>> sortedTags;

    private LocalDate currentDate;
    private int updateId;


    public SecondBrainController() {
        notes = new HashMap<>();
        sortedTags = new TreeMap<>();
        updateId = 0;
    }

//create command
    public void addLitNote(NoteType type, LocalDate date, String name, String content, String title, String author, LocalDate publicationDate, String URL, String quote) throws NoteAlreadyExistsException, NoTimeTravellingException, NoTimeTravellingDocumentException {
        validateNoteConditions(name, date);
        if (publicationDate.isAfter(currentDate)) {
            throw new NoTimeTravellingDocumentException();
        }

        addSubNotes(content, date);
        ContentNote note = new LiteratureNote(type, date, name, contentWithNoLinks(content), title, author, publicationDate, URL, quote, updateId++, extractLinks(content));
        notes.put(name, note);

        if(date.isAfter(currentDate)) {
            setCurrentDate(date);
        }
    }

    public void addPermNote(NoteType type, LocalDate date, String name, String content) throws NoteAlreadyExistsException, NoTimeTravellingException {
        validateNoteConditions(name, date);

        addSubNotes(content, date);
        ContentNote note = new PermanentNote(type, date, name, contentWithNoLinks(content), updateId++, extractLinks(content));
        notes.put(name, note);

        if(date.isAfter(currentDate)) {
            setCurrentDate(date);
        }
    }

    private void validateNoteConditions(String name, LocalDate date) throws NoTimeTravellingException, NoteAlreadyExistsException {
        if (notes.isEmpty()) {
            setCurrentDate(date);
        }
        if (date.isBefore(currentDate) && !date.isEqual(currentDate) ) {
            throw new NoTimeTravellingException();
        }
        if (notes.containsKey(name)){
            throw new NoteAlreadyExistsException(name);
        }
    }

    private void addSubNotes(String content, LocalDate date){
        ArrayList<String> subNoteNames = extractLinks(content);
        if (!subNoteNames.isEmpty()) {
            for (String subNoteName : subNoteNames) {
                if (!notes.containsKey(subNoteName)) {
                    String subNoteContent = subNoteName + ".";
                    addPermNote(NoteType.PERMANENT, date, subNoteName, subNoteContent);
                }
            }
        }
    }

    public int findLinksNum(String name){
        ContentNote note = (ContentNote) notes.get(name);
        return note.getNumberOfLinks();
    }


//read command
    public String getNoteDetails(String name) throws NoteNotFoundException{
        ensureNoteExists(name);
        ContentNote note = (ContentNote) notes.get(name);
        return String.format(NOTE_DETAILS, name, note.getContent(), note.getNumberOfLinks(), note.getNumberOfTags());
    }



//update command
    public void updateNoteContent(String name, String content, LocalDate date) throws NoteNotFoundException, NoTimeTravellingException {
        ensureNoteExists(name);
        if (date.isBefore(currentDate)) {
            throw new NoTimeTravellingException();
        }
        ContentNote note = (ContentNote) notes.get(name);
        if(note instanceof PermanentNote permanentNote){
            permanentNote.recordUpdate(date, updateId++);
            permanentNote.updateContent(contentWithNoLinks(content));
        }else {
            note.updateContent(contentWithNoLinks(content));
            note.updateDate(date, updateId++);
        }
        updateLinks(note, content);
        addSubNotes(content, date);
    }

    private void updateLinks(ContentNote note, String content) {
        ArrayList<String> links = extractLinks(content);
        note.loadLinks(links);
    }



//links command
    public Iterator<String> getLinksIterator(String noteName) throws NoteNotFoundException{
        ensureNoteExists(noteName);
        ContentNote note = (ContentNote) notes.get(noteName);
        return note.getLinks();
    }



//tag command
    public void addTag(String noteName, String tagName) throws NoteNotFoundException, TagAlreadyExistsException {
        ensureNoteExists(noteName);

        ContentNote note = (ContentNote) notes.get(noteName);
        ReferenceNote tag = (ReferenceNote) notes.get(tagName);

        if (tag != null) { // If the tag already exists
            if (tag.hasTaggedNote(noteName)) {
                throw new TagAlreadyExistsException();
            }
            int oldCount = tag.getNumberOfNotes();
            removeTagFromMap(oldCount, tagName);
            addTagToMap(oldCount + 1, tagName);
        } else { // If the tag does not exist, create it
            tag = new ReferenceNote(NoteType.REFERENCE, tagName);
            notes.put(tagName, tag);
            addTagToMap(FIRST_ADDITION_TO_MAP, tagName);
        }
        tag.addNoteToTag(noteName);
        note.addTag(tagName);
    }

    private void addTagToMap(int count, String tagName) {
        sortedTags.putIfAbsent(count, new LinkedList<>());
        sortedTags.get(count).add(tagName);
    }

    private void removeTagFromMap(int count, String tagName) {
        LinkedList<String> listOfTags = sortedTags.get(count);
        if (listOfTags != null) {
            listOfTags.remove(tagName);
            if (listOfTags.isEmpty()) {
                sortedTags.remove(count);
            }
        }
    }



//untag command
    public void untag(String noteName, String tagName) throws NoteNotFoundException, TagNotFoundException {
        ensureNoteExists(noteName);
        ensureTagExists(tagName);

        ReferenceNote tag = (ReferenceNote) notes.get(tagName);
        ContentNote note = (ContentNote) notes.get(noteName);

        if(!tag.hasTaggedNote(noteName)){
            throw new TagNotFoundException();
        }

        removeTagFromMap(tag.getNumberOfNotes(), tagName);
        tag.removeNoteFromTag(noteName);
        note.removeTag(tagName);
        if(tag.getNumberOfNotes() == 0){
            notes.remove(tagName);
        }else{
            addTagToMap(tag.getNumberOfNotes(), tagName);
        }
    }



//sortedTags command
    public Iterator<String> getTags(String noteName) throws NoteNotFoundException {
        ensureNoteExists(noteName);
        ContentNote note = (ContentNote) notes.get(noteName);
        return note.getTagsIterator();
    }



//tagged command
    public Iterator<String> getTaggedNotes(String tagName) throws TagNotFoundException {
        ensureTagExists(tagName);
        ReferenceNote tag = (ReferenceNote) notes.get(tagName);
        return tag.getNotesIterator();
    }



//trending command
    public Iterator<String> getTrendingTags() {
        List<String> trendingTags = new LinkedList<>();
        if (!sortedTags.isEmpty()) {
            trendingTags = sortedTags.get(sortedTags.lastKey()); // last key is the largest number
        }
        return trendingTags.iterator();
    }



//notes command
    public Iterator<String> getNotes(NoteType noteType, LocalDate startDate, LocalDate endDate) throws StartEndDateException {
        if (startDate.isAfter(endDate)) {
            throw new StartEndDateException();
        }
        List<ContentNote> filteredNotes = new ArrayList<>();
        for (Note note : notes.values()) {
            if (note instanceof ContentNote contentNote) {
                if (contentNote.getType() == noteType) {
                    LocalDate noteDate = contentNote.getLastUpdateDate();
                    if (!noteDate.isBefore(startDate) && !noteDate.isAfter(endDate)) {
                        filteredNotes.add(contentNote);
                    }
                }
            }
        }
        filteredNotes.sort(Comparator.comparing(ContentNote::getLastUpdateID));
        List<String> noteNames = new ArrayList<>();
        for (ContentNote note : filteredNotes) {
            noteNames.add(note.getName());
        }
        return noteNames.iterator();
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



//delete command
    public void deleteNote(String name) throws NoteNotFoundException {
        ensureNoteExists(name);
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
        removeTagFromMap(tag.getNumberOfNotes(), tag.getName());
        notes.remove(tag.getName());
    }

    private void deleteContentNote(ContentNote delNote) {
        String delNoteName = delNote.getName();
        for (Note note: notes.values()) {
            if(note instanceof PermanentNote || note instanceof LiteratureNote && ((ContentNote) note).hasLink(delNoteName)){
                ((ContentNote) note).removeLink(delNoteName);
            }
        }
        Iterator<String> it = delNote.getTagsIterator();
        while(it.hasNext()){
            ReferenceNote tag = (ReferenceNote) notes.get(it.next());
            untag(delNoteName, tag.getName());
        }
        notes.remove(delNoteName);
    }




    //for all

    private ArrayList<String> extractLinks(String content) {
        ArrayList<String> matches = new ArrayList<>();
        int startIndex = 0;
        while (true) {
            int openBracketIndex = content.indexOf(LINK_START, startIndex);
            if (openBracketIndex == -1) {
                break;
            }
            int closeBracketIndex = content.indexOf(LINK_END, openBracketIndex);
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

    private String contentWithNoLinks(String content) {
        ArrayList<String> links = extractLinks(content);
        for (String link : links) {
            content = content.replace(LINK_START + link + LINK_END, link);
        }
        return content;
    }

    private void ensureNoteExists(String noteName) throws NoteNotFoundException {
        if (!notes.containsKey(noteName)) {
            throw new NoteNotFoundException();
        }
    }

    private void ensureTagExists(String tagName) throws NoteNotFoundException {
        if (!notes.containsKey(tagName)) {
            throw new NoteNotFoundException();
        }
    }

    private void setCurrentDate(LocalDate date) {
        currentDate = date;
    }


     /*public Iterator<String> getAllTagsDescending() {
        LinkedList<String> sortedTags = new LinkedList<>();
        for (Map.Entry<Integer, LinkedList<String>> entry : sortedTags.descendingMap().entrySet()) {
            sortedTags.addAll(entry.getValue());
        }
        return sortedTags.iterator();
    }


    /*public Iterator<String> getTrendingTags(){///////старый вариант
        ArrayList<ReferenceNote> sortedTags = new ArrayList<>();
        ArrayList<String> tagNames = new ArrayList<>();
        for (int i = 0; i < notes.size(); i++) {
            if(notes.get(i) instanceof ReferenceNote) {
                ReferenceNote note = (ReferenceNote) notes.get(i);
                sortedTags.add(note);
            }
        }
        for (int i = 0; i < sortedTags.size() - 1; i++) {
            for (int j = 0; j < sortedTags.size() - i - 1; j++) {
                if(sortedTags.get(j).getNumberOfNotes() < sortedTags.get(j + 1).getNumberOfNotes() || (sortedTags.get(j).getNumberOfNotes() == sortedTags.get(j + 1).getNumberOfNotes() && sortedTags.get(j).getLastUpdateID() > sortedTags.get(j + 1).getLastUpdateID())) {
                    ReferenceNote temp = sortedTags.get(j);
                    sortedTags.set(j, sortedTags.get(j + 1));
                    sortedTags.set(j + 1, temp);
                }
            }
        }
        for (ReferenceNote tag : sortedTags) {
            tagNames.add(tag.getName());
        }
        return tagNames.iterator();
    }*/
}