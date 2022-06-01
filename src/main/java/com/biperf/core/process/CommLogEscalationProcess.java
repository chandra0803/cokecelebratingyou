/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/process/CommLogEscalationProcess.java,v $
 */

package com.biperf.core.process;

import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.commlog.CommLog;
import com.biperf.core.domain.enums.HierarchyRoleType;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserNode;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.commlog.CommLogAssociationRequest;
import com.biperf.core.service.commlog.CommLogService;
import com.biperf.core.service.hierarchy.NodeService;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.participant.UserAssociationRequest;
import com.biperf.core.service.system.SystemVariableService;

/**
 * This process is to escalate any open comm log to the the users in the escalation hierarchy if the
 * escalation time has exceeded for the reason a comm log has been created.
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
 * <td>sathish</td>
 * <td>Dec 1, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class CommLogEscalationProcess extends BaseProcessImpl
{
  private static final Log log = LogFactory.getLog( CommLogEscalationProcess.class );

  public static final String BEAN_NAME = "commLogEscalationProcess";
  public static final String ESCALATED_TO_MESSAGE_NAME = "Escalated Comm Log Email:New Assigned User";
  public static final String ESCALATED_FROM_MESSAGE_NAME = "Escalated Comm Log:Original Assigned User";

  private CommLogService commLogService;
  private NodeService nodeService;
  private SystemVariableService systemVariableService;

  /**
   * Overridden from
   * 
   * @see com.biperf.core.process.BaseProcessImpl#onExecute()
   */
  public void onExecute()
  {
    AssociationRequestCollection reqCollection = new AssociationRequestCollection();
    reqCollection.add( new CommLogAssociationRequest( CommLogAssociationRequest.ALL ) );
    // get fully populated because they may be saved later
    List commLogs = getCommLogService().getAllOpenCommLogs( reqCollection );
    if ( commLogs.isEmpty() )
    {
      log.debug( "No open CommLogs to process." );
      addComment( "No open CommLogs to process at this time." );
    }
    else
    {
      int count = 0;
      for ( Iterator iter = commLogs.iterator(); iter.hasNext(); )
      {
        CommLog commLog = (CommLog)iter.next();
        Integer hours = commLog.getCommLogReasonType().getEscalationHours();
        if ( hours != null && hours.intValue() > 0 )
        {
          long curTime = System.currentTimeMillis();
          long prevCommTime = commLog.getDateInitiated().getTime();
          if ( commLog.getDateEscalated() != null )
          {
            prevCommTime = commLog.getDateEscalated().getTime();
          }
          if ( curTime - prevCommTime > hours.intValue() * 3600000 )
          {
            Long hierarchyId = new Long( getSystemVariableService().getPropertyByName( SystemVariableService.ESCALATION_HIERARCHY_ID ).getStringVal() );
            List hierarchy = getNodeService().getNodesAsHierarchy( hierarchyId );
            AssociationRequestCollection requestCollection = new AssociationRequestCollection();
            requestCollection.add( new UserAssociationRequest( UserAssociationRequest.EMAIL ) );
            requestCollection.add( new UserAssociationRequest( UserAssociationRequest.NODE ) );
            User curUser = getUserService().getUserByIdWithAssociations( commLog.getAssignedToUser().getId(), requestCollection );
            Node assignedNode = null;
            for ( Iterator nodeIter = curUser.getUserNodes().iterator(); nodeIter.hasNext(); )
            {
              Node temp = ( (UserNode)nodeIter.next() ).getNode();
              if ( hierarchy.contains( temp ) )
              {
                assignedNode = temp;
                break;
              }
            }
            User nextUser = null;
            if ( assignedNode != null && assignedNode.getParentNode() != null )
            {
              nextUser = findNextUser( assignedNode.getParentNode() );
            }
            if ( nextUser != null )
            {
              commLog.setAssignedToUser( nextUser );
            }
            commLog.setDateEscalated( new Timestamp( System.currentTimeMillis() ) );
            getCommLogService().saveCommLog( commLog );

            sendMail( curUser, MessageService.COMM_LOG_ESCALATED_FROM_MESSAGE_CM_ASSET_CODE, null );

            if ( nextUser != null )
            {
              sendMail( curUser, MessageService.COMM_LOG_ESCALATED_FROM_MESSAGE_CM_ASSET_CODE, null );
            }
            count++;
          }
        }
      }
      addComment( count + " CommLogs were escalated." );
    }
  }

  private User findNextUser( Node node )
  {
    User nextUser = null;
    if ( node.getParentNode().getNodeOwner() != null )
    {
      nextUser = node.getParentNode().getNodeOwner();
    }
    else if ( !node.getParentNode().getUsersByRole( HierarchyRoleType.MANAGER ).isEmpty() )
    {
      nextUser = (User)node.getParentNode().getUsersByRole( HierarchyRoleType.MANAGER ).iterator().next();
    }
    else
    {
      if ( node.getParentNode().getParentNode() != null )
      {
        // keep going up the tree
        findNextUser( node.getParentNode().getParentNode() );
      }
    }
    return nextUser;
  }

  public CommLogService getCommLogService()
  {
    return commLogService;
  }

  public void setCommLogService( CommLogService commLogService )
  {
    this.commLogService = commLogService;
  }

  public SystemVariableService getSystemVariableService()
  {
    return systemVariableService;
  }

  public void setSystemVariableService( SystemVariableService systemVariableService )
  {
    this.systemVariableService = systemVariableService;
  }

  public NodeService getNodeService()
  {
    return nodeService;
  }

  public void setNodeService( NodeService nodeService )
  {
    this.nodeService = nodeService;
  }
}
