package model;

import enums.NoteType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class PermanentNote extends ContentNote {

    ArrayList<LocalDate> historyOfUpdates;

    public PermanentNote(NoteType type, LocalDate date, String name, String content, int ID, ArrayList<String> links) {
        super(type, date, name, content, ID, links);
        historyOfUpdates = new ArrayList<>();
    }

    public void recordUpdate(LocalDate date, int ID) {
        historyOfUpdates.add(date);
        updateDate(date, ID);
    }

}
