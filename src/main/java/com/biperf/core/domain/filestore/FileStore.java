
package com.biperf.core.domain.filestore;

import java.io.Serializable;
import java.util.Date;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.enums.FileStoreType;
import com.biperf.core.domain.user.User;

public class FileStore extends BaseDomain implements Serializable
{
  private User user;
  private String userFileName;
  private String internalFileName;
  private FileStoreType fileStoreType;
  private Date dateGenerated;
  private boolean downloaded;

  public User getUser()
  {
    return user;
  }

  public void setUser( User user )
  {
    this.user = user;
  }

  public String getUserFileName()
  {
    return userFileName;
  }

  public void setUserFileName( String userFileName )
  {
    this.userFileName = userFileName;
  }

  public String getInternalFileName()
  {
    return internalFileName;
  }

  public void setInternalFileName( String internalFileName )
  {
    this.internalFileName = internalFileName;
  }

  public Date getDateGenerated()
  {
    return dateGenerated;
  }

  public void setDateGenerated( Date dateGenerated )
  {
    this.dateGenerated = dateGenerated;
  }

  public boolean isDownloaded()
  {
    return downloaded;
  }

  public void setDownloaded( boolean downloaded )
  {
    this.downloaded = downloaded;
  }

  public FileStoreType getFileStoreType()
  {
    return fileStoreType;
  }

  public void setFileStoreType( FileStoreType fileStoreType )
  {
    this.fileStoreType = fileStoreType;
  }

  @Override
  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }

    if ( ! ( object instanceof FileStore ) )
    {
      return false;
    }

    FileStore fileStore = (FileStore)object;

    if ( getInternalFileName() != null ? !getInternalFileName().equals( fileStore.getInternalFileName() ) : fileStore.getInternalFileName() != null )
    {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode()
  {
    return getInternalFileName() != null ? getInternalFileName().hashCode() : 0;
  }

}
