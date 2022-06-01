
package com.biperf.core.ui.promotion;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.characteristic.UserCharacteristicType;
import com.biperf.core.domain.enums.BillCodeNominationType;
import com.biperf.core.domain.enums.BillCodeRecognitionType;
import com.biperf.core.domain.enums.BillCodeSSIType;
import com.biperf.core.domain.enums.PromotionStatusType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.service.participant.UserCharacteristicService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.InvalidClientStateException;

public class PromotionBillCodesController extends BaseController
{
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    PromotionBillCodesForm promotionBillCodesForm = (PromotionBillCodesForm)request.getAttribute( "promotionBillCodesForm" );

    if ( ObjectUtils.equals( promotionBillCodesForm.getPromotionStatus(), PromotionStatusType.EXPIRED ) )
    {
      request.setAttribute( "isPageEditable", Boolean.FALSE );
    }
    else
    {
      request.setAttribute( "isPageEditable", Boolean.TRUE );
    }
    Promotion promotion = getPromotionService().getPromotionById( new Long( promotionBillCodesForm.getPromotionId() ) );

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

    if ( promotionBillCodesForm.getPromotionTypeCode().equals( PromotionType.RECOGNITION ) )
    {
      request.setAttribute( "billCodesList", BillCodeRecognitionType.getList() );
    }
    else if ( promotionBillCodesForm.getPromotionTypeCode().equals( PromotionType.NOMINATION ) )
    {
      request.setAttribute( "billCodesList", BillCodeNominationType.getList() );
    }
    else if ( promotionBillCodesForm.getPromotionTypeCode().equals( PromotionType.SELF_SERV_INCENTIVES ) )
    {
      request.setAttribute( "billCodesList", BillCodeSSIType.getList() );
    }
    // WIP# 25127 Start
    else if ( promotionBillCodesForm.getPromotionTypeCode().equals( PromotionType.GOALQUEST ) )
    {
      request.setAttribute( "billCodesList", BillCodeNominationType.getList() );
    }
    // WIP# 25127 End
    request.setAttribute( "userCharList", getUserCharacteristicService().getAllCharacteristics() );
    request.setAttribute( "promotionStatus", promotionBillCodesForm.getPromotionStatus() );

    if ( promotionBillCodesForm.getPromotionTypeCode().equals( PromotionType.RECOGNITION ) || promotionBillCodesForm.getPromotionTypeCode().equals( PromotionType.NOMINATION ) )
    {
      if ( promotion.isRecognitionPromotion() )
      {
        if ( ( (RecognitionPromotion)promotion ).isIncludePurl() )
        {
          if ( ( (RecognitionPromotion)promotion ).isIncludeCelebrations() )
          {
            request.setAttribute( "pageNumber", "83" );
          }
          else
          {
            request.setAttribute( "pageNumber", "66" );
          }
          request.setAttribute( "isPurlIncluded", Boolean.TRUE );
        }
        else
        {
          if ( ( (RecognitionPromotion)promotion ).isIncludeCelebrations() )
          {
            request.setAttribute( "pageNumber", "78" );
          }
          else
          {
            request.setAttribute( "pageNumber", "12" );
          }
        }
        if ( ( (RecognitionPromotion)promotion ).isIncludeCelebrations() )
        {
          request.setAttribute( "isCelebrationsIncluded", Boolean.TRUE );
        }
      }
      else
      {
        request.setAttribute( "pageNumber", "12" );
      }
    }
    else if ( promotionBillCodesForm.getPromotionTypeCode().equals( PromotionType.QUIZ ) )
    {
      request.setAttribute( "pageNumber", "7" );
    }
    else if ( promotionBillCodesForm.getPromotionTypeCode().equals( PromotionType.PRODUCT_CLAIM ) )
    {
      request.setAttribute( "pageNumber", "9" );
    }
    else if ( promotionBillCodesForm.getPromotionTypeCode().equals( PromotionType.GOALQUEST ) || promotionBillCodesForm.getPromotionTypeCode().equals( PromotionType.CHALLENGE_POINT ) )
    {
      if ( ( (GoalQuestPromotion)promotion ).isPartnersEnabled() )
      {
        request.setAttribute( "pageNumber", "8" );
      }
      else
      {
        request.setAttribute( "pageNumber", "7" );
      }
    }
    else if ( promotionBillCodesForm.getPromotionTypeCode().equals( PromotionType.SELF_SERV_INCENTIVES ) )
    {
      request.setAttribute( "pageNumber", "7" );
      request.setAttribute( "isLastPage", Boolean.TRUE );
    }
  }

  private PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }

  private UserCharacteristicService getUserCharacteristicService()
  {
    return (UserCharacteristicService)getService( UserCharacteristicService.BEAN_NAME );
  }

  private List getCharacteristicsList( String trackBillCode )
  {
    List userCharList = getUserCharacteristicService().getAllCharacteristics();
    if ( trackBillCode != null )
    {
      Iterator userCharacteristics = userCharList.iterator();
      while ( userCharacteristics.hasNext() )
      {
        UserCharacteristicType userCharacteristic = (UserCharacteristicType)userCharacteristics.next();
        userCharacteristic.setCharacteristicName( trackBillCode + " " + userCharacteristic.getCharacteristicName() );
        userCharacteristic.setDescription( trackBillCode.toLowerCase() + "_" + userCharacteristic.getId() );
      }
    }
    return userCharList;
  }

}
