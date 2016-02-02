package com.codechallange.handler;

import com.dropbox.core.DbxAccountInfo;
import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxException;
import com.google.common.base.Joiner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class AccountInfoHandler implements CommandHandler {
    private final Logger logger = LoggerFactory.getLogger(AccountInfoHandler.class);
    private static final int ARGS_COUNT_WITH_LOCALE = 3;
    private static final String VERIFIED_EMAIL = "verified";
    private static final String NOT_VERIFIED_EMAIL = "not verified";
    private static final String ACCOUNT_INFO_MESSAGE = "--------------------------------------------------------\n" +
            "User ID: %s\n" +
            "Display name: %s\n" +
            "Name: %s %s (%s)\n" +
            "E-mail: %s (%s)\n" +
            "Country: %s\n" +
            "Referral link: %s\n" +
            "--------------------------------------------------------";

    @Autowired
    private DbxApiBuilder apiBuilder;

    @Override
    public void handleCommand(String[] args) throws DbxException {
        logger.debug("Trying to get user info for args: {}", Joiner.on(" ").join(args));
        Locale locale = args.length == ARGS_COUNT_WITH_LOCALE ? Locale.forLanguageTag(args[2]) : Locale.getDefault();
        printAccountInfo(args[1], locale);
    }

    private void printAccountInfo(String accessToken, Locale locale) throws DbxException {
        DbxClient client = apiBuilder.getDbxClient(accessToken, locale);
        DbxAccountInfo accountInfo = client.getAccountInfo();
        DbxAccountInfo.NameDetails nameDetails = accountInfo.nameDetails;
        String emailVerified = accountInfo.emailVerified ? VERIFIED_EMAIL : NOT_VERIFIED_EMAIL;
        String message =
                String.format(ACCOUNT_INFO_MESSAGE, accountInfo.userId, accountInfo.displayName, nameDetails.givenName,
                        nameDetails.surname, nameDetails.familiarName, accountInfo.email, emailVerified,
                        accountInfo.country, accountInfo.referralLink);
        System.out.print(message);
        logger.debug("Printed account info for user id: {}", accountInfo.userId);
    }
}
