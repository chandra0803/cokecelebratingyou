
package com.biperf.core.value.ssi;

/**
 * 
 * SSIContestAdminDetailTDResultActivityView.
 * 
 * @author chowdhur
 * @since Jan 13, 2015
 */
public class SSIContestSummaryTDResultActivityBean
{
  private Long id;
  private String activity; // Double
  private String payout; // Long
  private String payoutQuantity; // Long

  public SSIContestSummaryTDResultActivityBean()
  {

  }

  /**
   * @param activity
   * @param payout
   */
  public SSIContestSummaryTDResultActivityBean( Long id, String activity, String payout )
  {
    super();
    this.id = id;
    this.activity = activity;
    this.payout = payout;
  }

  /**
   * @param activity
   * @param payoutQuantity
   * @param payout
   */
  public SSIContestSummaryTDResultActivityBean( Long id, String activity, String payout, String payoutQuantity )
  {
    super();
    this.id = id;
    this.activity = activity;
    this.payout = payout;
    this.payoutQuantity = payoutQuantity;
  }

  public String getActivity()
  {
    return activity;
  }

  public void setActivity( String activity )
  {
    this.activity = activity;
  }

  public String getPayoutQuantity()
  {
    return payoutQuantity;
  }

  public void setPayoutQuantity( String payoutQuantity )
  {
    this.payoutQuantity = payoutQuantity;
  }

  public String getPayout()
  {
    return payout;
  }

  public void setPayout( String payout )
  {
    this.payout = payout;
  }

  public Long getId()
  {
    return id;
  }

  public void setId( Long id )
  {
    this.id = id;
  }

}
