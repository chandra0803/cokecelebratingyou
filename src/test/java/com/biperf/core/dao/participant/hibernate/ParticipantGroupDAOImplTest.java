/*
 * Copyright 2005 BI, Inc. All rights reserved.
 */

package com.biperf.core.dao.participant.hibernate;

import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import com.biperf.core.dao.hibernate.BaseDAOTest;
import com.biperf.core.dao.participant.ParticipantDAO;
import com.biperf.core.dao.participant.ParticipantGroupDAO;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.participant.ParticipantGroup;
import com.biperf.core.domain.participant.ParticipantGroupDetails;
import com.biperf.core.utils.ApplicationContextFactory;

/**
 * @author crosenquest Apr 27, 2005
 */
public class ParticipantGroupDAOImplTest extends BaseDAOTest
{
  @Test
  public void testDelete()
  {
    Participant owner = getParticipantDAO().saveParticipant( buildPax( "owner" ) );
    ParticipantGroup pg = buildParticipantGroup( owner, "Group1 Test" );
    getParticipantGroupDAO().saveOrUpdate( pg );

    Long particitipantGroupId = pg.getId();
    getParticipantGroupDAO().delete( particitipantGroupId );

    pg = getParticipantGroupDAO().find( particitipantGroupId );
    assertNull( pg );
  }

  @Test
  public void testInsert()
  {
    // create owner
    Participant owner = getParticipantDAO().saveParticipant( buildPax( "owner" ) );
    ParticipantGroup pg = buildParticipantGroup( owner, "Group1 Test" );
    getParticipantGroupDAO().saveOrUpdate( pg );
    assertNotNull( pg.getId() );
    assertNotNull( pg.getGroupDetails() );
    assertTrue( pg.getGroupDetails().size() == 1 );
  }

  @Test
  public void testFindCountByName()
  {
    // create owner
    Participant owner = getParticipantDAO().saveParticipant( buildPax( "owner" ) );
    ParticipantGroup g1 = buildParticipantGroup( owner, "Group 1 blakdjl " );
    ParticipantGroup g2 = buildParticipantGroup( owner, "Group 2 lmasdfds" );
    getParticipantGroupDAO().saveOrUpdate( g1 );
    getParticipantGroupDAO().saveOrUpdate( g2 );
    int count = getParticipantGroupDAO().findGroupCountByUserIdAndStartsWith( owner.getId(), "group " );
    assertTrue( count == 2 );
  }

  @Test
  public void testFindGroupsByUserIdAndStartsWithSorted()
  {
    Participant owner = getParticipantDAO().saveParticipant( buildPax( "owner" ) );
    ParticipantGroup g1 = buildParticipantGroup( owner, "Group 1 blakdjl" );
    ParticipantGroup g2 = buildParticipantGroup( owner, "Group E lmasdfds" );
    ParticipantGroup g3 = buildParticipantGroup( owner, "Group 4 lmasdfds" );
    ParticipantGroup g4 = buildParticipantGroup( owner, "Group a blakdjl" );
    getParticipantGroupDAO().saveOrUpdate( g1 );
    getParticipantGroupDAO().saveOrUpdate( g2 );
    getParticipantGroupDAO().saveOrUpdate( g3 );
    getParticipantGroupDAO().saveOrUpdate( g4 );
    List<ParticipantGroup> groups = getParticipantGroupDAO().findGroupsByUserIdAndStartsWith( owner.getId(), "Group" );
    assertNotNull( groups );
    assertTrue( groups.size() == 4 );
    assertTrue( groups.get( 0 ).getGroupName().equalsIgnoreCase( "Group 1 blakdjl" ) );
    assertTrue( groups.get( 1 ).getGroupName().equalsIgnoreCase( "Group 4 lmasdfds" ) );
    assertTrue( groups.get( 2 ).getGroupName().equalsIgnoreCase( "Group A blakdjl" ) );
    assertTrue( groups.get( 3 ).getGroupName().equalsIgnoreCase( "Group e lmasdfds" ) );
  }

  @Test
  public void testFindGroupsByUserIdAndStartsWith()
  {
    Participant owner = getParticipantDAO().saveParticipant( buildPax( "owner" ) );
    ParticipantGroup g1 = buildParticipantGroup( owner, "Group 1 blakdjl " );
    ParticipantGroup g2 = buildParticipantGroup( owner, "Group 2 lmasdfds" );
    getParticipantGroupDAO().saveOrUpdate( g1 );
    getParticipantGroupDAO().saveOrUpdate( g2 );
    List<ParticipantGroup> groups = getParticipantGroupDAO().findGroupsByUserIdAndStartsWith( owner.getId(), "Group 1" );
    assertNotNull( groups );
    assertTrue( groups.size() == 1 );
  }

  @Test
  public void testFindGroupsByUserIdAndStartsWithMixedCase()
  {
    Participant owner = getParticipantDAO().saveParticipant( buildPax( "owner" ) );
    ParticipantGroup g1 = buildParticipantGroup( owner, "Group 1 blakdjl " );
    ParticipantGroup g2 = buildParticipantGroup( owner, "GrouP 2 lmasdfds" );
    getParticipantGroupDAO().saveOrUpdate( g1 );
    getParticipantGroupDAO().saveOrUpdate( g2 );
    List<ParticipantGroup> groups = getParticipantGroupDAO().findGroupsByUserIdAndStartsWith( owner.getId(), "group 2 L" );
    assertNotNull( groups );
    assertTrue( groups.size() == 1 );
  }

  protected ParticipantGroup buildParticipantGroup( Participant owner, String groupName )
  {
    // members
    Participant g1 = getParticipantDAO().saveParticipant( buildPax( RandomStringUtils.random( 10 ) ) );

    ParticipantGroup pg = new ParticipantGroup();
    pg.setGroupName( groupName );
    pg.setGroupCreatedBy( owner );
    ParticipantGroupDetails details1 = new ParticipantGroupDetails();
    details1.setGroup( pg );
    details1.setParticipant( g1 );
    pg.getGroupDetails().add( details1 );

    return pg;
  }

  protected Participant buildPax( String username )
  {
    return ParticipantDAOImplTest.buildUniqueParticipant( username );
  }

  protected ParticipantGroupDAO getParticipantGroupDAO()
  {
    return (ParticipantGroupDAO)ApplicationContextFactory.getApplicationContext().getBean( ParticipantGroupDAO.BEAN_NAME );
  }

  public static ParticipantDAO getParticipantDAO()
  {
    return (ParticipantDAO)getDAO( "participantDAO" );
  }
}
