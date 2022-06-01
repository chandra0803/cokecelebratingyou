/*
 * (c) 2017 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.domain.company;

import java.util.UUID;

import com.biperf.core.domain.BaseDomain;

/**
 * 
 * 
 * @author palaniss
 * @since Nov 1, 2018
 * 
 */
public class Company extends BaseDomain
{

  private static final long serialVersionUID = 1L;
  private UUID companyId;
  private String companyIdentifier;
  private String companyName;
  private String companyEmail;

  public UUID getCompanyId()
  {
    return companyId;
  }

  public void setCompanyId( UUID companyId )
  {
    this.companyId = companyId;
  }

  public String getCompanyIdentifier()
  {
    return companyIdentifier;
  }

  public void setCompanyIdentifier( String companyIdentifier )
  {
    this.companyIdentifier = companyIdentifier;
  }

  public String getCompanyName()
  {
    return companyName;
  }

  public void setCompanyName( String companyName )
  {
    this.companyName = companyName;
  }

  public String getCompanyEmail()
  {
    return companyEmail;
  }

  public void setCompanyEmail( String companyEmail )
  {
    this.companyEmail = companyEmail;
  }

  @Override
  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }
    if ( object == null )
    {
      return false;
    }

    Company pgrm = (Company)object;
    if ( companyId == null || pgrm.getCompanyId() == null )
    {
      return false;
    }

    if ( companyId.equals( pgrm.getCompanyId() ) )
    {
      return true;
    }

    return false;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( companyId == null ? 0 : companyId.hashCode() );
    return result;
  }

}
