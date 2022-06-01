
package com.biperf.core.service.goalquest;

import com.biperf.core.service.goalquest.impl.GenerateAndMailExtractReportTest;
import com.biperf.core.service.goalquest.impl.GetEligibleParticipantsForPromotionTest;
import com.biperf.core.service.goalquest.impl.GetGoalQuestAwardSummaryByPromotionIdTest;
import com.biperf.core.service.goalquest.impl.GetGoalQuestMiniProfilesForUserAndPromotionTest;
import com.biperf.core.service.goalquest.impl.GetGoalQuestProgressByPromotionIdAndUserIdTest;
import com.biperf.core.service.goalquest.impl.IsParticipantInNodeTypeTest;
import com.biperf.core.service.goalquest.impl.IsParticipantInUserCharTypeTest;

import junit.framework.Test;
import junit.framework.TestSuite;

public class GoalQuestServiceSuite extends TestSuite
{
  public static Test suite()
  {
    TestSuite suite = new TestSuite( "com.biperf.core.service.goalquest.GoalQuestServiceSuite" );
    suite.addTestSuite( GenerateAndMailExtractReportTest.class );
    suite.addTestSuite( GetEligibleParticipantsForPromotionTest.class );
    suite.addTestSuite( GetGoalQuestAwardSummaryByPromotionIdTest.class );
    suite.addTestSuite( GetGoalQuestMiniProfilesForUserAndPromotionTest.class );
    suite.addTestSuite( GetGoalQuestProgressByPromotionIdAndUserIdTest.class );
    suite.addTestSuite( IsParticipantInNodeTypeTest.class );
    suite.addTestSuite( IsParticipantInUserCharTypeTest.class );
    return suite;
  }
}
