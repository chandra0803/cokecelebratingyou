
package com.biperf.core.dao.workhappier.hibernate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.biperf.core.dao.hibernate.BaseDAOTest;
import com.biperf.core.dao.workhappier.WorkHappierDAO;
import com.biperf.core.domain.workhappier.WorkHappier;
import com.biperf.core.domain.workhappier.WorkHappierFeedback;
import com.biperf.core.domain.workhappier.WorkHappierScore;

public class WorkHappierDAOImplTest extends BaseDAOTest
{

  public void testGetWorkHappier()
  {
    List<WorkHappier> expectedList = getWorkHappier();

    List<WorkHappier> actualList = getWorkHappierDAO().getWorkHappier();

    assertTrue( "Actual list didn't contain expected list for getWorkHappier.", actualList.containsAll( expectedList ) );
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

  public void testGetWHScore()
  {
    Long userId = (long)5583;
    int numberOfScores = 5;
    List<WorkHappierScore> expectedList = getWHScore( userId );

    List<WorkHappierScore> actualList = getWorkHappierDAO().getWHScore( userId, numberOfScores );

    assertTrue( "Actual list didn't contain expected list for getWHScore.", actualList.containsAll( expectedList ) );
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
    expectedWHFeedback.setComments( "" );

    getWorkHappierDAO().saveFeedcack( expectedWHFeedback );

    WorkHappierFeedback actualWHFeedback = getWorkHappierDAO().getFeedcackById( new Long( 1 ) );

    assertEquals( "Actual WorkHappier feedback didn't match with what is expected", expectedWHFeedback, actualWHFeedback );

  }

  public void testSaveScore()
  {
    WorkHappierScore expectedWHScore = new WorkHappierScore();

    expectedWHScore.setId( (long)1 );
    expectedWHScore.setNodeId( (long)5001 );
    expectedWHScore.setUserId( (long)5583 );
    expectedWHScore.setScore( (long)75 );

    getWorkHappierDAO().saveScore( expectedWHScore );

    WorkHappierScore actualWHScore = getWorkHappierDAO().getScoreById( new Long( 1 ) );

    assertEquals( "Actual WorkHappier score didn't match with what is expected", expectedWHScore, actualWHScore );
  }

  private static WorkHappierDAO getWorkHappierDAO()
  {
    return (WorkHappierDAO)getDAO( WorkHappierDAO.BEAN_NAME );
  }
}
