package enums;

import exceptions.UnknownNoteKindException;

public enum NoteType {
    PERMANENT("permanent"),
    LITERATURE("literature"),
    REFERENCE("reference");


    private final String noteType;
    NoteType(String noteType) {
        this.noteType = noteType;
    }

    public static NoteType parse(String noteKindStr) throws UnknownNoteKindException {
        if (noteKindStr == null) {
            throw new UnknownNoteKindException();
        }
        if (noteKindStr.equals("permanent")) {
            return PERMANENT;
        } else if (noteKindStr.equals("literature")) {
            return LITERATURE;
        } else {
            throw new UnknownNoteKindException();
        }
    }

    public static NoteType toNoteType(String noteType) {
        for(NoteType type : NoteType.values()) {
            if(type.noteType.equals(noteType)) {
                return type;
            }
        }
        return null;
    }
}
