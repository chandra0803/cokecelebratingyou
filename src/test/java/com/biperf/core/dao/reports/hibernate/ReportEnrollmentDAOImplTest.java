
package com.biperf.core.dao.reports.hibernate;

import java.util.HashMap;

import com.biperf.core.dao.hibernate.BaseDAOTest;
import com.biperf.core.dao.reports.EnrollmentReportsDAO;
import com.biperf.core.utils.ApplicationContextFactory;

public class ReportEnrollmentDAOImplTest extends BaseDAOTest
{

  public void testGetEnrollmentSummaryResults()
  {
    getEnrollmentReportsDAO().getEnrollmentSummaryResults( new HashMap<String, Object>() );
  }

  private EnrollmentReportsDAO getEnrollmentReportsDAO()
  {
    return (EnrollmentReportsDAO)ApplicationContextFactory.getApplicationContext().getBean( EnrollmentReportsDAO.BEAN_NAME );
  }

}
