/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/promotion/PromotionStackRankPayoutFormBean.java,v $
 */

package com.biperf.core.ui.promotion;

import java.io.Serializable;

/**
 * PromotionPayoutFormBean.
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
 * <td>attada</td>
 * <td>March 08, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionStackRankPayoutFormBean implements Serializable
{
  private Long promoPayoutId;
  private String fromRank;
  private String toRank;
  private String payoutAmount;
  private String removePayout;
  private String createdBy = "";
  private long dateCreated;
  private Long version;
  private boolean includePayout;
  private String nodeTypeId;
  private String submittersRankNameId;

  public PromotionStackRankPayoutFormBean()
  {
    payoutAmount = "1";
  }

  public Long getVersion()
  {
    return version;
  }

  public void setVersion( Long version )
  {
    this.version = version;
  }

  public String getRemovePayout()
  {
    return removePayout;
  }

  public void setRemovePayout( String removePayout )
  {
    this.removePayout = removePayout;
  }

  public Long getPromoPayoutId()
  {
    return promoPayoutId;
  }

  public void setPromoPayoutId( Long promoPayoutId )
  {
    this.promoPayoutId = promoPayoutId;
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

  public boolean isIncludePayout()
  {
    return includePayout;
  }

  public void setIncludePayout( boolean includePayout )
  {
    this.includePayout = includePayout;
  }

  /**
   * Checks equality of the object parameter to this. Overridden from
   * 
   * @see com.biperf.core.domain.BaseDomain#equals(Object)
   * @param object
   * @return boolean
   */
  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }

    if ( ! ( object instanceof PromotionStackRankPayoutFormBean ) )
    {
      return false;
    }

    final PromotionStackRankPayoutFormBean promotionStackRankPayoutFormBean = (PromotionStackRankPayoutFormBean)object;

    if ( promotionStackRankPayoutFormBean.getFromRank() != null && !promotionStackRankPayoutFormBean.getFromRank().equals( this.getFromRank() ) )
    {
      return false;
    }

    return true;
  }

  /**
   * Define the hashCode from the id. Overridden from
   * 
   * @see com.biperf.core.domain.BaseDomain#hashCode()
   * @return int
   */
  public int hashCode()
  {
    int result;
    result = getFromRank() != null ? getFromRank().hashCode() : 0;
    // result = 29 * result + ( getToRank() != null ? getToRank().hashCode() : 0 );
    // result = 29 * result + ( getPayoutAmount() != null ? getPayoutAmount().hashCode() : 0 );
    return result;
  }

  public String getFromRank()
  {
    return fromRank;
  }

  public void setFromRank( String fromRank )
  {
    this.fromRank = fromRank;
  }

  public String getPayoutAmount()
  {
    return payoutAmount;
  }

  public void setPayoutAmount( String payoutAmount )
  {
    this.payoutAmount = payoutAmount;
  }

  public String getToRank()
  {
    return toRank;
  }

  public void setToRank( String toRank )
  {
    this.toRank = toRank;
  }

  public String getNodeTypeId()
  {
    return nodeTypeId;
  }

  public void setNodeTypeId( String nodeTypeId )
  {
    this.nodeTypeId = nodeTypeId;
  }

  public String getSubmittersRankNameId()
  {
    return submittersRankNameId;
  }

  public void setSubmittersRankNameId( String submittersRankNameId )
  {
    this.submittersRankNameId = submittersRankNameId;
  }

}
