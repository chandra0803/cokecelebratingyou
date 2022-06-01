/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.process;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.service.promotion.PromotionService;

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
 * <td>wadzinsk</td>
 * <td>Nov 29, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ExpirePromotionsProcess extends BaseProcessImpl
{
  private static final Log log = LogFactory.getLog( MailingProcess.class );

  public static final String PROCESS_NAME = "Expire Promotions Process";
  public static final String BEAN_NAME = "expirePromotionsProcess";
  // public static final String PARAM_NAME = "mailingId";

  // // properties set from jobDataMap
  // private String mailingId;

  private PromotionService promotionService;

  // public void setMailingId( String mailingId )
  // {
  // this.mailingId = mailingId;
  // }

  public void onExecute()
  {

    // promotionService.process
    // mailingService.processMailing( new Long( Long.parseLong( mailingId ) ) );
    promotionService.expirePromotions();

  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.process.BaseProcessImpl#onExecute()
   * @param processInvocationId
   */
  public void onExecute( Long processInvocationId )
  {
    log.debug( "process " + processInvocationId + ":" + toString() );
    promotionService.expirePromotions();

    // mailingService.processMailing( new Long( Long.parseLong( mailingId ) ) );

  }

  /**
   * @param promotionService value for claimService property
   */
  public void setPromotionService( PromotionService promotionService )
  {
    this.promotionService = promotionService;
  }

}
