
package com.biperf.core.ui.awardgenerator;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.domain.awardgenerator.AwardGenAward;
import com.biperf.core.domain.awardgenerator.AwardGenBatch;
import com.biperf.core.domain.awardgenerator.AwardGenerator;
import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.process.Process;
import com.biperf.core.domain.promotion.AbstractRecognitionPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.process.AwardFileLaunchManagerEmailProcess;
import com.biperf.core.service.awardgenerator.AwardGeneratorService;
import com.biperf.core.service.process.ProcessService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.ServiceErrorStrutsUtils;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.AwardGenPlateauFormBean;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * AwardGeneratorAction
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
 * <td>chowdhur</td>
 * <td>July 15, 2013</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class AwardGeneratorAction extends BaseDispatchAction
{
  /** logger */
  private static final Log logger = LogFactory.getLog( AwardGeneratorAction.class );

  public static final String OUTPUT_RETURN_CODE = "p_out_return_code";

  public static final String GOOD = "0";

  public static final String AWARDFILE_LAUNCH_MANAGER_EMAIL_PROCESS_KEY = "AwardFileLaunchManagerEmailProcess";

  /**
   * Display the AwardGenerator
   *
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward display( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    String forwardTo = ActionConstants.SUCCESS_FORWARD;
    AwardGeneratorForm awardGeneratorForm = (AwardGeneratorForm)request.getAttribute( "awardGeneratorForm" );
    ActionMessages errors = new ActionMessages();

    Long promotionId = awardGeneratorForm.getPromotionId();
    Long awardGeneratorId = awardGeneratorForm.getAwardGeneratorId();
    AwardGenerator awardGenerator = null;
    Promotion promotion = null;

    if ( awardGeneratorId == null || awardGeneratorId == 0 )
    {
      if ( promotionId != null && promotionId != 0 )
      {
        promotion = getPromotionService().getPromotionById( promotionId );
      }
    }
    else
    {
      try
      {
        awardGenerator = getAwardGeneratorService().getAwardGeneratorById( awardGeneratorId );
      }
      catch( ServiceErrorException e )
      {
        logger.debug( e );
        List serviceErrors = e.getServiceErrors();
        ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, errors );
      }

    }
    awardGeneratorForm.load( awardGenerator, promotion );
    request.setAttribute( "awardGeneratorForm", awardGeneratorForm );

    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      forwardTo = ActionConstants.FAIL_FORWARD;
    }
    return mapping.findForward( forwardTo );
  }

  public ActionForward resetFormDisplay( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    String forwardTo = ActionConstants.SUCCESS_FORWARD;
    ActionMessages errors = new ActionMessages();
    AwardGeneratorForm awardGeneratorForm = (AwardGeneratorForm)request.getAttribute( "awardGeneratorForm" );

    List<AwardGenAward> awardGenAwards = new ArrayList<AwardGenAward>();
    List<AwardGenPlateauFormBean> plateauValueFormBeans = new ArrayList<AwardGenPlateauFormBean>();
    awardGeneratorForm.setAwardGenAwards( awardGenAwards );
    awardGeneratorForm.setPlateauValueFormBeans( plateauValueFormBeans );
    awardGeneratorForm.addEmptyAwardGenAwards();
    awardGeneratorForm.addEmptyPlateauAwards();
    awardGeneratorForm.setSetupName( null );
    awardGeneratorForm.setExamineField( null );
    awardGeneratorForm.setNotifyManager( false );
    awardGeneratorForm.setNumberOfDaysForAlert( null );

    Long promotionId = awardGeneratorForm.getPromotionId();
    Long awardGeneratorId = awardGeneratorForm.getAwardGeneratorId();

    AwardGenerator awardGenerator = null;
    Promotion promotion = null;

    if ( awardGeneratorId == null || awardGeneratorId == 0 )
    {
      if ( promotionId != null && promotionId != 0 )
      {
        promotion = getPromotionService().getPromotionById( promotionId );
      }
    }
    else
    {
      try
      {
        awardGenerator = getAwardGeneratorService().getAwardGeneratorById( awardGeneratorId );
      }
      catch( ServiceErrorException e )
      {
        logger.debug( e );
        List serviceErrors = e.getServiceErrors();
        ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, errors );
      }

    }
    awardGeneratorForm.load( awardGenerator, promotion );
    request.setAttribute( "awardGeneratorForm", awardGeneratorForm );

    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      forwardTo = ActionConstants.FAIL_FORWARD;
    }
    return mapping.findForward( forwardTo );
  }

  public ActionForward addAnotherPointAward( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionForward forward = mapping.findForward( ActionConstants.SUCCESS_FORWARD );
    ActionMessages errors = new ActionMessages();
    AwardGeneratorForm awardGeneratorForm = (AwardGeneratorForm)request.getAttribute( "awardGeneratorForm" );

    awardGeneratorForm.addEmptyAwardGenAwards();

    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      forward = mapping.findForward( ActionConstants.FAIL_FORWARD );
    }

    return forward;
  }

  public ActionForward addAnotherPlateauAward( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionForward forward = mapping.findForward( ActionConstants.SUCCESS_FORWARD );
    ActionMessages errors = new ActionMessages();
    AwardGeneratorForm awardGeneratorForm = (AwardGeneratorForm)request.getAttribute( "awardGeneratorForm" );

    awardGeneratorForm.addEmptyPlateauAwards();
    awardGeneratorForm.setDeleteAwardgenAwardIds( null );

    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      forward = mapping.findForward( ActionConstants.FAIL_FORWARD );
    }

    return forward;
  }

  public ActionForward save( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    String forwardTo = ActionConstants.SUCCESS_FORWARD;
    AwardGeneratorForm awardGeneratorForm = (AwardGeneratorForm)request.getAttribute( "awardGeneratorForm" );

    Long promotionId = awardGeneratorForm.getPromotionId();
    Promotion promotion = getPromotionService().getPromotionById( promotionId );
    AbstractRecognitionPromotion recognitionPromo = null;
    if ( promotion != null && promotion.isAbstractRecognitionPromotion() )
    {
      recognitionPromo = (AbstractRecognitionPromotion)promotion;
    }
    AwardGenerator awardGenSaved = null;
    String successMessage = "";

    ActionMessages errors = new ActionMessages();
    try
    {
      AwardGenerator awardGenerator = new AwardGenerator();
      if ( awardGeneratorForm.getAwardGeneratorId() != null && awardGeneratorForm.getAwardGeneratorId() != 0 )
      {
        awardGenerator = getAwardGeneratorService().getAwardGeneratorById( awardGeneratorForm.getAwardGeneratorId() );
        forwardTo = ActionConstants.SUCCESS_UPDATE;
      }
      else
      {
        forwardTo = "success_save";
      }
      List<AwardGenPlateauFormBean> detachedPlateaus = awardGeneratorForm.getPlateauValueFormBeans();
      List<AwardGenAward> plateauAwards = awardGeneratorForm.toDomainPlateauAwards( detachedPlateaus, recognitionPromo );
      List<AwardGenAward> detachedPoints = awardGeneratorForm.getAwardGenAwards();
      String awardType = awardGeneratorForm.getAwardType();
      List<AwardGenAward> detachedAwards = new ArrayList<AwardGenAward>();

      if ( PromotionAwardsType.MERCHANDISE.equals( awardType ) )
      {
        detachedAwards = plateauAwards;
      }
      else if ( PromotionAwardsType.POINTS.equals( awardType ) )
      {
        detachedAwards = detachedPoints;
      }
      AwardGenerator awardGeneratorToSave = awardGeneratorForm.toDomainObject( promotion, awardGenerator, detachedAwards );
      awardGenSaved = getAwardGeneratorService().saveAwardGenerator( awardGeneratorToSave );

    }
    catch( ServiceErrorException e )
    {
      log.debug( e );
      List serviceErrors = e.getServiceErrors();
      ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, errors );
    }

    if ( awardGenSaved != null )
    {
      successMessage = CmsResourceBundle.getCmsBundle().getString( "awardgenerator.maintain.SAVE_SUCCESS" );
    }
    else
    {
      successMessage = CmsResourceBundle.getCmsBundle().getString( "awardgenerator.maintain.SAVE_FAILURE" );
    }
    request.setAttribute( "successMessage", successMessage );

    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      forwardTo = ActionConstants.FAIL_FORWARD;
    }

    return mapping.findForward( forwardTo );
  }

  public ActionForward removeSelectedPoints( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    ActionMessages errors = new ActionMessages();
    if ( !isTokenValid( request, true ) )
    {
      errors.add( "tokenFailure", new ActionMessage( "system.errors.TOKEN_EXCEPTION" ) );
      saveErrors( request, errors );
      return actionMapping.findForward( ActionConstants.FAIL_UPDATE ); // EARLY EXIT
    }

    try
    {
      AwardGeneratorForm awardGeneratorForm = (AwardGeneratorForm)actionForm;
      List<AwardGenAward> awardGenAwards = awardGeneratorForm.getAwardGenAwards();

      getAwardGeneratorService().deleteAwardGenAwards( awardGenAwards );
    }
    catch( ServiceErrorException e )
    {
      log.debug( e );
      List serviceErrors = e.getServiceErrors();
      ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, errors );
    }

    ActionForward actionForward;
    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      actionForward = actionMapping.findForward( ActionConstants.FAIL_DELETE );
    }
    else
    {
      actionForward = actionMapping.findForward( ActionConstants.SUCCESS_DELETE );
    }
    return actionForward;
  }

  public ActionForward removeSelectedPlateau( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    ActionMessages errors = new ActionMessages();
    if ( !isTokenValid( request, true ) )
    {
      errors.add( "tokenFailure", new ActionMessage( "system.errors.TOKEN_EXCEPTION" ) );
      saveErrors( request, errors );
      return actionMapping.findForward( ActionConstants.FAIL_UPDATE ); // EARLY EXIT
    }

    try
    {
      AwardGeneratorForm awardGeneratorForm = (AwardGeneratorForm)actionForm;
      List<AwardGenPlateauFormBean> plateauValuesFormBeans = awardGeneratorForm.getPlateauValueFormBeans();
      boolean awardActive = awardGeneratorForm.isAwardActive();

      getAwardGeneratorService().deleteAwardGenPlateauAwards( plateauValuesFormBeans, awardActive );
    }
    catch( ServiceErrorException e )
    {
      log.debug( e );
      List serviceErrors = e.getServiceErrors();
      ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, errors );
    }

    ActionForward actionForward;
    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      actionForward = actionMapping.findForward( ActionConstants.FAIL_DELETE );
    }
    else
    {
      actionForward = actionMapping.findForward( ActionConstants.SUCCESS_DELETE );
    }
    return actionForward;
  }

  /**
   * Batch Generate and Update
   */
  public ActionForward launchBatch( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    String forwardTo = ActionConstants.SUCCESS_UPDATE;
    AwardGeneratorForm awardGeneratorForm = (AwardGeneratorForm)request.getAttribute( "awardGeneratorForm" );

    ActionMessages errors = new ActionMessages();
    Long awardGenId = awardGeneratorForm.getAwardGeneratorId();
    String successMessage = "";
    Long awardGenBatchId = awardGeneratorForm.getAwardGenBatchId();
    Long awardGenBatchSavedId = null;
    boolean updateBatch = false;
    Map<String, Object> output = new HashMap<String, Object>();
    if ( awardGenId != null && awardGenId != 0 )
    {
      if ( awardGenBatchId != null && awardGenBatchId != 0 )
      {
        updateBatch = true;
      }
      Map<String, Object> awardGenParams = new HashMap<String, Object>();
      awardGenParams.put( "awardGeneratorId", awardGenId );
      awardGenParams.put( "awardGeneratorBatchId", awardGenBatchId );
      if ( awardGeneratorForm.getStartDate() != null && awardGeneratorForm.getEndDate() != null )
      {
        awardGenParams.put( "batchStartDate", DateUtils.toDate( awardGeneratorForm.getStartDate() ) );
        awardGenParams.put( "batchEndDate", DateUtils.toDate( awardGeneratorForm.getEndDate() ) );
      }
      else
      {
        AwardGenBatch awardgenbatch = getAwardGeneratorService().getAwardGenBatchById( awardGenBatchId );
        awardGenParams.put( "batchStartDate", awardgenbatch.getStartDate() );
        awardGenParams.put( "batchEndDate", awardgenbatch.getEndDate() );
      }
      awardGenParams.put( "useIssueDate", awardGeneratorForm.isUseIssueDate() );
      awardGenParams.put( "issueDate", DateUtils.toDate( awardGeneratorForm.getIssueDate() ) );
      awardGenParams.put( "awardType", awardGeneratorForm.getAwardType() );

      output = getAwardGeneratorService().generateAndSaveBatch( awardGenParams );
      awardGenBatchSavedId = ( (BigDecimal)output.get( "p_award_generator_batch_id" ) ).longValue();
      boolean isNotifyManager = ( (BigDecimal)output.get( "p_out_notify_manager" ) ).intValue() != 0 ? true : false;

      // schedule the manager mails
      if ( !updateBatch && awardGenBatchSavedId != null && isNotifyManager ) // Only
                                                                             // on
                                                                             // Launch
                                                                             // send
                                                                             // managers
                                                                             // the
                                                                             // email
      {
        launchProcess( AWARDFILE_LAUNCH_MANAGER_EMAIL_PROCESS_KEY, AwardFileLaunchManagerEmailProcess.BEAN_NAME, awardGenBatchSavedId );
      }
    }

    if ( updateBatch )
    {
      if ( awardGenBatchSavedId != null )
      {
        successMessage = CmsResourceBundle.getCmsBundle().getString( "awardgenerator.batch.UPDATE_SUCCESS" );
      }
      else
      {
        successMessage = CmsResourceBundle.getCmsBundle().getString( "awardgenerator.batch.UPDATE_FAILURE" );
      }
      request.setAttribute( "successUpdateMessage", successMessage );
    }
    else
    {
      if ( awardGenBatchSavedId != null )
      {
        successMessage = CmsResourceBundle.getCmsBundle().getString( "awardgenerator.batch.LAUNCH_SUCCESS" );
      }
      else
      {
        successMessage = CmsResourceBundle.getCmsBundle().getString( "awardgenerator.batch.LAUNCH_FAILURE" );
      }
      request.setAttribute( "successLaunchMessage", successMessage );
    }

    if ( !GOOD.equals( output.get( OUTPUT_RETURN_CODE ) ) )
    {
      saveErrors( request, errors );
      forwardTo = ActionConstants.FAIL_FORWARD;
    }

    return mapping.findForward( forwardTo );

  }

  public ActionForward extractBatch( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    String forwardTo = ActionConstants.SUCCESS_UPDATE;
    AwardGeneratorForm awardGeneratorForm = (AwardGeneratorForm)request.getAttribute( "awardGeneratorForm" );
    ActionMessages errors = new ActionMessages();
    Long awardGenBatchId = awardGeneratorForm.getAwardGenBatchId();
    boolean success = true;
    String successMessage = "";

    try
    {
      if ( awardGenBatchId != null && awardGenBatchId != 0 )
      {
        getAwardGeneratorService().generateAndSendEmailExtract( awardGenBatchId );
      }
    }
    catch( ServiceErrorException e )
    {
      success = false;
      log.debug( e );
      List serviceErrors = e.getServiceErrors();
      ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, errors );
    }

    if ( success )
    {
      successMessage = CmsResourceBundle.getCmsBundle().getString( "awardgenerator.batch.EXTRACT_SUCCESS" );
    }
    else
    {
      successMessage = CmsResourceBundle.getCmsBundle().getString( "awardgenerator.batch.EXTRACT_FAILURE" );
    }
    request.setAttribute( "successExtractMessage", successMessage );

    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      forwardTo = ActionConstants.FAIL_FORWARD;
    }

    return mapping.findForward( forwardTo );

  }

  private void launchProcess( String processName, String processBeanName, Long batchId )
  {
    Process process = getProcessService().createOrLoadSystemProcess( processName, processBeanName );
    LinkedHashMap parameterValueMap = new LinkedHashMap();
    parameterValueMap.put( "batchId", new String[] { batchId.toString() } );
    getProcessService().launchProcess( process, parameterValueMap, UserManager.getUserId() );
  }

  public ActionForward dismissAlert( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException
  {
    Long userId = UserManager.getUser().getUserId();
    Long batchId = (Long)ClientStateUtils.getParameterValueAsObject( request, ClientStateUtils.getClientStateMap( request ), "batchId" );
    getAwardGeneratorService().dismissAlertForAwardGenManager( userId, batchId );

    super.writeAsJsonToResponse( "", response );
    return null;
  }

  public ActionForward cancel( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    return mapping.findForward( ActionConstants.CANCEL_FORWARD );
  }

  private AwardGeneratorService getAwardGeneratorService()
  {
    return (AwardGeneratorService)getService( AwardGeneratorService.BEAN_NAME );
  }

  private PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }

  private ProcessService getProcessService()
  {
    return (ProcessService)getService( ProcessService.BEAN_NAME );
  }
}
