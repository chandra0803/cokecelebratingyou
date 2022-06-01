
package com.biperf.core.service.groups;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biperf.core.dao.participant.ParticipantGroupDAO;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.participant.ParticipantGroup;
import com.biperf.core.domain.participant.ParticipantGroupDetails;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.groups.ParticipantGroupView;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.ParticipantPreviewBean;
import com.biperf.core.value.participant.ParticipantGroupList;
import com.biperf.core.value.participant.ParticipantGroupList.Group;

/**
 *  ParticipantGroupService implementation 
 */
@Service( "participantGroupService" )
public class ParticipantGroupServiceImpl implements ParticipantGroupService
{
  private @Autowired ParticipantService participantService;
  private @Autowired ParticipantGroupDAO participantGroupDAO;

  @Override
  public ParticipantGroupView getGroupDetailsByGroupId( Long groupId ) throws Exception
  {
    ParticipantGroup participantGroup = participantGroupDAO.find( groupId );
    return mapToParticipantGroupView( participantGroup );
  }

  @Override
  public ParticipantGroupList getGroupDetailsByUserId( Long userId ) throws Exception
  {
    return participantGroupDAO.findGroupsByPaxId( userId );
  }

  @Override
  public void delete( Long groupId )
  {
    participantGroupDAO.delete( groupId );
  }

  @Override
  public ParticipantGroupView saveParticipantGroup( Long groupId, String groupName, List<Long> groupMemebers ) throws ParticipantGroupException
  {
    ParticipantGroup participantGroup = null;
    Long userId = UserManager.getUserId();
    ParticipantGroupList participantGroupList = participantGroupDAO.findGroupsByPaxId( userId );

    if ( groupId == null )
    {
      List<Group> groups = participantGroupList.getGroups();
      if ( CollectionUtils.isNotEmpty( groups ) )
      {
        for ( Group group : groups )
        {
          if ( group.getName().equalsIgnoreCase( groupName ) )
          {
            throw new ParticipantGroupException( ServiceErrorMessageKeys.GROUP_ALREADY_EXISTS );
          }
        }
      }
      participantGroup = createNewGroup( groupMemebers, groupName );
    }
    else
    {
      if ( groupId != null )
      {
        ParticipantGroup group = participantGroupDAO.find( groupId );
        group.setGroupName( groupName );
        Set<ParticipantGroupDetails> participantGroupDetailsList = group.getGroupDetails();
        participantGroupDetailsList.clear();
        participantGroup = editGroup( groupMemebers, group );
      }
    }

    if ( StringUtils.isEmpty( groupName ) )
    {
      throw new ParticipantGroupException( ServiceErrorMessageKeys.GROUP_NAME_REQUIRED );
    }

    if ( groupMemebers.size() < 2 )
    {
      throw new ParticipantGroupException( ServiceErrorMessageKeys.GROUP_SIZE_ERROR );
    }

    participantGroup = participantGroupDAO.saveOrUpdate( participantGroup );
    return mapToParticipantGroupView( participantGroup );
  }

  private ParticipantGroup createNewGroup( List<Long> groupMemebers, String groupName )
  {
    ParticipantGroup group = new ParticipantGroup();
    group.setDateCreated( new Date() );
    group.setGroupName( groupName );
    group.setGroupCreatedBy( participantService.getParticipantById( UserManager.getUserId() ) );

    return createParticipantGroup( groupMemebers, group );
  }

  private ParticipantGroup editGroup( List<Long> groupMemebers, ParticipantGroup group )
  {
    return createParticipantGroup( groupMemebers, group );
  }

  private ParticipantGroup createParticipantGroup( List<Long> groupMemebers, ParticipantGroup group )
  {
    ParticipantGroupDetails detail;
    for ( Long memberId : groupMemebers )
    {
      detail = new ParticipantGroupDetails();
      detail.setGroup( group );
      detail.setParticipant( participantService.getParticipantById( memberId ) );
      group.addDetails( detail );
    }
    return group;
  }

  public void setParticipantService( ParticipantService participantService )
  {
    this.participantService = participantService;
  }

  public void setParticipantGroupDAO( ParticipantGroupDAO participantGroupDAO )
  {
    this.participantGroupDAO = participantGroupDAO;
  }

  private ParticipantGroupView mapToParticipantGroupView( ParticipantGroup pariticipantGroup )
  {
    ParticipantGroupView participantGroupView = new ParticipantGroupView();

    if ( null != pariticipantGroup )
    {
      participantGroupView.setGroupName( pariticipantGroup.getGroupName() );
      participantGroupView.setDateCreated( pariticipantGroup.getDateCreated() );
      participantGroupView.setGroupCreatedBy( mapParticipantPreviewBean( pariticipantGroup.getGroupCreatedBy() ) );
      participantGroupView.setGroupId( pariticipantGroup.getId() );
      Set<ParticipantGroupDetails> groupDetails = pariticipantGroup.getGroupDetails();
      for ( ParticipantGroupDetails dtls : groupDetails )
      {
        Participant participant = dtls.getParticipant();
        if ( participant.getActive() )
        {
          participantGroupView.addParticipant( mapParticipantPreviewBean( dtls.getParticipant() ) );
        }
      }
    }
    return participantGroupView;
  }

  private ParticipantPreviewBean mapParticipantPreviewBean( Participant participant )
  {
    ParticipantPreviewBean participantPreviewBean = new ParticipantPreviewBean();
    participantPreviewBean.setId( participant.getId() );
    participantPreviewBean.setFirstName( participant.getFirstName() );
    participantPreviewBean.setLastName( participant.getLastName() );
    participantPreviewBean.setDepartmentName( participant.getDepartmentType() );
    participantPreviewBean.setOrgName( participant.getPaxOrgName() );
    participantPreviewBean.setJobName( participant.getPaxJobName() );
    participantPreviewBean.setCountryCode( participant.getPrimaryCountryCode() );
    participantPreviewBean.setCountryName( participant.getPrimaryCountryName() );
    return participantPreviewBean;
  }

}
