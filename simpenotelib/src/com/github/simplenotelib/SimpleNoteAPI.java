package com.github.simplenotelib;

import java.io.IOException;

public interface SimpleNoteAPI {

    public abstract void login() throws IOException;

    public abstract Note get(String key) throws IOException;

    public abstract Note add(Note note) throws IOException;

    public abstract String getEmail();

}