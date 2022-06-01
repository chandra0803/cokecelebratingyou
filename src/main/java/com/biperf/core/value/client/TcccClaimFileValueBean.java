
package com.biperf.core.value.client;

import java.io.Serializable;
import java.sql.Timestamp;

import com.biperf.core.domain.client.TcccClaimFile;
import com.biperf.core.domain.enums.TcccClaimFileStatusType;

/**
 * 
 * TcccClaimImageValueBean.
 * This class is created as part of Client Customization for WIP #39189 
 * @author dudam
 * @since Nov 16, 2017
 * @version 1.0
 */
public class TcccClaimFileValueBean implements Serializable
{
  private static final long serialVersionUID = 7591045883906255555L;
  private Long claimFileId;
  private long version;
  private long dateCreated;
  private String createdBy;
  private String fileName;
  private String url;
  private String description;
  private TcccClaimFileStatusType status;

  public TcccClaimFileValueBean()
  {

  }

  public TcccClaimFileValueBean( String url, String description )
  {
    this.url = url;
    this.description = description;
  }

  public void load( TcccClaimFile tcccClaimFile )
  {
    if ( tcccClaimFile != null )
    {
      claimFileId = tcccClaimFile.getId();
      createdBy = tcccClaimFile.getAuditCreateInfo().getCreatedBy().toString();
      dateCreated = tcccClaimFile.getAuditCreateInfo().getDateCreated().getTime();
      version = tcccClaimFile.getVersion().longValue();
      fileName = tcccClaimFile.getFileName();
      url = tcccClaimFile.getFileUrl();
      status = tcccClaimFile.getStatus();
      description = tcccClaimFile.getFileName();
    }
  }

  public TcccClaimFile toDomainObject()
  {
    TcccClaimFile claimFile = new TcccClaimFile();
    if ( claimFileId != null && claimFileId.longValue() != 0 )
    {
      claimFile.setId( claimFileId );
      claimFile.setVersion( new Long( version ) );
      claimFile.getAuditCreateInfo().setCreatedBy( Long.valueOf( this.createdBy ) );
      claimFile.getAuditCreateInfo().setDateCreated( new Timestamp( this.dateCreated ) );
    }
    claimFile.setFileName( fileName );
    claimFile.setFileUrl( url );
    claimFile.setStatus( status );
    return claimFile;
  }

  public Long getClaimFileId()
  {
    return claimFileId;
  }

  public void setClaimFileId( Long claimFileId )
  {
    this.claimFileId = claimFileId;
  }

  public long getVersion()
  {
    return version;
  }

  public void setVersion( long version )
  {
    this.version = version;
  }

  public long getDateCreated()
  {
    return dateCreated;
  }

  public void setDateCreated( long dateCreated )
  {
    this.dateCreated = dateCreated;
  }

  public String getCreatedBy()
  {
    return createdBy;
  }

  public void setCreatedBy( String createdBy )
  {
    this.createdBy = createdBy;
  }

  public String getFileName()
  {
    return fileName;
  }

  public void setFileName( String fileName )
  {
    this.fileName = fileName;
  }

  public String getUrl()
  {
    return url;
  }

  public void setUrl( String url )
  {
    this.url = url;
  }

  public String getDescription()
  {
    return description;
  }

  public void setDescription( String description )
  {
    this.description = description;
  }

  public TcccClaimFileStatusType getStatus()
  {
    return status;
  }

  public void setStatus( TcccClaimFileStatusType status )
  {
    this.status = status;
  }

}
