package com.codechallange.argument;

import com.codechallange.handler.CommandHandler;
import com.dropbox.core.DbxException;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@Order(value=1)
public class AccountInfoArgumentPolicy implements ArgumentPolicy {
    private static final List<Integer> ALLOWED_ARGUMENT_COUNT = Lists.newArrayList(2, 3);

    @Autowired
    private ArgumentValidator argumentValidator;
    @Autowired
    @Qualifier("accountInfoHandler")
    private CommandHandler accountInfoHandler;

    @Override
    public void processArguments(String[] args) throws DbxException, IOException {
        argumentValidator.validateArgumentCount(ALLOWED_ARGUMENT_COUNT, args.length);
        accountInfoHandler.handleCommand(args);
    }

    @Override
    public boolean usePolicy(String[] args) {
        return argumentValidator.checkFirstArgumentName("info", args);
    }
}
