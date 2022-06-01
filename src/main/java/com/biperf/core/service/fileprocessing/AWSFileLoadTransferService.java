
package com.biperf.core.service.fileprocessing;

public interface AWSFileLoadTransferService
{
  public static final String BEAN_NAME = "awsFileLoadTransferService";

  public void transfer( String filename );
}
