package com.codechallange.handler.metadataclient;

import com.dropbox.core.DbxException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Locale;

@Component
public class DbxClientWithMetadata {
    private final Logger logger = LoggerFactory.getLogger(DbxClientWithMetadata.class);
    private static final String METADATA_API_PATH = "https://api.dropboxapi.com/1/metadata/auto";
    private static final String AUTH_HEADER = "Authorization";
    private static final String LOCALE_PARAM_NAME = "locale";
    private static final String BEARER_PREFIX = "Bearer ";

    @Autowired
    private RestTemplate restTemplate;

    public DbxMetadataEntry getMetadataEntries(String accessToken, String path, Locale locale)
            throws DbxException {
        HttpHeaders headers = new HttpHeaders();
        headers.add(AUTH_HEADER, BEARER_PREFIX + accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        String uri = UriComponentsBuilder.fromHttpUrl(METADATA_API_PATH)
                .queryParam(LOCALE_PARAM_NAME, locale.toLanguageTag())
                .path(path)
                .toUriString();
        logger.debug("Getting metadata entry using uri: {}", uri);
        try {
            ResponseEntity<DbxMetadataEntry> exchange =
                    restTemplate.exchange(uri, HttpMethod.GET, entity, DbxMetadataEntry.class);
            return exchange.getBody();
        } catch (DbxExceptionWrapper e) {
            throw e.getDbxException();
        }
    }
}
