
package com.biperf.core.strategy;

import javax.servlet.http.HttpServletResponse;

import com.biperf.core.exception.ServiceErrorException;

public interface ReportDownloadStrategy extends Strategy
{
  public static final String WEBDAV = "webdavReportDownloadStrategy";

  public byte[] getFileData( String filePath ) throws ServiceErrorException;

  public String getUrlLocationPrefix();

  public void writeFileData( String filePath, final HttpServletResponse response ) throws ServiceErrorException;

}
