/*
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/participant/AudienceService.java,v $
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.service.participant;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.biperf.core.domain.enums.HierarchyRoleType;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.participant.Audience;
import com.biperf.core.domain.participant.AudienceRole;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.participant.PaxAudience;
import com.biperf.core.domain.promotion.DivisionCompetitorsAudience;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionManagerWebRulesAudience;
import com.biperf.core.domain.user.User;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.exception.UniqueConstraintViolationException;
import com.biperf.core.security.AuthenticatedUser;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.SAO;
import com.biperf.core.service.participant.impl.AudienceListValueBean;
import com.biperf.core.value.participant.AudienceDetail;

/**
 * AudienceService.
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
 * <td>sharma</td>
 * <td>Jun 13, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public interface AudienceService extends SAO
{
  /** * BEAN_NAME is the reference for spring ** */
  public static final String BEAN_NAME = "audienceService";

  /**
   * Get all audiences
   * 
   * @return List
   */
  public List getAll();

  /**
   * Get the audience from database by id
   * 
   * @param audienceId
   * @param associationRequestCollection
   * @return Audience
   */
  public Audience getAudienceById( Long audienceId, AssociationRequestCollection associationRequestCollection );

  /**
   * Get the audience from database by name
   * 
   * @param name
   * @return Audience
   */
  public Audience getAudienceByName( String name );

  /**
   * Save the audience in database
   * 
   * @param audience
   * @return Audience
   * @throws ServiceErrorException
   */
  public Audience save( Audience audience ) throws ServiceErrorException;

  /**
   * @param participant
   * @param promotionAudiences
   * @return boolean
   */

  /**
   * Copies a Audience
   *
   * @param audienceIdToCopy
   * @param newAudienceName
   * @return Audience (The copied audience)
   * @throws UniqueConstraintViolationException
   * @throws ServiceErrorException
   */
  public Audience copyAudience( Long audienceIdToCopy, String newAudienceName, List newChildAudienceNameHolders ) throws UniqueConstraintViolationException, ServiceErrorException;

  public boolean isUserInPromotionAudiences( Participant participant, Set promotionAudiences );

  public boolean isUserInPromotionDivisionAudiences( Participant participant, Set<DivisionCompetitorsAudience> divCompAudiences );

  /**
   * @param promotion
   * @param participant
   * @return boolean
   */
  public boolean isParticipantInPrimaryAudience( Promotion promotion, Participant participant );

  /**
   * @param promotion
   * @param participant
   * @param submitterNode
   * @return boolean
   */
  public boolean isParticipantInSecondaryAudience( Promotion promotion, Participant participant, Node submitterNode );

  /**
   * @param promotion
   * @param participant
   * @return boolean if Participant is in WebRulesAudience
   */
  public boolean isParticipantInWebRulesAudience( Promotion promotion, Participant participant );

  /**
   * Return a set of all ParticipantAudiences and all CriteriaAudiences that are assigned to this
   * participant.
   * 
   * @param participant
   * @return boolean
   */
  public Set getAllParticipantAudiences( Participant participant );

  /**
   * Is audience name unique
   * 
   * @param audienceName
   * @return boolean
   */
  public boolean isAudienceNameUnique( String audienceName );

  /** 
  * check if there is a PaxAudience for the supplied audienceId and
  * particpantId
  *
  * @param participant
  * @param paxAudience
  * @return List of PaxAudience objects
  */
  public List checkPaxAudiencesByAudienceIdParticipantId( Participant participant, PaxAudience paxAudience );

  /**
  *
  * check if there is a Audience for the supplied participantId and
  * audience
  *
  * @param participantId
  * @param audience
  * @return boolean
  */
  public boolean isParticipantInAudience( Long participantId, Audience audience );

  /**
   * Will check the node to see if it is valid for any of the audiences.  If the
   * audience is a pax audience then the node will automatically be valid.  If it is a criteria
   * audience and the criteria contains node criteria the appropriate validations will
   * be done. If roleType passed is null then any node criteria for that node will be valid, otherwise
   * the role will also be validated.
   * 
   * @param node
   * @param promotionAudiences
   * @param roleType
   * @return
   */
  public boolean isNodeInPromotionAudiences( Node node, Set promotionAudiences, HierarchyRoleType roleType );

  /**
   * Filter the nodeList based on the secondaryAudience.
   * @param promotion
   * @param nodeList
   * @param submitterNode
   * @return a List of Nodes
   */
  public List<Node> filterNodeListBySecondaryAudience( Promotion promotion, Collection nodeCollection, Node submitterNode );

  /**
   * Will check each node to see if it is valid for each of the audiences.  The list
   * returned will contain all nodes that meet the specified audience criteria.  If the
   * audience is a pax audience then all nodes will be valid.  If it is a criteria
   * audience and the criteria contains node criteria the appropriate validations will
   * be done.
   * 
   * @param nodeList
   * @param audienceSet
   * @return a List of nodes filtered by the audience
   */
  public List<Node> filterNodesByAudiences( List nodeList, Set audienceSet );

  /**
   * @param participant
   * @param promotionPartnerAudiences
   * @return true or flase
   */

  public boolean isParticipantInSecondaryAudience( Promotion promotion, Participant participant );

  public boolean isUserInPromotionPartnerAudiences( Participant participant, Set<Audience> promotionPartnerAudiences );

  public boolean isUserInParticipantPartnerAudiences( Participant participant, Promotion promotion );

  /**
   * Clears the criteria audience cache
   */
  public void clearCriteriaAudienceCache();

  public void clearPromoEligibilityCache();

  public void processLogout( AuthenticatedUser authenticatedUser );

  public List getAudienceList();

  public Map rematchParticipantForAllCriteriaAudiences( Long participantId ) throws ServiceErrorException;

  public Map rematchNodeForAllCriteriaAudiences( Long sourceNodeId, Long destinationPaxNodeId, Long destinationChildNodeId ) throws ServiceErrorException;

  public Map recreateCriteriaAudienceParticipants( Long audienceId ) throws ServiceErrorException;

  public List filterParticipantNodesByAudienceNodeRole( List participants, Set audienceSet );

  public boolean isParticipantInPublicRecognitionAudience( Promotion promotion, Participant participant );

  public boolean isViewWebRulesVisible( Long promotionId, Long participantId );

  public boolean isPromotionPrimaryAudienceInManagerNode( List<Node> nodes, Long promotionId );

  public Map deleteAudience( Long audienceId );

  public AudienceRole updateAudienceRole( AudienceRole audienceRole );

  public List<Audience> getAudienceListFromAudienceRole( String roleCode );

  public AudienceRole deleteAllAudienceRoles();

  public List<AudienceRole> getAllAudienceRole();

  public String getAudienceNameById( Long audienceId );

  public boolean isUserInDIYCommAudience( Long userId, String roleCode );

  public boolean isUserInPromotionManagerAudiences( Participant participant, Set<PromotionManagerWebRulesAudience> promotionPartnerAudiences );

  public Map<String, Object> getCriteriaAudienceActiveandInactive( Map<String, Object> inputParameters );

  public List<AudienceListValueBean> getAudiencesForRosterSearchGroups( Long audienceId, String audienceName, String audienceType );

  public List<User> getAllPersonsByAudienceId( Long audienceId );

  public Audience updateAudience( Long audienceId, String audienceName, String audienceType, boolean isGroupPublic );

  public Long getAudienceIdByRosterAudienceId( UUID rosterAudienceId );

  public UUID getRosterAudienceIdByAudienceId( Long audienceId );

  public List<String> getRosterAudienceIdsByAudienceIds( List<Long> audienceIds );

  public List<AudienceDetail> getAudienceDetailsByUserId( Long userId );
  
  public Audience getAudienceByRosterAudienceId( UUID rosterAudienceId );

}
