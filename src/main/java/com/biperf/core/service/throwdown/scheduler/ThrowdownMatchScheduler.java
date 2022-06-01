
package com.biperf.core.service.throwdown.scheduler;

import java.util.List;
import java.util.Set;

import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.Match;
import com.biperf.core.domain.promotion.Round;
import com.biperf.core.domain.promotion.ThrowdownPromotion;
import com.biperf.core.service.SAO;
import com.biperf.core.service.throwdown.ParticipantAudienceConflictResult;

public interface ThrowdownMatchScheduler extends SAO
{
  public static String BEAN_NAME = "throwdownMatchScheduler";

  public Set<Match> scheduleHeadToHeadMatches( ThrowdownPromotion promotion );

  public Set<Match> scheduleIncrementalHeadToHeadMatches( Round round, List<Participant> potentialParticipantsForThisDivision, Set<ParticipantAudienceConflictResult> conflicts );
}
