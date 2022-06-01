
package com.biperf.core.service.home.strategy;

import java.util.Map;

import com.biperf.core.domain.homepage.FilterAppSetup;
import com.biperf.core.domain.participant.Participant;

public interface ModuleAppAudienceStrategy
{
  /*
   * This method determines if the user passed in should "see" the ModuleApp associated with the
   * passed in FilterAppSetup.
   */
  public boolean isUserInAudience( Participant participant, FilterAppSetup filter, Map<String, Object> parameterMap );
}
