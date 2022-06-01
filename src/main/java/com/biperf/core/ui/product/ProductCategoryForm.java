/*
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/product/ProductCategoryForm.java,v $
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.ui.product;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.domain.product.ProductCategory;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseActionForm;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * ProductCategoryForm.
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
public class ProductCategoryForm extends BaseActionForm
{
  private Long id;
  private String name;
  private String description;
  private Long parentCategoryId;
  private String parentCategoryName;

  private Long[] deleteProductCategoryIds;
  private String method;

  private Long productCategoryId;

  public Long[] getDeleteProductCategoryIds()
  {
    return deleteProductCategoryIds;
  }

  public void setDeleteProductCategoryIds( Long[] deleteProductCategoryIds )
  {
    this.deleteProductCategoryIds = deleteProductCategoryIds;
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

  public void loadDomainObject( ProductCategory productCategory )
  {
    setId( productCategory.getId() );
    setName( productCategory.getName() );
    setDescription( productCategory.getDescription() );
  }

  public ProductCategory toDomainObject()
  {
    ProductCategory productCategory = new ProductCategory();
    populateDomainObject( productCategory );
    return productCategory;
  }

  public void populateDomainObject( ProductCategory productCategory )
  {
    productCategory.setId( id );
    productCategory.setName( name );
    productCategory.setDescription( description );
  }

  public Long getParentCategoryId()
  {
    return parentCategoryId;
  }

  public void setParentCategoryId( Long parentCategoryId )
  {
    this.parentCategoryId = parentCategoryId;
  }

  public String getParentCategoryName()
  {
    return parentCategoryName;
  }

  public void setParentCategoryName( String parentCategoryName )
  {
    this.parentCategoryName = parentCategoryName;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public String getMethod()
  {
    return this.method;
  }

  public Long getProductCategoryId()
  {
    return productCategoryId;
  }

  public void setProductCategoryId( Long productCategoryId )
  {
    this.productCategoryId = productCategoryId;
  }

  /**
   * Validate the properties that have been set from this HTTP request, and return an
   * <code>ActionErrors</code> object that encapsulates any validation errors that have been
   * found. If no errors are found, return <code>null</code> or an <code>ActionErrors</code>
   * object with no recorded error messages.
   * 
   * @param mapping the mapping used to select this instance.
   * @param request the servlet request we are processing.
   * @return <code>ActionErrors</code> object that encapsulates any validation errors.
   */
  public ActionErrors validate( ActionMapping mapping, HttpServletRequest request )
  {
    ActionErrors actionErrors = super.validate( mapping, request );
    if ( actionErrors == null )
    {
      actionErrors = new ActionErrors();
    }

    if ( mapping.getPath().equalsIgnoreCase( "/productCategoryMaintain" ) )
    {
      if ( StringUtils.isEmpty( name ) )
      {
        actionErrors.add( ActionMessages.GLOBAL_MESSAGE,
                          new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "product.productcategory.details.NAME" ) ) );
      }
    }
    else
    {
      if ( !StringUtils.isEmpty( parentCategoryName ) && StringUtils.isEmpty( name ) )
      {
        actionErrors.add( ActionMessages.GLOBAL_MESSAGE,
                          new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "product.productsubcategory.details.SUBCAT_NAME" ) ) );
      }
    }

    return actionErrors;
  }
}
