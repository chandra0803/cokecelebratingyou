
package com.biperf.core.service.workhappier.impl;

import java.util.List;

import com.biperf.core.dao.workhappier.WorkHappierDAO;
import com.biperf.core.domain.homepage.ModuleApp;
import com.biperf.core.domain.participant.Audience;
import com.biperf.core.domain.workhappier.WorkHappier;
import com.biperf.core.domain.workhappier.WorkHappierFeedback;
import com.biperf.core.domain.workhappier.WorkHappierScore;
import com.biperf.core.service.participant.AudienceService;
import com.biperf.core.service.workhappier.WorkHappierService;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.UserManager;

public class WorkHappierServiceImpl implements WorkHappierService
{

  private WorkHappierDAO workHappierDAO;

  @Override
  public List<WorkHappier> getWorkHappier()
  {
    return workHappierDAO.getWorkHappier();
  }

  @Override
  public WorkHappierScore saveScore( WorkHappierScore whScore )
  {
    return workHappierDAO.saveScore( whScore );
  }

  @Override
  public WorkHappierScore getScoreById( Long whScoreId )
  {
    return workHappierDAO.getScoreById( whScoreId );
  }

  @Override
  public List<WorkHappierScore> getWHScore( Long userId, int numberOfScores )
  {
    return workHappierDAO.getWHScore( userId, numberOfScores );
  }

  @Override
  public void saveFeedcack( WorkHappierFeedback whFeedback )
  {
    workHappierDAO.saveFeedcack( whFeedback );
  }

  @Override
  public WorkHappierFeedback getFeedcackById( Long whFeedbackId )
  {
    return workHappierDAO.getFeedcackById( whFeedbackId );
  }

  @Override
  public WorkHappier geWorkHappierById( Long workHappierId )
  {
    return workHappierDAO.geWorkHappierById( workHappierId );
  }

  @Override
  public WorkHappier geWorkHappierByScore( Long score )
  {
    return workHappierDAO.geWorkHappierByScore( score );
  }

  public WorkHappierDAO getWorkHappierDAO()
  {
    return workHappierDAO;
  }

  public void setWorkHappierDAO( WorkHappierDAO workHappierDAO )
  {
    this.workHappierDAO = workHappierDAO;
  }

  @Override
  public boolean isWorkHappierAudience( ModuleApp moduleApp )
  {
    if ( moduleApp.getAudienceType().isNoneAudienceType() )
    {
      return false;
    }

    if ( moduleApp.getAudienceType().isAllActivePaxType() )
    {
      return true;
    }

    if ( moduleApp.getAudiences() != null )
    {
      for ( Audience audience : moduleApp.getAudiences() )
      {
        if ( getAudienceService().isParticipantInAudience( UserManager.getUser().getUserId(), audience ) )
        {
          return true;
        }
      }
    }
    return false;
  }

  private AudienceService getAudienceService()
  {
    return (AudienceService)BeanLocator.getBean( AudienceService.BEAN_NAME );
  }

}
