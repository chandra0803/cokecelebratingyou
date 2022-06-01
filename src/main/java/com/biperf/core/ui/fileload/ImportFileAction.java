/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/fileload/ImportFileAction.java,v $
 */

package com.biperf.core.ui.fileload;

import java.util.Date;
import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.domain.enums.FileImportApprovalType;
import com.biperf.core.domain.enums.FileImportTransactionType;
import com.biperf.core.domain.enums.ImportFileStatusType;
import com.biperf.core.domain.enums.ImportFileTypeType;
import com.biperf.core.domain.fileload.ImportFile;
import com.biperf.core.domain.process.Process;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.process.AutoVinImportProcess;
import com.biperf.core.process.AwardLevelImportProcess;
import com.biperf.core.process.BadgeImportProcess;
import com.biperf.core.process.BudgetDistributionImportProcess;
import com.biperf.core.process.BudgetImportProcess;
import com.biperf.core.process.DepositImportProcess;
import com.biperf.core.process.FileloadVerifyProcess;
import com.biperf.core.process.HierarchyImportProcess;
import com.biperf.core.process.LeaderBoardImportProcess;
import com.biperf.core.process.NominationCustomApproverProcess;
import com.biperf.core.process.ParticipantImportProcess;
import com.biperf.core.process.PaxBaseImportProcess;
import com.biperf.core.process.PaxCPLevelImportProcess;
import com.biperf.core.process.PaxGoalImportProcess;
import com.biperf.core.process.ProductImportProcess;
import com.biperf.core.process.ProgressImportProcess;
import com.biperf.core.process.QuizImportProcess;
import com.biperf.core.process.SSIContestATNImportProcess;
import com.biperf.core.process.SSIContestDTGTImportProcess;
import com.biperf.core.process.SSIContestOBJImportProcess;
import com.biperf.core.process.SSIContestSIUImportProcess;
import com.biperf.core.process.SSIContestSRImportProcess;
import com.biperf.core.process.ThrowdownProgressImportProcess;
import com.biperf.core.service.fileload.ImportService;
import com.biperf.core.service.process.ProcessService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.utils.AwsUtils;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.UserManager;
import com.biperf.util.StringUtils;
import com.objectpartners.cms.util.CmsResourceBundle;

/*
 * ImportFileAction <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th> <th>Date</th>
 * <th>Version</th> <th>Comments</th> </tr> <tr> <td>Thomas Eaton</td> <td>Aug 31, 2005</td>
 * <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 *
 */

public class ImportFileAction extends BaseDispatchAction
{
  // ---------------------------------------------------------------------------
  // Constants
  // ---------------------------------------------------------------------------

  /**
   * The key to the HTTP request attribute that contains an import file.
   */
  public static final String IMPORT_FILE = "importFile";

  public static final String BUDGET_IMPORT_PROCESS_KEY = "BudgetImportProcess";
  public static final String LEADERBOARD_IMPORT_PROCESS_KEY = "LeaderBoardImportProcess";
  public static final String DEPOSIT_IMPORT_PROCESS_KEY = "DepositImportProcess";
  public static final String HIERARCHY_IMPORT_PROCESS_KEY = "HierarchyImportProcess";
  public static final String PAX_IMPORT_PROCESS_KEY = "ParticipantImportProcess";
  public static final String PRODUCT_IMPORT_PROCESS_KEY = "ProductImportProcess";
  public static final String QUIZ_IMPORT_PROCESS_KEY = "QuizImportProcess";
  public static final String PAX_BASE_IMPORT_PROCESS_KEY = "PaxBaseImportProcess";
  public static final String PAX_GOAL_IMPORT_PROCESS_KEY = "PaxGoalImportProcess";
  public static final String PROGRESS_IMPORT_PROCESS_KEY = "ProgressImportProcess";
  public static final String VIN_IMPORT_PROCESS_KEY = "AutoVinImportProcess";
  public static final String CP_PAX_BASE_IMPORT_PROCESS_KEY = "CPPaxBaseImportProcess";
  public static final String CP_PAX_LEVEL_IMPORT_PROCESS_KEY = "CPPaxLevelImportProcess";
  public static final String CP_PROGRESS_IMPORT_PROCESS_KEY = "CPProgressImportProcess";
  public static final String AWARD_LEVEL_IMPORT_PROCESS_KEY = "awardLevelImportProcess";
  public static final String BADGE_IMPORT_PROCESS_KEY = "BadgeImportProcess";

  public static final String FILELOAD_VERIFY_PROCESS_KEY = "FileloadVerifyProcess";

  public static final String TD_PROGRESS_IMPORT_PROCESS_KEY = "TDProgressImportProcess";
  public static final String BUDGET_DISTRIBUTION_IMPORT_PROCESS_KEY = "BudgetDistributionImportProcess";

  public static final String NOMINATION_CUSTOM_APPROVER_IMPORT_PROCESS_KEY = "NominationCustomApproverProcess";

  public static final String SSI_CONTEST_ATN_IMPORT_PROCESS_KEY = "SSIContestATNImportProcess";
  public static final String SSI_CONTEST_DTGT_IMPORT_PROCESS_KEY = "SSIContestDTGTImportProcess";
  public static final String SSI_CONTEST_SIU_IMPORT_PROCESS_KEY = "SSIContestSIUImportProcess";
  public static final String SSI_CONTEST_SR_IMPORT_PROCESS_KEY = "SSIContestSRImportProcess";
  public static final String SSI_CONTEST_OBJ_IMPORT_PROCESS_KEY = "SSIContestOBJImportProcess";

  // ---------------------------------------------------------------------------
  // Public Methods
  // ---------------------------------------------------------------------------

  /**
   * Deletes the specified import file.
   * 
   * @param mapping the action mapping used to select this instance.
   * @param form the ActionForm bean for this request.
   * @param request the HTTP request we are processing.
   * @param response the HTTP response we are creating.
   * @return an <code>ActionForward</code> instance describing where and how control should be
   *         forwarded, or null if the response has already been completed.
   */
  public ActionForward deleteImportFile( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionForward forward = mapping.findForward( ActionConstants.SUCCESS_FORWARD );
    ActionMessages errors = new ActionMessages();

    if ( isTokenValid( request, true ) )
    {
      Long importFileId = new Long( ( (ImportFileForm)form ).getImportFileId() );
      getImportService().deleteImportFile( importFileId );
    }
    else
    {
      errors.add( ActionConstants.ERROR_TOKEN_FAILURE, new ActionMessage( ActionConstants.ERROR_TOKEN_EXCEPTION ) );
      saveErrors( request, errors );
      forward = mapping.findForward( ActionConstants.FAIL_FORWARD );
    }

    return forward;
  }

  public ActionForward selectTransactionType( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionForward forward = mapping.findForward( ActionConstants.SUCCESS_FORWARD );

    ImportFileForm importFileForm = (ImportFileForm)form;
    ImportFile importFile = getImportService().getImportFile( new Long( importFileForm.getImportFileId() ), new ImportFileAssociationRequest() );
    request.setAttribute( "importFile", importFile );

    return forward;
  }

  public ActionForward selectBudgetSegment( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionForward forward = mapping.findForward( ActionConstants.SUCCESS_FORWARD );

    ImportFileForm importFileForm = (ImportFileForm)form;
    ImportFile importFile = getImportService().getImportFile( new Long( importFileForm.getImportFileId() ), new ImportFileAssociationRequest() );
    request.setAttribute( "importFile", importFile );
    request.setAttribute( "promotionId", importFileForm.getPromotionId() );

    return forward;
  }

  /**
   * Imports the specified import file via an asynchronous process.
   * 
   * @param mapping the action mapping used to select this instance.
   * @param form the ActionForm bean for this request.
   * @param request the HTTP request we are processing.
   * @param response the HTTP response we are creating.
   * @return an <code>ActionForward</code> instance describing where and how control should be
   *         forwarded, or null if the response has already been completed.
   */
  public ActionForward importImportFileAsynch( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionForward forward = mapping.findForward( ActionConstants.SUCCESS_FORWARD );
    ActionMessages errors = new ActionMessages();

    ImportFileForm importFileForm = (ImportFileForm)form;

    Long importFileId = new Long( importFileForm.getImportFileId() );
    String importFileType = importFileForm.getImportFileType();

    boolean isRecognitionAwardLevel = false;
    if ( importFileType.equalsIgnoreCase( ImportFileTypeType.AWARD_LEVEL ) )
    {
      isRecognitionAwardLevel = true;
      ImportFile importFile = getImportService().getImportFile( importFileId );
      ImportFile recAwardLevel = null;
      if ( importFile.getPromotion().isRecognitionPromotion() )
      {
        recAwardLevel = (ImportFile)request.getSession().getAttribute( "recAwardsImportFile" );
      }
      if ( importFile.getSubmitter() == null || importFile.getPromotion().isRecognitionPromotion() && recAwardLevel == null )
      {
        // ActionMessages errors = new ActionMessages();
        errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "admin.fileload.errors.params", "SUBMITTER" ) ) );
        saveErrors( request, errors );
        return mapping.findForward( ActionConstants.FAIL_FORWARD );
      }
      if ( importFile.getPromotion().isRecognitionPromotion() )
      {
        recAwardLevel = (ImportFile)request.getSession().getAttribute( "recAwardsImportFile" );
        if ( recAwardLevel.getSubmitter() != null )
        {
          importFile.setSubmitterNode( recAwardLevel.getSubmitterNode() );
          importFile.setSubmitterComments( recAwardLevel.getSubmitterComments() );
          importFile.setCard( recAwardLevel.getCard() );
          importFile.setMessage( recAwardLevel.getMessage() );
          importFile.setStatus( ImportFileStatusType.lookup( ImportFileStatusType.IMPORT_IN_PROCESS ) );
          importFile.setSubmitter( recAwardLevel.getSubmitter() );
          getImportService().saveRecognitionAwardsLevel( importFile );
          request.getSession().removeAttribute( "recAwardsImportFile" );
          request.getSession().setAttribute( "recAwardsImportFile", null );
          launchProcess( AWARD_LEVEL_IMPORT_PROCESS_KEY, AwardLevelImportProcess.BEAN_NAME, importFileId );
        }
      }
    }

    boolean isRecognitionDeposit = false;
    // Token check exclusion processing for Deposit Load with recognition details due to pop-up
    // issues
    if ( importFileType.equalsIgnoreCase( ImportFileTypeType.DEPOSIT ) )
    {
      ImportFile importFile = getImportService().getImportFile( importFileId );
      if ( importFile.getPromotion().isRecognitionPromotion() && FileImportTransactionType.CREATE_RECOGNITION.equals( importFileForm.getTransactionType() ) )
      {
        // Set the Recognition Deposit flag to true
        isRecognitionDeposit = true;

        if ( importFile.getSubmitter() == null )
        {
          // ActionMessages errors = new ActionMessages();
          errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "admin.fileload.errors.params", "SUBMITTER" ) ) );
          saveErrors( request, errors );
          return mapping.findForward( ActionConstants.FAIL_FORWARD );
        }

        ImportFile recOptionsFile = (ImportFile)request.getSession().getAttribute( "recOptionsImportFile" );
        if ( recOptionsFile.getSubmitter() != null )
        {
          importFile.setRecognitionDeposit( new Boolean( true ) );
          importFile.setSubmitter( recOptionsFile.getSubmitter() );
          importFile.setSubmitterNode( recOptionsFile.getSubmitterNode() );
          importFile.setSubmitterComments( recOptionsFile.getSubmitterComments() );
          importFile.setCard( recOptionsFile.getCard() );
          importFile.setMessage( recOptionsFile.getMessage() );
          importFile.setStatus( ImportFileStatusType.lookup( ImportFileStatusType.IMPORT_IN_PROCESS ) );

          getImportService().saveRecognitionOptions( importFile );

          request.getSession().removeAttribute( "recOptionsImportFile" );
          request.getSession().setAttribute( "recOptionsImportFile", null );

          launchProcess( DEPOSIT_IMPORT_PROCESS_KEY, DepositImportProcess.BEAN_NAME, importFileId );
        }
      }
    }

    if ( !isRecognitionDeposit && !isRecognitionAwardLevel )
    {
      if ( isTokenValid( request, true ) )
      {
        // Save the in process status
        // for bug #15667, do not change the status for hierarchy in case they have error records.
        if ( !importFileType.equalsIgnoreCase( ImportFileTypeType.HIERARCHY ) )
        {
          getImportService().setImportFileStatus( importFileId, ImportFileStatusType.IMPORT_IN_PROCESS );
        }

        // Launch fileload import process based on file load type
        if ( importFileType.equalsIgnoreCase( ImportFileTypeType.BUDGET ) )
        {
          launchProcess( BUDGET_IMPORT_PROCESS_KEY, BudgetImportProcess.BEAN_NAME, importFileId );
        }
        else if ( importFileType.equalsIgnoreCase( ImportFileTypeType.DEPOSIT ) )
        {
          Long messageId = new Long( importFileForm.getMessageId() );
          if ( messageId.longValue() > -1 ) // Fix 18956
          {
            getImportService().setMessage( importFileId, messageId );
          }
          launchProcess( DEPOSIT_IMPORT_PROCESS_KEY, DepositImportProcess.BEAN_NAME, importFileId );
        }
        else if ( importFileType.equalsIgnoreCase( ImportFileTypeType.HIERARCHY ) )
        {
          if ( getImportService().getImportFile( importFileId ).getImportRecordErrorCount() <= 0 )
          {
            // for bug #15667 - now change the status, no error records.
            getImportService().setImportFileStatus( importFileId, ImportFileStatusType.IMPORT_IN_PROCESS );
            launchProcess( HIERARCHY_IMPORT_PROCESS_KEY, HierarchyImportProcess.BEAN_NAME, importFileId );
          }
          else
          {
            errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "admin.fileload.errors.HIERARCHY_ERRORS" ) );
            saveErrors( request, errors );
            forward = mapping.findForward( ActionConstants.FAIL_FORWARD );
          }
        }
        else if ( importFileType.equalsIgnoreCase( ImportFileTypeType.PARTICIPANT ) )
        {
          launchProcess( PAX_IMPORT_PROCESS_KEY, ParticipantImportProcess.BEAN_NAME, importFileId );
        }
        else if ( importFileType.equalsIgnoreCase( ImportFileTypeType.LEADERBOARD ) )
        {
          launchProcess( LEADERBOARD_IMPORT_PROCESS_KEY, LeaderBoardImportProcess.BEAN_NAME, importFileId );
        }
        else if ( importFileType.equalsIgnoreCase( ImportFileTypeType.PRODUCT ) )
        {
          launchProcess( PRODUCT_IMPORT_PROCESS_KEY, ProductImportProcess.BEAN_NAME, importFileId );
        }
        else if ( importFileType.equalsIgnoreCase( ImportFileTypeType.QUIZ ) )
        {
          launchProcess( QUIZ_IMPORT_PROCESS_KEY, QuizImportProcess.BEAN_NAME, importFileId );
        }
        else if ( importFileType.equalsIgnoreCase( ImportFileTypeType.GQ_BASE_DATA_LOAD ) )
        {
          launchProcess( PAX_BASE_IMPORT_PROCESS_KEY, PaxBaseImportProcess.BEAN_NAME, importFileId );
        }
        else if ( importFileType.equalsIgnoreCase( ImportFileTypeType.GQ_GOAL_DATA_LOAD ) )
        {
          launchProcess( PAX_GOAL_IMPORT_PROCESS_KEY, PaxGoalImportProcess.BEAN_NAME, importFileId );
        }
        else if ( importFileType.equalsIgnoreCase( ImportFileTypeType.GQ_PROGRESS_DATA_LOAD ) )
        {
          launchProcess( PROGRESS_IMPORT_PROCESS_KEY, ProgressImportProcess.BEAN_NAME, importFileId );
        }
        else if ( importFileType.equalsIgnoreCase( ImportFileTypeType.GQ_VIN_LOAD ) )
        {
          launchProcess( VIN_IMPORT_PROCESS_KEY, AutoVinImportProcess.BEAN_NAME, importFileId );
        }
        else if ( importFileType.equalsIgnoreCase( ImportFileTypeType.CP_BASE_DATA_LOAD ) )
        {
          launchProcess( CP_PAX_BASE_IMPORT_PROCESS_KEY, PaxBaseImportProcess.BEAN_NAME, importFileId );
        }
        else if ( importFileType.equalsIgnoreCase( ImportFileTypeType.CP_LEVEL_DATA_LOAD ) )
        {
          launchProcess( CP_PAX_LEVEL_IMPORT_PROCESS_KEY, PaxCPLevelImportProcess.BEAN_NAME, importFileId );
        }
        else if ( importFileType.equalsIgnoreCase( ImportFileTypeType.CP_PROGRESS_DATA_LOAD ) )
        {
          launchProcess( CP_PROGRESS_IMPORT_PROCESS_KEY, ProgressImportProcess.BEAN_NAME, importFileId );
        }
        else if ( importFileType.equalsIgnoreCase( ImportFileTypeType.BADGE ) )
        {
          launchProcess( BADGE_IMPORT_PROCESS_KEY, BadgeImportProcess.BEAN_NAME, importFileId );
        }
        else if ( importFileType.equalsIgnoreCase( ImportFileTypeType.TD_PROGRESS_DATA_LOAD ) )
        {
          launchProcess( TD_PROGRESS_IMPORT_PROCESS_KEY, ThrowdownProgressImportProcess.BEAN_NAME, importFileId );
        }
        else if ( importFileType.equalsIgnoreCase( ImportFileTypeType.BUDGET_DISTRIBUTION ) )
        {
          launchProcess( BUDGET_DISTRIBUTION_IMPORT_PROCESS_KEY, BudgetDistributionImportProcess.BEAN_NAME, importFileId );
        }
        else if ( importFileType.equalsIgnoreCase( ImportFileTypeType.NOMINATION_APPROVER ) )
        {
          launchProcess( NOMINATION_CUSTOM_APPROVER_IMPORT_PROCESS_KEY, NominationCustomApproverProcess.BEAN_NAME, importFileId );
        }
        else if ( importFileType.equalsIgnoreCase( ImportFileTypeType.SSI_CONTEST_ATN ) )
        {
          launchProcess( SSI_CONTEST_ATN_IMPORT_PROCESS_KEY, SSIContestATNImportProcess.BEAN_NAME, importFileId );
        }
        else if ( importFileType.equalsIgnoreCase( ImportFileTypeType.SSI_CONTEST_DTGT ) )
        {
          launchProcess( SSI_CONTEST_ATN_IMPORT_PROCESS_KEY, SSIContestDTGTImportProcess.BEAN_NAME, importFileId );
        }
        else if ( importFileType.equalsIgnoreCase( ImportFileTypeType.SSI_CONTEST_SIU ) )
        {
          launchProcess( SSI_CONTEST_ATN_IMPORT_PROCESS_KEY, SSIContestSIUImportProcess.BEAN_NAME, importFileId );
        }
        else if ( importFileType.equalsIgnoreCase( ImportFileTypeType.SSI_CONTEST_SR ) )
        {
          launchProcess( SSI_CONTEST_ATN_IMPORT_PROCESS_KEY, SSIContestSRImportProcess.BEAN_NAME, importFileId );
        }
        else if ( importFileType.equalsIgnoreCase( ImportFileTypeType.SSI_CONTEST_OBJ ) )
        {
          launchProcess( SSI_CONTEST_ATN_IMPORT_PROCESS_KEY, SSIContestOBJImportProcess.BEAN_NAME, importFileId );
        }
      }
      else
      {
        errors.add( ActionConstants.ERROR_TOKEN_FAILURE, new ActionMessage( ActionConstants.ERROR_TOKEN_EXCEPTION ) );
        saveErrors( request, errors );
        forward = mapping.findForward( ActionConstants.FAIL_FORWARD );
      }
    }

    return forward;
  }

  /**
   * Verifies the specified import file via an asynchronous process.
   * 
   * @param mapping the action mapping used to select this instance.
   * @param form the ActionForm bean for this request.
   * @param request the HTTP request we are processing.
   * @param response the HTTP response we are creating.
   * @return an <code>ActionForward</code> instance describing where and how control should be
   *         forwarded, or null if the response has already been completed.
   */
  public ActionForward verifyImportFileAsync( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionForward forward = mapping.findForward( ActionConstants.SUCCESS_FORWARD );
    ActionMessages errors = new ActionMessages();

    if ( isTokenValid( request, true ) )
    {
      ImportFileForm importFileForm = (ImportFileForm)form;
      Long importFileId = new Long( importFileForm.getImportFileId() );
      String importFileType = importFileForm.getImportFileType();

      log.info( "File import - import field id from request:" + importFileForm.getImportFileId() );
      log.info( "File import - leaderboard id from request:" + importFileForm.getLeaderBoardId() );

      // Bind dependent objects to the import file.
      if ( importFileType.equalsIgnoreCase( ImportFileTypeType.BUDGET ) || importFileType.equalsIgnoreCase( ImportFileTypeType.DEPOSIT )
          || importFileType.equalsIgnoreCase( ImportFileTypeType.PRODUCT_CLAIM ) || importFileType.equalsIgnoreCase( ImportFileTypeType.GQ_BASE_DATA_LOAD )
          || importFileType.equalsIgnoreCase( ImportFileTypeType.GQ_GOAL_DATA_LOAD ) || importFileType.equalsIgnoreCase( ImportFileTypeType.GQ_PROGRESS_DATA_LOAD )
          || importFileType.equalsIgnoreCase( ImportFileTypeType.GQ_VIN_LOAD ) || importFileType.equalsIgnoreCase( ImportFileTypeType.CP_BASE_DATA_LOAD )
          || importFileType.equalsIgnoreCase( ImportFileTypeType.CP_LEVEL_DATA_LOAD ) || importFileType.equalsIgnoreCase( ImportFileTypeType.CP_PROGRESS_DATA_LOAD )
          || importFileType.equalsIgnoreCase( ImportFileTypeType.AWARD_LEVEL ) || importFileType.equalsIgnoreCase( ImportFileTypeType.TD_PROGRESS_DATA_LOAD )
          || importFileType.equalsIgnoreCase( ImportFileTypeType.NOMINATION_APPROVER ) )
      {
        Long promotionId;
        if ( request.getParameter( "promotionId" ) != null )
        {
          promotionId = new Long( request.getParameter( "promotionId" ) );
        }
        else
        {
          promotionId = new Long( importFileForm.getPromotionId() );
        }
        getImportService().setPromotion( importFileId, promotionId );
      }

      if ( importFileType.equalsIgnoreCase( ImportFileTypeType.HIERARCHY ) || importFileType.equalsIgnoreCase( ImportFileTypeType.PARTICIPANT ) )
      {
        if ( importFileForm.getHierarchyId() != null )
        {
          Long hierarchyId = new Long( importFileForm.getHierarchyId() );
          getImportService().setHierarchy( importFileId, hierarchyId );
        }
      }

      if ( importFileType.equalsIgnoreCase( ImportFileTypeType.BUDGET ) || importFileType.equalsIgnoreCase( ImportFileTypeType.GQ_PROGRESS_DATA_LOAD )
          || importFileType.equalsIgnoreCase( ImportFileTypeType.GQ_GOAL_DATA_LOAD ) || importFileType.equalsIgnoreCase( ImportFileTypeType.CP_PROGRESS_DATA_LOAD )
          || importFileType.equalsIgnoreCase( ImportFileTypeType.CP_LEVEL_DATA_LOAD ) || importFileType.equalsIgnoreCase( ImportFileTypeType.TD_PROGRESS_DATA_LOAD ) )
      {
        Boolean replaceValues = importFileForm.getReplaceValues();
        getImportService().setReplaceValues( importFileId, replaceValues );
        Integer roundNumber = importFileForm.getRoundNumber();
        getImportService().setRoundNumber( importFileId, roundNumber );
      }

      if ( importFileType.equalsIgnoreCase( ImportFileTypeType.BUDGET ) )
      {
        Long budgetSegmentId = importFileForm.getBudgetSegmentId();
        getImportService().setBudgetSegmentId( importFileId, budgetSegmentId );

        Long countryId = importFileForm.getCountryId();
        getImportService().setCountry( importFileId, countryId );
      }

      if ( importFileType.equalsIgnoreCase( ImportFileTypeType.BUDGET_DISTRIBUTION ) )
      {
        Long budgetMasterId = importFileForm.getBudgetMasterId();
        getImportService().setBudgetMasterId( importFileId, budgetMasterId );

        Long budgetSegmentId = importFileForm.getBudgetSegmentId();
        getImportService().setBudgetSegmentId( importFileId, budgetSegmentId );
      }

      if ( importFileType.equalsIgnoreCase( ImportFileTypeType.LEADERBOARD ) )
      {

        getImportService().setActionType( importFileId, importFileForm.getActionType() );

        Date asOfDate = DateUtils.toDate( importFileForm.getAsOfDate() );
        getImportService().setasOfDate( importFileId, asOfDate );

        Long leaderBoardId = importFileForm.getLeaderBoardId();
        getImportService().setleaderBoardId( importFileId, leaderBoardId );
      }

      if ( importFileType.equalsIgnoreCase( ImportFileTypeType.BADGE ) )
      {
        Long badgeId = importFileForm.getTheBadgeId();
        getImportService().setBadgeId( importFileId, badgeId );
        Date earnedDate = DateUtils.toDate( importFileForm.getEarnedDate() );
        getImportService().setEarnedDate( importFileId, earnedDate );

      }

      if ( importFileType.equalsIgnoreCase( ImportFileTypeType.GQ_PROGRESS_DATA_LOAD ) || importFileType.equalsIgnoreCase( ImportFileTypeType.GQ_VIN_LOAD )
          || importFileType.equalsIgnoreCase( ImportFileTypeType.CP_PROGRESS_DATA_LOAD ) || importFileType.equalsIgnoreCase( ImportFileTypeType.TD_PROGRESS_DATA_LOAD ) )
      {
        Date progressEndDate = DateUtils.toDate( importFileForm.getProgressEndDate() );
        getImportService().setProgressEndDate( importFileId, progressEndDate );
      }

      if ( importFileType.equalsIgnoreCase( ImportFileTypeType.PRODUCT_CLAIM ) )
      {
        FileImportApprovalType fileImportApprovalType = FileImportApprovalType.lookup( importFileForm.getFileImportApprovalTypeCode() );
        getImportService().setFileImportApprovalType( importFileId, fileImportApprovalType );
      }

      if ( importFileType.equalsIgnoreCase( ImportFileTypeType.AWARD_LEVEL ) || importFileType.equalsIgnoreCase( ImportFileTypeType.DEPOSIT ) )
      {
        if ( importFileForm.isDelayAward() )
        {
          Date delayAwardDate = DateUtils.toDate( importFileForm.getDelayAwardDate() );
          getImportService().setDelayAwardDate( importFileId, delayAwardDate );
        }
        else
        {
          getImportService().setDelayAwardDate( importFileId, null );
        }
      }

      if ( importFileType.equalsIgnoreCase( ImportFileTypeType.SSI_CONTEST_OBJ ) || importFileType.equalsIgnoreCase( ImportFileTypeType.SSI_CONTEST_ATN )
          || importFileType.equalsIgnoreCase( ImportFileTypeType.SSI_CONTEST_DTGT ) || importFileType.equalsIgnoreCase( ImportFileTypeType.SSI_CONTEST_SIU )
          || importFileType.equalsIgnoreCase( ImportFileTypeType.SSI_CONTEST_SR ) )
      {

        Long contestId = importFileForm.getContestId();
        getImportService().setContestId( importFileId, contestId );

        /*
         * String contestType = ""; getImportService().setContestType
         */
      }

      // Save the in process status
      getImportService().setImportFileStatus( importFileId, ImportFileStatusType.VERIFY_IN_PROCESS );

      // Launch fileload verify process
      launchProcess( FILELOAD_VERIFY_PROCESS_KEY, FileloadVerifyProcess.BEAN_NAME, importFileId );
    }
    else
    {
      errors.add( ActionConstants.ERROR_TOKEN_FAILURE, new ActionMessage( ActionConstants.ERROR_TOKEN_EXCEPTION ) );
      saveErrors( request, errors );
      forward = mapping.findForward( ActionConstants.FAIL_FORWARD );
    }

    return forward;
  }

  public ActionForward recordList( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ImportFileForm importFileForm = (ImportFileForm)form;
    ImportFile importFile = getImportService().getImportFile( new Long( importFileForm.getImportFileId() ) );
    return mapping.findForward( importFile.getFileType().getCode() );
  }

  public ActionForward checkForDelayRecognition( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionForward forward = mapping.findForward( ActionConstants.SUCCESS_FORWARD );
    // ActionMessages errors = new ActionMessages();
    ImportFileForm importFileForm = (ImportFileForm)form;

    if ( !StringUtils.isEmpty( importFileForm.getPromotionId() ) )
    {
      Promotion promotion = getPromotionService().getPromotionById( new Long( importFileForm.getPromotionId() ) );
      if ( promotion.isFileLoadEntry() && promotion.isRecognitionPromotion() && ( (RecognitionPromotion)promotion ).isAllowRecognitionSendDate() )
      {
        request.setAttribute( "showDelayRecognition", Boolean.TRUE );
        Date delayEndDate = DateUtils.getDateAfterNumberOfDays( new Date(), ( (RecognitionPromotion)promotion ).getMaxDaysDelayed().intValue() );
        request.setAttribute( "delayEndDate", DateUtils.toDisplayString( delayEndDate ) );
      }
    }

    return forward;
  }

  // ---------------------------------------------------------------------------
  // Private Methods
  // ---------------------------------------------------------------------------

  private void launchProcess( String processName, String processBeanName, Long importFileId )
  {
    Process process = getProcessService().createOrLoadSystemProcess( processName, processBeanName );

    LinkedHashMap parameterValueMap = new LinkedHashMap();
    parameterValueMap.put( "importFileId", new String[] { importFileId.toString() } );

    getProcessService().launchProcess( process, parameterValueMap, UserManager.getUserId() );
  }

  /**
   * Returns a reference to the Import service.
   * 
   * @return a reference to the Import service.
   */
  private ImportService getImportService()
  {
    return (ImportService)getService( ImportService.BEAN_NAME );
  }

  /**
   * Returns a reference to the Process service.
   * 
   * @return a reference to the Process service.
   */
  private ProcessService getProcessService()
  {
    return (ProcessService)getService( ProcessService.BEAN_NAME );
  }

  private PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }

  private String getValidFileOutURL( String outputFile )
  {
    StringBuilder builder = new StringBuilder();
    builder.append( getWorkWipPath() );
    builder.append( "/" );
    builder.append( getPrefix() );
    builder.append( "/" );
    builder.append( getSubFolderName() );
    if ( !AwsUtils.isAws() )
    {
      builder.append( "/valid" );
    }
    builder.append( "/" );
    builder.append( outputFile );
    return builder.toString();
  }

  private String getWorkWipPath()
  {
    return getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.GLOBAL_FILE_PROCESSING_WORKWIP ).getStringVal();
  }

  private String getPrefix()
  {
    return getSystemVariableService().getPropertyByName( SystemVariableService.GLOBAL_FILE_PROCESSING_PREFIX ).getStringVal();
  }

  private String getSubFolderName()
  {
    return getSystemVariableService().getPropertyByName( SystemVariableService.GLOBAL_FILE_PROCESSING_SUBFOLDER ).getStringVal();
  }

  public SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }
}
