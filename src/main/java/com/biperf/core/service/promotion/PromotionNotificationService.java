/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/service/promotion/PromotionNotificationService.java,v $
 */

package com.biperf.core.service.promotion;

import java.util.List;

import com.biperf.core.domain.promotion.ClaimFormNotificationType;
import com.biperf.core.domain.promotion.PromotionNotification;
import com.biperf.core.domain.promotion.PromotionNotificationType;
import com.biperf.core.service.SAO;

/**
 * PromotionNotificationService.
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
 * <td>sedey</td>
 * <td>Aug 25, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public interface PromotionNotificationService extends SAO
{
  /** * BEAN_NAME is the reference for spring ** */
  public static final String BEAN_NAME = "promotionNotificationService";

  /**
   * Get the promotionTypeNotification by the id param.
   * 
   * @param promotionNotificationId
   * @return PromotionNotificationType
   */
  public PromotionNotificationType getPromotionNotificationTypeById( Long promotionNotificationId );

  /**
   * Get the claimTypeNotification by the id param.
   * 
   * @param promotionNotificationId
   * @return ClaimFormNotificationType
   */
  public ClaimFormNotificationType getClaimFormNotificationTypeById( Long promotionNotificationId );

  /**
   * Get a List of promotionTypeNotifications by the promotionId param.
   * 
   * @param promotionId
   * @return List
   */
  public List getPromotionTypeNotificationsByPromotionId( Long promotionId );

  /**
   * Get a List of claimFormTypeNotifications by the promotionId param.
   * 
   * @param promotionId
   * @return List
   */
  public List getClaimFormTypeNotificationsByPromotionId( Long promotionId );

  /**
   * Save the promotionNotification.
   * 
   * @param promotionId
   * @param promotionNotification
   * @return PromotionNotification
   */
  public PromotionNotification savePromotionNotification( Long promotionId, PromotionNotification promotionNotification );

  /**
   * Save a list of promotionNotifications.
   * 
   * @param promotionId
   * @param promotionNotifications
   */
  public void savePromotionNotifications( Long promotionId, List promotionNotifications );

  /**
   * Get a PromotionNotification instance given a ClaimFormSteEmailNotification id.
   * 
   * @param promotionId
   * @param claimFormStepEmailNotificationId
   * @return PromotionNotification
   */
  public PromotionNotification getClaimPromotionNotificationByClaimFormStepEmailNotificationId( Long promotionId, Long claimFormStepEmailNotificationId );

}
