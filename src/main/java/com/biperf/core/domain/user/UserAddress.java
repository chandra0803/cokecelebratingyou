/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/user/UserAddress.java,v $
 */

package com.biperf.core.domain.user;

import java.util.UUID;

import com.biperf.core.domain.Address;
import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.enums.AddressType;

/**
 * User Address domain object which represents the user's addresses.
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
 * <td>robinsra</td>
 * <td>Apr 22, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class UserAddress extends BaseDomain
{

  /**
   * serialVersionUID
   */
  private static final long serialVersionUID = 3761967172612010040L;

  private User user;
  private AddressType addressType;
  private Address address;
  private Boolean isPrimary;
  private UUID rosterAddressId;

  /**
   * @return value of address property
   */
  public Address getAddress()
  {
    return address;
  }

  /**
   * @param address value for address property
   */
  public void setAddress( Address address )
  {
    this.address = address;
  }

  /**
   * @return value of addressType property
   */
  public AddressType getAddressType()
  {
    return addressType;
  }

  /**
   * @param addressType value for addressType property
   */
  public void setAddressType( AddressType addressType )
  {
    this.addressType = addressType;
  }

  /**
   * @return Boolean value of isPrimary property
   */
  public Boolean getIsPrimary()
  {
    return isPrimary;
  }

  /**
   * @param isPrimary value for isPrimary property
   */
  public void setIsPrimary( Boolean isPrimary )
  {
    this.isPrimary = isPrimary;
  }

  public UUID getRosterAddressId()
  {
    return rosterAddressId;
  }

  public void setRosterAddressId( UUID rosterAddressId )
  {
    this.rosterAddressId = rosterAddressId;
  }

  /**
   * Tells if the user address is primary or not
   * 
   * @return boolean - true if primary, false if not primary
   */
  public boolean isPrimary()
  {
    if ( isPrimary != null )
    {
      return isPrimary.booleanValue();
    }
    return false;
  }

  /**
   * Tells if the user address is business address or not
   * 
   * @return boolean - true if type is business, false if not  
   */
  public boolean isBusinessAddress()
  {
    if ( this.addressType != null && addressType.getCode().equals( AddressType.BUSINESS_TYPE ) )
    {
      return true;
    }
    return false;
  }

  /**
   * @return value of user property
   */
  public User getUser()
  {
    return user;
  }

  /**
   * @param user value for user property
   */
  public void setUser( User user )
  {
    this.user = user;
  }

  /**
   * Define the hashcode from the combo of address type and user id Overridden from
   * 
   * @see com.biperf.core.domain.BaseDomain#hashCode()
   * @return int
   */
  public int hashCode()
  {
    int result1;
    int result2;
    int finalresult;
    result1 = getAddressType() != null ? getAddressType().hashCode() : 0;
    result2 = getUser() != null ? getUser().hashCode() : 0;
    finalresult = 29 * result1 + result2;
    return finalresult;
  }

  /**
   * Checks equality of the object parameter to this. Overridden from
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

    if ( ! ( object instanceof UserAddress ) )
    {
      return false;
    }

    final UserAddress userAddress = (UserAddress)object;

    if ( getAddressType() != null ? !getAddressType().equals( userAddress.getAddressType() ) : userAddress.getAddressType() != null )
    {
      return false;
    }

    if ( getUser() != null ? !getUser().equals( userAddress.getUser() ) : userAddress.getUser() != null )
    {
      return false;
    }

    return true;
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
    buf.append( "UserAddress [" );
    buf.append( "{id=" ).append( super.getId() ).append( "}, " );
    buf.append( "{userId=" ).append( this.getUser().getId() ).append( "}, " );
    buf.append( "{addressType=" ).append( this.getAddressType() ).append( "}, " );
    buf.append( "{address=" ).append( this.getAddress() ).append( "}, " );
    buf.append( "{isPrimary=" ).append( this.getIsPrimary() ).append( "}, " );
    buf.append( "]" );
    return buf.toString();
  }

} // end class UserAddress
