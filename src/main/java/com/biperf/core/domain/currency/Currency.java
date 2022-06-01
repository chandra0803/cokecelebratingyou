/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/currency/Attic/Currency.java,v $
 */

package com.biperf.core.domain.currency;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.utils.StringUtil;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
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
 * <td>dudam</td>
 * <td>December 19, 2014</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class Currency extends BaseDomain
{

  private static final long serialVersionUID = 1L;
  private String cmAssetName;
  private String currencyName;
  private String currencyCode;
  private String currencySymbol;
  private String status;

  public static final String CURRENCY_SECTION_CODE = "currency";
  public static final String CURRENCY_CMASSET_TYPE_NAME = "Currency Name";
  public static final String CURRENCY_CMASSET_CURRENCY_NAME = "Currency Name";
  public static final String CURRENCY_CMASSET_NAME = "NAME";
  public static final String CURRENCY_CMASSET_PREFIX = "currency.currencyName.";

  public String getCmAssetName()
  {
    return cmAssetName;
  }

  public void setCmAssetName( String cmAssetName )
  {
    this.cmAssetName = cmAssetName;
  }

  public String getCurrencyName()
  {
    return currencyName;
  }

  public void setCurrencyName( String currencyName )
  {
    this.currencyName = currencyName;
  }

  public String getCurrencyCode()
  {
    return currencyCode;
  }

  public void setCurrencyCode( String currencyCode )
  {
    this.currencyCode = currencyCode;
  }

  public String getCurrencySymbol()
  {
    return currencySymbol;
  }

  public void setCurrencySymbol( String currencySymbol )
  {
    this.currencySymbol = currencySymbol;
  }

  public String getStatus()
  {
    return status;
  }

  public void setStatus( String status )
  {
    this.status = status;
  }

  public String getDisplayCurrencyName()
  {
    String name = "";
    if ( !StringUtil.isNullOrEmpty( this.cmAssetName ) )
    {
      name = CmsResourceBundle.getCmsBundle().getString( this.cmAssetName, Currency.CURRENCY_CMASSET_NAME );
    }
    return name;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( currencyCode == null ? 0 : currencyCode.hashCode() );
    return result;
  }

  @Override
  public boolean equals( Object obj )
  {
    if ( this == obj )
    {
      return true;
    }
    if ( obj == null )
    {
      return false;
    }
    if ( getClass() != obj.getClass() )
    {
      return false;
    }
    Currency other = (Currency)obj;
    if ( currencyCode == null )
    {
      if ( other.currencyCode != null )
      {
        return false;
      }
    }
    else if ( !currencyCode.equals( other.currencyCode ) )
    {
      return false;
    }
    return true;
  }

}
