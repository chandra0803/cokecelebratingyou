
package com.biperf.core.ui.fileload;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;
import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;

import com.biperf.core.domain.enums.NominationAwardGroupSizeType;
import com.biperf.core.domain.enums.NominationAwardGroupType;
import com.biperf.core.domain.enums.PromotionBehaviorType;
import com.biperf.core.domain.enums.PromotionCertificate;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.fileload.ImportFile;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.message.Message;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.NominationPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionBehavior;
import com.biperf.core.domain.promotion.PromotionCert;
import com.biperf.core.domain.promotion.PromotionECard;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.fileload.ImportService;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.ui.promotion.PromotionCertificateFormBean;
import com.biperf.core.ui.utils.CardUtilties;
import com.biperf.util.StringUtils;
import com.objectpartners.cms.domain.Content;

/**
 * RecognitionOptionsController.
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
 * <td>reddy</td>
 * <td>Dec 10, 2007</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class RecognitionOptionsController extends BaseController
{
  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  private PromotionService promotionService = (PromotionService)getService( PromotionService.BEAN_NAME );
  private MessageService messageService = (MessageService)getService( MessageService.BEAN_NAME );

  // ---------------------------------------------------------------------------
  // Public Methods
  // ---------------------------------------------------------------------------

  /**
   * Fetches data for the Import Record List page.
   * 
   * @param tileContext the context for the tile associated with this controller.
   * @param request the HTTP request we are processing.
   * @param response the HTTP response we are creating.
   * @param servletContext the context for servlets of this web application.
   */
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext )
  {

    // StringBuffer cardString = new StringBuffer("card:");
    String promoId = request.getParameter( "promotionId" );
    if ( !StringUtils.isEmpty( promoId ) )
    {
      AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
      associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.ECARDS ) );
      associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.CERTIFICATES ) );
      associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.PROMOTION_BEHAVIORS ) );
      RecognitionPromotion recognitionPromo = (RecognitionPromotion)promotionService.getPromotionByIdWithAssociations( new Long( promoId ), associationRequestCollection );
      request.setAttribute( "adminCopyManager", new Boolean( recognitionPromo.isCopyRecipientManager() ) );
      Set eCardSet = recognitionPromo.getPromotionECard();
      if ( eCardSet != null && !eCardSet.isEmpty() )
      {
        Iterator cardIter = eCardSet.iterator();
        while ( cardIter.hasNext() )
        {
          PromotionECard card = (PromotionECard)cardIter.next();
          if ( card.geteCard() != null )
          {
            // Determine if selected card is a flash or not
            if ( card.geteCard().isFlashNeeded() )
            {
              // tileContext.putAttribute( "cardInsert", "/claim/nomrec/viewSWFCard.jsp" );
              // For eCards: flashNeeded (*.swf) or flashNotNeeded (*.gif) eCards
              request.setAttribute( "flashNeeded", new Boolean( true ) );
              String flashRequestString = CardUtilties.getSizedFlashRequestString( card.geteCard(), request, 250, 250 );
              request.setAttribute( "flashRequestString", flashRequestString );
              tileContext.putAttribute( "cardInsert", "/claim/nomrec/viewSWFCard.jsp" );
              break;
            }
          }
        }

        if ( request.getAttribute( "flashNeeded" ) == null )
        {
          tileContext.putAttribute( "cardInsert", "/claim/nomrec/viewNoCard.jsp" );
        }

        request.setAttribute( "isCardActive", "true" );
        request.setAttribute( "electronicCardsCount", new Integer( eCardSet.size() ) );
        request.setAttribute( "electronicCards", eCardSet );
        request.setAttribute( "electronicCardsEndIndex", new Integer( eCardSet.size() - 1 ) );
      }
      else
      {
        request.setAttribute( "isCardActive", "false" );
        request.setAttribute( "electronicCardsCount", new Integer( 0 ) );
      }

      List allCertificates = PromotionCertificate.getList( PromotionType.RECOGNITION );
      Set certificates = recognitionPromo.getPromotionCertificates();

      if ( certificates != null && !certificates.isEmpty() )
      {
        Set sortedSet = new TreeSet();
        for ( Iterator promoCertIter = certificates.iterator(); promoCertIter.hasNext(); )
        {
          PromotionCert promoCert = (PromotionCert)promoCertIter.next();
          for ( Iterator certificateIter = allCertificates.iterator(); certificateIter.hasNext(); )
          {
            Content certificate = (Content)certificateIter.next();
            String certificateId = (String)certificate.getContentDataMap().get( "ID" );
            if ( certificateId.equals( promoCert.getCertificateId().toString() ) )
            {
              PromotionCertificateFormBean certificateFormBean = new PromotionCertificateFormBean();
              certificateFormBean.setCertificateId( certificateId );
              certificateFormBean.setName( (String)certificate.getContentDataMap().get( "NAME" ) );
              certificateFormBean.setPreviewImageName( (String)certificate.getContentDataMap().get( "THUMBNAIL_IMAGE" ) );
              certificateFormBean.setImageName( (String)certificate.getContentDataMap().get( "LARGE_IMAGE" ) );
              sortedSet.add( certificateFormBean );
              break;
            }
          }
        }

        if ( sortedSet.size() > 0 )
        {
          request.setAttribute( "isCardActive", "true" );
        }
        request.setAttribute( "certificatesCount", new Integer( sortedSet.size() ) );
        request.setAttribute( "certificatesSet", sortedSet );
      }
      else
      {
        request.setAttribute( "certificatesCount", new Integer( 0 ) );
      }

      RecognitionOptionsForm recognitionOptionsForm = (RecognitionOptionsForm)request.getAttribute( "recognitionOptionsForm" );
      recognitionOptionsForm.setPromotionId( promoId );
      recognitionOptionsForm.setBehaviorActive( recognitionPromo.isBehaviorActive() );
      recognitionOptionsForm.setCmPromotionTypeCode( "recognition" );
      request.setAttribute( "isTeam", new Boolean( isTeam( recognitionPromo ) ) );
      Message msg = messageService.getMessageByCMAssetCode( MessageService.RECOGNITION_RECEIVED_MESSAGE_CM_ASSET_CODE );
      if ( msg != null )
      {
        recognitionOptionsForm.setMessage( msg.getName() );
        recognitionOptionsForm.setMessageId( String.valueOf( msg.getId() ) );
      }

      // display the comments section if STAGE_DEPOSIT_IMPORT_RECORD does not have comments.
      boolean showComments = getImportService().verifyRecordsComments( new Long( recognitionOptionsForm.getImportFileId() ), recognitionOptionsForm.getImportFileType() );
      request.setAttribute( "showComments", new Boolean( showComments ) );

      // Set the values on to the form that already saved for the RecognitionOptions page.
      if ( request.getSession().getAttribute( "recOptionsImportFile" ) != null )
      {
        ImportFile optionsImportFile = (ImportFile)request.getSession().getAttribute( "recOptionsImportFile" );
        if ( optionsImportFile.getPromotion() != null && String.valueOf( optionsImportFile.getPromotion().getId() ).equals( promoId ) )
        {
          if ( !StringUtils.isEmpty( optionsImportFile.getSubmitterComments() ) )
          {
            recognitionOptionsForm.setComments( optionsImportFile.getSubmitterComments() );
          }
          if ( optionsImportFile.getSubmitter() != null )
          {
            recognitionOptionsForm.setSubmitterId( String.valueOf( optionsImportFile.getSubmitter().getId() ) );
            recognitionOptionsForm.setSubmitterName( getSubmitterDetailsString( optionsImportFile.getSubmitter(), optionsImportFile.getSubmitterNode() ) );
          }
          if ( optionsImportFile.getSubmitterNode() != null )
          {
            recognitionOptionsForm.setSubmitterNodeId( String.valueOf( optionsImportFile.getSubmitterNode().getId() ) );
          }
          if ( optionsImportFile.getMessage() != null )
          {
            recognitionOptionsForm.setMessageId( String.valueOf( optionsImportFile.getMessage().getId() ) );
            recognitionOptionsForm.setMessage( optionsImportFile.getMessage().getName() );
          }
          if ( optionsImportFile.getCard() != null )
          {
            StringBuffer cardString = new StringBuffer( "card:" );
            cardString.append( String.valueOf( optionsImportFile.getCard() ) );
            recognitionOptionsForm.setCardId( cardString.toString() );
          }
          if ( optionsImportFile.getCertificateId() != null )
          {
            StringBuffer cardString = new StringBuffer( "certificate:" );
            cardString.append( String.valueOf( optionsImportFile.getCertificateId() ) );
            recognitionOptionsForm.setCertificateId( cardString.toString() );
            recognitionOptionsForm.setCardId( cardString.toString() );
          }
          if ( optionsImportFile.getBehavior() != null )
          {
            recognitionOptionsForm.setSelectedBehavior( optionsImportFile.getBehavior() );
          }
          recognitionOptionsForm.setCopyManager( optionsImportFile.isCopyManager() );
        }
      }
      // set behavior list
      recognitionOptionsForm.setBehaviorActive( false );

      Set promotionBehaviorsSet = recognitionPromo.getPromotionBehaviors();
      List behaviorList = new ArrayList();
      if ( null != promotionBehaviorsSet )
      {
        Iterator promoIterator = promotionBehaviorsSet.iterator();
        while ( promoIterator.hasNext() )
        {
          PromotionBehavior promoBehavior = (PromotionBehavior)promoIterator.next();
          PromotionBehaviorType promotionBehaviorType = promoBehavior.getPromotionBehaviorType();
          behaviorList.add( promotionBehaviorType );
        }
        recognitionOptionsForm.setBehaviorActive( true );
      }
      PropertyComparator.sort( behaviorList, new MutableSortDefinition( "sortOrder", false, true ) );
      request.setAttribute( "behaviorList", behaviorList );
    }
  }

  // ---------------------------------------------------------------------------
  // Private Methods
  // ---------------------------------------------------------------------------

  private boolean isTeam( Promotion promotion )
  {
    boolean isTeam = false;

    if ( promotion.isNominationPromotion() )
    {
      NominationAwardGroupType awardGroupType = ( (NominationPromotion)promotion ).getAwardGroupType();
      NominationAwardGroupSizeType awardGroupSizeType = ( (NominationPromotion)promotion ).getAwardGroupSizeType();

      if ( awardGroupType != null )
      {
        if ( ( awardGroupType.isTeam() || awardGroupType.isIndividualOrTeam() ) && ( awardGroupSizeType.isLimited() || awardGroupSizeType.isUnlimited() ) )
        {
          isTeam = true;
        }
      }
    }

    return isTeam;
  }

  private String getSubmitterDetailsString( Participant pax, Node node )
  {
    StringBuffer returnStr = new StringBuffer( "" );
    String delim = "-";
    if ( !StringUtils.isEmpty( pax.getNameLFMWithComma() ) )
    {
      returnStr.append( pax.getNameLFMWithComma() );
    }
    returnStr.append( delim );
    if ( node != null )
    {
      returnStr.append( node.getName() );
    }
    if ( pax.getDepartmentType() != null )
    {
      returnStr.append( delim );
      returnStr.append( pax.getDepartmentType() );
    }
    return returnStr.toString();
  }

  private ImportService getImportService()
  {
    return (ImportService)getService( ImportService.BEAN_NAME );
  }
}
