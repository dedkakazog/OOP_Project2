package model;

import enums.NoteType;

import java.time.LocalDate;
import java.util.ArrayList;

public class PermanentNote extends Note {
    ArrayList<LocalDate> historyOfUpdates;

    public PermanentNote(NoteType type, LocalDate date, String name, String content) {
        super(type, date, name, content);
        historyOfUpdates = new ArrayList<>();
    }

    public void recordUpdate(LocalDate date) {
        historyOfUpdates.add(date);
        updateDate(date);
    }

}
