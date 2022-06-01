
package com.biperf.core.service.ssi.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.ssi.SSIContestFileUploadService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.strategy.FileUploadStrategy;
import com.biperf.core.utils.AwsUtils;
import com.biperf.core.utils.ImageUtils;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.value.ssi.SSIContestFileUploadValue;

public class SSIContestFileUploadServiceImpl implements SSIContestFileUploadService
{
  private static final Log log = LogFactory.getLog( SSIContestServiceImpl.class );
  private static final int MEGABYTES_TO_BYTES_MULTIPLIER = 1024 * 1024;

  private SystemVariableService systemVariableService;
  private List<String> acceptableExtentions;
  private FileUploadStrategy appDataDirFileUploadStrategy;
  private FileUploadStrategy webdavFileUploadStrategy;
  private FileUploadStrategy webdavADCFileUploadStrategy;
  private FileUploadStrategy awsFileUploadStrategy;

  public SSIContestFileUploadValue uploadContestDocument( SSIContestFileUploadValue data ) throws ServiceErrorException
  {
    if ( validFileData( data ) )
    {
      try
      {
        data.setFull( ImageUtils.getSSIContestInfoDetailPath( data.getId(), data.getName() ) );
        if ( AwsUtils.isAws() )
        {
          awsFileUploadStrategy.uploadFileData( data.getFull(), data.getData() );
        }
        else
        {
          appDataDirFileUploadStrategy.uploadFileData( data.getFull(), data.getData() );
          moveFileToWebdav( data.getFull() );
        }
      }
      catch( Exception e )
      {
        log.error( "File Upload Failed " + e );
        throw new ServiceErrorException( "ssi_contest.generalInfo.FILE_UPLOAD_FAILED" );
      }
    }
    else
    {
      throw new ServiceErrorException( "ssi_contest.generalInfo.FILE_UPLOAD_INVALID" ); // file-type-or-size-invalid
    }
    return data;
  }

  private boolean moveFileToWebdav( String mediaUrl )
  {
    try
    {
      byte[] media = appDataDirFileUploadStrategy.getFileData( mediaUrl );
      webdavFileUploadStrategy.uploadFileData( mediaUrl, media );

      appDataDirFileUploadStrategy.delete( mediaUrl );
      return true;
    }
    catch( Throwable e )
    {
      // Must not have the file in AppDataDir of server executing this process
    }
    return false;
  }

  public boolean deleteMediaFromWebdav( String filePath )
  {
    if ( StringUtil.isEmpty( filePath ) )
    {
      return false;
    }

    try
    {
      try
      {
        byte[] data = null;
        if ( AwsUtils.isAws() )
        {
          data = awsFileUploadStrategy.getFileData( filePath );
        }
        else
        {
          data = webdavFileUploadStrategy.getFileData( filePath );
        }
        if ( null == data )
        {
          // If file data does not exist, there is nothing to delete
          // Might have got deleted in previous attempt
          return true;
        }
      }
      catch( Throwable e )
      {
        // If file data does not exist, there is nothing to delete
        // Might have got deleted in previous attempt
        return true;
      }
      if ( AwsUtils.isAws() )
      {
        return awsFileUploadStrategy.delete( filePath );
      }
      // Delete Media from Stage(WebDav)
      return webdavFileUploadStrategy.delete( filePath );
    }
    catch( Throwable e )
    {
      return false;
    }
  }

  private boolean validFileData( SSIContestFileUploadValue data )
  {
    String extension = ImageUtils.getFileExtension( data.getName() );
    if ( !acceptableExtentions.contains( extension.toLowerCase() ) )
    {
      return false;
    }
    // Check file size
    if ( SSIContestFileUploadValue.TYPE_PDF.equals( data.getType() ) )
    {
      int sizeLimit = MEGABYTES_TO_BYTES_MULTIPLIER * systemVariableService.getPropertyByName( SystemVariableService.SYSTEM_PDF_SIZE_LIMIT ).getIntVal();
      if ( data.getSize() > sizeLimit )
      {
        return false;
      }
    }
    else
    {
      int sizeLimit = MEGABYTES_TO_BYTES_MULTIPLIER * systemVariableService.getPropertyByName( SystemVariableService.SYSTEM_IMG_SIZE_LIMIT ).getIntVal();
      double lowerSizeLimit = MEGABYTES_TO_BYTES_MULTIPLIER * .001;
      if ( data.getSize() > sizeLimit )
      {
        return false;
      }
      else if ( data.getSize() <= lowerSizeLimit )
      {
        return false;
      }
    }

    return true;
  }

  public void moveFileToWevdavADC( SSIContestFileUploadValue data ) throws ServiceErrorException
  {
    try
    {
      data.setFull( ImageUtils.getSSIContestProgressFilePath( data.getType(), data.getId(), data.getName() ) );
      String medialUrl = data.getFull();
      byte[] media = null;
      if ( AwsUtils.isAws() )
      {
        log.info( "moveFileToWebdavADC medialUrl: " + medialUrl );
        awsFileUploadStrategy.uploadFileData( medialUrl, data.getData() );
        media = awsFileUploadStrategy.getFileData( medialUrl );
      }
      else
      {
        log.info( "moveFileToWebdavADC medialUrl: " + medialUrl );
        appDataDirFileUploadStrategy.uploadFileData( medialUrl, data.getData() );
        media = appDataDirFileUploadStrategy.getFileData( medialUrl );
      }

      if ( !AwsUtils.isAws() )
      {
        String adcFileUrl = getSubFolderName() + data.getAdcFileName();
        log.error( "adcFileUrl: " + adcFileUrl );
        boolean isUploaded = webdavADCFileUploadStrategy.uploadFileData( adcFileUrl, media );
        if ( isUploaded )
        {
          appDataDirFileUploadStrategy.delete( medialUrl );
        }
      }
    }
    catch( Exception e )
    {
      throw new ServiceErrorException( "ssi_contest.generalInfo.FILE_UPLOAD_FAILED", e );
    }
  }

  private String getSubFolderName()
  {
    String subfolder = systemVariableService.getPropertyByName( SystemVariableService.GLOBAL_FILE_PROCESSING_SUBFOLDER ).getStringVal();
    String incomingFolder = ImageUtils.getADCIncomingFolderName( subfolder );
    return incomingFolder;
  }

  public void setSystemVariableService( SystemVariableService systemVariableService )
  {
    this.systemVariableService = systemVariableService;
  }

  public void setAcceptableExtentions( List<String> acceptableExtentions )
  {
    this.acceptableExtentions = acceptableExtentions;
  }

  public void setAppDataDirFileUploadStrategy( FileUploadStrategy appDataDirFileUploadStrategy )
  {
    this.appDataDirFileUploadStrategy = appDataDirFileUploadStrategy;
  }

  public void setWebdavFileUploadStrategy( FileUploadStrategy webdavFileUploadStrategy )
  {
    this.webdavFileUploadStrategy = webdavFileUploadStrategy;
  }

  public void setWebdavADCFileUploadStrategy( FileUploadStrategy webdavADCFileUploadStrategy )
  {
    this.webdavADCFileUploadStrategy = webdavADCFileUploadStrategy;
  }

  public FileUploadStrategy getWebdavADCFileUploadStrategy()
  {
    return webdavADCFileUploadStrategy;
  }

  public void setAwsFileUploadStrategy( FileUploadStrategy awsFileUploadStrategy )
  {
    this.awsFileUploadStrategy = awsFileUploadStrategy;
  }

}
