/**
 * 
 */

package com.biperf.core.ui.promotion;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.service.cms.CMAssetService;
import com.biperf.core.ui.BaseController;

public class PromotionPublicRecognitionGiversController extends BaseController
{

  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {

    PromotionPublicRecognitionGiversForm promotionPublicRecognitionGiversForm = (PromotionPublicRecognitionGiversForm)request.getAttribute( "promotionPublicRecognitionGiversForm" );

    if ( promotionPublicRecognitionGiversForm.getPromotionTypeCode().equals( PromotionType.NOMINATION ) )
    {
      request.setAttribute( "pageNumber", "72" );
    }
    else
    {
      request.setAttribute( "pageNumber", "42" );
    }

    request.setAttribute( "isPageEditable", Boolean.TRUE );
  }

  private CMAssetService getCMAssetService()
  {
    return (CMAssetService)getService( CMAssetService.BEAN_NAME );
  }
}
