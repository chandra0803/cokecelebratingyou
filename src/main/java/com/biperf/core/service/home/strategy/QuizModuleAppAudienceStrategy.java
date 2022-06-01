
package com.biperf.core.service.home.strategy;

import java.util.Map;

import com.biperf.core.domain.homepage.FilterAppSetup;
import com.biperf.core.domain.participant.Participant;

public class QuizModuleAppAudienceStrategy extends AbstractModuleAppAudienceStrategy implements ModuleAppAudienceStrategy
{
  @Override
  public boolean isUserInAudience( Participant participant, FilterAppSetup filter, Map<String, Object> parameterMap )
  {
    if ( !participant.isParticipant() )
    {
      return false;
    }
    else
    {
      // This could be done even more efficiently with a SQL query - probably...
      // quizClaimList = getPromotionService().getPendingQuizSubmissionList( participant.getId() );
      return getPromotionService().isPendingQuizSubmissionsForUser( participant.getId() );
    }
  }
}
