
package com.biperf.core.ui.recognition;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.participant.NameIdBean;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.domain.user.UserNode;
import com.biperf.core.exception.DataException;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.promotion.NominationPromotionService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.service.util.ServiceError;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.PromotionMenuBean;
import com.biperf.core.value.RecognitionBean;
import com.biperf.core.value.nomination.EligibleNominationPromotionValueObject;
import com.objectpartners.cms.util.ContentReaderManager;

public class ParticipantDataServiceAction extends BaseRecognitionAction
{

  public ActionForward allRecogPromo( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {

    List<EligiblePromotion> eligiblePromoList = new ArrayList<EligiblePromotion>();

    // Please Don't Remove The Below Codes, As Required For RA Phase 2

    /*
     * if ( getSystemVariableService().getPropertyByName( SystemVariableService.RA_ENABLE
     * ).getBooleanVal() && !Objects.isNull( request.getParameter( "isRARecognitionFlow" ) ) &&
     * request.getParameter( "isRARecognitionFlow" ).toString().equals( "yes" ) ) { String
     * reporteeId = request.getParameter( "reporteeId" ); if ( Objects.nonNull( reporteeId ) ) {
     * String decodeUserId = URLDecoder.decode( reporteeId, "UTF-8" ); Long receiverId = new Long(
     * getEncryptionService().getDecryptedValue( decodeUserId ) );
     * List<RecognitionAdvisorPromotionValueBean> recAdvisorPromoBeanLst =
     * getPromotionService().getPromotionListForRA( UserManager.getUser().getUserId(), receiverId );
     * recAdvisorPromoBeanLst.forEach( p -> eligiblePromoList.add( new EligiblePromotion(
     * p.getPromotionId(), p.getPromotionName(), "rec", null, null, false ) ) ); } }
     */

    List<PromotionMenuBean> eligiblePromotions = getEligiblePromotions( request ).stream().filter( p -> p.isCanSubmit() ).collect( Collectors.toList() );

    List<EligibleNominationPromotionValueObject> nomPromosList = getNomPromoService().getEligibleNomPromotions( UserManager.getUser() );
    List<RecognitionBean> recogPromoList = getPromotionService().getRecognitionSubmissionList( UserManager.getUserId(), eligiblePromotions, UserManager.getUser().isParticipant() );

    for ( EligibleNominationPromotionValueObject nomPromo : nomPromosList )
    {
      eligiblePromoList.add( new EligiblePromotion( nomPromo.getPromoId(), nomPromo.getName(), "nom", nomPromo.getMaxSubmissionAllowed(), nomPromo.getUsedSubmission(), false ) );
    }

    for ( RecognitionBean recogPromo : recogPromoList )
    {
      eligiblePromoList.add( new EligiblePromotion( recogPromo.getId(), recogPromo.getName(), "rec", null, null, recogPromo.isPurlPromotion(), recogPromo.getAwardMin(), recogPromo.getAwardMax() ) );//Client customization
    }

    List<NameIdBean> userNodesList = new ArrayList<NameIdBean>();
 
		Set<UserNode> userNodes = getUserService().getUserNodes(UserManager.getUserId());
		// Client customization for WIP #41645 starts
		boolean hasManagerPrimary = false;
		if (userNodes != null && !userNodes.isEmpty()) {
			if (userNodes.size() == 1) {
				for (UserNode userNode : userNodes) {
					if(!userNode.getIsMgrPrimary() )
					{ 
						new ServiceError("No Manager Primary flag(ADIH_MGR_PRIMARY_ORG) is available for this Org Unit");					
					}
					if (!userNode.getNode().isDeleted()  ) {
						userNodesList
								.add(new NameIdBean(userNode.getNode().getId(), userNode.getNode().getName(), null));
					}
				}
			}else if (userNodes.size() > 1) {
				for (UserNode userNode : userNodes) {
					if (!userNode.getNode().isDeleted() && userNode.getIsMgrPrimary()) {
						userNodesList
								.add(new NameIdBean(userNode.getNode().getId(), userNode.getNode().getName(), null));
						hasManagerPrimary = true;
					}
				}
				if (!hasManagerPrimary) { 
					new ServiceError("No Manager Primary flag(ADIH_MGR_PRIMARY_ORG) is available for this Org Unit");					
				}
			} 
/*			else if (!hasManagerPrimary) {
				for (UserNode userNode : userNodes) {
					if (!userNode.getNode().isDeleted()) {
						userNodesList
								.add(new NameIdBean(userNode.getNode().getId(), userNode.getNode().getName(), null));
					}
				}
			}*/
		}
    // Client customization for WIP #41645 ends
    List<PromotionMenuBean> raEligiblePromoList = eligiblePromotions.stream()
        .filter( ( p ) -> p.isCanSubmit() && p.getPromotion().isRecognitionPromotion() && p.getPromotion().isLive() && ! ( (RecognitionPromotion)p.getPromotion() ).isIncludePurl()
            && ! ( (RecognitionPromotion)p.getPromotion() ).isIncludeCelebrations() && !p.getPromotion().getAwardType().isMerchandiseAwardType() && !p.getPromotion().isFileLoadEntry() )
        .collect( Collectors.toList() );

    EligiblePromotionsListView jsonResponseView = new EligiblePromotionsListView( eligiblePromoList.size(),
                                                                                  eligiblePromoList,
                                                                                  userNodesList,
                                                                                  getSystemVariableService().getPropertyByName( SystemVariableService.RA_ENABLE ).getBooleanVal()
                                                                                      && !UserManager.isUserDelegateOrLaunchedAs()
                                                                                      && ( UserManager.getUser().isManager() || UserManager.getUser().isOwner() ) && raEligiblePromoList.size() > 0 );

    writeAsJsonToResponse( jsonResponseView, response, ContentType.JSON );

    return null;
  }

  private NominationPromotionService getNomPromoService()
  {
    return (NominationPromotionService)getService( NominationPromotionService.BEAN_NAME );
  }

}
