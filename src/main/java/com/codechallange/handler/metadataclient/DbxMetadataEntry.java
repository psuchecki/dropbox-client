package com.codechallange.handler.metadataclient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DbxMetadataEntry {
    public static final String BACKSLASH = "/";
    private static String FILE_METADATA_LISTING = "%s : file, %s, %s, modified at: \"%s\"\n";
    private static String FOLDER_METADATA_LISTING = "%s : dir, modified at: \"%s\"\n";

    private String path;
    private String size;
    @JsonProperty(value="mime_type")
    private String mimeType;
    private Date modified;
    private List<DbxMetadataEntry> contents = Lists.newArrayList();
    @JsonProperty(value="is_dir")
    private boolean isDir;

    public String toMetadataListing(Locale locale){
        return isDir ? String.format(FOLDER_METADATA_LISTING, getRelativePath(), getModifiedDate(locale)) :
                String.format(FILE_METADATA_LISTING, getRelativePath(), size, mimeType, getModifiedDate(locale));
    }

    private String getModifiedDate(Locale locale) {
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.LONG, locale);
        return modified == null ? "" : dateFormat.format(modified);
    }

    public String getRelativePath() {
        return BACKSLASH + Iterables.getLast(Splitter.on(BACKSLASH).split(path));
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public void setContents(List<DbxMetadataEntry> contents) {
        this.contents = contents;
    }

    public List<DbxMetadataEntry> getChildren() {
        return contents;
    }
}
