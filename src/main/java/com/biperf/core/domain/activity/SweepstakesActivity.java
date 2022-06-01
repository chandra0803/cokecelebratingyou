/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.domain.activity;

import com.biperf.core.domain.promotion.PromotionSweepstake;

/**
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
 * <td>zahler</td>
 * <td>Oct 19, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class SweepstakesActivity extends Activity
{
  /**
   * The drawing
   */
  private PromotionSweepstake promotionSweepstake;

  private boolean submitter;

  public SweepstakesActivity()
  {
    // empty constructor
  }

  public SweepstakesActivity( String guid )
  {
    super( guid );
  }

  /**
   * @return value of promotionSweepstake property
   */
  public PromotionSweepstake getPromotionSweepstake()
  {
    return promotionSweepstake;
  }

  /**
   * @param promotionSweepstake value for promotionSweepstake property
   */
  public void setPromotionSweepstake( PromotionSweepstake promotionSweepstake )
  {
    this.promotionSweepstake = promotionSweepstake;
  }

  /**
   * @return value of submitter property
   */
  public boolean isSubmitter()
  {
    return submitter;
  }

  /**
   * @param submitter value for submitter property
   */
  public void setSubmitter( boolean submitter )
  {
    this.submitter = submitter;
  }

}
