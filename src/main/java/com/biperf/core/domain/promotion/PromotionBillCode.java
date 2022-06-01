
package com.biperf.core.domain.promotion;

public class PromotionBillCode extends BillCode implements Cloneable
{

  public static final boolean SWEEPS_BILLCODE = false;

  public PromotionBillCode()
  {
    super();
  }

  public PromotionBillCode( PromotionBillCode toCopy )
  {
    this.setBillCode( toCopy.getBillCode() );
    this.setTrackBillCodeBy( toCopy.getTrackBillCodeBy() );
    this.setCustomValue( toCopy.getCustomValue() );
    this.setSortOrder( toCopy.getSortOrder() );
  }

  public PromotionBillCode( Promotion promotion, Long sortOrder, String billCode, String customValue )
  {
    this.setPromotion( promotion );
    this.setSortOrder( sortOrder );
    this.setBillCode( billCode );
    this.setCustomValue( customValue );
  }

  public PromotionBillCode( Promotion promotion, Long sortOrder, String billCode, String customValue, String trackBillCodeBy )
  {
    this.setPromotion( promotion );
    this.setSortOrder( sortOrder );
    this.setBillCode( billCode );
    this.setCustomValue( customValue );
    this.setTrackBillCodeBy( trackBillCodeBy );
  }

  public PromotionBillCode deepCopy()
  {
    PromotionBillCode clone = new PromotionBillCode();
    clone.setSortOrder( this.getSortOrder() );
    clone.setBillCode( this.getBillCode() );
    clone.setCustomValue( this.getCustomValue() );
    clone.setTrackBillCodeBy( this.getTrackBillCodeBy() );
    return clone;
  }

  public Object clone() throws CloneNotSupportedException
  {

    PromotionBillCode clonedNonSweepStakePromotionBillCode = (PromotionBillCode)super.clone();

    clonedNonSweepStakePromotionBillCode.setPromotion( null );
    clonedNonSweepStakePromotionBillCode.resetBaseDomain();

    return clonedNonSweepStakePromotionBillCode;

  }

  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }

    if ( ! ( object instanceof PromotionBillCode ) )
    {
      return false;
    }

    return super.equals( object );
  }

  public int hashCode()
  {
    return super.hashCode();
  }
}
