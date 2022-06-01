
package com.biperf.core.ui.reports.participantsearch;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.biperf.core.domain.participant.Participant;
import com.biperf.core.service.hierarchy.NodeService;
import com.biperf.core.ui.search.ParticipantAutoCompleteSearchAction;
import com.biperf.core.utils.UserManager;

public class ParticipantSearchReportAction extends ParticipantAutoCompleteSearchAction
{

  @Override
  protected List<Participant> filterUsers( List<Participant> participants, HttpServletRequest request )
  {
    Long userId = UserManager.getUserId();

    // List<FormattedValueBean> nodeList = getNodeService().getUserNodesAndBelow( userId );
    // filter paxes
    for ( Iterator paxIter = participants.iterator(); paxIter.hasNext(); )
    {
      Participant pax = (Participant)paxIter.next();
      boolean shouldRemove = false;
    }

    return participants;
  }

  protected NodeService getNodeService()
  {
    return (NodeService)getService( NodeService.BEAN_NAME );
  }

}
