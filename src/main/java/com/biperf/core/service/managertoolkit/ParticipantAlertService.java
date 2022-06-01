
package com.biperf.core.service.managertoolkit;

import com.biperf.core.domain.managertoolkit.ParticipantAlert;
import com.biperf.core.service.SAO;

public interface ParticipantAlertService extends SAO
{
  public static final String BEAN_NAME = "participantAlertService";

  public ParticipantAlert saveParticipantAlert( ParticipantAlert participantAlert );

  public ParticipantAlert getAlertByPaxIdContestIdAndType( Long paxId, Long contestId, String type );

}
