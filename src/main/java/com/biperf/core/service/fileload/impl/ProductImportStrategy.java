/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/fileload/impl/ProductImportStrategy.java,v $
 */

package com.biperf.core.service.fileload.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.fileload.ImportFile;
import com.biperf.core.domain.fileload.ProductImportRecord;
import com.biperf.core.domain.product.Product;
import com.biperf.core.domain.product.ProductCategory;
import com.biperf.core.domain.product.ProductCharacteristicType;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.exception.UniqueConstraintViolationException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.fileload.ImportStrategy;
import com.biperf.core.service.product.ProductCategoryService;
import com.biperf.core.service.product.ProductCategoryToParentCategoryAssociation;
import com.biperf.core.service.product.ProductCharacteristicService;
import com.biperf.core.service.product.ProductService;
import com.biperf.core.service.product.ProductSubcategoryAssociation;
import com.biperf.core.service.util.ServiceError;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.util.StringUtils;

/*
 * ProductImportStrategy <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th>
 * <th>Date</th> <th>Version</th> <th>Comments</th> </tr> <tr> <td>jenniget</td> <td>Jan
 * 31, 2005</td> <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 *
 */

public class ProductImportStrategy extends ImportStrategy
{
  private static final Log logger = LogFactory.getLog( ProductImportStrategy.class );
  public static final String PRODUCT_CATEGORY_NAME = "product.productcategory.details.NAME";
  public static final String PRODUCT_CATEGORY_DESCRIPTION = "product.productcategory.details.DESCRIPTION";
  public static final String PRODUCT_NAME = "product.maintainproducts.PRODUCT_NAME_LABEL";
  public static final String PRODUCT_DESCRIPTION = "product.productcategory.details.DESCRIPTION_LABEL";
  public static final String INVALID_CHARACTERISTIC_ENTRY = "system.errors.INVALID_CHARACTERISTIC_ENTRY";

  private ProductService productService;
  private ProductCategoryService productCategoryService;
  private ProductCharacteristicService productCharacteristicService;

  private Product createProduct( ProductImportRecord productRecord, ProductCategory category )
  {
    Product product = new Product();
    product.setCode( productRecord.getSkuCode() );
    product.setDescription( productRecord.getProductDescription() );
    product.setName( productRecord.getProductName() );
    product.setProductCategory( category );

    buildProductCharacteristics( productRecord, product );

    return product;
  }

  /**
   * Verifies the specified import file records.
   * 
   * @param importFile the import file to import.
   * @param records
   */
  public void verifyImportFile( ImportFile importFile, List records )
  {
    // start out with the number of current records with errors
    int importRecordErrorCount = importFile.getImportRecordErrorCount();

    long counter = 0;
    logger.info( "processed record count: " + counter );
    for ( Iterator iterator = records.iterator(); iterator.hasNext(); )
    {
      counter++;
      ProductImportRecord record = (ProductImportRecord)iterator.next();
      boolean hasNoErrors = record.getImportRecordErrors().isEmpty();
      boolean foundError = false;

      // guard clause - only process this record if there are no errors with it
      if ( !record.getImportRecordErrors().isEmpty() )
      {
        continue;
      }

      Collection errors = validateProductImportRecord( record );

      if ( !errors.isEmpty() )
      {
        foundError = true;
        createAndAddImportRecordErrors( importFile, record, errors );
      }

      if ( foundError && hasNoErrors )
      {
        importRecordErrorCount++;
      }
    }

    importFile.setImportRecordErrorCount( importRecordErrorCount );
  }

  /**
   * Verifies the specified import file.
   * 
   * @param importFile the import file to import.
   * @param records
   * @param justForPaxRightNow
   * @throws ServiceErrorException   * 
   */
  public void verifyImportFile( ImportFile importFile, List records, boolean justForPaxRightNow ) throws ServiceErrorException
  {
    // First, make sure we are in the correct state
    if ( !importFile.getStatus().isVerifyInProcess() )
    {
      throw new ServiceErrorException( ServiceErrorMessageKeys.SYSTEM_ERRORS_INVALID_CALL );
    }

    verifyImportFile( importFile, records );
  }

  protected Collection validateProductImportRecord( ProductImportRecord productRecord )
  {
    Collection errors = new ArrayList();
    checkRequired( productRecord.getCategoryName(), PRODUCT_CATEGORY_NAME, errors );
    checkRequired( productRecord.getCategoryDescription(), PRODUCT_CATEGORY_DESCRIPTION, errors );
    checkRequired( productRecord.getProductDescription(), PRODUCT_DESCRIPTION, errors );
    checkRequired( productRecord.getProductName(), PRODUCT_NAME, errors );
    if ( !StringUtils.isEmpty( productRecord.getProductName() ) )
    {
      if ( !productService.isUniqueProductName( productRecord.getCategoryId(), productRecord.getProductId(), productRecord.getProductName() ) )
      {
        errors.add( new ServiceError( "product.errors.UNIQUE_CONSTRAINT" ) );
      }
    }
    if ( productRecord.getProductName() != null )
    {
      // make sure product doesn't already exist in this category and subcategory (if entered)
      Long catId = productRecord.getCategoryId();
      if ( productRecord.getSubCategoryId() != null )
      {
        catId = productRecord.getSubCategoryId();

        // make sure this subcategory is not already a parent category
        AssociationRequestCollection reqCollection = new AssociationRequestCollection();
        reqCollection.add( new ProductSubcategoryAssociation() );
        ProductCategory subCategory = productCategoryService.getProductCategoryById( productRecord.getSubCategoryId(), reqCollection );
        if ( !subCategory.getSubcategories().isEmpty() )
        {
          errors.add( new ServiceError( ServiceErrorMessageKeys.PRODUCT_SUBCATEGORY_ALREADY_EXISTS_AS_PARENT ) );
        }
      }
      if ( productRecord.getSubCategoryName() != null )
      {
        AssociationRequestCollection reqCollection = new AssociationRequestCollection();
        reqCollection.add( new ProductCategoryToParentCategoryAssociation() );
        if ( productRecord.getCategoryId() != null )
        {
          ProductCategory category = productCategoryService.getProductCategoryById( productRecord.getCategoryId(), reqCollection );
          if ( category.getParentProductCategory() != null )
          {
            errors.add( new ServiceError( ServiceErrorMessageKeys.PRODUCT_CATEGORY_HAS_PARENT ) );
          }
        }
      }
      if ( productService.getProductByNameAndCategoryId( productRecord.getProductName(), catId ) != null )
      {
        errors.add( new ServiceError( ServiceErrorMessageKeys.PRODUCT_EXISTS_IN_CATEGORY ) );
      }
    }

    checkAllRequiredOrAllEmpty( productRecord.getCharacteristicName1(), productRecord.getCharacteristicId1(), INVALID_CHARACTERISTIC_ENTRY, errors );
    checkAllRequiredOrAllEmpty( productRecord.getCharacteristicName2(), productRecord.getCharacteristicId2(), INVALID_CHARACTERISTIC_ENTRY, errors );
    checkAllRequiredOrAllEmpty( productRecord.getCharacteristicName3(), productRecord.getCharacteristicId3(), INVALID_CHARACTERISTIC_ENTRY, errors );
    checkAllRequiredOrAllEmpty( productRecord.getCharacteristicName4(), productRecord.getCharacteristicId4(), INVALID_CHARACTERISTIC_ENTRY, errors );
    checkAllRequiredOrAllEmpty( productRecord.getCharacteristicName5(), productRecord.getCharacteristicId5(), INVALID_CHARACTERISTIC_ENTRY, errors );

    return errors;
  }

  public void setProductCategoryService( ProductCategoryService productCategoryService )
  {
    this.productCategoryService = productCategoryService;
  }

  public void setProductService( ProductService productService )
  {
    this.productService = productService;
  }

  public void importImportFile( ImportFile importFile, List records ) throws ServiceErrorException
  {
    if ( !importFile.getStatus().isImportInProcess() )
    {
      throw new ServiceErrorException( ServiceErrorMessageKeys.SYSTEM_ERRORS_INVALID_CALL );
    }

    long counter = 0;
    logger.info( "processed record count: " + counter );

    Map categoriesAdded = new HashMap();
    for ( Iterator iterator = records.iterator(); iterator.hasNext(); )
    {
      counter++;
      ProductImportRecord productRecord = (ProductImportRecord)iterator.next();

      // guard clause - only process this record if there are no errors with it
      if ( !productRecord.getImportRecordErrors().isEmpty() )
      {
        continue;
      }

      ProductCategory parent = null;
      ProductCategory category = null;

      if ( productRecord.getSubCategoryId() != null )
      {
        category = productCategoryService.getProductCategoryById( productRecord.getSubCategoryId(), new AssociationRequestCollection() );
      }
      else if ( !StringUtils.isEmpty( productRecord.getSubCategoryName() ) )
      {
        category = (ProductCategory)categoriesAdded.get( productRecord.getSubCategoryName().toUpperCase() );
        if ( category == null )
        {
          category = new ProductCategory();
          category.setDescription( productRecord.getSubCategoryDescription() );
          category.setName( productRecord.getSubCategoryName() );
        }
      }
      if ( productRecord.getCategoryId() == null )
      {
        parent = (ProductCategory)categoriesAdded.get( productRecord.getCategoryName().toUpperCase() );
        if ( parent == null )
        {
          parent = new ProductCategory();
          parent.setDescription( productRecord.getCategoryDescription() );
          parent.setName( productRecord.getCategoryName() );
        }
      }
      else
      {
        parent = productCategoryService.getProductCategoryById( productRecord.getCategoryId(), new AssociationRequestCollection() );
      }
      try
      {
        if ( category != null )
        {
          if ( parent.getId() == null && categoriesAdded.get( parent.getName().toUpperCase() ) == null )
          {
            parent = productCategoryService.saveProductCategory( parent, null );
            categoriesAdded.put( parent.getName().toUpperCase(), parent );
          }
          category.setParentProductCategory( parent );
        }
        else
        {
          category = parent;
        }
        if ( category.getId() == null && categoriesAdded.get( category.getName().toUpperCase() ) == null )
        {
          category = productCategoryService.saveProductCategory( category, parent.getId() );
          categoriesAdded.put( category.getName().toUpperCase(), category );
        }
        Product product = createProduct( productRecord, category );

        productService.save( product.getProductCategory().getId(), product );
      }
      catch( UniqueConstraintViolationException e )
      {
        // we shouldn't ever get one of these, but log just in case
        logger.error( e.toString() );
      }
    } // for
  } // importImportFile

  /**
   * Builds up all of the productCharacteristics on the product
   * 
   * @param source
   * @param target
   */
  private void buildProductCharacteristics( ProductImportRecord source, Product target )
  {
    if ( source.getCharacteristicId1() != null )
    {
      ProductCharacteristicType characteristicType = (ProductCharacteristicType)productCharacteristicService.getCharacteristicById( source.getCharacteristicId1() );
      if ( characteristicType != null )
      {
        target.addProductCharacteristicByType( characteristicType );
      }
    }

    if ( source.getCharacteristicId2() != null )
    {
      ProductCharacteristicType characteristicType = (ProductCharacteristicType)productCharacteristicService.getCharacteristicById( source.getCharacteristicId2() );
      if ( characteristicType != null )
      {
        target.addProductCharacteristicByType( characteristicType );
      }
    }

    if ( source.getCharacteristicId3() != null )
    {
      ProductCharacteristicType characteristicType = (ProductCharacteristicType)productCharacteristicService.getCharacteristicById( source.getCharacteristicId3() );
      if ( characteristicType != null )
      {
        target.addProductCharacteristicByType( characteristicType );
      }
    }

    if ( source.getCharacteristicId4() != null )
    {
      ProductCharacteristicType characteristicType = (ProductCharacteristicType)productCharacteristicService.getCharacteristicById( source.getCharacteristicId4() );
      if ( characteristicType != null )
      {
        target.addProductCharacteristicByType( characteristicType );
      }
    }

    if ( source.getCharacteristicId5() != null )
    {
      ProductCharacteristicType characteristicType = (ProductCharacteristicType)productCharacteristicService.getCharacteristicById( source.getCharacteristicId5() );
      if ( characteristicType != null )
      {
        target.addProductCharacteristicByType( characteristicType );
      }
    }
  } // buildProductCharacteristics

  public void setProductCharacteristicService( ProductCharacteristicService productCharacteristicService )
  {
    this.productCharacteristicService = productCharacteristicService;
  }
}
