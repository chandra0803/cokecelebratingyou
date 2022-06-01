
package com.biperf.core.service.workhappier;

import java.util.List;

import com.biperf.core.domain.homepage.ModuleApp;
import com.biperf.core.domain.workhappier.WorkHappier;
import com.biperf.core.domain.workhappier.WorkHappierFeedback;
import com.biperf.core.domain.workhappier.WorkHappierScore;
import com.biperf.core.service.SAO;

public interface WorkHappierService extends SAO
{
  public final String BEAN_NAME = "workHappierService";

  public List<WorkHappier> getWorkHappier();

  public WorkHappierScore saveScore( WorkHappierScore whScore );

  public WorkHappierScore getScoreById( Long whScoreId );

  public List<WorkHappierScore> getWHScore( Long userId, int numberOfScores );

  public void saveFeedcack( WorkHappierFeedback whFeedback );

  public WorkHappierFeedback getFeedcackById( Long whFeedbackId );

  public WorkHappier geWorkHappierById( Long workHappierId );

  public WorkHappier geWorkHappierByScore( Long score );

  public boolean isWorkHappierAudience( ModuleApp moduleApp );

}
