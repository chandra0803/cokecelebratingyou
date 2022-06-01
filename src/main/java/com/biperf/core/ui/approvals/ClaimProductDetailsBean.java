
package com.biperf.core.ui.approvals;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.biperf.core.domain.claim.ClaimProductCharacteristic;
import com.biperf.core.ui.approvals.ClaimProductDetailsBean.ProductItem.ApprovalOptionType;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ClaimProductDetailsBean
{
  private List<String> messages;
  private List<ProductItem> claimIndex = new ArrayList<ProductItem>();

  public List<String> getMessages()
  {
    return messages;
  }

  public List<ProductItem> getClaimIndex()
  {
    return claimIndex;
  }

  public void addClaimIndex( Long productId,
                             String productName,
                             String category,
                             String subcategory,
                             int quantity,
                             Set claimProductCharacteristics,
                             Long claimProductId,
                             String approvalStatusTypeCode,
                             List<ApprovalOptionType> approvalOptionTypesList )
  {
    claimIndex.add( new ProductItem( productId, productName, category, subcategory, quantity, claimProductCharacteristics, claimProductId, approvalStatusTypeCode, approvalOptionTypesList ) );
  }

  public static class ProductItem
  {
    private Long productId;
    private String productName;
    private String category;
    private String subcategory;
    private int quantity;
    private String characteristics;
    private Long claimProductId;
    private String approvalStatusTypeCode;
    @JsonProperty( "approvalstatusTypes" )
    private List<ApprovalOptionType> approvalOptionTypesList = new ArrayList<ApprovalOptionType>();

    public ProductItem( Long productId,
                        String productName,
                        String category,
                        String subcategory,
                        int quantity,
                        Set claimProductCharacteristics,
                        Long claimProductId,
                        String approvalStatusTypeCode,
                        List<ApprovalOptionType> approvalOptionTypesList )
    {
      this.productId = productId;
      this.productName = productName;
      this.category = category;
      this.subcategory = subcategory;
      this.quantity = quantity;
      this.claimProductId = claimProductId;
      this.approvalStatusTypeCode = approvalStatusTypeCode;
      this.approvalOptionTypesList = approvalOptionTypesList;

      StringBuffer buffer = new StringBuffer();
      for ( Iterator iterator = claimProductCharacteristics.iterator(); iterator.hasNext(); )
      {
        ClaimProductCharacteristic claimProductCharacteristic = (ClaimProductCharacteristic)iterator.next();
        buffer.append( claimProductCharacteristic.getProductCharacteristicType().getCharacteristicName() + ": " + claimProductCharacteristic.getValue() );
        if ( iterator.hasNext() )
        {
          buffer.append( "\n" );
        }
      }
      this.characteristics = buffer.toString();
    }

    public Long getProductId()
    {
      return productId;
    }

    public String getProductName()
    {
      return productName;
    }

    public String getCategory()
    {
      return category;
    }

    public String getSubcategory()
    {
      return subcategory;
    }

    public int getQuantity()
    {
      return quantity;
    }

    public String getCharacteristics()
    {
      return characteristics;
    }

    public Long getClaimProductId()
    {
      return claimProductId;
    }

    public void setClaimProductId( Long claimProductId )
    {
      this.claimProductId = claimProductId;
    }

    public String getApprovalStatusTypeCode()
    {
      return approvalStatusTypeCode;
    }

    public void setApprovalStatusTypeCode( String approvalStatusTypeCode )
    {
      this.approvalStatusTypeCode = approvalStatusTypeCode;
    }

    public List<ApprovalOptionType> getApprovalOptionTypesList()
    {
      return approvalOptionTypesList;
    }

    public void setApprovalOptionTypesList( List<ApprovalOptionType> approvalOptionTypesList )
    {
      this.approvalOptionTypesList = approvalOptionTypesList;
    }

    public static class ApprovalOptionType
    {
      private String code;
      private String name;

      public String getCode()
      {
        return code;
      }

      public void setCode( String code )
      {
        this.code = code;
      }

      public String getName()
      {
        return name;
      }

      public void setName( String name )
      {
        this.name = name;
      }
    }
  }

}
