/*
 * (c) 2015 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/workhappier/WorkHappierAction.java,v $
 */

package com.biperf.core.ui.workhappier;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.user.User;
import com.biperf.core.domain.workhappier.WorkHappier;
import com.biperf.core.domain.workhappier.WorkHappierScore;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.workhappier.WorkHappierService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.UserManager;

/**
 * 
 * @author poddutur
 */
public class WorkHappierAction extends BaseDispatchAction
{
  private static final int numberOfScores = 6;
  private static final String WH_IMAGES_PATH = "/assets/img/";

  /**
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward(response put to stream for the ajax call)
   */
  public ActionForward fetchWorkHappierPastResults( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    Long score = Math.round( Double.parseDouble( request.getParameter( "value" ) ) );
    WorkHappier workHappier = getWorkHappierService().geWorkHappierByScore( score );
    WhPastResultsViewBean whPastResultsViewBean = new WhPastResultsViewBean();
    List<WorkHappierScoreViewBean> pastResults = new ArrayList<WorkHappierScoreViewBean>();
    WorkHappierScore whScore = new WorkHappierScore();
    User user = getUserService().getUserById( UserManager.getUserId() );
    whScore.setUserId( user.getId() );
    whScore.setNodeId( user.getPrimaryUserNode().getNode().getId() );
    whScore.setScore( score );
    whScore.setWorkHappier( workHappier );

    try
    {
      WorkHappierScore savedWhScore = getWorkHappierService().saveScore( whScore );
      List<WorkHappierScore> workHappierScoreList = getWorkHappierService().getWHScore( UserManager.getUserId(), numberOfScores );

      for ( WorkHappierScore workHappierScore : workHappierScoreList )
      {
        if ( savedWhScore.getId() != null && !savedWhScore.getId().equals( workHappierScore.getId() ) )
        {
          WorkHappierScoreViewBean workHappierScoreViewBean = new WorkHappierScoreViewBean();
          workHappierScoreViewBean.setImgUrl( RequestUtils.getBaseURI( request ) + WH_IMAGES_PATH + workHappierScore.getWorkHappier().getImageName() );
          workHappierScoreViewBean.setDate( DateUtils.toDisplayString( workHappierScore.getAuditCreateInfo().getDateCreated() ) );
          workHappierScoreViewBean.setMood( workHappierScore.getWorkHappier().getFeelingFromCM() );
          pastResults.add( workHappierScoreViewBean );
        }
      }
      whPastResultsViewBean.setPastResults( pastResults );
    }
    catch( Exception e )
    {
      log.debug( e );
    }
    super.writeAsJsonToResponse( whPastResultsViewBean, response );
    return null;
  }

  private WorkHappierService getWorkHappierService()
  {
    return (WorkHappierService)getService( WorkHappierService.BEAN_NAME );
  }

  private UserService getUserService()
  {
    return (UserService)getService( UserService.BEAN_NAME );
  }
}
