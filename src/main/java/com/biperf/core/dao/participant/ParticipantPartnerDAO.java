/*
 * Copyright 2008 BI, Inc.  All rights reserved.
 */

package com.biperf.core.dao.participant;

import java.util.List;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.participant.ParticipantPartner;

/**
 * ParticipantPartnerDAO.
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
 * <td>reddy</td>
 * <td>Feb 28, 2008</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public interface ParticipantPartnerDAO extends DAO
{
  public static final String BEAN_NAME = "participantPartnerDAO";

  public void clearPartnersForParticipantAndPromotion( Long promotionId, Long participantId );

  /**
   * Saves the participant address book information to the database.
   * 
   * @param participantPartnerAssoc
   * @return ParticipantPartnerAssoc
   */
  public ParticipantPartner saveParticipantPartnerAssoc( ParticipantPartner participantPartnerAssoc );

  /**
   * Retreieves a particular ParticipantPartnerAssoc
   * 
   * @param id
   * @return ParticipantPartnerAssoc
   */

  public List<ParticipantPartner> getParticipantPartnersByPromotion( Long promotionId );

  public List getPartnersByPromotionAndParticipant( Long promoId, Long paxId );

  public List getParticipantsByPromotionAndPartner( Long promoId, Long partnerId );

  public List<ParticipantPartner> getParticipantPartnersWhereSelectionEmailNotSentByPromotion( Long promotionId );

}
