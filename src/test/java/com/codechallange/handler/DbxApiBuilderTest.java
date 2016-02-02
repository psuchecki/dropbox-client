package com.codechallange.handler;

import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxWebAuthNoRedirect;
import org.junit.Test;

import java.util.Locale;

import static com.codechallange.handler.AuthorizationHandlerTest.*;
import static com.codechallange.handler.DbxApiBuilder.CLIENT_IDENTIFIER;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DbxApiBuilderTest {

    private DbxApiBuilder apiBuilder = new DbxApiBuilder();

    @Test
    public void shouldBuildDbxClient() {
        Locale locale = Locale.forLanguageTag("pl-PL");

        DbxClient dbxClient = apiBuilder.getDbxClient(TOKEN, locale);

        assertEquals(dbxClient.getAccessToken(), TOKEN);
        assertEquals(locale.toString(), dbxClient.getRequestConfig().userLocale);
        assertEquals(CLIENT_IDENTIFIER, dbxClient.getRequestConfig().clientIdentifier);
    }

    @Test
    public void shouldBuildDbxWebNoRedirect() {
        DbxWebAuthNoRedirect dbxWebAuthNoRedirect = apiBuilder.getDbxWebAuthNoRedirect(KEY, SECRET);

        assertNotNull(dbxWebAuthNoRedirect);
    }
}