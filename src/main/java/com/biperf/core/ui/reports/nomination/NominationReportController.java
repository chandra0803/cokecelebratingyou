/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/reports/nomination/NominationReportController.java,v $
 *
 */

package com.biperf.core.ui.reports.nomination;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.enums.ApprovalStatusType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.reports.NominationReportsService;
import com.biperf.core.ui.reports.ReportController;

/**
 * NominationReportController <p/> <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>drahn</td>
 * <td>Aug 15, 2012</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 * @author drahn
 *
 */
public class NominationReportController extends ReportController
{
  @SuppressWarnings( "unused" )
  private static final Log LOG = LogFactory.getLog( NominationReportController.class );

  /**
   * Fetches generic data for Report Display pages
   * 
   * @param tileContext the context for the tile associated with this controller.
   * @param request the HTTP request we are processing.
   * @param response the HTTP response we are creating.
   * @param servletContext the context for servlets of this web application.
   */
  @SuppressWarnings( "unchecked" )
  @Override
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    super.onExecute( tileContext, request, response, servletContext );

    // Bug 2510 - show only winner and non-winner statuses for nomination promotion
    List<ApprovalStatusType> approvalStatusTypeList = new ArrayList<ApprovalStatusType>();
    List<ApprovalStatusType> allNomApprovalList = ApprovalStatusType.getList( PromotionType.NOMINATION, null );
    for ( ApprovalStatusType approvalStatusType : allNomApprovalList )
    {
      if ( ApprovalStatusType.WINNER.equals( approvalStatusType.getCode() ) || ApprovalStatusType.NON_WINNER.equals( approvalStatusType.getCode() ) )
      {
        approvalStatusTypeList.add( approvalStatusType );
      }
    }
    request.setAttribute( "approvalStatusTypeList", approvalStatusTypeList );
  }

  protected PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }

  protected NominationReportsService getNominationReportsService()
  {
    return (NominationReportsService)getService( NominationReportsService.BEAN_NAME );
  }
}
