/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javatest/com/biperf/core/dao/DAOSuite.java,v $
 */

package com.biperf.core.dao;

import com.biperf.core.dao.activity.ActivityDAOSuite;
import com.biperf.core.dao.audit.AuditDAOSuite;
import com.biperf.core.dao.awardgenerator.AwardGenBatchDAOSuit;
import com.biperf.core.dao.budget.BudgetDAOSuite;
import com.biperf.core.dao.calculator.CalculatorDAOSuite;
import com.biperf.core.dao.challengepoint.ChallengepointDAOSuite;
import com.biperf.core.dao.claim.ClaimDAOSuite;
import com.biperf.core.dao.commlog.CommLogDAOSuite;
import com.biperf.core.dao.country.CountryDAOSuite;
import com.biperf.core.dao.employer.EmployerDAOSuite;
import com.biperf.core.dao.fileload.ImportFileDAOSuite;
import com.biperf.core.dao.goalquest.GoalLevelDAOSuite;
import com.biperf.core.dao.goalquest.GoalQuestDAOSuite;
import com.biperf.core.dao.hierarchy.HierarchyDAOSuite;
import com.biperf.core.dao.integration.IntegrationDAOSuite;
import com.biperf.core.dao.journal.JournalDAOSuite;
import com.biperf.core.dao.mailing.MailingDAOSuite;
import com.biperf.core.dao.merchandise.MerchOrderDAOSuite;
import com.biperf.core.dao.message.MessageDAOSuite;
import com.biperf.core.dao.multimedia.MultimediaDAOSuite;
import com.biperf.core.dao.participant.ParticipantDAOSuite;
import com.biperf.core.dao.process.ProcessDAOSuite;
import com.biperf.core.dao.product.ProductDAOSuite;
import com.biperf.core.dao.promotion.PromotionDAOSuite;
import com.biperf.core.dao.proxy.ProxyDAOSuite;
import com.biperf.core.dao.purl.PurlContributorCommentDAOSuite;
import com.biperf.core.dao.purl.PurlContributorDAOSuite;
import com.biperf.core.dao.purl.PurlContributorMediaDAOSuite;
import com.biperf.core.dao.purl.PurlRecipientDAOSuite;
import com.biperf.core.dao.quicksearch.QuickSearchDAOSuite;
import com.biperf.core.dao.quiz.QuizDAOSuite;
import com.biperf.core.dao.security.SecurityDAOSuite;
import com.biperf.core.dao.ssi.SSIDAOSuite;
import com.biperf.core.dao.system.SystemDAOSuite;
import com.biperf.core.dao.throwdown.ThrowdownDAOSuite;
import com.biperf.core.dao.workhappier.WorkHappierDAOSuite;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * DAOSuite is a container for running all DAO suites out of container.
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
 * <td>Adam</td>
 * <td>Feb 23, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class DAOSuite extends TestSuite
{

  /**
   * suite to run all DAOTests.
   * 
   * @return Test
   */
  public static Test suite()
  {
    TestSuite suite = new TestSuite( "com.biperf.core.core.dao.DaoSuite" );

    suite.addTest( ActivityDAOSuite.suite() );
    suite.addTest( AuditDAOSuite.suite() );
    suite.addTest( MerchOrderDAOSuite.suite() );
    suite.addTest( BudgetDAOSuite.suite() );
    suite.addTest( CalculatorDAOSuite.suite() );
    suite.addTest( ClaimDAOSuite.suite() );
    suite.addTest( CountryDAOSuite.suite() );
    suite.addTest( EmployerDAOSuite.suite() );
    suite.addTest( GoalQuestDAOSuite.suite() );
    suite.addTest( GoalLevelDAOSuite.suite() );
    suite.addTest( HierarchyDAOSuite.suite() );
    suite.addTest( ImportFileDAOSuite.suite() );
    suite.addTest( IntegrationDAOSuite.suite() );
    suite.addTest( JournalDAOSuite.suite() );
    suite.addTest( MailingDAOSuite.suite() );
    suite.addTest( MessageDAOSuite.suite() );
    suite.addTest( MultimediaDAOSuite.suite() );
    suite.addTest( ParticipantDAOSuite.suite() );
    suite.addTest( ProductDAOSuite.suite() );
    suite.addTest( ProcessDAOSuite.suite() );
    suite.addTest( PromotionDAOSuite.suite() );
    suite.addTest( ProxyDAOSuite.suite() );
    suite.addTest( QuickSearchDAOSuite.suite() );
    suite.addTest( SecurityDAOSuite.suite() );
    suite.addTest( SystemDAOSuite.suite() );
    suite.addTest( ThrowdownDAOSuite.suite() );
    suite.addTest( QuizDAOSuite.suite() );
    suite.addTest( CommLogDAOSuite.suite() );
    suite.addTest( ChallengepointDAOSuite.suite() );
    suite.addTest( PurlRecipientDAOSuite.suite() );
    suite.addTest( PurlContributorDAOSuite.suite() );
    suite.addTest( PurlContributorCommentDAOSuite.suite() );
    suite.addTest( PurlContributorMediaDAOSuite.suite() );
    suite.addTest( SSIDAOSuite.suite() );
    suite.addTest( WorkHappierDAOSuite.suite() );
    suite.addTest( AwardGenBatchDAOSuit.suite() );
    return suite;
  }

}
