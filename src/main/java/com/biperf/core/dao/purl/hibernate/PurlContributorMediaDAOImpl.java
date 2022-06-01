
package com.biperf.core.dao.purl.hibernate;

import java.util.List;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.purl.PurlContributorMediaDAO;
import com.biperf.core.domain.purl.PurlContributorMedia;
import com.biperf.core.utils.hibernate.HibernateUtil;

/**
 * PurlContributorMediaDAOImpl.
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

public class PurlContributorMediaDAOImpl extends BaseDAO implements PurlContributorMediaDAO
{

  public PurlContributorMedia getPurlContributorMediaById( Long id )
  {
    return (PurlContributorMedia)getSession().get( PurlContributorMedia.class, id );
  }

  public PurlContributorMedia save( PurlContributorMedia media )
  {
    return (PurlContributorMedia)HibernateUtil.saveOrUpdateOrShallowMerge( media );
  }

  public List<PurlContributorMedia> getMediaUploads( PurlContributorMediaQueryConstraint constraint )
  {
    return HibernateUtil.getObjectList( constraint );
  }

  public int getMediaUploadCount( PurlContributorMediaQueryConstraint constraint )
  {
    return HibernateUtil.getObjectListCount( constraint );
  }

}
