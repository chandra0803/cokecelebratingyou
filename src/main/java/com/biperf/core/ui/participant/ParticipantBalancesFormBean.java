/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/participant/ParticipantBalancesFormBean.java,v $
 */

package com.biperf.core.ui.participant;

import java.io.Serializable;

/**
 * ParticipantBalancesFormBean.
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
 * <td>Nov 16, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ParticipantBalancesFormBean implements Serializable
{
  private String awardType;
  private Long totalEarned;
  private Long currentBalance;

  public String getAwardType()
  {
    return awardType;
  }

  public void setAwardType( String awardType )
  {
    this.awardType = awardType;
  }

  public Long getCurrentBalance()
  {
    return currentBalance;
  }

  public void setCurrentBalance( Long currentBalance )
  {
    this.currentBalance = currentBalance;
  }

  public Long getTotalEarned()
  {
    return totalEarned;
  }

  public void setTotalEarned( Long totalEarned )
  {
    this.totalEarned = totalEarned;
  }
}
