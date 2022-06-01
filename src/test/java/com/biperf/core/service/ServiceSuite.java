/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javatest/com/biperf/core/service/ServiceSuite.java,v $
 */

package com.biperf.core.service;

import com.biperf.core.service.activity.ActivityServiceSuite;
import com.biperf.core.service.audit.AuditServiceSuite;
import com.biperf.core.service.awardbanq.AwardBanqServiceSuite;
import com.biperf.core.service.budget.BudgetServiceSuite;
import com.biperf.core.service.challengepoint.ChallengepointServiceSuite;
import com.biperf.core.service.claim.ClaimServiceSuite;
import com.biperf.core.service.commlog.CommLogServiceSuite;
import com.biperf.core.service.country.CountryServiceSuite;
import com.biperf.core.service.email.EmailNotificationServiceSuite;
import com.biperf.core.service.email.MailingServiceSuite;
import com.biperf.core.service.employer.EmployerServiceSuite;
import com.biperf.core.service.fileload.FileloadServiceSuite;
import com.biperf.core.service.goalquest.GoalQuestServiceSuite;
import com.biperf.core.service.help.ContactUsServiceSuite;
import com.biperf.core.service.hierarchy.HierarchyServiceSuite;
import com.biperf.core.service.home.HomePageContentServiceSuite;
import com.biperf.core.service.integration.IntegrationServiceSuite;
import com.biperf.core.service.jms.JmsServiceSuite;
import com.biperf.core.service.journal.JournalServiceSuite;
import com.biperf.core.service.maincontent.MainContentServiceSuite;
import com.biperf.core.service.merchlevel.MerchLevelServiceSuite;
import com.biperf.core.service.message.MessageServiceSuite;
import com.biperf.core.service.oracle.OracleServiceSuite;
import com.biperf.core.service.participant.ParticipantServiceSuite;
import com.biperf.core.service.process.ProcessServiceSuite;
import com.biperf.core.service.product.ProductServiceSuite;
import com.biperf.core.service.promotion.PromotionServiceSuite;
import com.biperf.core.service.promotion.engine.PromotionEngineServicesSuite;
import com.biperf.core.service.proxy.ProxyServiceSuite;
import com.biperf.core.service.purl.PurlServiceSuite;
import com.biperf.core.service.quiz.QuizServiceSuite;
import com.biperf.core.service.security.SecurityServiceSuite;
import com.biperf.core.service.shopping.ShoppingServiceSuite;
import com.biperf.core.service.system.SystemServiceSuite;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Service test suite for running all service tests out of container.
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
 * <td>Feb 28, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ServiceSuite extends TestSuite
{

  /**
   * Service Test Suite
   * 
   * @return Test
   */
  public static Test suite()
  {
    TestSuite suite = new TestSuite( "com.biperf.core.service.ServiceSuite" );

    suite.addTest( ActivityServiceSuite.suite() );
    suite.addTest( AuditServiceSuite.suite() );
    suite.addTest( AwardBanqServiceSuite.suite() );
    suite.addTest( BudgetServiceSuite.suite() );
    suite.addTest( ClaimServiceSuite.suite() );
    suite.addTest( CommLogServiceSuite.suite() );
    suite.addTest( ContactUsServiceSuite.suite() );
    suite.addTest( CountryServiceSuite.suite() );
    suite.addTest( EmailNotificationServiceSuite.suite() );
    suite.addTest( EmployerServiceSuite.suite() );
    suite.addTest( FileloadServiceSuite.suite() );
    suite.addTest( HomePageContentServiceSuite.suite() );
    suite.addTest( JmsServiceSuite.suite() );
    suite.addTest( JournalServiceSuite.suite() );
    suite.addTest( HierarchyServiceSuite.suite() );
    suite.addTest( IntegrationServiceSuite.suite() );
    suite.addTest( MailingServiceSuite.suite() );
    suite.addTest( MainContentServiceSuite.suite() );
    suite.addTest( MerchLevelServiceSuite.suite() );
    suite.addTest( MessageServiceSuite.suite() );
    suite.addTest( OracleServiceSuite.suite() );
    suite.addTest( ParticipantServiceSuite.suite() );
    suite.addTest( ProductServiceSuite.suite() );
    suite.addTest( PromotionEngineServicesSuite.suite() );
    suite.addTest( PromotionServiceSuite.suite() );
    suite.addTest( ProcessServiceSuite.suite() );
    suite.addTest( ProxyServiceSuite.suite() );
    suite.addTest( QuizServiceSuite.suite() );
    suite.addTest( SecurityServiceSuite.suite() );
    suite.addTest( ShoppingServiceSuite.suite() );
    suite.addTest( SystemServiceSuite.suite() );
    suite.addTest( ChallengepointServiceSuite.suite() );
    suite.addTest( PurlServiceSuite.suite() );
    suite.addTest( GoalQuestServiceSuite.suite() );
    // suite.addTest( FacebookServiceSuite.suite() );
    // suite.addTest( TwitterServiceSuite.suite() );
    return suite;
  }
}