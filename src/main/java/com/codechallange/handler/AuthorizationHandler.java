package com.codechallange.handler;

import com.dropbox.core.*;
import com.google.common.base.Joiner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Component
public class AuthorizationHandler implements CommandHandler {
    private final Logger logger = LoggerFactory.getLogger(AuthorizationHandler.class);

    static final String COPY_AUTH_CODE_PROMPT_MESSAGE = "1. Go to: %s\n" +
            "2. Click \"Allow\" (you might have to log in first)\n" +
            "3. Copy the authorization code.";
    static final String ACCESS_TOKEN_DISPLAY_MESSAGE = "Your access token:\n%s";

    @Autowired
    private DbxApiBuilder apiBuilder;

    @Override
    public void handleCommand(String[] args) throws DbxException, IOException {
        logger.debug("Trying to authenticate for args: {}", Joiner.on(" ").join(args));
        authenticate(args[1], args[2]);
    }

    private void authenticate(String appKey, String appSecret) throws IOException, DbxException {
        DbxWebAuthNoRedirect webAuth = apiBuilder.getDbxWebAuthNoRedirect(appKey, appSecret);

        String authorizationCode = getAuthorizationCode(webAuth);
        DbxAuthFinish finish = webAuth.finish(authorizationCode);
        System.out.print(String.format(ACCESS_TOKEN_DISPLAY_MESSAGE, finish.accessToken));
        logger.debug("Got access token: {}", finish.accessToken);
    }

    private String getAuthorizationCode(DbxWebAuthNoRedirect webAuth) throws IOException {
        String authorizationUrl = webAuth.start();
        logger.debug("Authorization url: {}", authorizationUrl);
        System.out.println(String.format(COPY_AUTH_CODE_PROMPT_MESSAGE, authorizationUrl));
        try (BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in))) {
            return inputReader.readLine().trim();
        }
    }
}
