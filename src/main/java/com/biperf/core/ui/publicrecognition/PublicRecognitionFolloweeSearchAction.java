
package com.biperf.core.ui.publicrecognition;

import com.biperf.core.domain.participant.Participant;
import com.biperf.core.ui.search.ParticipantAutoCompleteSearchAction;
import com.biperf.core.utils.UserManager;

public class PublicRecognitionFolloweeSearchAction extends ParticipantAutoCompleteSearchAction
{
  @Override
  protected boolean getIsLocked( Participant pax )
  {

    Long userId = UserManager.getUserId();
    if ( userId.equals( pax.getId() ) || !pax.isAllowPublicInformation() )
    {
      return Boolean.TRUE;
    }
    return !pax.isAllowPublicRecognition();
  }
}
