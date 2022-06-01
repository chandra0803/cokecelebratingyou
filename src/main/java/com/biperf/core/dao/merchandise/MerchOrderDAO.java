/*
 * (c) 2006 BI, Inc.  All rights reserved.
 */

package com.biperf.core.dao.merchandise;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.biperf.core.dao.DAO;
import com.biperf.core.dao.merchandise.hibernate.MerchOrderActivityQueryConstraint;
import com.biperf.core.domain.merchandise.MerchOrder;
import com.biperf.core.domain.user.PlateauRedemptionTracking;
import com.biperf.core.service.ProjectionCollection;
import com.biperf.core.value.MerchAwardReminderBean;

public interface MerchOrderDAO extends DAO
{

  public static final String BEAN_NAME = "merchOrderDAO";

  public MerchOrder getMerchOrderById( Long merchOrderId );

  // Alerts Performance Tuning
  public MerchOrder getMerchOrderByIdWithProjections( Long merchOrderId, ProjectionCollection collection );

  public MerchOrder saveMerchOrder( MerchOrder merchOrder );

  public PlateauRedemptionTracking savePlateauRedemptionTracking( PlateauRedemptionTracking plateauRedemptionTracking );

  /**
   * Returns the specified merchorders.
   */
  public abstract List getMerchOrderList( MerchOrderActivityQueryConstraint queryConstraint );

  public List<MerchAwardReminderBean> getMerchAwardReminders( Long participantId );

  public MerchOrder getMerchOrderByGiftCode( String giftCode );

  public MerchOrder getMerchOrderByReferenceNumber( String referenceNumber );

  public List<MerchAwardReminderBean> getPlateauAwardRemindersForNodes( Collection<Long> nodeIds, Date pastDueDate );

  public Long getNextBatchId();

  public List<Long> getGiftCodeFailures( Long promotionId ); // Bug 66870

  public Long getPromotionIdByMerchOrderId( Long merchOrderId );

  public List<Long> getMerchOrderIds();

  public abstract List getMerchOrdersList( Long merchOrderId );
  
  //Client customizations for wip #23129 starts
  public List getAllUnredeemedOrdersByPromotion( Long promoId, String mmyyyy );
  // Client customizations for wip #23129 ends

}
