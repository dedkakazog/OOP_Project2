package model;

import enums.NoteType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class PermanentNote extends ContentNote {
    ArrayList<LocalDate> historyOfUpdates;
    HashSet<ReferenceNote> tags = new HashSet<>();

    public PermanentNote(NoteType type, LocalDate date, String name, String content) {
        super(type, date, name, content);
        historyOfUpdates = new ArrayList<>();
    }


    public void recordUpdate(LocalDate date) {
        historyOfUpdates.add(date);
        updateDate(date);
    }

}
