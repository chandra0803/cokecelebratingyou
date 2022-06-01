/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/currency/Attic/CurrencyAction.java,v $ */

package com.biperf.core.ui.currency;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.currency.Currency;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.currency.CurrencyService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.InvalidClientStateException;

/**
 * Action class for Supplier CRU operations.
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
public class CurrencyAction extends BaseDispatchAction
{

  public ActionForward display( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    List<Currency> currencies = getCurrencyService().getAllCurrency();
    if ( currencies == null )
    {
      currencies = new ArrayList<Currency>();
    }
    request.setAttribute( "currencies", currencies );
    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  public ActionForward prepareCreate( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  public ActionForward prepareEdit( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws InvalidClientStateException
  {
    CurrencyForm currencyForm = (CurrencyForm)form;
    Long currencyId = getCurrencyId( request );
    Currency currency = getCurrencyService().getCurrencyById( currencyId );
    currencyForm.toForm( currency );
    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  public ActionForward save( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    CurrencyForm currencyForm = (CurrencyForm)form;
    if ( isCancelled( request ) )
    {
      return mapping.findForward( ActionConstants.CANCEL_FORWARD );
    }
    Currency currency = null;
    if ( currencyForm.getCurrencyId() != null && currencyForm.getCurrencyId() != 0 )
    {
      currency = getCurrencyService().getCurrencyById( currencyForm.getCurrencyId() );
    }
    currency = currencyForm.toDomain( currency );
    getCurrencyService().save( currency );
    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  private Long getCurrencyId( HttpServletRequest request ) throws InvalidClientStateException
  {
    String clientState = RequestUtils.getRequiredParamString( request, "clientState" );
    String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
    String password = ClientStatePasswordManager.getPassword();
    if ( cryptoPass != null && cryptoPass.equals( "1" ) )
    {
      password = ClientStatePasswordManager.getGlobalPassword();
    }
    Map clientStateMap = ClientStateSerializer.deserialize( clientState, password );
    return (Long)clientStateMap.get( "currencyId" );
  }

  private CurrencyService getCurrencyService()
  {
    return (CurrencyService)getService( CurrencyService.BEAN_NAME );
  }
}
