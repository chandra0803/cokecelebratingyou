/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/dao/purl/PurlRecipientDAO.java,v $
 */

package com.biperf.core.dao.purl;

import java.util.List;
import java.util.Map;

import com.biperf.core.dao.DAO;
import com.biperf.core.dao.purl.hibernate.PurlRecipientQueryConstraint;
import com.biperf.core.domain.enums.PurlRecipientType;
import com.biperf.core.domain.purl.PurlCelebrationsView;
import com.biperf.core.domain.purl.PurlRecipient;
import com.biperf.core.value.recognition.PurlRecipientValue;

/**
 * PurlRecipientDAO.
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
 * <td>gadapa</td>
 * <td>Oct 07, 2010</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public interface PurlRecipientDAO extends DAO
{

  public static final String BEAN_NAME = "purlRecipientDAO";

  /**
   * Get the PurlRecipient from the database by the id.
   * 
   * @param id
   * @return PurlRecipient
   */
  public PurlRecipient getPurlRecipientById( Long id );

  /**
   * Get the PurlRecipient from the database by claim id.  Purl will have to have been processed by award process to have a claim id.
   * 
   * @param claimId
   * @return PurlRecipient
   */
  public PurlRecipient getPurlRecipientByClaimId( Long claimId );

  /**
   * Saves the PurlRecipient to the database.
   * 
   * @param info
   * @return PurlRecipient
   */
  public PurlRecipient save( PurlRecipient info );

  /**
   * Deletes the PurlRecipient from the database.
   * 
   * @param PurlRecipient
   */
  public void delete( PurlRecipient info );

  public List<PurlRecipient> getPurlRecipientList( PurlRecipientQueryConstraint constraint );

  public List<Long> getPurlRecipientsForAutoInvite( Long promotionId );

  public List<PurlRecipient> getGlobalUpcomingPurlRecipients( int rowNumStart, int rowNumEnd, String sortedBy, String sortedOn, int pageSize );

  public int getGlobalUpcomingPurlRecipientsCount();

  public int getFollowedUserUpcomingPurlRecipientsCount( List<Long> followedUserIdsList );

  public int getTeamUpcomingPurlRecipientsCount( List<Long> nodeIds );

  public int getTeamAwardedPurlRecipientsCount( List<Long> nodeIds );

  public int getGlobalAwardedPurlRecipientsCount();

  public int getFollowedUserAwardedPurlRecipientsCount( List<Long> followedUserIdsList );

  public PurlRecipient getPurlRecipientByCelebrationManagerMessageId( Long id );

  public int getPurlRecipientsCountForAutoInvite( Long promotionId, Long purlRecipientId );

  public List<PurlCelebrationsView> getPurlRecipientsForCelebrationPage( Map<String, Object> queryParams );

  public int getRecommendedPurlRecipientsCountForGivenContributor( Long userId, PurlRecipientType pastOrUpcomming );

  public List<PurlRecipientValue> getUpComingCelebrationList();

  public List<PurlRecipient> getPurlRecipientByUserId( Long userId );

  public int getCelebrationAwardDate( Long userId, Long claimId );

  public int getPurlAwardDate( Long userId, Long purlRecipientId );
  
  public List<Long> getPurlRecipientsForAutoInvite( Long promotionId, Long numberOfDays );

  public List getCustomSortOfUpcomingCelebration( Long charaterticsDivisionId , Long userId , int pageNumber , int pageSize ); //Customization for the WIP#51332 
}
