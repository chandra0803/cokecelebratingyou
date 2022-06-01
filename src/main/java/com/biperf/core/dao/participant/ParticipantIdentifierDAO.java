
package com.biperf.core.dao.participant;

import java.util.List;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.participant.ParticipantIdentifier;

public interface ParticipantIdentifierDAO extends DAO
{
  public static final String BEAN_NAME = "participantIdentifierDAO";

  public List<ParticipantIdentifier> getAll();

  public void save( ParticipantIdentifier pi );

  public ParticipantIdentifier getById( Long id );

  public List<ParticipantIdentifier> getSelected();

  public List<ParticipantIdentifier> getUnSelected();
}
