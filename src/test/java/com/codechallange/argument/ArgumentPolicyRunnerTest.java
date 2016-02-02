package com.codechallange.argument;

import com.codechallange.argument.ArgumentPolicy;
import com.codechallange.argument.ArgumentPolicyRunner;
import com.codechallange.argument.InvalidCommandFormatException;
import com.codechallange.config.ArgumentPolicyTestConfig;
import com.codechallange.handler.CommandHandler;
import com.dropbox.core.DbxException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.io.IOException;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class,
        classes = {ArgumentPolicyTestConfig.class})
public class ArgumentPolicyRunnerTest {

    @Autowired
    private List<ArgumentPolicy> argumentPolicies;

    @Autowired
    @Qualifier("accountInfoHandler")
    private CommandHandler accountInfoHandler;
    @Autowired
    @Qualifier("listContentHandler")
    private CommandHandler listContentHandler;
    @Autowired
    @Qualifier("authorizationHandler")
    private CommandHandler authorizationHandler;

    @Autowired
    private ArgumentPolicyRunner argumentPolicyRunner;

    @Test(expected = InvalidCommandFormatException.class)
    public void shouldThrowInvalidCommandFormatExceptionWhenNoArgs() throws IOException, DbxException {
        argumentPolicyRunner.runArgumentPolicies(new String[]{});
    }

    @Test(expected = InvalidCommandFormatException.class)
    public void shouldThrowInvalidCommandFormatExceptionWhenUnknownCommand() throws IOException, DbxException {
        argumentPolicyRunner.runArgumentPolicies(new String[]{"unknowncommand"});
    }

    @Test(expected = InvalidCommandFormatException.class)
    public void shouldThrowInvalidCommandFormatExceptionWhenTooLittleArgsForAuth() throws IOException, DbxException {
        argumentPolicyRunner.runArgumentPolicies(new String[]{"auth", "key"});
    }

    @Test(expected = InvalidCommandFormatException.class)
    public void shouldThrowInvalidCommandFormatExceptionWhenTooManyArgsForAuth() throws IOException, DbxException {
        argumentPolicyRunner.runArgumentPolicies(new String[]{"auth", "key", "secret", "toomany"});
    }

    @Test(expected = InvalidCommandFormatException.class)
    public void shouldThrowInvalidCommandFormatExceptionWhenTooLittleArgsForInfo() throws IOException, DbxException {
        argumentPolicyRunner.runArgumentPolicies(new String[]{"info"});
    }

    @Test(expected = InvalidCommandFormatException.class)
    public void shouldThrowInvalidCommandFormatExceptionWhenTooManyArgsForInfo() throws IOException, DbxException {
        argumentPolicyRunner.runArgumentPolicies(new String[]{"info", "accessToken", "locale", "toomany"});
    }

    @Test(expected = InvalidCommandFormatException.class)
    public void shouldThrowInvalidCommandFormatExceptionWhenTooLittleArgsForList() throws IOException, DbxException {
        argumentPolicyRunner.runArgumentPolicies(new String[]{"list", "accessToken"});
    }

    @Test(expected = InvalidCommandFormatException.class)
    public void shouldThrowInvalidCommandFormatExceptionWhenTooManyArgsForList() throws IOException, DbxException {
        argumentPolicyRunner.runArgumentPolicies(new String[]{"list", "accessToken", "path", "locale", "toomany"});
    }

    @Test
    public void shouldRunAuthCommandForAuthArg() throws IOException, DbxException {
        String[] args = {"auth", "key", "secret"};

        argumentPolicyRunner.runArgumentPolicies(args);

        verify(authorizationHandler).handleCommand(args);
        verifyNoMoreInteractions(listContentHandler, accountInfoHandler);
    }

    @Test
    public void shouldRunInfoCommandForInfoArgWithoutLocale() throws IOException, DbxException {
        String[] args = {"info", "accessToken"};

        argumentPolicyRunner.runArgumentPolicies(args);

        verify(accountInfoHandler).handleCommand(args);
        verifyNoMoreInteractions(listContentHandler, authorizationHandler);
    }

    @Test
    public void shouldRunInfoCommandForInfoArgWithLocale() throws IOException, DbxException {
        String[] args = {"info", "accessToken", "locale"};

        argumentPolicyRunner.runArgumentPolicies(args);

        verify(accountInfoHandler).handleCommand(args);
        verifyNoMoreInteractions(listContentHandler, authorizationHandler);
    }

    @Test
    public void shouldRunListCommandForListArgWithoutLocale() throws IOException, DbxException {
        String[] args = {"list", "accessToken", "path"};

        argumentPolicyRunner.runArgumentPolicies(args);

        verify(listContentHandler).handleCommand(args);
        verifyNoMoreInteractions(accountInfoHandler, authorizationHandler);
    }

    @Test
    public void shouldRunListCommandForListArgWithLocale() throws IOException, DbxException {
        String[] args = {"list", "accessToken", "path", "locale"};

        argumentPolicyRunner.runArgumentPolicies(args);

        verify(listContentHandler).handleCommand(args);
        verifyNoMoreInteractions(accountInfoHandler, authorizationHandler);
    }

}
