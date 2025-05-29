import enums.Command;
import enums.NoteType;
import exceptions.*;
import model.*;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.Scanner;

public class Main {

    private static final int CREATION_DATE_INDEX = 1;
    private static final int DATE_INDEX = 0;

    private static final String MSG_UNKNOWN_COMM = "Unknown command. Type help to see available commands.";
    private static final String MSG_EXIT = "Bye!";
    private static final String MSG_INVALID_DATE = "Invalid date!";
    private static final String MSG_INVALID_DOC_DATE = "Invalid document date!";
    private static final String MSG_NO_LINKED_NOTES = "No linked notes.";
    private static final String MSG_NO_TAGS = "No tags.";
    private static final String MSG_NO_TAGS_DEFINED = "No tags defined yet!";
    private static final String MSG_N0_NOTES_IN_FRAME = "No notes in this time frame.";

    //composed strings
    private static final String MSG_NOTE_ADDED = "Note %s created successfully with links to %d notes.%n";
    private static final String MSG_NOTE_UPDATED = "Note %s updated. It now has %d links.%n";
    private static final String MSG_NOTE_TAGGED =  "%s tagged with %s.%n";
    private static final String MSG_TAG_DELETED = "Note %s no longer tagged with %s.%n";
    private static final String MSG_NOTE_DELETED = "Note %s deleted.%n";

    public static void main(String[] args) {
        SecondBrainController controller = new SecondBrainControllerClass();
        Scanner in = new Scanner(System.in);
        Command comm;
        do{
            String input = in.next().toLowerCase();
            comm = Command.toCommand(input);

            switch(comm){
                case CREATE -> createNote(in, controller);
                case READ -> printNoteDetails(in, controller);
                case UPDATE -> updateNote(in, controller);
                case LINKS -> listLinks(in, controller);
                case TAG -> addTagToNote(in, controller);
                case UNTAG -> removeTagFromNote(in, controller);
                case TAGS -> listNoteTags(in, controller);
                case TAGGED -> listTaggedNotes(in, controller);
                case TRENDING -> listTrendingTags(controller);
                case NOTES -> listNotes(in, controller);
                case DELETE -> deleteNote(in, controller);
                case HELP -> System.out.println(comm.getHelp());
                case EXIT -> System.out.println(MSG_EXIT);
                case null -> System.out.println(MSG_UNKNOWN_COMM);
            }
        }while (comm != Command.EXIT);
        in.close();
    }

    private static void createNote(Scanner in, SecondBrainController controller){
        String[] input = in.nextLine().trim().split(" ");
        String name = in.nextLine();
        String content = in.nextLine();
        NoteType type = NoteType.toNoteType(input[0]);
        LocalDate date;
        try{
            if(type == NoteType.LITERATURE){
                String title = in.nextLine();
                String author = in.nextLine();
                String[] publicationDate = in.nextLine().split(" ");
                String URL = in.nextLine();
                String quote = in.nextLine();
                date = toLocalDate(input, CREATION_DATE_INDEX);
                createLiteNote(controller, name, content, type, date, title, author, publicationDate, URL, quote);
            } else {
                date = toLocalDate(input, CREATION_DATE_INDEX);
                createPermNote(controller, name, content, type, date);
            }
        } catch (DateTimeException e){
            System.out.println(MSG_INVALID_DATE);
        }
    }

    private static void createLiteNote(SecondBrainController controller, String name, String content, NoteType type, LocalDate date, String title, String author, String[] publicationDate, String URL, String quote){
        try {
            LocalDate publishDate = toLocalDate(publicationDate, DATE_INDEX);
            controller.addLitNote(type, date, name, content, title, author, publishDate, URL, quote);
            System.out.printf(MSG_NOTE_ADDED, name, controller.findLinksNum(name));

        } catch (NoteAlreadyExistsException | NoTimeTravellingException | NoTimeTravellingDocumentException e){
            System.out.println(e.getMessage());
        } catch (DateTimeException e){
            System.out.println(MSG_INVALID_DOC_DATE);
        }
    }

    private static void createPermNote(SecondBrainController controller, String name, String content, NoteType type, LocalDate date){
        try {
            controller.addPermNote(type, date, name, content);
            System.out.printf(MSG_NOTE_ADDED, name, controller.findLinksNum(name));
        } catch (NoteAlreadyExistsException | InvalidNoteKindException | NoTimeTravellingException e){
            System.out.println(e.getMessage());
        }
    }

    private static LocalDate toLocalDate(String[] input, int lineStart){
        return LocalDate.of(Integer.parseInt(input[lineStart]), Integer.parseInt(input[lineStart + 1]), Integer.parseInt(input[lineStart + 2]));
    }



    private static void printNoteDetails(Scanner in, SecondBrainController controller){
        String name = in.nextLine().trim();
        try {
            String details = controller.getNoteDetails(name);
            System.out.println(details);
        } catch (NoteNotFoundException e){
            System.out.printf(e.getMessage(), name);
        }
    }

    private static void updateNote(Scanner in, SecondBrainController controller){
        String name = in.nextLine().trim();
        String[] input = in.nextLine().trim().split(" ");
        String content = in.nextLine().trim();
        try {
            LocalDate date = toLocalDate(input, DATE_INDEX);
            controller.updateNoteContent(name, content, date);
            System.out.printf(MSG_NOTE_UPDATED, name, controller.findLinksNum(name));
        } catch (DateTimeException e){
            System.out.println(MSG_INVALID_DATE);
        } catch (NoteNotFoundException e){
            System.out.printf(e.getMessage(), name);
        } catch (NoTimeTravellingException e){
            System.out.println(e.getMessage());
        }
    }

    private static void listLinks(Scanner in, SecondBrainController controller){
        String name = in.nextLine().trim();
        try {
            Iterator<String> it = controller.getLinksIterator(name);
            if(!it.hasNext()){
                System.out.println(MSG_NO_LINKED_NOTES);
            }
            while(it.hasNext()){
                System.out.println(it.next());
            }
        } catch (NoteNotFoundException e){
            System.out.printf(e.getMessage(), name);
        }
    }


    private static void addTagToNote(Scanner in, SecondBrainController controller){
        String name = in.nextLine().trim();
        String tag = in.nextLine().trim();
        try{
            controller.addTag(name, tag);
            System.out.printf(MSG_NOTE_TAGGED, name, tag);
        }catch (NoteNotFoundException e){
            System.out.printf(e.getMessage(), name);
        }catch (TagAlreadyExistsException e){
            System.out.printf(e.getMessage(), name, tag);
        }
    }

    private static void removeTagFromNote(Scanner in, SecondBrainController controller){
        String name = in.nextLine().trim();
        String tag = in.nextLine().trim();
        try{
            controller.untag(name, tag);
            System.out.printf(MSG_TAG_DELETED, name, tag);
        }catch (NoteNotFoundException e){
            System.out.printf(e.getMessage(), name);
        }catch (TagNotFoundException e){
            System.out.printf(e.getMessage(), name, tag);
        }
    }

    private static void listNoteTags(Scanner in, SecondBrainController controller){
        String name = in.nextLine().trim();
        try {
            Iterator<String> it = controller.getTags(name);
            if(!it.hasNext()){
                System.out.println(MSG_NO_TAGS);
            }
            while (it.hasNext()){
                System.out.println(it.next());
            }
        }catch (NoteNotFoundException e){
            System.out.printf(e.getMessage(), name);
        }
    }

    private static void listTaggedNotes(Scanner in, SecondBrainController controller){
        String tagName = in.nextLine().trim();
        try{
            Iterator<String> it = controller.getTaggedNotes(tagName);
            while (it.hasNext()){
                System.out.println(it.next());
            }
        }catch (NoteNotFoundException e){
            System.out.printf(e.getMessage(), tagName);
        }
    }

    /*private static void listTrendingTags(SecondBrainController controller){//////////не тестили ////////старый вариант
        Iterator<String> it = controller.getTrendingTags();
        while(it.hasNext()){
            System.out.println(it.next());
        }
    }*/

    private static void listTrendingTags(SecondBrainController controller){
        Iterator<String> it = controller.getTrendingTags();
        if(!it.hasNext()){
            System.out.println(MSG_NO_TAGS_DEFINED);
        } else {
            while(it.hasNext()){
                System.out.println(it.next());
            }
        }
    }

    private static void listNotes(Scanner in, SecondBrainController controller){
        String noteType = in.nextLine().trim();
        String startDateInput = in.nextLine().trim();
        String endDateInput = in.nextLine().trim();
        try {
            NoteType type = NoteType.parse(noteType);
            LocalDate startDate = controller.validateStartDate(startDateInput);
            LocalDate endDate = controller.validateEndDate(endDateInput);
            Iterator<String> it = controller.getNotes(type, startDate, endDate);
            if (!it.hasNext()) {
                System.out.println(MSG_N0_NOTES_IN_FRAME);
            } else {
                while (it.hasNext()){
                    System.out.println(it.next());
                }
            }
        }catch (UnknownNoteKindException | InvalidStartDateException | InvalidEndDateException | StartEndDateException e){
            System.out.printf(e.getMessage());
        }
    }

    private static void deleteNote(Scanner in, SecondBrainController controller){
        String name = in.nextLine().trim();
        try {
            controller.deleteNote(name);
            System.out.printf(MSG_NOTE_DELETED, name);
        } catch (NoteNotFoundException e){
            System.out.printf(e.getMessage(), name);
        }
    }
}

/*
create permanent 2025 05 05
Robert Baratheon
King Robert Baratheon was the seventeenth ruler of the Seven Kingdoms and the first king of the Baratheon dynasty.
update Robert Baratheon
2025 05 06
King Robert Baratheon was the seventeenth ruler of the [[Seven Kingdoms]] and the first king of the [[Baratheon dynasty]]. The love of his life was [[Lyanna Stark]].
tag Robert Baratheon
king
delete Seven Kingdoms
links Robert Baratheon
*/