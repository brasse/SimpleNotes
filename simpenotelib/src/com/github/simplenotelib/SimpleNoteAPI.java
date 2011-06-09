package com.github.simplenotelib;

import java.io.IOException;

public interface SimpleNoteAPI {

    /**
     * Login to the simple notes service
     * @throws IOException
     */
    public abstract void login() throws IOException;

    /**
     * Get a note based on it's key as returned by the SimpleNotes service
     * @param key the key to use when finding the note
     * @return the note found by key
     * @throws IOException
     */
    public abstract Note get(String key) throws IOException;

    /**
     * Sync a note to the SimpleNotes service
     * @param note the note to sync
     * @return The note that was synced, updated with the information added
     * by the SimpleNotes service
     * @throws IOException
     */
    public abstract Note add(Note note) throws IOException;

    /**
     * Set the username used for all transactions with the SimpleNote service
     */
    public abstract void setUserName(String userName);
    
    /**
     * Get the username used for all transactions with the SimpleNote service
     * @return the username used with the SimpleNote service
     */
    public abstract String getUserName();
    
    /**
     * Set the password to use for the account
     */
    public abstract void setPassword(String password);

}