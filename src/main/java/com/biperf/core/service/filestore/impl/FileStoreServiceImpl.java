
package com.biperf.core.service.filestore.impl;

import java.sql.Timestamp;
import java.util.List;

import com.biperf.core.dao.filestore.FileStoreDAO;
import com.biperf.core.domain.enums.FileStoreType;
import com.biperf.core.domain.filestore.FileStore;
import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.domain.user.User;
import com.biperf.core.service.filestore.FileStoreService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.system.SystemVariableService;

public class FileStoreServiceImpl implements FileStoreService
{
  private UserService userService = null;
  private SystemVariableService systemVariableService = null;
  private FileStoreDAO fileStoreDAO = null;

  @Override
  public FileStore save( Long userId, FileStore fileStore )
  {
    FileStore fileStoreReturned = null;
    User user = userService.getUserById( userId );
    fileStore.setUser( user );
    fileStore.setDateGenerated( new Timestamp( System.currentTimeMillis() ) );
    fileStoreReturned = fileStoreDAO.save( fileStore );
    return fileStoreReturned;
  }

  public String generateInternalFileName( Long userId, FileStoreType type )
  {
    StringBuilder sb = new StringBuilder();

    sb.append( System.currentTimeMillis() );
    sb.append( "_" );
    sb.append( userId );
    sb.append( "." );

    if ( type != null )
    {
      sb.append( type.getCode() );
    }
    else
    {
      sb.append( "unknown" );
    }

    return sb.toString();
  }

  @Override
  public boolean isLargeAudienceReportGenerationEnabled()
  {
    PropertySetItem property = systemVariableService.getPropertyByName( SystemVariableService.ENABLE_LARGE_AUDIENCE_REPORT_GENERATION );
    if ( null != property )
    {
      return property.getBooleanVal();
    }
    else
    {
      return false;
    }
  }

  @Override
  public List<FileStore> getFileStoresForUser( Long userId )
  {
    return fileStoreDAO.findFileStoresForUser( userId );
  }

  @Override
  public FileStore get( Long fileStoreId )
  {
    return fileStoreDAO.get( fileStoreId );
  }

  public FileStoreDAO getFileStoreDAO()
  {
    return fileStoreDAO;
  }

  public void setFileStoreDAO( FileStoreDAO fileStoreDAO )
  {
    this.fileStoreDAO = fileStoreDAO;
  }

  public UserService getUserService()
  {
    return userService;
  }

  public void setUserService( UserService userService )
  {
    this.userService = userService;
  }

  public SystemVariableService getSystemVariableService()
  {
    return systemVariableService;
  }

  public void setSystemVariableService( SystemVariableService systemVariableService )
  {
    this.systemVariableService = systemVariableService;
  }
}
