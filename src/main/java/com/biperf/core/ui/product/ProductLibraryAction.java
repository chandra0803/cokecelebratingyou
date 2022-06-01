/*
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/product/ProductLibraryAction.java,v $
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.ui.product;

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
import com.biperf.core.service.product.ProductSearchCriteria;
import com.biperf.core.service.product.ProductService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.ui.utils.ServiceErrorStrutsUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.InvalidClientStateException;

/**
 * ProductLibraryAction.
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
 * <td>Jun 7, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ProductLibraryAction extends BaseDispatchAction
{
  /**
   * @param mapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward unspecified( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    return displayPage( mapping, actionForm, request, response );
  }

  public ActionForward prepareDisplay( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    Long productCategoryId = null;
    Long parentCategoryId = null;
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
        productCategoryId = new Long( (String)clientStateMap.get( "productCategoryId" ) );
      }
      catch( ClassCastException e1 )
      {
        productCategoryId = (Long)clientStateMap.get( "productCategoryId" );
      }
      try
      {
        if ( clientStateMap.containsKey( "parentCategoryId" ) )
        {
          parentCategoryId = new Long( (String)clientStateMap.get( "parentCategoryId" ) );
        }
      }
      catch( ClassCastException cce )
      {
        parentCategoryId = (Long)clientStateMap.get( "parentCategoryId" );
      }
    }
    catch( InvalidClientStateException e )
    {
      // do nothing since these are optional parameters
    }
    if ( productCategoryId != null && productCategoryId.longValue() != 0 )
    {
      ProductLibraryForm prodLibraryForm = (ProductLibraryForm)actionForm;

      if ( parentCategoryId != null && parentCategoryId.longValue() != 0 )
      {
        prodLibraryForm.setSubCategoryId( productCategoryId );
        prodLibraryForm.setCategoryId( parentCategoryId );
      }
      else
      {
        prodLibraryForm.setCategoryId( productCategoryId );
      }

    }
    return mapping.findForward( ActionConstants.SEARCH_FORWARD );
  }

  /**
   * @param mapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward displayPage( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    return mapping.findForward( ActionConstants.SEARCH_FORWARD );
  }

  public ActionForward search( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ProductLibraryForm productLibraryForm = (ProductLibraryForm)form;

    Long productCategoryId = null;
    Long parentCategoryId = null;
    try
    {
      String clientState = RequestUtils.getOptionalParamString( request, "clientState" );
      if ( clientState != null && clientState.length() > 0 )
      {
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
          if ( clientStateMap.containsKey( "productCategoryId" ) )
          {
            productCategoryId = new Long( (String)clientStateMap.get( "productCategoryId" ) );
          }
        }
        catch( ClassCastException cce )
        {
          productCategoryId = (Long)clientStateMap.get( "productCategoryId" );
        }
        try
        {
          if ( clientStateMap.containsKey( "parentCategoryId" ) )
          {
            parentCategoryId = new Long( (String)clientStateMap.get( "parentCategoryId" ) );
          }
        }
        catch( ClassCastException cce )
        {
          parentCategoryId = (Long)clientStateMap.get( "parentCategoryId" );
        }
      }
    }
    catch( InvalidClientStateException e )
    {
      // do nothing since these are optional parameters
    }
    if ( productCategoryId != null && productCategoryId.longValue() != 0 )
    {
      if ( parentCategoryId != null && parentCategoryId.longValue() != 0 )
      {
        productLibraryForm.setSubCategoryId( productCategoryId );
        productLibraryForm.setCategoryId( parentCategoryId );
      }
      else
      {
        productLibraryForm.setCategoryId( productCategoryId );
        productLibraryForm.setSubCategoryId( new Long( -1 ) );
      }

    }

    ProductSearchCriteria searchCriteria = productLibraryForm.toDomainObject();

    List productList = getProductService().searchProducts( searchCriteria );
    request.setAttribute( "productList", productList );

    return mapping.findForward( ActionConstants.SUCCESS_SEARCH );
  }

  public ActionForward searchProductSubCategory( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    ProductLibraryForm productLibraryForm = (ProductLibraryForm)form;
    Long productCategoryId = null;

    if ( productLibraryForm.getCategoryId() != null && productLibraryForm.getCategoryId().longValue() > 0 )
    {
      productCategoryId = productLibraryForm.getCategoryId();
    }

    ProductUtils.setProductCategoryList( request, true );
    ProductUtils.setProductSubcategoryList( request, productCategoryId, true );

    return mapping.findForward( ActionConstants.SUCCESS_SEARCH );
  }

  public ActionForward delete( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionMessages errors = new ActionMessages();
    if ( !isTokenValid( request, true ) )
    {
      errors.add( ActionConstants.ERROR_TOKEN_FAILURE, new ActionMessage( ActionConstants.ERROR_TOKEN_EXCEPTION ) );
      saveErrors( request, errors );
      return mapping.findForward( ActionConstants.FAIL_DELETE ); // EARLY EXIT
    }

    ProductLibraryForm productLibraryForm = (ProductLibraryForm)form;
    Long[] deleteProductIds = productLibraryForm.getDeleteProductIds();
    try
    {
      if ( deleteProductIds != null )
      {
        for ( int i = 0; i < deleteProductIds.length; i++ )
        {
          Long deletProductId = deleteProductIds[i];
          getProductService().deleteProduct( deletProductId );
        }
      }
    }
    catch( ServiceErrorException e )
    {
      log.debug( e );
      List serviceErrors = e.getServiceErrors();
      ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, errors );
    }

    // in any case show the product list again
    ProductSearchCriteria searchCriteria = productLibraryForm.toDomainObject();

    List productList = getProductService().searchProducts( searchCriteria );
    request.setAttribute( "productList", productList );

    ActionForward actionForward;
    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      actionForward = mapping.findForward( ActionConstants.FAIL_DELETE );
    }
    else
    {
      actionForward = mapping.findForward( ActionConstants.SUCCESS_DELETE );
    }

    return actionForward;
  }

  /**
   * Gets a ProductService
   * 
   * @return ProductService
   */
  private ProductService getProductService()
  {
    return (ProductService)getService( ProductService.BEAN_NAME );
  } // end

}
