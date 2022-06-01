
package com.biperf.core.service.mtc;

import java.net.URL;

import org.json.JSONException;

import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.SAO;
import com.biperf.core.value.mtc.v1.upload.UploadResponse;

public interface MTCService extends SAO
{

  public static String BEAN_NAME = "mtcService";

  public UploadResponse uploadVideo( URL url ) throws ServiceErrorException, JSONException;
}
