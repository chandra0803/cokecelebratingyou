
package com.biperf.core.ui.nomination;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.ui.recognition.BaseRecognitionAction;
import com.biperf.core.value.nomination.NominationApproverValueBean;

public class ViewApproversListAction extends BaseRecognitionAction
{

  public ActionForward viewApprovers( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {

    String optionId = request.getParameter( "optionId" );

    PromotionService service = (PromotionService)getService( PromotionService.BEAN_NAME );
    service.getCustomApproverList( Integer.valueOf( optionId ) );

    return mapping.findForward( "viewTemplate" );
  }

  public ActionForward getApproversList( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {

    try
    {

      String optionId = request.getParameter( "optionId" );

      PromotionService promotionService = (PromotionService)getService( PromotionService.BEAN_NAME );
      List<NominationApproverValueBean> customApproverList = promotionService.getCustomApproverList( Integer.valueOf( optionId ) );
      writeAsJsonToResponse( customApproverList, response, ContentType.JSON );
    }
    catch( Exception e )
    {
      writeAppErrorAsJsonResponse( response, e );
    }

    return null;
  }

}
