/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.ui.product;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.biperf.core.domain.product.Product;
import com.biperf.core.domain.product.ProductCharacteristic;
import com.biperf.core.domain.product.ProductCharacteristicType;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.exception.UniqueConstraintViolationException;
import com.biperf.core.service.product.ProductCharacteristicService;
import com.biperf.core.service.product.ProductService;
import com.biperf.core.service.util.ServiceError;
import com.biperf.core.ui.characteristic.CharacteristicAction;
import com.biperf.core.ui.characteristic.CharacteristicForm;

/**
 * ProductCharacteristicTypeAction.
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
 * <td>wadzinsk</td>
 * <td>Jun 7, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ProductCharacteristicTypeAction extends CharacteristicAction
{
  /**
   * Overridden from
   * 
   * @param charForm
   * @throws ServiceErrorException
   */
  public void makeDeleteCharacteristicServiceCall( CharacteristicForm charForm ) throws ServiceErrorException
  {
    String[] idsToDelete = charForm.getDeleteValues();

    // There are two ways to reach this method. If you are accessing the page for a given
    // product (domainId is not null), then just remove the ProductCharacteristic record.
    // Otherwise remove the ProductCharacteristicType itself, as long as there are no
    // dependencies.

    if ( charForm.getDomainId() == null || charForm.getDomainId().equals( "" ) )
    {
      List serviceErrors = new ArrayList();

      for ( int i = 0; i < idsToDelete.length; i++ )
      {
        Long idToDelete = new Long( idsToDelete[i] );
        ProductCharacteristicType charItem = (ProductCharacteristicType)getCharacteristicService().getCharacteristicById( idToDelete );

        // Check to see if this Characteristic is assigned to any active products, if so,
        // delete is not allowed.
        if ( getCharacteristicService().isDeleteProductCharacteristicTypeAllowed( idToDelete ) )
        {
          charItem.setActive( false );
          getCharacteristicService().saveCharacteristic( charItem );
        }
        else
        {
          serviceErrors.add( new ServiceError( "admin.characteristic.errors.PROD_CHAR_DELETE", charItem.getCharacteristicName() ) );
        }
      }

      if ( !serviceErrors.isEmpty() )
      {
        throw new ServiceErrorException( serviceErrors );
      }
    }
    else
    {
      Product product = getProductService().getProductById( new Long( charForm.getDomainId() ) );
      Set productCharacteristics = product.getProductCharacteristics();

      for ( Iterator iter = productCharacteristics.iterator(); iter.hasNext(); )
      {
        ProductCharacteristic productCharacteristic = (ProductCharacteristic)iter.next();

        for ( int i = 0; i < idsToDelete.length; i++ )
        {
          Long idToDelete = new Long( idsToDelete[i] );
          if ( idToDelete.longValue() == productCharacteristic.getProductCharacteristicType().getId().longValue() )
          {
            iter.remove();
          }
        }
      }

      try
      {
        getProductService().save( product.getProductCategory().getId(), product );
      }
      catch( UniqueConstraintViolationException e )
      {
        throw new ServiceErrorException( "Product name '" + product.getName() + "' is not unique within the category family." );
      }
    }
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.ui.characteristic.CharacteristicAction#makeSaveCharacteristicServiceCall(com.biperf.core.ui.characteristic.CharacteristicForm)
   * @param charForm
   * @throws ServiceErrorException
   */
  public void makeSaveCharacteristicServiceCall( CharacteristicForm charForm ) throws ServiceErrorException
  {
    ProductCharacteristicType productCharacteristicType = charForm.toDomainObjectProductCharacteristicType();

    ProductCharacteristicType characteristic = (ProductCharacteristicType)getCharacteristicService().saveCharacteristic( productCharacteristicType );

    // Since this action allows for adding a new product characteristic to a specific product
    // or just a new stand alone product characteristic
    if ( charForm.getDomainId() != null && !charForm.getDomainId().equals( "" ) )
    {
      Product product = getProductService().getProductById( new Long( charForm.getDomainId() ) );
      product.addProductCharacteristicByType( characteristic );
      try
      {
        getProductService().save( product.getProductCategory().getId(), product );
      }
      catch( UniqueConstraintViolationException e )
      {
        throw new ServiceErrorException( "Product name '" + product.getName() + "' is not unique within the category family." );
      }
    }
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.ui.characteristic.CharacteristicAction#makeGetCharacteristicServiceCallAndLoad(java.lang.Long,
   *      com.biperf.core.ui.characteristic.CharacteristicForm)
   * @param characteristicId
   * @param charForm
   */
  public void makeGetCharacteristicServiceCallAndLoad( Long characteristicId, CharacteristicForm charForm )
  {
    ProductCharacteristicType charToUpdate = (ProductCharacteristicType)getCharacteristicService().getCharacteristicById( characteristicId );
    charForm.loadProductCharacteristicType( charToUpdate );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.ui.characteristic.CharacteristicAction#onDisplayCreate(com.biperf.core.ui.characteristic.CharacteristicForm)
   * @param charForm
   */
  public void onDisplayCreate( CharacteristicForm charForm )
  {
    // Initial Unique property (not done in form because form is shared with other charactertic
    // types
    charForm.setUnique( Boolean.FALSE );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.ui.characteristic.CharacteristicAction#getDeleteSuccessForward(com.biperf.core.ui.characteristic.CharacteristicForm)
   * @param charForm
   * @return String
   */
  public String getDeleteSuccessForward( CharacteristicForm charForm )
  {
    // If you are accessing the page for a given
    // product (domainId is not null), then page should be default.
    // Otherwise, it should be the no_product mapping.

    String forwardTo = super.getDeleteSuccessForward( charForm );

    if ( charForm.getDomainId() == null || charForm.getDomainId().equals( "" ) )
    {
      forwardTo = "success_delete_no_product";
    }
    return forwardTo;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.ui.characteristic.CharacteristicAction#getUpdateSuccessForward(com.biperf.core.ui.characteristic.CharacteristicForm)
   * @param charForm
   * @return String
   */
  public String getUpdateSuccessForward( CharacteristicForm charForm )
  {
    // If you are accessing the page for a given
    // product (domainId is not null), then page should be default.
    // Otherwise, it should be the no_product mapping.

    String forwardTo = super.getUpdateSuccessForward( charForm );

    if ( charForm.getDomainId() == null || charForm.getDomainId().equals( "" ) )
    {
      forwardTo = "success_no_product";
    }
    return forwardTo;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.ui.characteristic.CharacteristicAction#getInsertSuccessForward(com.biperf.core.ui.characteristic.CharacteristicForm)
   * @param charForm
   * @return String
   */
  public String getInsertSuccessForward( CharacteristicForm charForm )
  {
    // If you are accessing the page for a given
    // product (domainId is not null), then page should be default.
    // Otherwise, it should be the no_product mapping.

    String forwardTo = super.getInsertSuccessForward( charForm );

    if ( charForm.getDomainId() == null || charForm.getDomainId().equals( "" ) )
    {
      forwardTo = "success_no_product";
    }
    return forwardTo;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.ui.characteristic.CharacteristicAction#getDeleteFailureForward(com.biperf.core.ui.characteristic.CharacteristicForm)
   * @param charForm
   * @return String
   */
  public String getDeleteFailureForward( CharacteristicForm charForm )
  {
    // If you are accessing the page for a given
    // product (domainId is not null), then page should be default.
    // Otherwise, it should be the no_product mapping.

    String forwardTo = super.getDeleteFailureForward( charForm );

    if ( charForm.getDomainId() == null || charForm.getDomainId().equals( "" ) )
    {
      forwardTo = "failure_no_product";
    }
    return forwardTo;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.ui.characteristic.CharacteristicAction#getCancelForward(com.biperf.core.ui.characteristic.CharacteristicForm)
   * @param charForm
   * @return String
   */
  public String getCancelForward( CharacteristicForm charForm )
  {
    // If you are accessing the page for a given
    // product (domainId is not null), then page should be default.
    // Otherwise, it should be the no_product mapping.

    String forwardTo = super.getCancelForward( charForm );

    if ( charForm.getDomainId() == null || charForm.getDomainId().equals( "" ) )
    {
      forwardTo = "cancel_no_product";
    }
    return forwardTo;
  }

  /**
   * Get the CharacteristicService From the bean factory through a locator.
   * 
   * @return ProductCharacteristicService
   */
  protected ProductCharacteristicService getCharacteristicService()
  {
    return (ProductCharacteristicService)getService( ProductCharacteristicService.BEAN_NAME );
  }

  /**
   * Get the ProductService from the beanLocator.
   * 
   * @return ProductService
   */
  protected ProductService getProductService()
  {
    return (ProductService)getService( ProductService.BEAN_NAME );
  }
}
