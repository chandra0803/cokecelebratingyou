/*
 * (c) 2015 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/workhappier/WorkHappierFeedbackAction.java,v $
 */

package com.biperf.core.ui.workhappier;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.user.User;
import com.biperf.core.domain.workhappier.WorkHappierFeedback;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.workhappier.WorkHappierService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.utils.UserManager;

/**
 * 
 * @author poddutur
 * @since Dec 10, 2015
 */
public class WorkHappierFeedbackAction extends BaseDispatchAction
{
  @Override
  public ActionForward execute( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    WorkHappierFeedbackForm workHappierFeedbackForm = (WorkHappierFeedbackForm)form;

    Long userId = UserManager.getUserId();
    User user = getUserService().getUserById( userId );

    WorkHappierFeedback whFeedback = new WorkHappierFeedback();
    whFeedback.setComments( workHappierFeedbackForm.getMessage() );
    whFeedback.setNodeId( user.getPrimaryUserNode().getNode().getId() );

    try
    {
      getWorkHappierService().saveFeedcack( whFeedback );
    }
    catch( Exception e )
    {
      log.debug( e );
    }

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
