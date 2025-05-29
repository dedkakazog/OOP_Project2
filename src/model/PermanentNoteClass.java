package model;

import enums.NoteType;
import java.time.LocalDate;
import java.util.ArrayList;

public class PermanentNoteClass extends ContentNoteClass implements PermanentNote {

    ArrayList<LocalDate> historyOfUpdates;

    public PermanentNoteClass(NoteType type, LocalDate date, String name, String content, int ID, ArrayList<String> links) {
        super(type, date, name, content, ID, links);
        historyOfUpdates = new ArrayList<>();
    }

    public void recordUpdate(LocalDate date, int ID) {
        historyOfUpdates.add(date);
        updateDate(date, ID);
    }

}
