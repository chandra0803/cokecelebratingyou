
package com.biperf.core.dao.participant;

import java.util.List;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.user.UserCountryChanges;
import com.biperf.core.exception.ServiceErrorException;

/**
 * UserCountryChangesDAO.
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
 * <td>Bala</td>
 * <td>Dec 09, 2011</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 */

public interface UserCountryChangesDAO extends DAO
{
  public static final String BEAN_NAME = "userCountryChangesDAO";

  public List getUsersToMoveBalance();

  public UserCountryChanges saveUserCountryChanges( UserCountryChanges userCountryChanges ) throws ServiceErrorException;

  public UserCountryChanges getUserByOldCampaign( String oldCampaignNbr, Long userId );

  public String getDecryptedValue( String awardbanqNbr );

  public String getEncryptedValue( String toEncrypt );

  /**
   * @param id
   * @return UserCountryChanges
   */
  public UserCountryChanges getById( Long id );

  /**
   * @return List of userCountryChanges IDs
   */
  public List<Long> getUCCsWithAccountBalancesToTranfer();
}
