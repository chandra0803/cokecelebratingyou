/*
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/dao/participant/AudienceDAO.java,v $
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.dao.participant;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.participant.Audience;
import com.biperf.core.domain.participant.AudienceRole;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.participant.impl.AudienceListValueBean;
import com.biperf.core.value.participant.AudienceDetail;

/**
 * AudienceDAO.
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
 * <td>Jun 14, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public interface AudienceDAO extends DAO
{

  public static final String BEAN_NAME = "audienceDAO";

  /**
   * Get the audience from database by id
   * 
   * @return Audience
   */
  public List getAll();

  public List getAllCriteriaAudiences();

  /**
   * Get all PaxAudience objects of which the participant is a member.
   * 
   * @param participantId
   * @return List
   */
  public List getAllPaxAudiencesByParticipantId( Long participantId );

  /**
   * Get the audience from database by id
   * 
   * @param audienceId
   * @return Audience
   */
  public Audience getAudienceById( Long audienceId );

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
   */
  public Audience save( Audience audience );

  /**
   * Is audience name unique
   * 
   * @param audienceName
   * @return boolean
   */
  public boolean isAudienceNameUnique( String audienceName );

  /**
   * 
   * check if there is a PaxAudience for the supplied audienceId and
   * participantId
   *
   * @param audienceId
   * @param participantId
   * @return List of PaxAudience objects
   */
  public List checkPaxAudiencesByAudienceIdParticipantId( Long audienceId, Long participantId );

  /**
   * 
   * check if there is a Audience for the supplied audienceId and
   * particpantId
   *
   * @param audienceId
   * @param participantId
   * @return int 0 indicating false and 1 indicating true
   */
  public int checkAudiencesByAudienceIdParticipantId( Long audienceId, Long participantId );

  public boolean isPaxInPromotionPartnerAudiences( Long participantId, Long promotionId );

  public List getAudienceList();

  public Map rematchParticipantForAllCriteriaAudiences( Long participantId ) throws ServiceErrorException;

  public Map rematchNodeForAllCriteriaAudiences( Long sourceNodeId, Long destinationPaxNodeId, Long destinationChildNodeId );

  public Map recreateCriteriaAudienceParticipants( Long audienceId );

  public boolean isPromotionPrimaryAudienceInManagerNode( List<Node> nodes, Long promotionId );

  public Map deleteAudience( Long audienceId );

  public AudienceRole updateAudienceRole( AudienceRole audienceRole );

  public List<Audience> getAudienceListFromAudienceRole( String roleCode );

  public List<AudienceRole> getAllAudienceRole();

  public void deleteAudienceRole( AudienceRole audienceRole );

  public String getAudienceNameById( Long audienceId );

  public boolean isUserInDIYCommAudience( Long userId, String roleCode );

  public Map<String, Object> getCriteriaAudienceActiveandInactive( Map<String, Object> inputParameters );

  public List<AudienceListValueBean> getAudiencesForRosterSearchGroups( Long audienceId, String audienceName, String audienceType );

  public List<Object> getAllUsersByAudienceId( Long audienceId );

  public Long getAudienceIdByRosterAudienceId( UUID rosterAudienceId );

  public UUID getRosterAudienceIdByAudienceId( Long audienceId );

  public List<String> getRosterAudienceIdsByAudienceIds( List<Long> audienceIds );

  public List<AudienceDetail> getAudienceDetailsByUserId( Long userId );
  
  public Audience getAudienceByRosterAudienceId( UUID rosterAudienceId );

}
