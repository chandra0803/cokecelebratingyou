
package com.biperf.core.dao.diycommunications;

import java.util.List;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.diycommunications.DIYCommunications;
import com.biperf.core.domain.participant.Audience;
import com.biperf.core.value.FormattedValueBean;

public interface ParticipantDIYCommunicationsDAO extends DAO
{
  public static final String BEAN_NAME = "participantDIYCommunicationsDAO";

  public List<DIYCommunications> getActiveByCommunicationType( Long managerId, String communicationType );

  public List<DIYCommunications> getArchievedByCommunicationType( Long managerId, String communicationType );

  public DIYCommunications getDIYCommunicationsById( Long id );

  public List<Audience> getPublicAudienceList();

  public DIYCommunications saveDIYCommunications( DIYCommunications diyCommunications );

  public DIYCommunications getCommunicationsByTitleAndType( String communicationsTitle, String communicationsType );

  public List<DIYCommunications> getActiveByCommunicationType( String communicationType );

  public List<FormattedValueBean> getAudienceParticipants( Long audienceId );

}
