/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/dao/reports/hibernate/ReportsDAOImpl.java,v $
 *
 */

package com.biperf.core.dao.reports.hibernate;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.reports.ReportsDAO;
import com.biperf.core.domain.report.Report;
import com.biperf.core.domain.report.ReportDashboard;
import com.biperf.core.utils.HibernateSessionManager;
import com.biperf.core.utils.NewServiceAnniversaryUtil;
import com.biperf.core.utils.hibernate.HibernateUtil;
import com.biperf.core.value.FormattedValueBean;

/**
 * ReportsDAOImpl <p/> <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>dunne</td>
 * <td>Oct 31, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 * @author dunne
 */
public class ReportsDAOImpl extends BaseDAO implements ReportsDAO
{
  /**
   * Retrieve the user characteristics that can be used for reporting.
   * 
   * @return List
   */
  public List getReportUserCharacteristics()
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.characteristic.ReportUserCharacteristics" );

    return query.list();
  }

  /**
   * Retreive the node type characteristics that can be used for reporting.
   * 
   * @return List
   */
  public List getReportNodeTypeCharacteristics()
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.characteristic.ReportNodeCharacteristics" );

    return query.list();
  }

  /**
   * Retrieve the Reports.
   * 
   * @return List
   */
  public List getReports()
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.report.getReport" );

    return query.list();
  }

  /**
   * Retrieve all the Reports.
   * 
   * @return List
   */
  public List getAllReports()
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.report.getAllReport" );
    return query.list();
  }

  /**
   * Gets the date and time of the Overridden from
   * 
   * @see com.biperf.core.dao.reports.ReportsDAO#getReportDate(java.lang.String, java.lang.String)
   * @param reportName
   * @param reportType
   * @return Date
   */
  public Date getReportDate( String reportCategory )
  {
    /*
     * StringBuffer dateQuery = new StringBuffer(); dateQuery.append( "SELECT refresh_date " );
     * dateQuery.append( "  FROM rpt_refresh_date " ); dateQuery.append(
     * " WHERE report_category_type = '"+ reportCategory + "' " ); dateQuery.append( "UNION " );
     * dateQuery.append( "SELECT refresh_date " ); dateQuery.append( "  FROM rpt_refresh_date " );
     * dateQuery.append( " WHERE report_category_type = 'ReportRefresh' " ); dateQuery.append(
     * "   AND NOT EXISTS ( select 1 from rpt_refresh_date where report_category_type = '"+
     * reportCategory + "' ) " ); Date dateExecuted = (Date)getSession().createSQLQuery(
     * dateQuery.toString() ).addScalar( "refresh_date", TypeHelper ).uniqueResult();
     */
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.report.getRefreshDate" );
    query.setParameter( "categoryType", reportCategory );
    Timestamp dateExecuted = (Timestamp)query.uniqueResult();
    return dateExecuted;

  }

  /**
   * Retreive the User Dashboard along with its items
   * 
   * @return Report Dashboard.
   */
  public ReportDashboard getUserDashboard( Long id )
  {
    if ( id == null || id.longValue() <= 0 )
    {
      return null;
    }
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.report.getUserDashboard" );

    query.setLong( "paxId", id.longValue() );
    return (ReportDashboard)query.uniqueResult();
  }

  /**
   * Saves the Report Dashboard information to the database.
   * 
   * @param reportDashboard
   * @return ReportDashboard
   */
  public ReportDashboard saveReportDashboard( ReportDashboard reportDashboard )
  {
    return (ReportDashboard)HibernateUtil.saveOrUpdateOrShallowMerge( reportDashboard );
  }

  /**
   * Get the report by Id. Overridden from
   * 
   * @param reportId
   * @return Report
   */
  public Report getReport( Long reportId )
  {
    return (Report)getSession().get( Report.class, reportId );
  }

  /**
   * Get the report by code. Overridden from
   * 
   * @param reportCode
   * @return Report
   */
  @Override
  public Report getReportByCode( String reportCode )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.report.getByReportCode" );
    query.setString( "reportCode", reportCode );
    return (Report)query.uniqueResult();
  }

  /**
   * Gets a report parameter drop down list using named query 
   * @param namedQuery
   * @return List
   */
  @Override
  public List getReportParameters( String namedQuery, Object param )
  {
    if ( NewServiceAnniversaryUtil.isNewServiceAnniversaryEnabled() )
    {
      List<String> oldNamedQuery = Arrays.asList( "com.biperf.core.domain.node.reportParameterValueChoices.allLivePromotions",
                                                  "com.biperf.core.domain.node.reportParameterValueChoices.allLivePromotionsWithAwards",
                                                  "com.biperf.core.domain.node.reportParameterValueChoices.allLivePromotionsWithBudget",
                                                  "com.biperf.core.domain.node.reportParameterValueChoices.allLivePromotionsWithCashBudget",
                                                  "com.biperf.core.domain.node.reportParameterValueChoices.allLiveRecognitionPromotions",
                                                  "com.biperf.core.domain.node.reportParameterValueChoices.allLivePurlRecognitionPromotions",
                                                  "com.biperf.core.domain.node.reportParameterValueChoices.allLiveRecognitionBehaviorPromotions",
                                                  "com.biperf.core.domain.node.reportParameterValueChoices.allLiveBehaviorPromotions",
                                                  "com.biperf.core.domain.node.reportParameterValueChoices.allLivePlateauPromotions" );
      if ( oldNamedQuery.contains( namedQuery ) )
      {
        namedQuery += ".dmsa";
      }

    }

    Query query = getSession().getNamedQuery( namedQuery );

    if ( param != null )
    {
      query.setParameter( "param", param );
    }
    query.setResultTransformer( Transformers.aliasToBean( FormattedValueBean.class ) );
    return query.list();
  }

  /**
   * Update the report
   * 
   * @param report
   */
  public void updateReports( Report report )
  {
    Session session = HibernateSessionManager.getSession();
    session.update( report );
  }

  @Override
  public String getAwardType( Long dashboardItemId, Long reportId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.report.getAwardtype" );

    query.setLong( "dashboardItemId", dashboardItemId );
    query.setLong( "reportId", reportId );
    return ( (String)query.uniqueResult() ).equalsIgnoreCase( "merchandise" ) ? "Plateau" : (String)query.uniqueResult();

  }

}
