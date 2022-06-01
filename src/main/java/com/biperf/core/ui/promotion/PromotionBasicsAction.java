/**
 *
 */

package com.biperf.core.ui.promotion;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.hibernate.exception.ConstraintViolationException;
import org.json.JSONException;

import com.biperf.core.domain.claim.ClaimForm;
import com.biperf.core.domain.enums.BadgeType;
import com.biperf.core.domain.enums.NominationAwardGroupType;
import com.biperf.core.domain.enums.NominationEvaluationType;
import com.biperf.core.domain.enums.PayoutStructure;
import com.biperf.core.domain.enums.PickListItem;
import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.enums.PromotionCertificate;
import com.biperf.core.domain.enums.PromotionIssuanceType;
import com.biperf.core.domain.enums.PromotionStatusType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.enums.SweepstakesClaimEligibilityType;
import com.biperf.core.domain.enums.SweepstakesMultipleAwardsType;
import com.biperf.core.domain.enums.SweepstakesWinnerEligibilityType;
import com.biperf.core.domain.enums.SweepstakesWinnersType;
import com.biperf.core.domain.gamification.Badge;
import com.biperf.core.domain.gamification.BadgePromotion;
import com.biperf.core.domain.gamification.BadgeRule;
import com.biperf.core.domain.promotion.AdminPurlAvatarObjectView;
import com.biperf.core.domain.promotion.AdminSSIContestGuideObjectView;
import com.biperf.core.domain.promotion.ChallengePointPromotion;
import com.biperf.core.domain.promotion.EngagementPromotion;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.domain.promotion.NominationPromotion;
import com.biperf.core.domain.promotion.ProductClaimPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.QuizPromotion;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.domain.promotion.Survey;
import com.biperf.core.domain.promotion.SurveyPromotion;
import com.biperf.core.domain.promotion.ThrowdownPromotion;
import com.biperf.core.domain.promotion.WellnessPromotion;
import com.biperf.core.domain.quiz.Quiz;
import com.biperf.core.domain.quiz.QuizLearningDetails;
import com.biperf.core.domain.quiz.QuizLearningObject;
import com.biperf.core.domain.ssi.SSIPromotion;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.exception.ServiceErrorExceptionWithRollback;
import com.biperf.core.exception.UniqueConstraintViolationException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.claim.ClaimFormAssociationRequest;
import com.biperf.core.service.claim.ClaimFormDefinitionService;
import com.biperf.core.service.cms.CMAssetService;
import com.biperf.core.service.gamification.GamificationService;
import com.biperf.core.service.profileavatar.ProfileAvatarService;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.service.promotion.PromotionBadgeUpdateAssociation;
import com.biperf.core.service.promotion.PromotionBasicsUpdateAssociation;
import com.biperf.core.service.promotion.PromotionCertificateUpdateAssociation;
import com.biperf.core.service.purl.PurlService;
import com.biperf.core.service.quiz.QuizService;
import com.biperf.core.service.ssi.SSIPromotionService;
import com.biperf.core.service.survey.SurveyService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.constants.ViewAttributeNames;
import com.biperf.core.ui.utils.JsonResponse;
import com.biperf.core.ui.utils.RosterUtil;
import com.biperf.core.ui.utils.ServiceErrorStrutsUtils;
import com.biperf.core.ui.utils.ValidatorChecks;
import com.biperf.core.utils.AwsUtils;
import com.biperf.core.utils.Environment;
import com.biperf.core.utils.MeshServicesUtil;
import com.biperf.core.utils.NewServiceAnniversaryUtil;
import com.biperf.core.utils.ProfileAvatarUploadUtil;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.value.PromotionBasicsBadgeFormBean;
import com.biperf.core.value.PurlFileUploadValue;
import com.biperf.core.value.SSIFileUpload;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * PromotionBasicsAction.
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
 * <td>sondgero</td>
 * <td>Jun 28, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 *
 *
 */
public class PromotionBasicsAction extends PromotionBaseDispatchAction
{
  /** Log */
  private static final Log logger = LogFactory.getLog( PromotionBasicsAction.class );
  public static final String IMAGE_SERVICE_LARGE_IMG = "/images/size/144/144/";

  /**
   * Method which is dispatched to when there is no value for specified request parameter included
   * in the request.
   *
   * @param mapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward unspecified( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    return display( mapping, actionForm, request, response );
  }

  /**
   * Method to refresh the page on submit
   *
   * @param mapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward refresh( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward display( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {

    String forwardTo = ActionConstants.SUCCESS_FORWARD;
    Promotion promotion = null;

    PromotionBasicsForm promoBasicsForm = (PromotionBasicsForm)form;

    // New Service Anniversary Celebration Module Enabling.
    if ( NewServiceAnniversaryUtil.isNewServiceAnniversaryEnabled() && getSystemVariableService().getPropertyByName( SystemVariableService.PURL_AVAILABLE ).getBooleanVal() )
    {
      promoBasicsForm.setIncludePurl( false );
      promoBasicsForm.setIncludeCelebrations( false );
    }

    promoBasicsForm.setMethod( "save" );

    String promotionId = promoBasicsForm.getPromotionId();

    // WIZARD MODE
    if ( request.getSession().getAttribute( ViewAttributeNames.PAGE_MODE ).equals( ViewAttributeNames.WIZARD_MODE ) )
    {
      PromotionWizardManager promotionWizardManager = (PromotionWizardManager)request.getSession().getAttribute( PromotionWizardManager.SESSION_KEY );

      promotion = promotionWizardManager.getPromotion();

      if ( promotion != null )
      {
        if ( promotionWizardManager.getPromotionType().getCode().equals( PromotionType.DIY_QUIZ ) )
        {
          AssociationRequestCollection promoAssociationRequestCollection = new AssociationRequestCollection();
          promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.CERTIFICATES ) );
          promotion = (QuizPromotion)getPromotionService().getPromotionByIdWithAssociations( new Long( promotionId ), promoAssociationRequestCollection );
        }

        promoBasicsForm.load( promotion );

        if ( promotionWizardManager.getPromotionType().getCode().equals( PromotionType.RECOGNITION ) )
        {
          RecognitionPromotion recogPromotion = null;
          if ( promotionId != null )
          {
            recogPromotion = (RecognitionPromotion)getPromotionService().getPromotionById( new Long( promotionId ) );

          }

          if ( recogPromotion != null && recogPromotion.isPurlStandardMessageEnabled() )
          {
            List<QuizLearningDetails> purlStandardMessagePictureObjectsDetails = getPromotionService().getPurlStandardMessagePictureObjects( recogPromotion.getContentResourceCMCode() );
            Iterator pictureObjectsItr = purlStandardMessagePictureObjectsDetails.iterator();
            String imagePicUrl = "";
            String videoUrl = "";
            String videoUrlMp4 = "";
            String videoUrlWebm = "";
            String videoUrl3gp = "";
            String videoUrlOgg = "";
            String mediaPath = "";
            String leftColumn = "";
            String uploadType = "image";
            while ( pictureObjectsItr.hasNext() )
            {
              QuizLearningDetails pictureObjectsDetail = (QuizLearningDetails)pictureObjectsItr.next();
              mediaPath = pictureObjectsDetail.getFilePath();
              leftColumn = pictureObjectsDetail.getLeftColumn();
              if ( leftColumn.contains( "<p>" ) )
              {
                uploadType = "image";
                String s = "<img src=\"";
                int ix = leftColumn.indexOf( s ) + s.length();
                imagePicUrl = leftColumn.substring( ix, leftColumn.indexOf( "\"", ix + 1 ) );
              }
              else
              {
                uploadType = "video";
                videoUrl = leftColumn;
                videoUrlMp4 = pictureObjectsDetail.getVideoUrlMp4();
                videoUrlWebm = pictureObjectsDetail.getVideoUrlWebm();
                videoUrl3gp = pictureObjectsDetail.getVideoUrl3gp();
                videoUrlOgg = pictureObjectsDetail.getVideoUrlOgg();
              }
              String rightColumn = pictureObjectsDetail.getRightColumn();
              promoBasicsForm.loadPurlStandardMessagePictureObjects( imagePicUrl, rightColumn, uploadType, mediaPath, videoUrlMp4, videoUrlWebm, videoUrl3gp, videoUrlOgg );
            }
            request.setAttribute( "imagePicUrl", imagePicUrl );
            request.setAttribute( "videoUrl", videoUrl );
            request.setAttribute( "videoUrlMp4", videoUrlMp4 );
            request.setAttribute( "videoUrlWebm", videoUrlWebm );
            request.setAttribute( "videoUrl3gp", videoUrl3gp );
            request.setAttribute( "videoUrlOgg", videoUrlOgg );
            request.setAttribute( "uploadType", uploadType );
          }
        }
        else if ( promotionWizardManager.getPromotionType().getCode().equals( PromotionType.SELF_SERV_INCENTIVES ) )
        {
          SSIPromotion ssiPromotion = null;
          if ( promotionId != null )
          {
            ssiPromotion = (SSIPromotion)getPromotionService().getPromotionById( new Long( promotionId ) );
            promoBasicsForm.setContestFilePath( getSSIPromotionService().buildSSIContestGuideUrl( ssiPromotion.getContestGuideUrl() ) );
          }
        }
      }
      else
      {

        if ( promotionWizardManager.getPromotionType().getCode().equals( PromotionType.PRODUCT_CLAIM ) )
        {

          promotion = new ProductClaimPromotion();
          promotion.setPromotionType( PromotionType.lookup( PromotionType.PRODUCT_CLAIM ) );

        }
        else if ( promotionWizardManager.getPromotionType().getCode().equals( PromotionType.RECOGNITION ) )
        {
          promotion = new RecognitionPromotion();
          promotion.setPromotionType( PromotionType.lookup( PromotionType.RECOGNITION ) );
          promoBasicsForm.setIssuanceMethod( PromotionIssuanceType.FILE_LOAD );
        }
        else if ( promotionWizardManager.getPromotionType().getCode().equals( PromotionType.NOMINATION ) )
        {

          promotion = new NominationPromotion();
          promotion.setPromotionType( PromotionType.lookup( PromotionType.NOMINATION ) );
          promoBasicsForm.setEvaluationType( PickListItem.getDefaultItem( NominationEvaluationType.class ).getCode() );

          promoBasicsForm.setAwardGroupMethod( PickListItem.getDefaultItem( NominationAwardGroupType.class ).getCode() );

        }
        else if ( promotionWizardManager.getPromotionType().getCode().equals( PromotionType.QUIZ ) )
        {

          promotion = new QuizPromotion();
          promotion.setPromotionType( PromotionType.lookup( PromotionType.QUIZ ) );
          promoBasicsForm.setSweepstakeWinnerType( (SweepstakesWinnersType)SweepstakesWinnersType.getList().get( 0 ) );

        }
        else if ( promotionWizardManager.getPromotionType().getCode().equals( PromotionType.DIY_QUIZ ) )
        {

          promotion = new QuizPromotion();
          promotion.setPromotionType( PromotionType.lookup( PromotionType.DIY_QUIZ ) );

        }
        else if ( promotionWizardManager.getPromotionType().getCode().equals( PromotionType.ENGAGEMENT ) )
        {

          promotion = new EngagementPromotion();
          promotion.setPromotionType( PromotionType.lookup( PromotionType.ENGAGEMENT ) );

        }
        else if ( promotionWizardManager.getPromotionType().getCode().equals( PromotionType.SURVEY ) )
        {

          promotion = new SurveyPromotion();
          promotion.setPromotionType( PromotionType.lookup( PromotionType.SURVEY ) );
          promoBasicsForm.setSweepstakeWinnerType( (SweepstakesWinnersType)SweepstakesWinnersType.getList().get( 0 ) );

        }
        else if ( promotionWizardManager.getPromotionType().getCode().equals( PromotionType.GOALQUEST ) )
        {
          promotion = new GoalQuestPromotion();
          promotion.setPromotionType( PromotionType.lookup( PromotionType.GOALQUEST ) );
        }

        else if ( promotionWizardManager.getPromotionType().getCode().equals( PromotionType.CHALLENGE_POINT ) )
        {
          promotion = new ChallengePointPromotion();
          promotion.setPromotionType( PromotionType.lookup( PromotionType.CHALLENGE_POINT ) );

        }

        else if ( promotionWizardManager.getPromotionType().getCode().equals( PromotionType.WELLNESS ) )
        {
          promotion = new WellnessPromotion();
          promotion.setPromotionType( PromotionType.lookup( PromotionType.WELLNESS ) );
          promoBasicsForm.setIssuanceMethod( "file_load" );

        }
        else if ( promotionWizardManager.getPromotionType().getCode().equals( PromotionType.THROWDOWN ) )
        {
          promotion = new ThrowdownPromotion();
          promotion.setPromotionType( PromotionType.lookup( PromotionType.THROWDOWN ) );
        }
        else if ( promotionWizardManager.getPromotionType().getCode().equals( PromotionType.SELF_SERV_INCENTIVES ) )
        {
          promotion = new SSIPromotion();
          promotion.setPromotionType( PromotionType.lookup( PromotionType.SELF_SERV_INCENTIVES ) );
        }

        promoBasicsForm.setPromotionTypeCode( promotionWizardManager.getPromotionType().getCode() );
        promoBasicsForm.setPromotionTypeName( promotionWizardManager.getPromotionType().getName() );

        promotionWizardManager.setPromotion( promotion );
        request.getSession().setAttribute( PromotionWizardManager.SESSION_KEY, promotionWizardManager );
      }

    }
    // NORMAL MODE
    else
    {
      if ( promotionId != null && promotionId.length() > 0 )
      {
        if ( promoBasicsForm.getPromotionTypeCode().equals( PromotionType.DIY_QUIZ ) )
        {
          AssociationRequestCollection promoAssociationRequestCollection = new AssociationRequestCollection();
          promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.CERTIFICATES ) );
          promotion = (QuizPromotion)getPromotionService().getPromotionByIdWithAssociations( new Long( promotionId ), promoAssociationRequestCollection );
          promoBasicsForm.load( promotion );
        }
        else if ( promoBasicsForm.getPromotionTypeCode().equals( PromotionType.RECOGNITION ) )
        {
          RecognitionPromotion recPromotion = (RecognitionPromotion)getPromotionService().getPromotionById( new Long( promotionId ) );
          request.getSession().setAttribute( "imageUrlFullPath", recPromotion.getDefaultContributorAvatar() );
          if ( recPromotion.isPurlStandardMessageEnabled() )
          {
            List<QuizLearningDetails> purlStandardMessagePictureObjectsDetails = getPromotionService().getPurlStandardMessagePictureObjects( recPromotion.getContentResourceCMCode() );
            Iterator pictureObjectsItr = purlStandardMessagePictureObjectsDetails.iterator();
            String imagePicUrl = "";
            String videoUrl = "";
            String videoUrlMp4 = "";
            String videoUrlWebm = "";
            String videoUrl3gp = "";
            String videoUrlOgg = "";
            String mediaPath = "";
            String leftColumn = "";
            String uploadType = "image";
            while ( pictureObjectsItr.hasNext() )
            {
              QuizLearningDetails pictureObjectsDetail = (QuizLearningDetails)pictureObjectsItr.next();
              mediaPath = pictureObjectsDetail.getFilePath();
              leftColumn = pictureObjectsDetail.getLeftColumn();
              if ( leftColumn.contains( "<p>" ) )
              {
                uploadType = "image";
                String s = "<img src=\"";
                int ix = leftColumn.indexOf( s ) + s.length();
                imagePicUrl = leftColumn.substring( ix, leftColumn.indexOf( "\"", ix + 1 ) );
                request.getSession().setAttribute( "imagePicUrlFullPath", imagePicUrl );
              }
              else
              {
                uploadType = "video";
                videoUrl = leftColumn;
                videoUrlMp4 = pictureObjectsDetail.getVideoUrlMp4();
                videoUrlWebm = pictureObjectsDetail.getVideoUrlWebm();
                videoUrl3gp = pictureObjectsDetail.getVideoUrl3gp();
                videoUrlOgg = pictureObjectsDetail.getVideoUrlOgg();
              }
              String rightColumn = pictureObjectsDetail.getRightColumn();
              promoBasicsForm.loadPurlStandardMessagePictureObjects( imagePicUrl, rightColumn, uploadType, mediaPath, videoUrlMp4, videoUrlWebm, videoUrl3gp, videoUrlOgg );
            }
            request.setAttribute( "imagePicUrl", imagePicUrl );
            request.setAttribute( "videoUrl", videoUrl );
            request.setAttribute( "videoUrlMp4", videoUrlMp4 );
            request.setAttribute( "videoUrlWebm", videoUrlWebm );
            request.setAttribute( "videoUrl3gp", videoUrl3gp );
            request.setAttribute( "videoUrlOgg", videoUrlOgg );
            request.setAttribute( "uploadType", uploadType );
          }
          promoBasicsForm.load( recPromotion );
          promotion = recPromotion;
        }
        else if ( promoBasicsForm.getPromotionTypeCode().equals( PromotionType.SELF_SERV_INCENTIVES ) )
        {
          SSIPromotion ssiPromotion = (SSIPromotion)getPromotionService().getPromotionById( new Long( promotionId ) );
          promoBasicsForm.setContestFilePath( getSSIPromotionService().buildSSIContestGuideUrl( ssiPromotion.getContestGuideUrl() ) );
          promoBasicsForm.load( ssiPromotion );
        }
        else
        {
          promotion = getPromotionService().getPromotionById( new Long( promotionId ) );
          promoBasicsForm.load( promotion );
        }
      }
    }

    if ( promotion != null )
    {
      if ( promotion.getPromotionType() != null )
      {
        request.setAttribute( "promotionType", promotion.getPromotionType().getCode() );
      }

      if ( promotion.isQuizPromotion() )
      {
        request.setAttribute( "certificateList", PromotionCertificate.getList( promotion.getPromotionType() ) );
      }
      if ( promotion.getPromotionType().getCode().equals( PromotionType.CHALLENGE_POINT ) || promotion.getPromotionType().getCode().equals( PromotionType.THROWDOWN ) )
      {
        promoBasicsForm.setAwardsType( PromotionAwardsType.lookup( PromotionAwardsType.POINTS ).getCode() );
        promoBasicsForm.setAwardsTypeName( PromotionAwardsType.lookup( PromotionAwardsType.POINTS ).getName() );
      }
    }

    return mapping.findForward( forwardTo );
  }

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward save( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionForward forward = mapping.findForward( ActionConstants.WIZARD_SAVE_AND_EXIT_ATTRIBUTE );
    ActionMessages errors = new ActionMessages();

    PromotionBasicsForm promotionBasicsForm = (PromotionBasicsForm)form;

    Promotion promotion = null;

    if ( isCancelled( request ) )
    {
      if ( isWizardMode( request ) )
      {
        forward = super.cancelPromotion( request, mapping, errors );
      }
      else
      {
        forward = getCancelForward( mapping, request );
      }

      return forward;
    }

    // WIZARD MODE
    if ( isWizardMode( request ) )
    {
      promotion = getWizardPromotion( request );
      if ( promotion == null )
      {
        return mapping.findForward( ActionConstants.FAIL_FORWARD );
      }

      // TODO: Verify if this were these default values should be set
      /** *** Begin set default data on not-nullable fields **** */
      String promotionId = promotionBasicsForm.getPromotionId();
      if ( promotion.isRecognitionPromotion() && ( promotionId == null || promotionId.length() == 0 ) )
      {
        RecognitionPromotion recognitionPromotion = (RecognitionPromotion)promotion;

        recognitionPromotion.setAwardActive( false );
        recognitionPromotion.setBehaviorActive( false );
        recognitionPromotion.setCardActive( false );
        recognitionPromotion.setSweepstakesActive( false );
        recognitionPromotion.setIncludePurl( false );
        recognitionPromotion.setCopyRecipientManager( false );
        recognitionPromotion.setIncludeCertificate( false );
        recognitionPromotion.setFileloadBudgetAmount( false );
        recognitionPromotion.setAwardAmountTypeFixed( true );
        recognitionPromotion.setAllowManagerAward( false );
        recognitionPromotion.setSweepstakesWinnerEligibilityType( (SweepstakesWinnerEligibilityType)PickListItem.getDefaultItem( SweepstakesWinnerEligibilityType.class ) );
        recognitionPromotion.setSweepstakesMultipleAwardType( (SweepstakesMultipleAwardsType)PickListItem.getDefaultItem( SweepstakesMultipleAwardsType.class ) );

        recognitionPromotion.setPublicRecogAwardAmountTypeFixed( true );
        // Celebration Page Fields
        recognitionPromotion.setServiceAnniversary( true );
        recognitionPromotion.setAnniversaryInYears( true );
        recognitionPromotion.setAllowOwnerMessage( true );
        recognitionPromotion.setAllowDefaultMessage( true );
        recognitionPromotion.setShareToMedia( true );
      }

      if ( promotion.isWellnessPromotion() && ( promotionId == null || promotionId.length() == 0 ) )
      {
        WellnessPromotion wellnessPromotion = (WellnessPromotion)promotion;
        wellnessPromotion.setAwardActive( false );
        wellnessPromotion.setAwardAmountTypeFixed( true );
      }

      if ( promotion.isSurveyPromotion() && ( promotionId == null || promotionId.length() == 0 ) )
      {
        SurveyPromotion survyePromotion = (SurveyPromotion)promotion;
        survyePromotion.setSweepstakesWinnerEligibilityType( (SweepstakesWinnerEligibilityType)PickListItem.getDefaultItem( SweepstakesWinnerEligibilityType.class ) );
      }

      if ( promotion.isEngagementPromotion() && ( promotionId == null || promotionId.length() == 0 ) )
      {
        EngagementPromotion engagementPromotion = (EngagementPromotion)promotion;
        engagementPromotion.setCompanyGoal( 0.0 );
      }

      if ( promotion.isProductClaimPromotion() && ( promotionId == null || promotionId.length() == 0 ) )
      {
        ProductClaimPromotion productClaimPromotion = (ProductClaimPromotion)promotion;
        productClaimPromotion.setSweepstakesActive( false );
        productClaimPromotion.setSweepstakesWinnerEligibilityType( SweepstakesWinnerEligibilityType.getProductClaimDefaultItem() );
        productClaimPromotion.setSweepstakesMultipleAwardType( (SweepstakesMultipleAwardsType)PickListItem.getDefaultItem( SweepstakesMultipleAwardsType.class ) );

        productClaimPromotion.setSweepstakesClaimEligibilityType( (SweepstakesClaimEligibilityType)PickListItem.getDefaultItem( SweepstakesClaimEligibilityType.class ) );
      }

      if ( promotion.isNominationPromotion() && ( promotionId == null || promotionId.length() == 0 ) )
      {
        NominationPromotion nominationPromotion = (NominationPromotion)promotion;

        nominationPromotion.setAwardActive( false );
        nominationPromotion.setBehaviorActive( false );
        nominationPromotion.setCardActive( false );
        nominationPromotion.setSweepstakesActive( false );
        nominationPromotion.setSelfNomination( false );
        nominationPromotion.setIncludeCertificate( false );
        nominationPromotion.setFileloadBudgetAmount( false );
        nominationPromotion.setAwardAmountTypeFixed( true );
        nominationPromotion.setSweepstakesWinnerEligibilityType( (SweepstakesWinnerEligibilityType)PickListItem.getDefaultItem( SweepstakesWinnerEligibilityType.class ) );
        nominationPromotion.setSweepstakesMultipleAwardType( (SweepstakesMultipleAwardsType)PickListItem.getDefaultItem( SweepstakesMultipleAwardsType.class ) );
        nominationPromotion.setEvaluationType( (NominationEvaluationType)PickListItem.getDefaultItem( NominationEvaluationType.class ) );
        nominationPromotion.setAwardGroupType( (NominationAwardGroupType)PickListItem.getDefaultItem( NominationAwardGroupType.class ) );
        nominationPromotion.setPublicationDateActive( false );
        nominationPromotion.setAllowPublicRecognition( false );

      }

      /** *** End set default data on not-nullable fields **** */
    }
    else
    {
      String promotionId = promotionBasicsForm.getPromotionId();
      if ( promotionId != null && promotionId.length() > 0 )
      {
        if ( promotionBasicsForm.getPromotionTypeCode().equals( PromotionType.DIY_QUIZ ) )
        {
          promotion = new QuizPromotion();
          AssociationRequestCollection promoAssociationRequestCollection = new AssociationRequestCollection();
          promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.CERTIFICATES ) );
          promotion = (QuizPromotion)getPromotionService().getPromotionByIdWithAssociations( new Long( promotionId ), promoAssociationRequestCollection );
        }
        else
        {
          promotion = getPromotionService().getPromotionById( new Long( promotionId ) );
        }
      }
      else
      {
        if ( promotionBasicsForm.getPromotionTypeCode().equals( PromotionType.PRODUCT_CLAIM ) )
        {
          promotion = new ProductClaimPromotion();
        }
        else if ( promotionBasicsForm.getPromotionTypeCode().equals( PromotionType.RECOGNITION ) )
        {
          promotion = new RecognitionPromotion();
        }
        else if ( promotionBasicsForm.getPromotionTypeCode().equals( PromotionType.QUIZ ) )
        {
          promotion = new QuizPromotion();
        }
        else if ( promotionBasicsForm.getPromotionTypeCode().equals( PromotionType.DIY_QUIZ ) )
        {
          promotion = new QuizPromotion();
        }
        else if ( promotionBasicsForm.getPromotionTypeCode().equals( PromotionType.NOMINATION ) )
        {
          promotion = new NominationPromotion();
        }
        else if ( promotionBasicsForm.getPromotionTypeCode().equals( PromotionType.SURVEY ) )
        {
          promotion = new SurveyPromotion();
        }
        else if ( promotionBasicsForm.getPromotionTypeCode().equals( PromotionType.GOALQUEST ) )
        {
          promotion = new GoalQuestPromotion();
        }

        else if ( promotionBasicsForm.getPromotionTypeCode().equals( PromotionType.CHALLENGE_POINT ) )
        {
          promotion = new ChallengePointPromotion();
        }
        else if ( promotionBasicsForm.getPromotionTypeCode().equals( PromotionType.WELLNESS ) )
        {
          promotion = new WellnessPromotion();
        }
        else if ( promotionBasicsForm.getPromotionTypeCode().equals( PromotionType.ENGAGEMENT ) )
        {
          promotion = new EngagementPromotion();
        }
        else if ( promotionBasicsForm.getPromotionTypeCode().equals( PromotionType.SELF_SERV_INCENTIVES ) )
        {
          promotion = new SSIPromotion();
        }
      }
    }

    try
    {
      boolean gqawardmediaaltered = false;
      // bugFix 20294.
      if ( promotionBasicsForm.getAwardsType() != null && promotion != null && promotion.getAwardType() != null && promotion.getAwardType().getCode().equals( PromotionAwardsType.MERCHANDISE )
          && promotionBasicsForm.getAwardsType().equals( PromotionAwardsType.POINTS ) )
      {
        gqawardmediaaltered = true;
      }
      promotionBasicsForm.toDomain( promotion );

      if ( promotion.isRecognitionPromotion() )
      {
        String imageUrlFullPath = (String)request.getSession().getAttribute( "imageUrlFullPath" );
        String imagePicUrlFullPath = (String)request.getSession().getAttribute( "imagePicUrlFullPath" );
        // request.getSession().removeAttribute( "imageUrlFullPath" );
        if ( promotionBasicsForm.isPurlStandardMessageEnabled() )
        {
          if ( imageUrlFullPath != null && !imageUrlFullPath.isEmpty() )
          {
            ( (RecognitionPromotion)promotion ).setDefaultContributorAvatar( imageUrlFullPath );
            promotionBasicsForm.setImageUrl( imageUrlFullPath );
          }

          if ( imagePicUrlFullPath != null && !imagePicUrlFullPath.isEmpty() )
          {
            promotionBasicsForm.setImagePicUrl( imagePicUrlFullPath );
          }
        }

        if ( promotion != null )
        {
          if ( promotion.isRecognitionPromotion() && promotionBasicsForm.isPurlStandardMessageEnabled() )
          {
            String withoutHtml = (String)StringUtil.escapeHTML( promotionBasicsForm.getImageUrl() );
            if ( imageUrlFullPath == null && ( promotionBasicsForm.getImageUrl() == null || promotionBasicsForm.getImageUrl().isEmpty() ) || withoutHtml.contains( "undefined" ) )
            {
              errors.add( "defaultContributorAvatar", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.basics.AVATAR" ) ) );
              saveErrors( request, errors );
              forward = mapping.findForward( ActionConstants.FAIL_FORWARD );
              return forward;
            }

            String mediaUrl = "";
            String mediaHtmlString = "";
            QuizLearningObject quizObj = new QuizLearningObject();
            String mediaFilePath = promotionBasicsForm.getMediaFilePath();

            if ( promotionBasicsForm.getUploadType().equalsIgnoreCase( "video" ) )
            {
              mediaUrl = promotionBasicsForm.getVideoUrl();
              mediaHtmlString = getHtmlVideoString( mediaUrl );
            }

            if ( promotionBasicsForm.getUploadType().equalsIgnoreCase( "image" ) )
            {
              if ( imagePicUrlFullPath != null && !imagePicUrlFullPath.isEmpty() )
              {
                mediaUrl = promotionBasicsForm.getImagePicUrl();
                mediaHtmlString = getHtmlImageString( mediaUrl );
              }
            }

            if ( promotionBasicsForm.getUploadType().equalsIgnoreCase( "video" ) )
            {
              promotion = getPromotionService().savePurlStandardMessageVideo( (RecognitionPromotion)promotion,
                                                                              promotionBasicsForm.getVideoUrlMp4(),
                                                                              promotionBasicsForm.getVideoUrlWebm(),
                                                                              promotionBasicsForm.getVideoUrl3gp(),
                                                                              promotionBasicsForm.getVideoUrlOgg(),
                                                                              null );
            }
            else
            {
              promotion = getPromotionService().savePurlStandardMessageImage( (RecognitionPromotion)promotion, mediaHtmlString, null, mediaFilePath );
            }
          }
        }
        RecognitionPromotion recognitionPromotion = (RecognitionPromotion)promotion;
        if ( recognitionPromotion.isIncludeCelebrations() && recognitionPromotion.isAllowRecognitionSendDate() )
        {
          if ( recognitionPromotion.isIncludePurl() )
          {
            ( (RecognitionPromotion)promotion ).setAllowRecognitionSendDate( false );
          }
        }
      }

      if ( promotionBasicsForm.getIssuanceMethod() != null )
      {
        if ( promotionBasicsForm.getIssuanceMethod().equalsIgnoreCase( "online" ) )
        {
          promotion.setOnlineEntry( true );
        }
        else if ( promotionBasicsForm.getIssuanceMethod().equalsIgnoreCase( "file_load" ) )
        {
          promotion.setFileLoadEntry( true );
        }
      }
      // save promo_name_asset_code in cm
      if ( promotionBasicsForm.getPromotionName() != null )
      {
        promotion = getPromotionService().savePromoNameCmText( promotion, promotionBasicsForm.getPromotionName() );
      }

      if ( promotionBasicsForm.getPromotionTypeCode().equals( PromotionType.QUIZ ) || promotionBasicsForm.getPromotionTypeCode().equals( PromotionType.DIY_QUIZ ) )
      {
        if ( !StringUtils.isEmpty( promotionBasicsForm.getOverviewDetailsText() ) )
        {
          promotion = getPromotionService().savePromotionOverviewCmText( promotion, promotionBasicsForm.getOverviewDetailsText() );
        }
      }

      if ( promotionBasicsForm.getPromotionTypeCode().equals( PromotionType.GOALQUEST ) || promotionBasicsForm.getPromotionTypeCode().equals( PromotionType.CHALLENGE_POINT ) )
      {
        GoalQuestPromotion goalQuestPromotion = (GoalQuestPromotion)promotion;
        if ( promotionBasicsForm.getPromotionObjective() != null )
        {
          promotion = getPromotionService().savePromoObjectivieCmText( goalQuestPromotion, promotionBasicsForm.getPromotionObjective() );
        }
        if ( !StringUtils.isEmpty( promotionBasicsForm.getOverviewDetailsText() ) )
        {
          promotion = getPromotionService().savePromotionOverviewCmText( promotion, promotionBasicsForm.getOverviewDetailsText() );
        }
      }

      if ( promotionBasicsForm.getPromotionTypeCode().equals( PromotionType.THROWDOWN ) )
      {
        if ( !StringUtils.isEmpty( promotionBasicsForm.getOverviewDetailsText() ) )
        {
          promotion = getPromotionService().savePromotionOverviewCmText( promotion, promotionBasicsForm.getOverviewDetailsText() );
        }
      }

      if ( promotionBasicsForm.getPromotionTypeCode().equals( PromotionType.ENGAGEMENT ) )
      {
        promotion = getPromotionService().savePromotionOverviewCmText( promotion, promotionBasicsForm.getOverviewDetailsText() );
      }

      // Bug # 38684 End//
      if ( promotionBasicsForm.getPromotionTypeCode().equals( PromotionType.QUIZ ) )
      {
        if ( promotion instanceof QuizPromotion )
        {
          QuizPromotion quizPromotion = (QuizPromotion)promotion;
          if ( quizPromotion.getId() != null && quizPromotion.getId().longValue() > 0 )
          {
            promotion = getPromotionService().savePromotion( quizPromotion );
          }

          Quiz quiz = getQuizService().getQuizById( new Long( promotionBasicsForm.getActivityForm() ) );
          quizPromotion.setQuiz( quiz );

          promotion = getPromotionService().savePromotion( quizPromotion );
        }
      }
      else if ( promotionBasicsForm.getPromotionTypeCode().equals( PromotionType.DIY_QUIZ ) )
      {
        if ( promotion instanceof QuizPromotion )
        {
          QuizPromotion quizPromotion = (QuizPromotion)promotion;

          if ( isNewBadge( quizPromotion, promotionBasicsForm ) )
          {
            createBadge( quizPromotion, promotionBasicsForm );
          }

          if ( promotion.getId() == null )
          {
            promotion = getPromotionService().savePromotion( quizPromotion );
            if ( promotion.getBadge() != null )
            {
              BadgePromotion badgePromo = new BadgePromotion();
              badgePromo.setBadgePromotion( promotion.getBadge() );
              badgePromo.setEligiblePromotion( promotion );
              getGamificationService().saveBadgePromotion( badgePromo );
            }
          }
          else
          {
            Integer badgePromotionCount = getPromotionService().getBadgePromotionCountForPromoId( promotion.getId() );
            if ( badgePromotionCount == 0 )
            {
              BadgePromotion badgePromo = new BadgePromotion();
              badgePromo.setBadgePromotion( promotion.getBadge() );
              badgePromo.setEligiblePromotion( promotion );
              getGamificationService().saveBadgePromotion( badgePromo );
            }

            if ( badgePromotionCount != 0 )
            {
              List<BadgeRule> badgeRulesList = createBadgeRules( promotion.getBadge(), promotionBasicsForm, quizPromotion );
              promotion.getBadge().setBadgeRules( new HashSet<BadgeRule>( badgeRulesList ) );
            }
            // Existing promotion
            List updateAssociations = new ArrayList();
            PromotionCertificateUpdateAssociation promoCertificateUpdateAssociation = new PromotionCertificateUpdateAssociation( quizPromotion );
            updateAssociations.add( promoCertificateUpdateAssociation );
            PromotionBadgeUpdateAssociation promotionBadgeUpdateAssociation = new PromotionBadgeUpdateAssociation( quizPromotion );
            updateAssociations.add( promotionBadgeUpdateAssociation );
            PromotionBasicsUpdateAssociation promotionBasicsUpdateAssociation = new PromotionBasicsUpdateAssociation( quizPromotion );
            updateAssociations.add( promotionBasicsUpdateAssociation );
            promotion = (QuizPromotion)getPromotionService().savePromotion( quizPromotion.getId(), updateAssociations );
          }
        }
      }
      else if ( promotionBasicsForm.getPromotionTypeCode().equals( PromotionType.SURVEY ) )
      {
        if ( promotion instanceof SurveyPromotion )
        {
          SurveyPromotion surveyPromotion = (SurveyPromotion)promotion;
          if ( surveyPromotion.getId() != null && surveyPromotion.getId().longValue() > 0 )
          {
            promotion = getPromotionService().savePromotion( surveyPromotion );
          }

          Survey survey = getSurveyService().getSurveyById( new Long( promotionBasicsForm.getActivityForm() ) );
          surveyPromotion.setSurvey( survey );

          promotion = getPromotionService().savePromotion( surveyPromotion );
        }
      }
      else if ( promotionBasicsForm.getPromotionTypeCode().equals( PromotionType.WELLNESS ) )
      {
        if ( promotion instanceof WellnessPromotion )
        {
          WellnessPromotion wellnessPromotion = (WellnessPromotion)promotion;
          promotion = getPromotionService().savePromotion( wellnessPromotion );
        }
      }
      else if ( promotionBasicsForm.getPromotionTypeCode().equals( PromotionType.GOALQUEST )
          && ( promotionBasicsForm.getActivityForm() == null || promotionBasicsForm.getActivityForm().equals( "" ) ) )
      {
        if ( promotion instanceof GoalQuestPromotion )
        {
          GoalQuestPromotion goalQuestPromotion = (GoalQuestPromotion)promotion;
          if ( gqawardmediaaltered )
          {
            goalQuestPromotion.setPayoutStructure( PayoutStructure.lookup( PayoutStructure.FIXED ) );
          }
          promotion = getPromotionService().savePromotion( goalQuestPromotion );
        }
      }

      else if ( promotionBasicsForm.getPromotionTypeCode().equals( PromotionType.CHALLENGE_POINT )
          && ( promotionBasicsForm.getActivityForm() == null || promotionBasicsForm.getActivityForm().equals( "" ) ) )
      {
        if ( promotion instanceof ChallengePointPromotion )
        {
          ChallengePointPromotion challengePointPromotion = (ChallengePointPromotion)promotion;
          if ( logger.isDebugEnabled() )
          {
            logger.debug( "before saving challenge point promotion challenge point award type-->" + challengePointPromotion.getChallengePointAwardType() );
          }
          promotion = getPromotionService().savePromotion( challengePointPromotion );
        }
      }
      else if ( promotionBasicsForm.getPromotionTypeCode().equals( PromotionType.THROWDOWN ) )
      {
        if ( promotion instanceof ThrowdownPromotion )
        {
          ThrowdownPromotion throwdownPromotion = (ThrowdownPromotion)promotion;
          promotion = getPromotionService().savePromotion( throwdownPromotion );
        }
      }
      else if ( promotionBasicsForm.getPromotionTypeCode().equals( PromotionType.ENGAGEMENT ) )
      {
        if ( promotion instanceof EngagementPromotion )
        {
          EngagementPromotion engagementPromotion = (EngagementPromotion)promotion;
          // If eligible promotions is modified then set the last executed date as null;
          // Bug 55774
          if ( promotionBasicsForm.isEngagementEligiblePromotionsModified() )
          {
            engagementPromotion.setPrevProcessDate( null );
          }
          promotion = getPromotionService().savePromotion( engagementPromotion );
        }
      }
      else if ( promotionBasicsForm.getPromotionTypeCode().equals( PromotionType.SELF_SERV_INCENTIVES ) )
      {
        if ( promotion instanceof SSIPromotion )
        {
          SSIPromotion ssiPromotion = (SSIPromotion)promotion;

          if ( request.getSession().getAttribute( "ssiPdfFullPath" ) != null )
          {
            String contestGuideUrl = (String)request.getSession().getAttribute( "ssiPdfFullPath" );
            ssiPromotion.setContestGuideUrl( contestGuideUrl );
          }

          if ( ssiPromotion.getId() != null )
          {
            List updateAssociations = new ArrayList();
            PromotionBasicsUpdateAssociation promotionBasicsUpdateAssociation = new PromotionBasicsUpdateAssociation( ssiPromotion );
            updateAssociations.add( promotionBasicsUpdateAssociation );
            promotion = getPromotionService().savePromotion( ssiPromotion.getId(), updateAssociations );
          }
          else
          {
            promotion = getPromotionService().savePromotion( ssiPromotion );
          }

          request.getSession().removeAttribute( "ssiPdfFullPath" );
        }
      }
      else
      {
        promotion = getPromotionService().savePromotion( promotion );
        AssociationRequestCollection arc = new AssociationRequestCollection();
        arc.add( new ClaimFormAssociationRequest( ClaimFormAssociationRequest.STEPS ) );

        ClaimForm claimForm = getClaimFormService().getClaimFormByIdWithAssociations( new Long( promotionBasicsForm.getActivityForm() ), arc );
        promotion.setClaimForm( claimForm );

        String promotionId = promotionBasicsForm.getPromotionId();
        if ( promotionId != null && promotionId.length() > 0 )
        {
          // existing promotion
          PromotionBasicsUpdateAssociation pbua = new PromotionBasicsUpdateAssociation( promotion );
          getPromotionService().validatePromotion( promotion.getId(), pbua );
          promotion = getPromotionService().savePromotion( promotion.getId(), pbua );
        }
        else
        {
          // new promotion - just save it
          promotion = getPromotionService().savePromotion( promotion );
        }
      }

      if ( promotion != null && promotion.isGoalQuestOrChallengePointPromotion() && promotion.getAwardType().getCode().equals( PromotionAwardsType.POINTS ) )
      {
        promotionBasicsForm.setGqpartnersEnabled( "true" );
      }
      if ( promotion != null )
      {
        if ( isWizardMode( request ) )
        {

          setPromotionInWizardManager( request, promotion );
          forward = getWizardNextPage( mapping, request, promotion );

        }
        else
        {
          forward = saveAndExit( mapping, request, promotion );
        }
      }
    }
    catch( ConstraintViolationException e )
    {
      logger.error( e.getMessage(), e );
      errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "promotion.errors.UNIQUE_CONSTRAINT" ) );
    }
    catch( UniqueConstraintViolationException e )
    {
      logger.error( e.getMessage(), e );
      errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "promotion.errors.UNIQUE_CONSTRAINT" ) );
    }
    catch( MalformedURLException | JSONException e )
    {
      logger.error( e.getMessage(), e );
      errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "promotion.errors.UNIQUE_CONSTRAINT" ) );
    }
    catch( ServiceErrorExceptionWithRollback e )
    {
      ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( e.getServiceErrors(), errors );
    }
    catch( ServiceErrorException e1 )
    {
      logger.error( e1.getMessage(), e1 );
    }
    catch( Exception e1 )
    {
      logger.error( e1.getMessage(), e1 );
    }

    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      forward = mapping.findForward( ActionConstants.FAIL_FORWARD );
    }

    return forward;
  }

  private void createBadge( QuizPromotion quizPromotion, PromotionBasicsForm promotionBasicsForm ) throws ServiceErrorException, UniqueConstraintViolationException
  {
    Badge promotion = new Badge();
    if ( promotionBasicsForm.getBadgeId() != null && !promotionBasicsForm.getBadgeId().equals( 0L ) )
    {
      promotion.setId( promotionBasicsForm.getBadgeId() );
    }
    promotion.setName( quizPromotion.getName() + "Badge" );
    promotion.setBadgeType( BadgeType.lookup( BadgeType.EARNED_OR_NOT_EARNED ) );
    promotion.setPromotionType( PromotionType.lookup( PromotionType.BADGE ) );
    promotion = (Badge)getPromotionService().savePromoNameCmText( promotion, quizPromotion.getName() + " Badge" );
    promotion.setDisplayEndDays( new Long( 0 ) );
    promotion.setTileHighlightPeriod( new Long( 0 ) );
    promotion.setStatus( Badge.BADGE_ACTIVE );
    promotion.setPromotionStatus( generatePromotionStatus( quizPromotion.getSubmissionStartDate() ) );
    promotion.setSubmissionStartDate( quizPromotion.getSubmissionStartDate() );
    promotion.setAwardType( PromotionAwardsType.lookup( PromotionAwardsType.POINTS ) );
    promotion.setSweepstakesWinnerEligibilityType( (SweepstakesWinnerEligibilityType)PickListItem.getDefaultItem( SweepstakesWinnerEligibilityType.class ) );

    Badge badgeReturned = (Badge)getPromotionService().savePromotion( promotion );

    List<BadgeRule> badgeRulesList = createBadgeRules( badgeReturned, promotionBasicsForm, quizPromotion );
    promotion.setBadgeRules( new HashSet<BadgeRule>( badgeRulesList ) );
    quizPromotion.setBadge( badgeReturned );

  }

  public PromotionStatusType generatePromotionStatus( Date badgeStartDate )
  {
    Date currentDate = new Date();
    // if the submission start date is has passed, automatically set the promotion to live
    if ( badgeStartDate.before( currentDate ) )
    {
      return PromotionStatusType.lookup( PromotionStatusType.LIVE );
    }
    else
    {
      return PromotionStatusType.lookup( PromotionStatusType.COMPLETE );
    }
  }

  private List<BadgeRule> createBadgeRules( Badge badgeReturned, PromotionBasicsForm promotionBasicsForm, QuizPromotion quizPromotion ) throws ServiceErrorException
  {
    List<PromotionBasicsBadgeFormBean> promotionBasicsBadgeFormBeanList = promotionBasicsForm.getPromotionBasicsBadgeFormBeanList();
    List<BadgeRule> badgeRulesFinalList = new ArrayList<BadgeRule>();
    for ( PromotionBasicsBadgeFormBean displayBean : promotionBasicsBadgeFormBeanList )
    {
      if ( quizPromotion.isUnderConstruction() && displayBean.isSelected() )
      {
        BadgeRule badgeRule = new BadgeRule();
        if ( displayBean.getBadgeRuleId() != null && !displayBean.getBadgeRuleId().equals( 0L ) )
        {
          badgeRule.setId( displayBean.getBadgeRuleId() );
        }
        String badgeNameCmAsset = getGamificationService().saveRulesCmText( badgeRule.getBadgeName(), displayBean.getBadgeName() );
        badgeRule.setBadgePromotion( badgeReturned );
        badgeRule.setMaximumQualifier( 0L );
        badgeRule.setMinimumQualifier( 0L );
        badgeRule.setBadgeLibraryCMKey( displayBean.getCmAssetKey() );
        badgeRule.setBadgeName( badgeNameCmAsset );
        badgeRulesFinalList.add( badgeRule );
      }
      else if ( !quizPromotion.isUnderConstruction() && ( displayBean.isSelected() || displayBean.getBadgeRuleId() != null && displayBean.getBadgeRuleId() > 0 ) )
      {
        BadgeRule badgeRule = new BadgeRule();
        if ( displayBean.getBadgeRuleId() != null && !displayBean.getBadgeRuleId().equals( 0L ) )
        {
          badgeRule.setId( displayBean.getBadgeRuleId() );
        }
        String badgeNameCmAsset = getGamificationService().saveRulesCmText( badgeRule.getBadgeName(), displayBean.getBadgeName() );
        badgeRule.setBadgePromotion( badgeReturned );
        badgeRule.setMaximumQualifier( 0L );
        badgeRule.setMinimumQualifier( 0L );
        badgeRule.setBadgeLibraryCMKey( displayBean.getCmAssetKey() );
        badgeRule.setBadgeName( badgeNameCmAsset );
        badgeRulesFinalList.add( badgeRule );
      }
    }
    return badgeRulesFinalList;
  }

  /**
   * Get the GamificationService from the beanFactory locator.
   *
   * @return GamificationService
   */
  private GamificationService getGamificationService()
  {
    return (GamificationService)getService( GamificationService.BEAN_NAME );
  }

  private ClaimFormDefinitionService getClaimFormService()
  {
    return (ClaimFormDefinitionService)getService( ClaimFormDefinitionService.BEAN_NAME );
  }

  /**
   * Get the quizService from the applicationContext.
   *
   * @return QuizService
   */
  private QuizService getQuizService()
  {
    return (QuizService)getService( QuizService.BEAN_NAME );
  }

  private SurveyService getSurveyService()
  {
    return (SurveyService)getService( SurveyService.BEAN_NAME );
  }

  public ActionForward processPhoto( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    String status = JsonResponse.STATUS_UNKNOWN;
    String globalUniqueId = "";
    boolean detailImgmovetoWebDav = false;
    boolean thumbImgmovetoWebDav = false;

    String imageSizeValidMessage = CmsResourceBundle.getCmsBundle().getString( "quiz.learningForm.VALID_IMAGE_SIZE" );
    String imageSizeLowerLimitMessage = CmsResourceBundle.getCmsBundle().getString( "quiz.learningForm.VALID_IMAGE_LOWER_LIMIT" );
    try
    {
      PromotionBasicsForm promotionBasicsForm = (PromotionBasicsForm)form;
      String promotionName = promotionBasicsForm.getPromotionName() != null ? promotionBasicsForm.getPromotionName() : "";
      String orginalfilename = promotionBasicsForm.getFileAsset().getFileName();
      String extension = "." + getFileExtension( orginalfilename );
      String filename = orginalfilename.substring( 0, orginalfilename.length() - extension.length() );
      if ( filename != null )
      {
        filename = ValidatorChecks.removesSpecialCharactersAndSpaces( filename );
      }
      filename = filename + extension;
      int filesize = promotionBasicsForm.getFileAsset().getFileSize();
      byte[] imageInByte = promotionBasicsForm.getFileAsset().getFileData();

      // Not getting the parameter through AJAX file upload, so get it from the session
      globalUniqueId = String.valueOf( new Date().getTime() );

      PurlFileUploadValue data = new PurlFileUploadValue();
      data.setData( imageInByte );
      data.setType( PurlFileUploadValue.TYPE_AVATAR );
      data.setName( filename );
      data.setSize( filesize );
      boolean validImageSize = getPurlService().validFileData( data );

      if ( validImageSize )
      {
        String imageInBytes = Base64.getEncoder().encodeToString( imageInByte );
        String contentType = promotionBasicsForm.getFileAsset().getContentType();

        String avatarUrl = null;

        avatarUrl = getProfileAvatarService().uploadDefaultPurlAvatar( RosterUtil.getRandomUUId().toString(), ProfileAvatarUploadUtil.getImageData( contentType, imageInBytes ) );

        if ( Objects.nonNull( avatarUrl ) )
        {
          status = JsonResponse.STATUS_SUCCESS;
          String imageUrlPath = avatarUrl;
          request.getSession().setAttribute( "imageUrlFullPath", imageUrlPath );

          AdminPurlAvatarObjectView purlAvatarView = new AdminPurlAvatarObjectView();
          purlAvatarView.setFull( imageUrlPath );
          purlAvatarView.setFilename( data.getName() );
          purlAvatarView.setPromotionName( promotionName );
          purlAvatarView.setId( globalUniqueId );
          purlAvatarView.setImageurl( imageUrlPath );
          purlAvatarView.setMedia( "picture" );
          purlAvatarView.setStatus( status );
          purlAvatarView.setThumb( imageUrlPath );
          writeUploadJsonToResponse( purlAvatarView, response );
        }
        else
        {
          log.error( "Uploading avatar to profile service status : " + avatarUrl );
          status = JsonResponse.STATUS_FAILED;
          AdminPurlAvatarObjectView purlAvatarView = new AdminPurlAvatarObjectView();
          purlAvatarView.setStatus( status );
          purlAvatarView.setId( globalUniqueId );
          purlAvatarView.setFail( CmsResourceBundle.getCmsBundle().getString( "profile.personal.info.PLEASE_TRY_LATER" ) );
        }
      }
      else
      {
        status = JsonResponse.STATUS_FAILED;
        AdminPurlAvatarObjectView purlAvatarView = new AdminPurlAvatarObjectView();
        double lowerSizeLimit = 1024 * 1024 * .001;
        if ( data.getSize() <= lowerSizeLimit )
        {
          purlAvatarView.setFail( imageSizeLowerLimitMessage );
        }
        else
        {
          purlAvatarView.setFail( imageSizeValidMessage );
        }
        purlAvatarView.setStatus( status );
        writeUploadJsonToResponse( purlAvatarView, response );
      }
    }
    catch( Throwable e )
    {
      status = JsonResponse.STATUS_FAILED;
      AdminPurlAvatarObjectView purlAvatarView = new AdminPurlAvatarObjectView();
      purlAvatarView.setStatus( status );
      purlAvatarView.setId( globalUniqueId );
      purlAvatarView.setFail( imageSizeValidMessage );
      logger.error( "Error during recognition process photo:" + e );
      e.printStackTrace();
    }
    return null;
  }

  public ActionForward processPhotoPicture( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    String status = JsonResponse.STATUS_UNKNOWN;
    String globalUniqueId = "";
    boolean detailImgmovetoWebDav = false;
    boolean thumbImgmovetoWebDav = false;

    String imageSizeValidMessage = CmsResourceBundle.getCmsBundle().getString( "quiz.learningForm.VALID_IMAGE_SIZE" );
    String imageSizeLowerLimitMessage = CmsResourceBundle.getCmsBundle().getString( "quiz.learningForm.VALID_IMAGE_LOWER_LIMIT" );
    try
    {
      PromotionBasicsForm promotionBasicsForm = (PromotionBasicsForm)form;
      String promotionName = promotionBasicsForm.getPromotionName() != null ? promotionBasicsForm.getPromotionName() : "";
      String orginalfilename = promotionBasicsForm.getFileAssetPic().getFileName();
      String extension = "." + getFileExtension( orginalfilename );
      String filename = orginalfilename.substring( 0, orginalfilename.length() - extension.length() );
      if ( filename != null )
      {
        filename = ValidatorChecks.removesSpecialCharactersAndSpaces( filename );
      }
      filename = filename + extension;
      int filesize = promotionBasicsForm.getFileAssetPic().getFileSize();
      byte[] imageInByte = promotionBasicsForm.getFileAssetPic().getFileData();

      // Not getting the parameter through AJAX file upload, so get it from the session
      globalUniqueId = String.valueOf( new Date().getTime() );

      PurlFileUploadValue data = new PurlFileUploadValue();
      data.setData( imageInByte );
      data.setType( PurlFileUploadValue.TYPE_PICTURE );
      data.setName( filename );
      data.setSize( filesize );
      boolean validImageSize = getPurlService().validFileData( data );

      if ( validImageSize )
      {
        String imageInBytes = Base64.getEncoder().encodeToString( imageInByte );
        String contentType = promotionBasicsForm.getFileAssetPic().getContentType();

        String pictureUrl = null;

        pictureUrl = getProfileAvatarService().uploadDefaultPurlPicture( RosterUtil.getRandomUUId().toString(), ProfileAvatarUploadUtil.getImageData( contentType, imageInBytes ) );

        String urlPath = pictureUrl;
        String[] splitArr = urlPath.split( "/" );
        pictureUrl = MeshServicesUtil.getNackleMeshServicesBaseEndPoint() + IMAGE_SERVICE_LARGE_IMG + splitArr[splitArr.length - 1];

        if ( Objects.nonNull( pictureUrl ) )
        {
          status = JsonResponse.STATUS_SUCCESS;
          String imageUrlPath = pictureUrl;
          request.getSession().setAttribute( "imagePicUrlFullPath", imageUrlPath );

          AdminPurlAvatarObjectView purlAvatarView = new AdminPurlAvatarObjectView();
          purlAvatarView.setFull( imageUrlPath );
          purlAvatarView.setFilename( data.getName() );
          purlAvatarView.setPromotionName( promotionName );
          purlAvatarView.setId( globalUniqueId );
          purlAvatarView.setImageurl( imageUrlPath );
          purlAvatarView.setMedia( "picture" );
          purlAvatarView.setStatus( status );
          purlAvatarView.setThumb( imageUrlPath );
          writeUploadJsonToResponse( purlAvatarView, response );
        }
        else
        {
          log.error( "Uploading picture to Image service status : " + pictureUrl );
          status = JsonResponse.STATUS_FAILED;
          AdminPurlAvatarObjectView purlAvatarView = new AdminPurlAvatarObjectView();
          purlAvatarView.setStatus( status );
          purlAvatarView.setId( globalUniqueId );
          purlAvatarView.setFail( CmsResourceBundle.getCmsBundle().getString( "profile.personal.info.PLEASE_TRY_LATER" ) );
        }
      }
      else
      {
        status = JsonResponse.STATUS_FAILED;
        AdminPurlAvatarObjectView purlAvatarView = new AdminPurlAvatarObjectView();
        double lowerSizeLimit = 1024 * 1024 * .001;
        if ( data.getSize() <= lowerSizeLimit )
        {
          purlAvatarView.setFail( imageSizeLowerLimitMessage );
        }
        else
        {
          purlAvatarView.setFail( imageSizeValidMessage );
        }
        purlAvatarView.setStatus( status );
        writeUploadJsonToResponse( purlAvatarView, response );
      }
    }
    catch( Throwable e )
    {
      status = JsonResponse.STATUS_FAILED;
      AdminPurlAvatarObjectView purlAvatarView = new AdminPurlAvatarObjectView();
      purlAvatarView.setStatus( status );
      purlAvatarView.setId( globalUniqueId );
      purlAvatarView.setFail( imageSizeValidMessage );
      logger.error( "Error during picture process photo:" + e );
      e.printStackTrace();
    }
    return null;
  }

  public ActionForward processVideo( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    String videoSizeValidMessage = CmsResourceBundle.getCmsBundle().getString( "quiz.learningForm.VALID_VIDEO_SIZE" );

    String status = JsonResponse.STATUS_UNKNOWN;
    boolean detailImgmovetoWebDav = false;
    boolean thumbImgmovetoWebDav = false;
    String globalUniqueId = "";
    try
    {
      PromotionBasicsForm promotionBasicsForm = (PromotionBasicsForm)form;
      String filename = null;
      int filesize = 0;
      byte[] imageInByte = null;
      for ( int i = 0; i < promotionBasicsForm.getFileAssetVideo().size(); i++ )
      {
        filename = promotionBasicsForm.getFileAssetVideo().get( i ).getFileName();
        filesize = promotionBasicsForm.getFileAssetVideo().get( i ).getFileSize();
        imageInByte = promotionBasicsForm.getFileAssetVideo().get( i ).getFileData();
      }
      // Not getting the parameter through AJAX file upload, so get it from the session
      globalUniqueId = String.valueOf( new Date().getTime() );

      PurlFileUploadValue data = new PurlFileUploadValue();
      data.setData( imageInByte );
      data.setType( PurlFileUploadValue.TYPE_VIDEO );
      data.setName( filename );
      data.setSize( filesize );

      boolean validVideoSize = getPurlService().validVideoFileData( data );
      if ( validVideoSize )
      {
        data = getPurlService().uploadVideoForStandardMessage( data );
        detailImgmovetoWebDav = getQuizService().moveFileToWebdav( data.getFull() );

        status = JsonResponse.STATUS_SUCCESS;
        String siteUrlPrefix = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
        if ( !Environment.isCtech() )
        {
          siteUrlPrefix = getSystemVariableService().getPropertyByName( SystemVariableService.SITE_URL_PREFIX + "." + Environment.ENV_QA ).getStringVal();
        }

        String imageUrlPath = siteUrlPrefix + "-cm/cm3dam" + '/' + data.getFull();

        // imageUrlPath=imageUrlPath.substring( 0, imageUrlPath.lastIndexOf( "." ) );
        String fileExtension = data.getFull().substring( data.getFull().lastIndexOf( "." ) + 1, data.getFull().length() );

        AdminPurlAvatarObjectView purlVideoView = new AdminPurlAvatarObjectView();
        if ( !StringUtils.isEmpty( fileExtension ) && fileExtension.equalsIgnoreCase( "mp4" ) )
        {
          purlVideoView.setVideoUrlMp4( imageUrlPath );
        }
        else if ( !StringUtils.isEmpty( fileExtension ) && fileExtension.equalsIgnoreCase( "webm" ) )
        {
          purlVideoView.setVideoUrlWebm( imageUrlPath );
        }
        else if ( !StringUtils.isEmpty( fileExtension ) && fileExtension.equalsIgnoreCase( "3gp" ) )
        {
          purlVideoView.setVideoUrl3gp( imageUrlPath );
        }
        else if ( !StringUtils.isEmpty( fileExtension ) && fileExtension.equalsIgnoreCase( "ogg" ) )
        {
          purlVideoView.setVideoUrlOgg( imageUrlPath );
        }

        purlVideoView.setFull( data.getFull() );
        purlVideoView.setFilename( data.getName() );
        purlVideoView.setId( globalUniqueId );
        purlVideoView.setImageurl( imageUrlPath );
        purlVideoView.setMedia( "video" );
        purlVideoView.setStatus( status );
        purlVideoView.setThumb( data.getThumb() );
        writeUploadJsonToResponse( purlVideoView, response );
      }
      else
      {
        status = JsonResponse.STATUS_FAILED;
        AdminPurlAvatarObjectView purlVideoView = new AdminPurlAvatarObjectView();
        purlVideoView.setFail( videoSizeValidMessage );
        purlVideoView.setStatus( status );
        writeUploadJsonToResponse( purlVideoView, response );
      }

    }
    catch( Throwable e )
    {
      status = JsonResponse.STATUS_FAILED;
      AdminPurlAvatarObjectView purlVideoView = new AdminPurlAvatarObjectView();
      purlVideoView.setStatus( status );
      purlVideoView.setId( globalUniqueId );
      purlVideoView.setFail( videoSizeValidMessage );

      logger.error( "Error during recognition process video:" + e );

    }

    return null;

  }

  public String getHtmlImageString( String url )
  {
    StringBuilder htmlImageString = new StringBuilder();
    htmlImageString.append( "<p><img src=\"" + url + "\" alt=\"Photo\" class=\"thumb\"/></p>" );

    return htmlImageString.toString();
  }

  public String getHtmlVideoString( String url )
  {
    StringBuilder htmlVideoString = new StringBuilder();
    String globalUniqueId = String.valueOf( new Date().getTime() );
    htmlVideoString.append( "<video id='example_video_'" + globalUniqueId + " class='video-js vjs-default-skin' controls width='250' height='186' preload='auto'>" );
    htmlVideoString.append( "<source type='video/mp4' src='" + url + "'>" );
    htmlVideoString.append( "</video><script>var myPlayer = _V_('example_video_'" + globalUniqueId + ");</script>" );

    return htmlVideoString.toString();
  }

  private boolean isNewBadge( QuizPromotion promotion, PromotionBasicsForm promotionBasicsForm )
  {
    List<PromotionBasicsBadgeFormBean> promotionBasicsBadgeFormBeanList = promotionBasicsForm.getPromotionBasicsBadgeFormBeanList();
    if ( promotion != null && promotion.getId() != null && !promotion.getId().equals( 0L ) )
    {
      if ( getPromotionService().getBadgePromotionCountForPromoId( promotion.getId() ) > 0 )
      {
        return false;
      }
    }
    for ( PromotionBasicsBadgeFormBean displayBean : promotionBasicsBadgeFormBeanList )
    {
      if ( displayBean.isSelected() )
      {
        return true;
      }
    }

    return false;
  }

  /**
   * We need to set the content type as text/html for upload.
   * Default is text/html
   */
  private void writeUploadJsonToResponse( Object bean, HttpServletResponse response ) throws IOException
  {
    PrintWriter out = response.getWriter();
    out.print( toJson( bean ) );
    out.flush();
    out.close();
  }

  public ActionForward processPDF( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    logger.info( "enters the processPDF method" );
    String pdfSizeValidMessage = CmsResourceBundle.getCmsBundle().getString( "quiz.learningForm.VALID_PDF_SIZE" );
    String globalUniqueId = "";
    Long formId = 0L;
    try
    {
      PromotionBasicsForm promotionBasicsForm = (PromotionBasicsForm)form;
      String promotionName = promotionBasicsForm.getPromotionName() != null ? promotionBasicsForm.getPromotionName() : "";
      String filename = promotionBasicsForm.getFileAssetPdf().getFileName();
      int filesize = promotionBasicsForm.getFileAssetPdf().getFileSize();
      byte[] imageInByte = promotionBasicsForm.getFileAssetPdf().getFileData();
      String fileExtension = filename.substring( filename.lastIndexOf( "." ) + 1, filename.length() );

      // Not getting the parameter through AJAX file upload, so get it from the session
      globalUniqueId = String.valueOf( new Date().getTime() );
      logger.info( "Data Received For Processing... File Name: " + filename + " File Size: " + filesize + " File Extension: " + fileExtension + " Global Unique ID: " + globalUniqueId );

      SSIFileUpload data = new SSIFileUpload();
      data.setId( formId );
      data.setData( imageInByte );
      data.setType( fileExtension );
      data.setName( filename );
      data.setSize( filesize );

      logger.info( "Test for Bug fix 60549 - In Admin setup page not able to upload contest Guide with size more than 300 KB" );
      data = getSSIPromotionService().uploadPdfForSSIGuide( data );
      if ( !AwsUtils.isAws() )
      {
        getSSIPromotionService().moveFileToWebdav( data.getFull() );
      }

      String siteUrlPrefix = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
      if ( !Environment.isCtech() )
      {
        siteUrlPrefix = getSystemVariableService().getPropertyByName( SystemVariableService.SITE_URL_PREFIX + "." + Environment.ENV_QA ).getStringVal();
      }

      String imageUrlPath = siteUrlPrefix + "-cm/cm3dam" + '/' + data.getFull();
      request.getSession().setAttribute( "ssiPdfFullPath", data.getFull() );

      AdminSSIContestGuideObjectView ssiContestGuideObject = new AdminSSIContestGuideObjectView();
      ssiContestGuideObject.setFull( data.getFull() );
      ssiContestGuideObject.setFilename( data.getName() );
      ssiContestGuideObject.setPromotionName( promotionName );
      ssiContestGuideObject.setId( globalUniqueId );
      ssiContestGuideObject.setImageurl( imageUrlPath );
      ssiContestGuideObject.setMedia( fileExtension );
      ssiContestGuideObject.setStatus( JsonResponse.STATUS_SUCCESS );
      ssiContestGuideObject.setThumb( data.getThumb() );
      writeAsJsonToResponse( ssiContestGuideObject, response );
    }
    catch( ServiceErrorException see )
    {
      logger.error( "Error during SSI process pdf:" + see );
      AdminSSIContestGuideObjectView ssiContestGuideObject = new AdminSSIContestGuideObjectView();
      ssiContestGuideObject.setStatus( JsonResponse.STATUS_FAILED );
      ssiContestGuideObject.setId( globalUniqueId );
      ssiContestGuideObject.setFail( pdfSizeValidMessage );
      writeAsJsonToResponse( ssiContestGuideObject, response );
    }
    return null;
  }

  private String getFileExtension( String filename )
  {
    return FilenameUtils.getExtension( filename );
  }

  private SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }

  private PurlService getPurlService()
  {
    return (PurlService)getService( PurlService.BEAN_NAME );
  }

  private SSIPromotionService getSSIPromotionService()
  {
    return (SSIPromotionService)getService( SSIPromotionService.BEAN_NAME );
  }

  private CMAssetService getCMAssetService()
  {
    return (CMAssetService)getService( CMAssetService.BEAN_NAME );
  }

  private ProfileAvatarService getProfileAvatarService()
  {
    return (ProfileAvatarService)getService( ProfileAvatarService.BEAN_NAME );
  }

}
