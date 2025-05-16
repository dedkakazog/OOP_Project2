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
        notes.put(name, new PermanentNote(type, date, name, content));
        if (getLinksAmount(content) > 0){
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        }
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
        notes.put(name, new LiteraryNote(type, date, name, content, title, author, publicationDate, URL, quote));
        if (getLinksAmount(content) > 0){
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        }
    }

    public String getNoteDetails(String name) throws NoteNotFoundException{
        if (!notes.containsKey(name)){
            throw new NoteNotFoundException();
        }
        Note note = notes.get(name);
        return String.format(NOTE_DETAILS, name, note.getContent(), 0, 0);
    }
    /// ////////////////////////////////////////////////////////////////////////

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



}