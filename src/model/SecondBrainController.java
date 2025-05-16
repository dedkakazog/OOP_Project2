package model;

import enums.NoteType;
import exceptions.*;

import java.time.LocalDate;
import java.util.*;


public class SecondBrainController {

    private HashMap<String, Note> notes;
    private HashMap<String, HashSet<String>> tags;

    public SecondBrainController() {
        notes = new HashMap<>();
        tags = new HashMap<>();
    }


    public void addPermNote(NoteType type, LocalDate date, String name, String content) throws NoteAlreadyExistsException, InvalidNoteKindException, InvalidDateException {
        if (date.isAfter(LocalDate.now())) {
            throw new InvalidDateException();
        }
        if (notes.containsKey(name)) {
            throw new NoteAlreadyExistsException(name);
        }        notes.put(name, new PermanentNote(type, date, name, content));
        if (getLinksAmount(content) > 0){
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        }
    }

    public void addLitNote(NoteType type, LocalDate date, String name, String content, String title, String author, LocalDate publicationDate, String URL, String quote){
        if (date.isAfter(LocalDate.now())) {
            throw new InvalidDateException();
        }
        if (notes.containsKey(name)) {
            throw new NoteAlreadyExistsException(name);
        }
        notes.put(name, new LiteraryNote(type, date, name, content, title, author, publicationDate, URL, quote));
        if (getLinksAmount(content) > 0){
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        }
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