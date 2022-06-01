/**
 * 
 */

package com.biperf.core.dao.promotion.hibernate;

import java.util.List;

import org.hibernate.Query;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.promotion.PromotionSweepstakeDAO;
import com.biperf.core.domain.promotion.PromotionSweepstake;
import com.biperf.core.utils.hibernate.HibernateUtil;

/**
 * PromotionSweepstakeDAOImpl.
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
 * <td>asondgeroth</td>
 * <td>Nov 2, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public class PromotionSweepstakeDAOImpl extends BaseDAO implements PromotionSweepstakeDAO
{
  /**
   * Saves the hierarchy to the database.
   * 
   * @param promotionSweepstake
   * @return PromotionSweepstake
   */
  public PromotionSweepstake save( PromotionSweepstake promotionSweepstake )
  {
    return (PromotionSweepstake)HibernateUtil.saveOrUpdateOrShallowMerge( promotionSweepstake );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.promotion.PromotionSweepstakeDAO#delete(com.biperf.core.domain.promotion.PromotionSweepstake)
   * @param promotionSweepstake
   */
  public void delete( PromotionSweepstake promotionSweepstake )
  {
    getSession().delete( promotionSweepstake );
  }

  /**
   * Get the hierarchy from the database by the id.
   * 
   * @param id
   * @return PromotionSweepstake
   */
  public PromotionSweepstake getPromotionSweepstakeById( Long id )
  {
    return (PromotionSweepstake)getSession().get( PromotionSweepstake.class, id );
  }

  /**
   * Retrieves all the PromotionSweepstakes from the database.
   * 
   * @return List of PromotionSweepstakes
   */
  public List getAllPromotionSweepstakes()
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.AllPromotionSweepstakes" );

    return query.list();
  }

  /**
   * Retrieves all the PromotionSweepstakes sorted by date from the database.
   * 
   * @return List of PromotionSweepstakes
   */
  public List getAllPromotionSweepstakesListSortedByDate()
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.AllPromotionSweepstakesSortedByDate" );

    return query.list();
  }

  /**
   * Retrieves all the PromotionSweepstakes given promotionId sorted by date from the database.
   * 
   * @param promotionId
   * @return List of PromotionSweepstakes
   */
  public List getAllPromotionSweepstakesListByPromotionIdSortedByDate( Long promotionId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.AllPromotionSweepstakesByPromotionIdSortedByDate" );

    query.setParameter( "promotionId", promotionId );

    return query.list();
  }

  /* Bug # 34020 start */
  public int getPromotionSweepstakesNotProcessedCount( Long promotionId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.PromotionSweepstakesNotProcessedCount" );
    query.setParameter( "promoId", promotionId );

    Integer count = (Integer)query.uniqueResult();
    return count.intValue();
  }

  public int getPromotionSweepstakesHistoryCount( Long promotionId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.PromotionSweepstakesHistoryCount" );
    query.setParameter( "promoId", promotionId );

    Integer count = (Integer)query.uniqueResult();
    return count.intValue();
  }

  public List getPromotionSweepstakesByPromotionIdNotProcessed( Long promotionId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.PromotionSweepstakesByPromotionIdNotProcessed" );

    query.setParameter( "promotionId", promotionId );

    return query.list();
  }

  public List getAllPromotionSweepstakeWinnersByDrawingId( Long drawingId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.AllPromotionSweepstakeWinnersByDrawingId" );

    query.setParameter( "drawingId", drawingId );

    return query.list();
  }
  /* Bug # 34020 end */
}
