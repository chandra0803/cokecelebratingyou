
package com.biperf.core.dao.purl;

import java.util.List;

import com.biperf.core.dao.DAO;
import com.biperf.core.dao.purl.hibernate.PurlContributorMediaQueryConstraint;
import com.biperf.core.domain.purl.PurlContributorMedia;

/**
 * PurlContributorMediaDAO.
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

public interface PurlContributorMediaDAO extends DAO
{

  public static final String BEAN_NAME = "purlContributorMediaDAO";

  public PurlContributorMedia getPurlContributorMediaById( Long id );

  public PurlContributorMedia save( PurlContributorMedia media );

  public List<PurlContributorMedia> getMediaUploads( PurlContributorMediaQueryConstraint constraint );

  public int getMediaUploadCount( PurlContributorMediaQueryConstraint constraint );

}
