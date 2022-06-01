/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/service/promotion/PromotionParticipantService.java,v $
 */

package com.biperf.core.service.promotion;

import com.biperf.core.domain.promotion.PromotionApprovalParticipant;
import com.biperf.core.domain.promotion.PromotionAudience;
import com.biperf.core.domain.promotion.PromotionTeamPosition;
import com.biperf.core.service.SAO;

/**
 * PromotionParticipantService.
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
public interface PromotionParticipantService extends SAO
{

  /** Name of service bean in the beanFactory */
  public static final String BEAN_NAME = "promotionParticipantService";

  /**
   * Get a promotionAudience by Id.
   * 
   * @param promotionAudienceId
   * @return PromotionAudience
   */
  public PromotionAudience getPromotionAudienceById( Long promotionAudienceId );

  /**
   * Delete the promotionAudience associated to the id param.
   * 
   * @param promotionAudienceId
   */
  public void deletePromotionAudience( Long promotionAudienceId );

  /**
   * Save the promotionAudience.
   * 
   * @param promotionAudience
   * @return PromotionAudience
   */
  public PromotionAudience savePromotionAudience( PromotionAudience promotionAudience );

  /**
   * Save the promotionTeamPosition.
   * 
   * @param promotionTeamPosition
   * @return PromotionTeamPosition
   */
  public PromotionTeamPosition savePromotionTeamPosition( PromotionTeamPosition promotionTeamPosition );

  /**
   * Get the promotionTeamPosition by id.
   * 
   * @param promotionTeamPositionId
   * @return PromotionTeamPosition
   */
  public PromotionTeamPosition getPromotionTeamPositionById( Long promotionTeamPositionId );

  /**
   * Delete the promotionTeamPosition by the id.
   * 
   * @param promotionTeamPositionId
   */
  public void deletePromotionTeamPosition( Long promotionTeamPositionId );

  /**
   * Get a promotionApprovalParticipant by Id.
   * 
   * @param promotionApprovalParticipantId
   * @return PromotionApprovalParticipant
   */

  public PromotionApprovalParticipant getPromotionApprovalParticipantById( Long promotionApprovalParticipantId );

  /**
   * Delete the promotionApprovalParticipant associated to the id param.
   * 
   * @param promotionApprovalParticipantId
   */
  public void deletePromotionApprovalParticipant( Long promotionApprovalParticipantId );

  /**
   * Save the promotionApprovalParticipant.
   * 
   * @param promotionApprovalParticipant
   * @return PromotionApprovalParticipant
   */
  public PromotionApprovalParticipant savePromotionApprovalParticipant( PromotionApprovalParticipant promotionApprovalParticipant );

}
