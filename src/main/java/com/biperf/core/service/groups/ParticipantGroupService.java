
package com.biperf.core.service.groups;

import java.util.List;

import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.SAO;
import com.biperf.core.ui.groups.ParticipantGroupView;
import com.biperf.core.value.participant.ParticipantGroupList;

/**
 *  ParticipantGroupService  interface
 */
public interface ParticipantGroupService extends SAO
{
  public static final String BEAN_NAME = "participantGroupService";

  public ParticipantGroupView getGroupDetailsByGroupId( Long groupId ) throws Exception;

  public ParticipantGroupList getGroupDetailsByUserId( Long userId ) throws Exception;

  public void delete( Long groupId );

  public ParticipantGroupView saveParticipantGroup( Long groupId, String groupName, List<Long> groupMemebers ) throws ServiceErrorException;
}
