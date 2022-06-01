
package com.biperf.core.service.participant;

import java.util.List;

import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.participant.ParticipantIdentifier;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.SAO;
import com.biperf.core.ui.user.ActivationField;
import com.biperf.core.ui.user.ContactType;
import com.biperf.core.ui.user.PaxContactType;

public interface ParticipantActivationService extends SAO
{
  public static final String BEAN_NAME = "participantActivationService";

  public List<ParticipantIdentifier> getParticipantIdentifiers();

  public List<ParticipantIdentifier> getActiveParticipantIdentifiers();

  public void save( ParticipantIdentifier pi ) throws ServiceErrorException;

  public void save( List<ParticipantIdentifier> pis ) throws ServiceErrorException;

  public boolean isValidActivationFields( Participant participant, List<ActivationField> activationFields );

  public boolean isValidActivationFields( Long userId, List<ActivationField> activationFields );

  public boolean isValidPaxActivationIdentifier( Participant participant, ParticipantIdentifier pi, String valueToCheck );

  public ParticipantIdentifier getParticipantIdentifier( Long participantIdentifierId );

  public PaxContactType sendActivationLinkToParticipant( Long contactId, ContactType contactType ) throws ServiceErrorException;
}
