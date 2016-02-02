package com.codechallange.handler;

import com.codechallange.SystemOutMockedTest;
import com.codechallange.config.CommandHandlerConfig;
import com.dropbox.core.DbxException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import static com.codechallange.handler.AuthorizationHandlerTest.ACCESS_TOKEN;
import static com.codechallange.handler.AuthorizationHandlerTest.INVALID_ACCESS_TOKEN;
import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class,
        classes = {CommandHandlerConfig.class})
public class AccountInfoHandlerIntegrationTest extends SystemOutMockedTest {
    private static final String EXPECTED_ACCOUNT_INFO = "--------------------------------------------------------\n" +
            "User ID: 531784557\n" +
            "Display name: dropboxchallangefirstname dropboxchallangelastname\n" +
            "Name: dropboxchallangefirstname dropboxchallangelastname (dropboxchallangefirstname)\n" +
            "E-mail: sastimucre@thrma.com (not verified)\n" +
            "Country: PH\n" +
            "Referral link: https://db.tt/9IT4hyfx\n" +
            "--------------------------------------------------------";

    @Autowired
    private AccountInfoHandler accountInfoHandler;

    @Test
    public void shouldGetAccountInfoWithDefaultLocale() throws DbxException {
        String[] args = {"info", ACCESS_TOKEN};
        accountInfoHandler.handleCommand(args);

        assertEquals(EXPECTED_ACCOUNT_INFO, outContent.toString());
    }

    @Test
    public void shouldGetAccountInfoWithLocale() throws DbxException {
        String[] args = {"info", ACCESS_TOKEN, "pl-PL"};
        accountInfoHandler.handleCommand(args);

        assertEquals(EXPECTED_ACCOUNT_INFO, outContent.toString());
    }

    @Test(expected = DbxException.class)
    public void shouldThrowDbxExceptionWhenInvalidAccessToken() throws DbxException {
        String[] args = {"info", INVALID_ACCESS_TOKEN};
        accountInfoHandler.handleCommand(args);
    }
}