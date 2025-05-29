package model;

import java.time.LocalDate;

/**
 * Interface for working with permanent notes
 */
public interface PermanentNote extends ContentNote {

    /**
     * Records an update with date and ID
     * @param date update date
     * @param ID update ID
     */
    void recordUpdate(LocalDate date, int ID);
}