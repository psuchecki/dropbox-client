package com.codechallange.handler;

import com.dropbox.core.DbxException;

import java.io.IOException;

public interface CommandHandler {
    void handleCommand(String args[]) throws DbxException, IOException;
}
