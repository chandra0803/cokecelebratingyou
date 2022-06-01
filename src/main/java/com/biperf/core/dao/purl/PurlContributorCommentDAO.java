
package com.biperf.core.dao.purl;

import java.util.List;

import com.biperf.core.dao.DAO;
import com.biperf.core.dao.purl.hibernate.PurlContributorCommentQueryConstraint;
import com.biperf.core.domain.purl.PurlContributorComment;
import com.biperf.core.value.participant.PurlContributorImageData;

/**
 * PurlContributorCommentDAO.
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
 * <td>Dec 15, 2010</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */

public interface PurlContributorCommentDAO extends DAO
{

  public static final String BEAN_NAME = "purlContributorCommentDAO";

  public PurlContributorComment getPurlContributorCommentById( Long id );

  public PurlContributorComment save( PurlContributorComment comment );

  public List<PurlContributorComment> getComments( PurlContributorCommentQueryConstraint constraint );

  public int getCommentCount( PurlContributorCommentQueryConstraint constraint );

  public List<PurlContributorImageData> getNotMigratedPurlContributorImgPaxData();

  public void updateMigratedPurlContributorImage( Long purlContributorCommentId, String imageUrl, String imageUrlThumb );

}
