/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/dao/promotion/hibernate/PromotionNotificationDAOImpl.java,v $
 */

package com.biperf.core.dao.promotion.hibernate;

import java.util.List;

import org.hibernate.Query;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.promotion.PromotionNotificationDAO;
import com.biperf.core.domain.promotion.ClaimFormNotificationType;
import com.biperf.core.domain.promotion.PromotionNotification;
import com.biperf.core.domain.promotion.PromotionNotificationType;
import com.biperf.core.utils.hibernate.HibernateUtil;

/**
 * PromotionNotificationDAOImpl.
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
public class PromotionNotificationDAOImpl extends BaseDAO implements PromotionNotificationDAO
{

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.promotion.PromotionNotificationDAO#getPromotionEmailNotificationTypeById(java.lang.Long)
   * @param id
   * @return PromotionNotification
   */
  public PromotionNotification getPromotionEmailNotificationTypeById( Long id )
  {
    return (PromotionNotification)getSession().get( PromotionNotificationType.class, id );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.promotion.PromotionNotificationDAO#getClaimFormEmailNotificationTypeById(java.lang.Long)
   * @param id
   * @return PromotionNotification
   */
  public PromotionNotification getClaimFormEmailNotificationTypeById( Long id )
  {
    return (PromotionNotification)getSession().get( ClaimFormNotificationType.class, id );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.promotion.PromotionNotificationDAO#save(com.biperf.core.domain.promotion.PromotionNotification)
   * @param promotionNotification
   * @return PromotionNotification
   */
  public PromotionNotification save( PromotionNotification promotionNotification )
  {
    return (PromotionNotification)HibernateUtil.saveOrUpdateOrShallowMerge( promotionNotification );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.promotion.PromotionNotificationDAO#getAllPromotionEmailNotificationsByPromotionId(java.lang.Long)
   * @param promotionId
   * @return List
   */
  public List getAllPromotionEmailNotificationsByPromotionId( Long promotionId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.AllPromotionNotificationTypeByPromotionIdList" );
    query.setLong( "promotionId", promotionId.longValue() );
    return query.list();
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.promotion.PromotionNotificationDAO#getAllClaimFormStepEmailNotificationsByPromotionId(java.lang.Long)
   * @param promotionId
   * @return List
   */
  public List getAllClaimFormStepEmailNotificationsByPromotionId( Long promotionId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.AllClaimFormNotificationTypeByPromotionIdList" );
    query.setLong( "promotionId", promotionId.longValue() );
    return query.list();
  }

  /**
   * Get a PromotionNotification instance given a ClaimFormStepEmailNotification id.
   * 
   * @param promotionId
   * @param claimFormStepEmailNotificationId
   * @return PromotionNotification
   */
  public PromotionNotification getClaimPromotionNotificationByClaimFormStepEmailNotificationId( Long promotionId, Long claimFormStepEmailNotificationId )
  {

    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.ClaimPromotionNotificationByClaimFormStepEmailNotificationId" );
    query.setLong( "promotionId", promotionId.longValue() );
    query.setLong( "claimFormStepEmailNotificationId", claimFormStepEmailNotificationId.longValue() );
    return (PromotionNotification)query.uniqueResult();
  }

}
