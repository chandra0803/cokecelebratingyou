/*
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/product/ProductMaintainAction.java,v $
 * (c) 2005 BI, Inc. All rights reserved.
 */

package com.biperf.core.ui.product;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.domain.product.Product;
import com.biperf.core.domain.product.ProductCharacteristicType;
import com.biperf.core.exception.UniqueConstraintViolationException;
import com.biperf.core.service.product.ProductCharacteristicService;
import com.biperf.core.service.product.ProductService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.characteristic.CharacteristicForm;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.ActionUtils;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.InvalidClientStateException;

/**
 * ProductMaintainAction.
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
 * <td>lee</td>
 * <td>Jun 9, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ProductMaintainAction extends BaseDispatchAction
{
  /**
   * Prepare the display for creating a product.
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

    // get the actionForward to display the udpate pages.
    return mapping.findForward( forwardTo );
  }

  /**
   * Prepare the display for updating a product.
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
    Long productId = new Long( 0 );
    ActionMessages errors = new ActionMessages();

    if ( isCancelled( request ) )
    {
      return mapping.findForward( ActionConstants.CANCEL_FORWARD ); // EARLY EXIT
    }

    ProductForm productForm = (ProductForm)form;
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
        String productIdString = (String)clientStateMap.get( "productId" );
        productId = new Long( productIdString );
      }
      catch( ClassCastException e2 )
      {
        productId = (Long)clientStateMap.get( "productId" );
      }
      catch( NumberFormatException e1 )
      {
        // do nothing as checking for null later on
      }
      if ( productId == null )
      {
        errors.add( "errorMessage", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, "id as part of clientState" ) );
        saveErrors( request, errors );
        return mapping.findForward( ActionConstants.FAIL_FORWARD );
      }
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }
    if ( productForm.getProductCategoryId() == null )
    {
      Product product = getProductService().getProductById( productId );
      productForm.load( product );
    }

    // get the actionForward to display the udpate pages.
    return mapping.findForward( forwardTo );
  }

  /**
   * execute method will forward to the SUCCESS_UPDATE after updating the Product.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward create( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionForward forward = null;

    ProductForm productForm = (ProductForm)form;
    Product product = null;

    ActionMessages errors = new ActionMessages();

    if ( isCancelled( request ) )
    {
      String productCategoryId = productForm.getProductCategoryId();
      if ( productCategoryId == null || productCategoryId.equals( "" ) )
      {
        productCategoryId = "0";
      }
      request.setAttribute( "productCategoryId", new Long( Long.parseLong( productCategoryId ) ) );
      return mapping.findForward( ActionConstants.CANCEL_FORWARD ); // EARLY EXIT
    }

    if ( isTokenValid( request, true ) )
    {
      if ( productForm.getSubProductCategoryId() == null || productForm.getSubProductCategoryId().equals( "" ) )
      {
        try
        {
          product = getProductService().save( new Long( productForm.getProductCategoryId() ), productForm.toDomainObject() );
        }
        catch( UniqueConstraintViolationException e )
        {
          errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "product.errors.UNIQUE_CONSTRAINT" ) );
        }
      }
      else
      {
        try
        {
          product = getProductService().save( new Long( productForm.getSubProductCategoryId() ), productForm.toDomainObject() );
        }
        catch( UniqueConstraintViolationException e )
        {
          errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "product.errors.UNIQUE_CONSTRAINT" ) );
        }
      }
    }
    else
    {
      errors.add( ActionConstants.ERROR_TOKEN_FAILURE, new ActionMessage( ActionConstants.ERROR_TOKEN_EXCEPTION ) );
    }

    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      forward = mapping.findForward( ActionConstants.FAIL_FORWARD );
    }
    else
    {
      Map clientStateParameterMap = new HashMap();
      clientStateParameterMap.put( "productId", product.getId() );
      String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
      queryString += "&method=display";
      forward = ActionUtils.forwardWithParameters( mapping, ActionConstants.SUCCESS_FORWARD, new String[] { queryString } );
    }

    return forward;
  }

  /**
   * Display the product with a list of characteristics.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward display( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    String forwardTo = ActionConstants.SUCCESS_FORWARD;
    CharacteristicForm charForm = (CharacteristicForm)form;
    charForm.setCharacteristicDataType( "" );
    String productId = null;
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
        productId = (String)clientStateMap.get( "productId" );
        if ( productId == null )
        {
          productId = (String)clientStateMap.get( "domainId" );
        }
      }
      catch( ClassCastException e1 )
      {
        productId = ( (Long)clientStateMap.get( "productId" ) ).toString();
        if ( productId == null )
        {
          productId = ( (Long)clientStateMap.get( "domainId" ) ).toString();
        }
      }

      if ( productId == null )
      {
        ActionMessages errors = new ActionMessages();
        errors.add( "errorMessage", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, "productId as part of clientState" ) );
        saveErrors( request, errors );
        return mapping.findForward( ActionConstants.FAIL_ERRORS_ONLY );
      }
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }
    RequestUtils.getOptionalParamString( request, "productId" );
    charForm.setDomainId( productId );

    request.setAttribute( CharacteristicForm.FORM_NAME, charForm );
    // get the actionForward to display the udpate pages.
    return mapping.findForward( forwardTo );
  }

  /**
   * execute method will forward to the SUCCESS_UPDATE after updating the Product.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward update( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionForward forward = null;

    ProductForm productForm = (ProductForm)form;
    Product product = null;

    ActionMessages errors = new ActionMessages();

    if ( isCancelled( request ) )
    {
      return mapping.findForward( ActionConstants.CANCEL_FORWARD ); // EARLY EXIT
    }

    if ( isTokenValid( request, true ) )
    {
      if ( productForm.getSubProductCategoryId() == null || productForm.getSubProductCategoryId().equals( "" ) )
      {
        try
        {
          product = getProductService().save( new Long( productForm.getProductCategoryId() ), productForm.toDomainObject() );
        }
        catch( UniqueConstraintViolationException e )
        {
          errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "product.errors.UNIQUE_CONSTRAINT" ) );
        }
      }
      else
      {
        try
        {
          product = getProductService().save( new Long( productForm.getSubProductCategoryId() ), productForm.toDomainObject() );
        }
        catch( UniqueConstraintViolationException e )
        {
          errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "product.errors.UNIQUE_CONSTRAINT" ) );
        }
      }
    }
    else
    {
      errors.add( ActionConstants.ERROR_TOKEN_FAILURE, new ActionMessage( ActionConstants.ERROR_TOKEN_EXCEPTION ) );
    }
    if ( errors.size() > 0 )
    {
      saveErrors( request, errors );
      forward = mapping.findForward( ActionConstants.FAIL_FORWARD );
    }
    else
    {
      Map clientStateParameterMap = new HashMap();
      clientStateParameterMap.put( "productId", product.getId() );
      String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
      queryString += "&method=display";
      forward = ActionUtils.forwardWithParameters( mapping, ActionConstants.SUCCESS_FORWARD, new String[] { queryString } );
    }

    return forward;
  }

  /**
   * execute method will forward to the SUCCESS_UPDATE after updating the Product.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward addExistingCharacteristic( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionForward forward = null;

    ProductForm productForm = (ProductForm)form;
    Product product = null;

    ActionMessages errors = new ActionMessages();

    if ( isCancelled( request ) )
    {
      return mapping.findForward( ActionConstants.CANCEL_FORWARD ); // EARLY EXIT
    }

    if ( !isTokenValid( request, true ) )
    {
      errors.add( ActionConstants.ERROR_TOKEN_FAILURE, new ActionMessage( ActionConstants.ERROR_TOKEN_EXCEPTION ) );
      saveErrors( request, errors );
      return mapping.findForward( ActionConstants.FAIL_FORWARD );
    }

    String selectedCharacteristicId = productForm.getSelectedCharacteristicId();

    ProductCharacteristicType charType = null;
    if ( selectedCharacteristicId != null && !selectedCharacteristicId.equals( "" ) )
    {
      charType = (ProductCharacteristicType)getCharacteristicService().getCharacteristicById( new Long( selectedCharacteristicId ) );
    }

    if ( charType != null )
    {
      product = getProductService().getProductById( productForm.getId() );
      product.addProductCharacteristicByType( charType );
      try
      {
        product = getProductService().save( product.getProductCategory().getId(), product );
      }
      catch( UniqueConstraintViolationException e )
      {
        errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "product.errors.UNIQUE_CONSTRAINT" ) );
      }
    }

    if ( errors.size() > 0 )
    {
      saveErrors( request, errors );
      forward = mapping.findForward( ActionConstants.FAIL_FORWARD );
    }
    else
    {
      Map clientStateParameterMap = new HashMap();
      clientStateParameterMap.put( "productId", product.getId() );
      String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
      queryString += "&method=display";
      forward = ActionUtils.forwardWithParameters( mapping, ActionConstants.SUCCESS_FORWARD, new String[] { queryString } );
    }

    return forward;
  }

  /**
   * Gets a ProductCategory Service
   * 
   * @return ProductCategoryService
   */
  private ProductService getProductService()
  {
    return (ProductService)getService( ProductService.BEAN_NAME );
  } // end

  /**
   * Get the CharacteristicService From the bean factory through a locator.
   * 
   * @return ProductCharacteristicService
   */
  protected ProductCharacteristicService getCharacteristicService()
  {
    return (ProductCharacteristicService)getService( ProductCharacteristicService.BEAN_NAME );
  }
}
