package com.codechallange;

import com.codechallange.argument.ArgumentPolicyRunner;
import com.codechallange.argument.InvalidCommandFormatException;
import com.dropbox.core.DbxException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ApplicationRunnerTest extends SystemOutMockedTest{
    private static final String[] ARGS = new String[]{};

    @Mock
    private ArgumentPolicyRunner argumentPolicyRunner;

    @InjectMocks
    private ApplicationRunner applicationRunner;

    @Test
    public void shouldExecuteApplicationRunner() throws IOException, DbxException {
        applicationRunner.run(ARGS);

        verify(argumentPolicyRunner).runArgumentPolicies(ARGS);
    }

    @Test
    public void shouldPrintErrorFromDropBoxWhenDbxexceptionIsThrown() throws IOException, DbxException {
        String message = "dbxmessage";

        doThrow(new DbxException(message)).when(argumentPolicyRunner).runArgumentPolicies(ARGS);
        applicationRunner.run(ARGS);

        assertEquals(String.format(ApplicationRunner.ERROR_RETURNED_FROM_DROPBOX, message), outContent.toString());
    }

    @Test
    public void shouldPrintErrorOccuredWhenIoExceptionIsThrown() throws IOException, DbxException {
        String message = "iomessage";

        doThrow(new IOException(message)).when(argumentPolicyRunner).runArgumentPolicies(ARGS);
        applicationRunner.run(ARGS);

        assertEquals(String.format(ApplicationRunner.ERROR_OCCURED, message), outContent.toString());
    }

    @Test
    public void shouldPrintInvalidCommandErrorWhenInvalidCommandExceptionIsThrown() throws IOException, DbxException {
        doThrow(new InvalidCommandFormatException()).when(argumentPolicyRunner).runArgumentPolicies(ARGS);
        applicationRunner.run(ARGS);

        assertEquals(ApplicationRunner.ERROR_INVALID_COMMAND_FORMAT, outContent.toString());
    }


}