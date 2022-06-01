
package com.biperf.core.domain.client;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.claim.NominationClaim;
import com.biperf.core.domain.enums.TcccClaimFileStatusType;

/**
 * TcccClaimFile.
 * 
 * This class is created as part of Client Customization for WIP #39189
 * 
 * @author dudam
 * @since Nov 16, 2017
 * @version 1.0
 */
public class TcccClaimFile extends BaseDomain
{
  private static final long serialVersionUID = 1L;
  private NominationClaim claim;
  private String fileName;
  private String fileUrl;
  private TcccClaimFileStatusType status;

  public NominationClaim getClaim()
  {
    return claim;
  }

  public void setClaim( NominationClaim claim )
  {
    this.claim = claim;
  }

  public String getFileName()
  {
    return fileName;
  }

  public void setFileName( String fileName )
  {
    this.fileName = fileName;
  }

  public String getFileUrl()
  {
    return fileUrl;
  }

  public void setFileUrl( String fileUrl )
  {
    this.fileUrl = fileUrl;
  }

  public TcccClaimFileStatusType getStatus()
  {
    return status;
  }

  public void setStatus( TcccClaimFileStatusType status )
  {
    this.status = status;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( ( fileName == null ) ? 0 : fileName.hashCode() );
    result = prime * result + ( ( fileUrl == null ) ? 0 : fileUrl.hashCode() );
    result = prime * result + ( ( status == null ) ? 0 : status.hashCode() );
    return result;
  }

  @Override
  public boolean equals( Object obj )
  {
    if ( this == obj )
      return true;
    if ( obj == null )
      return false;
    if ( getClass() != obj.getClass() )
      return false;
    TcccClaimFile other = (TcccClaimFile)obj;
    if ( fileName == null )
    {
      if ( other.fileName != null )
        return false;
    }
    else if ( !fileName.equals( other.fileName ) )
      return false;
    if ( fileUrl == null )
    {
      if ( other.fileUrl != null )
        return false;
    }
    else if ( !fileUrl.equals( other.fileUrl ) )
      return false;
    if ( status == null )
    {
      if ( other.status != null )
        return false;
    }
    else if ( !status.equals( other.status ) )
      return false;
    return true;
  }

}
// Client customization for WIP #39189 ends
