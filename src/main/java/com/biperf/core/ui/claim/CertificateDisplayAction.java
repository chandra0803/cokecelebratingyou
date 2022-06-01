/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/claim/CertificateDisplayAction.java,v $
 *
 */

package com.biperf.core.ui.claim;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.ujac.print.DocumentPrinter;

import com.biperf.core.domain.claim.Claim;
import com.biperf.core.domain.claim.ClaimRecipient;
import com.biperf.core.domain.claim.NominationClaim;
import com.biperf.core.domain.claim.QuizClaim;
import com.biperf.core.domain.claim.RecognitionClaim;
import com.biperf.core.domain.diyquiz.DIYQuiz;
import com.biperf.core.domain.enums.LanguageType;
import com.biperf.core.domain.enums.PromoRecognitionBehaviorType;
import com.biperf.core.domain.enums.PromotionBehaviorType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.multimedia.ECard;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.process.ProcessInvocation;
import com.biperf.core.domain.process.ProcessInvocationParameter;
import com.biperf.core.domain.process.ProcessInvocationParameterValue;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionCert;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.claim.ClaimAssociationRequest;
import com.biperf.core.service.claim.ClaimService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.process.ProcessInvocationService;
import com.biperf.core.service.process.impl.ProcessInvocationAssociationRequest;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.reports.ReportsUtils;
import com.biperf.core.ui.utils.CustomUjacHttpResourceLoader;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.Environment;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.core.utils.LocaleUtils;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.utils.UserManager;
import com.objectpartners.cms.domain.Content;

/**
 * ReportsAction <p/> <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>Ashok Attada</td>
 * <td>Dec 06, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 * @author Ashok Attada
 *
 */
public class CertificateDisplayAction extends BaseDispatchAction
{
  /**
   * to prpare the jasper report file name
   */
  public static final String RECOGNITION_CERTIFICATE = "recognition";
  public static final String NOMINATION_CERTIFICATE = "nomination";
  public static final String QUIZ_CERTIFICATE = "quiz";
  private static Log logger = LogFactory.getLog( CertificateDisplayAction.class );

  /**
   * Get the certificate id for the claim.  This is currently only valid for recognition claims
   * @param claimId
   * @return
   */
  private String lookupCertificateIdForClaim( String claimId )
  {
    Long id = Long.valueOf( claimId );

    if ( id != null )
    {
      Claim claim = getClaimService().getClaimById( id );
      if ( claim.isRecognitionClaim() )
      {
        Long certificateId = ( (RecognitionClaim)claim ).getCertificateId();
        if ( certificateId != null )
        {
          return certificateId.toString();
        }
      }
    }

    return null;
  }

  /**
   * Prepares anything necessary before displaying recognition certificate screen.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  @SuppressWarnings( { "rawtypes", "unchecked", "resource" } )
  public ActionForward showCertificateRecognitionDetail( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    // This flag is used for Product Claimed Summary Reports - we have to do this report in display
    // tags.
    if ( isCancelled( request ) )
    {
      // EARLY EXIT
      return mapping.findForward( ActionConstants.CANCEL_FORWARD );
    }

    CertificateDisplayForm certificateDisplayForm = (CertificateDisplayForm)form;

    String claimItemId = certificateDisplayForm.getRecipientId();
    String claimId = certificateDisplayForm.getClaimId();
    String promotionId2 = certificateDisplayForm.getPromotionId();
    String promotionId = "";
    Long promotionIdLong = 0L;

    if ( promotionId2.indexOf( "." ) != -1 )
    {
      promotionId = promotionId2.substring( 0, promotionId2.indexOf( "." ) );
    }
    else
    {
      promotionId = promotionId2;
    }

    try
    {
      String clientState = RequestUtils.getOptionalParamString( request, "clientState" );
      if ( StringUtils.isNotEmpty( clientState ) )
      {
        String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
        String password = ClientStatePasswordManager.getPassword();

        if ( cryptoPass != null && cryptoPass.equals( "1" ) )
        {
          password = ClientStatePasswordManager.getGlobalPassword();
        }
        // Deserialize the client state.
        Map clientStateMap = ClientStateSerializer.deserialize( clientState, password );
        if ( claimItemId == null )
        {
          Long claimItemIdLong = (Long)clientStateMap.get( "claimItemId" );
          if ( claimItemIdLong != null )
          {
            claimItemId = claimItemIdLong.toString();
          }
        }
        if ( claimId == null )
        {
          Long claimIdLong = (Long)clientStateMap.get( "claimId" );
          if ( claimIdLong != null )
          {
            claimId = claimIdLong.toString();
          }
        }
        if ( promotionId == null )
        {
          promotionIdLong = (Long)clientStateMap.get( "promotionId" );
          if ( promotionIdLong != 0L )
          {
            promotionId = promotionIdLong.toString();
          }
        }
      }
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "required client state parameters userId/claimId/promotionId was missing" );
    }

    if ( claimItemId == null || claimItemId.length() == 0 )
    {
      throw new IllegalArgumentException( "required parameter claimItemId was missing" );
    }

    if ( claimId == null || claimId.length() == 0 )
    {
      throw new IllegalArgumentException( "required parameter claimId was missing" );
    }

    String certificateId = lookupCertificateIdForClaim( claimId );

    Promotion promotion = getPromotionService().getPromotionById( Long.parseLong( promotionId ) );

    AssociationRequestCollection claimAssociationRequestCollection = new AssociationRequestCollection();
    claimAssociationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.CLAIM_RECIPIENTS ) );
    RecognitionClaim recClaim = (RecognitionClaim)getClaimService().getClaimByIdWithAssociations( Long.parseLong( claimId ), claimAssociationRequestCollection );
    try
    {
      String xmlString = buildXmlCertificateRecognition( promotion, recClaim, request.getContextPath(), certificateId );
      logger.debug( "****XML String for recognition certificate is:" + xmlString );
      InputStream templateStream = new ByteArrayInputStream( xmlString.getBytes() );

      Map documentProperties = new HashMap();
      // instantiating the document printer
      Reader reader = new InputStreamReader( templateStream, "UTF-8" );
      DocumentPrinter documentPrinter = new DocumentPrinter( reader, documentProperties );
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
      // generating the document output
      // FileOutputStream pdfStream = new
      // FileOutputStream(Environment.getTmpDir()+"/certOutFile_"+System.currentTimeMillis()+".pdf");
      String outFileName = "certOutFile_recognition_" + recClaim.getPromotion().getId() + "_" + recClaim.getSubmitter().getId() + ".pdf";
      // FileOutputStream pdfStream = new
      // FileOutputStream(Environment.getTmpDir()+"/certOutFile_"+System.currentTimeMillis()+".pdf");
      FileOutputStream pdfStream = new FileOutputStream( Environment.getTmpDir() + "/" + outFileName );
      documentPrinter.printDocument( pdfStream );
      response.setContentType( "application/pdf" );

      // bug fix 50565, properly display pdf in all browsers(we had an issue IE8)
      response.setHeader( "Pragma", "public" );
      response.setHeader( "Expires", "0" );
      response.setHeader( "Cache-Control", "no-store, no-cache" );
      response.setHeader( "Cache-Control", "must-revalidate, post-check=0, pre-check=0" );
      response.setHeader( "Cache-Control", "public" );
      response.setHeader( "Content-Description", "File Transfer" );
      response.setHeader( "Content-Transfer-Encoding", "binary" );

      FileInputStream fis = new FileInputStream( Environment.getTmpDir() + "/" + outFileName );
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
      logger.error( e );
      e.printStackTrace();
    }

    return null;
  }

  @SuppressWarnings( "rawtypes" )
  public String buildXmlCertificateRecognition( Promotion promotion, RecognitionClaim claim, String basePath, String certificateId )
  {
    StringBuilder xmlString = new StringBuilder( "" );
    Participant receipient = null;
    Set receipients = claim.getClaimRecipients();
    String comments = claim.getSubmitterComments();

    Iterator receipientItr = null;
    if ( receipients != null && receipients.size() > 0 )
    {
      receipientItr = receipients.iterator();
    }
    if ( receipientItr != null )
    {
      while ( receipientItr.hasNext() )
      {
        ClaimRecipient receiver = (ClaimRecipient)receipientItr.next();
        receipient = receiver.getRecipient();
      }
    }
    Map<String, String> certificateDetails = new HashMap<String, String>();
    certificateDetails = getCertificateDetailsRecognition( certificateId );

    String imageSource = certificateDetails.get( "imageName" ).toString();

    String header1 = StringUtil.escapeXml( certificateDetails.get( "header1" ).toString() );
    String header2 = StringUtil.escapeXml( certificateDetails.get( "header2" ).toString() );
    String header3 = StringUtil.escapeXml( certificateDetails.get( "header3" ).toString() );
    String header4 = StringUtil.escapeXml( certificateDetails.get( "header4" ).toString() );
    String header5 = StringUtil.escapeXml( certificateDetails.get( "header5" ).toString() );
    String margin = (String)StringUtil.escapeHTML( certificateDetails.get( "margin" ).toString() );

    if ( comments.length() < 750 )
    {
      xmlString.append( "<document size=\"A4\" " + margin + " rotate=\"true\">" );
      xmlString.append( "<font-def name=\"promoNameFont\" family=\"Helvetica\" style=\"bold\" size=\"30\"/>" );
      xmlString.append( "<font-def name=\"smallBold\" family=\"Helvetica\" style=\"bold\" size=\"13\"/>" );
      xmlString.append( "<font-def name=\"small\" family=\"Helvetica\" style=\"normal\" size=\"12\"/>" );
      xmlString.append( "<font-def name=\"normal\" family=\"Helvetica\" size=\"16\" style=\"normal\"/>" );
      xmlString.append( "<font-def name=\"paxNameFont\" family=\"Helvetica\" size=\"22\" style=\"bold\"/>" );

      xmlString.append( "<style-def name=\".certificateAligner\" style=\"padding:20; padding-top:10; padding-bottom:10; line-spacing:1.2; \"/>" );
    }
    else if ( claim.getBehavior() != null )
    {
      xmlString.append( "<document size=\"A4\" " + margin + " rotate=\"true\">" );
      xmlString.append( "<font-def name=\"promoNameFont\" family=\"Helvetica\" style=\"bold\" size=\"23\"/>" );
      xmlString.append( "<font-def name=\"smallBold\" family=\"Helvetica\" style=\"bold\" size=\"11\"/>" );
      if ( comments.length() <= 1450 )
      {
        xmlString.append( "<font-def name=\"small\" family=\"Helvetica\" style=\"normal\" size=\"10\"/>" );
      }
      else
      {
        String fontSize = buildCommentFontSizeBasedOnCommentLength( comments.length() );
        xmlString.append( "<font-def name=\"small\" family=\"Helvetica\" style=\"normal\" size=\"" + fontSize + "\"/>" );
      }

      xmlString.append( "<font-def name=\"normal\" family=\"Helvetica\" size=\"11\" style=\"normal\"/>" );
      xmlString.append( "<font-def name=\"paxNameFont\" family=\"Helvetica\" size=\"16\" style=\"bold\"/>" );

      xmlString.append( "<style-def name=\".certificateAligner\" style=\"padding:20; padding-top:10; padding-bottom:10; line-spacing:1.2; \"/>" );
    }
    else
    {
      xmlString.append( "<document size=\"A4\" " + margin + " rotate=\"true\">" );
      xmlString.append( "<font-def name=\"promoNameFont\" family=\"Helvetica\" style=\"bold\" size=\"24\"/>" );
      xmlString.append( "<font-def name=\"smallBold\" family=\"Helvetica\" style=\"bold\" size=\"12\"/>" );
      if ( comments.length() <= 1450 )
      {
        xmlString.append( "<font-def name=\"small\" family=\"Helvetica\" style=\"normal\" size=\"10\"/>" );
      }
      else
      {
        String fontSize = buildCommentFontSizeBasedOnCommentLength( comments.length() );
        xmlString.append( "<font-def name=\"small\" family=\"Helvetica\" style=\"normal\" size=\"" + fontSize + "\"/>" );
      }
      xmlString.append( "<font-def name=\"normal\" family=\"Helvetica\" size=\"12\" style=\"normal\"/>" );
      xmlString.append( "<font-def name=\"paxNameFont\" family=\"Helvetica\" size=\"17\" style=\"bold\"/>" );

      xmlString.append( "<style-def name=\".certificateAligner\" style=\"padding:20; padding-top:10; padding-bottom:10; line-spacing:1.2; \"/>" );
    }

    Date submitDate = claim.getSubmissionDate();
    String promotionName = StringUtil.escapeXml( promotion.getName() );
    String submitDateString = "";
    String promoNameFont = "promoNameFont";
    String normalFont = "normal";
    String paxFont = "paxNameFont";
    String commentsLabelFont = "smallBold";
    String commentsFont = "small";
    String behaviorFont = "normal";

    String resultComment = "";
    if ( !StringUtils.isEmpty( comments ) )
    {
      resultComment = removeFormatting( comments );
    }

    xmlString.append( "<image halign=\"center\" textwrap=\"true\" underlying=\"true\" source=\"" + imageSource + "\" height=\"595\" width=\"792\" rotate=\"0\" />" );

    xmlString.append( "<paragraph font=\"" + promoNameFont + "\" halign=\"left\" line-spacing=\"1.0\" spacing-before=\"100\">" );

    xmlString.append( promotionName );

    xmlString.append( "\\n" );

    xmlString.append( "</paragraph>" );

    xmlString.append( "<paragraph halign=\"left\" line-spacing=\"1.5\" spacing-before=\"0\">" );

    xmlString.append( "<phrase font=\"" + normalFont + "\">" );

    if ( receipient != null )
    {
      xmlString.append( header2 );
      xmlString.append( "</phrase>" );
      xmlString.append( "<phrase font=\"" + paxFont + "\">" );
      xmlString.append( "\\n" + StringUtil.escapeXml( receipient.getFirstName() ) + " " + StringUtil.escapeXml( receipient.getLastName() ) );
      xmlString.append( "</phrase>" );
      submitDateString = DateUtils
          .toDisplayString( submitDate, LocaleUtils.getLocale( receipient.getLanguageType() == null ? UserManager.getDefaultLocale().toString() : receipient.getLanguageType().getCode() ) );
    }
    if ( claim.getBehavior() != null )
    {
      xmlString.append( "<phrase font=\"" + behaviorFont + "\" no-wrap=\"true\">" );
      xmlString.append( "\\n" + header3 + " " + StringUtil.escapeXml( claim.getBehavior().getName() ) );
      xmlString.append( "</phrase>" );

    }
    xmlString.append( "<phrase font=\"" + normalFont + "\" no-wrap=\"true\">" );
    xmlString.append( "\\n" + header4 + " " + submitDateString );
    xmlString.append( "\\n" + header1 + " " + StringUtil.escapeXml( claim.getSubmitter().getNameFLNoComma() ) );
    xmlString.append( "</phrase>" );
    xmlString.append( "</paragraph>" );
    xmlString.append( "<paragraph halign=\"justified\" line-spacing=\"0.8\" spacing-before=\"5\" class=\"certificateAligner\">" );

    xmlString.append( "<phrase font=\"" + commentsLabelFont + "\" no-wrap=\"true\" trim-body=\"true\">" );
    xmlString.append( "\\n" + header5 );
    xmlString.append( "</phrase>" );

    xmlString.append( "<phrase font=\"" + commentsFont + "\" no-wrap=\"false\" trim-body=\"true\">" );
    xmlString.append( "\\ " );
    String commentsString = resultComment;
    int count = 0;
    while ( commentsString.length() > 0 )
    {
      int subStringCount = 80;
      String resultCommentSub = "";
      if ( count > 0 )
      {
        subStringCount = 90;
      }
      if ( commentsString.length() >= subStringCount )
      {
        resultCommentSub = commentsString.substring( 0, subStringCount );
        int lastSpaceIndexComments = resultCommentSub.lastIndexOf( " " );
        if ( lastSpaceIndexComments > 0 )
        {
          resultCommentSub = resultCommentSub.substring( 0, lastSpaceIndexComments );
        }
      }
      else
      {
        resultCommentSub = commentsString;
      }

      xmlString.append( resultCommentSub );
      resultComment = resultComment.substring( resultCommentSub.length(), resultComment.length() );
      commentsString = resultComment;
      count++;
    }

    xmlString.append( "</phrase>" );
    xmlString.append( "</paragraph>" );

    xmlString.append( "</document>" );
    return xmlString.toString();
  }

  /**
   * Prepares anything necessary before displaying nomination certificate screen.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  @SuppressWarnings( { "rawtypes", "unchecked", "resource" } )
  public ActionForward showCertificateNominationDetail( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    // This flag is used for Product Claimed Summary Reports - we have to do this report in display
    // tags.
    // This flag is used for Product Claimed Summary Reports - we have to do this report in display
    // tags.

    if ( isCancelled( request ) )
    {
      // EARLY EXIT
      return mapping.findForward( ActionConstants.CANCEL_FORWARD );
    }

    CertificateDisplayForm certificateDisplayForm = (CertificateDisplayForm)form;

    String userId = certificateDisplayForm.getUserId();
    String claimId = certificateDisplayForm.getClaimId();
    String promotionId2 = certificateDisplayForm.getPromotionId();
    String promotionId = "";
    Long promotionIdLong = 0L;

    if ( promotionId2.indexOf( "." ) != -1 )
    {
      promotionId = promotionId2.substring( 0, promotionId2.indexOf( "." ) );
    }
    else
    {
      promotionId = promotionId2;
    }

    try
    {
      String clientState = RequestUtils.getOptionalParamString( request, "clientState" );
      if ( StringUtils.isNotEmpty( clientState ) )
      {
        String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
        String password = ClientStatePasswordManager.getPassword();

        if ( cryptoPass != null && cryptoPass.equals( "1" ) )
        {
          password = ClientStatePasswordManager.getGlobalPassword();
        }
        // Deserialize the client state.
        Map clientStateMap = ClientStateSerializer.deserialize( clientState, password );
        if ( userId == null )
        {
          Long userIdLong = (Long)clientStateMap.get( "userId" );
          if ( userIdLong != null )
          {
            userId = userIdLong.toString();
          }
        }
        if ( claimId == null )
        {
          Long claimIdLong = (Long)clientStateMap.get( "claimId" );
          if ( claimIdLong != null )
          {
            claimId = claimIdLong.toString();
          }
        }
        if ( promotionId == null )
        {
          promotionIdLong = (Long)clientStateMap.get( "promotionId" );
          if ( promotionIdLong != 0L )
          {
            promotionId = promotionIdLong.toString();
          }
        }
      }
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "required client state parameters userId/claimId/promotionId was missing" );
    }

    if ( userId == null || userId.length() == 0 )
    {
      throw new IllegalArgumentException( "required parameter userId was missing" );
    }

    if ( claimId == null || claimId.length() == 0 )
    {
      throw new IllegalArgumentException( "required parameter claimId was missing" );
    }

    Promotion promotion = getPromotionService().getPromotionById( Long.parseLong( promotionId ) );

    AssociationRequestCollection claimAssociationRequestCollection = new AssociationRequestCollection();
    claimAssociationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.CLAIM_RECIPIENTS ) );
    NominationClaim nominationClaim = (NominationClaim)getClaimService().getClaimByIdWithAssociations( Long.parseLong( claimId ), claimAssociationRequestCollection );

    Long certificateLongId = nominationClaim.getCertificateId();
    String certificateId = certificateLongId != null ? certificateLongId.toString() : null;

    try
    {
      String xmlString = buildXmlCertificateNomination( promotion, nominationClaim, request.getContextPath(), certificateId, userId );
      logger.debug( "****XML String for nomination certificate is:" + xmlString );
      InputStream templateStream = new ByteArrayInputStream( xmlString.getBytes() );

      Map documentProperties = new HashMap();
      // instantiating the document printer
      Reader reader = new InputStreamReader( templateStream, "UTF-8" );
      DocumentPrinter documentPrinter = new DocumentPrinter( reader, documentProperties );
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
      // generating the document output
      String outFileName = "certOutFile_nomination_" + nominationClaim.getPromotion().getId() + "_" + nominationClaim.getSubmitter().getId() + ".pdf";
      FileOutputStream pdfStream = new FileOutputStream( Environment.getTmpDir() + "/" + outFileName );
      documentPrinter.printDocument( pdfStream );
      response.setContentType( "application/pdf" );

      response.setHeader( "Pragma", "public" );
      response.setHeader( "Expires", "0" );
      response.setHeader( "Cache-Control", "no-store, no-cache" );
      response.setHeader( "Cache-Control", "must-revalidate, post-check=0, pre-check=0" );
      response.setHeader( "Cache-Control", "public" );
      response.setHeader( "Content-Description", "File Transfer" );
      response.setHeader( "Content-Transfer-Encoding", "binary" );

      FileInputStream fis = new FileInputStream( Environment.getTmpDir() + "/" + outFileName );
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
      logger.error( e );
      e.printStackTrace();
    }

    return null;
  }

  @SuppressWarnings( { "rawtypes", "resource" } )
  public ActionForward showCertificateQuizDetail( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    // This flag is used for Product Claimed Summary Reports - we have to do this report in display
    // tags.

    if ( isCancelled( request ) )
    {
      // EARLY EXIT
      return mapping.findForward( ActionConstants.CANCEL_FORWARD );
    }

    CertificateDisplayForm certificateDisplayForm = (CertificateDisplayForm)form;

    String userId = certificateDisplayForm.getUserId();
    String claimId = certificateDisplayForm.getClaimId();
    String promotionId2 = certificateDisplayForm.getPromotionId();
    String promotionId = "";

    if ( promotionId2.indexOf( "." ) != -1 )
    {
      promotionId = promotionId2.substring( 0, promotionId2.indexOf( "." ) );
    }
    else
    {
      promotionId = promotionId2;
    }
    try
    {
      String clientState = RequestUtils.getOptionalParamString( request, "clientState" );
      if ( StringUtils.isNotEmpty( clientState ) )
      {
        String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
        String password = ClientStatePasswordManager.getPassword();

        if ( cryptoPass != null && cryptoPass.equals( "1" ) )
        {
          password = ClientStatePasswordManager.getGlobalPassword();
        }
        // Deserialize the client state.
        Map clientStateMap = ClientStateSerializer.deserialize( clientState, password );
        if ( userId == null )
        {
          Long userIdLong = (Long)clientStateMap.get( "userId" );
          if ( userIdLong != null )
          {
            userId = userIdLong.toString();
          }
        }
        if ( claimId == null )
        {
          Long claimIdLong = (Long)clientStateMap.get( "claimId" );
          if ( claimIdLong != null )
          {
            claimId = claimIdLong.toString();
          }
        }
        if ( promotionId == null )
        {
          Long promotionIdLong = (Long)clientStateMap.get( "promotionId" );
          if ( promotionIdLong != null )
          {
            promotionId = promotionIdLong.toString();
          }
        }
      }
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "required client state parameters userId/claimId/promotionId was missing" );
    }

    if ( userId == null || userId.length() == 0 )
    {
      throw new IllegalArgumentException( "required parameter userId was missing" );
    }
    if ( claimId == null || claimId.length() == 0 )
    {
      throw new IllegalArgumentException( "required parameter claimId was missing" );
    }
    if ( promotionId == null || promotionId.length() == 0 )
    {
      throw new IllegalArgumentException( "required parameter promotionId was missing" );
    }

    QuizClaim quizClaim = (QuizClaim)getClaimService().getClaimById( Long.parseLong( claimId ) );

    String certificateId = null;
    if ( quizClaim.getPromotion().isDIYQuizPromotion() )
    {
      DIYQuiz quiz = (DIYQuiz)quizClaim.getQuiz();
      PromotionCert promoCert = quiz.getCertificate();
      certificateId = promoCert.getCertificateId();
    }
    else
    {
      certificateId = quizClaim.getPromotion().getCertificate();
    }
    try
    {
      String xmlString = buildXmlCertificateQuiz( quizClaim.getPromotion(), quizClaim, request.getContextPath(), certificateId );
      logger.debug( "****XML String for quiz certificate is:" + xmlString );
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
      // documentPrinter.setResourceLoader(new
      // ClassPathResourceLoader(this.getClass().getClassLoader()));
      // generating the document output
      String outFileName = "certOutFile_quiz_" + quizClaim.getPromotion().getId() + "_" + quizClaim.getSubmitter().getId() + ".pdf";
      // FileOutputStream pdfStream = new
      // FileOutputStream(Environment.getTmpDir()+"/certOutFile_"+System.currentTimeMillis()+".pdf");

      // if(logger.isDebugEnabled())
      logger.error( "Output File [" + Environment.getTmpDir() + "/" + outFileName + "]" );

      FileOutputStream pdfStream = new FileOutputStream( Environment.getTmpDir() + "/" + outFileName );
      documentPrinter.printDocument( pdfStream );

      // if(logger.isDebugEnabled())
      logger.error( "After printing document" );

      response.setContentType( "application/pdf" );

      FileInputStream fis = new FileInputStream( Environment.getTmpDir() + "/" + outFileName );
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
      logger.error( e );
      e.printStackTrace();
    }

    // return mapping.findForward( forwardAction );
    return null;
  }

  public String buildXmlCertificateQuiz( Promotion promotion, QuizClaim claim, String basePath, String certificateId )
  {
    StringBuilder xmlString = new StringBuilder( "" );
    Participant receipient = claim.getSubmitter();

    Map<String, String> certificateDetails = new HashMap<String, String>();
    certificateDetails = getCertificateDetailsQuiz( certificateId );

    String imageSource = certificateDetails.get( "imageName" ).toString();
    String header1 = StringUtil.escapeXml( certificateDetails.get( "header1" ).toString() );
    String header2 = StringUtil.escapeXml( certificateDetails.get( "header2" ).toString() );
    String header3 = StringUtil.escapeXml( certificateDetails.get( "header3" ).toString() );
    String header4 = StringUtil.escapeXml( certificateDetails.get( "header4" ).toString() );

    xmlString.append( "<document size=\"A4\" margin-left=\"0\" margin-right=\"0\" margin-top=\"0\" margin-bottom=\"0\" rotate=\"true\">" );
    xmlString.append( "<font-def name=\"header\" family=\"Helvetica\" style=\"bold\" size=\"32\"/>" );
    xmlString.append( "<font-def name=\"small\" family=\"Helvetica\" style=\"normal\" size=\"12\"/>" );
    xmlString.append( "<font-def name=\"normal\" family=\"Helvetica\" size=\"22\" style=\"normal\"/>" );
    xmlString.append( "<font-def name=\"paxNameFont\" family=\"Helvetica\" size=\"26\" style=\"bold\"/>" );

    Date submitDate = claim.getSubmissionDate();
    String submitDateString = "";

    xmlString.append( "<image halign=\"center\" textwrap=\"true\" underlying=\"true\" source=\"" + imageSource + "\" height=\"600\" width=\"792\" rotate=\"0\" />" );
    xmlString.append( "<paragraph font=\"header\" halign=\"center\" line-spacing=\"2.5\" spacing-before=\"5\">" );
    xmlString.append( "\\n" + header1 );
    xmlString.append( "</paragraph>" );

    xmlString.append( "<paragraph halign=\"center\" line-spacing=\"1.0\" spacing-before=\"10\">" );
    xmlString.append( "<phrase font=\"normal\">" );

    if ( receipient != null )
    {
      xmlString.append( "\\n" + header2 );
      xmlString.append( "</phrase>" );
      xmlString.append( "<phrase font=\"paxNameFont\">" );
      xmlString.append( "\\n\\n" + StringUtil.escapeXml( receipient.getFirstName() ) + " " + StringUtil.escapeXml( receipient.getLastName() ) );
      xmlString.append( "</phrase>" );
      submitDateString = DateUtils
          .toDisplayString( submitDate, LocaleUtils.getLocale( receipient.getLanguageType() == null ? UserManager.getDefaultLocale().toString() : receipient.getLanguageType().getCode() ) );
    }
    xmlString.append( "<phrase font=\"normal\">" );
    xmlString.append( "\\n\\n" + header3 + " " );
    xmlString.append( "</phrase>" );
    xmlString.append( "<phrase font=\"paxNameFont\" no-wrap=\"true\">" );
    if ( promotion.isDIYQuizPromotion() )
    {
      xmlString.append( "\\n\\n" + StringUtil.escapeXml( claim.getQuiz().getName() ) );
    }
    else
    {
      xmlString.append( "\\n\\n" + StringUtil.escapeXml( promotion.getName() ) );
    }
    xmlString.append( "</phrase>" );
    xmlString.append( "<phrase font=\"normal\">" );
    xmlString.append( "\\n\\n" + header4 + " " + submitDateString );

    xmlString.append( "</phrase>" );
    xmlString.append( "</paragraph>" );
    xmlString.append( "<new-page/>" );

    xmlString.append( "</document>" );
    return xmlString.toString();
  }

  @SuppressWarnings( "rawtypes" )
  public String buildXmlCertificateNomination( Promotion promotion, NominationClaim claim, String basePath, String certificateId, String userId )
  {
    StringBuilder xmlString = new StringBuilder( "" );
    Participant receipient = null;
    Set receipients = claim.getClaimRecipients();
    String comments = claim.getSubmitterComments();

    Iterator receipientItr = null;
    if ( receipients != null && receipients.size() > 0 )
    {
      receipientItr = receipients.iterator();
    }
    if ( receipientItr != null )
    {
      while ( receipientItr.hasNext() )
      {
        ClaimRecipient receiver = (ClaimRecipient)receipientItr.next();
        if ( !StringUtils.isEmpty( userId ) && receiver.getRecipient().getId().equals( Long.parseLong( userId ) ) )
        {
          receipient = receiver.getRecipient();
        }
      }
    }
    if ( !StringUtils.isEmpty( userId ) && receipient == null )
    {
      receipient = getParticipantService().getParticipantById( Long.parseLong( userId ) );
    }

    Map<String, String> certificateDetails = new HashMap<String, String>();
    certificateDetails = getCertificateDetailsNomination( certificateId );

    // removed unwanted toString() method call to avoid NullPointerException and map is of String
    // type
    String imageSource = StringUtil.escapeXml( certificateDetails.get( "imageName" ) );
    String header1 = StringUtil.escapeXml( certificateDetails.get( "header1" ) );
    String header2 = StringUtil.escapeXml( certificateDetails.get( "header2" ) );
    String header3 = StringUtil.escapeXml( certificateDetails.get( "header3" ) );
    String header4 = StringUtil.escapeXml( certificateDetails.get( "header4" ) );
    String header5 = StringUtil.escapeXml( certificateDetails.get( "header5" ) );
    String margin = (String)StringUtil.escapeHTML( certificateDetails.get( "margin" ) );

    if ( comments.length() < 750 )
    {
      xmlString.append( "<document size=\"A4\" " + margin + " rotate=\"true\">" );
      xmlString.append( "<font-def name=\"promoNameFont\" family=\"Helvetica\" style=\"bold\" size=\"30\"/>" );
      xmlString.append( "<font-def name=\"smallBold\" family=\"Helvetica\" style=\"bold\" size=\"13\"/>" );
      xmlString.append( "<font-def name=\"small\" family=\"Helvetica\" style=\"normal\" size=\"12\"/>" );
      xmlString.append( "<font-def name=\"normal\" family=\"Helvetica\" size=\"16\" style=\"normal\"/>" );
      xmlString.append( "<font-def name=\"paxNameFont\" family=\"Helvetica\" size=\"22\" style=\"bold\"/>" );

      xmlString.append( "<style-def name=\".certificateAligner\" style=\"padding:20; padding-top:10; padding-bottom:10; line-spacing:1.2; \"/>" );
    }
    else
    {
      xmlString.append( "<document size=\"A4\" " + margin + " rotate=\"true\">" );
      xmlString.append( "<font-def name=\"promoNameFont\" family=\"Helvetica\" style=\"bold\" size=\"24\"/>" );
      xmlString.append( "<font-def name=\"smallBold\" family=\"Helvetica\" style=\"bold\" size=\"12\"/>" );
      xmlString.append( "<font-def name=\"small\" family=\"Helvetica\" style=\"normal\" size=\"11\"/>" );
      xmlString.append( "<font-def name=\"normal\" family=\"Helvetica\" size=\"12\" style=\"normal\"/>" );
      xmlString.append( "<font-def name=\"paxNameFont\" family=\"Helvetica\" size=\"17\" style=\"bold\"/>" );

      xmlString.append( "<style-def name=\".certificateAligner\" style=\"padding:20; padding-top:10; padding-bottom:10; line-spacing:1.2; \"/>" );
    }

    Date submitDate = claim.getSubmissionDate();
    String promotionName = StringUtil.escapeXml( promotion.getName() );
    String submitDateString = "";
    String promoNameFont = "promoNameFont";
    String normalFont = "normal";
    String paxFont = "paxNameFont";
    String commentsFont = "small";
    String commentsLabelFont = "smallBold";
    String behaviorFont = "normal";

    String resultComment = "";
    if ( !StringUtils.isEmpty( comments ) )
    {
      resultComment = removeFormatting( comments );
    }

    xmlString.append( "<image halign=\"center\" textwrap=\"true\" underlying=\"true\" source=\"" + imageSource + "\" height=\"612\" width=\"792\" rotate=\"0\" />" );

    xmlString.append( "<paragraph font=\"" + promoNameFont + "\" halign=\"left\" line-spacing=\"1.0\" spacing-before=\"100\">" );

    xmlString.append( promotionName );

    xmlString.append( "\\n" );

    xmlString.append( "</paragraph>" );

    xmlString.append( "<paragraph halign=\"left\" line-spacing=\"1.5\" spacing-before=\"0\">" );

    xmlString.append( "<phrase font=\"" + normalFont + "\">" );

    if ( receipient != null )
    {
      xmlString.append( header2 );
      xmlString.append( "</phrase>" );
      xmlString.append( "<phrase font=\"" + paxFont + "\">" );
      xmlString.append( "\\n" + StringUtil.escapeXml( receipient.getFirstName() ) + " " + StringUtil.escapeXml( receipient.getLastName() ) );
      xmlString.append( "</phrase>" );
      submitDateString = DateUtils
          .toDisplayString( submitDate, LocaleUtils.getLocale( receipient.getLanguageType() == null ? UserManager.getDefaultLocale().toString() : receipient.getLanguageType().getCode() ) );
    }
    if ( claim.getBehavior() != null )
    {
      xmlString.append( "<phrase font=\"" + behaviorFont + "\" no-wrap=\"true\">" );
      xmlString.append( "\\n" + header3 + " " + StringUtil.escapeXml( claim.getBehavior().getName() ) );
      xmlString.append( "</phrase>" );

    }
    xmlString.append( "<phrase font=\"" + normalFont + "\" no-wrap=\"true\">" );
    xmlString.append( "\\n" + header4 + " " + submitDateString );
    xmlString.append( "\\n" + header1 + " " + StringUtil.escapeXml( claim.getSubmitter().getNameFLNoComma() ) );
    xmlString.append( "</phrase>" );
    xmlString.append( "</paragraph>" );
    xmlString.append( "<paragraph halign=\"justified\" line-spacing=\"0.8\" spacing-before=\"5\" class=\"certificateAligner\">" );

    xmlString.append( "<phrase font=\"" + commentsLabelFont + "\" no-wrap=\"true\" trim-body=\"true\">" );
    xmlString.append( "\\n" + header5 );
    xmlString.append( "</phrase>" );

    xmlString.append( "<phrase font=\"" + commentsFont + "\" no-wrap=\"false\" trim-body=\"true\">" );
    xmlString.append( "\\ " );

    String commentsString = resultComment;
    int count = 0;
    while ( commentsString.length() > 0 )
    {
      int subStringCount = 80;
      String resultCommentSub = "";
      if ( count > 0 )
      {
        subStringCount = 90;
      }
      if ( commentsString.length() >= subStringCount )
      {
        resultCommentSub = commentsString.substring( 0, subStringCount );
        int lastSpaceIndexComments = resultCommentSub.lastIndexOf( " " );
        if ( lastSpaceIndexComments > 0 )
        {
          resultCommentSub = resultCommentSub.substring( 0, lastSpaceIndexComments );
        }
      }
      else
      {
        resultCommentSub = commentsString;
      }
      xmlString.append( resultCommentSub );
      resultComment = resultComment.substring( resultCommentSub.length(), resultComment.length() );
      commentsString = resultComment;
      count++;
    }

    xmlString.append( "</phrase>" );
    xmlString.append( "</paragraph>" );

    xmlString.append( "</document>" );
    return xmlString.toString();
  }

  // Recognition
  public Map<String, String> getCertificateDetailsRecognition( String certificateId )
  {
    Map<String, String> certificateDetails = new HashMap<String, String>();
    String certImageName = "";
    String certAwardedToHeader = "";
    String header1 = "";
    String header3 = "";
    String header4 = "";
    String header5 = "";
    String margin = "";
    Content certificateContent = ReportsUtils.getCertificateContent( PromotionType.RECOGNITION, certificateId );
    if ( certificateContent != null )
    {
      certImageName = (String)certificateContent.getContentDataMap().get( "LARGE_IMAGE" );
      header1 = (String)certificateContent.getContentDataMap().get( "COPY3" );
      certAwardedToHeader = (String)certificateContent.getContentDataMap().get( "HEADER2" );
      header3 = (String)certificateContent.getContentDataMap().get( "HEADER3" );
      header4 = (String)certificateContent.getContentDataMap().get( "COPY4" );
      header5 = (String)certificateContent.getContentDataMap().get( "COPY5" );
      margin = (String)certificateContent.getContentDataMap().get( "PAGE_MARGIN" );

      certificateDetails.put( "imageName", certImageName );
      certificateDetails.put( "header2", certAwardedToHeader );
      certificateDetails.put( "header3", header3 );
      certificateDetails.put( "header4", header4 );
      certificateDetails.put( "header5", header5 );
      certificateDetails.put( "header1", header1 );
      certificateDetails.put( "margin", margin );

    }

    return certificateDetails;
  }

  // Nomination
  public Map<String, String> getCertificateDetailsNomination( String certificateId )
  {
    Map<String, String> certificateDetails = new HashMap<String, String>();
    String certImageName = "";
    String header1 = "";
    String header2 = "";
    String header3 = "";
    String header4 = " ";
    String header5 = " ";
    String margin = "";
    Content certificateContent = ReportsUtils.getCertificateContent( PromotionType.NOMINATION, certificateId );

    if ( certificateContent != null )
    {
      certImageName = (String)certificateContent.getContentDataMap().get( "LARGE_IMAGE" );
      if ( certImageName == null )
      {
        certImageName = certificateContent.getContentDataMap().get( "CERT_NAME" ) + "-full.jpg";
      }
      header1 = (String)certificateContent.getContentDataMap().get( "COPY1" );
      header2 = (String)certificateContent.getContentDataMap().get( "HEADER2" );
      header3 = (String)certificateContent.getContentDataMap().get( "HEADER3" );
      header4 = (String)certificateContent.getContentDataMap().get( "COPY4" );
      header5 = (String)certificateContent.getContentDataMap().get( "COPY5" );
      margin = (String)certificateContent.getContentDataMap().get( "PAGE_MARGIN" );

      certificateDetails.put( "imageName", certImageName );
      certificateDetails.put( "header1", header1 );
      certificateDetails.put( "header2", header2 );
      certificateDetails.put( "header3", header3 );
      certificateDetails.put( "header4", header4 );
      certificateDetails.put( "header5", header5 );
      certificateDetails.put( "margin", margin );
    }

    return certificateDetails;
  }

  // Build Map for Quiz certificate

  public Map<String, String> getCertificateDetailsQuiz( String certificateId )
  {
    Map<String, String> certificateDetails = new HashMap<String, String>();
    String certImageName = "";

    String header1 = "";
    String header2 = "";
    String header3 = "";
    String header4 = " ";
    String header5 = " ";
    Content certificateContent = ReportsUtils.getCertificateContent( PromotionType.QUIZ, certificateId );
    if ( certificateContent != null )
    {
      certImageName = (String)certificateContent.getContentDataMap().get( "LARGE_IMAGE" );
      header1 = (String)certificateContent.getContentDataMap().get( "CERTIFICATE_OF_REPORT_TYPE_HEADING" );
      header2 = (String)certificateContent.getContentDataMap().get( "HEADER2" );
      header3 = (String)certificateContent.getContentDataMap().get( "HEADER3" );
      header4 = (String)certificateContent.getContentDataMap().get( "COPY4" );
      header5 = (String)certificateContent.getContentDataMap().get( "COPY6" );

      certificateDetails.put( "imageName", certImageName );
      certificateDetails.put( "header1", header1 );
      certificateDetails.put( "header2", header2 );
      certificateDetails.put( "header3", header3 );
      certificateDetails.put( "header4", header4 );
      certificateDetails.put( "header5", header5 );

    }

    return certificateDetails;
  }

  // Bug fix #65296
  private String removeFormatting( String comments )
  {
    final Pattern REMOVE_TAGS = Pattern.compile( "<.+?>" );

    comments = comments.replaceAll( "<b>", " &lt;b&gt;" );
    comments = comments.replaceAll( "</b>", "&lt;/b&gt;" );
    comments = comments.replaceAll( "<i>", " &lt;i&gt;" );
    comments = comments.replaceAll( "</i>", "&lt;/i&gt;" );
    comments = comments.replaceAll( "<u>", " &lt;u&gt;" );
    comments = comments.replaceAll( "</u>", "&lt;/u&gt;" );

    comments = comments.replaceAll( "&nbsp;", "\\ " );
    comments = comments.replaceAll( "&apos;", "'" );
    comments = comments.replaceAll( "&#39;", "'" );

    comments = REMOVE_TAGS.matcher( comments ).replaceAll( " " );
    comments = StringEscapeUtils.unescapeHtml4( comments );

    comments = comments.replaceAll( "&", "&amp;" );

    return comments;
  }

  /**
   * Test Recognition Certificate from AdminTestRecogReceivedEmailProcess.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  @SuppressWarnings( { "rawtypes", "unchecked", "resource" } )
  public ActionForward adminTestCertificateRecognitionDetail( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {

    String promotionId = "";
    String certificateId = "";
    String behavior = "";
    String recipientId = "";
    String submitterId = "";
    String processInvocationId = "";
    String certLocale = "";
    try
    {
      String clientState = RequestUtils.getOptionalParamString( request, "clientState" );
      if ( StringUtils.isNotEmpty( clientState ) )
      {
        String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
        String password = ClientStatePasswordManager.getPassword();

        if ( cryptoPass != null && cryptoPass.equals( "1" ) )
        {
          password = ClientStatePasswordManager.getGlobalPassword();
        }
        // Deserialize the client state.
        Map clientStateMap = ClientStateSerializer.deserialize( clientState, password );
        Long processInvocationIdLong = (Long)clientStateMap.get( "processInvocationId" );
        if ( processInvocationIdLong != null )
        {
          processInvocationId = processInvocationIdLong.toString();
        }
        String certificateIdString = (String)clientStateMap.get( "certificateId" );
        if ( certificateIdString != null )
        {
          certificateId = certificateIdString.toString();
        }
        Long recipientIdLong = (Long)clientStateMap.get( "recipientId" );
        if ( recipientIdLong != 0L )
        {
          recipientId = recipientIdLong.toString();
        }
        Long submitterIdLong = (Long)clientStateMap.get( "submitterId" );
        if ( submitterIdLong != 0L )
        {
          submitterId = submitterIdLong.toString();
        }
        Long promotionIdLong = (Long)clientStateMap.get( "promotionId" );
        if ( promotionIdLong != 0L )
        {
          promotionId = promotionIdLong.toString();
        }
        String behaviorString = (String)clientStateMap.get( "behavior" );
        if ( !StringUtils.isEmpty( behaviorString ) )
        {
          behavior = behaviorString;
        }
        String certLocaleString = (String)clientStateMap.get( "certLocale" );
        if ( !StringUtils.isEmpty( certLocaleString ) )
        {
          certLocale = certLocaleString;
        }
      }
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "required client state parameters was missing" );
    }

    if ( promotionId == null || promotionId.length() == 0 )
    {
      throw new IllegalArgumentException( "required parameter promotionId was missing" );
    }

    if ( certificateId == null || certificateId.length() == 0 )
    {
      throw new IllegalArgumentException( "required parameter certificateId was missing" );
    }

    if ( submitterId == null || submitterId.length() == 0 )
    {
      throw new IllegalArgumentException( "required parameter submitterId was missing" );
    }

    if ( recipientId == null || recipientId.length() == 0 )
    {
      throw new IllegalArgumentException( "required parameter recipientId was missing" );
    }

    Promotion promotion = getPromotionService().getPromotionById( Long.parseLong( promotionId ) );

    RecognitionClaim recClaim = new RecognitionClaim();
    recClaim.setPromotion( promotion );
    Participant submitterPax = getParticipantService().getParticipantById( new Long( submitterId ) );
    Participant recipientPax = getParticipantService().getParticipantById( new Long( recipientId ) );
    recipientPax.setLanguageType( LanguageType.lookup( certLocale ) );

    recClaim.setSubmissionDate( new Date() );
    if ( !StringUtils.isEmpty( behavior ) )
    {
      PromotionBehaviorType promotionBehaviorType = PromoRecognitionBehaviorType.lookup( behavior );
      recClaim.setBehavior( promotionBehaviorType );
    }
    Set claimRecipients = new LinkedHashSet();
    ClaimRecipient claimRecipient = new ClaimRecipient();
    claimRecipient.setRecipient( recipientPax );
    claimRecipients.add( claimRecipient );
    recClaim.setClaimRecipients( claimRecipients );
    recClaim.setSubmitter( submitterPax );

    AssociationRequestCollection assocReqs = new AssociationRequestCollection();
    assocReqs.add( new ProcessInvocationAssociationRequest( ProcessInvocationAssociationRequest.ALL ) );
    ProcessInvocation processInvocation = getProcessInvocationService().getProcessInvocationById( new Long( processInvocationId ), assocReqs );
    String submitterComments = "";
    for ( Object object : processInvocation.getProcessInvocationParameters() )
    {
      ProcessInvocationParameter param = (ProcessInvocationParameter)object;
      if ( param.getProcessParameterName().equals( "submitterComments" ) )
      {
        ProcessInvocationParameterValue paramValue = (ProcessInvocationParameterValue)param.getProcessInvocationParameterValues().iterator().next();
        submitterComments = paramValue.getValue();
      }
    }
    recClaim.setSubmitterComments( submitterComments );

    try
    {
      String xmlString = buildXmlCertificateRecognition( promotion, recClaim, request.getContextPath(), certificateId );
      logger.debug( "****XML String for recognition certificate is:" + xmlString );
      InputStream templateStream = new ByteArrayInputStream( xmlString.getBytes() );

      Map documentProperties = new HashMap();
      // instantiating the document printer
      Reader reader = new InputStreamReader( templateStream, "UTF-8" );
      DocumentPrinter documentPrinter = new DocumentPrinter( reader, documentProperties );
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
      // generating the document output
      // FileOutputStream pdfStream = new
      // FileOutputStream(Environment.getTmpDir()+"/certOutFile_"+System.currentTimeMillis()+".pdf");
      String outFileName = "certOutFile_recognition_" + recClaim.getPromotion().getId() + "_" + recClaim.getSubmitter().getId() + ".pdf";
      // FileOutputStream pdfStream = new
      // FileOutputStream(Environment.getTmpDir()+"/certOutFile_"+System.currentTimeMillis()+".pdf");
      FileOutputStream pdfStream = new FileOutputStream( Environment.getTmpDir() + "/" + outFileName );
      documentPrinter.printDocument( pdfStream );
      response.setContentType( "application/pdf" );

      // bug fix 50565, properly display pdf in all browsers(we had an issue IE8)
      response.setHeader( "Pragma", "public" );
      response.setHeader( "Expires", "0" );
      response.setHeader( "Cache-Control", "no-store, no-cache" );
      response.setHeader( "Cache-Control", "must-revalidate, post-check=0, pre-check=0" );
      response.setHeader( "Cache-Control", "public" );
      response.setHeader( "Content-Description", "File Transfer" );
      response.setHeader( "Content-Transfer-Encoding", "binary" );

      FileInputStream fis = new FileInputStream( Environment.getTmpDir() + "/" + outFileName );
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
      logger.error( e );
      e.printStackTrace();
    }

    return null;
  }

  private PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }

  private ParticipantService getParticipantService()
  {
    return (ParticipantService)getService( ParticipantService.BEAN_NAME );
  }

  private ClaimService getClaimService()
  {
    return (ClaimService)getService( ClaimService.BEAN_NAME );
  }

  private SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)BeanLocator.getBean( SystemVariableService.BEAN_NAME );
  }

  private ProcessInvocationService getProcessInvocationService()
  {
    return (ProcessInvocationService)BeanLocator.getBean( ProcessInvocationService.BEAN_NAME );
  }

  private String buildCommentFontSizeBasedOnCommentLength( int commentLength )
  {
    // Changing the comment font size based on comment length
    String fontSize = "8";
    if ( commentLength > 1450 && commentLength <= 1650 )
    {
      fontSize = "10";
    }
    else if ( commentLength > 1650 && commentLength <= 1850 )
    {
      fontSize = "9";
    }
    return fontSize;
  }
}
