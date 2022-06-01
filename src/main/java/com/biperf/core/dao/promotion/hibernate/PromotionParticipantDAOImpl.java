/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/dao/promotion/hibernate/PromotionParticipantDAOImpl.java,v $
 */

package com.biperf.core.dao.promotion.hibernate;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.promotion.PromotionParticipantDAO;
import com.biperf.core.domain.promotion.PromotionApprovalParticipant;
import com.biperf.core.domain.promotion.PromotionAudience;
import com.biperf.core.domain.promotion.PromotionTeamPosition;

/**
 * PromotionParticipantDAOImpl.
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
 * <td>crosenquest</td>
 * <td>Aug 29, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public class PromotionParticipantDAOImpl extends BaseDAO implements PromotionParticipantDAO
{

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.promotion.PromotionParticipantDAO#getPromotionAudienceById(java.lang.Long)
   * @param promotionAudienceId
   * @return PromotionAudience
   */
  public PromotionAudience getPromotionAudienceById( Long promotionAudienceId )
  {
    return (PromotionAudience)getSession().get( PromotionAudience.class, promotionAudienceId );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.promotion.PromotionParticipantDAO#deletePromotionAudience(com.biperf.core.domain.promotion.PromotionAudience)
   * @param promotionAudience
   */
  public void deletePromotionAudience( PromotionAudience promotionAudience )
  {
    getSession().delete( promotionAudience );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.promotion.PromotionParticipantDAO#savePromotionAudience(com.biperf.core.domain.promotion.PromotionAudience)
   * @param promotionAudience
   * @return PromotionAudience
   */
  public PromotionAudience savePromotionAudience( PromotionAudience promotionAudience )
  {
    getSession().save( promotionAudience );
    return promotionAudience;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.promotion.PromotionParticipantDAO#getPromotionApprovalParticipantById(java.lang.Long)
   * @param promotionApprovalParticipantId
   * @return PromotionApprovalParticipant
   */
  public PromotionApprovalParticipant getPromotionApprovalParticipantById( Long promotionApprovalParticipantId )
  {
    return (PromotionApprovalParticipant)getSession().get( PromotionApprovalParticipant.class, promotionApprovalParticipantId );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.promotion.PromotionParticipantDAO#deletePromotionApprovalParticipant(com.biperf.core.domain.promotion.PromotionApprovalParticipant)
   * @param promotionApprovalParticipant
   */
  public void deletePromotionApprovalParticipant( PromotionApprovalParticipant promotionApprovalParticipant )
  {
    getSession().delete( promotionApprovalParticipant );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.promotion.PromotionParticipantDAO#savePromotionApprovalParticipant(com.biperf.core.domain.promotion.PromotionApprovalParticipant)
   * @param promotionApprovalParticipant
   * @return PromotionApprovalParticipant
   */
  public PromotionApprovalParticipant savePromotionApprovalParticipant( PromotionApprovalParticipant promotionApprovalParticipant )
  {
    getSession().save( promotionApprovalParticipant );
    return promotionApprovalParticipant;
  }

  /**
   * Saves the promotionTeamPosition. Overridden from
   * 
   * @see com.biperf.core.dao.promotion.PromotionParticipantDAO#savePromotionTeamPosition(com.biperf.core.domain.promotion.PromotionTeamPosition)
   * @param promotionTeamPosition
   * @return PromotionTeamPosition
   */
  public PromotionTeamPosition savePromotionTeamPosition( PromotionTeamPosition promotionTeamPosition )
  {
    getSession().save( promotionTeamPosition );
    return promotionTeamPosition;
  }

  /**
   * Gets the promotionTeamPosition by id. Overridden from
   * 
   * @see com.biperf.core.dao.promotion.PromotionParticipantDAO#getPromotionTeamPositionById(java.lang.Long)
   * @param promotionTeamPositionId
   * @return PromotionTeamPosition
   */
  public PromotionTeamPosition getPromotionTeamPositionById( Long promotionTeamPositionId )
  {
    return (PromotionTeamPosition)getSession().get( PromotionTeamPosition.class, promotionTeamPositionId );
  }

  /**
   * Delete the promotionTeamPosition. Overridden from
   * 
   * @see com.biperf.core.dao.promotion.PromotionParticipantDAO#deletePromotionTeamPosition(com.biperf.core.domain.promotion.PromotionTeamPosition)
   * @param promotionTeamPosition
   */
  public void deletePromotionTeamPosition( PromotionTeamPosition promotionTeamPosition )
  {
    getSession().delete( promotionTeamPosition );
  }

}
