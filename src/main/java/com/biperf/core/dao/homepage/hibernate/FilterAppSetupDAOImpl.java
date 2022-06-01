/*
 * (c) 2009 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/dao/homepage/hibernate/FilterAppSetupDAOImpl.java,v $
 */

package com.biperf.core.dao.homepage.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.homepage.FilterAppSetupDAO;
import com.biperf.core.domain.homepage.FilterAppSetup;

public class FilterAppSetupDAOImpl extends BaseDAO implements FilterAppSetupDAO
{

  @Override
  public FilterAppSetup getFilterAppSetupById( Long filterAppSetupId )
  {
    FilterAppSetup filterAppSetup = (FilterAppSetup)getSession().get( FilterAppSetup.class, filterAppSetupId );
    return filterAppSetup;
  }

  @Override
  public List<FilterAppSetup> getAll()
  {
    Criteria criteria = getSession().createCriteria( FilterAppSetup.class );
    return criteria.list();
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public List<FilterAppSetup> getAllForHomePage()
  {
    Criteria criteria = getSession().createCriteria( FilterAppSetup.class );
    // order
    criteria.addOrder( Order.asc( "priority" ) );
    criteria.addOrder( Order.asc( "orderNumber" ) );

    return criteria.list();
  }

  @Override
  public FilterAppSetup save( FilterAppSetup filterAppSetup )
  {
    getSession().saveOrUpdate( filterAppSetup );
    return filterAppSetup;
  }

  @Override
  public void delete( FilterAppSetup filterAppSetup )
  {
    getSession().delete( filterAppSetup );
  }

  @Override
  public List<FilterAppSetup> getFilterAppSetupByFilterTypeCode( String code )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.homepage.FilterAppSetupByCode" );
    query.setParameter( "code", code );
    return query.list();
  }

}
