
package com.biperf.core.dao.participant;

import java.util.List;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.participant.ParticipantGroup;
import com.biperf.core.value.participant.ParticipantGroupList;

public interface ParticipantGroupDAO extends DAO
{

  public static final String BEAN_NAME = "participantGroupDAO";

  public ParticipantGroup find( Long groupId );

  public ParticipantGroup saveOrUpdate( ParticipantGroup group );

  public ParticipantGroupList findGroupsByPaxId( Long userId );

  public List<ParticipantGroup> findGroupsByUserIdAndStartsWith( Long userId, String startsWith );

  public int findGroupCountByUserIdAndStartsWith( Long userId, String startsWith );

  public void delete( Long participantGroupId );

}
