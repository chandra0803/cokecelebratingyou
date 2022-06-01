
package com.biperf.core.dao.purl.hibernate;

import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.purl.PurlContributorCommentDAO;
import com.biperf.core.domain.purl.PurlContributorComment;
import com.biperf.core.utils.hibernate.HibernateUtil;
import com.biperf.core.value.participant.PurlContributorImageData;

/**
 * PurlContributorCommentDAOImpl.
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

public class PurlContributorCommentDAOImpl extends BaseDAO implements PurlContributorCommentDAO
{
  private static final Log log = LogFactory.getLog( PurlContributorCommentDAOImpl.class );

  private DataSource dataSource;

  private JdbcTemplate jdbcTemplate;

  public void setDataSource( DataSource dataSource )
  {
    this.dataSource = dataSource;
    this.jdbcTemplate = new JdbcTemplate( dataSource );
  }

  public PurlContributorComment getPurlContributorCommentById( Long id )
  {
    return (PurlContributorComment)getSession().get( PurlContributorComment.class, id );
  }

  public PurlContributorComment save( PurlContributorComment comment )
  {
    return (PurlContributorComment)HibernateUtil.saveOrUpdateOrShallowMerge( comment );
  }

  public List<PurlContributorComment> getComments( PurlContributorCommentQueryConstraint constraint )
  {
    return HibernateUtil.getObjectList( constraint );
  }

  public int getCommentCount( PurlContributorCommentQueryConstraint constraint )
  {
    return HibernateUtil.getObjectListCount( constraint );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<PurlContributorImageData> getNotMigratedPurlContributorImgPaxData()
  {
    getSession().flush();
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.purl.getNotMigratedPurlContributorImgPaxData" );
    query.setResultTransformer( Transformers.aliasToBean( PurlContributorImageData.class ) );
    return (List<PurlContributorImageData>)query.list();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void updateMigratedPurlContributorImage( Long purlContributorCommentId, String imageUrl, String imageUrlThumb )
  {
    String query = "update purl_contributor_comment set image_url = ?, image_url_thumb = ?, modified_by = 651, date_modified = sysdate where purl_contributor_comment_id = ?";

    Object[] params = { imageUrl, imageUrlThumb, purlContributorCommentId };

    try
    {
      jdbcTemplate.update( query, params );
    }
    catch( DataAccessException e )
    {
      log.error( "DAO Layer, the purlContributorCommentId : " + purlContributorCommentId + " : " + e );
    }

  }

}
