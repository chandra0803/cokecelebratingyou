/**
 * 
 */

package com.biperf.core.ui.claim;

import java.util.LinkedHashSet;
import java.util.Set;

import com.biperf.core.domain.claim.ClaimProductCharacteristic;

/**
 * @author poddutur
 *
 */
public class ClaimProductsViewBean
{
  String productCategoryName;
  String productSubCategory;
  String productName;
  Set<ClaimProductCharacteristic> claimProductCharacteristics = new LinkedHashSet<ClaimProductCharacteristic>();
  int quantity;
  String approvalStatusTypeName;
  String promotionApprovalOptionReasonTypeName;

  public String getProductCategoryName()
  {
    return productCategoryName;
  }

  public void setProductCategoryName( String productCategoryName )
  {
    this.productCategoryName = productCategoryName;
  }

  public String getProductSubCategory()
  {
    return productSubCategory;
  }

  public void setProductSubCategory( String productSubCategory )
  {
    this.productSubCategory = productSubCategory;
  }

  public String getProductName()
  {
    return productName;
  }

  public void setProductName( String productName )
  {
    this.productName = productName;
  }

  public Set<ClaimProductCharacteristic> getClaimProductCharacteristics()
  {
    return claimProductCharacteristics;
  }

  public void setClaimProductCharacteristics( Set<ClaimProductCharacteristic> claimProductCharacteristics )
  {
    this.claimProductCharacteristics = claimProductCharacteristics;
  }

  public int getQuantity()
  {
    return quantity;
  }

  public void setQuantity( int quantity )
  {
    this.quantity = quantity;
  }

  public String getApprovalStatusTypeName()
  {
    return approvalStatusTypeName;
  }

  public void setApprovalStatusTypeName( String approvalStatusTypeName )
  {
    this.approvalStatusTypeName = approvalStatusTypeName;
  }

  public String getPromotionApprovalOptionReasonTypeName()
  {
    return promotionApprovalOptionReasonTypeName;
  }

  public void setPromotionApprovalOptionReasonTypeName( String promotionApprovalOptionReasonTypeName )
  {
    this.promotionApprovalOptionReasonTypeName = promotionApprovalOptionReasonTypeName;
  }

}
