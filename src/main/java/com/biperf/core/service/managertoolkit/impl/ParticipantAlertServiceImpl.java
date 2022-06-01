
package com.biperf.core.service.managertoolkit.impl;

import com.biperf.core.dao.managertoolkit.ParticipantAlertDAO;
import com.biperf.core.domain.managertoolkit.ParticipantAlert;
import com.biperf.core.service.managertoolkit.ParticipantAlertService;

public class ParticipantAlertServiceImpl implements ParticipantAlertService
{
  private ParticipantAlertDAO participantAlertDAO;

  public ParticipantAlert saveParticipantAlert( ParticipantAlert participantAlert )
  {
    return participantAlertDAO.saveParticipantAlert( participantAlert );
  }

  public ParticipantAlert getAlertByPaxIdContestIdAndType( Long paxId, Long contestId, String type )
  {
    return participantAlertDAO.getAlertByPaxIdContestIdAndType( paxId, contestId, type );
  }

  public void setParticipantAlertDAO( ParticipantAlertDAO participantAlertDAO )
  {
    this.participantAlertDAO = participantAlertDAO;
  }

}
