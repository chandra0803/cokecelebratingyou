/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/service/integration/impl/SupplierServiceImpl.java,v $
 */

package com.biperf.core.service.integration.impl;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.dao.integration.SupplierDAO;
import com.biperf.core.domain.supplier.Supplier;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.exception.UniqueConstraintViolationException;
import com.biperf.core.service.cms.CMAssetService;
import com.biperf.core.service.cms.CMDataElement;
import com.biperf.core.service.integration.SupplierService;

/**
 * SupplierServiceImpl.
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
 * <td>Jun 1, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class SupplierServiceImpl implements SupplierService
{

  private CMAssetService cmAssetService;

  /** SupplierDAO * */
  private SupplierDAO supplierDAO;

  /**
   * Set the SupplierDAO through IoC
   * 
   * @param supplierDAO
   */
  public void setSupplierDAO( SupplierDAO supplierDAO )
  {
    this.supplierDAO = supplierDAO;
  }

  public void setCmAssetService( CMAssetService cmAssetService )
  {
    this.cmAssetService = cmAssetService;
  }

  public Long getNumberOfAssociationsForSupplier( Long supplierCode )
  {
    return supplierDAO.getNumberOfAssociationsForSupplier( supplierCode );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.integration.SupplierService#saveSupplier(com.biperf.core.domain.supplier.Supplier)
   * @param supplierToSave
   * @return Supplier
   * @throws UniqueConstraintViolationException
   */
  public Supplier saveSupplier( Supplier supplierToSave ) throws UniqueConstraintViolationException, ServiceErrorException
  {
    // Check to see if the supplier already exists in the database.
    Supplier dbSupplier = this.supplierDAO.getSupplierByName( supplierToSave.getSupplierName() );
    if ( dbSupplier != null )
    {
      // if we found a record in the database with the given supplierName, and our supplierToSave ID
      // is
      // null,
      // we are trying to insert a duplicate record.
      if ( supplierToSave.getId() == null )
      {
        throw new UniqueConstraintViolationException();
      }

      // if we found a record in the database with the given supplierName, but the ids are not
      // equal,
      // we are trying to update to a supplierName that already exists so throw a
      // UniqueConstraintViolationException
      if ( dbSupplier.getId().compareTo( supplierToSave.getId() ) != 0 )
      {
        throw new UniqueConstraintViolationException();
      }
    }

    supplierToSave = saveSupplierInCM( supplierToSave );

    return this.supplierDAO.saveSupplier( supplierToSave );
  }

  private Supplier saveSupplierInCM( Supplier supplier ) throws ServiceErrorException
  {
    if ( supplier.getId() == null )
    {
      supplier.setCmAssetCode( cmAssetService.getUniqueAssetCode( Supplier.CM_SUPPLIER_ASSET_PREFIX ) );
    }
    supplier.setImageCmKey( Supplier.CM_SUPPLIER_IMAGE_KEY );
    supplier.setTitleCmKey( Supplier.CM_SUPPLIER_NAME_KEY );
    supplier.setDescCmKey( Supplier.CM_SUPPLIER_DESCRIPTION_KEY );
    supplier.setButtonCmKey( Supplier.CM_SUPPLIER_BUTTON_KEY );

    CMDataElement cmDataElementImageName = new CMDataElement( Supplier.CM_SUPPLIER_IMAGE_KEY_DESC, Supplier.CM_SUPPLIER_IMAGE_KEY, supplier.getImageName(), false );

    CMDataElement cmDataElementTitle = new CMDataElement( Supplier.CM_SUPPLIER_NAME_KEY_DESC, Supplier.CM_SUPPLIER_NAME_KEY, supplier.getSupplierName(), false );

    CMDataElement cmDataElementDescription = new CMDataElement( Supplier.CM_SUPPLIER_DESCRIPTION_DESC, Supplier.CM_SUPPLIER_DESCRIPTION_KEY, supplier.getDescription(), false );

    CMDataElement cmDataElementButton = new CMDataElement( Supplier.CM_SUPPLIER_BUTTON_KEY_DESC, Supplier.CM_SUPPLIER_BUTTON_KEY, supplier.getButtonName(), false );

    List elementList = new ArrayList();
    elementList.add( cmDataElementImageName );
    elementList.add( cmDataElementTitle );
    elementList.add( cmDataElementDescription );
    elementList.add( cmDataElementButton );

    cmAssetService.createOrUpdateAsset( Supplier.CM_SUPPLIER_SECTION, Supplier.CM_SUPPLIER_ASSET_TYPE, Supplier.CM_SUPPLIER_NAME_KEY_DESC, supplier.getCmAssetCode(), elementList );

    return supplier;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.integration.SupplierService#getSupplierById(java.lang.Long)
   * @param id
   * @return Supplier
   */
  public Supplier getSupplierById( Long id )
  {
    return this.supplierDAO.getSupplierById( id );
  }
  
  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.integration.SupplierService#getSupplierByName(java.lang.String)
   * @param supplierName
   * @return Supplier
   */
  public Supplier getSupplierByName( String supplierName )
  {
    return this.supplierDAO.getSupplierByName( supplierName );
  }


  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.integration.SupplierService#getAll()
   * @return List
   */
  public List getAll()
  {
    return this.supplierDAO.getAll();
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.integration.SupplierService#getAll()
   * @return List
   */
  public List getAllActive()
  {
    return this.supplierDAO.getAllActive();
  }

}
