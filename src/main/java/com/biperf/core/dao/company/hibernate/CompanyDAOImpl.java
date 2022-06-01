/*
 * (c) 2017 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.dao.company.hibernate;

import org.hibernate.Criteria;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.company.CompanyDAO;
import com.biperf.core.domain.company.Company;
import com.biperf.core.utils.HibernateSessionManager;
import com.biperf.core.utils.hibernate.HibernateUtil;

/**
 * 
 * @author palaniss
 * @since Nov 01, 2018
 * 
 */
public class CompanyDAOImpl extends BaseDAO implements CompanyDAO
{

  @Override
  public Company saveCompany( Company company )
  {
    return (Company)HibernateUtil.saveOrUpdateOrDeepMerge( company );
  }

  @Override
  public Company getCompanyDetail()
  {
    Criteria criteria = HibernateSessionManager.getSession().createCriteria( Company.class );
    return (Company)criteria.uniqueResult();
  }
}
