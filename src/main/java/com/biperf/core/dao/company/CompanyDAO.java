/*
 * (c) 2017 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.dao.company;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.company.Company;

/**
 * 
 * @author palaniss
 * @since Nov 01, 2018
 * 
 */
public interface CompanyDAO extends DAO
{
  public static final String BEAN_NAME = "companyDAO";

  Company saveCompany( Company company );

  Company getCompanyDetail();

}
