/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/currency/Attic/CurrencyForm.java,v $
 */

package com.biperf.core.ui.currency;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.biperf.core.domain.currency.Currency;
import com.biperf.core.service.currency.CurrencyService;
import com.biperf.core.ui.BaseForm;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.utils.UserManager;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * Currency ActionForm transfer object.
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
public class CurrencyForm extends BaseForm
{
  private static final long serialVersionUID = 1L;
  private Long currencyId;
  private String cmAssetName;
  private String currencyName;
  private String currencyCode;
  private String currencySymbol;
  private String status;

  public Long getCurrencyId()
  {
    return currencyId;
  }

  public void setCurrencyId( Long currencyId )
  {
    this.currencyId = currencyId;
  }

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

  public void toForm( Currency currency )
  {
    this.cmAssetName = currency.getCmAssetName();
    this.currencyCode = currency.getCurrencyCode();
    this.currencyId = currency.getId();
    this.currencyName = currency.getDisplayCurrencyName();
    this.currencySymbol = currency.getCurrencySymbol();
    this.status = currency.getStatus();
  }

  public Currency toDomain( Currency currency )
  {
    if ( currency == null )
    {
      currency = new Currency();
    }
    currency.setId( this.currencyId );
    currency.setCmAssetName( this.cmAssetName );
    currency.setCurrencyCode( this.currencyCode.toUpperCase() );
    currency.setCurrencyName( this.currencyName );
    currency.setCurrencySymbol( this.currencySymbol );
    currency.setStatus( this.status );
    return currency;
  }

  public ActionErrors validate( ActionMapping mapping, HttpServletRequest request )
  {
    ActionErrors actionErrors = new ActionErrors();
    if ( StringUtil.isNullOrEmpty( this.currencyName ) )
    {
      actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "currency.label.CURRENCY_NAME" ) ) );
    }
    else
    {
      boolean currencyNameUnique = getCurrencyService().isCurrencyNameUnique( this.currencyName, this.currencyId, UserManager.getUserLocale() );
      if ( !currencyNameUnique )
      {
        actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "currency.label.NAME_INUSE" ) );
      }
    }
    if ( StringUtil.isNullOrEmpty( this.currencyCode ) )
    {
      actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "currency.label.CURRENCY_CODE" ) ) );
    }
    if ( ( this.currencyId == null || this.currencyId == 0 ) && !StringUtil.isNullOrEmpty( this.currencyCode ) )
    {
      Currency currency = getCurrencyService().getCurrencyByCode( this.currencyCode );
      if ( currency != null )
      {
        actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "currency.label.CODE_INUSE" ) );
      }
    }
    return actionErrors;
  }

  private CurrencyService getCurrencyService()
  {
    return (CurrencyService)BeanLocator.getBean( CurrencyService.BEAN_NAME );
  }

}
