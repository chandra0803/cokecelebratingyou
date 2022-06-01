
package com.biperf.core.service.groups;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import com.biperf.core.dao.participant.ParticipantGroupDAO;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.participant.ParticipantGroup;
import com.biperf.core.domain.participant.ParticipantGroupDetails;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.ui.UnitTest;
import com.biperf.core.value.participant.ParticipantGroupList;
import com.biperf.core.value.participant.ParticipantGroupList.Group;
import com.google.common.collect.Sets;

@RunWith( MockitoJUnitRunner.class )
public class ParticipantGroupServiceImplTest extends UnitTest
{
  @InjectMocks
  ParticipantGroupService testObject = new ParticipantGroupServiceImpl();
  @Mock
  ParticipantGroupDAO participantGroupDAO;
  @Mock
  ParticipantService participantService;

  @Test
  public void test_getGroupDetailsByGroupId() throws Exception
  {
    ParticipantGroup participantGroup = new ParticipantGroup();
    participantGroup.setId( 123L );
    Participant participant = new Participant();
    participant.setId( 24L );
    participantGroup.setGroupCreatedBy( participant );
    when( participantGroupDAO.find( anyLong() ) ).thenReturn( participantGroup );
    testObject.getGroupDetailsByGroupId( 123L );
    verify( participantGroupDAO, atLeastOnce() ).find( 123L );
  }

  @Test
  public void test_getGroupDetailsByUserId() throws Exception
  {
    testObject.getGroupDetailsByUserId( 123L );
    verify( participantGroupDAO, atLeastOnce() ).findGroupsByPaxId( 123L );
  }

  @SuppressWarnings( "rawtypes" )
  @Test
  public void test_delete()
  {
    ParticipantGroup participantGroup = new ParticipantGroup();
    doAnswer( new Answer()
    {
      public Object answer( InvocationOnMock invocation )
      {
        participantGroup.setId( 12L );
        participantGroup.setGroupName( "test" );
        return participantGroup;
      }
    } ).when( participantGroupDAO ).find( 123L );
    testObject.delete( 123L );
    verify( participantGroupDAO, atLeastOnce() ).delete( anyLong() );
  }

  @Test
  public void test_createParticipantGroup() throws ServiceErrorException
  {
    ParticipantGroupList participantGroupList = new ParticipantGroupList();
    Group group = new Group();
    group.setId( 123L );
    group.setName( "test group" );
    group.setPaxCount( 12L );
    participantGroupList.addGroup( group );
    when( participantGroupDAO.findGroupsByPaxId( anyLong() ) ).thenReturn( participantGroupList );
    Participant participant = new Participant();
    participant.setId( 123L );
    when( participantService.getParticipantById( anyLong() ) ).thenReturn( participant );
    testObject.saveParticipantGroup( null, "new group creation", Arrays.asList( 1020L, 2030L ) );
    verify( participantGroupDAO, atLeastOnce() ).saveOrUpdate( anyObject() );
  }

  @Test
  public void test_updateParticipantGroup() throws ServiceErrorException
  {
    ParticipantGroupList participantGroupList = new ParticipantGroupList();
    Group group = new Group();
    group.setId( 123L );
    group.setName( "test group" );
    group.setPaxCount( 12L );
    participantGroupList.addGroup( group );
    when( participantGroupDAO.findGroupsByPaxId( anyLong() ) ).thenReturn( participantGroupList );
    Participant participant = new Participant();
    participant.setId( 123L );
    when( participantService.getParticipantById( anyLong() ) ).thenReturn( participant );
    ParticipantGroup participantGroup = new ParticipantGroup();
    ParticipantGroupDetails participantGroupDetails = new ParticipantGroupDetails();
    participantGroupDetails.setId( 2060L );
    participantGroup.setGroupDetails( Sets.newHashSet( participantGroupDetails ) );
    when( participantGroupDAO.find( 123L ) ).thenReturn( participantGroup );
    testObject.saveParticipantGroup( 123L, "test group", Arrays.asList( 1020L, 2030L ) );
    verify( participantGroupDAO, atLeastOnce() ).saveOrUpdate( anyObject() );
  }
}