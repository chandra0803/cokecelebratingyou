/*
 * Copyright 2008 BI, Inc.  All rights reserved.
 */

package com.biperf.core.dao.participant.hibernate;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.participant.ParticipantPartnerDAO;
import com.biperf.core.domain.participant.ParticipantPartner;
import com.biperf.core.utils.hibernate.HibernateUtil;

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
public class ParticipantPartnerDAOImpl extends BaseDAO implements ParticipantPartnerDAO
{

  /**
   * Saves the participant address book information to the database.
   * 
   * @param participantPartnerAssoc
   * @return ParticipantPartnerAssoc
   */
  public ParticipantPartner saveParticipantPartnerAssoc( ParticipantPartner participantPartnerAssoc )
  {
    return (ParticipantPartner)HibernateUtil.saveOrUpdateOrShallowMerge( participantPartnerAssoc );
  }

  @SuppressWarnings( "unchecked" )
  public List<ParticipantPartner> getParticipantPartnersWhereSelectionEmailNotSentByPromotion( Long promotionId )
  {
    Session session = getSession();
    Query query = session.getNamedQuery( "com.biperf.core.domain.participant.AllPartnersNotSentSelectionAlertForPromotion" );
    query.setParameter( "promotionId", promotionId );
    return query.list();
  }

  @SuppressWarnings( "unchecked" )
  public List<ParticipantPartner> getParticipantPartnersByPromotion( Long promotionId )
  {
    Session session = getSession();
    Query query = session.getNamedQuery( "com.biperf.core.domain.participant.AllParticipantPartnersForSpecificPromotion" );
    query.setParameter( "promotionId", promotionId );
    return query.list();
  }

  public List getPartnersByPromotionAndParticipant( Long promoId, Long paxId )
  {
    Session session = getSession();
    Query query = session.getNamedQuery( "com.biperf.core.domain.participant.AllPartnersOfParticipantForSpecificPromotion" );
    query.setParameter( "paxId", paxId );
    query.setParameter( "promotionId", promoId );
    return query.list();
  }

  public void clearPartnersForParticipantAndPromotion( Long promotionId, Long participantId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.participant.DeletePartnersForPaxForPromotion" );
    query.setParameter( "participantId", participantId );
    query.setParameter( "promotionId", promotionId );
    query.executeUpdate();
  }

  public List getParticipantsByPromotionAndPartner( Long promoId, Long partnerId )
  {
    Session session = getSession();
    Query query = session.getNamedQuery( "com.biperf.core.domain.participant.AllParticipantsOfPartnerForSpecificPromotion" );
    query.setParameter( "partnerId", partnerId );
    query.setParameter( "promotionId", promoId );
    return query.list();
  }
}
