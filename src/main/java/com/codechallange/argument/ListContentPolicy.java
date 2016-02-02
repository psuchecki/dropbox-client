package com.codechallange.argument;

import com.codechallange.handler.CommandHandler;
import com.dropbox.core.DbxException;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;

@Component
@Order(value=3)
public class ListContentPolicy implements ArgumentPolicy {
    private static final ArrayList<Integer> ALLOWED_ARGUMENT_COUNT = Lists.newArrayList(3, 4);

    @Autowired
    private ArgumentValidator argumentValidator;
    @Autowired
    @Qualifier("listContentHandler")
    private CommandHandler listContentHandler;

    @Override
    public void processArguments(String[] args) throws DbxException, IOException {
        argumentValidator.validateArgumentCount(ALLOWED_ARGUMENT_COUNT, args.length);
        listContentHandler.handleCommand(args);
    }

    @Override
    public boolean usePolicy(String[] args) {
        return argumentValidator.checkFirstArgumentName("list", args);
    }
}
