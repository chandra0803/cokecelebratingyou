/**
 * 
 */

package com.biperf.core.service.home.strategy;

import java.util.Map;

import com.biperf.core.domain.homepage.FilterAppSetup;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.service.forum.ForumTopicService;

/**
 * @author poddutur
 *
 */
public class ForumModuleAppAudienceStrategy extends AbstractModuleAppAudienceStrategy implements ModuleAppAudienceStrategy
{
  private ForumTopicService forumTopicService;

  @Override
  public boolean isUserInAudience( Participant participant, FilterAppSetup filter, Map<String, Object> parameterMap )
  {
    if ( participant.isParticipant() && forumTopicService.isPaxExistsInAudienceListOfForumTopics( participant.getId() ) )
    {
      return true;
    }
    if ( participant.isParticipant() && forumTopicService.isAllActivePaxTypeInForumTopics() )
    {
      return true;
    }
    return false;
  }

  public void setForumTopicService( ForumTopicService forumTopicService )
  {
    this.forumTopicService = forumTopicService;
  }
}
