
package com.biperf.core.value;

import java.io.Serializable;

import com.biperf.core.domain.promotion.Promotion;

/**
 * 
 * PromotionSweepstakesListValueBean.
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
 * <td>kunaseka</td>
 * <td>Aug 30, 2010</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 *
 */
public class PromotionSweepstakesListValueBean implements Serializable
{
  private Promotion promotion;
  private boolean hasSweepstakesToProcess;
  private boolean hasSweepstakesHistory;

  public Promotion getPromotion()
  {
    return promotion;
  }

  public void setPromotion( Promotion promotion )
  {
    this.promotion = promotion;
  }

  public boolean isHasSweepstakesToProcess()
  {
    return hasSweepstakesToProcess;
  }

  public void setHasSweepstakesToProcess( boolean hasSweepstakesToProcess )
  {
    this.hasSweepstakesToProcess = hasSweepstakesToProcess;
  }

  public boolean isHasSweepstakesHistory()
  {
    return hasSweepstakesHistory;
  }

  public void setHasSweepstakesHistory( boolean hasSweepstakesHistory )
  {
    this.hasSweepstakesHistory = hasSweepstakesHistory;
  }

}
