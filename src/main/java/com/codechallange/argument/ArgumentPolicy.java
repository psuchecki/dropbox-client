package com.codechallange.argument;

import com.dropbox.core.DbxException;

import java.io.IOException;

public interface ArgumentPolicy {
    void processArguments(String[] args) throws IOException, DbxException;
    boolean usePolicy(String[] args);
}
