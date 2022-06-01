/*
 * (c) 2015 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javatest/com/biperf/core/service/workhappier/impl/WorkHappierServiceImplTest.java,v $
 */

package com.biperf.core.service.workhappier.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jmock.Mock;

import com.biperf.core.dao.workhappier.WorkHappierDAO;
import com.biperf.core.domain.workhappier.WorkHappier;
import com.biperf.core.domain.workhappier.WorkHappierFeedback;
import com.biperf.core.domain.workhappier.WorkHappierScore;
import com.biperf.core.service.BaseServiceTest;

/**
 * 
 * @author poddutur
 * @since Nov 30, 2015
 */
public class WorkHappierServiceImplTest extends BaseServiceTest
{

  /**
   * Constructor to take the name of the test.
   * 
   * @param test
   */
  public WorkHappierServiceImplTest( String test )
  {
    super( test );
  }

  /** WorkHappierService */
  private WorkHappierServiceImpl workHappierServiceImpl = new WorkHappierServiceImpl();

  /** mockWorkHappierDAO */
  private Mock mockWorkHappierDAO = null;

  /**
   * Setup the test. Overridden from
   * 
   * @see junit.framework.TestCase#setUp()
   * @throws Exception
   */
  protected void setUp() throws Exception
  {
    super.setUp();
    mockWorkHappierDAO = new Mock( WorkHappierDAO.class );
    workHappierServiceImpl.setWorkHappierDAO( (WorkHappierDAO)mockWorkHappierDAO.proxy() );
  }

  public void testGetWorkHappier()
  {
    List<WorkHappier> expectedList = getWorkHappier();

    mockWorkHappierDAO.expects( once() ).method( "getWorkHappier" ).will( returnValue( expectedList ) );
    List<WorkHappier> actualList = workHappierServiceImpl.getWorkHappier();

    assertTrue( "Actual list didn't contain expected list for getWorkHappier.", actualList.containsAll( expectedList ) );

    mockWorkHappierDAO.verify();
  }

  private List<WorkHappier> getWorkHappier()
  {
    List<WorkHappier> workHappierList = new ArrayList<WorkHappier>();
    Set<WorkHappierScore> workHappierScoreSet = new HashSet<WorkHappierScore>();

    WorkHappierScore workHappierScore = new WorkHappierScore();
    workHappierScore.setId( (long)1 );
    workHappierScore.setUserId( (long)5583 );
    workHappierScore.setNodeId( (long)5001 );
    workHappierScore.setScore( (long)70 );
    workHappierScoreSet.add( workHappierScore );

    WorkHappier workHappier = new WorkHappier();
    workHappier.setId( (long)1 );
    workHappier.setWhAssetCode( "workhappier.data.1" );
    workHappier.setImageName( "image01.jpg" );
    workHappier.setMaxValue( (long)80 );
    workHappier.setMinValue( (long)66 );
    workHappier.setWorkHappierScores( workHappierScoreSet );
    workHappierList.add( workHappier );

    return workHappierList;
  }

  /*
   * public void testGeWHExpressionById() { Long expressionId = (long)1; WorkHappierExpression
   * expectedObj = geWHExpressionById( expressionId ); mockWorkHappierDAO.expects( once() ).method(
   * "geWHExpressionById" ).will( returnValue( expectedObj ) ); WorkHappierExpression actualObj =
   * workHappierServiceImpl.geWHExpressionById( expressionId ); assertTrue(
   * "Actual Object is equal to expected Object for geWHExpressionById", actualObj.equalsId(
   * expectedObj ) ); mockWorkHappierDAO.verify(); } private WorkHappierExpression
   * geWHExpressionById( Long expressionId ) { WorkHappierExpression workHappierExpression = new
   * WorkHappierExpression(); workHappierExpression.setId( expressionId );
   * workHappierExpression.setImageName( "image01.jpg" ); workHappierExpression.setScore( (long)70
   * ); return workHappierExpression; } public void testGetWHExpressions() {
   * List<WorkHappierExpression> expectedList = getWHExpressions(); mockWorkHappierDAO.expects(
   * once() ).method( "getWHExpressions" ).will( returnValue( expectedList ) );
   * List<WorkHappierExpression> actualList = workHappierServiceImpl.getWHExpressions(); assertTrue(
   * "Actual list didn't contain expected list for getWHExpressions.", actualList.containsAll(
   * expectedList ) ); mockWorkHappierDAO.verify(); } private List<WorkHappierExpression>
   * getWHExpressions() { List<WorkHappierExpression> workHappierExpressionList = new
   * ArrayList<WorkHappierExpression>(); WorkHappierExpression workHappierExpression1 = new
   * WorkHappierExpression(); workHappierExpression1.setId( (long)1 );
   * workHappierExpression1.setImageName( "image01.jpg" ); workHappierExpression1.setScore( (long)70
   * ); workHappierExpressionList.add( workHappierExpression1 ); WorkHappierExpression
   * workHappierExpression2 = new WorkHappierExpression(); workHappierExpression2.setId( (long)2 );
   * workHappierExpression2.setImageName( "image02.jpg" ); workHappierExpression2.setScore( (long)85
   * ); workHappierExpressionList.add( workHappierExpression2 ); return workHappierExpressionList; }
   */

  public void testGetWHScore()
  {
    Long userId = (long)5583;
    int numberOfScores = 5;
    List<WorkHappierScore> expectedList = getWHScore( userId );

    mockWorkHappierDAO.expects( once() ).method( "getWHScore" ).will( returnValue( expectedList ) );
    List<WorkHappierScore> actualList = workHappierServiceImpl.getWHScore( userId, numberOfScores );

    assertTrue( "Actual list didn't contain expected list for getWHScore.", actualList.containsAll( expectedList ) );

    mockWorkHappierDAO.verify();
  }

  public List<WorkHappierScore> getWHScore( Long userId )
  {
    List<WorkHappierScore> workHappierScoreList = new ArrayList<WorkHappierScore>();

    WorkHappierScore workHappierScore1 = new WorkHappierScore();

    workHappierScore1.setId( (long)1 );
    workHappierScore1.setNodeId( (long)5001 );
    workHappierScore1.setUserId( userId );
    workHappierScore1.setScore( (long)75 );
    workHappierScoreList.add( workHappierScore1 );

    WorkHappierScore workHappierScore2 = new WorkHappierScore();

    workHappierScore2.setId( (long)2 );
    workHappierScore2.setNodeId( (long)5001 );
    workHappierScore2.setUserId( userId );
    workHappierScore2.setScore( (long)85 );
    workHappierScoreList.add( workHappierScore2 );

    WorkHappierScore workHappierScore3 = new WorkHappierScore();

    workHappierScore3.setId( (long)3 );
    workHappierScore3.setNodeId( (long)5001 );
    workHappierScore3.setUserId( userId );
    workHappierScore3.setScore( (long)65 );
    workHappierScoreList.add( workHappierScore3 );

    WorkHappierScore workHappierScore4 = new WorkHappierScore();

    workHappierScore4.setId( (long)4 );
    workHappierScore4.setNodeId( (long)5001 );
    workHappierScore4.setUserId( userId );
    workHappierScore4.setScore( (long)55 );
    workHappierScoreList.add( workHappierScore4 );

    WorkHappierScore workHappierScore5 = new WorkHappierScore();

    workHappierScore5.setId( (long)5 );
    workHappierScore5.setNodeId( (long)5001 );
    workHappierScore5.setUserId( userId );
    workHappierScore5.setScore( (long)95 );
    workHappierScoreList.add( workHappierScore5 );

    return workHappierScoreList;
  }

  public void testSaveFeedBack()
  {

    WorkHappierFeedback expectedWHFeedback = new WorkHappierFeedback();
    expectedWHFeedback.setId( (long)1 );
    expectedWHFeedback.setNodeId( (long)5001 );
    expectedWHFeedback.setComments( "comment" );

    mockWorkHappierDAO.expects( once() ).method( "saveFeedcack" ).with( same( expectedWHFeedback ) );

    workHappierServiceImpl.saveFeedcack( expectedWHFeedback );

    mockWorkHappierDAO.verify();
  }

  public void testSaveScore()
  {
    WorkHappierScore expectedWHScore = new WorkHappierScore();
    expectedWHScore.setId( (long)1 );
    expectedWHScore.setNodeId( (long)5001 );
    expectedWHScore.setUserId( (long)5583 );
    expectedWHScore.setScore( (long)75 );

    mockWorkHappierDAO.expects( once() ).method( "saveScore" ).with( same( expectedWHScore ) );

    workHappierServiceImpl.saveScore( expectedWHScore );

    mockWorkHappierDAO.verify();
  }
}
