package model;

import enums.NoteType;
import exceptions.*;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.*;


public class SecondBrainController {

    private static final String NOTE_DETAILS = "%s: %s %d links. %d tags.";

    private HashMap<String, Note> notes;
    private HashMap<String, String> tags;
    private LocalDate currentDate;

    public SecondBrainController() {
        notes = new HashMap<>();
        tags = new HashMap<>();
    }


    public void addPermNote(NoteType type, LocalDate date, String name, String content) throws NoteAlreadyExistsException, InvalidNoteKindException, NoTimeTravellingException {
        if (notes.isEmpty()) {
            setCurrentDate(date);
        }
        if (date != null && date.isBefore(currentDate)) {
            throw new NoTimeTravellingException();
        }
        if (notes.containsKey(name)) {
            throw new NoteAlreadyExistsException(name);
        }
        ContentNote note = new PermanentNote(type, date, name, content);
        notes.put(name, note);
        addLinks(note);
    }

    public void addLitNote(NoteType type, LocalDate date, String name, String content, String title, String author, LocalDate publicationDate, String URL, String quote) throws NoteAlreadyExistsException, NoTimeTravellingException, NoTimeTravellingDocumentException {
        if (notes.isEmpty()) {
            setCurrentDate(date);
        }
        if (date.isBefore(currentDate)) {
            throw new NoTimeTravellingException();
        }
        if (notes.containsKey(name)) {
            throw new NoteAlreadyExistsException(name);
        }
        if (publicationDate.isAfter(currentDate)) {
            throw new NoTimeTravellingDocumentException();
        }
        ContentNote note = new LiteraryNote(type, date, name, content, title, author, publicationDate, URL, quote);
        notes.put(name, note);
        addLinks(note);
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

    public void addReferenceNote(String name, String tag) throws NoteNotFoundException, TagAlreadyExistsException {
        if (!notes.containsKey(name)){
            throw new NoteNotFoundException();
        }

        if(notes.containsKey(tag)) {
            ReferenceNote note = (ReferenceNote) notes.get(tag);
            if(note.alreadyTagged(name)){
                throw new TagAlreadyExistsException();
            } else {
                note.addTag(name);
            }
        } else {
            ReferenceNote note = new ReferenceNote(NoteType.REFERENCE, tag, name);
            notes.put(tag, note);
        }

    }
}