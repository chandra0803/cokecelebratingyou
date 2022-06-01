/*
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/product/ProductForm.java,v $
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.ui.product;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.product.Product;
import com.biperf.core.ui.BaseForm;

/**
 * ProductForm.
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
 */
public class ProductForm extends BaseForm
{
  private Long id;
  private String name;
  private String code;
  private String description;
  private String productCategoryId;
  private String subProductCategoryId;
  private String selectedCharacteristicId;
  private String method;
  private Long version;
  private String createdBy;
  private long dateCreated;

  public void load( Product product )
  {

    this.setId( product.getId() );
    this.setName( product.getName() );
    this.setCode( product.getCode() );
    this.setDescription( product.getDescription() );
    if ( product.getProductCategory().getParentProductCategory() == null )
    {
      this.setProductCategoryId( product.getProductCategory().getId().toString() );
    }
    else
    {
      this.setProductCategoryId( product.getProductCategory().getParentProductCategory().getId().toString() );
      this.setSubProductCategoryId( product.getProductCategory().getId().toString() );
    }
    this.setVersion( product.getVersion() );
    this.setCreatedBy( product.getAuditCreateInfo().getCreatedBy().toString() );
    this.setDateCreated( product.getAuditCreateInfo().getDateCreated().getTime() );
  }

  public Product toDomainObject()
  {

    Product product = new Product();
    product.setId( this.getId() );
    product.setName( this.getName() );
    product.setCode( this.getCode() );
    product.setDescription( this.getDescription() );
    product.setVersion( this.getVersion() );

    if ( this.createdBy != null && !this.createdBy.equals( "" ) )
    {
      product.getAuditCreateInfo().setCreatedBy( Long.valueOf( this.createdBy ) );
    }
    product.getAuditCreateInfo().setDateCreated( new Timestamp( this.dateCreated ) );

    return product;
  }

  public String getDescription()
  {
    return description;
  }

  public void setDescription( String description )
  {
    this.description = description;
  }

  public Long getId()
  {
    return id;
  }

  public void setId( Long id )
  {
    this.id = id;
  }

  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  public String getProductCategoryId()
  {
    return productCategoryId;
  }

  public void setProductCategoryId( String productCategoryId )
  {
    this.productCategoryId = productCategoryId;
  }

  public String getSubProductCategoryId()
  {
    return subProductCategoryId;
  }

  public void setSubProductCategoryId( String subProductCategoryId )
  {
    this.subProductCategoryId = subProductCategoryId;
  }

  public String getCode()
  {
    return code;
  }

  public void setCode( String code )
  {
    this.code = code;
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public Long getVersion()
  {
    return version;
  }

  public void setVersion( Long version )
  {
    this.version = version;
  }

  public String getCreatedBy()
  {
    return createdBy;
  }

  public void setCreatedBy( String createdBy )
  {
    this.createdBy = createdBy;
  }

  public long getDateCreated()
  {
    return dateCreated;
  }

  public void setDateCreated( long dateCreated )
  {
    this.dateCreated = dateCreated;
  }

  public String getSelectedCharacteristicId()
  {
    return selectedCharacteristicId;
  }

  public void setSelectedCharacteristicId( String selectedCharacteristicId )
  {
    this.selectedCharacteristicId = selectedCharacteristicId;
  }

  /**
   * Validate the form before submitting Overridden from
   *
   * @see org.apache.struts.action.ActionForm#validate(org.apache.struts.action.ActionMapping,
   *      javax.servlet.http.HttpServletRequest)
   * @param mapping
   * @param request
   * @return ActionErrors
   */
  public ActionErrors validate( ActionMapping mapping, HttpServletRequest request )
  {
    ActionErrors errors = super.validate( mapping, request );

    return errors;
  }
}
