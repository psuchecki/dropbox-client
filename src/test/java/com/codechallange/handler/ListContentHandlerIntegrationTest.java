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

import java.io.IOException;

import static com.codechallange.handler.AuthorizationHandlerTest.ACCESS_TOKEN;
import static com.codechallange.handler.AuthorizationHandlerTest.INVALID_ACCESS_TOKEN;
import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class,
        classes = {CommandHandlerConfig.class})
public class ListContentHandlerIntegrationTest extends SystemOutMockedTest {

    private static final String EXPECTED_EMPTY_DIR_CONTENT =
            "--------------------------------------------------------\n" +
                    "/lvl3 : dir, modified at: \"February 2, 2016\"\n" +
                    "--------------------------------------------------------";

    private static final String EXPECTED_ROOT_DIR_CONTENT =
            "--------------------------------------------------------\n" +
            "/ : dir, modified at: \"\"\n" +
            " - /getting started.pdf : file, 1.1 MB, application/pdf, modified at: \"February 2, 2016\"\n" +
            " - /test_folder : dir, modified at: \"February 2, 2016\"\n" +
            " - /test_folder2 : dir, modified at: \"February 2, 2016\"\n" +
            "--------------------------------------------------------";

    private static final String EXPECTED_DIR_CONTENT =
            "--------------------------------------------------------\n" +
                    "/lvl2 : dir, modified at: \"February 2, 2016\"\n" +
                    " - /Code_Challenge_Dropbox_client.pdf : file, 10.9 KB, application/pdf, modified at: \"February 2, 2016\"\n" +
                    " - /lvl3 : dir, modified at: \"February 2, 2016\"\n" +
                    " - /przychody.jpg : file, 38.5 KB, image/jpeg, modified at: \"February 2, 2016\"\n" +
                    " - /Specifications.pdf : file, 550.1 KB, application/pdf, modified at: \"February 2, 2016\"\n" +
                    "--------------------------------------------------------";
    private static final String EXPECTED_DIR_CONTENT_IN_PL =
            "--------------------------------------------------------\n" +
            "/lvl2 : dir, modified at: \"2 lutego 2016\"\n" +
            " - /Code_Challenge_Dropbox_client.pdf : file, 10,9 KB, application/pdf, modified at: \"2 lutego 2016\"\n" +
            " - /lvl3 : dir, modified at: \"2 lutego 2016\"\n" +
            " - /przychody.jpg : file, 38,5 KB, image/jpeg, modified at: \"2 lutego 2016\"\n" +
            " - /Specifications.pdf : file, 550,1 KB, application/pdf, modified at: \"2 lutego 2016\"\n" +
            "--------------------------------------------------------";
    private static final String EXPECTED_FILE_CONTENT =
            "--------------------------------------------------------\n" +
            "/przychody.jpg : file, 38.5 KB, image/jpeg, modified at: \"February 2, 2016\"\n" +
            "--------------------------------------------------------";

    private static final String EXPECTED_FILE_CONTENT_IN_PL =
            "--------------------------------------------------------\n" +
            "/przychody.jpg : file, 38,5 KB, image/jpeg, modified at: \"2 lutego 2016\"\n" +
            "--------------------------------------------------------";

    @Autowired
    private ListContentHandler listContentHandler;

    @Test
    public void shouldListDirContentForRoot() throws IOException, DbxException {
        String[] args = {"list", ACCESS_TOKEN, "/"};
        listContentHandler.handleCommand(args);

        assertEquals(EXPECTED_ROOT_DIR_CONTENT, outContent.toString());
    }

    @Test
    public void shouldListDirContentWithDefaultLocale() throws IOException, DbxException {
        String[] args = {"list", ACCESS_TOKEN, "/test_folder/lvl2"};
        listContentHandler.handleCommand(args);

        assertEquals(EXPECTED_DIR_CONTENT, outContent.toString());
    }

    @Test
    public void shouldListEmptyDirContentWithDefaultLocale() throws IOException, DbxException {
        String[] args = {"list", ACCESS_TOKEN, "/test_folder/lvl2/lvl3"};
        listContentHandler.handleCommand(args);

        assertEquals(EXPECTED_EMPTY_DIR_CONTENT, outContent.toString());
    }

    @Test
    public void shouldListDirContentWithLocale() throws IOException, DbxException {
        String[] args = {"list", ACCESS_TOKEN, "/test_folder/lvl2", "pl-PL"};
        listContentHandler.handleCommand(args);

        assertEquals(EXPECTED_DIR_CONTENT_IN_PL, outContent.toString());
    }

    @Test
    public void shouldListFileContentWithDefaultLocale() throws IOException, DbxException {
        String[] args = {"list", ACCESS_TOKEN, "/test_folder/lvl2/przychody.jpg"};
        listContentHandler.handleCommand(args);

        assertEquals(EXPECTED_FILE_CONTENT, outContent.toString());
    }

    @Test
    public void shouldListFileContentWithLocale() throws IOException, DbxException {
        String[] args = {"list", ACCESS_TOKEN, "/test_folder/lvl2/przychody.jpg", "pl-PL"};
        listContentHandler.handleCommand(args);

        assertEquals(EXPECTED_FILE_CONTENT_IN_PL, outContent.toString());
    }

    @Test(expected = DbxException.class)
    public void shouldThrowDbxExceptionWhenInvalidAccessToken() throws DbxException, IOException {
        String[] args = {"list", INVALID_ACCESS_TOKEN, "/test_folder/lvl2/przychody.jpg"};
        listContentHandler.handleCommand(args);
    }

    @Test(expected = DbxException.class)
    public void shouldThrowDbxExceptionWhenFileNotExists() throws DbxException, IOException {
        String[] args = {"list", ACCESS_TOKEN, "/test_folder/lvl2/notExistingFile.jpg"};
        listContentHandler.handleCommand(args);
    }

    @Test(expected = DbxException.class)
    public void shouldThrowDbxExceptionWhenWrongPath() throws DbxException, IOException {
        String[] args = {"list", ACCESS_TOKEN, "dsadsadsada//sdasda/"};
        listContentHandler.handleCommand(args);
    }
}
