/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/claim/ClaimProductForm.java,v $
 */

package com.biperf.core.ui.claim;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.biperf.core.domain.claim.ClaimProduct;
import com.biperf.core.domain.claim.ClaimProductCharacteristic;
import com.biperf.core.domain.enums.ApprovalStatusType;
import com.biperf.core.domain.product.Product;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseForm;
import com.biperf.core.ui.utils.CharacteristicUtils;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.value.CharacteristicValueBean;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * ClaimProductForm.
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
 * <td>tennant</td>
 * <td>Jun 30, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */

public class ClaimProductForm extends BaseForm
{
  Long claimProductId;
  Product product;
  Long productId; // This will be set to product.id
  String quantity = "";
  boolean showQty;
  String serialId;
  String approvalStatusTypeCode;
  List claimProductCharacteristicValueList = new ArrayList();

  /**
   * index used to determine which claimProduct out of the list is needed to update
   */
  Integer claimProductFormIndex;

  private long version;
  private long dateCreated;
  private String createdBy;
  private String method;

  /**
   * Validate the properties that have been set from this HTTP request, and return an
   * <code>ActionErrors</code> object that encapsulates any validation errors that have been
   * found. If no errors are found, return <code>null</code> or an <code>ActionErrors</code>
   * object with no recorded error messages. Validation is being done inside the form because there
   * are dynamic fields that may or may not need validating.
   * 
   * @param actionMapping the ActionMapping used to select this instance
   * @param request the HTTP request that is being processed
   * @return org.apache.struts.action.ActionErrors - collection of errors found on validation of
   *         data in form
   */

  public ActionErrors validate( ActionMapping actionMapping, HttpServletRequest request )
  {
    ActionErrors actionErrors = super.validate( actionMapping, request );

    if ( actionErrors == null )
    {
      actionErrors = new ActionErrors();
    }
    if ( quantity.equals( "0" ) )
    {
      actionErrors.add( "primaryProductId", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "claims.product.details.QTY_SOLD" ) ) );
    }
    else
    {
      try
      {
        long value = Long.parseLong( quantity );
        if ( value < 0 )
        {
          actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "claims.submission.errors.QTY_SOLD_NEGATIVE" ) );
        }
      }
      catch( NumberFormatException e )
      {

      }
    }
    if ( getClaimProductCharacteristicValueListCount() > 0 )
    {
      CharacteristicUtils.validateCharacteristicValueList( claimProductCharacteristicValueList, actionErrors );
    }

    return actionErrors;
  } // end validate

  /**
   * clears the contents of the form
   */
  public void clearForm()
  {
    product = new Product();
    productId = null;
    quantity = "";
    showQty = false;
    claimProductId = null;
    serialId = "";
    claimProductCharacteristicValueList = new ArrayList();
    claimProductFormIndex = null;
    version = 0;
    dateCreated = 0;
    createdBy = null;
    method = null;
    approvalStatusTypeCode = null;

  }

  /**
   * Load this form with the ClaimProduct data.
   * 
   * @param claimProduct
   */
  public void load( ClaimProduct claimProduct )
  {
    this.product = claimProduct.getProduct();
    this.productId = claimProduct.getProduct().getId();
    this.quantity = String.valueOf( claimProduct.getQuantity() );

    this.claimProductId = claimProduct.getId();
    this.serialId = claimProduct.getSerialId();

    approvalStatusTypeCode = claimProduct.getApprovalStatusType() == null ? null : claimProduct.getApprovalStatusType().getCode();

    // TODO - load claimProductCharValueList
    // This is the set of the claimProductCharacteristics that we have values for
    Set claimProductCharacteristics = claimProduct.getClaimProductCharacteristics();

    // List of all Product chars
    Set allProductCharacteristics = new LinkedHashSet();
    allProductCharacteristics = product.getProductCharacteristicTypes();
    List availableProductChars = new ArrayList();
    availableProductChars.addAll( allProductCharacteristics );

    // load the char into a list
    List characteristicList = CharacteristicUtils.getProductCharacteristicValueList( claimProductCharacteristics, availableProductChars );

    this.setClaimProductCharacteristicValueList( characteristicList );

    this.createdBy = claimProduct.getAuditCreateInfo().getCreatedBy().toString();
    if ( claimProduct.getAuditCreateInfo().getDateCreated() != null )
    {
      this.dateCreated = claimProduct.getAuditCreateInfo().getDateCreated().getTime();
    }
    this.version = claimProduct.getVersion().longValue();
  }

  /**
   * Create a detached ClaimElement from this form.
   * 
   * @return ClaimElement
   */
  public ClaimProduct toDomainObject()
  {

    ClaimProduct claimProduct = new ClaimProduct();
    if ( claimProductId != null && claimProductId.longValue() != 0 )
    {
      claimProduct.setId( claimProductId );
      claimProduct.setVersion( new Long( version ) );
      if ( createdBy != null && dateCreated != 0 )
      {
        claimProduct.getAuditCreateInfo().setCreatedBy( Long.valueOf( this.createdBy ) );
        claimProduct.getAuditCreateInfo().setDateCreated( new Timestamp( this.dateCreated ) );
      }
      claimProduct.setApprovalStatusType( ApprovalStatusType.lookup( approvalStatusTypeCode ) );
    }
    else
    {
      // new record, set initial approval status to pending.
      claimProduct.setApprovalStatusType( ApprovalStatusType.lookup( ApprovalStatusType.PENDING ) );
    }

    claimProduct.setProduct( this.product );
    claimProduct.setQuantity( Integer.valueOf( this.quantity ).intValue() );
    claimProduct.setSerialId( this.serialId );

    Collection claimProductCharacteristics = CharacteristicUtils.toListOfClaimProductCharacteristicDomainObjects( claimProductCharacteristicValueList );
    Iterator claimProductCharIterator = claimProductCharacteristics.iterator();
    while ( claimProductCharIterator.hasNext() )
    {
      ClaimProductCharacteristic claimProductCharacteristic = (ClaimProductCharacteristic)claimProductCharIterator.next();
      claimProduct.addClaimProductCharacteristics( claimProductCharacteristic );
    }

    return claimProduct;
  }

  /**
   * Overridden from
   * 
   * @see org.apache.struts.action.ActionForm#reset(org.apache.struts.action.ActionMapping,
   *      javax.servlet.http.HttpServletRequest)
   * @param mapping
   * @param request
   */
  public void reset( ActionMapping mapping, HttpServletRequest request )
  {
    // Reset needs to be used to populate an empty list of ClaimProductCharacteristicFormBeans.
    // If this is not done, the form wont initialize properly.
    claimProductCharacteristicValueList = CharacteristicUtils.getEmptyValueList( RequestUtils.getOptionalParamInt( request, "claimProductCharacteristicValueListCount" ) );
  } // end reset

  public List getClaimProductCharacteristicValueList()
  {
    return claimProductCharacteristicValueList;
  }

  public void setClaimProductCharacteristicValueList( List claimProductCharacteristicValueList )
  {
    this.claimProductCharacteristicValueList = claimProductCharacteristicValueList;
  }

  public int getClaimProductCharacteristicValueListCount()
  {
    if ( claimProductCharacteristicValueList != null )
    {
      return claimProductCharacteristicValueList.size();
    }
    return 0;
  } // end getClaimProductCharacteristicValueListCount

  /**
   * Accessor for the value list
   * 
   * @param index
   * @return Single instance of CharacteristicFormBean from the value list
   */
  public CharacteristicValueBean getProductCharacteristicValueInfo( int index )
  {
    try
    {
      return (CharacteristicValueBean)claimProductCharacteristicValueList.get( index );
    }
    catch( Exception anException )
    {
      return null;
    }
  } // end getClaimProductCharacteristicValueInfo

  public Long getClaimProductId()
  {
    return claimProductId;
  }

  public void setClaimProductId( Long claimProductId )
  {
    this.claimProductId = claimProductId;
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

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public Product getProduct()
  {
    return product;
  }

  public void setProduct( Product product )
  {
    this.product = product;
  }

  public Long getProductId()
  {
    return productId;
  }

  public void setProductId( Long productId )
  {
    this.productId = productId;
  }

  public String getQuantity()
  {
    return quantity;
  }

  public void setQuantity( String quantity )
  {
    this.quantity = quantity;
  }

  public long getVersion()
  {
    return version;
  }

  public void setVersion( long version )
  {
    this.version = version;
  }

  public Integer getClaimProductFormIndex()
  {
    return claimProductFormIndex;
  }

  public void setClaimProductFormIndex( Integer claimProductFormIndex )
  {
    this.claimProductFormIndex = claimProductFormIndex;
  }

  public boolean isShowQty()
  {
    return showQty;
  }

  public void setShowQty( boolean showQty )
  {
    this.showQty = showQty;
  }

  public String getSerialId()
  {
    return serialId;
  }

  public void setSerialId( String serialId )
  {
    this.serialId = serialId;
  }

  /**
   * @return value of editable property
   */
  public boolean isEditable()
  {
    boolean editable = true;

    if ( approvalStatusTypeCode == null || ApprovalStatusType.DENIED.equals( approvalStatusTypeCode ) || ApprovalStatusType.APPROVED.equals( approvalStatusTypeCode ) )
    {
      editable = false;
    }

    return editable;
  }

  /**
   * @return value of approvalStatusTypeCode property
   */
  public String getApprovalStatusTypeCode()
  {
    return approvalStatusTypeCode;
  }

  /**
   * @param approvalStatusTypeCode value for approvalStatusTypeCode property
   */
  public void setApprovalStatusTypeCode( String approvalStatusTypeCode )
  {
    this.approvalStatusTypeCode = approvalStatusTypeCode;
  }

} // end class ClaimProductForm
