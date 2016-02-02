package com.codechallange;

import com.codechallange.argument.ArgumentPolicyRunner;
import com.codechallange.argument.InvalidCommandFormatException;
import com.dropbox.core.DbxException;
import com.google.common.base.Joiner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ApplicationRunner {
    private final Logger logger = LoggerFactory.getLogger(ApplicationRunner.class);

    static final String ERROR_RETURNED_FROM_DROPBOX = "Error returned from dropbox: %s\n" +
            "Please fix root cause of problem and try again";
    static final String ERROR_OCCURED = "Error occured: %s";
    static final String ERROR_INVALID_COMMAND_FORMAT =
            "Error: Invalid command format, please use one of commands:\n" +
            "1. java -jar dropbox-client.jar auth {appKey} {appSecret}\n" +
            "2. java -jar dropbox-client.jar info {accessToken} {locale}\n" +
            "3. java -jar dropbox-client.jar list {accessToken} {path} {locale}\n" +
            "Note: locale argument is optional";

    @Autowired
    private ArgumentPolicyRunner argumentPolicyRunner;

    public void run(String[] args) {
        try {
            argumentPolicyRunner.runArgumentPolicies(args);
        } catch (DbxException e) {
            logger.error("Dropbox error: ",e);
            System.out.print(String.format(ERROR_RETURNED_FROM_DROPBOX, e.getMessage()));
        } catch (IOException e) {
            logger.error("Error: ",e);
            System.out.print(String.format(ERROR_OCCURED, e.getMessage()));
        } catch (InvalidCommandFormatException e) {
            logger.warn("Invalid argument format for: {}", Joiner.on(" ").join(args));
            System.out.print(ERROR_INVALID_COMMAND_FORMAT);
        }
    }
}
