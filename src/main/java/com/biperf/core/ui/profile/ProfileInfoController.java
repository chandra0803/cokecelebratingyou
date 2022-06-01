/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/profile/ProfileInfoController.java,v $
 */

package com.biperf.core.ui.profile;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.enums.AboutMeQuestionType;
import com.biperf.core.domain.enums.HierarchyRoleType;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.participant.ParticipantEmployer;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserNode;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.hierarchy.NodeService;
import com.biperf.core.service.participant.ParticipantAssociationRequest;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserAssociationRequest;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.utils.DateFormatterUtil;
import com.biperf.core.utils.UserManager;

/**
 * ChangePasswordController <p/> <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>tennant</td>
 * <td>May 26, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */

public class ProfileInfoController extends BaseController
{
  private static final Log logger = LogFactory.getLog( ProfileInfoController.class );

  /**
   * Prepares the form and request for the userProfile page.
   * 
   * @param tileContext
   * @param request
   * @param response
   * @param servletContext
   * @throws Exception
   */
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    // fetch the User and place on the request
    Long userId = UserManager.getUserId();
    if ( UserManager.getUser().isParticipant() )
    {
      AssociationRequestCollection userAssociationRequestCollection = new AssociationRequestCollection();
      userAssociationRequestCollection.add( new UserAssociationRequest( UserAssociationRequest.ALL ) );
      AssociationRequestCollection reqCollection = new AssociationRequestCollection();
      reqCollection.add( new ParticipantAssociationRequest( ParticipantAssociationRequest.EMPLOYER ) );
      reqCollection.add( new ParticipantAssociationRequest( ParticipantAssociationRequest.ADDRESSES ) );
      reqCollection.add( new ParticipantAssociationRequest( ParticipantAssociationRequest.USER_NODE ) );
      reqCollection.add( new ParticipantAssociationRequest( ParticipantAssociationRequest.PHONES ) );
      reqCollection.add( new ParticipantAssociationRequest( ParticipantAssociationRequest.EMAILS ) );
      Participant user = getParticipantService().getParticipantByIdWithAssociations( userId, reqCollection );
      request.setAttribute( "user", user );
      request.setAttribute( "deptName", user.getPaxDeptName() != null ? user.getPaxDeptName() : null );
      request.setAttribute( "positionName", user.getPaxJobName() != null ? user.getPaxJobName() : null );
      if ( user != null && user.getUserNodes() != null )
      {
        boolean hasNodes = !user.getUserNodes().isEmpty();
        request.setAttribute( "hasNodes", String.valueOf( hasNodes ) );

        HashMap ownerMap = new HashMap( user.getUserNodes().size() );
        HashMap managerSets = new HashMap( user.getUserNodes().size() );
        for ( Iterator iter = user.getUserNodes().iterator(); iter.hasNext(); )
        {
          UserNode userNode = (UserNode)iter.next();

          Node node = getNodeService().getNodeById( userNode.getNode().getId() );

          List owners = getUserService().getAllUsersOnNodeHavingRole( node.getId(), HierarchyRoleType.lookup( HierarchyRoleType.OWNER ), userAssociationRequestCollection );

          if ( owners != null && !owners.isEmpty() )
          {
            ownerMap.put( node.getName(), getFormattedName( (User)owners.iterator().next() ) );
          }

          List managerObjs = getUserService().getAllUsersOnNodeHavingRole( node.getId(), HierarchyRoleType.lookup( HierarchyRoleType.MANAGER ), userAssociationRequestCollection );

          List managers = new ArrayList( managerObjs.size() );
          for ( Iterator manIter = managerObjs.iterator(); manIter.hasNext(); )
          {
            User mgr = (User)manIter.next();
            managers.add( getFormattedName( mgr ) );
          }
          managerSets.put( node.getName(), managers );
        }

        request.setAttribute( "nodeOwners", ownerMap );
        request.setAttribute( "nodeManagers", managerSets );
      }
      String avatarSmall = null;
      String avatarLarge = null;

      if ( user != null )
      {
        if ( !StringUtils.isEmpty( user.getAvatarSmall() ) )
        {
          avatarSmall = user.getAvatarSmallFullPath();
        }
        if ( !StringUtils.isEmpty( user.getAvatarOriginal() ) )
        {
          avatarLarge = user.getAvatarOriginalFullPath();
        }
      }
      request.setAttribute( "avatarSmall", avatarSmall );
      request.setAttribute( "avatarLarge", avatarLarge );

      // Choose Sub Nav
      String subNavSelected = (String)tileContext.getAttribute( "subNavSelected" );
      request.setAttribute( "subNavSelected", subNavSelected );
      logger.debug( "subNavSelected=" + subNavSelected );

      // about me question
      request.setAttribute( "aboutmeQuestionList", AboutMeQuestionType.getList() );
      request.setAttribute( "userAddress", user.getPrimaryAddress() );
      request.setAttribute( "userPhone", user.getPrimaryPhone() );
      request.setAttribute( "userEmailAddress", user.getPrimaryEmailAddress() );
    }
    else
    {
      User user = getUserService().getUserById( userId );
      request.setAttribute( "user", user );
    }
    ParticipantEmployer paxEmployer = getParticipantService().getCurrentParticipantEmployer( userId );
    if ( paxEmployer != null )
    {
      Date hireDate = getParticipantService().getActiveHireDate( userId );
      request.setAttribute( "hireDate", hireDate );
    }

    request.setAttribute( "JstlDatePattern", DateFormatterUtil.getDatePattern( UserManager.getLocale() ) );

  }

  private String getFormattedName( User user )
  {
    String name = user.getLastName() + ", " + user.getFirstName();
    if ( user.getMiddleName() != null && !user.getMiddleName().equals( "" ) )
    {
      name = name + " " + user.getMiddleName().substring( 0, 1 );
    }
    return name;
  }

  /**
   * Get the UserService.
   * 
   * @return UserService
   */
  private UserService getUserService()
  {
    return (UserService)getService( UserService.BEAN_NAME );
  }

  private ParticipantService getParticipantService()
  {
    return (ParticipantService)getService( ParticipantService.BEAN_NAME );
  }

  private NodeService getNodeService()
  {
    return (NodeService)getService( NodeService.BEAN_NAME );
  }

  private static SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }
}
