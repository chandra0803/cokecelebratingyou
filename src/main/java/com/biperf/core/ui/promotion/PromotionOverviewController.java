/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/promotion/PromotionOverviewController.java,v $
 */

package com.biperf.core.ui.promotion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.enums.PromotionIssuanceType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.service.cms.CMAssetService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.ssi.SSIPromotionService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.NewServiceAnniversaryUtil;

/**
 * .
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
 * <td>sedey</td>
 * <td>Aug 26, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionOverviewController extends BaseController
{

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
  @SuppressWarnings( "unchecked" )
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    PromotionOverviewForm promoOverviewForm = (PromotionOverviewForm)request.getSession().getAttribute( "promotionOverviewForm" );

    // ******** Remove the purl & celebration promotions. **********
    if ( NewServiceAnniversaryUtil.isNewServiceAnniversaryEnabled() )
    {
      List<String> filteredNames = new ArrayList<>();
      List<String> excludedNames = getPromotionService().getNonPurlAndCelebPromotionsName();
      List<String> existingNames = promoOverviewForm.getEngagementEligiblePromotionList();

      for ( String name : existingNames )
      {
        for ( String curName : excludedNames )
        {
          if ( name.equals( curName ) )
          {
            filteredNames.add( name );
          }
        }
      }

      promoOverviewForm.setEngagementEligiblePromotionList( filteredNames );
      request.getSession().setAttribute( "promotionOverviewForm", promoOverviewForm );
    }

    boolean isPlateauPlatformOnly = getSystemVariableService().getPropertyByName( SystemVariableService.PLATEAU_PLATFORM_ONLY ).getBooleanVal();
    request.setAttribute( "isPlateauPlatformOnly", isPlateauPlatformOnly );

    if ( promoOverviewForm != null && promoOverviewForm.getPromotionTypeCode() != null )
    {
      if ( promoOverviewForm.getPromotionTypeCode().equals( PromotionType.RECOGNITION ) )
      {
        RecognitionPromotion promo = (RecognitionPromotion)getPromotionService().getPromotionById( new Long( promoOverviewForm.getPromotionId() ) );
        if ( promo.isIncludePurl() && !NewServiceAnniversaryUtil.isNewServiceAnniversaryEnabled() )
        {
          request.setAttribute( "isPurlIncluded", Boolean.TRUE );
        }
        if ( promo.isIncludeCelebrations() && !NewServiceAnniversaryUtil.isNewServiceAnniversaryEnabled() )
        {
          request.setAttribute( "isCelebrationsIncluded", Boolean.TRUE );
        }
      }

      if ( !promoOverviewForm.getPromotionTypeCode().equals( PromotionType.WELLNESS ) )
      {
        List localeItems = getCMAssetService().getSupportedLocales( true );
        if ( localeItems != null && localeItems.size() > 0 ) // QC#3861
        {
          request.setAttribute( "displayTranslations", Boolean.TRUE );
        }
      }

      if ( promoOverviewForm.getPromotionTypeCode().equals( PromotionType.SELF_SERV_INCENTIVES ) )
      {
        Promotion promo = getPromotionService().getPromotionById( new Long( promoOverviewForm.getPromotionId() ) );
        request.setAttribute( "hideEditLinks", Boolean.valueOf( getSsiPromotionService().getLiveSSIPromotion() != null && promo.getPromotionStatus().isExpired() ) );
      }
    }

    if ( promoOverviewForm != null && promoOverviewForm.getPromotionTypeCode() != null )
    {
      // DIY Quiz promotion uses the same path as the quiz promotion
      if ( PromotionType.DIY_QUIZ.equals( promoOverviewForm.getPromotionTypeCode() ) )
      {
        request.setAttribute( "promotionStrutsModulePath", PromotionWizardManager.getStrutsModulePath( PromotionType.QUIZ ) );
      }
      else
      {
        String modulePath = PromotionWizardManager.getStrutsModulePath( promoOverviewForm.getPromotionTypeCode() );
        request.setAttribute( "promotionStrutsModulePath", modulePath );

        Promotion promo = getPromotionService().getPromotionById( new Long( promoOverviewForm.getPromotionId() ) );

        if ( promo.isUnderConstruction() )
        {
          if ( promoOverviewForm.getProcessingMode() != null && promoOverviewForm.getProcessingMode().equals( PromotionIssuanceType.lookup( PromotionIssuanceType.ONLINE ).getName() )
              && promoOverviewForm.getPromotionTypeCode().equals( PromotionType.RECOGNITION ) )
          {
            @SuppressWarnings( "rawtypes" )
            Map paramMap = new HashMap();
            paramMap.put( "promotionId", promoOverviewForm.getPromotionId() );
            paramMap.put( "promotionTypeCode", promoOverviewForm.getPromotionTypeCode() );
            paramMap.put( "claimFormId", promoOverviewForm.getClaimFormId() );
            paramMap.put( "version", promoOverviewForm.getVersion() );
            String validateUrl = "/" + modulePath + "/promotionAudience.do?method=validateAudienceDisplay";
            request.setAttribute( "validateAudienceViewUrl", ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI( request ), validateUrl, paramMap ) );
          }
        }
      }
    }

    if ( request.getSession().getAttribute( "rpmAudienceUpdated" ) != null )
    {
      request.setAttribute( "rpmAudienceUpdated", request.getSession().getAttribute( "rpmAudienceUpdated" ) );
      request.getSession().removeAttribute( "rpmAudienceUpdated" );
    }

  }

  /**
   * Returns the PromotionService.
   * 
   * @return a reference to the PromotionService.
   */
  private PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }

  private CMAssetService getCMAssetService()
  {
    return (CMAssetService)getService( CMAssetService.BEAN_NAME );
  }

  private SSIPromotionService getSsiPromotionService()
  {
    return (SSIPromotionService)getService( SSIPromotionService.BEAN_NAME );
  }

  private SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }

}
