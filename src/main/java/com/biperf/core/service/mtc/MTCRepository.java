
package com.biperf.core.service.mtc;

import java.net.URL;

import com.biperf.core.value.mtc.v1.upload.UploadResponse;

public interface MTCRepository
{

  public UploadResponse uploadVideo( URL file, String transcoderProfile );

}
