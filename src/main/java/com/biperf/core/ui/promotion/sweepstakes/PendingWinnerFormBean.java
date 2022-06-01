/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/promotion/sweepstakes/PendingWinnerFormBean.java,v $
 */

package com.biperf.core.ui.promotion.sweepstakes;

import com.biperf.core.ui.BaseFormBean;

/**
 * PendingWinnerFormBean.
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
 * <td>jenniget</td>
 * <td>Nov 4, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PendingWinnerFormBean extends BaseFormBean
{
  String id;
  String description;
  String winnerType;
  boolean replace = false;
  boolean remove = false;
  boolean validCountry = true;
  String countryCode;
  String countryDisplayValue;

  public String getDescription()
  {
    return description;
  }

  public void setDescription( String description )
  {
    this.description = description;
  }

  public String getId()
  {
    return id;
  }

  public void setId( String id )
  {
    this.id = id;
  }

  public boolean isRemove()
  {
    return remove;
  }

  public void setRemove( boolean remove )
  {
    this.remove = remove;
  }

  public boolean isReplace()
  {
    return replace;
  }

  public void setReplace( boolean replace )
  {
    this.replace = replace;
  }

  public String getWinnerType()
  {
    return winnerType;
  }

  public void setWinnerType( String winnerType )
  {
    this.winnerType = winnerType;
  }

  public boolean isValidCountry()
  {
    return validCountry;
  }

  public void setValidCountry( boolean validCountry )
  {
    this.validCountry = validCountry;
  }

  public String getCountryDisplayValue()
  {
    return countryDisplayValue;
  }

  public void setCountryDisplayValue( String countryDisplayValue )
  {
    this.countryDisplayValue = countryDisplayValue;
  }

  public String getCountryCode()
  {
    return countryCode;
  }

  public void setCountryCode( String countryCode )
  {
    this.countryCode = countryCode;
  }
}
