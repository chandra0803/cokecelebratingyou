
package com.biperf.core.dao.challengepoint.hibernate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.challengepoint.ChallengepointProgressDAO;
import com.biperf.core.domain.challengepoint.ChallengepointProgress;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.utils.hibernate.HibernateUtil;

/**
 * ChallengepointProgressDAOImpl.
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
 * <td>reddy</td>
 * <td>Jul 17, 2008</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public class ChallengepointProgressDAOImpl extends BaseDAO implements ChallengepointProgressDAO
{

  public void deleteChallengepointProgress( ChallengepointProgress challengepointProgress )
  {
    getSession().delete( challengepointProgress );

  }

  public ChallengepointProgress getChallengepointProgressById( Long id )
  {
    return (ChallengepointProgress)getSession().get( ChallengepointProgress.class, id );
  }

  /**
   * Overridden from @see com.biperf.core.dao.challengepoint.ChallengepointProgressDAO#getChallengepointProgressByPromotionIdAndUserId(java.lang.Long, java.lang.Long, com.biperf.core.service.AssociationRequestCollection)
   * @param promotionId
   * @param userId
   * @param associationRequestCollection
   * @return list of cp progress ordered by submission date.
   */
  public List<ChallengepointProgress> getChallengepointProgressByPromotionIdAndUserId( Long promotionId, Long userId, AssociationRequestCollection associationRequestCollection )
  {

    List<ChallengepointProgress> cpProgresslist = new ArrayList<ChallengepointProgress>();
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.challengepoint.ChallengepointPromotionByUserIdAndPromotionId" );
    query.setLong( "userId", userId.longValue() );
    query.setLong( "promotionId", promotionId.longValue() );
    cpProgresslist = query.list();

    Iterator iter = cpProgresslist.iterator();
    while ( iter.hasNext() )
    {
      if ( associationRequestCollection != null )
      {
        associationRequestCollection.process( (ChallengepointProgress)iter.next() );
      }
    }
    return cpProgresslist;

  }

  /**
   * Overridden from @see com.biperf.core.dao.challengepoint.ChallengepointProgressDAO#getChallengepointProgressByPromotionIdAndUserId(java.lang.Long, java.lang.Long, com.biperf.core.service.AssociationRequestCollection)
   * @param promotionId
   
   * @param associationRequestCollection
   * @return list of cp progress ordered by submission date.
   */
  public List<ChallengepointProgress> getChallengepointProgressByPromotionId( Long promotionId, AssociationRequestCollection associationRequestCollection )
  {

    List<ChallengepointProgress> cpProgresslist = new ArrayList<ChallengepointProgress>();
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.challengepoint.ChallengepointPromotionByPromotionId" );
    query.setLong( "promotionId", promotionId.longValue() );
    cpProgresslist = query.list();

    Iterator iter = cpProgresslist.iterator();
    while ( iter.hasNext() )
    {
      if ( associationRequestCollection != null )
      {
        associationRequestCollection.process( (ChallengepointProgress)iter.next() );
      }
    }
    return cpProgresslist;

  }

  public ChallengepointProgress saveChallengepointProgress( ChallengepointProgress challengepointProgress )
  {
    return (ChallengepointProgress)HibernateUtil.saveOrUpdateOrShallowMerge( challengepointProgress );
  }

}
