
package com.biperf.core.dao.workhappier;

import java.util.List;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.workhappier.WorkHappier;
import com.biperf.core.domain.workhappier.WorkHappierFeedback;
import com.biperf.core.domain.workhappier.WorkHappierScore;

public interface WorkHappierDAO extends DAO
{

  public static final String BEAN_NAME = "workHappierDAO";

  public List<WorkHappier> getWorkHappier();

  public WorkHappierScore saveScore( WorkHappierScore whScore );

  public List<WorkHappierScore> getWHScore( Long userId, int numberOfScores );

  public void saveFeedcack( WorkHappierFeedback whFeedback );

  public WorkHappierScore getScoreById( Long whScoreId );

  public WorkHappierFeedback getFeedcackById( Long whFeedbackId );

  public WorkHappier geWorkHappierById( Long workHappierId );

  public WorkHappier geWorkHappierByScore( Long score );

}
