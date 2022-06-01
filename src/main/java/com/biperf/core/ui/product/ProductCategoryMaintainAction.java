/*
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/product/ProductCategoryMaintainAction.java,v $
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.ui.product;

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

import com.biperf.core.domain.product.ProductCategory;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.exception.UniqueConstraintViolationException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.product.ProductCategoryService;
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
 * ProductCategoryMaintainAction.
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
 * <td>sharma</td>
 * <td>Jun 3, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ProductCategoryMaintainAction extends BaseDispatchAction
{
  /**
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward update( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    ActionMessages errors = new ActionMessages();
    if ( !isTokenValid( request, true ) )
    {
      errors.add( ActionConstants.ERROR_TOKEN_FAILURE, new ActionMessage( ActionConstants.ERROR_TOKEN_EXCEPTION ) );
      saveErrors( request, errors );
      return actionMapping.findForward( ActionConstants.FAIL_UPDATE ); // EARLY EXIT
    }

    ProductCategoryForm productCategoryForm = (ProductCategoryForm)actionForm;
    Long parentCategoryId = productCategoryForm.getParentCategoryId();
    try
    {
      ProductCategory productCategory = getProductCategoryService().getProductCategoryById( productCategoryForm.getId(), null );
      productCategoryForm.populateDomainObject( productCategory );
      if ( parentCategoryId != null )
      {
        ProductCategory parentCategory = getProductCategoryService().getProductCategoryById( parentCategoryId, null );
        productCategory.setParentProductCategory( parentCategory );
      }
      getProductCategoryService().saveProductCategory( productCategory, parentCategoryId );
      productCategoryForm.setId( productCategory.getId() );
    }
    catch( UniqueConstraintViolationException e )
    {
      errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "product.errors.DUPLICATE_NAME" ) );
    }

    ActionForward actionForward;
    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      actionForward = actionMapping.findForward( ActionConstants.FAIL_UPDATE );
    }
    else
    {
      Long id = productCategoryForm.getId();
      if ( parentCategoryId != null )
      {
        id = parentCategoryId;
      }
      Map clientStateParameterMap = new HashMap();
      clientStateParameterMap.put( "productCategoryId", id );
      String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
      actionForward = ActionUtils.forwardWithParameters( actionMapping, ActionConstants.SUCCESS_UPDATE, new String[] { queryString } );
    }

    return actionForward;
  }

  /**
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
    if ( !isTokenValid( request, true ) )
    {
      errors.add( ActionConstants.ERROR_TOKEN_FAILURE, new ActionMessage( ActionConstants.ERROR_TOKEN_EXCEPTION ) );
      saveErrors( request, errors );
      forwardTo = ActionConstants.FAIL_CREATE;

    }
    else
    {

      ProductCategoryForm productCategoryForm = (ProductCategoryForm)actionForm;
      Long parentCategoryId = productCategoryForm.getParentCategoryId();

      try
      {
        ProductCategory productCategory = productCategoryForm.toDomainObject();
        if ( parentCategoryId != null )
        {
          ProductCategory parentCategory = getProductCategoryService().getProductCategoryById( parentCategoryId, null );
          productCategory.setParentProductCategory( parentCategory );
        }

        getProductCategoryService().saveProductCategory( productCategory, parentCategoryId );
        request.setAttribute( "productCategoryId", productCategory.getId() );

      }
      catch( UniqueConstraintViolationException e )
      {
        errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "product.errors.DUPLICATE_NAME" ) );
      }

      if ( !errors.isEmpty() )
      {
        saveErrors( request, errors );
        forwardTo = ActionConstants.FAIL_CREATE;
      }

    }

    return actionMapping.findForward( forwardTo );
  }

  /**
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward prepareUpdate( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    String forwardTo = ActionConstants.UPDATE_FORWARD;
    Long productCategoryId = null;
    if ( RequestUtils.containsAttribute( request, "productCategoryId" ) )
    {
      RequestUtils.getOptionalAttributeLong( request, "productCategoryId" );
    }
    else
    {
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
          productCategoryId = new Long( (String)clientStateMap.get( "id" ) );
        }
        catch( ClassCastException cce )
        {
          productCategoryId = (Long)clientStateMap.get( "id" );
        }
      }
      catch( InvalidClientStateException e )
      {
        // do nothing since this was an optional parameter
      }
    }

    ProductCategoryForm productCategoryForm = (ProductCategoryForm)actionForm;
    productCategoryForm.setMethod( "update" );

    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    ProductCategory productCategory = getProductCategoryService().getProductCategoryById( productCategoryId, associationRequestCollection );
    productCategoryForm.loadDomainObject( productCategory );

    request.setAttribute( "productCategory", productCategory );
    request.setAttribute( "productCategoryId", productCategory.getId() );

    return actionMapping.findForward( forwardTo );
  }

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward prepareCreate( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ProductCategoryForm productCategoryForm = (ProductCategoryForm)form;

    ProductCategory productCategory = new ProductCategory();
    productCategoryForm.loadDomainObject( productCategory );
    request.setAttribute( "productCategory", productCategory );

    // get the actionForward to display the create pages.
    return mapping.findForward( ActionConstants.CREATE_FORWARD );
  }

  /**
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward delete( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    ActionMessages errors = new ActionMessages();
    if ( !isTokenValid( request, true ) )
    {
      errors.add( ActionConstants.ERROR_TOKEN_FAILURE, new ActionMessage( ActionConstants.ERROR_TOKEN_EXCEPTION ) );
      saveErrors( request, errors );
      return actionMapping.findForward( ActionConstants.FAIL_UPDATE ); // EARLY EXIT
    }

    ProductCategoryForm productCategoryForm = (ProductCategoryForm)actionForm;
    try
    {
      Long[] deleteIds = productCategoryForm.getDeleteProductCategoryIds();
      if ( deleteIds != null )
      {
        getProductCategoryService().deleteProductCategoryList( deleteIds );
      }
    }
    catch( ServiceErrorException e )
    {
      log.debug( e );
      List serviceErrors = e.getServiceErrors();
      ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, errors );
    }

    ActionForward actionForward;
    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      actionForward = actionMapping.findForward( ActionConstants.FAIL_DELETE );
    }
    else
    {
      actionForward = actionMapping.findForward( ActionConstants.SUCCESS_DELETE );
    }
    return actionForward;
  }

  /**
   * Gets a ProductCategory Service
   * 
   * @return ProductCategoryService
   */
  private ProductCategoryService getProductCategoryService()
  {
    return (ProductCategoryService)getService( ProductCategoryService.BEAN_NAME );
  } // end
}
