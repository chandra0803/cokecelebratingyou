
package com.biperf.core.strategy;

import java.io.InputStream;

import com.biperf.core.exception.ServiceErrorException;

public interface FileUploadStrategy extends Strategy
{
  public static final String APPDATADIR = "appDataDirFileUploadStrategy";
  public static final String WEBDAV = "webdavFileUploadStrategy";

  public boolean uploadFileData( String filePath, byte[] data ) throws ServiceErrorException;

  public boolean uploadFileData( String filePath, InputStream inputStream ) throws ServiceErrorException;

  public byte[] getFileData( String filePath ) throws ServiceErrorException;

  public boolean delete( String filePath ) throws ServiceErrorException;

  public String getUrlLocationPrefix();

  public byte[] getFileDataforPushProcess( String filePath ) throws ServiceErrorException;//Client customization for WIP #57733
}
