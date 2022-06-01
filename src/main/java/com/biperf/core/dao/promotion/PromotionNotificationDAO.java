/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/dao/promotion/PromotionNotificationDAO.java,v $
 */

package com.biperf.core.dao.promotion;

import java.util.List;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.promotion.PromotionNotification;

/**
 * PromotionNotificationDAO.
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
 * <td>Aug 24, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public interface PromotionNotificationDAO extends DAO
{

  /** name of bean in factory * */
  public static final String BEAN_NAME = "promotionNotificationDAO";

  /**
   * Get a PromotionNotification containing promotionEmailNotificationType subclass by Id.
   * 
   * @param id
   * @return PromotionNotification
   */
  public PromotionNotification getPromotionEmailNotificationTypeById( Long id );

  /**
   * Get a PromotionNotification containing ClaimFormStepEmailNotificatonType subclass by Id.
   * 
   * @param id
   * @return PromotionNotification
   */
  public PromotionNotification getClaimFormEmailNotificationTypeById( Long id );

  /**
   * Save a PromotionNotification.
   * 
   * @param promotionNotification
   * @return PromotionNotification
   */
  public PromotionNotification save( PromotionNotification promotionNotification );

  /**
   * Get a list of promotionNotifications containing promotionEmailNotification subclasses by the
   * Promotion Id.
   * 
   * @param promotionId
   * @return List
   */
  public List getAllPromotionEmailNotificationsByPromotionId( Long promotionId );

  /**
   * Get a list of promotionNotifications containing claimFormStepEmailNotification subclasses by
   * the Promotion Id.
   * 
   * @param promotionId
   * @return List
   */
  public List getAllClaimFormStepEmailNotificationsByPromotionId( Long promotionId );

  /**
   * Get a PromotionNotification instance given a ClaimFormSteEmailNotification id.
   * 
   * @param promotionId
   * @param claimFormStepEmailNotificationId
   * @return PromotionNotification
   */
  public PromotionNotification getClaimPromotionNotificationByClaimFormStepEmailNotificationId( Long promotionId, Long claimFormStepEmailNotificationId );
}
