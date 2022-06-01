/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/user/UserPhone.java,v $
 */

package com.biperf.core.domain.user;

import java.util.UUID;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.enums.PhoneType;
import com.biperf.core.domain.enums.VerificationStatusType;
import com.biperf.core.service.country.CountryService;
import com.biperf.core.utils.BeanLocator;

/**
 * UserPhone.
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
 * <td>zahler</td>
 * <td>Apr 25, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class UserPhone extends BaseDomain
{
  /**
   * serialVersionUID
   */
  private static final long serialVersionUID = 3258131375264707123L;

  private User user;
  private PhoneType phoneType;
  private String phoneNbr;
  private String phoneExt;
  private Boolean isPrimary;
  private String countryPhoneCode;
  private String displayCountryPhoneCode;
  private VerificationStatusType verificationStatus;
  private UUID rosterPhoneId;

  /**
   * Builds a String representation of this class. Overridden from
   * 
   * @see java.lang.Object#toString()
   * @return String
   */
  public String toString()
  {
    final StringBuffer buf = new StringBuffer();
    buf.append( "UserPhone [" );
    buf.append( "{id=" + super.getId() + "}, " );
    buf.append( "{user=" + this.getUser() + "}, " );
    buf.append( "{phoneType=" + this.getPhoneType() + "}, " );
    buf.append( "{countryPhoneCode=" + this.getCountryPhoneCode() + "}, " );
    buf.append( "{phoneNbr=" + this.getPhoneNbr() + "}, " );
    buf.append( "{phoneExt=" + this.getPhoneExt() + "}, " );
    buf.append( "]" );
    return buf.toString();
  }

  /**
   * Does equals on the Business Key ( userId and phoneType ) Overridden from
   * 
   * @see java.lang.Object#toString()
   * @param object
   * @return boolean
   */
  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }
    if ( ! ( object instanceof UserPhone ) )
    {
      return false;
    }

    final UserPhone userPhone = (UserPhone)object;

    if ( getUser() != null ? !getUser().equals( userPhone.getUser() ) : userPhone.getUser() != null )
    {
      return false;
    }
    if ( getPhoneType() != null ? !getPhoneType().equals( userPhone.getPhoneType() ) : userPhone.getPhoneType() != null )
    {
      return false;
    }
    if ( getPhoneNbr() != null ? !getPhoneNbr().equals( userPhone.getPhoneNbr() ) : userPhone.getPhoneNbr() != null )
    {
      return false;
    }
    return true;
  }

  /**
   * Does hashCode on the Business Key ( userId and phoneType ) Overridden from
   * 
   * @see com.biperf.core.domain.BaseDomain#hashCode()
   * @return int
   */
  public int hashCode()
  {
    if ( getUser() != null && getPhoneType() != null )
    {
      return 29 * getUser().hashCode() + getPhoneType().hashCode();
    }
    return 0;
  }

  public String getPhoneExt()
  {
    return phoneExt;
  }

  public void setPhoneExt( String phoneExt )
  {
    this.phoneExt = phoneExt;
  }

  public String getPhoneNbr()
  {
    return phoneNbr;
  }

  public void setPhoneNbr( String phoneNbr )
  {
    this.phoneNbr = phoneNbr;
  }

  public PhoneType getPhoneType()
  {
    return phoneType;
  }

  public void setPhoneType( PhoneType phoneType )
  {
    this.phoneType = phoneType;
  }

  public User getUser()
  {
    return user;
  }

  public void setUser( User user )
  {
    this.user = user;
  }

  public Boolean getIsPrimary()
  {
    return isPrimary;
  }

  public void setIsPrimary( Boolean isPrimary )
  {
    this.isPrimary = isPrimary;
  }

  public boolean isPrimary()
  {
    if ( isPrimary != null )
    {
      return isPrimary.booleanValue();
    }
    return false;
  }

  public void setCountryPhoneCode( String countryPhoneCode )
  {
    this.countryPhoneCode = countryPhoneCode;
  }

  public String getCountryPhoneCode()
  {
    return countryPhoneCode;
  }

  public UUID getRosterPhoneId()
  {
    return rosterPhoneId;
  }

  public void setRosterPhoneId( UUID rosterPhoneId )
  {
    this.rosterPhoneId = rosterPhoneId;
  }

  public String getDisplayCountryPhoneCode()
  {
    this.displayCountryPhoneCode = getCountryService().getCountryByCode( this.countryPhoneCode ).getPhoneCountryCode();
    return displayCountryPhoneCode;
  }

  public VerificationStatusType getVerificationStatus()
  {
    return verificationStatus;
  }

  public void setVerificationStatus( VerificationStatusType verificationStatus )
  {
    this.verificationStatus = verificationStatus;
  }

  private CountryService getCountryService()
  {
    return (CountryService)BeanLocator.getBean( CountryService.BEAN_NAME );
  }
}
