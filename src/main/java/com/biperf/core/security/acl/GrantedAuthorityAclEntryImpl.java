/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/security/acl/GrantedAuthorityAclEntryImpl.java,v $
 */

package com.biperf.core.security.acl;

import java.io.Serializable;

import org.springframework.security.core.GrantedAuthority;

/**
 * Basic concrete implementation of a {@link GrantedAuthority}.
 * <p>
 * Stores a <code>String</code> representation of an authority granted to the {@link AclEntry}
 * object.
 * </p>
 * 
 * @author Ashok Attada
 *
 */
public class GrantedAuthorityAclEntryImpl implements GrantedAuthority, Serializable
{
  // ~ Instance fields ========================================================

  // aclCode will be of the form "ACL_" + CODE where CODE is string
  private String aclCode;
  private AclEntry aclEntry;

  // ~ Constructors ===========================================================

  /**
   * @param aclCode
   * @param aclEntry
   */
  public GrantedAuthorityAclEntryImpl( String aclCode, AclEntry aclEntry )
  {
    super();
    this.aclCode = aclCode;
    this.aclEntry = aclEntry;
  }

  /**
   * 
   */
  protected GrantedAuthorityAclEntryImpl()
  {
    throw new IllegalArgumentException( "Cannot use default constructor" );
  }

  // ~ Methods ================================================================

  /**
   * Overridden from
   * 
   * @see org.acegisecurity.GrantedAuthority#getAuthority()
   * @return aclCode
   */
  public String getAuthority()
  {
    return this.aclCode;
  }

  /**
   * @return aclEntry
   */
  public AclEntry getAclEntry()
  {
    return this.aclEntry;

  }

  /**
   * @return aclCode
   */
  public String getAclCode()
  {
    return this.aclCode;
  }

  /**
   * Checks equality of the object parameter to this.
   * 
   * @param o
   * @return boolean
   */

  public boolean equals( Object o )
  {
    if ( this == o )
    {
      return true;
    }
    if ( ! ( o instanceof GrantedAuthorityAclEntryImpl ) )
    {
      return false;
    }

    final GrantedAuthorityAclEntryImpl grantedAuthorityAclEntryImpl = (GrantedAuthorityAclEntryImpl)o;

    if ( this.getAclCode() != null && !this.getAclCode().equals( grantedAuthorityAclEntryImpl.aclCode ) )
    {
      return false;
    }

    if ( this.getAclEntry() != null && !this.getAclEntry().equals( grantedAuthorityAclEntryImpl.aclEntry ) )
    {
      return false;
    }

    return true;
  }

  /**
   * Define the hashCode from the id. Overridden from
   * 
   * @see com.biperf.core.domain.BaseDomain#hashCode()
   * @return int
   */
  public int hashCode()
  {
    int hashCode = 0;
    hashCode += getAclCode() != null ? getAclCode().hashCode() : 0;
    hashCode += getAclEntry() != null ? getAclEntry().hashCode() : 0;
    return hashCode;
  }

  /**
   * Builds a String representation of this class. Overridden from
   * 
   * @see java.lang.Object#toString()
   * @return String
   */
  public String toString()
  {
    final StringBuffer buf = new StringBuffer();
    buf.append( "GrantedAuthorityAclEntryImpl [" );
    buf.append( "{aclCode =" + this.getAclCode() + "}" );
    buf.append( "]" );

    return buf.toString();
  }

}
