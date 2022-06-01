/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/Address.java,v $
 */

package com.biperf.core.domain;

import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.enums.StateType;
import com.biperf.core.utils.AddressUtil;

/**
 * Address value object which represents all attributes of an address.
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
 * <td>Apr 25, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class Address implements java.io.Serializable
{
  /**
   * serialVersionUID
   */
  private static final long serialVersionUID = 3544953255596732467L;
  private Country country;
  private String addr1;
  private String addr2;
  private String addr3;
  private String city;
  private StateType stateType;
  private String postalCode;

  /**
   * @return value of addr1 property
   */
  public String getAddr1()
  {
    return addr1;
  }

  /**
   * @param addr1 value for addr1 property
   */
  public void setAddr1( String addr1 )
  {
    this.addr1 = addr1;
  }

  /**
   * @return value of addr2 property
   */
  public String getAddr2()
  {
    return addr2;
  }

  /**
   * @param addr2 value for addr2 property
   */
  public void setAddr2( String addr2 )
  {
    this.addr2 = addr2;
  }

  /**
   * @return value of addr3 property
   */
  public String getAddr3()
  {
    return addr3;
  }

  /**
   * @param addr3 value for addr3 property
   */
  public void setAddr3( String addr3 )
  {
    this.addr3 = addr3;
  }

  /**
   * @return value of city property
   */
  public String getCity()
  {
    return city;
  }

  /**
   * @param city value for city property
   */
  public void setCity( String city )
  {
    this.city = city;
  }

  /**
   * @return value of postalCode property
   */
  public String getPostalCode()
  {
    return postalCode;
  }

  /**
   * @param postalCode value for postalCode property
   */
  public void setPostalCode( String postalCode )
  {
    this.postalCode = postalCode;
  }

  /**
   * Returns the country in which this address is located.
   * 
   * @return the country in which this address is located.
   */
  public Country getCountry()
  {
    return country;
  }

  /**
   * Sets the country in which this address is located.
   * 
   * @param country the country in which this address is located.
   */
  public void setCountry( Country country )
  {
    this.country = country;
  }

  /**
   * @return value of stateType property
   */
  public StateType getStateType()
  {
    return stateType;
  }

  /**
   * @param stateType value for stateType property
   */
  public void setStateType( StateType stateType )
  {
    this.stateType = stateType;
  }

  /**
   * Returns true if this address is in North America; returns false otherwise.
   * 
   * @return true if this address is in North America; false otherwise.
   */
  public boolean isNorthAmerican()
  {
    boolean isNorthAmerican = false;

    if ( country != null )
    {
      isNorthAmerican = AddressUtil.isNorthAmerican( country.getCountryCode() );
    }

    return isNorthAmerican;
  }

  /**
   * Checks the country code to determine if it is International or not
   * 
   * @return boolean - true if Country is International - false if Country is not International
   */
  public boolean isInternational()
  {
    return !isNorthAmerican();
  } // end is International

} // end class Address
