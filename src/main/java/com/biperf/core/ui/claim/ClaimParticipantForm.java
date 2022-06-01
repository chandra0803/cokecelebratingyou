/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/claim/ClaimParticipantForm.java,v $
 */

package com.biperf.core.ui.claim;

import com.biperf.core.domain.claim.ProductClaimParticipant;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.PromotionTeamPosition;
import com.biperf.core.ui.BaseForm;

/**
 * ClaimParticipantForm.
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
 * <td>zahler</td>
 * <td>Jul 11, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ClaimParticipantForm extends BaseForm
{
  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  /**
   * <p>
   * If the promotion organizes claim participants by group, then the <code>participant</code>
   * field represents a participant who will share in the payout from this claim. In this case, this
   * attribute will be not null.
   * </p>
   * <p>
   * If the promotion organizes claim participants by role, then the <code>participant</code>
   * field--in addition to representing a participant who will share in the payout from this
   * claim--associates a participant with the role specified by the
   * <code>claimParticipantRoleType</code> attribute. This attribute is null if no participant is
   * assigned to the role specified by <code>claimParticipantRoleType</code>.
   * </p>
   */
  private Participant participant;

  private String paxId;

  private Node node;

  private String nodeId;

  private String claimParticipantId;

  private String version;

  private String dateCreated;

  private String createdBy;

  /**
   * When the promotion organizes claim participants by group, this attribute is null.
   */
  private PromotionTeamPosition promotionTeamPosition;

  // ---------------------------------------------------------------------------
  // Constructors
  // ---------------------------------------------------------------------------

  /**
   * Constructs a {@link ClaimParticipantForm} object.
   */
  public ClaimParticipantForm()
  {
    // Default constructor
  }

  /**
   * Constructs a {@link ClaimParticipantForm} object.
   * 
   * @param participant represents a participant.
   */
  public ClaimParticipantForm( Participant participant )
  {
    this.participant = participant;
    this.paxId = participant.getId() != null ? participant.getId().toString() : null;
  }

  /**
   * Constructs a {@link ClaimParticipantForm} object.
   * 
   * @param productClaimParticipant represents a claim participant.
   */
  public ClaimParticipantForm( ProductClaimParticipant productClaimParticipant )
  {
    participant = productClaimParticipant.getParticipant();
    promotionTeamPosition = productClaimParticipant.getPromotionTeamPosition();
    paxId = productClaimParticipant.getParticipant() != null ? productClaimParticipant.getParticipant().getId().toString() : null;
    node = productClaimParticipant.getNode();
    nodeId = productClaimParticipant.getNode() != null ? productClaimParticipant.getNode().getId().toString() : null;
    claimParticipantId = productClaimParticipant.getId() != null ? productClaimParticipant.getId().toString() : null;
    version = productClaimParticipant.getVersion() != null ? productClaimParticipant.getVersion().toString() : null;
    createdBy = productClaimParticipant.getAuditCreateInfo().getCreatedBy() != null ? productClaimParticipant.getAuditCreateInfo().getCreatedBy().toString() : null;
    dateCreated = productClaimParticipant.getAuditCreateInfo().getDateCreated() != null ? String.valueOf( productClaimParticipant.getAuditCreateInfo().getDateCreated().getTime() ) : null;
  }

  // ---------------------------------------------------------------------------
  // Getter and Setter Methods
  // ---------------------------------------------------------------------------

  /**
   * Returns a participant associated with this claim.
   * 
   * @return a participant associated with this claim.
   */
  public Participant getParticipant()
  {
    return participant;
  }

  /**
   * Sets a participant associated with this claim.
   * 
   * @param participant a participant associated with this claim.
   */
  public void setParticipant( Participant participant )
  {
    this.participant = participant;
    this.paxId = participant != null ? participant.getId().toString() : null;
  }

  /**
   * @return value of promotionTeamPosition property
   */
  public PromotionTeamPosition getPromotionTeamPosition()
  {
    return promotionTeamPosition;
  }

  /**
   * @param promotionTeamPosition value for promotionTeamPosition property
   */
  public void setPromotionTeamPosition( PromotionTeamPosition promotionTeamPosition )
  {
    this.promotionTeamPosition = promotionTeamPosition;
  }

  public String getPaxId()
  {
    return paxId;
  }

  public void setPaxId( String paxId )
  {
    this.paxId = paxId;
  }

  public Node getNode()
  {
    return node;
  }

  public void setNode( Node node )
  {
    this.node = node;
    this.nodeId = node != null ? node.getId().toString() : null;
  }

  /**
   * @return value of nodeId property
   */
  public String getNodeId()
  {
    return nodeId;
  }

  /**
   * @param nodeId value for nodeId property
   */
  public void setNodeId( String nodeId )
  {
    this.nodeId = nodeId;
  }

  /**
   * @return value of claimParticipantId property
   */
  public String getClaimParticipantId()
  {
    return claimParticipantId;
  }

  /**
   * @param claimParticipantId value for claimParticipantId property
   */
  public void setClaimParticipantId( String claimParticipantId )
  {
    this.claimParticipantId = claimParticipantId;
  }

  /**
   * @return value of createdBy property
   */
  public String getCreatedBy()
  {
    return createdBy;
  }

  /**
   * @param createdBy value for createdBy property
   */
  public void setCreatedBy( String createdBy )
  {
    this.createdBy = createdBy;
  }

  /**
   * @return value of dateCreated property
   */
  public String getDateCreated()
  {
    return dateCreated;
  }

  /**
   * @param dateCreated value for dateCreated property
   */
  public void setDateCreated( String dateCreated )
  {
    this.dateCreated = dateCreated;
  }

  /**
   * @return value of version property
   */
  public String getVersion()
  {
    return version;
  }

  /**
   * @param version value for version property
   */
  public void setVersion( String version )
  {
    this.version = version;
  }
}
