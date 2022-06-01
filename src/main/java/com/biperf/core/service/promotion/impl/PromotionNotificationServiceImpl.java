/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/service/promotion/impl/PromotionNotificationServiceImpl.java,v $
 */

package com.biperf.core.service.promotion.impl;

import java.util.Iterator;
import java.util.List;

import com.biperf.core.dao.claim.ClaimFormDAO;
import com.biperf.core.dao.promotion.PromotionDAO;
import com.biperf.core.dao.promotion.PromotionNotificationDAO;
import com.biperf.core.domain.promotion.ClaimFormNotificationType;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionNotification;
import com.biperf.core.domain.promotion.PromotionNotificationType;
import com.biperf.core.service.promotion.PromotionNotificationService;

/**
 * PromotionNotificationServiceImpl.
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
public class PromotionNotificationServiceImpl implements PromotionNotificationService
{

  // private static final Log log = LogFactory.getLog( PromotionNotificationService.class );

  private PromotionDAO promotionDAO = null;
  private PromotionNotificationDAO promotionNotificationDAO = null;
  private ClaimFormDAO claimFormDAO = null;

  /**
   * Set the PromotionDAO through dependency injection (IoC).
   * 
   * @param promotionDAO
   */
  public void setPromotionDAO( PromotionDAO promotionDAO )
  {
    this.promotionDAO = promotionDAO;
  }

  /**
   * Set the PromotionNotificationDAO through dependency injection (IoC).
   * 
   * @param promotionNotificationDAO
   */
  public void setPromotionNotificationDAO( PromotionNotificationDAO promotionNotificationDAO )
  {
    this.promotionNotificationDAO = promotionNotificationDAO;
  }

  /**
   * Set the ClaimFormDAO through dependency injection (IoC).
   * 
   * @param claimFormDAO
   */
  public void setClaimFormDAO( ClaimFormDAO claimFormDAO )
  {
    this.claimFormDAO = claimFormDAO;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.promotion.PromotionNotificationService#getPromotionNotificationTypeById(java.lang.Long)
   * @param promotionNotificationId
   * @return PromotionNotification
   */
  public PromotionNotificationType getPromotionNotificationTypeById( Long promotionNotificationId )
  {
    return (PromotionNotificationType)this.promotionNotificationDAO.getPromotionEmailNotificationTypeById( promotionNotificationId );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.promotion.PromotionNotificationService#getClaimFormNotificationTypeById(java.lang.Long)
   * @param promotionNotificationId
   * @return PromotionNotification
   */
  public ClaimFormNotificationType getClaimFormNotificationTypeById( Long promotionNotificationId )
  {
    return (ClaimFormNotificationType)this.promotionNotificationDAO.getClaimFormEmailNotificationTypeById( promotionNotificationId );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.promotion.PromotionNotificationService#getPromotionTypeNotificationsByPromotionId(java.lang.Long)
   * @param promotionId
   * @return List
   */
  public List getPromotionTypeNotificationsByPromotionId( Long promotionId )
  {
    return this.promotionNotificationDAO.getAllPromotionEmailNotificationsByPromotionId( promotionId );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.promotion.PromotionNotificationService#getClaimFormTypeNotificationsByPromotionId(java.lang.Long)
   * @param promotionId
   * @return List
   */
  public List getClaimFormTypeNotificationsByPromotionId( Long promotionId )
  {
    return this.promotionNotificationDAO.getAllClaimFormStepEmailNotificationsByPromotionId( promotionId );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.promotion.PromotionNotificationService#savePromotionNotification(java.lang.Long,
   *      com.biperf.core.domain.promotion.PromotionNotification)
   * @param promotionId
   * @param promoNotification
   * @return PromotionNotification
   */
  public PromotionNotification savePromotionNotification( Long promotionId, PromotionNotification promoNotification )
  {
    // Look up the promotion by id, promotion is needed when adding new notifications
    Promotion promotion = this.promotionDAO.getPromotionById( promotionId );

    if ( promoNotification instanceof ClaimFormNotificationType )
    {
      // If the promoNotification is a ClaimFormNotificationType, and has an ID, call the
      // getClaimFormEmailNotificationTypeById to get the attached PromotionNotification object
      // and update the values
      ClaimFormNotificationType claimFormNotification = (ClaimFormNotificationType)promoNotification;
      if ( claimFormNotification.getId() != null )
      {
        ClaimFormNotificationType existingNotification = (ClaimFormNotificationType)this.promotionNotificationDAO.getClaimFormEmailNotificationTypeById( claimFormNotification.getId() );
        existingNotification.setNotificationMessageId( claimFormNotification.getNotificationMessageId() );
        return this.promotionNotificationDAO.save( existingNotification );
      }
      // If the promoNotification does not have and ID it is a new one and a lookup needs to
      // be done to get the ClaimFormStepEmailNotification object
      claimFormNotification.setClaimFormStepEmailNotification( this.claimFormDAO.getClaimFormStepEmailNotificationById( claimFormNotification.getClaimFormStepEmailNotification().getId() ) );
      promotion.addPromotionNotification( promoNotification );
      return this.promotionNotificationDAO.save( promoNotification );
    }

    // If promoNotification is not an instance of ClaimFormNotificationType, it must be
    // a PromotionNotificationType, if it has an ID, call the getPromotionEmailNotificationTypeById
    // to get the attached PromotionNotification object and update the values
    PromotionNotificationType promoNotificationType = (PromotionNotificationType)promoNotification;
    if ( promoNotificationType.getId() != null )
    {
      PromotionNotificationType existingPromoNotification = (PromotionNotificationType)this.promotionNotificationDAO.getPromotionEmailNotificationTypeById( promoNotificationType.getId() );
      existingPromoNotification.setNotificationMessageId( promoNotificationType.getNotificationMessageId() );
      existingPromoNotification.setNumberOfDays( promoNotificationType.getNumberOfDays() );
      return this.promotionNotificationDAO.save( existingPromoNotification );
    }

    // If the promoNotification does not have and ID it is a new one
    promotion.addPromotionNotification( promoNotification );
    return this.promotionNotificationDAO.save( promoNotification );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.promotion.PromotionNotificationService#savePromotionNotifications(java.lang.Long,
   *      java.util.List)
   * @param promotionId
   * @param promotionNotifications
   */
  public void savePromotionNotifications( Long promotionId, List promotionNotifications )
  {
    Iterator it = promotionNotifications.iterator();
    while ( it.hasNext() )
    {
      PromotionNotification promoNotification = (PromotionNotification)it.next();
      promoNotification = savePromotionNotification( promotionId, promoNotification );
    }
  }

  /**
   * Get a PromotionNotification instance given a ClaimFormSteEmailNotification id.
   * 
   * @param promotionId
   * @param claimFormStepEmailNotificationId
   * @return PromotionNotification
   */
  public PromotionNotification getClaimPromotionNotificationByClaimFormStepEmailNotificationId( Long promotionId, Long claimFormStepEmailNotificationId )
  {
    return promotionNotificationDAO.getClaimPromotionNotificationByClaimFormStepEmailNotificationId( promotionId, claimFormStepEmailNotificationId );
  }

}
