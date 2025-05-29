package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Interface for working with content notes
 */
public  interface ContentNote extends Note {

    /**
     * Updates the content of the note
     * @param content new content
     */
    void updateContent(String content);

    /**
     * Gets the content of the note
     * @return note content
     */
    String getContent();

    /**
     * Loads a list of links
     * @param links list of links to load
     */
    void loadLinks(ArrayList<String> links);

    /**
     * Removes a link from the list
     * @param link link to remove
     */
    void removeLink(String link);

    /**
     * Checks if a link exists
     * @param link link to check
     * @return true if link exists, false otherwise
     */
    boolean hasLink(String link);

    /**
     * Gets an iterator for traversing links
     * @return links iterator
     */
    Iterator<String> getLinks();

    /**
     * Gets the number of links
     * @return number of links
     */
    int getNumberOfLinks();

    /**
     * Adds a tag to the note
     * @param tag tag to add
     */
    void addTag(String tag);

    /**
     * Removes a tag from the note
     * @param tag tag to remove
     */
    void removeTag(String tag);

    /**
     * Gets the number of tags of this note
     * @return number of tags
     */
    int getNumberOfTags();

    /**
     * Gets an iterator for traversing tags of this note
     * @return tags iterator
     */
    Iterator<String> getTagsIterator();

    /**
     * Gets the last update date
     * @return last update date
     */
    LocalDate getLastUpdateDate();

    /**
     * Updates the date and ID
     * @param date new update date
     * @param ID new update ID
     */
    void updateDate(LocalDate date, int ID);

    /**
     * Gets the last update ID
     * @return last update ID
     */
    int getLastUpdateID();
}