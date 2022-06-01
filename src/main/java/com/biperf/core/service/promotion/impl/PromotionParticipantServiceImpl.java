/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/service/promotion/impl/PromotionParticipantServiceImpl.java,v $
 */

package com.biperf.core.service.promotion.impl;

import com.biperf.core.dao.promotion.PromotionParticipantDAO;
import com.biperf.core.domain.promotion.PromotionApprovalParticipant;
import com.biperf.core.domain.promotion.PromotionAudience;
import com.biperf.core.domain.promotion.PromotionTeamPosition;
import com.biperf.core.service.promotion.PromotionParticipantService;

/**
 * PromotionParticipantServiceImpl.
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
 *
 */
public class PromotionParticipantServiceImpl implements PromotionParticipantService
{

  private PromotionParticipantDAO promotionParticipantDAO;

  /**
   * Set the promotionParticipantDAO through IoC.
   * 
   * @param promotionParticipantDAO
   */
  public void setPromotionParticipantDAO( PromotionParticipantDAO promotionParticipantDAO )
  {
    this.promotionParticipantDAO = promotionParticipantDAO;
  }

  /**
   * Get the promotionAudience by the id param. Overridden from
   * 
   * @see com.biperf.core.service.promotion.PromotionParticipantService#getPromotionAudienceById(java.lang.Long)
   * @param promotionAudienceId
   * @return PromotionAudience
   */
  public PromotionAudience getPromotionAudienceById( Long promotionAudienceId )
  {
    return this.promotionParticipantDAO.getPromotionAudienceById( promotionAudienceId );
  }

  /**
   * Delete the promotion. Overridden from
   * 
   * @see com.biperf.core.service.promotion.PromotionParticipantService#deletePromotionAudience(java.lang.Long)
   * @param promotionAudienceId
   */
  public void deletePromotionAudience( Long promotionAudienceId )
  {
    PromotionAudience promotionAudience = this.getPromotionAudienceById( promotionAudienceId );
    this.promotionParticipantDAO.deletePromotionAudience( promotionAudience );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.promotion.PromotionParticipantService#savePromotionAudience(com.biperf.core.domain.promotion.PromotionAudience)
   * @param promotionAudience
   * @return PromotionAudience
   */
  public PromotionAudience savePromotionAudience( PromotionAudience promotionAudience )
  {
    return this.promotionParticipantDAO.savePromotionAudience( promotionAudience );
  }

  /**
   * Get the promotionApprovalParticipant by the id param. Overridden from
   * 
   * @see com.biperf.core.service.promotion.PromotionParticipantService#getPromotionApprovalParticipantById(java.lang.Long)
   * @param promotionApprovalParticipantId
   * @return PromotionApprovalParticipant
   */
  public PromotionApprovalParticipant getPromotionApprovalParticipantById( Long promotionApprovalParticipantId )
  {
    return this.promotionParticipantDAO.getPromotionApprovalParticipantById( promotionApprovalParticipantId );
  }

  /**
   * Delete the promotion. Overridden from
   * 
   * @see com.biperf.core.service.promotion.PromotionParticipantService#deletePromotionApprovalParticipant(java.lang.Long)
   * @param promotionApprovalParticipantId
   */
  public void deletePromotionApprovalParticipant( Long promotionApprovalParticipantId )
  {
    PromotionApprovalParticipant promotionApprovalParticipant = this.getPromotionApprovalParticipantById( promotionApprovalParticipantId );
    this.promotionParticipantDAO.deletePromotionApprovalParticipant( promotionApprovalParticipant );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.promotion.PromotionParticipantService#savePromotionApprovalParticipant(com.biperf.core.domain.promotion.PromotionApprovalParticipant)
   * @param promotionApprovalParticipant
   * @return PromotionApprovalParticipant
   */
  public PromotionApprovalParticipant savePromotionApprovalParticipant( PromotionApprovalParticipant promotionApprovalParticipant )
  {
    return this.promotionParticipantDAO.savePromotionApprovalParticipant( promotionApprovalParticipant );
  }

  /**
   * Save the promotionTeamPosition. Overridden from
   * 
   * @see com.biperf.core.service.promotion.PromotionParticipantService#savePromotionTeamPosition(com.biperf.core.domain.promotion.PromotionTeamPosition)
   * @param promotionTeamPosition
   * @return PromotionTeamPosition
   */
  public PromotionTeamPosition savePromotionTeamPosition( PromotionTeamPosition promotionTeamPosition )
  {
    return this.promotionParticipantDAO.savePromotionTeamPosition( promotionTeamPosition );
  }

  /**
   * Get the promotionTeamPosition by the id param. Overridden from
   * 
   * @see com.biperf.core.service.promotion.PromotionParticipantService#getPromotionTeamPositionById(java.lang.Long)
   * @param promotionTeamPositionId
   * @return PromotionTeamPosition
   */
  public PromotionTeamPosition getPromotionTeamPositionById( Long promotionTeamPositionId )
  {
    return this.promotionParticipantDAO.getPromotionTeamPositionById( promotionTeamPositionId );
  }

  /**
   * Delete the PromotionTeamPosition Overridden from
   * 
   * @see com.biperf.core.service.promotion.PromotionParticipantService#deletePromotionTeamPosition(java.lang.Long)
   * @param promotionTeamPositionId
   */
  public void deletePromotionTeamPosition( Long promotionTeamPositionId )
  {
    this.promotionParticipantDAO.deletePromotionTeamPosition( this.getPromotionTeamPositionById( promotionTeamPositionId ) );
  }

}
