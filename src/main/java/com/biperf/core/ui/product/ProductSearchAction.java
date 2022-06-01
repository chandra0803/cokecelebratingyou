/*
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/product/ProductSearchAction.java,v $
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.ui.product;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.product.Product;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.product.ProductAssociationRequest;
import com.biperf.core.service.product.ProductSearchCriteria;
import com.biperf.core.service.product.ProductService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.core.value.FormattedValueBean;

/**
 * ProductSearchAction.
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
public class ProductSearchAction extends BaseDispatchAction
{
  public static final String PRODUCT_LIST = "productList";
  public static String RETURN_ACTION_URL_PARAM = "returnActionUrl";
  public static String SESSION_RETURN_PRODUCT_LIST = "returnProductList";

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
   * Displays the Product Lookup Search Page.
   *
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward displaySearch( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ProductSearchForm searchForm = (ProductSearchForm)form;
    searchForm.setReturnActionUrl( RequestUtils.getOptionalParamString( request, RETURN_ACTION_URL_PARAM ) );
    Long categoryId = null;
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
      // do nothing since this is an option parameter
    }
    searchForm.setCategoryId( categoryId );
    prepareSelectedBox( request, searchForm );

    return mapping.findForward( ActionConstants.SEARCH_FORWARD );
  }

  /**
   * Searches for products based on the search criteria.
   *
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward search( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ProductSearchForm productSearchForm = (ProductSearchForm)form;

    ProductSearchCriteria searchCriteria = productSearchForm.toDomainObject();

    // get a list of product domain objects based on the search criteria
    List productList = getProductList( searchCriteria );

    // The search results are displayed in a multi select box that only allows
    // one value for the property and labelProperty attributes, the requirement is
    // to display "product.name; + product.code;". To do this a FormattedValueBean
    // is used to hold the formatted values.
    List formattedProductList = new ArrayList();

    for ( int i = 0; i < productList.size(); i++ )
    {
      Product product = (Product)productList.get( i );
      FormattedValueBean valueBean = new FormattedValueBean();
      valueBean.setId( product.getId() );
      valueBean.setValue( product.getName() + "    " + product.getCode() + ";" );
      formattedProductList.add( valueBean );
    }

    // Place the formattedProductList into the request for access later.
    request.setAttribute( PRODUCT_LIST, formattedProductList );

    prepareSelectedBox( request, productSearchForm );

    return mapping.findForward( ActionConstants.SUCCESS_SEARCH );
  }

  protected List getProductList( ProductSearchCriteria searchCriteria )
  {
    return getProductService().searchProducts( searchCriteria );
  }

  /**
   * Continues back to the calling page.
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
    ProductSearchForm searchForm = (ProductSearchForm)form;

    List productList = new ArrayList();

    if ( searchForm.getSelectedBox() != null && searchForm.getSelectedBox().length > 0 )
    {
      for ( int i = 0; i < searchForm.getSelectedBox().length; i++ )
      {
        FormattedValueBean valueBean = FormattedValueBean.parseFormattedId( searchForm.getSelectedBox()[i] );

        AssociationRequestCollection reqCollection = new AssociationRequestCollection();
        reqCollection.add( new ProductAssociationRequest( ProductAssociationRequest.PARENT_PRODUCT_CATEGORY ) );
        Product product = getProductService().getProductById( valueBean.getId(), reqCollection );
        productList.add( product );
      }
    }

    request.getSession().setAttribute( SESSION_RETURN_PRODUCT_LIST, productList );

    response.sendRedirect( RequestUtils.getBaseURI( request ) + searchForm.getReturnActionUrl() );

    return null;
  }

  private void prepareSelectedBox( HttpServletRequest request, ProductSearchForm searchForm )
  {
    // Get the selected box items from the form and place that list in the request for
    // redisplay. Also get the productList from the request and remove any results
    // that are already selected.

    List selectedList = null;
    if ( searchForm.getSelectedBox() != null && searchForm.getSelectedBox().length > 0 )
    {
      selectedList = buildSelectedResultList( searchForm.getSelectedBox() );
    }
    List productList = (List)request.getAttribute( PRODUCT_LIST );
    if ( productList != null && selectedList != null )
    {
      productList.removeAll( selectedList );
    }
    request.setAttribute( PRODUCT_LIST, productList );
    request.setAttribute( "selectedResults", selectedList );

    // once the selected list is built, we no longer want those values in the in the form's
    // String[] because when the page displays, they will show up as selected.
    searchForm.setSelectedBox( null );
  }

  /**
   * Builds a list of FormattedValueBean objects from a String[] of id's
   *
   * @param selectedResults
   * @return List
   */
  private List buildSelectedResultList( String[] selectedResults )
  {
    List selectedResultList = new ArrayList();
    for ( int i = 0; i < selectedResults.length; i++ )
    {
      String idValue = selectedResults[i];

      FormattedValueBean selectedResult = FormattedValueBean.parseFormattedId( idValue );

      selectedResultList.add( selectedResult );
    }
    return selectedResultList;
  }

  /**
   * Gets a ProductService
   *
   * @return ProductService
   */
  private ProductService getProductService()
  {
    return (ProductService)getService( ProductService.BEAN_NAME );
  }

}
