
package com.biperf.core.service.participantsurvey.impl;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.biperf.core.dao.instantpoll.InstantPollDAO;
import com.biperf.core.dao.participant.ParticipantSurveyDAO;
import com.biperf.core.domain.promotion.SurveyQuestionResponse;
import com.biperf.core.domain.survey.ParticipantSurvey;
import com.biperf.core.domain.survey.ParticipantSurveyResponse;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.participantsurvey.ParticipantSurveyService;

public class ParticipantSurveyServiceImpl implements ParticipantSurveyService
{
  private ParticipantSurveyDAO participantSurveyDAO;
  private InstantPollDAO instantPollDAO;

  public List<ParticipantSurvey> getCompletedSurveyByPromoId( Long promoId )
  {
    return participantSurveyDAO.getCompletedSurveyByPromoId( promoId );
  }

  public ParticipantSurvey getParticipantSurveyByPromotionAndSurveyIdAndUserId( Long promotionId, Long surveyId, Long userId )
  {
    return participantSurveyDAO.getParticipantSurveyByPromotionAndSurveyIdAndUserId( promotionId, surveyId, userId );
  }

  public ParticipantSurvey getParticipantSurveyBySurveyIdAndUserIdWithAssociations( Long surveyId, Long userId, AssociationRequestCollection associationRequestCollection )
  {
    return participantSurveyDAO.getParticipantSurveyBySurveyIdAndUserIdWithAssociations( surveyId, userId, associationRequestCollection );
  }

  public List<ParticipantSurvey> getParticipantSurveyByPromotionIdAndUserId( Long promotionId, Long userId )
  {
    return participantSurveyDAO.getParticipantSurveyByPromotionIdAndUserId( promotionId, userId );
  }

  public ParticipantSurvey save( ParticipantSurvey participantSurvey )
  {
    // get the children and hold onto them.
    Set paxSurveyResponse = participantSurvey.getParticipantSurveyResponse();

    // clear out children for clean insert of participant
    participantSurvey.setParticipantSurveyResponse( new LinkedHashSet() );

    Iterator paxSurveyResponseIterator = paxSurveyResponse.iterator();
    while ( paxSurveyResponseIterator.hasNext() )
    {
      participantSurvey.addParticipantSurveyResponse( (ParticipantSurveyResponse)paxSurveyResponseIterator.next() );
    }
    return participantSurveyDAO.save( participantSurvey );
  }

  public SurveyQuestionResponse saveSurveyQuestionResponse( SurveyQuestionResponse surveyQuestionResponse )
  {
    return instantPollDAO.saveSurveyQuestionResponse( surveyQuestionResponse );
  }

  public ParticipantSurvey getParticipantSurveyByPromotionAndSurveyIdAndUserIdWithAssociations( Long promotionId,
                                                                                                Long surveyId,
                                                                                                Long userId,
                                                                                                AssociationRequestCollection associationRequestCollection )
  {
    return participantSurveyDAO.getParticipantSurveyByPromotionAndSurveyIdAndUserIdWithAssociations( promotionId, surveyId, userId, associationRequestCollection );
  }

  public void setParticipantSurveyDAO( ParticipantSurveyDAO participantSurveyDAO )
  {
    this.participantSurveyDAO = participantSurveyDAO;
  }

  public void setInstantPollDAO( InstantPollDAO instantPollDAO )
  {
    this.instantPollDAO = instantPollDAO;
  }
}
