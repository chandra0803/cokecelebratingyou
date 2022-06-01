/*
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/product/ProductCategorySearchAction.java,v $
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.ui.product;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.product.ProductCategory;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.product.ProductCategoryService;
import com.biperf.core.service.product.ProductCategoryToParentCategoryAssociation;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.InvalidClientStateException;

/**
 * ProductCategorySearchAction.
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
 * <td>June 28, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ProductCategorySearchAction extends BaseDispatchAction
{
  public static String RETURN_ACTION_URL_PARAM = "returnActionUrl";
  public static String SESSION_RETURN_CATEGORY = "sessionReturnCategory";

  /**
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward unspecified( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    return displaySearch( actionMapping, actionForm, request, response );
  }

  /**
   * Displays the NodeLookup Search Page.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward displaySearch( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ProductCategorySearchForm searchForm = (ProductCategorySearchForm)form;
    searchForm.setReturnActionUrl( RequestUtils.getOptionalParamString( request, RETURN_ACTION_URL_PARAM ) );
    Long categoryId = searchForm.getCategoryId();
    if ( categoryId == null )
    {
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
          String categoryIdString = (String)clientStateMap.get( "categoryId" );
          categoryId = new Long( categoryIdString );
        }
      }
      catch( InvalidClientStateException e )
      {
        // do nothing since this was an optional parameter
      }
      searchForm.setCategoryId( categoryId );
    }

    return mapping.findForward( ActionConstants.SEARCH_FORWARD );
  }

  /**
   * Displays the CategoryLookup Search Page.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   * @throws IOException
   */
  public ActionForward continueBack( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException
  {
    ProductCategorySearchForm searchForm = (ProductCategorySearchForm)form;

    ProductCategory productCategory = null;

    Long categoryId = searchForm.getCategoryId();
    Long subCategoryId = searchForm.getSubCategoryId();
    if ( subCategoryId != null && !subCategoryId.equals( new Long( -1 ) ) )
    {
      categoryId = subCategoryId;
    }

    if ( !categoryId.equals( new Long( -1 ) ) )
    {
      AssociationRequestCollection reqCollection = new AssociationRequestCollection();
      reqCollection.add( new ProductCategoryToParentCategoryAssociation() );
      productCategory = getProductCategoryService().getProductCategoryById( categoryId, reqCollection );
    }

    request.getSession().setAttribute( SESSION_RETURN_CATEGORY, productCategory );

    response.sendRedirect( RequestUtils.getBaseURI( request ) + searchForm.getReturnActionUrl() );

    return null;
  }

  /**
   * Gets a ProductCategoryService
   * 
   * @return ProductCategoryService
   */
  private ProductCategoryService getProductCategoryService()
  {
    return (ProductCategoryService)getService( ProductCategoryService.BEAN_NAME );
  }
}
