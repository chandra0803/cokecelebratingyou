/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/country/CountryAction.java,v $ */

package com.biperf.core.ui.country;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.currency.Currency;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.awardbanq.AwardBanQService;
import com.biperf.core.service.awardbanq.AwardBanQServiceFactory;
import com.biperf.core.service.country.CountryService;
import com.biperf.core.service.currency.CurrencyService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.ui.utils.ServiceErrorStrutsUtils;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.InvalidClientStateException;

/**
 * Action class for Country CRU operations.
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
 * <td>sedey</td>
 * <td>June 3, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class CountryAction extends BaseDispatchAction
{
  /** Log */
  private static final Log logger = LogFactory.getLog( CountryAction.class );

  private static final String SESSION_COUNTRY_FORM = "countryForm";

  /**
   * Prepare the display for creating a country.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward displayCreate( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    String forwardTo = ActionConstants.CREATE_FORWARD;

    if ( isCancelled( request ) )
    {
      return mapping.findForward( ActionConstants.CANCEL_FORWARD ); // EARLY EXIT
    }

    CountryForm createCountryForm = (CountryForm)form;

    createCountryForm.setAddCountry( true );

    // get the actionForward to display the udpate pages.
    return mapping.findForward( forwardTo );
  }

  /**
   * Prepare the display for updating a country.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward displayUpdate( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    String forwardTo = ActionConstants.EDIT_FORWARD;

    CountryForm updateCountryForm = (CountryForm)form;
    String countryId = null;
    ActionMessages errors = new ActionMessages();

    if ( isCancelled( request ) )
    {
      return mapping.findForward( ActionConstants.CANCEL_FORWARD ); // EARLY EXIT
    }

    try
    {
      String clientState = RequestUtils.getRequiredParamString( request, "clientState" );
      String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
      String password = ClientStatePasswordManager.getPassword();

      if ( cryptoPass != null && cryptoPass.equals( "1" ) )
      {
        password = ClientStatePasswordManager.getGlobalPassword();
      }
      // Deserialize the client state.
      Map clientStateMap = ClientStateSerializer.deserialize( clientState, password );
      try
      {
        countryId = (String)clientStateMap.get( "countryId" );
      }
      catch( ClassCastException cce )
      {
        Long id = (Long)clientStateMap.get( "countryId" );
        countryId = id.toString();
      }
      if ( countryId == null )
      {
        errors.add( "errorMessage", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, "countryId as part of clientState" ) );
        saveErrors( request, errors );
        mapping.findForward( ActionConstants.FAIL_FORWARD );
      }
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }

    Country countryToUpdate = getCountryService().getCountryById( new Long( countryId ) );
    updateCountryForm.load( countryToUpdate );
    updateCountryForm.setMethod( "displayUpdate" );

    if ( countryToUpdate.getCurrencyCode() != null )
    {
      Currency currency = getCurrencyService().getCurrencyByCode( countryToUpdate.getCurrencyCode() );
      if ( currency != null )
      {
        updateCountryForm.setCurrencyId( currency.getId() );
      }
    }

    // If Country support email address is empty, default to ContactUs email
    if ( StringUtils.isEmpty( updateCountryForm.getSupportEmailAddr() ) )
    {
      String contactUsEmail = getSystemVariableService().getPropertyByName( SystemVariableService.CONTACT_US_EMAIL_ADDRESS_KEY ).getStringVal();
      updateCountryForm.setSupportEmailAddr( contactUsEmail );
    }

    updateCountryForm.setAddCountry( false );
    updateCountryForm.setBudgetMediaValue( getAwardBanQService().getNullableMediaValueForCountry( updateCountryForm.getCountryCode() ) );

    // get the actionForward to display the udpate pages.
    return mapping.findForward( forwardTo );
  }

  /**
   * execute method will forward to the SUCCESS_FORWARD after Creating the Country.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward create( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    String forwardTo = ActionConstants.SUCCESS_FORWARD;
    ActionMessages errors = new ActionMessages();

    CountryForm countryForm = (CountryForm)form;

    // Bug fix 39925 starts
    /*
     * // Get the form back out of the Session to redisplay. CountryForm sessionCountryForm =
     * (CountryForm)request.getSession().getAttribute( "countryForm" ); if ( sessionCountryForm !=
     * null ) { countryForm.setCountrySuppliersList(sessionCountryForm.getCountrySuppliersList()); }
     */

    /*
     * errors = checkSupplierOption( countryForm.getCountrySuppliersList() ); if ( errors.size() > 0
     * ) { saveErrors( request, errors ); return mapping.findForward( ActionConstants.FAIL_FORWARD
     * ); } // clean up the session request.getSession().removeAttribute( "SESSION_COUNTRY_FORM" );
     */
    // Bug fix 39925 ends
    if ( isCancelled( request ) )
    {
      return mapping.findForward( ActionConstants.CANCEL_FORWARD ); // EARLY EXIT
    }

    if ( isTokenValid( request, true ) )
    {
      try
      {
        Country country = countryForm.toDomainObject();
        if ( countryForm.getCurrencyId() != null )
        {
          Currency currency = getCurrencyService().getCurrencyById( countryForm.getCurrencyId() );
          country.setCurrencyCode( currency.getCurrencyCode() );
        }
        getCountryService().saveCountry( countryForm.getSupplierId(), country );
      }
      catch( ServiceErrorException e )
      {
        logger.debug( e );
        List serviceErrors = e.getServiceErrors();
        ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, errors );
      }
    }
    else
    {
      errors.add( ActionConstants.ERROR_TOKEN_FAILURE, new ActionMessage( ActionConstants.ERROR_TOKEN_EXCEPTION ) );
    }
    if ( errors.size() > 0 )
    {
      saveErrors( request, errors );
      forwardTo = ActionConstants.FAIL_FORWARD;
    }

    return mapping.findForward( forwardTo );
  }

  /**
   * execute method will forward to the SUCCESS_FORWARD after updating the Country.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward update( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    String forwardTo = ActionConstants.SUCCESS_FORWARD;
    CountryForm countryForm = (CountryForm)form;
    ActionMessages errors = new ActionMessages();

    if ( isCancelled( request ) )
    {
      return mapping.findForward( ActionConstants.CANCEL_FORWARD ); // EARLY EXIT
    }

    if ( isTokenValid( request, true ) )
    {
      try
      {
        Country country = getCountryService().getCountryById( countryForm.getCountryId() );
        Country countryFromForm = countryForm.toDomainObject();
        if ( countryForm.getCurrencyId() != null )
        {
          Currency currency = getCurrencyService().getCurrencyById( countryForm.getCurrencyId() );
          countryFromForm.setCurrencyCode( currency.getCurrencyCode() );
        }
        getCountryService().saveCountry( countryForm.getSupplierId(), countryFromForm );
      }
      catch( ServiceErrorException e )
      {
        logger.debug( e );
        List serviceErrors = e.getServiceErrors();
        ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, errors );
      }
    }
    else
    {
      errors.add( ActionConstants.ERROR_TOKEN_FAILURE, new ActionMessage( ActionConstants.ERROR_TOKEN_EXCEPTION ) );
    }
    if ( errors.size() > 0 )
    {
      saveErrors( request, errors );
      forwardTo = ActionConstants.FAIL_FORWARD;
    }
    countryForm.setAddCountry( true );
    return mapping.findForward( forwardTo );
  }

  public ActionForward display( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {

    String forwardTo = ActionConstants.CREATE_FORWARD;

    CountryForm countryForm = (CountryForm)form;
    if ( countryForm.getCountryId() == null )
    {
      countryForm.setAddCountry( true );
    }
    else
    {
      countryForm.setAddCountry( false );
    }

    countryForm.setMethod( "display" );

    // Put the form in the session to be reloaded when coming back from select country suppliers.
    // request.getSession().setAttribute( SESSION_COUNTRY_FORM, countryForm );

    return mapping.findForward( ActionConstants.CREATE_FORWARD );
  }
  // Bug fix 39925 ends

  // Private Methods
  /**
   * Get the CountryService From the bean factory through a locator.
   * 
   * @return CountryService
   */
  private CountryService getCountryService()
  {
    return (CountryService)getService( CountryService.BEAN_NAME );
  }

  private SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }

  private AwardBanQService getAwardBanQService()
  {
    AwardBanQServiceFactory factory = (AwardBanQServiceFactory)BeanLocator.getBean( AwardBanQServiceFactory.BEAN_NAME );
    return factory.getAwardBanQService();
  }

  private CurrencyService getCurrencyService()
  {
    return (CurrencyService)getService( CurrencyService.BEAN_NAME );
  }
}
