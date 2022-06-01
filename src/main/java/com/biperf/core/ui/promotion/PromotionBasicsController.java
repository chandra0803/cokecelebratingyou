/**
 *
 */

package com.biperf.core.ui.promotion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.dao.promotion.hibernate.PromotionQueryConstraint;
import com.biperf.core.domain.enums.ChallengePointAwardType;
import com.biperf.core.domain.enums.ClaimFormModuleType;
import com.biperf.core.domain.enums.MerchGiftCodeType;
import com.biperf.core.domain.enums.ProgressLoadType;
import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.enums.PromotionCertificate;
import com.biperf.core.domain.enums.PromotionStatusType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.enums.PurlPromotionMediaType;
import com.biperf.core.domain.enums.PurlPromotionMediaValue;
import com.biperf.core.domain.enums.SSIContestType;
import com.biperf.core.domain.enums.TeamUnavailableResolverType;
import com.biperf.core.domain.enums.ThrowdownPromotionType;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.service.claim.ClaimFormDefinitionService;
import com.biperf.core.service.participant.UserCharacteristicService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.quiz.QuizService;
import com.biperf.core.service.survey.SurveyService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.NewServiceAnniversaryUtil;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.value.FormattedValueBean;
import com.biperf.util.StringUtils;

/**
 * PromotionBasicsController.
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
 * <td>Jun 27, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 *
 *
 */
public class PromotionBasicsController extends BaseController
{

  /**
   * Overridden from
   *
   * @see org.apache.struts.tiles.Controller#execute(org.apache.struts.tiles.ComponentContext,
   *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
   *      javax.servlet.ServletContext)
   * @param tileContext
   * @param request
   * @param response
   * @param servletContext
   * @throws Exception
   */
  @SuppressWarnings( "unchecked" )
  @Override
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    PromotionBasicsForm basicsForm = (PromotionBasicsForm)request.getAttribute( "promotionBasicsForm" );
    Date todaysDate = new Date();
    boolean isPlateauPlatformOnly = getSystemVariableService().getPropertyByName( SystemVariableService.PLATEAU_PLATFORM_ONLY ).getBooleanVal();

    request.setAttribute( "isPlateauPlatformOnly", isPlateauPlatformOnly );
    request.setAttribute( "hasParent", Boolean.valueOf( basicsForm.isHasParent() ) );
    request.setAttribute( "promotionStatus", basicsForm.getPromotionStatus() );
    request.setAttribute( "pageNumber", "1" );

    request.setAttribute( "isFirstPage", Boolean.TRUE );

    if ( basicsForm.isFileLoadEntry() )
    {
      request.setAttribute( "isFileLoad", Boolean.TRUE );
    }

    request.setAttribute( "isPageEditable", Boolean.TRUE );

    if ( basicsForm.getPromotionTypeCode().equals( PromotionType.GOALQUEST ) || basicsForm.getPromotionTypeCode().equals( PromotionType.CHALLENGE_POINT ) )
    {
      List awardTypeList = PromotionAwardsType.getGoalQuestList( getSystemVariableService().getPropertyByName( SystemVariableService.SALES_MAKER ).getBooleanVal() );
      // List challengePointAwardTypeList = ChallengePointAwardType.getList();
      List<PromotionAwardsType> filteredAwardTypeList = new ArrayList<PromotionAwardsType>();
      if ( isPlateauPlatformOnly )
      {
        for ( Iterator iter = awardTypeList.iterator(); iter.hasNext(); )
        {
          PromotionAwardsType promotionAwardsType = (PromotionAwardsType)iter.next();
          if ( promotionAwardsType.getCode().equals( PromotionAwardsType.MERCHANDISE ) )
          {
            filteredAwardTypeList.add( promotionAwardsType );
          }
        }
      }
      else
      {
        filteredAwardTypeList.addAll( awardTypeList );
      }

      List challengePointAwardTypeList = PromotionAwardsType.getChallengePointList( getSystemVariableService().getPropertyByName( SystemVariableService.SALES_MAKER ).getBooleanVal() );
      List<ChallengePointAwardType> filteredChallengePointAwardTypeList = new ArrayList<ChallengePointAwardType>();

      if ( basicsForm.getPromotionTypeCode().equals( PromotionType.CHALLENGE_POINT ) )
      {

        if ( isPlateauPlatformOnly )
        {
          for ( Iterator iter = challengePointAwardTypeList.iterator(); iter.hasNext(); )
          {
            ChallengePointAwardType promotionAwardsType = (ChallengePointAwardType)iter.next();
            if ( promotionAwardsType.getCode().equals( PromotionAwardsType.MERCHANDISE ) || promotionAwardsType.getCode().equals( ChallengePointAwardType.MERCHTRAVEL ) )
            {
              filteredChallengePointAwardTypeList.add( promotionAwardsType );
            }
          }
        }
        else
        {
          filteredChallengePointAwardTypeList.addAll( challengePointAwardTypeList );
        }

      }
      request.setAttribute( "awardTypeList", filteredAwardTypeList );
      request.setAttribute( "challengePointAwardTypeList", filteredChallengePointAwardTypeList );
      request.setAttribute( "merchGiftCodeTypeList", MerchGiftCodeType.getList() );

      if ( !StringUtils.isEmpty( basicsForm.getGoalSelectionStartDate() ) )
      {
        Date goalSelectionStart = DateUtils.toDate( basicsForm.getGoalSelectionStartDate() );
        if ( todaysDate.compareTo( goalSelectionStart ) <= 0 )
        {
          request.setAttribute( "goalSelectionDateEditable", new Boolean( true ) );
        }
        else
        {
          request.setAttribute( "goalSelectionDateEditable", new Boolean( false ) );
        }
      }
      if ( !StringUtils.isEmpty( basicsForm.getFinalProcessDateString() ) )
      {
        Date finalProcessDate = DateUtils.toDate( basicsForm.getFinalProcessDateString() );
        if ( todaysDate.compareTo( finalProcessDate ) <= 0 )
        {
          request.setAttribute( "goalProcessDateEditable", new Boolean( true ) );
        }
        else
        {
          request.setAttribute( "goalProcessDateEditable", new Boolean( false ) );
        }
      }
    }
    else if ( basicsForm.getPromotionTypeCode().equals( PromotionType.RECOGNITION ) )
    {
      List recognitionList = PromotionAwardsType.getRecognitionList();
      List<PromotionAwardsType> filteredRecognitionList = new ArrayList<PromotionAwardsType>();

      if ( NewServiceAnniversaryUtil.isNewServiceAnniversaryEnabled() && getSystemVariableService().getPropertyByName( SystemVariableService.PURL_AVAILABLE ).getBooleanVal() )
      {
        request.setAttribute( "isPurlAvailable", Boolean.FALSE );
      }

      // New Service Anniversary Celebration Module Enabling.
      if ( !NewServiceAnniversaryUtil.isNewServiceAnniversaryEnabled() && getSystemVariableService().getPropertyByName( SystemVariableService.PURL_AVAILABLE ).getBooleanVal() )
      {
        request.setAttribute( "isPurlAvailable", Boolean.TRUE );
        request.setAttribute( "purlPromoMediaList", PurlPromotionMediaType.getList() );
        request.setAttribute( "purlPromoMediaValueList", PurlPromotionMediaValue.getList() );
      }

      if ( isPlateauPlatformOnly )
      {
        for ( Iterator iter = recognitionList.iterator(); iter.hasNext(); )
        {
          PromotionAwardsType promotionAwardsType = (PromotionAwardsType)iter.next();
          if ( promotionAwardsType.getCode().equals( PromotionAwardsType.MERCHANDISE ) )
          {
            filteredRecognitionList.add( promotionAwardsType );
          }
        }
      }
      else
      {
        filteredRecognitionList.addAll( recognitionList );
      }

      request.setAttribute( "awardTypeList", filteredRecognitionList );

    }
    else if ( basicsForm.getPromotionTypeCode().equals( PromotionType.WELLNESS ) )
    {
      request.setAttribute( "awardTypeList", PromotionAwardsType.getWellnessList() );
    }
    else if ( basicsForm.getPromotionTypeCode().equals( PromotionType.NOMINATION ) )
    {
      request.setAttribute( "awardTypeList", PromotionAwardsType.getNominationList() );
    }
    else
    {
      request.setAttribute( "awardTypeList", PromotionAwardsType.getOtherList() );
    }

    // Get only the appropriate claim forms (i.e. Product Claim, Recognition, etc.)
    List activityFormList = null;
    // Adding mgrAwardPromotion
    PromotionQueryConstraint queryConstraint = new PromotionQueryConstraint();
    queryConstraint.setPromotionStatusTypesIncluded( new PromotionStatusType[] { PromotionStatusType.lookup( PromotionStatusType.LIVE ), PromotionStatusType.lookup( PromotionStatusType.EXPIRED ) } );
    queryConstraint.setOrderByPromotionNameCaseInsensitive( true );

    if ( basicsForm.getPromotionTypeCode().equals( PromotionType.PRODUCT_CLAIM ) )
    {
      activityFormList = getClaimFormService().getAllClaimFormsNotUnderConstructionByModuleType( ClaimFormModuleType.MODULE_PRODUCT_CLAIMS );
    }

    if ( basicsForm.getPromotionTypeCode().equals( PromotionType.RECOGNITION ) )
    {
      activityFormList = getClaimFormService().getAllClaimFormsNotUnderConstructionByModuleType( ClaimFormModuleType.MODULE_RECOGNITION );

      // Add module specific constraints
      queryConstraint.setPromotionTypesIncluded( new PromotionType[] { PromotionType.lookup( PromotionType.RECOGNITION ) } );

      request.setAttribute( "mgrRecogPromoList", filterThisPromotionOut( getPromotionService().getAllLiveWithBudget(), basicsForm.getPromotionId() ) );
    }

    if ( basicsForm.getPromotionTypeCode().equals( PromotionType.NOMINATION ) )
    {
      activityFormList = getClaimFormService().getAllClaimFormsNotUnderConstructionByModuleType( ClaimFormModuleType.MODULE_NOMINATION );
    }

    if ( basicsForm.getPromotionTypeCode().equals( PromotionType.QUIZ ) )
    {
      activityFormList = getQuizService().getAllCompletedAndAssignedQuizzes();
    }

    if ( basicsForm.getPromotionTypeCode().equals( PromotionType.GOALQUEST ) )
    {
      activityFormList = getClaimFormService().getAllClaimFormsNotUnderConstructionByModuleType( ClaimFormModuleType.MODULE_GOALQUEST );
      request.setAttribute( "progressLoadTypeList", ProgressLoadType.getList() );
    }

    if ( basicsForm.getPromotionTypeCode().equals( PromotionType.SURVEY ) )
    {
      activityFormList = getSurveyService().getAllSurveyFormsNotUnderConstructionByModuleType( PromotionType.SURVEY );
    }

    request.setAttribute( "activityFormList", activityFormList );
    request.setAttribute( "certificateList", PromotionCertificate.getList( basicsForm.getPromotionTypeCode() ) );
    request.setAttribute( "promotionTypeCode", basicsForm.getPromotionTypeCode() );

    if ( basicsForm.getPromotionTypeCode().equals( PromotionType.DIY_QUIZ ) )
    {
      request.setAttribute( "badgeList", getPromotionService().buildBadgeLibraryList() );
    }

    if ( basicsForm.getPromotionTypeCode().equals( PromotionType.THROWDOWN ) )
    {
      request.setAttribute( "throwdownPromotionTypeList", ThrowdownPromotionType.getList() );
      request.setAttribute( "teamUnavailableResolverTypeList", TeamUnavailableResolverType.getList() );
    }

    if ( basicsForm.getPromotionTypeCode().equals( PromotionType.ENGAGEMENT ) )
    {
      List<FormattedValueBean> eligiblePromotionList = getEngagementEligiblePromotions();
      if ( StringUtil.isNullOrEmpty( basicsForm.getPromotionId() ) )
      {
        List<Long> eligiblePromotionsNotSelected = new ArrayList<Long>();
        for ( FormattedValueBean valueBean : eligiblePromotionList )
        {
          eligiblePromotionsNotSelected.add( valueBean.getId() );
        }
        basicsForm.setEngagementNotSelectedPromos( eligiblePromotionsNotSelected.toArray( new Long[0] ) );
      }

      // ******** Remove the purl & celebration promotions. **********
      if ( NewServiceAnniversaryUtil.isNewServiceAnniversaryEnabled() )
      {
        List<FormattedValueBean> filteredEligiblePromotionList = new ArrayList<>();
        List<String> excludedNames = getPromotionService().getNonPurlAndCelebPromotionsName();

        for ( FormattedValueBean name : eligiblePromotionList )
        {
          for ( String curName : excludedNames )
          {
            if ( name.getValue().equals( curName ) )
            {
              filteredEligiblePromotionList.add( name );
            }
          }
        }
        request.setAttribute( "engagementEligiblePromoList", filteredEligiblePromotionList );

      }
      else
      {
        request.setAttribute( "engagementEligiblePromoList", eligiblePromotionList );
      }

    }

    if ( basicsForm.getPromotionTypeCode().equals( PromotionType.SELF_SERV_INCENTIVES ) )
    {
      request.setAttribute( "ssiContestTypes", SSIContestType.getList() );

      request.setAttribute( "userCharList", getUserCharacteristicService().getAllCharacteristics() );
    }

    PropertySetItem processingMode = getSystemVariableService().getPropertyByName( SystemVariableService.CLAIM_PROCESSING_ALLOW_BATCH );

    request.setAttribute( "allowBatchProcessing", Boolean.valueOf( processingMode.getBooleanVal() ) );

  }

  private List<FormattedValueBean> getEngagementEligiblePromotions()
  {
    List<FormattedValueBean> engagementEligiblePromoList = getPromotionService().getEngagementEligiblePromotionList();
    Collections.sort( engagementEligiblePromoList, new Comparator()
    {
      @Override
      public int compare( Object o1, Object o2 )
      {
        FormattedValueBean r1 = (FormattedValueBean)o1;
        FormattedValueBean r2 = (FormattedValueBean)o2;

        return r1.getValue().compareTo( r2.getValue() );
      }
    } );
    return engagementEligiblePromoList;
  }

  private List filterThisPromotionOut( List list, String promotionId )
  {
    List newList = new ArrayList();

    for ( Iterator iter = list.iterator(); iter.hasNext(); )
    {
      Promotion promotion = (Promotion)iter.next();

      if ( promotion.getPromotionType().getCode().equals( PromotionType.RECOGNITION ) )
      {

        if ( promotion.getPromotionStatus().getCode().equals( PromotionStatusType.COMPLETE ) || promotion.getPromotionStatus().getCode().equals( PromotionStatusType.LIVE ) )
        {

          if ( !String.valueOf( promotion.getId() ).equals( promotionId == null ? "" : promotionId ) )
          {
            newList.add( promotion );
          }
        }
      }
    }

    return newList;
  }

  private ClaimFormDefinitionService getClaimFormService()
  {
    return (ClaimFormDefinitionService)getService( ClaimFormDefinitionService.BEAN_NAME );
  }

  /**
   * Get the quizService from the beanFactory.
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

  /**
   * Get the systemVariableService from the beanFactory.
   *
   * @return SystemVariableService
   */
  private SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }

  /**
   * Returns a reference to Promotion Service
   *
   * @return reference to Promotion Service
   */
  private PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }

  /**
   * Returns a reference to Promotion Service
   *
   * @return reference to Promotion Service
   */
  private UserCharacteristicService getUserCharacteristicService()
  {
    return (UserCharacteristicService)getService( UserCharacteristicService.BEAN_NAME );
  }
}
