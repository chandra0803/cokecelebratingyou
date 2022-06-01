/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/AuditUpdateInfo.java,v $
 */

package com.biperf.core.domain;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Class to hold the update audit information for domain objects.
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>dunne</td>
 * <td>Apr 19, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class AuditUpdateInfo implements Serializable
{
  private Timestamp dateModified;
  private Long modifiedBy;

  /**
   * Gets the modified date from Audit info.
   * 
   * @return java.sql.Timestamp
   */
  public Timestamp getDateModified()
  {
    return dateModified;
  }

  /**
   * Sets the modified date for audit info.
   * 
   * @param dateModified java.sql.Timestamp
   */
  public void setDateModified( Timestamp dateModified )
  {
    this.dateModified = dateModified;
  }

  /**
   * Gets the modified by audit info.
   * 
   * @return modifiedBy java.lang.Long
   */
  public Long getModifiedBy()
  {
    return modifiedBy;
  }

  /**
   * Sets the modified by audit info.
   * 
   * @param modifiedBy java.lang.Long
   */
  public void setModifiedBy( Long modifiedBy )
  {
    this.modifiedBy = modifiedBy;
  }

  /**
   * Overridden from
   * 
   * @see java.lang.Object#equals(java.lang.Object)
   * @param o
   * @return boolean
   */
  public boolean equals( Object o )
  {
    if ( this == o )
    {
      return true;
    }
    if ( ! ( o instanceof AuditUpdateInfo ) )
    {
      return false;
    }

    final AuditUpdateInfo auditUpdateInfo = (AuditUpdateInfo)o;

    if ( getDateModified() != null ? !getDateModified().equals( auditUpdateInfo.getDateModified() ) : auditUpdateInfo.getDateModified() != null )
    {
      return false;
    }
    if ( getModifiedBy() != null ? !getModifiedBy().equals( auditUpdateInfo.getModifiedBy() ) : auditUpdateInfo.getModifiedBy() != null )
    {
      return false;
    }

    return true;
  }

  /**
   * Overridden from
   * 
   * @see java.lang.Object#hashCode()
   * @return int
   */
  public int hashCode()
  {
    int result;
    result = getDateModified() != null ? getDateModified().hashCode() : 0;
    result = 29 * result + ( getModifiedBy() != null ? getModifiedBy().hashCode() : 0 );
    return result;
  }

  /**
   * Overridden from
   * 
   * @see java.lang.Object#toString()
   * @return Stirng
   */
  public String toString()
  {
    final StringBuffer buf = new StringBuffer();
    buf.append( "AuditUpdateInfo" );
    buf.append( "{dateModified=" ).append( getDateModified() );
    buf.append( ",modifiedBy=" ).append( getModifiedBy() );
    buf.append( '}' );
    return buf.toString();
  }
}
