
package com.biperf.core.value;

import java.util.LinkedHashSet;
import java.util.Set;

import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.user.User;

public class ClaimApproversValue
{
  private Set approverUsers = new LinkedHashSet();
  private boolean autoApprove;
  private User autoApproveUser;
  private boolean additionalApprovalRoundRequired;

  /**
   * if the approval type is node based, what what the source node that produced the
   */
  private Node sourceNode;

  /**
   * @param approverUsers
   */
  public ClaimApproversValue( Set approverUsers )
  {
    super();
    this.approverUsers = approverUsers;
  }

  public ClaimApproversValue()
  {
    super();
  }

  /**
   * @return value of additionalApprovalRoundRequired property
   */
  public boolean isAdditionalApprovalRoundRequired()
  {
    return additionalApprovalRoundRequired;
  }

  /**
   * @param additionalApprovalRoundRequired value for additionalApprovalRoundRequired property
   */
  public void setAdditionalApprovalRoundRequired( boolean additionalApprovalRoundRequired )
  {
    this.additionalApprovalRoundRequired = additionalApprovalRoundRequired;
  }

  /**
   * @return value of approverUsers property
   */
  public Set getApproverUsers()
  {
    return approverUsers;
  }

  /**
   * @param approverUsers value for approverUsers property
   */
  public void setApproverUsers( Set approverUsers )
  {
    this.approverUsers = approverUsers;
  }

  /**
   * @return value of autoApprove property
   */
  public boolean isAutoApprove()
  {
    return autoApprove;
  }

  /**
   * @param autoApprove value for autoApprove property
   */
  public void setAutoApprove( boolean autoApprove )
  {
    this.autoApprove = autoApprove;
  }

  /**
   * @return value of sourceNode property
   */
  public Node getSourceNode()
  {
    return sourceNode;
  }

  /**
   * @param sourceNode value for sourceNode property
   */
  public void setSourceNode( Node sourceNode )
  {
    this.sourceNode = sourceNode;
  }

  /**
   * @return value of autoApproveUser property
   */
  public User getAutoApproveUser()
  {
    return autoApproveUser;
  }

  /**
   * @param autoApproveUser value for autoApproveUser property
   */
  public void setAutoApproveUser( User autoApproveUser )
  {
    this.autoApproveUser = autoApproveUser;
  }

}
