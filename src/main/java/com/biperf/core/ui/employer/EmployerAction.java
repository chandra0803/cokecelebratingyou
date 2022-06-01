/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/employer/EmployerAction.java,v $
 */

package com.biperf.core.ui.employer;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.hibernate.exception.ConstraintViolationException;

import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.employer.Employer;
import com.biperf.core.domain.enums.CountryStatusType;
import com.biperf.core.service.country.CountryService;
import com.biperf.core.service.employer.EmployerService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.InvalidClientStateException;

/**
 * EmployerAction.
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
public class EmployerAction extends BaseDispatchAction
{
  /** logger */
  private static final Log logger = LogFactory.getLog( EmployerAction.class );

  /**
   * unspecified will display list
   * 
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward unspecified( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    return displayList( actionMapping, actionForm, request, response );
  } // end unspecified

  /**
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward cancelled( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    return displayList( actionMapping, actionForm, request, response );
  } // end cancelled

  /**
   * Display a list of all employers.
   * 
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward displayList( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    final String METHOD_NAME = "displayList";

    if ( isCancelled( request ) )
    {
      // EARLY EXIT
      logger.info( ">>> " + METHOD_NAME + " cancelled." );
      return actionMapping.findForward( ActionConstants.CANCEL_FORWARD );
    }

    return actionMapping.findForward( ActionConstants.SUCCESS_FORWARD );
  } // end displayList

  /**
   * Prepares anything necessary before displaying the create screen.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward prepareCreate( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    EmployerForm employerForm = (EmployerForm)form;
    employerForm.setActive( true );

    // Default the Country to United States
    Country country = getCountryService().getCountryByCode( Country.UNITED_STATES );

    if ( CountryStatusType.ACTIVE.equalsIgnoreCase( country.getStatus().getCode() ) )
    {
      employerForm.getAddressFormBean().setCountryCode( country.getCountryCode() );
      employerForm.getAddressFormBean().setCountryName( country.getI18nCountryName() );
    }
    else
    {
      List activeCountryList = getCountryService().getAllActive();
      if ( activeCountryList != null )
      {
        if ( activeCountryList.size() == 1 )
        {
          country = (Country)activeCountryList.get( 0 );
          employerForm.getAddressFormBean().setCountryCode( country.getCountryCode() );
          employerForm.getAddressFormBean().setCountryName( country.getI18nCountryName() );
        }
        else
        {
          request.setAttribute( "multiple", new Boolean( true ) );
        }
      }
    }

    // get the actionForward to display the create pages.
    employerForm.setMethod( "create" );
    request.setAttribute( "employerForm", employerForm );
    return mapping.findForward( ActionConstants.CREATE_FORWARD );
  } // end prepareCreate

  /**
   * Country has changed on the form, used just to reload it, and forward back again.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward changeCountryCreate( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    EmployerForm employerForm = (EmployerForm)form;
    employerForm.setCountryPhoneCode( employerForm.getAddressFormBean().getCountryCode() );
    request.setAttribute( "employerForm", employerForm );
    // Country has changed on the Form, used just to reload it, and forward back again
    return mapping.findForward( ActionConstants.CREATE_FORWARD );
  } // end changeCountryCreate

  /**
   * Creates a new user address
   * 
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward create( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    String forwardTo = ActionConstants.SUCCESS_CREATE;
    ActionMessages errors = new ActionMessages();

    EmployerForm employerForm = (EmployerForm)actionForm;
    if ( isTokenValid( request, true ) )
    {

      Employer employer = employerForm.toDomainObject();
      employer.setVersion( null );
      try
      {
        getEmployerService().saveEmployer( employer );
      }
      catch( ConstraintViolationException cv )
      {
        errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "employer.error.UNIQUE_CONSTRAINT" ) );

        logger.info( " CheckedException - " + cv.getMessage() );
      }
    }
    else
    {
      errors.add( ActionConstants.ERROR_TOKEN_FAILURE, new ActionMessage( ActionConstants.ERROR_TOKEN_EXCEPTION ) );
    }

    if ( errors.size() > 0 )
    {
      saveErrors( request, errors );
      forwardTo = ActionConstants.FAIL_CREATE;
    }
    request.setAttribute( "employerForm", employerForm );
    return actionMapping.findForward( forwardTo );
  } // end create

  /**
   * Prepares anything necessary before displaying the update screen.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward prepareUpdate( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    String forwardTo = ActionConstants.UPDATE_FORWARD;
    ActionMessages errors = new ActionMessages();
    EmployerForm employerForm = (EmployerForm)form;

    String employerId = null;
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
        employerId = (String)clientStateMap.get( "employerId" );
      }
      catch( ClassCastException cce )
      {
        Long id = (Long)clientStateMap.get( "employerId" );
        employerId = id.toString();
      }

      if ( employerId == null )
      {
        errors.add( "errorMessage", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, "countryId as part of clientState" ) );
        saveErrors( request, errors );
        mapping.findForward( ActionConstants.FAIL_UPDATE );
      }
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }

    Employer employerToUpdate = getEmployerService().getEmployerById( new Long( employerId ) );
    // BugFix 20568,set country code attribute to populate country statelist by the controller.
    request.setAttribute( "countryCode", employerToUpdate.getAddress().getCountry().getCountryCode() );
    employerForm.load( employerToUpdate );
    employerForm.setMethod( "update" );
    request.setAttribute( "employerForm", employerForm );
    // get the actionForward to display the udpate pages.
    return mapping.findForward( forwardTo );
  } // end prepareUpdate

  /**
   * Country has changed on the form, used just to reload it, and forward back again.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward changeCountryUpdate( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    EmployerForm employerForm = (EmployerForm)form;
    employerForm.setCountryPhoneCode( employerForm.getAddressFormBean().getCountryCode() );
    request.setAttribute( "employerForm", employerForm );
    // Country has changed on the Form, used just to reload it, and forward back again
    return mapping.findForward( ActionConstants.UPDATE_FORWARD );
  } // end changeCountryUpdate

  /**
   * Update the Employer with the data provided through the Form.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward update( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionMessages errors = new ActionMessages();
    String forwardTo = ActionConstants.SUCCESS_UPDATE;
    EmployerForm employerForm = (EmployerForm)form;
    if ( isTokenValid( request, true ) )
    {
      Employer updatedEmployer = employerForm.toDomainObject();
      getEmployerService().saveEmployer( updatedEmployer );
    }
    else
    {
      errors.add( ActionConstants.ERROR_TOKEN_FAILURE, new ActionMessage( ActionConstants.ERROR_TOKEN_EXCEPTION ) );
    }

    if ( errors.size() > 0 )
    {
      saveErrors( request, errors );
      forwardTo = ActionConstants.FAIL_UPDATE;
    }
    request.setAttribute( "employerForm", employerForm );
    return mapping.findForward( forwardTo );
  } // end update

  /**
   * Get the employerService from the bean factory.
   * 
   * @return EmployerService
   */
  private EmployerService getEmployerService()
  {

    return (EmployerService)getService( EmployerService.BEAN_NAME );

  } // end getEmployerService

  /**
   * Returns a reference to the Country service.
   * 
   * @return a reference to the Country service.
   */
  private CountryService getCountryService()
  {
    return (CountryService)getService( CountryService.BEAN_NAME );
  }
}
