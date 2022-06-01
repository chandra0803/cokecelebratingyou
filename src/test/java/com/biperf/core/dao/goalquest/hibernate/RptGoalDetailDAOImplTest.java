/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/dao/goalquest/hibernate/RptGoalDetailDAOImplTest.java,v $
 */

package com.biperf.core.dao.goalquest.hibernate;

import com.biperf.core.dao.goalquest.RptGoalDetailDAO;
import com.biperf.core.dao.hibernate.BaseDAOTest;
import com.biperf.core.dao.participant.hibernate.ParticipantDAOImplTest;
import com.biperf.core.dao.promotion.hibernate.PromotionDAOImplTest;
import com.biperf.core.domain.calculator.Calculator;
import com.biperf.core.domain.goalquest.RptGoalDetail;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.utils.ApplicationContextFactory;

/**
 * RptGoalDetailDAOImplTest.
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
 * <td>sedey</td>
 * <td>Feb 27, 2007</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class RptGoalDetailDAOImplTest extends BaseDAOTest
{
  /**
   * Tests getting a list of rptGoalDetails.
   */
  public void testGetRptGoalDetails()
  {
    // create a list of saved RptGoalDetails
    Participant participant = ParticipantDAOImplTest.buildAndSaveParticipant( "paxToTest" );
    // GoalQuestPromotion promotion = PromotionDAOImplTest.buildGoalQuestPromotion( "testPromo" );
    GoalQuestPromotion promotion = (GoalQuestPromotion)PromotionDAOImplTest.getSavedGoalQuestPromotionForTesting( "testPromo" );
    RptGoalDetail expectedRptGoalDetail = new RptGoalDetail();
    expectedRptGoalDetail.setFirstName( "First Name" );
    expectedRptGoalDetail.setLastName( "Last Name" );
    expectedRptGoalDetail.setParticipant( participant );
    expectedRptGoalDetail.setGoalQuestPromotion( promotion );

    RptGoalDetail savedRptGoalDetail = this.getRptGoalDetailDAO().saveRptGoalDetail( expectedRptGoalDetail );
  }

  /**
   * Build a calculator with the uniqueString param and saves it for testing.
   * 
   * @param uniqueString
   * @return Calculator
   */
  private Calculator getSavedCalculator( String uniqueString )
  {
    // return getCalculatorDAO().saveCalculator( buildCalculator( uniqueString ) );
    return null;
  }

  /**
   * Builds a static calculator for testing.
   * 
   * @param uniqueString
   * @return calculator
   */
  public static Calculator buildRptGoalDetail( String uniqueString )
  {
    /*
     * RptGoalDetail rptGoalDetail = new RptGoalDetail(); Participant pax =
     * ParticipantDAOImplTest.buildUniqueParticipant( uniqueString ); GoalQuestPromotion
     * goalQuestPromotion= PromotionDAOImplTest.buildGoalQuestPromotion( "GOALQUEST" + uniqueString
     * ); calculator.addCalculatorCriterion( buildCalculatorCriterion( "1-Criterion-" + uniqueString
     * ) ); calculator.addCalculatorCriterion( buildCalculatorCriterion( "2-Criterion-" +
     * uniqueString ) ); calculator.addCalculatorCriterion( buildCalculatorCriterion( "3-Criterion-"
     * + uniqueString ) ); calculator.addCalculatorPayout( buildCalculatorPayout(1, 3, 5, 10 ) );
     * calculator.addCalculatorPayout( buildCalculatorPayout(4, 6, 11, 20 ) );
     * calculator.addCalculatorPayout( buildCalculatorPayout(7, 10, 21, 30 ) ); return calculator;
     */
    return null;
  }

  /**
   * Get the RptGoalDetailDAO.
   * 
   * @return RptGoalDetailDAO
   */
  private RptGoalDetailDAO getRptGoalDetailDAO()
  {
    return (RptGoalDetailDAO)ApplicationContextFactory.getApplicationContext().getBean( "rptGoalDetailDAO" );
  }

}
