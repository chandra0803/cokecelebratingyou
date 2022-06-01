/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/AuditCreateInfo.java,v $
 */

package com.biperf.core.domain;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Class to hold the create audit information for domain objects.
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
public class AuditCreateInfo implements Serializable
{
  private static final long serialVersionUID = 3257008761007585332L;
  private Timestamp dateCreated;
  private Long createdBy;

  /**
   * getDateCreated from Audited info.
   * 
   * @return java.sql.Timestamp
   */
  public Timestamp getDateCreated()
  {
    return dateCreated;
  }

  /**
   * setDateCreated() sets the audit field dateCreated.
   * 
   * @param dateCreated java.sql.Timestamp
   */
  public void setDateCreated( Timestamp dateCreated )
  {
    this.dateCreated = dateCreated;
  }

  /**
   * Gets the created by audit info.
   * 
   * @return createdBy java.lang.String
   */
  public Long getCreatedBy()
  {
    return createdBy;
  }

  /**
   * setCreatedBy() sets the audit field createdBy
   * 
   * @param createdBy
   */
  public void setCreatedBy( Long createdBy )
  {
    this.createdBy = createdBy;
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
    if ( ! ( o instanceof AuditCreateInfo ) )
    {
      return false;
    }

    final AuditCreateInfo auditCreateInfo = (AuditCreateInfo)o;

    if ( getCreatedBy() != null ? !getCreatedBy().equals( auditCreateInfo.getCreatedBy() ) : auditCreateInfo.getCreatedBy() != null )
    {
      return false;
    }
    if ( getDateCreated() != null ? !getDateCreated().equals( auditCreateInfo.getDateCreated() ) : auditCreateInfo.getDateCreated() != null )
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
    result = getDateCreated() != null ? getDateCreated().hashCode() : 0;
    result = 29 * result + ( getCreatedBy() != null ? getCreatedBy().hashCode() : 0 );
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
    buf.append( "AuditCreateInfo" );
    buf.append( "{dateCreated=" ).append( getDateCreated() );
    buf.append( ",createdBy=" ).append( getCreatedBy() );
    buf.append( '}' );
    return buf.toString();
  }
}
