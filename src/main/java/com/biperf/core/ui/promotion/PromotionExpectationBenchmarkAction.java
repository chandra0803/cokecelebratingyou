
package com.biperf.core.ui.promotion;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.domain.enums.ProcessFrequencyType;
import com.biperf.core.domain.process.Process;
import com.biperf.core.domain.process.ProcessSchedule;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.exception.UniqueConstraintViolationException;
import com.biperf.core.process.EngagementRefreshScoresProcess;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.process.ProcessService;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.constants.ViewAttributeNames;
import com.biperf.core.ui.utils.ActionUtils;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.UserManager;

/**
 * 
 * PromotionExpectationBenchmarkAction.
 * 
 * @author kandhi
 * @since Apr 10, 2014
 * @version 1.0
 */
public class PromotionExpectationBenchmarkAction extends PromotionBaseDispatchAction
{
  /**
   * RETURN_ACTION_URL_PARAM
   */
  public static String RETURN_ACTION_URL_PARAM = "returnActionUrl";

  public ActionForward unspecified( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    return display( mapping, actionForm, request, response );
  }

  public ActionForward display( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    String forwardTo = ActionConstants.SUCCESS_FORWARD;

    Promotion promotion = null;

    PromotionExpectationBenchmarkForm promoExpectationBenchmarkForm = (PromotionExpectationBenchmarkForm)form;

    promoExpectationBenchmarkForm.setReturnActionUrl( RequestUtils.getOptionalParamString( request, RETURN_ACTION_URL_PARAM ) );

    String promotionId = promoExpectationBenchmarkForm.getPromotionId();

    if ( isWizardMode( request ) )
    {
      promotion = getWizardPromotion( request );
      promotionId = promotion.getId().toString();
    }

    if ( promotionId != null && promotionId.length() > 0 )
    {
      promotion = getPromotion( new Long( promotionId ), promoExpectationBenchmarkForm.getPromotionTypeCode() );
    }
    else
    {
      throw new IllegalArgumentException( "promotionId is null" );
    }

    promoExpectationBenchmarkForm.load( promotion );

    return mapping.findForward( forwardTo );
  }

  public ActionForward save( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionForward forward = mapping.findForward( ActionConstants.SUCCESS_FORWARD );
    ActionMessages errors = new ActionMessages();

    PromotionExpectationBenchmarkForm promoExpectationBenchmarkForm = (PromotionExpectationBenchmarkForm)form;

    Promotion promotion = null;

    String promotionId = promoExpectationBenchmarkForm.getPromotionId();

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
    if ( request.getSession().getAttribute( ViewAttributeNames.PAGE_MODE ).equals( ViewAttributeNames.WIZARD_MODE ) )
    {
      promotion = getWizardPromotion( request );
    }
    else
    {
      promotion = getPromotion( new Long( promotionId ), promoExpectationBenchmarkForm.getPromotionTypeCode() );
    }

    promoExpectationBenchmarkForm.toDomain( promotion );

    try
    {
      promotion = getPromotionService().savePromotion( promotion );
    }
    catch( UniqueConstraintViolationException e )
    {
      throw new BeaconRuntimeException( "This call shouldn't change any unique fields.", e );
    }

    if ( isWizardMode( request ) )
    {
      forward = getWizardNextPage( mapping, request, promotion );
    }
    else
    {
      forward = saveAndExit( mapping, request, promotion );
    }

    return forward;
  }

  public ActionForward addBenchmark( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    String forwardTo = ActionConstants.DISPLAY_FORWARD;

    Promotion promotion = null;

    PromotionExpectationBenchmarkForm promoExpectationBenchmarkForm = (PromotionExpectationBenchmarkForm)form;

    String promotionId = promoExpectationBenchmarkForm.getPromotionId();

    if ( isWizardMode( request ) )
    {
      promotion = getWizardPromotion( request );
      promotionId = promotion.getId().toString();
    }

    if ( promotionId != null && promotionId.length() > 0 )
    {
      promotion = getPromotion( new Long( promotionId ), promoExpectationBenchmarkForm.getPromotionTypeCode() );
    }
    else
    {
      throw new IllegalArgumentException( "promotionId is null" );
    }

    promoExpectationBenchmarkForm.addBenchmark( promotion );

    return mapping.findForward( forwardTo );
  }

  public ActionForward deleteBenchmark( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    String forwardTo = ActionConstants.DISPLAY_FORWARD;

    Promotion promotion = null;

    PromotionExpectationBenchmarkForm promoExpectationBenchmarkForm = (PromotionExpectationBenchmarkForm)form;

    String promotionId = promoExpectationBenchmarkForm.getPromotionId();

    if ( isWizardMode( request ) )
    {
      promotion = getWizardPromotion( request );
      promotionId = promotion.getId().toString();
    }

    if ( promotionId != null && promotionId.length() > 0 )
    {
      promotion = getPromotion( new Long( promotionId ), promoExpectationBenchmarkForm.getPromotionTypeCode() );
    }
    else
    {
      throw new IllegalArgumentException( "promotionId is null" );
    }

    promoExpectationBenchmarkForm.removeBenchmark( promotion );

    return mapping.findForward( forwardTo );
  }

  @SuppressWarnings( { "unchecked", "rawtypes" } )
  public ActionForward refreshScores( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    PromotionExpectationBenchmarkForm promoExpectationBenchmarkForm = (PromotionExpectationBenchmarkForm)form;

    Process process = getProcessService().createOrLoadSystemProcess( EngagementRefreshScoresProcess.PROCESS_NAME, EngagementRefreshScoresProcess.BEAN_NAME );
    LinkedHashMap<String, Object> parameterValueMap = new LinkedHashMap<String, Object>();
    ProcessSchedule processSchedule = new ProcessSchedule();
    processSchedule.setStartDate( new Date() );
    processSchedule.setTimeOfDayMillis( new Long( 0 ) );
    processSchedule.setProcessFrequencyType( ProcessFrequencyType.lookup( ProcessFrequencyType.ONE_TIME_ONLY ) );
    getProcessService().scheduleProcess( process, processSchedule, parameterValueMap, UserManager.getUserId() );

    Map clientStateParameterMap = new HashMap();
    clientStateParameterMap.put( "promotionId", promoExpectationBenchmarkForm.getPromotionId() );
    String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
    String[] requestParams = new String[] { METHOD_QUERY_STRING, queryString };

    return ActionUtils.forwardWithParameters( mapping, ActionConstants.SUCCESS_FORWARD, requestParams );
  }

  @SuppressWarnings( "unchecked" )
  private Promotion getPromotion( Long promotionId, String promotionType )
  {
    // Set the Association Request Collection
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.PRIMARY_AUDIENCES ) );

    // Get the Promotion details
    Promotion promotion = getPromotionService().getPromotionByIdWithAssociations( promotionId, associationRequestCollection );

    return promotion;
  }

  protected static ProcessService getProcessService()
  {
    return (ProcessService)getService( ProcessService.BEAN_NAME );
  }
}
