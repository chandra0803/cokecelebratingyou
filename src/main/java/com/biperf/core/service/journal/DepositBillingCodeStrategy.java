/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/journal/DepositBillingCodeStrategy.java,v $
 */

package com.biperf.core.service.journal;

import java.util.Set;

import com.biperf.core.domain.journal.Journal;
import com.biperf.core.domain.merchandise.MerchOrder;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.domain.promotion.RecognitionPromotion;

/**
 * DepositBillingCodeStrategy is designed to allow customization around the setting of the journal
 * billing codes as part of the deposit service(s). This strategy will set the billing codes sent
 * over to AwardsBanQ and will save them on the Journal after the deposit is complete.
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
 * <td>Brian Repko</td>
 * <td>Sep 1, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public interface DepositBillingCodeStrategy
{
  public void setJournalBillingCodes( Journal journal );

  public void setMerchOrderBillingCodes( MerchOrder merchOrder, RecognitionPromotion recPromo, Set activity ); // WIP#
                                                                                                               // 25130

  public void setMerchOrderBillingCodes( MerchOrder merchOrder, GoalQuestPromotion gqPromo, Set activity ); // WIP#
                                                                                                            // 25130

  public void setMerchOrderBillingCodesForSweepstakes( MerchOrder merchOrder, RecognitionPromotion recPromo ); // WIP#
                                                                                                               // 25130

}
