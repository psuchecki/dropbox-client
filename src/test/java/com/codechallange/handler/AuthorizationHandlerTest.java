package com.codechallange.handler;

import com.codechallange.SystemOutMockedTest;
import com.dropbox.core.DbxAuthFinish;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxWebAuthNoRedirect;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AuthorizationHandlerTest extends SystemOutMockedTest {
    static final String KEY = "fakeKey";
    static final String SECRET = "fakeSecret";
    static final String TOKEN = "fakeToken";
    static final String CODE = "fakeCode";
    static final String URL = "fakeUrl";

    //user:sastimucre@thrma.com pass:dropboxchallangepassword - access token doesn't expire in dropbox
    //do not regenerate access token for this user
    static final String ACCESS_TOKEN = "JD0NGAo6L0AAAAAAAAAACAwT2PYbfVLL0GLRS5xIg3z-a0H413x5K7x5gIGf8mLD";
    static final String INVALID_ACCESS_TOKEN = "invalidAccessToken";

    @Mock
    private DbxApiBuilder api;

    @Mock
    private DbxWebAuthNoRedirect dbxWebAuthNoRedirect;

    @Mock
    private InputStream exceptionThrowinStream;

    @InjectMocks
    private AuthorizationHandler authorizationHandler;

    @Before
    public void setUp() {
        when(api.getDbxWebAuthNoRedirect(KEY, SECRET)).thenReturn(dbxWebAuthNoRedirect);
        InputStream stubInputStream = IOUtils.toInputStream(CODE);
        System.setIn(stubInputStream);
    }

    @Test
    public void shouldPrintValidAccessToken() throws IOException, DbxException {
        String[] args = {"auth", KEY, SECRET};

        when(dbxWebAuthNoRedirect.start()).thenReturn(URL);
        when(dbxWebAuthNoRedirect.finish(CODE)).thenReturn(new DbxAuthFinish(TOKEN, null, null));
        authorizationHandler.handleCommand(args);

        String expectedMessage = String.format(AuthorizationHandler.COPY_AUTH_CODE_PROMPT_MESSAGE +
                "\r\n" + AuthorizationHandler.ACCESS_TOKEN_DISPLAY_MESSAGE, URL, TOKEN);
        assertEquals(expectedMessage, outContent.toString());
    }

    @Test(expected = DbxException.class)
    public void shouldThrowExceptionWhenDropboxReturnException() throws IOException, DbxException {
        String[] args = {"auth", KEY, SECRET};

        when(dbxWebAuthNoRedirect.start()).thenReturn(URL);
        doThrow(new DbxException("message")).when(dbxWebAuthNoRedirect).finish(CODE);
        authorizationHandler.handleCommand(args);
    }

    @Test(expected = IOException.class)
    public void shouldThrowIoExceptionWhenInputStreamFail() throws IOException, DbxException {
        String[] args = {"auth", KEY, SECRET};

        when(dbxWebAuthNoRedirect.start()).thenReturn(URL);
        doThrow(new IOException()).when(exceptionThrowinStream).read();
        System.setIn(exceptionThrowinStream);
        authorizationHandler.handleCommand(args);
    }
}