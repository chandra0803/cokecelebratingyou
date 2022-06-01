/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/user/UserAddressAction.java,v $
 */

package com.biperf.core.ui.user;

import java.util.HashMap;
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

import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.user.UserAddress;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.country.CountryService;
import com.biperf.core.service.participant.AudienceService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.ActionUtils;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.ui.utils.ServiceErrorStrutsUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.InvalidClientStateException;

/**
 * UserAddressAction.
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
 * <td>May 3, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class UserAddressAction extends BaseDispatchAction
{

  private static final Log logger = LogFactory.getLog( UserAddressAction.class );

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
  }

  /**
   * Displays a list of all User Addresses
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

    UserAddressListForm userAddressListForm = (UserAddressListForm)actionForm;

    // Put the user id onto the form
    String userId = "";
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
        userId = (String)clientStateMap.get( "userId" );
      }
      catch( ClassCastException cce )
      {
        Long id = (Long)clientStateMap.get( "userId" );
        userId = id.toString();
      }
      if ( userId == null )
      {
        ActionMessages errors = new ActionMessages();
        errors.add( "errorMessage", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, "userId as part of clientState" ) );
        saveErrors( request, errors );
        return actionMapping.findForward( ActionConstants.FAIL_ERRORS_ONLY );
      }
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }

    if ( isCancelled( request ) )
    {
      // EARLY EXIT
      logger.info( ">>> " + METHOD_NAME + " cancelled." );
      return actionMapping.findForward( ActionConstants.CANCEL_FORWARD );
    }

    userAddressListForm.setUserId( userId );
    // Find the Primary Address for the given user
    Long longUserId = new Long( userId );
    UserAddress userAddress = getUserService().getPrimaryUserAddress( longUserId );
    if ( userAddress != null )
    {
      userAddressListForm.setPrimary( userAddress.getAddressType().getCode() );
    }

    return actionMapping.findForward( ActionConstants.SUCCESS_FORWARD );
  } // end displayList

  /**
   * Removes all selected user addresses
   * 
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward remove( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    ActionMessages errors = new ActionMessages();
    String forwardAction = ActionConstants.SUCCESS_FORWARD;

    UserAddressListForm userAddressListForm = (UserAddressListForm)actionForm;

    Long userId = new Long( userAddressListForm.getUserId() );
    String[] deleteList = userAddressListForm.getDelete();

    if ( deleteList != null )
    {
      for ( int i = 0; i < deleteList.length; i++ )
      {
        String deleteAddressCode = deleteList[i];
        try
        {
          getUserService().deleteUserAddress( userId, deleteAddressCode );
          getAudienceService().rematchParticipantForAllCriteriaAudiences( userId );
        }
        catch( ServiceErrorException se )
        {
          logger.debug( se.toString() );
          List serviceErrors = se.getServiceErrors();
          ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, errors );
        }
      } // end for
    }

    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      forwardAction = ActionConstants.FAIL_DELETE;
    }
    else
    {
      forwardAction = ActionConstants.SUCCESS_FORWARD;
    }

    Map clientStateParameterMap = new HashMap();
    clientStateParameterMap.put( "userId", userAddressListForm.getUserId() );
    String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
    return ActionUtils.forwardWithParameters( actionMapping, forwardAction, new String[] { queryString } );
  } // end remove

  /**
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward changePrimary( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    ActionMessages errors = new ActionMessages();
    String forwardAction = ActionConstants.SUCCESS_FORWARD;
    UserAddressListForm userAddressListForm = (UserAddressListForm)actionForm;

    String primaryType = userAddressListForm.getPrimary();
    Long userId = new Long( userAddressListForm.getUserId() );
    try
    {
      getUserService().updatePrimaryAddress( userId, primaryType );
      getAudienceService().rematchParticipantForAllCriteriaAudiences( userId );
    }
    catch( ServiceErrorException se )
    {
      logger.debug( se.toString() );
      List serviceErrors = se.getServiceErrors();
      ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, errors );
    }

    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      forwardAction = ActionConstants.FAIL_FORWARD;
    }
    else
    {
      forwardAction = ActionConstants.SUCCESS_FORWARD;
    }

    Map clientStateParameterMap = new HashMap();
    clientStateParameterMap.put( "userId", userAddressListForm.getUserId() );
    String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
    return ActionUtils.forwardWithParameters( actionMapping, forwardAction, new String[] { queryString } );
  } // end changePrimary

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
    // Default the Country to United States
    UserAddressForm userAddressForm = (UserAddressForm)form;

    Country country = getCountryService().getCountryByCode( Country.UNITED_STATES );
    userAddressForm.getAddressFormBean().setCountryCode( country.getCountryCode() );
    userAddressForm.getAddressFormBean().setCountryName( country.getI18nCountryName() );

    // get the actionForward to display the create pages.
    userAddressForm.setMethod( "create" );
    return mapping.findForward( ActionConstants.CREATE_FORWARD );
  } // end prepareCreate

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
    UserAddressForm userAddressForm = (UserAddressForm)actionForm;
    Long userId = new Long( userAddressForm.getUserId() );

    if ( isCancelled( request ) )
    {
      String queryString = "method=displayList";
      Map clientStateParameterMap = new HashMap();
      clientStateParameterMap.put( "userId", userId );
      String userIdString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
      return ActionUtils.forwardWithParameters( actionMapping, ActionConstants.CANCEL_FORWARD, new String[] { queryString, userIdString } );
    }

    ActionMessages errors = new ActionMessages();
    String forwardAction = "";

    try
    {
      getUserService().addUserAddress( userId, userAddressForm.toInsertedDomainObject() );
      getAudienceService().rematchParticipantForAllCriteriaAudiences( userId );
    }
    catch( ServiceErrorException se )
    {
      logger.debug( se.toString() );
      List serviceErrors = se.getServiceErrors();
      ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, errors );
    }
    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      forwardAction = ActionConstants.FAIL_FORWARD;
    }
    else
    {
      forwardAction = ActionConstants.SUCCESS_CREATE;
    }

    Map clientStateParameterMap = new HashMap();
    clientStateParameterMap.put( "userId", userAddressForm.getUserId() );
    String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
    return ActionUtils.forwardWithParameters( actionMapping, forwardAction, new String[] { queryString } );
  } // end create

  /**
   * Prepares anything necessary before showing the Update address
   * 
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward prepareUpdate( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    UserAddressForm userAddressForm = (UserAddressForm)actionForm;

    Long userId = new Long( userAddressForm.getUserId() );
    String addressType = userAddressForm.getAddressType();

    UserAddress userAddress = getUserService().getUserAddress( userId, addressType );

    userAddressForm.load( userAddress );

    return actionMapping.findForward( ActionConstants.EDIT_FORWARD );
  }

  /**
   * Updates a user address
   * 
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward update( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    UserAddressForm userAddressForm = (UserAddressForm)actionForm;
    Long userId = new Long( userAddressForm.getUserId() );

    if ( isCancelled( request ) )
    {
      String queryString = "method=displayList";
      Map clientStateParameterMap = new HashMap();
      clientStateParameterMap.put( "userId", userId );
      String userIdString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
      return ActionUtils.forwardWithParameters( actionMapping, ActionConstants.CANCEL_FORWARD, new String[] { queryString, userIdString } );
    }

    String forwardAction = ActionConstants.SUCCESS_UPDATE;

    ActionMessages errors = new ActionMessages();

    UserAddress userAddress = userAddressForm.toFullDomainObject();

    try
    {
      getUserService().updateUserAddress( userId, userAddress );
      getAudienceService().rematchParticipantForAllCriteriaAudiences( userId );
    }
    catch( ServiceErrorException se )
    {
      logger.debug( se.toString() );
      List serviceErrors = se.getServiceErrors();
      ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, errors );
    }

    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      forwardAction = ActionConstants.FAIL_FORWARD;
    }
    else
    {
      forwardAction = ActionConstants.SUCCESS_UPDATE;
    }

    Map clientStateParameterMap = new HashMap();
    clientStateParameterMap.put( "userId", userAddressForm.getUserId() );
    String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
    return ActionUtils.forwardWithParameters( actionMapping, forwardAction, new String[] { queryString } );
  } // end update

  /**
   * Country has changed on the form, used just to reload it, and forward back again.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward changeCountry( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    // Country has changed on the Form, used just to reload it, and forward back again
    return mapping.findForward( ActionConstants.CREATE_FORWARD );
  } // end changeCountry

  private UserService getUserService()
  {
    return (UserService)getService( UserService.BEAN_NAME );
  } // end getUserService

  /**
   * Returns a reference to the Country service.
   * 
   * @return a reference to the Country service.
   */
  private CountryService getCountryService()
  {
    return (CountryService)getService( CountryService.BEAN_NAME );
  }

  private AudienceService getAudienceService()
  {
    return (AudienceService)getService( AudienceService.BEAN_NAME );
  }
} // end class UserAddressAction
