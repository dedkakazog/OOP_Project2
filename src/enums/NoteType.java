package enums;

public enum NoteType {
    PERMANENT("permanent"),
    LITERATURE("literature"),
    REFERENCE("reference");


    private final String noteType;
    NoteType(String noteType) {
        this.noteType = noteType;
    }

    public String getNoteType() {
        return noteType;
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
