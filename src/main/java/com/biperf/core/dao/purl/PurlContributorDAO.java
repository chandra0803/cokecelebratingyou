
package com.biperf.core.dao.purl;

import java.util.List;

import com.biperf.core.dao.DAO;
import com.biperf.core.dao.purl.hibernate.PurlContributorQueryConstraint;
import com.biperf.core.domain.purl.PurlContributor;
import com.biperf.core.value.participant.PaxAvatarData;
import com.biperf.core.value.participant.PurlContributorAvatarData;
import com.biperf.core.value.participant.PurlNotMigratedPaxData;

/**
 * PurlContributorDAO.
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
 * <td>shanmuga</td>
 * <td>Nov 22, 2010</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */

public interface PurlContributorDAO extends DAO
{

  public static final String BEAN_NAME = "purlContributorDAO";

  /**
    * Saves the PurlContributor to the database.
    * 
    * @param info
    * @return PurlContributor
    */
  public PurlContributor save( PurlContributor contributor );

  /**
   * Deletes the PurlContributor from the database.
   * 
   * @param PurlContributor
   */
  public void delete( PurlContributor contributor );

  /**
   * Gets the PurlContributor from the database.
   * 
   * @param PurlContributor
   */
  public PurlContributor getPurlContributorById( Long id );

  public List<PurlContributor> getContributors( PurlContributorQueryConstraint constraint );

  public int getContributorCount( PurlContributorQueryConstraint constraint );

  public PurlContributor getContributorStepElementById( Long id );

  public List<PaxAvatarData> getNotMigratedPurlContributorAvatarData();

  public List<PurlContributor> getAllPendingPurlContributionsProActive( Long promotionId, Long numberOfDays );

  public void updateMigratedPurlContributorAvatar( Long purlContributorId, String avatarUrl );

  public List<Long> getAllInternalPurlContributorUser();

  public List<PurlContributorAvatarData> getNotSyncPurlContrbUserAvatarData( List<Long> userIds );

  public void updateMigratedPurlContrPaxAvatar( Long userId, String avatarUrl );

  public List<Long> getAllPurlUsersAvatarMigrated( List<Long> userIds );

  public List<PurlNotMigratedPaxData> getNotMigratedPurlUserAvatar( List<Long> userIds );

  public List<Long> getAllPurlContrbUsersToCopyTheUrl();  
  
  /*Customization for WIP 32479 starts here*/	
  public void unsubscribeExternalUser( String emailAddress );

  public boolean isExternalContributorExists( String emailAddr );
  
  /*Customization for WIP 32479 ends here*/ 
}
