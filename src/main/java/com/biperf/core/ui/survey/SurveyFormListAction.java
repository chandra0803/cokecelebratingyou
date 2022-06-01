
package com.biperf.core.ui.survey;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.survey.SurveyService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.ServiceErrorStrutsUtils;
import com.biperf.core.utils.ArrayUtil;

public class SurveyFormListAction extends BaseDispatchAction
{
  /**
   * Delete under construction surveys
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward deleteUnderConstructionSurveys( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    String forwardTo = ActionConstants.SUCCESS_FORWARD;

    SurveyListForm surveyListForm = (SurveyListForm)form;

    if ( surveyListForm.getDeleteUnderConstructionIds() != null && surveyListForm.getDeleteUnderConstructionIds().length > 0 )
    {
      deleteSurveys( surveyListForm.getDeleteUnderConstructionIds(), request );
    }

    // get the actionForward to display the udpate pages.
    return mapping.findForward( forwardTo );
  }

  /**
   * Delete complete surveyes
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward deleteCompleteSurveys( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    String forwardTo = ActionConstants.SUCCESS_FORWARD;

    SurveyListForm surveyListForm = (SurveyListForm)form;

    if ( surveyListForm.getDeleteCompletedIds() != null && surveyListForm.getDeleteCompletedIds().length > 0 )
    {
      deleteSurveys( surveyListForm.getDeleteCompletedIds(), request );
    }

    // get the actionForward to display the udpate pages.
    return mapping.findForward( forwardTo );
  }

  /**
   * delete a list of surveys
   * 
   * @param surveyFormIds
   */
  @SuppressWarnings( "rawtypes" )
  private void deleteSurveys( String[] surveyFormIds, HttpServletRequest request )
  {
    // Convert String[] of surveyFormIds to Long[]
    List surveyFormIdList = ArrayUtil.convertStringArrayToListOfLongObjects( surveyFormIds );
    ActionMessages errors = new ActionMessages();
    try
    {
      getSurveyService().deleteSurveys( surveyFormIdList );
    }
    catch( ServiceErrorException se )
    {
      List serviceErrors = se.getServiceErrors();
      ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, errors );
    }
    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
    }
  }

  private SurveyService getSurveyService()
  {
    return (SurveyService)getService( SurveyService.BEAN_NAME );
  }
}
