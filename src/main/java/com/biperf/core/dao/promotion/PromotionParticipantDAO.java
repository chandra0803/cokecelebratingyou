/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/dao/promotion/PromotionParticipantDAO.java,v $
 */

package com.biperf.core.dao.promotion;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.promotion.PromotionApprovalParticipant;
import com.biperf.core.domain.promotion.PromotionAudience;
import com.biperf.core.domain.promotion.PromotionTeamPosition;

/**
 * PromotionParticipantDAO.
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
public interface PromotionParticipantDAO extends DAO
{

  /**
   * Gets the promotionAudience by id.
   * 
   * @param promotionAudienceId
   * @return PromotionAudience
   */
  public PromotionAudience getPromotionAudienceById( Long promotionAudienceId );

  /**
   * Deletes the promotionAudience.
   * 
   * @param promotionAudience
   */
  public void deletePromotionAudience( PromotionAudience promotionAudience );

  /**
   * Saves the promotionAudience.
   * 
   * @param promotionAudience
   * @return PromotionAudience
   */
  public PromotionAudience savePromotionAudience( PromotionAudience promotionAudience );

  /**
   * Gets the promotionApprovalParticipant by id.
   * 
   * @param promotionApprovalParticipantId
   * @return PromotionApprovalParticipant
   */
  public PromotionApprovalParticipant getPromotionApprovalParticipantById( Long promotionApprovalParticipantId );

  /**
   * Deletes the promotionApprovalParticipant.
   * 
   * @param promotionApprovalParticipant
   */
  public void deletePromotionApprovalParticipant( PromotionApprovalParticipant promotionApprovalParticipant );

  /**
   * Saves the promotionApprovalParticipant.
   * 
   * @param promotionApprovalParticipant
   * @return PromotionApprovalParticipant
   */
  public PromotionApprovalParticipant savePromotionApprovalParticipant( PromotionApprovalParticipant promotionApprovalParticipant );

  /**
   * Save the promotionTeamPosition.
   * 
   * @param promotionTeamPosition
   * @return PromotionTeamPosition
   */
  public PromotionTeamPosition savePromotionTeamPosition( PromotionTeamPosition promotionTeamPosition );

  /**
   * Get the promotionTeamPosition by the id param.
   * 
   * @param promotionTeamPositionId
   * @return PromotionTeamPosition
   */
  public PromotionTeamPosition getPromotionTeamPositionById( Long promotionTeamPositionId );

  /**
   * Delete the promotionTeamPosition.
   * 
   * @param promotionTeamPosition
   */
  public void deletePromotionTeamPosition( PromotionTeamPosition promotionTeamPosition );

}
