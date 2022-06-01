/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/user/UserNodeForm.java,v $
 */

package com.biperf.core.ui.user;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.biperf.core.domain.enums.HierarchyRoleType;
import com.biperf.core.domain.user.User;
import com.biperf.core.service.hierarchy.NodeService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.ui.BaseActionForm;
import com.biperf.core.utils.BeanLocator;

/**
 * UserNodeForm.
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
 * <td>crosenquest</td>
 * <td>May 5, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class UserNodeForm extends BaseActionForm
{
  public static final String FORM_NAME = "userNodeForm";

  private String[] nodeIds = new String[10];

  private String nodeId;
  private String nameOfNode;

  private String method;

  private String userId;
  private String userName;

  private String role;

  private String returnActionMapping;

  private boolean primary;

  public void setMethod( String method )
  {
    this.method = method;
  }

  public String getMethod()
  {
    return this.method;
  }

  public void setNodeIds( String[] nodeIds )
  {
    this.nodeIds = nodeIds;
  }

  public String[] getNodeIds()
  {
    return this.nodeIds;
  }

  public void setNodeId( String nodeId )
  {
    this.nodeId = nodeId;
  }

  public String getNodeId()
  {
    return this.nodeId;
  }

  public void setUserId( String userId )
  {
    this.userId = userId;
  }

  public String getUserId()
  {
    return this.userId;
  }

  public void setRole( String role )
  {
    this.role = role;
  }

  public String getRole()
  {
    return this.role;
  }

  public String getNameOfNode()
  {
    return nameOfNode;
  }

  public void setNameOfNode( String nameOfNode )
  {
    this.nameOfNode = nameOfNode;
  }

  public String getUserName()
  {
    return userName;
  }

  public void setUserName( String userName )
  {
    this.userName = userName;
  }

  public String getReturnActionMapping()
  {
    return returnActionMapping;
  }

  public void setReturnActionMapping( String returnActionMapping )
  {
    this.returnActionMapping = returnActionMapping;
  }

  private NodeService getNodeService()
  {
    return (NodeService)BeanLocator.getBean( NodeService.BEAN_NAME );
  }

  private UserService getUserService()
  {
    return (UserService)BeanLocator.getBean( UserService.BEAN_NAME );
  }

  private ParticipantService getParticipantService()
  {
    return (ParticipantService)BeanLocator.getBean( ParticipantService.BEAN_NAME );
  }

  /**
   * Makes sure that at least one value is present in the delete[] attribute
   * 
   * @param mapping
   * @param request
   * @return ActionErrors
   */
  public ActionErrors validate( ActionMapping mapping, HttpServletRequest request )
  {
    ActionErrors actionErrors = super.validate( mapping, request );
    if ( actionErrors == null )
    {
      actionErrors = new ActionErrors();
    }
    if ( getNodeId() != null && getRole() != null && getRole().equals( HierarchyRoleType.OWNER ) )
    {
      User owner = getParticipantService().getNodeOwner( Long.valueOf( getNodeId() ) );
      if ( owner != null )
      {
        if ( Long.parseLong( getUserId() ) != owner.getId().longValue() )
        {
          actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "participant.errors.NODE_ALREADY_HAS_OWNER", getUserService().getUserById( Long.valueOf( getUserId() ) ).getUserName() ) );
        }
      }
    }

    return actionErrors;
  }

  public void setPrimary( boolean primary )
  {
    this.primary = primary;
  }

  public boolean isPrimary()
  {
    return primary;
  }
}
