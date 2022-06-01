/*
 * (c) 2017 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.dao.ots.hibernate;

import com.biperf.core.dao.hibernate.BaseDAOTest;
import com.biperf.core.dao.ots.OTSProgramDAO;
import com.biperf.core.domain.ots.OTSBatch;
import com.biperf.core.domain.ots.OTSProgram;
import com.biperf.core.domain.ots.ProgramAudience;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.participant.PaxAudience;
import com.biperf.core.utils.ApplicationContextFactory;

/**
 * 
 * @author rajadura
 * @since Dec 6, 2017
 * 
 */
public class OTSProgramDAOImplTest extends BaseDAOTest
{

  private OTSProgramDAO getOTSProgramDAO()
  {
    return (OTSProgramDAO)ApplicationContextFactory.getApplicationContext().getBean( OTSProgramDAO.BEAN_NAME );
  }

  public void testSave()
  {
    OTSProgramDAO otsProgramDAO = getOTSProgramDAO();
    OTSProgram otsProgram = new OTSProgram();
    otsProgram.setId( 123456L );
    otsProgram.setClientName( "Test" );
    otsProgramDAO.save( otsProgram );

  }

  public void testSaveBatch()
  {
    OTSProgramDAO otsProgramDAO = getOTSProgramDAO();
    OTSBatch otsBatch = getOTSBatch();
    otsProgramDAO.saveBatch( otsBatch );

  }

  public void testGetOTSBatchByBatchNumber()
  {
    OTSBatch otsBatch = getOTSBatch();
    OTSBatch actual = getOTSProgramDAO().getOTSBatchByBatchNumber( otsBatch.getBatchNumber() );
    assertEquals( otsBatch, actual );
  }

  private OTSBatch getOTSBatch()
  {
    OTSProgram otsProgram = new OTSProgram();
    otsProgram.setId( new Long( 23 ) );
    otsProgram.setClientName( "Test" );

    OTSBatch otsBatch = new OTSBatch();
    otsBatch.setId( 123L );
    otsBatch.setBatchNumber( 123456L );
    otsBatch.setOtsProgram( otsProgram );
    return otsBatch;
  }

  public void testOTSProgramByProgramNumber()
  {
    OTSProgramDAO otsProgramDAO = getOTSProgramDAO();
    OTSProgram otsProgram = new OTSProgram();
    otsProgram.setId( 12346L );
    otsProgram.setClientName( "Test" );
    otsProgramDAO.save( otsProgram );

    OTSProgram actual = otsProgramDAO.getOTSProgramByProgramNumber( 12346L );
    assertEquals( otsProgram, actual );

  }

  public void testGetOTSProgramAudienceByProgramNumberAndAudienceId()
  {
    OTSProgram otsProgram = new OTSProgram();
    otsProgram.setProgramNumber( 1001L );
    otsProgram.setClientName( "Test" );

    OTSProgramDAO otsProgramDAO = getOTSProgramDAO();

    PaxAudience paxAudience = new PaxAudience();
    Participant testPax = new Participant();
    paxAudience.addParticipant( testPax );
    paxAudience.setId( 10250L );

    ProgramAudience programAudience = new ProgramAudience();
    programAudience.setOtsProgram( otsProgram );
    programAudience.setAudience( paxAudience );

    ProgramAudience actual = otsProgramDAO.getOTSProgramAudienceByProgramNumberAndAudienceId( 1001L, 10250L );
    assertEquals( programAudience, actual );
  }
}
