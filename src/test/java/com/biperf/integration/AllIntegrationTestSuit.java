
package com.biperf.integration;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.biperf.core.service.participant.impl.AutoCompleteServiceImplIntegTest;
import com.biperf.integration.service.claim.nom.NominationClaimServiceIntegTest;
import com.biperf.integration.service.promotion.NominationPromotionServiceIntegTest;
import com.biperf.integration.ui.claim.nom.NominationInProgressActionIntegTest;

@RunWith( Suite.class )
@SuiteClasses( { NominationPromotionServiceIntegTest.class, NominationClaimServiceIntegTest.class, NominationInProgressActionIntegTest.class, AutoCompleteServiceImplIntegTest.class } )
public class AllIntegrationTestSuit
{

  @BeforeClass
  public static void setUpClass()
  {
    System.out.println( "Before IntegrationTestSuit starts  *************** " );

  }

  @AfterClass
  public static void tearDownClass()
  {
    System.out.println( "End IntegrationTestSuit  ****************" );
  }

}
