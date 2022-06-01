/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/system/SystemVariableAction.java,v $
 */

package com.biperf.core.ui.system;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.cache.CacheManagementService;
import com.biperf.core.service.home.FilterAppSetupService;
import com.biperf.core.service.maincontent.MainContentService;
import com.biperf.core.service.merchlevel.MerchLevelService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.ui.utils.ServiceErrorStrutsUtils;
import com.biperf.core.utils.UserManager;

/**
 * Action class for SystemVariable CRUD operations.
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
 * <td>waldal</td>
 * <td>Apr 5, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class SystemVariableAction extends BaseDispatchAction
{
  /** Log */
  private static final Log LOG = LogFactory.getLog( SystemVariableAction.class );

  public ActionForward clearSystemVariables( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {

    // clear cache for all system variables
    this.getCacheManagementService().clearCache( "com.biperf.core.domain.system.PropertySetItem" );
    // this.getCacheManagementService().clearCache( SystemVariableDAOImpl.CACHE_REGION_NAME );
    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  public ActionForward clearMerchLevel( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {

    // clear cache for all system variables
    MerchLevelService merchLevelService = getMerchLevelService();
    merchLevelService.clearPropertyFromCache();

    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  /**
   * Prepare the display for creating a system variable.
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

    SystemVariableForm updateSystemVariableForm = (SystemVariableForm)form;

    updateSystemVariableForm.setMethod( "displayCreate" );

    // get the actionForward to display the udpate pages.
    return mapping.findForward( forwardTo );
  }

  /**
   * Prepare the display for updating a system variable.
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
    String entityName = "";
    ActionMessages errors = new ActionMessages();

    if ( isCancelled( request ) )
    {
      return mapping.findForward( ActionConstants.CANCEL_FORWARD ); // EARLY EXIT
    }

    SystemVariableForm updateSystemVariableForm = (SystemVariableForm)form;

    try
    {
      // The requestUtils will throw an exception if the required entityName param is not in the
      // request.
      entityName = RequestUtils.getRequiredParamString( request, "entityName" );
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
      PropertySetItem propertySetToUpdate = getSystemVariableService().getPropertyByName( entityName );
      updateSystemVariableForm.load( propertySetToUpdate );
      updateSystemVariableForm.setMethod( "displayUpdate" );
    }

    // get the actionForward to display the udpate pages.
    return mapping.findForward( forwardTo );
  }

  /**
   * execute method will forward to the SUCCESS_UPDATE after updating the SystemVariable.
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

    if ( isCancelled( request ) )
    {
      return mapping.findForward( ActionConstants.CANCEL_FORWARD ); // EARLY EXIT
    }

    SystemVariableForm systemVariableForm = (SystemVariableForm)form;

    ActionMessages errors = new ActionMessages();

    if ( isTokenValid( request, true ) )
    {
      try
      {
        getSystemVariableService().saveProperty( systemVariableForm.toDomainObject() );

        if ( systemVariableForm.toDomainObject().getEntityName().equals( SystemVariableService.PARTICIPANT_ALLOW_ESTATEMENTS ) )
        {
          // need to set all participants on or off
          getParticipantService().updateAllActiveAllowEstatements( systemVariableForm.toDomainObject().getBooleanVal() );
        }
        else if ( systemVariableForm.getEntityName().equalsIgnoreCase( SystemVariableService.MEPLUS_ENABLED ) )
        {
          getFilterAppSetupService().updateMEPlusFilterPageSetup( Boolean.parseBoolean( systemVariableForm.getStringVal() ) );
        }
        else if ( systemVariableForm.getEntityName().equalsIgnoreCase( SystemVariableService.RECOGNITION_ONLY_ENABLED ) )
        {
          getFilterAppSetupService().updateRecognitionOnlyFilterPageSetup( Boolean.parseBoolean( systemVariableForm.getStringVal() ) );
        }
        else if ( systemVariableForm.getEntityName().equalsIgnoreCase( SystemVariableService.SALES_MAKER ) )
        {
          getFilterAppSetupService().updateSalesMakerFilterPageSetup( Boolean.parseBoolean( systemVariableForm.getStringVal() ) );
          getMainContentService().clearMenuCache( UserManager.getUser() );
        }
        else if ( systemVariableForm.getEntityName().equalsIgnoreCase( SystemVariableService.SELF_ENROLL_ALLOWED ) )
        {
          getMainContentService().clearMenuCache( UserManager.getUser() );
        }

      }
      catch( ServiceErrorException serviceException )
      {
        List serviceErrors = serviceException.getServiceErrors();
        ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, errors );
        saveErrors( request, errors );
        return mapping.findForward( ActionConstants.FAIL_FORWARD );
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
   * execute method will forward to the SUCCESS_DELETE after deleting the SystemVariable.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward deleteSystemVariable( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    SystemVariableForm systemVariableForm = (SystemVariableForm)form;

    String forwardTo = ActionConstants.SUCCESS_DELETE;

    try
    {
      if ( systemVariableForm.getDeleteValues() != null )
      {
        for ( int i = 0; i < systemVariableForm.getDeleteValues().length; i++ )
        {
          PropertySetItem prop = getSystemVariableService().getPropertyByName( systemVariableForm.getDeleteValues()[i] );
          getSystemVariableService().deleteProperty( prop );
        }
      }
    }
    catch( Exception e )
    {
      LOG.error( "SystemVariableAction.deleteSystemVariable: failed to get service " + e.getMessage() );
      forwardTo = ActionConstants.FAIL_FORWARD;
    }

    return mapping.findForward( forwardTo );
  }

  // Private Methods
  /**
   * Get the SystemVariableService From the bean factory through a locator.
   * 
   * @return SystemVariableService
   */
  private SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }

  private CacheManagementService getCacheManagementService()
  {
    return (CacheManagementService)getService( CacheManagementService.BEAN_NAME );
  }

  private MerchLevelService getMerchLevelService()
  {
    return (MerchLevelService)getService( MerchLevelService.BEAN_NAME );
  }

  private ParticipantService getParticipantService()
  {
    return (ParticipantService)getService( ParticipantService.BEAN_NAME );
  }

  private FilterAppSetupService getFilterAppSetupService()
  {
    return (FilterAppSetupService)getService( FilterAppSetupService.BEAN_NAME );
  }

  private MainContentService getMainContentService()
  {
    return (MainContentService)getService( MainContentService.BEAN_NAME );
  }

}
