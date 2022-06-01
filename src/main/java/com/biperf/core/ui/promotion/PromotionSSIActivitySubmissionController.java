
package com.biperf.core.ui.promotion;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.enums.ClaimFormModuleType;
import com.biperf.core.domain.enums.PromotionStatusType;
import com.biperf.core.service.claim.ClaimFormDefinitionService;
import com.biperf.core.ui.BaseController;

/**
 * 
 * PromotionSSIActivitySubmissionController.
 * 
 * @author kandhi
 * @since Oct 30, 2014
 * @version 1.0
 */
public class PromotionSSIActivitySubmissionController extends BaseController
{

  @Override
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    PromotionSSIActivitySubmissionForm promotionSSIActivitySubmissionForm = (PromotionSSIActivitySubmissionForm)request.getAttribute( "promotionSSIActivitySubmissionForm" );
    if ( ObjectUtils.equals( promotionSSIActivitySubmissionForm.getPromotionStatus(), PromotionStatusType.EXPIRED ) )
    {
      request.setAttribute( "isPageEditable", Boolean.FALSE );
    }
    else
    {
      request.setAttribute( "isPageEditable", Boolean.TRUE );
    }
    request.setAttribute( "activityFormList", getClaimFormService().getAllClaimFormsNotUnderConstructionByModuleType( ClaimFormModuleType.MODULE_SSI ) );
    request.setAttribute( "pageNumber", "4" );
  }

  private ClaimFormDefinitionService getClaimFormService()
  {
    return (ClaimFormDefinitionService)getService( ClaimFormDefinitionService.BEAN_NAME );
  }
}
