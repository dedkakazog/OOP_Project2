package model;

import java.time.LocalDate;

public class ReferenceNote extends Note {
    public ReferenceNote(String type, LocalDate date, String name, String content) {
        super(type, date, name, content);
    }
}
