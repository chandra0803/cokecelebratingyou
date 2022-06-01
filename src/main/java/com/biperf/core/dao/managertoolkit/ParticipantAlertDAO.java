
package com.biperf.core.dao.managertoolkit;

import com.biperf.core.domain.managertoolkit.ParticipantAlert;
import com.biperf.core.service.SAO;

public interface ParticipantAlertDAO extends SAO
{
  public static final String BEAN_NAME = "participantAlertDAO";

  public ParticipantAlert saveParticipantAlert( ParticipantAlert participantAlert );

  public ParticipantAlert getAlertByPaxIdContestIdAndType( Long paxId, Long contestId, String type );

}
