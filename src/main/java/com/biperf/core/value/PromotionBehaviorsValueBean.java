
package com.biperf.core.value;

public class PromotionBehaviorsValueBean
{

  private String checked;
  private String promoNominationBehaviorTypeName;
  private String promoNominationBehaviorTypeCode;
  private String behaviorOrder;

  public PromotionBehaviorsValueBean()
  {
  }

  public PromotionBehaviorsValueBean( String promoNominationBehaviorTypeName, String promoNominationBehaviorTypeCode, String behaviorOrder )
  {
    this.promoNominationBehaviorTypeName = promoNominationBehaviorTypeName;
    this.promoNominationBehaviorTypeCode = promoNominationBehaviorTypeCode;
    this.behaviorOrder = behaviorOrder;
  }

  public PromotionBehaviorsValueBean( String checked, String promoNominationBehaviorTypeName, String promoNominationBehaviorTypeCode, String behaviorOrder )
  {
    this.checked = checked;
    this.promoNominationBehaviorTypeName = promoNominationBehaviorTypeName;
    this.promoNominationBehaviorTypeCode = promoNominationBehaviorTypeCode;
    this.behaviorOrder = behaviorOrder;
  }

  public String getBehaviorOrder()
  {
    return behaviorOrder;
  }

  public void setBehaviorOrder( String behaviorOrder )
  {
    this.behaviorOrder = behaviorOrder;
  }

  public String getChecked()
  {
    return checked;
  }

  public void setChecked( String checked )
  {
    this.checked = checked;
  }

  public String getPromoNominationBehaviorTypeName()
  {
    return promoNominationBehaviorTypeName;
  }

  public void setPromoNominationBehaviorTypeName( String promoNominationBehaviorTypeName )
  {
    this.promoNominationBehaviorTypeName = promoNominationBehaviorTypeName;
  }

  public String getPromoNominationBehaviorTypeCode()
  {
    return promoNominationBehaviorTypeCode;
  }

  public void setPromoNominationBehaviorTypeCode( String promoNominationBehaviorTypeCode )
  {
    this.promoNominationBehaviorTypeCode = promoNominationBehaviorTypeCode;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( promoNominationBehaviorTypeName == null ? 0 : promoNominationBehaviorTypeName.hashCode() );
    result = prime * result + ( promoNominationBehaviorTypeCode == null ? 0 : promoNominationBehaviorTypeCode.hashCode() );
    return result;
  }

  public boolean equals( Object o )
  {
    if ( this == o )
    {
      return true;
    }

    if ( ! ( o instanceof PromotionBehaviorsValueBean ) )
    {
      return false;
    }

    final PromotionBehaviorsValueBean behavior = (PromotionBehaviorsValueBean)o;

    if ( behavior.getPromoNominationBehaviorTypeName() == null || !promoNominationBehaviorTypeName.equals( behavior.getPromoNominationBehaviorTypeName() ) )
    {
      return false;
    }

    if ( behavior.getPromoNominationBehaviorTypeCode() == null || !promoNominationBehaviorTypeCode.equals( behavior.getPromoNominationBehaviorTypeCode() ) )
    {
      return false;
    }

    return true;
  }
}
