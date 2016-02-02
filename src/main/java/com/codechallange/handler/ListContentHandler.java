package com.codechallange.handler;

import com.codechallange.handler.metadataclient.DbxClientWithMetadata;
import com.codechallange.handler.metadataclient.DbxMetadataEntry;
import com.dropbox.core.DbxException;
import com.google.common.base.Joiner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

@Component
public class ListContentHandler implements CommandHandler {
    private final Logger logger = LoggerFactory.getLogger(ListContentHandler.class);
    private static final String LINE_SEPARATOR = "--------------------------------------------------------";
    private static final String INDENTATION = " - ";
    private static final int ARGS_COUNT_WITH_LOCALE = 4;

    @Autowired
    private DbxClientWithMetadata dbxClientWithMetadata;

    @Override
    public void handleCommand(String[] args) throws DbxException, IOException {
        logger.debug("Trying to list content for args: {}", Joiner.on(" ").join(args));
        Locale locale = args.length == ARGS_COUNT_WITH_LOCALE ? Locale.forLanguageTag(args[3]) : Locale.getDefault();
        printContentMetadata(args[1], args[2], locale);
    }

    private void printContentMetadata(String accessToken, String path, Locale locale) throws DbxException {
        DbxMetadataEntry metadataEntries = dbxClientWithMetadata.getMetadataEntries(accessToken, path, locale);

        StringBuilder metadataContentBuilder = new StringBuilder();
        metadataContentBuilder.append(LINE_SEPARATOR + "\n");
        metadataContentBuilder.append(metadataEntries.toMetadataListing(locale));
        List<DbxMetadataEntry> childEntries = metadataEntries.getChildren();
        childEntries.stream().map(childEntry -> childEntry.toMetadataListing(locale))
                .forEach(metadataListing -> metadataContentBuilder.append(INDENTATION).append(metadataListing));
        metadataContentBuilder.append(LINE_SEPARATOR);

        System.out.print(metadataContentBuilder);
        logger.debug("Printed content for: {}", metadataEntries.getRelativePath());
    }
}
