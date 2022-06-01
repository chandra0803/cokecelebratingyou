/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/employer/Employer.java,v $
 */

package com.biperf.core.domain.employer;

import com.biperf.core.domain.Address;
import com.biperf.core.domain.BaseDomain;

/**
 * Domain object to represent the Employer.
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
 * <td>crosenquest</td>
 * <td>Apr 20, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class Employer extends BaseDomain
{
  // Fields

  private String name;
  private boolean active;
  private String statusReason;
  private String federalTaxId;
  private String stateTaxId;
  private String phoneNumber;
  private String phoneExtension;
  private String countryPhoneCode;
  private int activeParticipantCount;
  private Address address = new Address();

  public Address getAddress()
  {
    return address;
  }

  public void setAddress( Address address )
  {
    this.address = address;
  }

  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  public boolean isActive()
  {
    return active;
  }

  public void setActive( boolean active )
  {
    this.active = active;
  }

  public String getStatusReason()
  {
    return statusReason;
  }

  public void setStatusReason( String statusReason )
  {
    this.statusReason = statusReason;
  }

  public String getFederalTaxId()
  {
    return federalTaxId;
  }

  public void setFederalTaxId( String federalTaxId )
  {
    this.federalTaxId = federalTaxId;
  }

  public String getStateTaxId()
  {
    return stateTaxId;
  }

  public void setStateTaxId( String stateTaxId )
  {
    this.stateTaxId = stateTaxId;
  }

  public String getPhoneNumber()
  {
    return phoneNumber;
  }

  public void setPhoneNumber( String phoneNumber )
  {
    this.phoneNumber = phoneNumber;
  }

  public int getActiveParticipantCount()
  {
    return activeParticipantCount;
  }

  public void setActiveParticipantCount( int activeParticipantCount )
  {
    this.activeParticipantCount = activeParticipantCount;
  }

  /**
   * Ensure equality between this and the object param. Overridden from
   * 
   * @see com.biperf.core.domain.BaseDomain#equals(java.lang.Object)
   * @param object
   * @return boolean
   */
  public boolean equals( Object object )
  {

    if ( this == object )
    {
      return true;
    }

    if ( ! ( object instanceof Employer ) )
    {
      return false;
    }

    Employer employer = (Employer)object;

    if ( this.getName() != null ? !this.getName().equals( employer.getName() ) : employer.getName() != null )
    {
      return false;
    }

    return true;

  }

  /**
   * Generated hashCode required by Hiberate for this. Overridden from
   * 
   * @see com.biperf.core.domain.BaseDomain#hashCode()
   * @return int
   */
  public int hashCode()
  {
    return this.getName() != null ? this.getName().hashCode() : 0;
  }

  public void setPhoneExtension( String phoneExtension )
  {
    this.phoneExtension = phoneExtension;
  }

  public String getPhoneExtension()
  {
    return phoneExtension;
  }

  public void setCountryPhoneCode( String countryPhoneCode )
  {
    this.countryPhoneCode = countryPhoneCode;
  }

  public String getCountryPhoneCode()
  {
    return countryPhoneCode;
  }

}
