
package com.biperf.core.dao.throwdown.hibernate;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.biperf.core.dao.hibernate.BaseDAOTest;
import com.biperf.core.dao.throwdown.MatchTeamOutcomeDAO;
import com.biperf.core.dao.throwdown.MatchTeamProgressDAO;
import com.biperf.core.domain.AuditCreateInfo;
import com.biperf.core.domain.enums.ThrowdownMatchProgressType;
import com.biperf.core.domain.promotion.MatchTeamOutcome;
import com.biperf.core.domain.promotion.MatchTeamProgress;
import com.biperf.core.utils.ApplicationContextFactory;

public class MatchTeamProgressDAOImplTest extends BaseDAOTest
{

  public MatchTeamProgress buildMatchTeamProgress()
  {
    MatchTeamProgress matchTeamProgress = new MatchTeamProgress();
    MatchTeamOutcome matchTeamOutcome = MatchTeamOutcomeDAOImplTest.buildMatchTeamOutcomeWithDetails();
    getMatchTeamOutcomeDAO().save( matchTeamOutcome );
    matchTeamProgress.setTeamOutcome( matchTeamOutcome );
    matchTeamProgress.setProgressType( ThrowdownMatchProgressType.lookup( ThrowdownMatchProgressType.INCREMENTAL ) );
    matchTeamProgress.setProgress( new BigDecimal( 83473847 ) );
    AuditCreateInfo auditCreateInfo = new AuditCreateInfo();
    auditCreateInfo.setCreatedBy( new Long( 5662 ) );
    auditCreateInfo.setDateCreated( new Timestamp( new Date().getTime() ) );
    matchTeamProgress.setAuditCreateInfo( auditCreateInfo );
    matchTeamProgress.setVersion( new Long( 1 ) );

    return matchTeamProgress;
  }

  public void testSave()
  {
    MatchTeamProgress matchTeamProgress = buildMatchTeamProgress();
    MatchTeamProgress expectedMatchTeamProgress = getMatchTeamProgressDAO().save( matchTeamProgress );
    assertEquals( "Actual saved matchTeamProgress is equal to what was expected", matchTeamProgress, expectedMatchTeamProgress );
  }

  public void testGetProgressListByOutcome()
  {
    MatchTeamProgress matchTeamProgress = buildMatchTeamProgress();
    MatchTeamProgress savedMatchTeamProgress = getMatchTeamProgressDAO().save( matchTeamProgress );
    Long matchTeamOutcomeId = savedMatchTeamProgress.getTeamOutcome().getId();
    List<MatchTeamProgress> expectedProgressList = getMatchTeamProgressDAO().getProgressListByOutcome( matchTeamOutcomeId );
    assertTrue( "Expected progress list contains the actual matchTeamProgress", expectedProgressList.contains( savedMatchTeamProgress ) );
  }

  /**
   * Get the MatchTeamProgressDAO.
   * 
   * @return MatchTeamProgressDAO
   */
  private static MatchTeamProgressDAO getMatchTeamProgressDAO()
  {
    return (MatchTeamProgressDAO)ApplicationContextFactory.getApplicationContext().getBean( "matchTeamProgressDAO" );
  }

  /**
   * Get the MatchTeamOutcomeDAO.
   * 
   * @return MatchTeamOutcomeDAO
   */
  private static MatchTeamOutcomeDAO getMatchTeamOutcomeDAO()
  {
    return (MatchTeamOutcomeDAO)ApplicationContextFactory.getApplicationContext().getBean( "matchTeamOutcomeDAO" );
  }
}
