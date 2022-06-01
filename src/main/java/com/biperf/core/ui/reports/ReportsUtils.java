/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/reports/ReportsUtils.java,v $
 */

package com.biperf.core.ui.reports;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import com.biperf.core.domain.enums.PromotionCertificate;
import com.biperf.core.domain.enums.ReportCategoryType;
import com.biperf.core.domain.report.Report;
import com.biperf.core.security.acl.AclEntry;
import com.biperf.core.security.acl.ReportNodeAclEntry;
import com.biperf.core.service.security.AuthorizationService;
import com.biperf.core.utils.ServiceLocator;
import com.objectpartners.cms.domain.Content;

/**
 * ReportsUtils.
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
 * <td>moisei</td>
 * <td>Aug 12, 2005</td>
 * <td>1.0</td>
 * <td>pilot</td>
 * </tr>
 * </table>
 * 
 * @author Constantin Moisei
 *
 */

public class ReportsUtils
{
  public static final String FORMAT_PDF = "PDF";
  public static final String FORMAT_XML = "XML";
  public static final String FORMAT_HTML = "HTML";
  public static final String FORMAT_XLS = "XLS";
  public static final String FORMAT_CSV = "CSV";

  /**
   * @param response
   * @param output
   * @throws ServletException
   */
  public static void writeReport( HttpServletResponse response, byte[] output ) throws ServletException
  {
    response.setHeader( "Pragma", "public" );
    response.setHeader( "Cache-Control", "max-age=0" );
    response.setContentType( "application/pdf" );
    String pdffilename = "reportDoc.pdf";
    response.setHeader( "Content-disposition", "inline; filename=" + pdffilename );
    if ( output == null || output.length == 0 )
    {
      return;
    }

    ServletOutputStream ouputStream;
    try
    {
      // logger.debug("Writing " + output.length + " bytes to output stream");
      ouputStream = response.getOutputStream();
      ouputStream.write( output, 0, output.length );
      ouputStream.flush();
      ouputStream.close();
    }
    catch( IOException e )
    {
      // logger.error("Error writing report output", e);
      throw new ServletException( e.getMessage(), e );
    }

  }

  public static Content getCertificateContent( String promotionType, String certificateId )
  {
    List certificates = PromotionCertificate.getList( promotionType );
    for ( Iterator iter = certificates.iterator(); iter.hasNext(); )
    {
      Content content = (Content)iter.next();
      if ( content.getContentDataMap().get( "ID" ).equals( certificateId ) )
      {
        return content;
      }
    }
    return null;
  }

  public static List<Report> getFilteredReports( List<Report> reportList, Boolean plateauPlatformOnly, boolean isBIAdmin )
  {
    List<Report> filteredReportList = new ArrayList<Report>();
    if ( !plateauPlatformOnly )
    {
      if ( isBIAdmin )
      {
        return reportList;
      }
      else
      {
        for ( Iterator<Report> reportIter = reportList.iterator(); reportIter.hasNext(); )
        {
          Report report = (Report)reportIter.next();
          if ( !report.getCategoryType().getCode().equals( ReportCategoryType.WORKHAPPIER ) )
          {
            if ( report.getReportCode().equals( Report.PLATEAU_AWARD_ITEM_SELECTION ) || report.getReportCode().equals( Report.PLATEAU_AWARD_LEVEL_ACTIVITY )
                || report.getReportCode().equals( Report.HIERARCHY_EXPORT ) || report.getReportCode().equals( Report.PARTICIPANT_EXPORT ) )
            {
              AuthorizationService azn = (AuthorizationService)ServiceLocator.getService( AuthorizationService.BEAN_NAME );
              AclEntry reportsNodeAclEntry = azn.getAclEntry( ReportNodeAclEntry.ACL_CODE );
              if ( reportsNodeAclEntry == null )
              {
                filteredReportList.add( report );
              }
            }
            else
            {
              filteredReportList.add( report );
            }
          }
        }
      }
    }
    else
    {
      if ( isBIAdmin )
      {
        for ( Iterator<Report> reportIter = reportList.iterator(); reportIter.hasNext(); )
        {
          Report report = (Report)reportIter.next();
          if ( report.isIncludedInPlateau() )
          {
            filteredReportList.add( report );
          }
        }
      }
      else
      {
        for ( Iterator<Report> reportIter = reportList.iterator(); reportIter.hasNext(); )
        {
          Report report = (Report)reportIter.next();
          if ( report.isIncludedInPlateau() && !report.getCategoryType().getCode().equals( ReportCategoryType.WORKHAPPIER ) )
          {
            if ( report.getReportCode().equals( Report.PLATEAU_AWARD_ITEM_SELECTION ) || report.getReportCode().equals( Report.PLATEAU_AWARD_LEVEL_ACTIVITY )
                || report.getReportCode().equals( Report.HIERARCHY_EXPORT ) || report.getReportCode().equals( Report.PARTICIPANT_EXPORT ) )
            {
              AuthorizationService azn = (AuthorizationService)ServiceLocator.getService( AuthorizationService.BEAN_NAME );
              AclEntry reportsNodeAclEntry = azn.getAclEntry( ReportNodeAclEntry.ACL_CODE );
              if ( reportsNodeAclEntry == null )
              {
                filteredReportList.add( report );
              }
            }
            else
            {
              filteredReportList.add( report );
            }
          }
        }
      }
    }
    return filteredReportList;

  }
}
