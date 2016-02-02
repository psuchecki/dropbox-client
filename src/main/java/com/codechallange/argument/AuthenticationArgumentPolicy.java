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
@Order(value=2)
public class AuthenticationArgumentPolicy implements ArgumentPolicy {
    private static final String AUTH_ARGUMENT_NAME = "auth";
    private static final List<Integer> ALLOWED_ARGUMENT_COUNT = Lists.newArrayList(3);

    @Autowired
    private ArgumentValidator argumentValidator;
    @Autowired
    @Qualifier("authorizationHandler")
    private CommandHandler authorizationHandler;

    @Override
    public void processArguments(String[] args) throws IOException, DbxException {
        argumentValidator.validateArgumentCount(ALLOWED_ARGUMENT_COUNT, args.length);
        authorizationHandler.handleCommand(args);
    }

    @Override
    public boolean usePolicy(String[] args) {
        return argumentValidator.checkFirstArgumentName(AUTH_ARGUMENT_NAME, args);
    }
}
