import enums.Command;
import enums.Messages;
import enums.NoteType;
import exceptions.*;
import model.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    private static final int DATE_ID = 1;
    private static final int PUBLICATION_DATE_ID = 0;

    private static final String MSG_UNKNOWN_COMM = "Unknown command. Type help to see available commands.";
    private static final String MSG_EXIT = "Bye!";
    private static final String MSG_NOTE_ADDED = "Note %s created successfully with links to %d notes.";

    public static void main(String[] args) {
        SecondBrainController controller = new SecondBrainController();
        Scanner in = new Scanner(System.in);
        Command comm;
        do{
            String input = in.next().toLowerCase();
            comm = Command.toCommand(input);

            switch(comm){
                case CREATE -> createNote(in, controller);
                case READ -> printNote(in, controller);
                case UPDATE -> updateNote(in, controller);
                case LINKS -> listLinks(in, controller);
                case TAG -> addTag(in, controller);
                case UNTAG -> removeTag(in, controller);
                case TAGS -> listTags(in, controller);
                case TAGGED -> listTagged(in, controller);
                case TRENDING -> sortedTags(controller);
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
        String[] input = in.nextLine().split(" ");
        String name = in.nextLine();
        String content = in.nextLine();
        NoteType type = NoteType.toNoteType(input[0]);
        LocalDate date = toLocalDate(input, DATE_ID);
        try{
            if(type == NoteType.LITERATURE){
                String title = in.nextLine();
                String author = in.nextLine();
                LocalDate publishDate = toLocalDate(in.nextLine().split(" "), PUBLICATION_DATE_ID);
                String URL = in.nextLine();
                String quote = in.nextLine();
                controller.addLitNote(type, date, name, content, title, author, publishDate, URL, quote);
            } else{
                controller.addPermNote(type, date, name, content);
            }
            System.out.printf(MSG_NOTE_ADDED, name, controller.getLinksAmount(content));
        }catch (InvalidDateException | NoteAlreadyExistsException e) {
            System.out.println(e.getMessage());
        }
    }

    private static LocalDate toLocalDate(String[] input, int lineStart) {
        return LocalDate.of(Integer.parseInt(input[lineStart]), Integer.parseInt(input[lineStart + 1]), Integer.parseInt(input[lineStart + 2]));
    }

    private static void printNote(Scanner in, SecondBrainController controller){

    }

    private static void updateNote(Scanner in, SecondBrainController controller){

    }

    private static void listLinks(Scanner in, SecondBrainController controller){

    }

    private static void addTag(Scanner in, SecondBrainController controller){

    }

    private static void removeTag(Scanner in, SecondBrainController controller){

    }

    private static void listTags(Scanner in, SecondBrainController controller){

    }

    private static void listTagged(Scanner in, SecondBrainController controller){

    }

    private static void sortedTags(SecondBrainController controller){

    }

    private static void listNotes(Scanner in, SecondBrainController controller){

    }

    private static void deleteNote(Scanner in, SecondBrainController controller){

    }

    //auxiliary methods



}