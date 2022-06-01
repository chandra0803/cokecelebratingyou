
package com.biperf.core.dao.participant;

import java.util.Date;
import java.util.List;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.survey.ParticipantSurvey;
import com.biperf.core.service.AssociationRequestCollection;

public interface ParticipantSurveyDAO extends DAO
{
  public static final String BEAN_NAME = "participantSurveyDAO";

  public List<ParticipantSurvey> getCompletedSurveyByPromoId( Long promoId );

  public ParticipantSurvey getParticipantSurveyByPromotionAndSurveyIdAndUserId( Long promotionId, Long surveyId, Long userId );

  public List<ParticipantSurvey> getParticipantSurveyByPromotionIdAndUserId( Long promotionId, Long userId );

  public ParticipantSurvey save( ParticipantSurvey participantSurvey );

  public List getUserIdsByPromoIdWithinRange( Long promoId, Date startDate, Date endDate );

  public ParticipantSurvey getParticipantSurveyByPromotionAndSurveyIdAndUserIdWithAssociations( Long promotionId,
                                                                                                Long surveyId,
                                                                                                Long userId,
                                                                                                AssociationRequestCollection associationRequestCollection );

  public ParticipantSurvey getParticipantSurveyBySurveyIdAndUserIdWithAssociations( Long surveyId, Long userId, AssociationRequestCollection associationRequestCollection );
}
