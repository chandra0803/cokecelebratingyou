/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/utils/AddressUtil.java,v $
 */

package com.biperf.core.utils;

import java.util.Iterator;

import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.country.CountrySupplier;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.supplier.Supplier;
import com.biperf.core.domain.user.UserAddress;

/**
 * AddressUtil.
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
 * <td>May 10, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class AddressUtil
{
  /**
   * Checks if a country code to determine if it is North American or not
   * 
   * @param countryCode
   * @return boolean true if countryCode is North American, false if not North American
   */
  public static boolean isNorthAmerican( String countryCode )
  {
    boolean isNA = false;
    if ( countryCode != null )
    {
      if ( countryCode.equalsIgnoreCase( Country.UNITED_STATES ) || countryCode.equalsIgnoreCase( Country.CANADA ) || countryCode.equalsIgnoreCase( Country.MEXICO ) )
      {
        isNA = true;
      }
    }
    return isNA;
  } // end isNorthAmerican

  /**
   * Checks the country code to determine if it is International or not
   * 
   * @param countryCode
   * @return boolean - true if Country is International, false if Country is not International
   */
  public static boolean isInternational( String countryCode )
  {
    boolean isInternational = false;
    if ( countryCode != null && !"".equals( countryCode ) )
    {
      isInternational = !isNorthAmerican( countryCode );
    }
    return isInternational;
  } // end is International

  /**
   * Checks if the given participant's supplier is BI Bank.
   * Uses the supplier of the country of the primary address of the participant.
   * Returns true if the participant does not have a primary address. 
   */
  public static boolean isSupplierBIBank( Participant participant )
  {
    boolean BIBankIsSupplier = true;

    if ( participant != null )
    {
      UserAddress primaryAddress = participant.getPrimaryAddress();
      if ( primaryAddress != null && primaryAddress.getAddress() != null && !AddressUtil.isSupplierBIBank( primaryAddress.getAddress().getCountry() ) )
      {
        BIBankIsSupplier = false;
      }
    }

    return BIBankIsSupplier;
  }

  /**
   * Checks if the given country's supplier is BI Bank.
   */
  public static boolean isSupplierBIBank( Country country )
  {
    boolean BIBankIsSupplier = false;

    if ( country != null && country.getCountrySuppliers() != null )
    {
      for ( Iterator iter = country.getCountrySuppliers().iterator(); iter.hasNext(); )
      {
        CountrySupplier countrySupplier = (CountrySupplier)iter.next();
        if ( Supplier.BI_BANK.equals( countrySupplier.getSupplier().getSupplierName() ) )
        {
          BIBankIsSupplier = true;
          break;
        }

      }
    }

    return BIBankIsSupplier;
  }

} // end class AddressUtil
