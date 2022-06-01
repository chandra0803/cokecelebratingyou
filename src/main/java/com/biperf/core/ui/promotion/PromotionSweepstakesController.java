/**
 * 
 */

package com.biperf.core.ui.promotion;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts.tiles.ComponentContext;
import org.apache.struts.util.LabelValueBean;

import com.biperf.core.domain.enums.PromotionStatusType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.enums.SweepstakesClaimEligibilityType;
import com.biperf.core.domain.enums.SweepstakesMultipleAwardsType;
import com.biperf.core.domain.enums.SweepstakesWinnerEligibilityType;
import com.biperf.core.domain.enums.SweepstakesWinnersType;
import com.biperf.core.domain.promotion.ProductClaimPromotion;
import com.biperf.core.domain.promotion.PromoMerchCountry;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.participant.UserCharacteristicService;
import com.biperf.core.service.promotion.PromoMerchCountryService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.promotion.PromotionSweepstakeService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.InvalidClientStateException;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * PromotionSweepstakesController.
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
 * <td>jenniget</td>
 * <td>Oct 6, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionSweepstakesController extends BaseController
{
  private static String BILLCODE_NOMINATION_TYPE_NOMINATOR = "Nominator";
  private static String BILLCODE_NOMINATION_TYPE_NOMINEE = "Nominee";
  private static String BILLCODE_RECOGNITION_TYPE_GIVER = "Giver";
  private static String BILLCODE_RECOGNITION_TYPE_RECEIVER = "Receiver";

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
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    PromotionSweepstakesForm sweepstakeForm = (PromotionSweepstakesForm)request.getAttribute( "promotionSweepstakesForm" );

    if ( ObjectUtils.equals( sweepstakeForm.getPromotionStatus(), PromotionStatusType.EXPIRED ) )
    {
      request.setAttribute( "isPageEditable", Boolean.FALSE );
    }
    else
    {
      request.setAttribute( "isPageEditable", Boolean.TRUE );
    }

    setMultipleAwardLists( request );

    request.setAttribute( "pageNumber", "5" );

    if ( sweepstakeForm.getPromotionTypeCode().equals( PromotionType.RECOGNITION ) )
    {
      request.setAttribute( "availableEligibleWinners", SweepstakesWinnerEligibilityType.getRecognitionEligibleWinnersList() );

      // retrieve Level information if necessary
      if ( sweepstakeForm.isAwardLevelPromotion() )
      {
        Long promotionId = new Long( sweepstakeForm.getPromotionId() );
        request.setAttribute( "availableMerchLevels", getAvailableAwardLevels( promotionId ) );
      }
      if ( sweepstakeForm.isActiveNotEditable() )
      {
        request.setAttribute( "isActiveNotEditable", Boolean.TRUE );
      }
      else
      {
        request.setAttribute( "isActiveNotEditable", Boolean.FALSE );
      }
      RecognitionPromotion promo = (RecognitionPromotion)getPromotionService().getPromotionById( new Long( sweepstakeForm.getPromotionId() ) );
      if ( promo.isIncludePurl() )
      {
        request.setAttribute( "pageNumber", 60 );
        // request.setAttribute( "isBackToAwards", Boolean.TRUE );
        request.setAttribute( "isBackToBehavior", Boolean.FALSE );
        request.setAttribute( "isPurlIncluded", Boolean.TRUE );
      }
      if ( !promo.isIncludeCelebrations() )
      {
        request.setAttribute( "isBackToAwards", Boolean.TRUE );
      }
      if ( promo.isIncludeCelebrations() )
      {
        request.setAttribute( "pageNumber", 71 );
        request.setAttribute( "isCelebrationsIncluded", Boolean.TRUE );
      }
    }
    else if ( sweepstakeForm.getPromotionTypeCode().equals( PromotionType.NOMINATION ) )
    {
      request.setAttribute( "availableEligibleWinners", SweepstakesWinnerEligibilityType.getNominationEligibleWinnersList() );
      request.setAttribute( "pageNumber", "8" );
    }
    else if ( sweepstakeForm.getPromotionTypeCode().equals( PromotionType.PRODUCT_CLAIM ) )
    {
      ProductClaimPromotion promotion = (ProductClaimPromotion)getPromotionService().getPromotionById( new Long( sweepstakeForm.getPromotionId() ) );

      if ( promotion.hasParent() && promotion.getParentPromotion().isSweepstakesActive() )
      {
        request.setAttribute( "hasParent", Boolean.TRUE );
      }

      if ( promotion.isTeamUsed() )
      {
        request.setAttribute( "availableEligibleWinners", SweepstakesWinnerEligibilityType.getClaimEligibleWinnersList() );
      }
      else
      {
        request.setAttribute( "availableEligibleWinners", SweepstakesWinnerEligibilityType.getClaimEligibleWinnersWithoutTeamList() );
      }

      request.setAttribute( "availableEligibleClaims", SweepstakesClaimEligibilityType.getList() );
    }
    else if ( sweepstakeForm.getPromotionTypeCode().equals( PromotionType.QUIZ ) )
    {
      request.setAttribute( "pageNumber", "4" );
    }
    else if ( sweepstakeForm.getPromotionTypeCode().equals( PromotionType.SURVEY ) )
    {
      request.setAttribute( "availableEligibleWinners", SweepstakesWinnerEligibilityType.getSurveyEligibleWinnersList() );
      request.setAttribute( "pageNumber", "3" );
    }

    else if ( sweepstakeForm.getPromotionTypeCode().equals( PromotionType.BADGE ) )
    {
      request.setAttribute( "availableEligibleWinners", SweepstakesWinnerEligibilityType.getBadgeEligibleWinnersList() );
    }

    String domainId = null;
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
        try
        {
          domainId = (String)clientStateMap.get( "domainId" );
        }
        catch( ClassCastException cce )
        {
          Long id = (Long)clientStateMap.get( "domainId" );
          domainId = id.toString();
        }
      }
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }

    Long domainIdLong = null;
    if ( StringUtils.isNotEmpty( domainId ) )
    {
      domainIdLong = new Long( domainId );
    }
    request.setAttribute( "userCharList", getUserCharacteristicService().getAllCharacteristics() );
    request.setAttribute( "availableWinnerTypes", SweepstakesWinnersType.getList() );
    request.setAttribute( "hasParent", Boolean.valueOf( false ) );
    request.setAttribute( "promotionStatus", sweepstakeForm.getPromotionStatus() );
    request.setAttribute( "promotionTypeCode", sweepstakeForm.getPromotionTypeCode() );
  }

  private void setMultipleAwardLists( HttpServletRequest request )
  {
    List fullMultipleAwards = SweepstakesMultipleAwardsType.getList();
    request.setAttribute( "availableMultipleAwards", fullMultipleAwards );

    // remove the per time period option from this lists
    List trimmedMultipleAwards = new ArrayList( fullMultipleAwards.size() - 1 );
    for ( Iterator iter = fullMultipleAwards.iterator(); iter.hasNext(); )
    {
      SweepstakesMultipleAwardsType item = (SweepstakesMultipleAwardsType)iter.next();
      if ( !item.getCode().equals( SweepstakesMultipleAwardsType.PERIOD_CODE ) )
      {
        trimmedMultipleAwards.add( item );
      }
    }
    request.setAttribute( "availableMultipleAwardsTrimmed", trimmedMultipleAwards );
  }

  private List getAvailableAwardLevels( Long promotionId ) throws ServiceErrorException
  {
    List promoMerchCountryList = getPromoMerchCountryService().getPromoMerchCountriesByPromotionId( promotionId );

    List programIds = new ArrayList();

    for ( int i = 0; i < promoMerchCountryList.size(); i++ )
    {
      programIds.add( ( (PromoMerchCountry)promoMerchCountryList.get( i ) ).getProgramId() );
    }

    int numberOfLevels = getPromotionService().getMaximumLevelForPrograms( programIds );

    List availableLevelSelections = new ArrayList();

    String levelLabel = CmsResourceBundle.getCmsBundle().getString( "recognition.merchandise.LEVEL_NAME" );

    for ( int i = 1; i <= numberOfLevels; i++ )
    {
      availableLevelSelections.add( new LabelValueBean( levelLabel + " " + i, i + "" ) );
    }

    return availableLevelSelections;
  }

  /**
   * Does a Bean lookup for the PromotionService
   * 
   * @return PromotionService
   */
  private static PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }

  /**
   * Does a Bean lookup for the PromotionService
   * 
   * @return PromotionService
   */
  private static PromoMerchCountryService getPromoMerchCountryService()
  {
    return (PromoMerchCountryService)getService( PromoMerchCountryService.BEAN_NAME );
  }

  private static PromotionSweepstakeService getPromotionSweepstakeService()
  {
    return (PromotionSweepstakeService)getService( PromotionSweepstakeService.BEAN_NAME );
  }

  private UserCharacteristicService getUserCharacteristicService()
  {
    return (UserCharacteristicService)getService( UserCharacteristicService.BEAN_NAME );
  }

  private List getCharacteristicsList()
  {
    List userCharList = getUserCharacteristicService().getAllCharacteristics();

    return userCharList;
  }
}
