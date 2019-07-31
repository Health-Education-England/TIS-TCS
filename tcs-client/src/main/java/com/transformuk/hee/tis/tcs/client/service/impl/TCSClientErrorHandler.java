package com.transformuk.hee.tis.tcs.client.service.impl;

import java.io.IOException;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

public class TCSClientErrorHandler implements ResponseErrorHandler {

  public static boolean isError(HttpStatus status) {
    HttpStatus.Series series = status.series();
    return (HttpStatus.Series.CLIENT_ERROR.equals(series)
        || HttpStatus.Series.SERVER_ERROR.equals(series));
  }

  @Override
  public boolean hasError(ClientHttpResponse response) throws IOException {
    return isError(response.getStatusCode());
  }

  @Override
  public void handleError(ClientHttpResponse response) throws IOException {
    throw new IOException(IOUtils.toString(response.getBody(), "UTF-8"));
  }
}
