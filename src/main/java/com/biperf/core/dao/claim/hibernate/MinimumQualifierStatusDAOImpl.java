/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.dao.claim.hibernate;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.claim.MinimumQualifierStatusDAO;
import com.biperf.core.domain.claim.MinimumQualifierStatus;
import com.biperf.core.utils.hibernate.HibernateUtil;

/**
 * .
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
 * <td>wadzinsk</td>
 * <td>Mar 2, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public class MinimumQualifierStatusDAOImpl extends BaseDAO implements MinimumQualifierStatusDAO
{

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.claim.MinimumQualifierStatusDAO#save(com.biperf.core.domain.claim.MinimumQualifierStatus)
   * @param minimumQualifierStatus
   */
  public MinimumQualifierStatus save( MinimumQualifierStatus minimumQualifierStatus )
  {
    return (MinimumQualifierStatus)HibernateUtil.saveOrUpdateOrShallowMerge( minimumQualifierStatus );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.claim.MinimumQualifierStatusDAO#getMinimumQualifierStatus(java.lang.Long)
   * @param minimumQualifierStatusId
   */
  public MinimumQualifierStatus getMinimumQualifierStatus( Long minimumQualifierStatusId )
  {
    return (MinimumQualifierStatus)getSession().load( MinimumQualifierStatus.class, minimumQualifierStatusId );
  }

  public MinimumQualifierStatus getMinimumQualifierStatusById( Long minimumQualifierStatusId )
  {
    return (MinimumQualifierStatus)getSession().get( MinimumQualifierStatus.class, minimumQualifierStatusId );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.claim.MinimumQualifierStatusDAO#getMinQualifierStatusByPromoPayoutGroup(java.lang.Long,
   *      java.lang.Long)
   * @param submitterId
   * @param promotionId
   */
  public Map getMinQualifierStatusByPromoPayoutGroup( Long submitterId, Long promotionId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.claim.MinimumQualifierStatusForSubmitterAndPromotion" );

    query.setParameter( "submitterId", submitterId );
    query.setParameter( "promotionId", promotionId );

    List statuses = query.list();

    Map minQualifierStatusByPromoPayoutGroup = new LinkedHashMap();

    for ( Iterator iter = statuses.iterator(); iter.hasNext(); )
    {
      MinimumQualifierStatus minimumQualifierStatus = (MinimumQualifierStatus)iter.next();
      minQualifierStatusByPromoPayoutGroup.put( minimumQualifierStatus.getPromotionPayoutGroup(), minimumQualifierStatus );
    }

    return minQualifierStatusByPromoPayoutGroup;
  }

}
