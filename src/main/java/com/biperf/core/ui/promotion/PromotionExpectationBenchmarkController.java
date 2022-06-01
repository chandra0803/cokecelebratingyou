
package com.biperf.core.ui.promotion;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.enums.KPMMultipleAudienceType;
import com.biperf.core.domain.enums.PromotionStatusType;
import com.biperf.core.ui.BaseController;

public class PromotionExpectationBenchmarkController extends BaseController
{

  @SuppressWarnings( "rawtypes" )
  @Override
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    PromotionExpectationBenchmarkForm promotionExpectationBenchmarkForm = (PromotionExpectationBenchmarkForm)request.getAttribute( "promotionExpectationBenchmarkForm" );
    if ( ObjectUtils.equals( promotionExpectationBenchmarkForm.getPromotionStatus(), PromotionStatusType.EXPIRED ) )
    {
      request.setAttribute( "isPageEditable", Boolean.FALSE );
    }
    else
    {
      request.setAttribute( "isPageEditable", Boolean.TRUE );
    }
    request.setAttribute( "pageNumber", "2" );
    List kpmMultipleAudienceTypeList = KPMMultipleAudienceType.getList();
    request.setAttribute( "kpmMultipleAudienceTypeList", kpmMultipleAudienceTypeList );
  }

}
