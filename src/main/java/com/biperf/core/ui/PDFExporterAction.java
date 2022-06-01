
package com.biperf.core.ui;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.ujac.print.DocumentPrinter;

import com.biperf.core.domain.claim.Approvable;
import com.biperf.core.domain.claim.ApprovableItem;
import com.biperf.core.domain.claim.ApprovableItemApprover;
import com.biperf.core.domain.enums.ApprovalStatusType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.multimedia.ECard;
import com.biperf.core.domain.promotion.NominationPromotion;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.claim.ClaimAssociationRequest;
import com.biperf.core.service.claim.ClaimGroupAssociationRequest;
import com.biperf.core.service.claim.ClaimGroupService;
import com.biperf.core.service.claim.ClaimService;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.approvals.ApprovalsNominationListForm;
import com.biperf.core.ui.utils.CustomUjacHttpResourceLoader;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.Environment;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.PromotionApprovableValue;

/** 
 * @author dudam
 * @since Jan 23, 2013
 * @version 1.0
 */
public class PDFExporterAction extends BaseDispatchAction
{

  public ActionForward nominationApprovalList( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    // get form and its values
    ApprovalsNominationListForm approvalsNominationListForm = (ApprovalsNominationListForm)form;
    Date startDate = DateUtils.toDate( approvalsNominationListForm.getStartDate() );
    Date endDate = DateUtils.toDate( approvalsNominationListForm.getEndDate() );
    Long promotionId = new Long( approvalsNominationListForm.getPromotionId() );
    String filterApprovalStatusCode = approvalsNominationListForm.getFilterApprovalStatusCode();

    // build approvables list
    List<Approvable> approvables = buildApprovablesList( startDate, endDate, promotionId, filterApprovalStatusCode );

    String outputFileName = null;
    String xmlString = null;

    // build output file name
    for ( Approvable approvable : approvables )
    {
      if ( outputFileName == null )
      {
        outputFileName = approvable.getPromotion().getName() + "_" + "approvals.pdf";
        break;
      }
    }

    // build xmlString
    xmlString = buildXMLString( approvables );

    InputStream templateStream = new ByteArrayInputStream( xmlString.getBytes() );

    Map documentProperties = new HashMap();

    // instantiating the document printer
    DocumentPrinter documentPrinter = new DocumentPrinter( templateStream, documentProperties );
    documentPrinter.setXmlReaderClass( "org.apache.xerces.parsers.SAXParser" );
    String siteUrl = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
    String imageSource = siteUrl + ECard.CERTIFICATES_FOLDER;
    if ( Environment.isCtech() )
    {
      documentPrinter.setResourceLoader( new CustomUjacHttpResourceLoader( Environment.buildProxy(), imageSource ) );
    }
    else
    {
      documentPrinter.setResourceLoader( new CustomUjacHttpResourceLoader( imageSource ) );
    }
    try
    {
      FileOutputStream pdfStream = new FileOutputStream( Environment.getTmpDir() + "/" + outputFileName );
      documentPrinter.printDocument( pdfStream );
      response.setContentType( "application/pdf" );

      FileInputStream fis = new FileInputStream( Environment.getTmpDir() + "/" + outputFileName );
      byte[] b;
      int x = fis.available();
      b = new byte[x];
      fis.read( b );

      OutputStream os = response.getOutputStream();
      os.write( b );
      os.flush();
    }
    catch( Exception e )
    {
      e.printStackTrace();
    }
    return null;
  }

  private String buildXMLString( List<Approvable> approvables )
  {

    StringBuilder xmlString = new StringBuilder( "" );
    xmlString.append( "<document size=\"A4\" margin-left=\"25\" margin-right=\"25\" margin-top=\"25\" margin-bottom=\"25\"> " );
    xmlString.append( " <font-def name=\"header\" family=\"Helvetica\" size=\"12\" style=\"bold\" />" );
    xmlString.append( "<font-def name=\"normal\" family=\"Helvetica\" size=\"12\" style=\"normal\" /> " );
    xmlString.append( "<font-def name=\"small\" family=\"Helvetica\" style=\"normal\" size=\"9\"   />" );
    xmlString.append( "<header font=\"small\">" );
    xmlString.append( "<header-part halign=\"left\">" );
    xmlString.append( "Test" );
    xmlString.append( "Heading goes here.." );
    xmlString.append( "</header-part>" );
    xmlString.append( "</header>" );
    xmlString.append( "<footer font=\"small\">" );
    xmlString.append( "<footer-part padding-top=\"10\" halign=\"center\">" );
    xmlString.append( "Page of " );
    xmlString.append( "</footer-part>" );
    xmlString.append( "</footer>" );
    xmlString.append( "<paragraph>" );
    xmlString.append( "grasdfasdfasedfasdf para" );
    xmlString.append( "</paragraph>" );
    xmlString.append( "<font name=\"normal\">" );
    xmlString.append( "<table width=\"100\" widths=\"10,20,20,12,20,15\" padding=\"10\" padding-top=\"5\" padding-bottom=\"5\">" );
    xmlString.append( "<font name=\"header\">" );
    xmlString.append( "<table-header border-style=\"top|bottom\" border-width=\"1.5\">" );
    xmlString.append( "<cell halign=\"right\"><phrase rotate=\"0\">Nominee</phrase></cell>" );
    xmlString.append( "<cell><phrase rotate=\"0\">Org Name</phrase></cell>" );
    xmlString.append( "<cell><phrase rotate=\"0\">Nominator</phrase></cell>" );
    xmlString.append( "<cell><phrase rotate=\"0\">Date Submitted</phrase></cell>" );
    xmlString.append( "<cell><phrase rotate=\"0\">Award</phrase></cell>" );
    xmlString.append( "<cell><phrase rotate=\"0\">Notification Date</phrase></cell>" );
    xmlString.append( "</table-header>" );
    xmlString.append( "</font>" );
    for ( Approvable approvable : approvables )
    {
      xmlString.append( "<table-row><cell halign=\"right\">" + approvable.getPromotion().getName() + "</cell>" );
      xmlString.append( "<cell>" + approvable.getPromotion().getName() + "</cell>" );
      xmlString.append( "<cell>" + approvable.getPromotion().getName() + "</cell>" );
      xmlString.append( "<cell>" + approvable.getPromotion().getName() + "</cell>" );
      xmlString.append( "<cell>" + approvable.getPromotion().getName() + "</cell>" );
      xmlString.append( "<cell>" + approvable.getPromotion().getName() + "</cell>" );
      xmlString.append( "</table-row>" );
    }
    xmlString.append( "</table>" );
    xmlString.append( "</font>" );
    xmlString.append( "</document>" );
    return xmlString.toString();
  }

  private List<Approvable> buildApprovablesList( Date startDate, Date endDate, Long promotionId, String filterApprovalStatusCode )
  {
    AssociationRequestCollection claimAssociationRequestCollection = new AssociationRequestCollection();
    claimAssociationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.ALL ) );
    AssociationRequestCollection promotionAssociationRequestCollection = new AssociationRequestCollection();
    promotionAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.APPROVAL_OPTION ) );
    NominationPromotion promotion = (NominationPromotion)getPromotionService().getPromotionById( promotionId );
    List<Approvable> approvableList = new ArrayList<Approvable>();
    Boolean open;
    Boolean expired;
    if ( ApprovalStatusType.PENDING.equals( filterApprovalStatusCode ) )
    {
      open = Boolean.TRUE;
      expired = Boolean.FALSE;
    }
    else if ( ApprovalStatusType.EXPIRED.equals( filterApprovalStatusCode ) )
    {
      // Show open claims/groups that are past approval End Date
      open = Boolean.TRUE;
      expired = Boolean.TRUE;
    }
    else
    {
      // Show open and closed claims where approver was the approver for a particular round
      open = null;
      expired = Boolean.FALSE;
    }

    if ( promotion.isCumulative() )
    {
      AssociationRequestCollection claimGroupAssociationRequestCollection = new AssociationRequestCollection();
      claimGroupAssociationRequestCollection.add( new ClaimGroupAssociationRequest( ClaimGroupAssociationRequest.ALL_WITH_CLAIMS_ALL ) );

      // First collect add any existing claim groups (will only happen for during 2nd and later
      // rounds of approval. For first level approval, the group doesn't yet exist.
      List<PromotionApprovableValue> promotionClaimGroupsValueList = getClaimGroupService().getClaimGroupsForApprovalByUser( UserManager.getUserId(),
                                                                                                                             promotionId != null ? new Long[] { promotionId } : null,
                                                                                                                             open,
                                                                                                                             PromotionType.lookup( PromotionType.NOMINATION ),
                                                                                                                             claimGroupAssociationRequestCollection,
                                                                                                                             promotionAssociationRequestCollection,
                                                                                                                             expired,
                                                                                                                             Boolean.FALSE );

    }

    // Second get the claims where the claim was approved by the user.
    List<PromotionApprovableValue> promotionClaimsValueList = getClaimService().getClaimsForApprovalByUser( UserManager.getUserId(),
                                                                                                            promotionId != null ? new Long[] { promotionId } : null,
                                                                                                            open,
                                                                                                            startDate,
                                                                                                            endDate,
                                                                                                            PromotionType.lookup( PromotionType.NOMINATION ),
                                                                                                            claimAssociationRequestCollection,
                                                                                                            promotionAssociationRequestCollection,
                                                                                                            expired,
                                                                                                            Boolean.FALSE );
    promotionClaimsValueList = getClaimService().getClaimsForApprovalByUser( UserManager.getUserId(),
                                                                             promotionId != null ? new Long[] { promotionId } : null,
                                                                             open,
                                                                             startDate,
                                                                             endDate,
                                                                             PromotionType.lookup( PromotionType.NOMINATION ),
                                                                             claimAssociationRequestCollection,
                                                                             promotionAssociationRequestCollection,
                                                                             expired,
                                                                             Boolean.TRUE );
    if ( !ApprovalStatusType.PENDING.equals( filterApprovalStatusCode ) )
    {
      List<Approvable> filteredApprovableList = new ArrayList<Approvable>();
      Set<Long> dupCheckSet = new HashSet<Long>();
      for ( Approvable approvable : approvableList )
      {
        ApprovableItem approvableItem = (ApprovableItem)approvable.getApprovableItems().iterator().next();
        Set<ApprovableItemApprover> approvableItemApprovers = approvableItem.getApprovableItemApprovers();
        for ( ApprovableItemApprover approvableItemApprover : approvableItemApprovers )
        {
          if ( filterApprovalStatusCode.equals( approvableItemApprover.getApprovalStatusType().getCode() ) )
          {
            approvableItem.setApprovalStatusType( ApprovalStatusType.lookup( filterApprovalStatusCode ) );
            if ( dupCheckSet != null )
            {
              if ( !dupCheckSet.contains( approvableItem.getId() ) )
              {
                filteredApprovableList.add( approvable );
              }
            }
            dupCheckSet.add( approvableItem.getId() );

            break;
          }
        }
      }
      approvableList = filteredApprovableList;
    }
    return approvableList;
  }

  private static ClaimService getClaimService()
  {
    return (ClaimService)getService( ClaimService.BEAN_NAME );
  }

  private static ClaimGroupService getClaimGroupService()
  {
    return (ClaimGroupService)getService( ClaimGroupService.BEAN_NAME );
  }

  private static PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }

  private static SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }

}
