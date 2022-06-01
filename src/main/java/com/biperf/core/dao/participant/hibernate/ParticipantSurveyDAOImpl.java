
package com.biperf.core.dao.participant.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.participant.ParticipantSurveyDAO;
import com.biperf.core.domain.survey.ParticipantSurvey;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.utils.hibernate.HibernateUtil;

public class ParticipantSurveyDAOImpl extends BaseDAO implements ParticipantSurveyDAO
{
  public List<ParticipantSurvey> getCompletedSurveyByPromoId( Long promoId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.dao.participant.completedSurveysByPromotionId" );
    query.setParameter( "promoId", promoId );
    return query.list();
  }

  @Override
  public ParticipantSurvey getParticipantSurveyByPromotionAndSurveyIdAndUserIdWithAssociations( Long promotionId,
                                                                                                Long surveyId,
                                                                                                Long userId,
                                                                                                AssociationRequestCollection associationRequestCollection )
  {
    Criteria criteria = getSession().createCriteria( ParticipantSurvey.class );
    criteria.add( Restrictions.eq( "participant.id", userId ) );
    criteria.add( Restrictions.eq( "promotionId", promotionId ) );
    criteria.add( Restrictions.eq( "surveyId", surveyId ) );
    ParticipantSurvey participantSurvey = (ParticipantSurvey)criteria.uniqueResult();
    if ( associationRequestCollection != null )
    {
      associationRequestCollection.process( participantSurvey );
    }
    return participantSurvey;
  }

  public ParticipantSurvey getParticipantSurveyByPromotionAndSurveyIdAndUserId( Long promotionId, Long surveyId, Long userId )
  {
    Criteria criteria = getSession().createCriteria( ParticipantSurvey.class );
    criteria.add( Restrictions.eq( "participant.id", userId ) );
    criteria.add( Restrictions.eq( "promotionId", promotionId ) );
    criteria.add( Restrictions.eq( "surveyId", surveyId ) );
    return (ParticipantSurvey)criteria.uniqueResult();
  }

  public ParticipantSurvey getParticipantSurveyBySurveyIdAndUserIdWithAssociations( Long surveyId, Long userId, AssociationRequestCollection associationRequestCollection )
  {
    Criteria criteria = getSession().createCriteria( ParticipantSurvey.class );
    criteria.add( Restrictions.eq( "participant.id", userId ) );
    criteria.add( Restrictions.eq( "surveyId", surveyId ) );
    ParticipantSurvey participantSurvey = (ParticipantSurvey)criteria.uniqueResult();
    if ( associationRequestCollection != null )
    {
      associationRequestCollection.process( participantSurvey );
    }
    return participantSurvey;
  }

  public List<ParticipantSurvey> getParticipantSurveyByPromotionIdAndUserId( Long promotionId, Long userId )
  {
    Criteria criteria = getSession().createCriteria( ParticipantSurvey.class );
    criteria.add( Restrictions.eq( "participant.id", userId ) );
    criteria.add( Restrictions.eq( "promotionId", promotionId ) );
    criteria.add( Restrictions.eq( "completed", true ) );
    return criteria.list();
  }

  public ParticipantSurvey save( ParticipantSurvey participantSurvey )
  {
    return (ParticipantSurvey)HibernateUtil.saveOrUpdateOrShallowMerge( participantSurvey );
  }

  public List getUserIdsByPromoIdWithinRange( Long promoId, Date startDate, Date endDate )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.claim.SueveyUserIdsByPromoIdForTimePeriod" );

    query.setParameter( "promoId", promoId );
    query.setParameter( "startDate", startDate );
    query.setParameter( "endDate", endDate );

    return query.list();
  }

}
