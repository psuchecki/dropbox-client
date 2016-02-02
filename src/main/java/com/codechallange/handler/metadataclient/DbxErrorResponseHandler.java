package com.codechallange.handler.metadataclient;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestUtil;
import com.dropbox.core.http.HttpRequestor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

@Component
public class DbxErrorResponseHandler implements ResponseErrorHandler {
    private ResponseErrorHandler responseErrorHandler = new DefaultResponseErrorHandler();

    @Override
    public boolean hasError(ClientHttpResponse clientHttpResponse) throws IOException {
        return responseErrorHandler.hasError(clientHttpResponse);
    }

    @Override
    public void handleError(ClientHttpResponse clientHttpResponse) throws IOException {
        try {
            int status = clientHttpResponse.getStatusCode().value();
            HttpRequestor.Response dbxResponse = new HttpRequestor.Response(status, clientHttpResponse.getBody(), null);
            throw DbxRequestUtil.unexpectedStatus(dbxResponse);
        } catch (DbxException e) {
            throw new DbxExceptionWrapper(e);
        }
    }
}
