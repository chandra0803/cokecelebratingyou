
package com.biperf.core.service.reports.impl;

import org.jmock.Mock;

import com.biperf.core.dao.reports.ReportsDAO;
import com.biperf.core.domain.report.Report;
import com.biperf.core.domain.report.ReportDashboardItem;
import com.biperf.core.domain.report.ReportDashboardItemParam;
import com.biperf.core.domain.report.ReportParameter;
import com.biperf.core.service.BaseServiceTest;
import com.biperf.core.service.reports.ReportsService;

public class ReportsServiceImplTest extends BaseServiceTest
{
  public ReportsServiceImplTest( String test )
  {
    super( test );
  }

  private ReportsService reportsService = new ReportsServiceImpl();
  private Mock mockReportsDAO = null;

  protected void setUp() throws Exception
  {
    mockReportsDAO = new Mock( ReportsDAO.class );
    ( (ReportsServiceImpl)reportsService ).setReportsDAO( (ReportsDAO)mockReportsDAO.proxy() );
  }

  public void testGetAwardType()
  {
    Report report = new Report();
    report.setId( 1000L );
    ReportParameter reportParameter = new ReportParameter();
    reportParameter.setReport( report );
    reportParameter.setParameterName( "AWARDTYPE" );
    reportParameter.setId( 5000L );
    ReportDashboardItem reportDashBoardItem = new ReportDashboardItem();
    reportDashBoardItem.setId( 2000L );
    ReportDashboardItemParam reportDashBoardItemParam = new ReportDashboardItemParam();
    reportDashBoardItemParam.setReportDashboardItem( reportDashBoardItem );
    reportDashBoardItemParam.setValue( "points" );
    reportDashBoardItemParam.setReportParameter( reportParameter );

    mockReportsDAO.expects( once() ).method( "getAwardType" ).with( same( reportDashBoardItem.getId() ), same( report.getId() ) ).will( returnValue( "Points" ) );
    String actualRole = reportsService.getAwardType( reportDashBoardItem.getId(), report.getId() );
    assertTrue( "Actual  matches to what was expected", actualRole.equals( "Points" ) );

  }

}
