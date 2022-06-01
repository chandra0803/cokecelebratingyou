/**
 * 
 */

package com.biperf.core.ui.celebration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.claim.Claim;
import com.biperf.core.domain.claim.RecognitionClaim;
import com.biperf.core.domain.enums.PurlRecipientState;
import com.biperf.core.domain.multimedia.Card;
import com.biperf.core.domain.multimedia.ECard;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.domain.purl.PurlContributor;
import com.biperf.core.domain.purl.PurlRecipient;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.celebration.CelebrationService;
import com.biperf.core.service.claim.ClaimAssociationRequest;
import com.biperf.core.service.claim.ClaimService;
import com.biperf.core.service.purl.PurlService;
import com.biperf.core.service.purl.impl.PurlRecipientAssociationRequest;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.celebration.CelebrationModuleValueBean;

/**
 * @author poddutur
 *
 */
public class CelebrationModuleController extends BaseController
{
  private static final String CELEBRATION = "celebration";
  private static final String IMAGETYPE = "_lg";

  private static final String IMAGE_SERVICE_FLASH_IMG = "/images/size/864/864/";

  @Override
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    Long participantId = UserManager.getUserId();

    List<Long> claimIds = getClaimService().getCelebrationClaims( participantId );
    List<CelebrationModuleValueBean> claimList = new ArrayList<CelebrationModuleValueBean>();
    for ( int i = 0; i < claimIds.size(); i++ )
    {
      RecognitionClaim recognitionClaim = null;
      Claim claim = null;
      AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
      associationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.PROMOTION ) );
      associationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.CARD ) );
      associationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.ELEMENTS ) );
      claim = getClaimService().getClaimByIdWithAssociations( claimIds.get( i ), associationRequestCollection );

      if ( claim instanceof RecognitionClaim )
      {
        recognitionClaim = (RecognitionClaim)claim;
        RecognitionPromotion recognitionPromotion = (RecognitionPromotion)recognitionClaim.getPromotion();
        if ( recognitionPromotion.isIncludeCelebrations() )
        {
          if ( recognitionPromotion.isIncludePurl() )
          {
            AssociationRequestCollection asc = new AssociationRequestCollection();
            asc.add( new PurlRecipientAssociationRequest( PurlRecipientAssociationRequest.PURL_CONTRIBUTOR ) );
            asc.add( new PurlRecipientAssociationRequest( PurlRecipientAssociationRequest.PURL_CONTRIBUTOR_COMMENT ) );
            PurlRecipient purlRecipient = getPurlService().getPurlRecipientByClaimIdWithAssociations( recognitionClaim.getId(), asc );

            if ( purlRecipient != null && purlRecipient.getState() != null
                && ( purlRecipient.getState().getCode().equals( PurlRecipientState.RECOGNITION ) || purlRecipient.getState().getCode().equals( PurlRecipientState.COMPLETE )
                    || purlRecipient.getState().getCode().equals( PurlRecipientState.EXPIRED ) && recognitionPromotion.isIncludeCelebrations() && !isContributionsAvailable( purlRecipient ) ) )
            {
              String customFormElementsInfo = "";
              CelebrationModuleValueBean celebrationModuleValueBean = new CelebrationModuleValueBean();
              celebrationModuleValueBean.setClaimId( recognitionClaim.getId() );
              celebrationModuleValueBean.setPromotionName( recognitionPromotion.getPromotionName() );
              celebrationModuleValueBean.seteCardUrl( getECardUrl( recognitionClaim, request ) );
              celebrationModuleValueBean.setAnniversaryNumberOfDays( recognitionClaim.getAnniversaryNumberOfDays() );
              celebrationModuleValueBean.setAnniversaryNumberOfYears( recognitionClaim.getAnniversaryNumberOfYears() );
              celebrationModuleValueBean.setCelebrationPageUrl( getCelebrationPageUrl( recognitionClaim.getId(), request ) );

              customFormElementsInfo = purlRecipient.getContributorDisplayInfo();
              celebrationModuleValueBean.setCustomFormElementsInfo( customFormElementsInfo );
              claimList.add( celebrationModuleValueBean );
            }
          }
          else
          {
            CelebrationModuleValueBean celebrationModuleValueBean = new CelebrationModuleValueBean();
            celebrationModuleValueBean.setClaimId( recognitionClaim.getId() );
            celebrationModuleValueBean.setPromotionName( recognitionPromotion.getPromotionName() );
            celebrationModuleValueBean.seteCardUrl( getECardUrl( recognitionClaim, request ) );
            celebrationModuleValueBean.setAnniversaryNumberOfDays( recognitionClaim.getAnniversaryNumberOfDays() );
            celebrationModuleValueBean.setAnniversaryNumberOfYears( recognitionClaim.getAnniversaryNumberOfYears() );
            celebrationModuleValueBean.setCelebrationPageUrl( getCelebrationPageUrl( recognitionClaim.getId(), request ) );

            claimList.add( celebrationModuleValueBean );
          }
        }
      }
    }
    request.setAttribute( "claimList", claimList );
  }

  private boolean isContributionsAvailable( PurlRecipient purlRecipient )
  {
    for ( PurlContributor purlContributor : purlRecipient.getContributors() )
    {
      if ( isContributionsAvailable( purlContributor ) )
      {
        return true;
      }
    }
    return false;
  }

  private boolean isContributionsAvailable( PurlContributor purlContributor )
  {
    return null != purlContributor && !purlContributor.getComments().isEmpty();
  }

  private String getCelebrationPageUrl( Long claimId, HttpServletRequest request )
  {
    Map<String, String> paramMap = new HashMap<String, String>();
    paramMap.put( "claimId", claimId.toString() );
    String celebrationPageUrl = ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI( request ), "/celebration/celebrationPage.do", paramMap );
    return celebrationPageUrl;
  }

  private String getECardUrl( RecognitionClaim recognitionClaim, HttpServletRequest request )
  {
    RecognitionPromotion recognitionPromotion = (RecognitionPromotion)recognitionClaim.getPromotion();
    String eCardUrl = "";
    if ( recognitionClaim.getCard() != null )
    {
      String flashName = null;
      Card card = recognitionClaim.getCard();
      if ( card instanceof ECard )
      {
        ECard eCard = (ECard)card;
        flashName = eCard.getFlashNameLocale( UserManager.getLocale().toString() );
      }
      eCardUrl = flashName;
    }
    else if ( recognitionPromotion.isServiceAnniversary() )
    {
      Integer anniversaryNumberOfDaysOrYears;
      if ( recognitionPromotion.getAnniversaryInYears() )
      {
        anniversaryNumberOfDaysOrYears = recognitionClaim.getAnniversaryNumberOfYears();
      }
      else
      {
        anniversaryNumberOfDaysOrYears = recognitionClaim.getAnniversaryNumberOfDays();
      }
      String ecardFlashName = CELEBRATION + anniversaryNumberOfDaysOrYears + IMAGETYPE;
      String ecard = getCelebrationService().getServiceAnniversaryEcardOrDefaultCelebrationEcard( ecardFlashName, recognitionPromotion.getId(), UserManager.getLocale().toString() );
      eCardUrl = ecard;
    }
    else
    {
      String ecard = getCelebrationService().getServiceAnniversaryEcardOrDefaultCelebrationEcard( null, recognitionPromotion.getId(), UserManager.getLocale().toString() );
      eCardUrl = ecard;
    }
    return eCardUrl;
  }

  private static ClaimService getClaimService()
  {
    return (ClaimService)getService( ClaimService.BEAN_NAME );
  }

  private SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }

  private PurlService getPurlService()
  {
    return (PurlService)getService( PurlService.BEAN_NAME );
  }

  private CelebrationService getCelebrationService()
  {
    return (CelebrationService)getService( CelebrationService.BEAN_NAME );
  }

}
