/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/dao/participant/hibernate/AboutMeDAOImpl.java,v $
 */

package com.biperf.core.dao.participant.hibernate;

import java.util.List;

import org.hibernate.Query;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.participant.AboutMeDAO;
import com.biperf.core.domain.participant.AboutMe;
import com.biperf.core.utils.hibernate.HibernateUtil;

/**
 * UserDAOImpl used for persisting and retreiving User domain objects.
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
 * <td>tennant</td>
 * <td>Apr 6, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public class AboutMeDAOImpl extends BaseDAO implements AboutMeDAO
{
  @Override
  public AboutMe getAboutMeById( Long id )
  {
    return (AboutMe)getSession().get( AboutMe.class, id );
  }

  @Override
  public AboutMe saveAboutMe( AboutMe aboutMe )
  {
    return (AboutMe)HibernateUtil.saveOrUpdateOrShallowMerge( aboutMe );
  }

  @Override
  public void deleteAboutMe( AboutMe aboutMe )
  {
    getSession().delete( aboutMe );
  }

  public List<AboutMe> getAllAboutMeByUserId( Long userId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.participant.AboutMeByUserId" );
    query.setParameter( "userId", userId );

    return query.list();
  }

  @Override
  public AboutMe getAboutMeByUserIdAndCode( Long userId, String code )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.participant.AboutMeByUserIdAndCode" );
    query.setParameter( "userId", userId );
    query.setParameter( "code", code );

    return (AboutMe)query.uniqueResult();
  }

}
