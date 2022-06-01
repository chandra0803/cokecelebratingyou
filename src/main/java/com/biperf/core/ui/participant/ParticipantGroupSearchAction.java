
package com.biperf.core.ui.participant;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.participant.ParticipantGroupList;

public class ParticipantGroupSearchAction extends BaseDispatchAction
{

  public ActionForward getGroupList( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response ) throws IOException
  {
    ParticipantGroupList groupList = getParticipantService().getGroupList( UserManager.getUserId() );
    writeAsJsonToResponse( groupList, response );
    return null;
  }

  private ParticipantService getParticipantService()
  {
    return (ParticipantService)getService( ParticipantService.BEAN_NAME );
  }
}
