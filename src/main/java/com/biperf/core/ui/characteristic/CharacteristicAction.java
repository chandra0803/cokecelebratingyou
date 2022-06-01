/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/characteristic/CharacteristicAction.java,v $
 */

package com.biperf.core.ui.characteristic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.ActionUtils;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.ui.utils.ServiceErrorStrutsUtils;
import com.biperf.core.utils.ClientStateUtils;

/**
 * Action class for Characteristic CRUD operations.
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
 * <td>Apr 28, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public abstract class CharacteristicAction extends BaseDispatchAction
{
  /**
   * Display a list of characteristics.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward displayList( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    String forwardTo = ActionConstants.SUCCESS_FORWARD;
    CharacteristicForm charForm = (CharacteristicForm)form;
    charForm.setCharacteristicDataType( "" );

    request.setAttribute( CharacteristicForm.FORM_NAME, charForm );
    // get the actionForward to display the udpate pages.
    return mapping.findForward( forwardTo );
  }

  /**
   * Prepare the display for updating a characteristic.
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
    ActionMessages errors = new ActionMessages();

    CharacteristicForm updateCharForm = (CharacteristicForm)form;
    Long charId = new Long( 0 );

    try
    {
      // The requestUtils will throw an exception if the required charId param is not in the
      // request.
      charId = new Long( RequestUtils.getRequiredParamString( request, "charId" ) );
    }
    catch( IllegalArgumentException e )
    {
      errors.add( "errorMessage", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED ) );
    }

    if ( errors.size() != 0 )
    {
      saveErrors( request, errors );
      forwardTo = ActionConstants.FAIL_FORWARD;
    }
    else
    {
      makeGetCharacteristicServiceCallAndLoad( charId, updateCharForm );
      updateCharForm.setMethod( "update" );

      request.setAttribute( CharacteristicForm.FORM_NAME, updateCharForm );
    }

    // get the actionForward to display the udpate pages.
    return mapping.findForward( forwardTo );
  }

  /**
   * Update the characteristic with the data provided through the Form.
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

    CharacteristicForm charForm = (CharacteristicForm)form;
    String forwardTo = getUpdateSuccessForward( charForm );

    if ( isTokenValid( request, true ) )
    {
      try
      {
        makeSaveCharacteristicServiceCall( charForm );

        charForm.setCharacteristicName( "" );
      }
      catch( ServiceErrorException e )
      {
        List serviceErrors = e.getServiceErrors();
        ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, errors );
        saveErrors( request, errors );
        return mapping.findForward( getUpdateFailureForward( charForm ) );
      }

    }
    else
    {
      errors.add( ActionConstants.ERROR_TOKEN_FAILURE, new ActionMessage( ActionConstants.ERROR_TOKEN_EXCEPTION ) );
    }

    if ( errors.size() > 0 )
    {
      saveErrors( request, errors );
      forwardTo = getUpdateFailureForward( charForm );
    }

    return mapping.findForward( forwardTo );
  }

  /**
   * Forward to displayCreate pages.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward displayCreate( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    CharacteristicForm charForm = (CharacteristicForm)form;
    charForm.setCharacteristicName( "" );

    request.setAttribute( CharacteristicForm.FORM_NAME, charForm );

    onDisplayCreate( charForm );

    // get the actionForward to display the create pages.
    return mapping.findForward( ActionConstants.CREATE_FORWARD );
  }

  /**
   * Create the Characteristic from the user's form submission.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward create( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {

    CharacteristicForm charForm = (CharacteristicForm)form;
    String forwardTo = getInsertSuccessForward( charForm );

    ActionMessages errors = new ActionMessages();

    if ( isTokenValid( request, true ) )
    {
      try
      {
        makeSaveCharacteristicServiceCall( charForm );
      }
      catch( ServiceErrorException e )
      {
        List serviceErrors = e.getServiceErrors();
        ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, errors );
        saveErrors( request, errors );
        return mapping.findForward( getInsertFailureForward( charForm ) );
      }

    }
    else
    {
      errors.add( ActionConstants.ERROR_TOKEN_FAILURE, new ActionMessage( ActionConstants.ERROR_TOKEN_EXCEPTION ) );
    }

    if ( errors.size() > 0 )
    {
      saveErrors( request, errors );
      forwardTo = getInsertFailureForward( charForm );
    }
    else
    {
      request.setAttribute( CharacteristicForm.FORM_NAME, new CharacteristicForm() );
    }

    // get the actionForward to display the create pages.
    return mapping.findForward( forwardTo );
  }

  /**
   * execute method will forward to the SUCCESS_DELETE after inactivating the Characterisic.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward deleteCharacteristic( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    CharacteristicForm charForm = (CharacteristicForm)form;
    String forwardTo = getDeleteSuccessForward( charForm );

    ActionMessages errors = new ActionMessages();

    if ( isTokenValid( request, true ) )
    {
      try
      {
        if ( charForm.getDeleteValues() != null )
        {
          makeDeleteCharacteristicServiceCall( charForm );
        }
      }
      catch( ServiceErrorException e )
      {
        List serviceErrors = e.getServiceErrors();
        ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, errors );
        saveErrors( request, errors );
        return mapping.findForward( getDeleteFailureForward( charForm ) );
      }

    }
    else
    {
      errors.add( ActionConstants.ERROR_TOKEN_FAILURE, new ActionMessage( ActionConstants.ERROR_TOKEN_EXCEPTION ) );
    }

    if ( errors.size() > 0 )
    {
      saveErrors( request, errors );
      forwardTo = getDeleteFailureForward( charForm );
    }
    else
    {
      request.setAttribute( CharacteristicForm.FORM_NAME, new CharacteristicForm() );
    }
    Map clientStateParameterMap = new HashMap();
    clientStateParameterMap.put( "domainId", charForm.getDomainId() );
    String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );

    ActionForward actionForward = ActionUtils.forwardWithParameters( mapping, forwardTo, new String[] { queryString, "method=displayList" } );
    return actionForward;
  }

  /**
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */

  public ActionForward cancelled( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    return actionMapping.findForward( getCancelForward( (CharacteristicForm)actionForm ) );
  }

  /**
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

  // Private Methods

  /**
   * @param characteristicForm
   * @throws ServiceErrorException
   */
  public abstract void makeDeleteCharacteristicServiceCall( CharacteristicForm characteristicForm ) throws ServiceErrorException;

  /**
   * @param charForm
   * @throws ServiceErrorException
   */
  public abstract void makeSaveCharacteristicServiceCall( CharacteristicForm charForm ) throws ServiceErrorException;

  /**
   * @param characteristicId
   * @param charForm
   */
  public abstract void makeGetCharacteristicServiceCallAndLoad( Long characteristicId, CharacteristicForm charForm );

  protected String getUpdateSuccessForward( CharacteristicForm form )
  {
    // Subclasses can implement if necessary, otherwise return the default
    return ActionConstants.SUCCESS_FORWARD;
  }

  protected String getInsertSuccessForward( CharacteristicForm form )
  {
    // Subclasses can implement if necessary, otherwise return the default
    return ActionConstants.SUCCESS_FORWARD;
  }

  protected String getDeleteSuccessForward( CharacteristicForm form )
  {
    // Subclasses can implement if necessary, otherwise return the default
    return ActionConstants.SUCCESS_DELETE;
  }

  protected String getUpdateFailureForward( CharacteristicForm form )
  {
    // Subclasses can implement if necessary, otherwise return the default
    return ActionConstants.FAIL_FORWARD;
  }

  protected String getInsertFailureForward( CharacteristicForm form )
  {
    // Subclasses can implement if necessary, otherwise return the default
    return ActionConstants.FAIL_FORWARD;
  }

  protected String getDeleteFailureForward( CharacteristicForm form )
  {
    // Subclasses can implement if necessary, otherwise return the default
    return ActionConstants.FAIL_FORWARD;
  }

  protected String getCancelForward( CharacteristicForm form )
  {
    // Subclasses can implement if necessary, otherwise return the default
    return ActionConstants.CANCEL_FORWARD;
  }

  /**
   * @param charForm
   */
  public abstract void onDisplayCreate( CharacteristicForm charForm );

}
