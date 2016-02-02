package com.codechallange.handler;

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWebAuthNoRedirect;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class DbxApiBuilder {
    static final String CLIENT_IDENTIFIER = "DropboxClientChallange";

    public DbxClient getDbxClient(String accessToken, Locale locale) {
        return new DbxClient(getConfig(locale), accessToken);
    }

    public DbxWebAuthNoRedirect getDbxWebAuthNoRedirect(String appKey, String appSecret) {
        DbxAppInfo appInfo = new DbxAppInfo(appKey, appSecret);
        return new DbxWebAuthNoRedirect(getDefaultConfig(), appInfo);
    }

    private DbxRequestConfig getDefaultConfig() {
        return getConfig(Locale.getDefault());
    }

    private DbxRequestConfig getConfig(Locale locale) {
        return new DbxRequestConfig(CLIENT_IDENTIFIER, locale.toString());
    }
}
