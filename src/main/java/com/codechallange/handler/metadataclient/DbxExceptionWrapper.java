package com.codechallange.handler.metadataclient;

import com.dropbox.core.DbxException;

public class DbxExceptionWrapper extends RuntimeException {
    private DbxException dbxException;

    public DbxExceptionWrapper(DbxException dbxException) {
        super();
        this.dbxException = dbxException;
    }

    public DbxException getDbxException() {
        return dbxException;
    }
}
