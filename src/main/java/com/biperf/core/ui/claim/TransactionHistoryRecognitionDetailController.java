
package com.biperf.core.ui.claim;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.activity.MerchOrderActivity;
import com.biperf.core.domain.claim.AbstractRecognitionClaim;
import com.biperf.core.domain.claim.ClaimRecipient;
import com.biperf.core.domain.claim.RecognitionClaim;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.merchandise.MerchOrder;
import com.biperf.core.domain.multimedia.Card;
import com.biperf.core.domain.multimedia.ECard;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.activity.ActivityService;
import com.biperf.core.service.claim.ClaimAssociationRequest;
import com.biperf.core.service.claim.ClaimService;
import com.biperf.core.service.merchorder.MerchOrderService;
import com.biperf.core.service.multimedia.MultimediaService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.ui.reports.ReportsUtils;
import com.biperf.core.ui.utils.CardUtilties;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.core.value.claim.ClaimRecipientValueObject;
import com.biperf.core.value.claim.RecognitionClaimValueObject;
import com.objectpartners.cms.domain.Content;

/**
 * TransactionHistoryRecognitionDetailController.
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
 * <td>Tammy Cheng</td>
 * <td>Oct 6, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 *          Exp $
 */
public class TransactionHistoryRecognitionDetailController extends BaseController
{
  private static final Log LOG = LogFactory.getLog( TransactionHistoryRecognitionDetailController.class );
  private static final String MODE_SENT = "sent";
  private static final String MODE_RECEIVED = "received";

  /**
   * Overridden from
   * 
   * @see com.biperf.core.ui.BaseController#onExecute(org.apache.struts.tiles.ComponentContext,
   *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
   *      javax.servlet.ServletContext)
   * @param tileContext
   * @param request
   * @param response
   * @param servletContext
   * @throws Exception
   */
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    Long participantId = null;
    Long claimId = null;
    Long claimRecipientId = null;
    String mode = null;
    try
    {
      String clientState = RequestUtils.getRequiredParamString( request, "clientState" );
      String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
      String password = ClientStatePasswordManager.getPassword();

      if ( cryptoPass != null && cryptoPass.equals( "1" ) )
      {
        password = ClientStatePasswordManager.getGlobalPassword();
      }
      // Deserialize the client state.
      Map clientStateMap = ClientStateSerializer.deserialize( clientState, password );
      try
      {
        participantId = (Long)clientStateMap.get( "userId" );
      }
      catch( ClassCastException cce )
      {
        participantId = new Long( (String)clientStateMap.get( "userId" ) );
      }
      try
      {
        claimId = (Long)clientStateMap.get( "id" );
      }
      catch( ClassCastException cce2 )
      {
        claimId = new Long( (String)clientStateMap.get( "id" ) );
      }
      try
      {
        claimRecipientId = (Long)clientStateMap.get( "claimRecipientId" );
      }
      catch( ClassCastException cce3 )
      {
        claimRecipientId = new Long( (String)clientStateMap.get( "claimRecipientId" ) );
      }
      mode = (String)clientStateMap.get( "mode" );
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }

    if ( claimId != null && participantId != null && claimRecipientId != null )
    {
      Participant participant = getParticipantService().getParticipantById( participantId );
      ClaimRecipient claimRecipient = null;

      AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
      associationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.PROMOTION ) );
      associationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.CLAIM_RECIPIENTS ) );
      associationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.CARD ) );
      RecognitionClaim recognitionClaim = (RecognitionClaim)getClaimService().getClaimByIdWithAssociations( claimId, associationRequestCollection );

      for ( Iterator claimRecipientsIter = recognitionClaim.getClaimRecipients().iterator(); claimRecipientsIter.hasNext(); )
      {

        ClaimRecipient tempClaimRecipient = (ClaimRecipient)claimRecipientsIter.next();

        if ( tempClaimRecipient.getId().longValue() == claimRecipientId.longValue() )
        {
          claimRecipient = tempClaimRecipient;
        }
      }

      Long recipientId = null;
      if ( claimRecipient.getRecipient() != null )
      {
        recipientId = claimRecipient.getRecipient().getId();
      }

      if ( recognitionClaim.isRecognitionClaim() && ( (RecognitionPromotion)recognitionClaim.getPromotion() ).isAwardActive()
          && ( (RecognitionPromotion)recognitionClaim.getPromotion() ).getAwardType().isMerchandiseAwardType() )
      {
        // List activities = getActivityService()
        // .getMerchOrderActivitiesByClaimIdAndUserId( recognitionClaim.getId(), recipientId );
        List activities = null;
        boolean flag = false;
        if ( claimRecipient.getClaim().getPromotion().isRecognitionPromotion() )
        {
          RecognitionPromotion recPromotion = (RecognitionPromotion)claimRecipient.getClaim().getPromotion();
          if ( recPromotion.isSelfRecognitionEnabled() )
          {
            if ( claimRecipient.getRecipient().getId().longValue() == claimRecipient.getClaim().getSubmitter().getId().longValue() )
            {
              flag = true;

            }
          }
        }
        if ( flag )
        {
          activities = getActivityService().getMerchOrderActivitiesByClaimIdAndUserId( claimRecipient.getClaim().getId(), claimRecipient.getRecipient().getId(), null );
        }
        else
        {
          activities = getActivityService().getMerchOrderActivitiesByClaimIdAndUserId( claimRecipient.getClaim().getId(), claimRecipient.getRecipient().getId() );
        }
        if ( activities != null && !activities.isEmpty() )
        {
          MerchOrderActivity activity = (MerchOrderActivity)activities.get( 0 );
          if ( activity.getMerchOrder() != null )
          {
            MerchOrder merchOrder = activity.getMerchOrder();
            if ( !merchOrder.isRedeemed() )
            {
              merchOrder = getMerchOrderService().updateOrderStatus( merchOrder );
            }
            request.setAttribute( "merchOrder", merchOrder );
          }
        }
      }

      tileContext.putAttribute( "cardInsert", "" );
      request.setAttribute( "flashNeeded", new Boolean( false ) );
      request.setAttribute( "cardPresent", new Boolean( false ) );

      Card card = recognitionClaim.getCard();

      if ( card != null && card.getClass().equals( ECard.class ) )
      {
        ECard eCard = (ECard)card;

        boolean flashNeeded = eCard.isFlashNeeded();
        request.setAttribute( "flashNeeded", new Boolean( flashNeeded ) );

        if ( eCard.isSwf() )
        {
          String flashRequestString = CardUtilties.getFlashRequestString( eCard, request );
          request.setAttribute( "flashRequestString", flashRequestString );
          tileContext.putAttribute( "cardInsert", "/claim/nomrec/viewSWFCard.jsp" );
          request.setAttribute( "cardPresent", new Boolean( true ) );
        }
        else
        {
          String staticRequestString = CardUtilties.getStaticRequestString( eCard, request );
          request.setAttribute( "staticRequestString", staticRequestString );
          tileContext.putAttribute( "cardInsert", "/claim/nomrec/viewNonSWFCard.jsp" );
          request.setAttribute( "cardPresent", new Boolean( true ) );
        }

        request.setAttribute( "eCard", eCard );

      }
      else if ( recognitionClaim.getCertificateId() != null )
      {
        Content certificateContent = ReportsUtils.getCertificateContent( PromotionType.RECOGNITION, recognitionClaim.getCertificateId().toString() );
        if ( certificateContent != null )
        {
          String imageName = (String)certificateContent.getContentDataMap().get( "LARGE_IMAGE" );
          if ( StringUtils.isNotBlank( imageName ) )
          {
            String staticRequestString = CardUtilties.getStaticRequestString( imageName, request, true, false );
            request.setAttribute( "staticRequestString", staticRequestString );
            tileContext.putAttribute( "cardInsert", "/claim/nomrec/viewNonSWFCard.jsp" );
            request.setAttribute( "cardPresent", new Boolean( true ) );
          }
        }
      }

      request.setAttribute( "participant", participant );
      request.setAttribute( "printerClaimId", claimId );
      request.setAttribute( "promotionId", recognitionClaim.getPromotion().getId() );
      request.setAttribute( "claimRecipient", claimRecipient );
      request.setAttribute( "recognitionClaim", recognitionClaim );
      request.setAttribute( "mode", mode );
      request.setAttribute( "recognitionClaimValueObject", getRecognitionClam( recognitionClaim ) );
      request.setAttribute( "claimRecipientValueObject", getClaimRecipientValueObject( claimRecipient ) );

    }
    else
    {
      LOG.error( "claimId or userId not found in client state" );
    }
  }

  private ClaimRecipientValueObject getClaimRecipientValueObject( ClaimRecipient claimRecipient )
  {
    ClaimRecipientValueObject claimRecipientValueObject = new ClaimRecipientValueObject();
    claimRecipientValueObject.setNodeName( claimRecipient.getNode().getName() );
    claimRecipientValueObject.setDisplayName( claimRecipient.getRecipientDisplayName() );
    claimRecipientValueObject.setPositionTypepicklistName( claimRecipient.getRecipient().getPositionTypePickList().getName() );
    claimRecipientValueObject.setDepartmentTypePickListName( claimRecipient.getRecipient().getDepartmentTypePickList().getName() );
    claimRecipientValueObject.setRecipientItemId( claimRecipient.getRecipient().getId() );
    claimRecipientValueObject.setClaimItemId( claimRecipient.getId() );
    claimRecipientValueObject.setCalculatorScore( claimRecipient.getCalculatorScore() );
    claimRecipientValueObject.setAwardTypeName( claimRecipient.getClaim().getPromotion().getAwardType().getName() );
    claimRecipientValueObject.setAwardQuantity( claimRecipient.getAwardQuantity() );
    return claimRecipientValueObject;
  }

  private RecognitionClaimValueObject getRecognitionClam( AbstractRecognitionClaim claim )
  {
    RecognitionClaimValueObject recognitionClaimValueObject = new RecognitionClaimValueObject();
    recognitionClaimValueObject.setRecognitionClaim( claim.isRecognitionClaim() );
    recognitionClaimValueObject.setPromotionName( claim.getPromotion().getName() );
    recognitionClaimValueObject.setEarnings( claim.getEarnings() );
    recognitionClaimValueObject.setSubmitterLastName( claim.getSubmitter().getLastName() );
    recognitionClaimValueObject.setSubmitterFirstName( claim.getSubmitter().getFirstName() );
    recognitionClaimValueObject.setPositionTypePickListName( claim.getSubmitter().getPositionTypePickList().getName() );
    recognitionClaimValueObject.setDepartmentTypePickListName( claim.getSubmitter().getDepartmentTypePickList().getName() );
    recognitionClaimValueObject.setSubmitterId( claim.getSubmitter().getId() );
    recognitionClaimValueObject.setNominationPromotion( claim.getPromotion().isNominationPromotion() );
    recognitionClaimValueObject.setRecognitionClaim( claim.getPromotion().isRecognitionPromotion() );
    recognitionClaimValueObject.setCertificate( claim.getPromotion().getCertificate() );
    recognitionClaimValueObject.setCertificateId( ( (RecognitionClaim)claim ).getCertificateId() );

    if ( !Objects.isNull( claim.getNode() ) )
    {
      recognitionClaimValueObject.setRecognitionClaimNodeName( claim.getNode().getName() );
    }

    if ( !Objects.isNull( claim.getPromotion().getScoreBy() ) )
    {
      recognitionClaimValueObject.setScoreByGiver( claim.getPromotion().getScoreBy().isScoreByGiver() );
    }
    if ( !Objects.isNull( claim.getPromotion().getCalculator() ) )
    {
      recognitionClaimValueObject.setDisplayScores( claim.getPromotion().getCalculator().isDisplayScores() );
    }
    if ( claim.getClaimGroup() != null )
    {
      recognitionClaimValueObject.setAwardQuantity( claim.getClaimGroup().getAwardQuantity() );
    }
    recognitionClaimValueObject.setOpen( claim.isOpen() );
    recognitionClaimValueObject.setPromotionTypeCode( claim.getPromotion().getPromotionType().getCode() );
    recognitionClaimValueObject.setSubmissionDate( claim.getSubmissionDate() );
    recognitionClaimValueObject.setCopyManager( ( (RecognitionClaim)claim ).isCopyManager() );
    recognitionClaimValueObject.setCopySender( claim.isCopySender() );
    recognitionClaimValueObject.setPromotionId( claim.getPromotion().getId() );
    recognitionClaimValueObject.setClaimId( claim.getId() );
    recognitionClaimValueObject.setPromotionType( claim.getPromotion().getPromotionType().toString() );
    recognitionClaimValueObject.setBehavior( claim.getBehavior() );
    recognitionClaimValueObject.setBehaviorName( claim.getBehavior() == null ? "" : claim.getBehavior().getName() );
    recognitionClaimValueObject.setSubmitterComments( claim.getSubmitterComments() );
    return recognitionClaimValueObject;
  }

  /**
   * Does a Bean lookup for the ClaimService
   * 
   * @return ClaimService
   */
  protected static ClaimService getClaimService()
  {
    return (ClaimService)getService( ClaimService.BEAN_NAME );
  }

  /**
   * Does a Bean lookup for the ClaimService
   * 
   * @return MultiMediaService
   */
  protected static MultimediaService getMultimediaService()
  {
    return (MultimediaService)getService( MultimediaService.BEAN_NAME );
  }

  /**
   * Does a Bean lookup for the ClaimService
   * 
   * @return ClaimService
   */
  protected static ParticipantService getParticipantService()
  {
    return (ParticipantService)getService( ParticipantService.BEAN_NAME );
  }

  /**
   * Get the ActivityService from the beanLocator.
   * 
   * @return ActivityService
   */
  private ActivityService getActivityService()
  {
    return (ActivityService)getService( ActivityService.BEAN_NAME );
  }

  /**
   * Get the MerchOrderService from the beanLocator.
   * 
   * @return MerchOrderService
   */
  private MerchOrderService getMerchOrderService()
  {
    return (MerchOrderService)getService( MerchOrderService.BEAN_NAME );
  }

}
