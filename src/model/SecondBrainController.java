package model;

import enums.NoteType;
import exceptions.*;
import java.time.LocalDate;
import java.util.Iterator;

/**
 * Interface for the Second Brain Controller
 */
public interface SecondBrainController {

    /**
     * Adds a literature note
     * @param type note type
     * @param date note creation date
     * @param name note name
     * @param content note content
     * @param title literature title
     * @param author literature author
     * @param publicationDate publication date of attached title
     * @param URL reference URL
     * @param quote quoted text of title
     * @throws NoteAlreadyExistsException if note already exists
     * @throws NoTimeTravellingException if date is in the past
     * @throws NoTimeTravellingDocumentException if publication date of attached title is in future
     */
    void addLitNote(NoteType type, LocalDate date, String name, String content,
                    String title, String author, LocalDate publicationDate,
                    String URL, String quote) throws NoteAlreadyExistsException,
            NoTimeTravellingException, NoTimeTravellingDocumentException;

    /**
     * Adds a permanent note
     * @param type note type
     * @param date creation date
     * @param name note name
     * @param content note content
     * @throws NoteAlreadyExistsException if note already exists
     * @throws NoTimeTravellingException if date is in the past
     */
    void addPermNote(NoteType type, LocalDate date, String name, String content)
            throws NoteAlreadyExistsException, NoTimeTravellingException;

    /**
     * Finds the number of links in a note
     * @param name note name
     * @return number of links
     */
    int findLinksNum(String name);

    /**
     * Gets note details as formatted string
     * @param name note name
     * @return formatted note details
     * @throws NoteNotFoundException if note doesn't exist
     */
    String getNoteDetails(String name) throws NoteNotFoundException;

    /**
     * Updates note content
     * @param name note name
     * @param content new content
     * @param date update date
     * @throws NoteNotFoundException if note doesn't exist
     * @throws NoTimeTravellingException if date is in the past
     */
    void updateNoteContent(String name, String content, LocalDate date)
            throws NoteNotFoundException, NoTimeTravellingException;

    /**
     * Gets an iterator of links for a note
     * @param noteName note name
     * @return iterator of links
     * @throws NoteNotFoundException if note doesn't exist
     */
    Iterator<String> getLinksIterator(String noteName) throws NoteNotFoundException;

    /**
     * Adds a tag to a note
     * @param noteName note name
     * @param tagName tag name
     * @throws NoteNotFoundException if note doesn't exist
     * @throws TagAlreadyExistsException if tag already exists on note
     */
    void addTag(String noteName, String tagName)
            throws NoteNotFoundException, TagAlreadyExistsException;

    /**
     * Removes a tag from a note
     * @param noteName note name
     * @param tagName tag name
     * @throws NoteNotFoundException if note doesn't exist
     * @throws TagNotFoundException if tag doesn't exist
     */
    void untag(String noteName, String tagName)
            throws NoteNotFoundException, TagNotFoundException;

    /**
     * Gets tags for a note
     * @param noteName note name
     * @return iterator of tags
     * @throws NoteNotFoundException if note doesn't exist
     */
    Iterator<String> getTags(String noteName) throws NoteNotFoundException;

    /**
     * Gets notes tagged with a specific tag
     * @param tagName tag name
     * @return iterator of tagged note names
     * @throws TagNotFoundException if tag doesn't exist
     */
    Iterator<String> getTaggedNotes(String tagName) throws TagNotFoundException;

    /**
     * Gets trending tags (most used tags)
     * @return iterator of trending tag names
     */
    Iterator<String> getTrendingTags();

    /**
     * Gets notes filtered by type and date range
     * @param noteType type filter
     * @param startDate start date filter
     * @param endDate end date filter
     * @return iterator of filtered note names
     * @throws StartEndDateException if start date is after end date
     */
    Iterator<String> getNotes(NoteType noteType, LocalDate startDate, LocalDate endDate)
            throws StartEndDateException;

    /**
     * Validates and parses start date
     * @param dateStr date string
     * @return parsed LocalDate
     * @throws InvalidStartDateException if date format is invalid
     */
    LocalDate validateStartDate(String dateStr) throws InvalidStartDateException;

    /**
     * Validates and parses end date
     * @param dateEnd date string
     * @return parsed LocalDate
     * @throws InvalidEndDateException if date format is invalid
     */
    LocalDate validateEndDate(String dateEnd) throws InvalidEndDateException;

    /**
     * Deletes a note
     * @param name note name
     * @throws NoteNotFoundException if note doesn't exist
     */
    void deleteNote(String name) throws NoteNotFoundException;
}