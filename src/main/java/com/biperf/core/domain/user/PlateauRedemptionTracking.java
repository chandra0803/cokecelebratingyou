
package com.biperf.core.domain.user;

import java.sql.Timestamp;

import com.biperf.core.domain.BaseDomain;

/**
 * 
 * PlateauRedemptionTracking.
 * 
 * @author Rameshj
 * @since Aug 25, 2017
 * @version 1.0
 */

public class PlateauRedemptionTracking extends BaseDomain
{

  private static final long serialVersionUID = 1L;
  private Long merchOrderId;
  private Long userId;
  private Long version;
  private Timestamp dateCreated;
  private Long createdBy;
  private Timestamp dateModified;
  private Long modifiedBy;

  /**
   * @return the version
   */
  public Long getVersion()
  {
    return version;
  }

  /**
   * @param version the version to set
   */
  public void setVersion( Long version )
  {
    this.version = version;
  }

  public Long getMerchOrderId()
  {
    return merchOrderId;
  }

  public void setMerchOrderId( Long merchOrderId )
  {
    this.merchOrderId = merchOrderId;
  }

  public Long getUserId()
  {
    return userId;
  }

  public void setUserId( Long userId )
  {
    this.userId = userId;
  }

  /**
   * @return the dateCreated
   */
  public Timestamp getDateCreated()
  {
    return dateCreated;
  }

  /**
   * @param dateCreated the dateCreated to set
   */
  public void setDateCreated( Timestamp dateCreated )
  {
    this.dateCreated = dateCreated;
  }

  /**
   * @return the createdBy
   */
  public Long getCreatedBy()
  {
    return createdBy;
  }

  /**
   * @param createdBy the createdBy to set
   */
  public void setCreatedBy( Long createdBy )
  {
    this.createdBy = createdBy;
  }

  /**
   * @return the dateModified
   */
  public Timestamp getDateModified()
  {
    return dateModified;
  }

  /**
   * @param dateModified the dateModified to set
   */
  public void setDateModified( Timestamp dateModified )
  {
    this.dateModified = dateModified;
  }

  /**
   * @return the modifiedBy
   */
  public Long getModifiedBy()
  {
    return modifiedBy;
  }

  /**
   * @param modifiedBy the modifiedBy to set
   */
  public void setModifiedBy( Long modifiedBy )
  {
    this.modifiedBy = modifiedBy;
  }

  /*
   * (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( ( createdBy == null ) ? 0 : createdBy.hashCode() );
    result = prime * result + ( ( dateCreated == null ) ? 0 : dateCreated.hashCode() );
    result = prime * result + ( ( dateModified == null ) ? 0 : dateModified.hashCode() );
    result = prime * result + ( ( merchOrderId == null ) ? 0 : merchOrderId.hashCode() );
    result = prime * result + ( ( modifiedBy == null ) ? 0 : modifiedBy.hashCode() );
    result = prime * result + ( ( userId == null ) ? 0 : userId.hashCode() );
    result = prime * result + ( ( version == null ) ? 0 : version.hashCode() );
    return result;
  }

  /*
   * (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals( Object obj )
  {
    if ( this == obj )
    {
      return true;
    }
    if ( obj == null )
    {
      return false;
    }
    if ( getClass() != obj.getClass() )
    {
      return false;
    }
    PlateauRedemptionTracking other = (PlateauRedemptionTracking)obj;
    if ( createdBy == null )
    {
      if ( other.createdBy != null )
      {
        return false;
      }
    }
    else if ( !createdBy.equals( other.createdBy ) )
    {
      return false;
    }
    if ( dateCreated == null )
    {
      if ( other.dateCreated != null )
      {
        return false;
      }
    }
    else if ( !dateCreated.equals( other.dateCreated ) )
    {
      return false;
    }
    if ( dateModified == null )
    {
      if ( other.dateModified != null )
      {
        return false;
      }
    }
    else if ( !dateModified.equals( other.dateModified ) )
    {
      return false;
    }
    if ( merchOrderId == null )
    {
      if ( other.merchOrderId != null )
      {
        return false;
      }
    }
    else if ( !merchOrderId.equals( other.merchOrderId ) )
    {
      return false;
    }
    if ( modifiedBy == null )
    {
      if ( other.modifiedBy != null )
      {
        return false;
      }
    }
    else if ( !modifiedBy.equals( other.modifiedBy ) )
    {
      return false;
    }
    if ( userId == null )
    {
      if ( other.userId != null )
      {
        return false;
      }
    }
    else if ( !userId.equals( other.userId ) )
    {
      return false;
    }
    if ( version == null )
    {
      if ( other.version != null )
      {
        return false;
      }
    }
    else if ( !version.equals( other.version ) )
    {
      return false;
    }
    return true;
  }

}
