package com.codechallange.argument;

import com.dropbox.core.DbxException;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Order(value=Integer.MAX_VALUE)
public class InvalidCommandPolicy implements ArgumentPolicy {
    @Override
    public void processArguments(String[] args) throws IOException, DbxException {
        throw new InvalidCommandFormatException();
    }

    @Override
    public boolean usePolicy(String[] args) {
        return true;
    }
}
