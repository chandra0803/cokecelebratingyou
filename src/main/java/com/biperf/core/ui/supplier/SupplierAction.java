/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/supplier/SupplierAction.java,v $ */

package com.biperf.core.ui.supplier;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.domain.supplier.Supplier;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.exception.UniqueConstraintViolationException;
import com.biperf.core.service.integration.SupplierService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.ui.utils.ServiceErrorStrutsUtils;
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
 * <td>sedey</td>
 * <td>June 2, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class SupplierAction extends BaseDispatchAction
{
  /** Log */
  // private static final Log logger = LogFactory.getLog( SupplierAction.class );
  /**
   * Prepare the display for creating a supplier.
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

    SupplierForm createSupplierForm = (SupplierForm)form;

    createSupplierForm.setMethod( "displayCreate" );

    // get the actionForward to display the udpate pages.
    return mapping.findForward( forwardTo );
  }

  /**
   * Prepare the display for updating a supplier.
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

    if ( isCancelled( request ) )
    {
      return mapping.findForward( ActionConstants.CANCEL_FORWARD ); // EARLY EXIT
    }

    SupplierForm updateSupplierForm = (SupplierForm)form;
    String supplierId = null;
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
        supplierId = (String)clientStateMap.get( "supplierId" );
      }
      catch( ClassCastException cce )
      {
        Long id = (Long)clientStateMap.get( "supplierId" );
        supplierId = id.toString();
      }
      if ( supplierId == null )
      {
        errors.add( "errorMessage", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, "supplierId as part of clientState" ) );
        saveErrors( request, errors );
        return mapping.findForward( ActionConstants.FAIL_FORWARD );
      }
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }
    Supplier supplierToUpdate = getSupplierService().getSupplierById( new Long( supplierId ) );
    updateSupplierForm.load( supplierToUpdate );
    updateSupplierForm.setMethod( "displayUpdate" );

    // get the actionForward to display the udpate pages.
    return mapping.findForward( forwardTo );
  }

  /**
   * execute method will forward to the SUCCESS_UPDATE after updating the Supplier.
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

    SupplierForm supplierForm = (SupplierForm)form;

    ActionMessages errors = new ActionMessages();

    if ( isCancelled( request ) )
    {
      return mapping.findForward( ActionConstants.CANCEL_FORWARD ); // EARLY EXIT
    }

    if ( isTokenValid( request, true ) )
    {
      try
      {
        getSupplierService().saveSupplier( supplierForm.toDomainObject() );
      }
      catch( UniqueConstraintViolationException ex )
      {
        errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "admin.supplier.errors.DUPLICATE" ) );
      }
      catch( ServiceErrorException e )
      {
        log.debug( e );
        List serviceErrors = e.getServiceErrors();
        ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, errors );
        saveErrors( request, errors );
        forwardTo = ActionConstants.FAIL_FORWARD;
        return mapping.findForward( forwardTo );
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
   * execute method will forward to the SUCCESS_UPDATE after updating the Supplier.
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

    SupplierForm supplierForm = (SupplierForm)form;

    ActionMessages errors = new ActionMessages();

    if ( isCancelled( request ) )
    {
      return mapping.findForward( ActionConstants.CANCEL_FORWARD ); // EARLY EXIT
    }

    Long associationCount = getSupplierService().getNumberOfAssociationsForSupplier( supplierForm.getSupplierId() );
    if ( associationCount != 0 )
    {
      if ( supplierForm.getStatus().equals( "inactive" ) )
      {
        // error
        errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "admin.supplier.errors.ASSOCIATION_EXISTS" ) );
      }
      if ( errors.size() > 0 )
      {
        saveErrors( request, errors );
        forwardTo = ActionConstants.FAIL_FORWARD;
      }
    }
    if ( isTokenValid( request, true ) )
    {
      try
      {
        getSupplierService().saveSupplier( supplierForm.toDomainObject() );
      }
      catch( UniqueConstraintViolationException ex )
      {
        errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "admin.supplier.errors.DUPLICATE" ) );
      }
      catch( ServiceErrorException e )
      {
        log.debug( e );
        List serviceErrors = e.getServiceErrors();
        ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, errors );
        saveErrors( request, errors );
        forwardTo = ActionConstants.FAIL_FORWARD;
        return mapping.findForward( forwardTo );
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

  // Private Methods
  /**
   * Get the SupplierService From the bean factory through a locator.
   * 
   * @return SupplierService
   */
  private SupplierService getSupplierService()
  {
    return (SupplierService)getService( SupplierService.BEAN_NAME );
  }
}
