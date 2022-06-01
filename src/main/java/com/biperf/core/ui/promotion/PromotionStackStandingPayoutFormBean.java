
package com.biperf.core.ui.promotion;

import java.io.Serializable;

@SuppressWarnings( "serial" )
public class PromotionStackStandingPayoutFormBean implements Serializable
{
  private Long promoPayoutId;
  private String fromStanding;
  private String toStanding;
  private String payoutAmount;
  private String removePayout;
  private String createdBy = "";
  private long dateCreated;
  private Long version;
  private boolean includePayout;
  private String nodeTypeId;

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

    if ( ! ( object instanceof PromotionStackStandingPayoutFormBean ) )
    {
      return false;
    }

    final PromotionStackStandingPayoutFormBean promotionStackStandingPayoutFormBean = (PromotionStackStandingPayoutFormBean)object;

    if ( promotionStackStandingPayoutFormBean.getFromStanding() != null && !promotionStackStandingPayoutFormBean.getFromStanding().equals( this.getFromStanding() ) )
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
    result = getFromStanding() != null ? getFromStanding().hashCode() : 0;
    // result = 29 * result + ( getToStanding() != null ? getToStanding().hashCode() : 0 );
    // result = 29 * result + ( getPayoutAmount() != null ? getPayoutAmount().hashCode() : 0 );
    return result;
  }

  public String getFromStanding()
  {
    return fromStanding;
  }

  public int getIntFromStanding()
  {
    return Integer.parseInt( fromStanding );
  }

  public void setFromStanding( String fromStanding )
  {
    this.fromStanding = fromStanding;
  }

  public String getPayoutAmount()
  {
    return payoutAmount;
  }

  public void setPayoutAmount( String payoutAmount )
  {
    this.payoutAmount = payoutAmount;
  }

  public String getToStanding()
  {
    return toStanding;
  }

  public void setToStanding( String toStanding )
  {
    this.toStanding = toStanding;
  }

  public String getNodeTypeId()
  {
    return nodeTypeId;
  }

  public void setNodeTypeId( String nodeTypeId )
  {
    this.nodeTypeId = nodeTypeId;
  }

}
