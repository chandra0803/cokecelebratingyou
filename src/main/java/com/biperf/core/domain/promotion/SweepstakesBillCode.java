
package com.biperf.core.domain.promotion;

public class SweepstakesBillCode extends BillCode implements Cloneable
{

  public static final boolean SWEEPS_BILLCODE = true;

  public SweepstakesBillCode()
  {
    super();
  }

  public SweepstakesBillCode( SweepstakesBillCode toCopy )
  {
    this.setBillCode( toCopy.getBillCode() );
    this.setTrackBillCodeBy( toCopy.getTrackBillCodeBy() );
    this.setCustomValue( toCopy.getCustomValue() );
    this.setSortOrder( toCopy.getSortOrder() );
  }

  public SweepstakesBillCode( Promotion promotion, Long sortOrder, String billCode, String customValue )
  {
    this.setPromotion( promotion );
    this.setSortOrder( sortOrder );
    this.setBillCode( billCode );
    this.setCustomValue( customValue );
  }

  public SweepstakesBillCode( Promotion promotion, Long sortOrder, String billCode, String customValue, String trackBillCodeBy )
  {
    this.setPromotion( promotion );
    this.setSortOrder( sortOrder );
    this.setBillCode( billCode );
    this.setCustomValue( customValue );
    this.setTrackBillCodeBy( trackBillCodeBy );
  }

  public SweepstakesBillCode deepCopy()
  {
    SweepstakesBillCode clone = new SweepstakesBillCode();
    clone.setSortOrder( this.getSortOrder() );
    clone.setBillCode( this.getBillCode() );
    clone.setCustomValue( this.getCustomValue() );
    clone.setTrackBillCodeBy( this.getTrackBillCodeBy() );
    return clone;
  }

  public Object clone() throws CloneNotSupportedException
  {

    SweepstakesBillCode clonedsweepStakePromotionBillCode = (SweepstakesBillCode)super.clone();

    clonedsweepStakePromotionBillCode.setPromotion( null );
    clonedsweepStakePromotionBillCode.resetBaseDomain();

    return clonedsweepStakePromotionBillCode;

  }

  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }

    if ( ! ( object instanceof SweepstakesBillCode ) )
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
