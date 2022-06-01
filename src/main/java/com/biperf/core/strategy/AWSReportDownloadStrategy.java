
package com.biperf.core.strategy;

import javax.servlet.http.HttpServletResponse;

import com.biperf.core.exception.ServiceErrorException;

public interface AWSReportDownloadStrategy extends Strategy
{

  public static final String AWS = "awsReportDownloadStrategy";

  public void awsWriteFileData( String filePath, final HttpServletResponse response ) throws ServiceErrorException;

}
