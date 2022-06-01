
package com.biperf.core.domain.activity;

public class SweepstakesAwardAmountActivity extends SweepstakesActivity
{
  private Long awardQuantity;

  public SweepstakesAwardAmountActivity()
  {
    // empty constructor
  }

  public SweepstakesAwardAmountActivity( String guid )
  {
    super( guid );
  }

  /**
   * @return value of awardQuantity property
   */
  public Long getAwardQuantity()
  {
    return awardQuantity;
  }

  /**
   * @param awardQuantity value for awardQuantity property
   */
  public void setAwardQuantity( Long awardQuantity )
  {
    this.awardQuantity = awardQuantity;
  }

}
