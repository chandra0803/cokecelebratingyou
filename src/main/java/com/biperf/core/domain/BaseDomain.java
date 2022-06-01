/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/BaseDomain.java,v $
 */

package com.biperf.core.domain;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;

import com.biperf.core.exception.BeaconRuntimeException;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * BaseDomain is to be extended for all Domain objects the AuditInterface gives the Audit
 * capabilities storing modified dates, etc...
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
 * <td>jdunne</td>
 * <td>Feb 18, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public abstract class BaseDomain implements AuditCreateInterface, AuditUpdateInterface, Serializable
{
  private java.lang.Long id;
  private Long version;
  private AuditCreateInfo auditCreateInfo = new AuditCreateInfo();
  private AuditUpdateInfo auditUpdateInfo = new AuditUpdateInfo();

  /**
   * BaseDomain Constuctor
   */
  protected BaseDomain()
  {
    // default constructor
  }

  /**
   * BaseDomain constructor for building a specific domain with id and version.
   * 
   * @param id
   * @param version
   */
  protected BaseDomain( Long id, Long version )
  {
    this.id = id;
    this.version = version;
  }

  /**
   * BaseDomain only taking in an id.
   * 
   * @param id
   */
  protected BaseDomain( Long id )
  {
    this.id = id;
  }

  /**
   * Method to reset id, version and auditing information to original values. Used for cloning
   * objects which extend this.
   */
  protected void resetBaseDomain()
  {
    this.setId( null );
    this.setVersion( new Long( 0 ) );
    this.setAuditCreateInfo( new AuditCreateInfo() );
    this.setAuditUpdateInfo( new AuditUpdateInfo() );
  }

  /**
   * @return Returns the id.
   */
  public java.lang.Long getId()
  {
    return id;
  }

  /**
   * @param id The id to set.
   */
  public void setId( Long id )
  {
    this.id = id;
  }

  /**
   * Return the version of the domain obj.
   * 
   * @return Long version
   */
  @JsonIgnore
  public Long getVersion()
  {
    return version;
  }

  /**
   * Set the version of the domain.
   * 
   * @param version
   */
  public void setVersion( Long version )
  {
    this.version = version;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.domain.AuditCreateInterface#getAuditCreateInfo()
   * @return AuditCreateInfo
   */
  @JsonIgnore
  public AuditCreateInfo getAuditCreateInfo()
  {
    return auditCreateInfo;
  }

  /**
   * setAuditCreateInfo
   * 
   * @param auditCreateInfo
   */
  public void setAuditCreateInfo( AuditCreateInfo auditCreateInfo )
  {
    this.auditCreateInfo = auditCreateInfo;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.domain.AuditUpdateInterface#getAuditUpdateInfo()
   * @return AuditUpdateInfo
   */
  @JsonIgnore
  public AuditUpdateInfo getAuditUpdateInfo()
  {
    return auditUpdateInfo;
  }

  /**
   * setAuditUpdateInfo
   * 
   * @param auditUpdateInfo
   */
  public void setAuditUpdateInfo( AuditUpdateInfo auditUpdateInfo )
  {
    this.auditUpdateInfo = auditUpdateInfo;
  }

  /**
   * Overridden from
   * 
   * @see java.lang.Object#equals(java.lang.Object)
   * @param object
   * @return boolean
   */
  public abstract boolean equals( Object object );

  /**
   * Overridden from
   * 
   * @see java.lang.Object#hashCode()
   * @return int
   */
  public abstract int hashCode();

  /**
   * @return true if this is a 'new' object - meaning it is not yet in the db
   */
  public boolean isNew()
  {
    return getId() == null;
  }

  /**
   * Overridden from
   * 
   * @see java.lang.Object#toString()
   * @return String
   */
  public String toString()
  {
    final StringBuffer buf = new StringBuffer();
    buf.append( getClass().getName() );
    buf.append( "{id=" ).append( id );
    buf.append( ",version=" ).append( version );
    buf.append( '}' );
    return buf.toString();
  }

  /**
   * Iterates through a List of domain objects, looking for the particular ID. Will throw an
   * exception if it's not found.
   * 
   * @param domainObjects
   * @param idToSearchFor
   * @throws com.biperf.core.exception.BeaconRuntimeException
   * @return BaseDomain
   */
  public static BaseDomain getBusinessObjectWithId( Collection domainObjects, Long idToSearchFor )
  {

    if ( idToSearchFor != null && domainObjects != null )
    {

      for ( Iterator iterator = domainObjects.iterator(); iterator.hasNext(); )
      {
        BaseDomain domain = (BaseDomain)iterator.next();
        if ( idToSearchFor.equals( domain.getId() ) )
        {
          return domain;
        }
      }

    }

    throw new BeaconRuntimeException( "object not found. Looking for " + idToSearchFor + " in " + domainObjects );
  }

  /**
   * Compare objects' ids for equality.
   * 
   * @param rhs
   * @throws NullPointerException if "this" object's id is null or if the compared object is null.
   * @return true if the objects are of the same class and if the objects' ids are equal
   */
  public boolean equalsId( BaseDomain rhs )
  {
    return this.getClass().equals( rhs.getClass() ) && this.getId().equals( rhs.getId() );
  }
}
