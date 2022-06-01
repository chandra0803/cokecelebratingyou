
package com.biperf.core.dao.awardgenerator.hibernate;

import java.util.Date;
import java.util.List;

import com.biperf.core.dao.awardgenerator.AwardGenBatchDAO;
import com.biperf.core.dao.awardgenerator.AwardGeneratorDAO;
import com.biperf.core.dao.hibernate.BaseDAOTest;
import com.biperf.core.domain.awardgenerator.AwardGenBatch;
import com.biperf.core.domain.awardgenerator.AwardGenerator;
import com.biperf.core.utils.ApplicationContextFactory;
import com.biperf.core.utils.HibernateSessionManager;

/**
 * AwardGenBatchDAOImplTest
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>chowdhur</td>
 * <td>Jul 20, 2013</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class AwardGenBatchDAOImplTest extends BaseDAOTest
{
  public void testGetAllAwardBatches()
  {
    List<AwardGenBatch> awardGenBatchs = getAwardGenBatchDAO().getAllAwardGenBatchs();

  }

  public void testSaveAwardGenBatch()
  {
    int count = 0;

    count = getAwardGenBatchDAO().getAllAwardGenBatchs().size();

    AwardGenBatch awardGenBatch = new AwardGenBatch();

    AwardGenerator awardGen = AwardGeneratorDAOImplTest.buildAwardGenerator();
    getAwardGeneratorDAO().saveAwardGenerator( awardGen );

    awardGenBatch.setAwardGen( awardGen );
    awardGenBatch.setStartDate( new Date() );
    awardGenBatch.setEndDate( new Date() );
    awardGenBatch.setUseIssueDate( Boolean.TRUE );
    awardGenBatch.setIssueDate( new Date() );

    AwardGenBatch savedAwardGenBatch = getAwardGenBatchDAO().save( awardGenBatch );

    HibernateSessionManager.getSession().flush();

    int newCount = 0;

    newCount = getAwardGenBatchDAO().getAllAwardGenBatchs().size();

    assertEquals( "List of AwardGenBatch aren't the same size.", count + 1, newCount );
  }

  private static AwardGenBatchDAO getAwardGenBatchDAO()
  {
    return (AwardGenBatchDAO)ApplicationContextFactory.getApplicationContext().getBean( "awardGenBatchDAO" );
  }

  private static AwardGeneratorDAO getAwardGeneratorDAO()
  {
    return (AwardGeneratorDAO)ApplicationContextFactory.getApplicationContext().getBean( "awardGeneratorDAO" );
  }

}
