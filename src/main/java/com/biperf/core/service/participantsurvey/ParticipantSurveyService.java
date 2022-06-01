
package com.biperf.core.service.participantsurvey;

import java.util.List;

import com.biperf.core.domain.promotion.SurveyQuestionResponse;
import com.biperf.core.domain.survey.ParticipantSurvey;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.SAO;

public interface ParticipantSurveyService extends SAO
{
  public static final String BEAN_NAME = "participantSurveyService";

  List<ParticipantSurvey> getCompletedSurveyByPromoId( Long promoId );

  public ParticipantSurvey getParticipantSurveyByPromotionAndSurveyIdAndUserId( Long promotionId, Long surveyId, Long userId );

  public List<ParticipantSurvey> getParticipantSurveyByPromotionIdAndUserId( Long promotionId, Long userId );

  public ParticipantSurvey save( ParticipantSurvey participantSurvey );

  public ParticipantSurvey getParticipantSurveyByPromotionAndSurveyIdAndUserIdWithAssociations( Long promotionId,
                                                                                                Long surveyId,
                                                                                                Long userId,
                                                                                                AssociationRequestCollection associationRequestCollection );

  public ParticipantSurvey getParticipantSurveyBySurveyIdAndUserIdWithAssociations( Long surveyId, Long userId, AssociationRequestCollection associationRequestCollection );

  public SurveyQuestionResponse saveSurveyQuestionResponse( SurveyQuestionResponse surveyQuestionResponse );

}
