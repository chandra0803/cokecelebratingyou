/*
 * Copyright 2005 BI, Inc. All rights reserved.
 */

package com.biperf.core.dao.quicksearch.hibernate;

import java.util.List;

import com.biperf.core.dao.hibernate.BaseDAOTest;
import com.biperf.core.dao.participant.ParticipantDAO;
import com.biperf.core.dao.participant.hibernate.ParticipantDAOImplTest;
import com.biperf.core.dao.quicksearch.QuickSearchDAO;
import com.biperf.core.domain.participant.Participant;

/**
 * @author crosenquest Apr 27, 2005
 */
public class QuickSearchDAOImplTest extends BaseDAOTest
{

  public static QuickSearchDAO getQuickSearchDAO()
  {
    return (QuickSearchDAO)getDAO( "quickSearchDAO" );
  }

  public static ParticipantDAO getParticipantDAO()
  {
    return (ParticipantDAO)getDAO( "participantDAO" );
  }

  public void testSearchPaxLastName()
  {
    Participant pax = ParticipantDAOImplTest.buildStaticParticipant();
    getParticipantDAO().saveParticipant( pax );
    Participant pax1 = getParticipantDAO().getParticipantById( pax.getId() );
    assertEquals( "testLASTNAME", pax.getLastName() );
    // List results = getQuickSearchDAO().searchByPage( "pax_lastname", "Italy", -1, false, 1, 40 );
    // assertTrue( results.size() == 1 );
  }

  public void testSearchPaxEmail()
  {
    // TODO: create the test data before searching for a pax; instead of relying of pre-existing
    // data
    List results = getQuickSearchDAO().searchByPage( "pax_email", "", -1, false, 1, 40 );
    assertFalse( results.isEmpty() );
  }

  public void testSearchPaxNodeName()
  {
    // TODO: create the test data before searching for a pax; instead of relying of pre-existing
    // data
    List results = getQuickSearchDAO().searchByPage( "pax_nodename", "", -1, false, 1, 40 );
    assertFalse( results.isEmpty() );
  }

  public void testSearchPaxSsn()
  {
    Participant pax = ParticipantDAOImplTest.buildStaticParticipant();

    getParticipantDAO().saveParticipant( pax );
    Participant pax1 = getParticipantDAO().getParticipantById( pax.getId() );
    assertEquals( "111223333", pax1.getSsn() );

  }

  public void testSearchPaxBanqNum()
  {
    Participant pax = ParticipantDAOImplTest.buildStaticParticipant();

    getParticipantDAO().saveParticipant( pax );
    Participant pax1 = getParticipantDAO().getParticipantById( pax.getId() );
    assertEquals( "testABN", pax1.getAwardBanqNumber() );

  }

  public void testSearchPaxUserId()
  {
    Participant pax = ParticipantDAOImplTest.buildStaticParticipant();

    getParticipantDAO().saveParticipant( pax );
    Participant pax1 = getParticipantDAO().getParticipantById( pax.getId() );
    assertTrue( pax.getUserName().toLowerCase().equals( pax1.getUserName().toLowerCase() ) );
  }

  public void testSearchPaxState()
  {
    // TODO: create the test data before searching for a pax; instead of relying of pre-existing
    // data
    List results = getQuickSearchDAO().searchByPage( "pax_state", "", -1, false, 1, 40 );
    assertFalse( results.isEmpty() );
  }

  public void testSearchPaxPostalCode()
  {
    // TODO: create the test data before searching for a pax; instead of relying of pre-existing
    // data
    List results = getQuickSearchDAO().searchByPage( "pax_postalcode", "", -1, false, 1, 40 );
    assertFalse( results.isEmpty() );
  }

  public void testSearchClaimOpenClosedStatus()
  {
    // TODO: create the test data before searching for a pax; instead of relying of pre-existing
    // data

    List results = getQuickSearchDAO().searchByPage( "claim_txstatus", "", -1, false, 1, 40 );
    assertTrue( results.isEmpty() );

    results = getQuickSearchDAO().searchByPage( "claim_txstatus", "open", -1, false, 1, 40 );
    assertTrue( results.isEmpty() );

    results = getQuickSearchDAO().searchByPage( "claim_txstatus", "closed", -1, false, 1, 40 );
    assertTrue( results.isEmpty() );

    results = getQuickSearchDAO().searchByPage( "claim_txstatus", "asdfasdfadf", -1, false, 1, 40 );
    assertTrue( results.isEmpty() );
  }

  public void testSearchClaimProductName()
  {
    // TODO: create the test data before searching for a pax; instead of relying of pre-existing
    // data
    List results = getQuickSearchDAO().searchByPage( "claim_product", "", -1, false, 1, 40 );
    assertTrue( results.isEmpty() );
  }

  public void testSearchClaimSubmitterLastName()
  {
    // TODO: create the test data before searching for a pax; instead of relying of pre-existing
    // data
    List results = getQuickSearchDAO().searchByPage( "claim_submitter", "", -1, false, 1, 40 );
    assertTrue( results.isEmpty() );
  }

  public void testSearchClaimTeamMemberLastName()
  {
    // TODO: create the test data before searching for a pax; instead of relying of pre-existing
    // data
    List results = getQuickSearchDAO().searchByPage( "claim_teammember", "", -1, false, 1, 40 );
    assertTrue( results.isEmpty() );
  }

  public void testSearchClaimClaimNum()
  {
    // TODO: create the test data before searching for a pax; instead of relying of pre-existing
    // data
    List results = getQuickSearchDAO().searchByPage( "claim_claimnum", "", -1, false, 1, 40 );
    assertTrue( results.isEmpty() );
  }

}
