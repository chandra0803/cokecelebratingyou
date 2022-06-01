/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/fileload/ImportFileForm.java,v $
 */

package com.biperf.core.ui.fileload;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.biperf.core.domain.enums.FileImportTransactionType;
import com.biperf.core.domain.enums.ImportFileTypeType;
import com.biperf.core.domain.fileload.ImportFile;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseForm;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.DateUtils;
import com.biperf.util.StringUtils;
import com.objectpartners.cms.util.CmsResourceBundle;

/*
 * ImportFileForm <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th> <th>Date</th>
 * <th>Version</th> <th>Comments</th> </tr> <tr> <td>Thomas Eaton</td> <td>Sep 12, 2005</td>
 * <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 *
 */

public class ImportFileForm extends BaseForm
{
  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  /**
   * The ID of the import file.
   */
  private String importFileId;

  /**
   * The type of the import file.
   */
  private String importFileType;

  /**
   * If the field "importFileId" refers to a hierarchy import file, then the field "hierarchyId"
   * refers to the hierarchy into which the contents of the import file will be inserted. If the
   * field "importFileId" refers to any other type of import file, then the field "hierarchyId" is
   * null.
   */
  private String hierarchyId;

  private String fileImportApprovalTypeCode;

  /**
   * <p>
   * If the field "importFileId" refers to a budget import file, then the field "promotionId" refers
   * to the promotion to which import file's budgets will be bound.
   * </p>
   * <p>
   * If the field "importFileId" refers to a deposit import file, then the field "promotionId"
   * refers to the promotion into which import file's deposits will be inserted.
   * </p>
   * <p>
   * If the field "importFileId" refers to any other type of import file, then the field
   * "promotionId" is null.
   */
  private String promotionId;

  /**
   * <p>
   * This is currently only used for "Budget" imports.  Indicated if budget amount will be
   * replaced or added to. 
   * </p>
   * <p>
   * If the field "importFileId" refers to any other type of import file, then the field
   * "replaceValues" is null.
   */
  private Boolean replaceValues;

  /**
   * <p>
   * This is currently only used for "LeaderBoard" imports.  Indicated if LeaderBoard is
   * updated or deleted. 
   * </p>
   * <p>
   * If actionType is Update "U" then user is active in the database
   * </p>
   * <p>
   * If the field "importFileId" refers to any other type of import file, then the field
   * "action" is null.
   */
  private String actionType;

  /**
   * <p>
   * This is currently only used for "LeaderBoard" imports. Indicated if LeaderBoard is
   * asOfDate (or not).
   * </p>
   * <p>
   * If the field "importFileId" refers to any other type of import file, then the field
   * "asOfDate" is null.
   */
  private String asOfDate = DateUtils.displayDateFormatMask;

  /**
   * <p>
   * This is currently only used for "Badge" imports. I
   * </p>
   * <p>
   * If the field "importFileId" refers to any other type of import file, then the field
   * "earnedDate" is null.
   */
  private String earnedDate = DateUtils.displayDateFormatMask;

  /**
   * <p>
   * This is currently only used for "LeaderBoard" imports. 
   * </p>
   * <p>
   * If the field "importFieldId" refers to any other type of import file, then the field
   * "LeaderBoardId" is null.
   */
  private Long leaderboardId;

  /**
   * <p>
   * This is currently only used for "Badge" imports. 
   * </p>
   * <p>
   * If the field "importFieldId" refers to any other type of import file, then the field
   * "theBadgeId" is null.
   */
  private Long theBadgeId;

  /**
   * <p>
   * This is currently only used for "Budget" imports. 
   * </p>
   * <p>
   * If the field "importFieldId" refers to any other type of import file, then the field
   * "countryId" is null.
   */
  private Long countryId;

  /**
   * <p>
   * If the field "importFileId" refers to a deposit import file whose state is "verified," then the
   * field "messageId" refers to a message stored in the Content Manager that will be sent to all
   * participants who receive awards as a result of importing the import file.
   * </p>
   * <p>
   * In all other cases, the field "messageId" is null.
   * </p>
   */
  private String messageId;

  /**
   * <p>
   * The progress end date refers to the date the goalquest progress load should use.
   * </p>
   */
  private String progressEndDate = DateUtils.displayDateFormatMask;

  /*
   * Specifies the Transaction type for Recognition Deposit Loads
   */
  private String transactionType;

  private Integer roundNumber;

  /**
   * <p>
   * This is currently only used for "Budget" imports. 
   * </p>
   * <p>
   * If the field "importFieldId" refers to any other type of import file, then the field
   * "budgetSegmentId" is null.
   */
  private Long budgetSegmentId;

  private Long budgetMasterId;

  private boolean delayAward;
  private String delayAwardDate = DateUtils.displayDateFormatMask;

  private Long contestId;

  // ---------------------------------------------------------------------------
  // Getter and Setter Methods
  // ---------------------------------------------------------------------------

  /**
   * @return value of fileImportApprovalTypeCode property
   */
  public String getFileImportApprovalTypeCode()
  {
    return fileImportApprovalTypeCode;
  }

  /**
   * Returns the hierarchy ID.
   * 
   * @return the hierarchy ID.
   */
  public String getHierarchyId()
  {
    return hierarchyId;
  }

  /**
   * Returns the import file ID.
   * 
   * @return the import file ID.
   */
  public String getImportFileId()
  {
    return importFileId;
  }

  /**
   * Returns the import file type.
   * 
   * @return the import file type.
   */
  public String getImportFileType()
  {
    return importFileType;
  }

  /**
   * Returns the message ID.
   * 
   * @return the message ID.
   */
  public String getMessageId()
  {
    return messageId;
  }

  /**
   * Returns the promotion ID.
   * 
   * @return the promotion ID.
   */
  public String getPromotionId()
  {
    return promotionId;
  }

  /**
   * Returns the replaceValues.
   * 
   * @return true if budget amounts should be replaced; false if they should be added to.
   */
  public Boolean getReplaceValues()
  {
    return replaceValues;
  }

  /**
   * Returns the action.
   * 
   * @return true if LeaderBoard should be updated; false if they should be deleted.
   */
  public String getActionType()
  {
    return actionType;
  }

  public String getAsOfDate()
  {
    return asOfDate;
  }

  public String getEarnedDate()
  {
    return earnedDate;
  }

  public void setEarnedDate( String earnedDate )
  {
    this.earnedDate = earnedDate;
  }

  /**
   * Returns LeaderBoardId.
   */
  public Long getLeaderBoardId()
  {
    return leaderboardId;
  }

  public Long getTheBadgeId()
  {
    return theBadgeId;
  }

  public void setTheBadgeId( Long theBadgeId )
  {
    this.theBadgeId = theBadgeId;
  }

  public void setCountryId( Long countryId )
  {
    this.countryId = countryId;
  }

  public Long getCountryId()
  {
    return countryId;
  }

  /**
   * @param fileImportApprovalTypeCode value for fileImportApprovalTypeCode property
   */
  public void setFileImportApprovalTypeCode( String fileImportApprovalTypeCode )
  {
    this.fileImportApprovalTypeCode = fileImportApprovalTypeCode;
  }

  /**
   * Sets the hierarchy ID.
   * 
   * @param hierarchyId the hierarchy ID.
   */
  public void setHierarchyId( String hierarchyId )
  {
    this.hierarchyId = hierarchyId;
  }

  /**
   * Sets the import file ID.
   * 
   * @param importFileId the import file ID.
   */
  public void setImportFileId( String importFileId )
  {
    this.importFileId = importFileId;
  }

  /**
   * Sets the import file type.
   * 
   * @param importFileType the import file type.
   */
  public void setImportFileType( String importFileType )
  {
    this.importFileType = importFileType;
  }

  /**
   * Sets the message ID.
   * 
   * @param messageId the message ID.
   */
  public void setMessageId( String messageId )
  {
    this.messageId = messageId;
  }

  /**
   * Sets the promotion ID.
   * 
   * @param promotionId the promotion ID.
   */
  public void setPromotionId( String promotionId )
  {
    this.promotionId = promotionId;
  }

  /**
   * Sets replaceValues.
   * 
   * @param replaceValues true if budget amounts should be replaced; false if they should be added to.
   */
  public void setReplaceValues( Boolean replaceValues )
  {
    this.replaceValues = replaceValues;
  }

  /**
   * Sets the action.
   * 
   * @param action true if LeaderBoard should be updated; false if they should be Deleted.
   */
  public void setactionType( String actionType )
  {
    this.actionType = actionType;
  }

  /**
   * Sets AsOfDate
   * 
   * @param asOfDate the AsOfDate
   */
  public void setAsOfDate( String asOfDate )
  {
    this.asOfDate = asOfDate;
  }

  /**
   * Sets the LeaderBoardId
   * 
   * @param leaderBoardId the LeaderBoardId
   */
  public void setLeaderBoardId( Long leaderBoardId )
  {
    this.leaderboardId = leaderBoardId;
  }

  public Integer getRoundNumber()
  {
    return roundNumber;
  }

  public void setRoundNumber( Integer roundNumber )
  {
    this.roundNumber = roundNumber;
  }

  // ---------------------------------------------------------------------------
  // Validation Methods
  // ---------------------------------------------------------------------------

  /**
   * Validates the data in this form.
   * 
   * @param mapping The mapping used to select this instance
   * @param request The servlet request we are processing
   * @return <code>ActionErrors</code> object that encapsulates any validation errors
   */
  public ActionErrors validate( ActionMapping mapping, HttpServletRequest request )
  {
    ActionErrors actionErrors = super.validate( mapping, request );

    if ( actionErrors == null )
    {
      actionErrors = new ActionErrors();
    }

    Date formatDate = null;
    if ( progressEndDate != null && progressEndDate.length() > 0 )
    {
      formatDate = DateUtils.toDate( progressEndDate );
      if ( !DateUtils.toDisplayString( formatDate ).equals( progressEndDate ) )
      {
        actionErrors.add( "progressEndDate",
                          new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_DATE, CmsResourceBundle.getCmsBundle().getString( "admin.fileload.importFileDetails.PROGRESS_END_DATE" ) ) );
      }
    }

    // Note: This form's importFileId property is validated by the Struts
    // Validator. See validation-admin.xml.

    String path = mapping.getPath();
    if ( path.equalsIgnoreCase( "/verifyImportFile" ) )
    {
      // Validate this form's importFileType property.
      if ( importFileType == null )
      {
        actionErrors.add( ActionErrors.GLOBAL_MESSAGE,
                          new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "admin.fileload.errors.params", "IMPORT_FILE_TYPE" ) ) );
      }
      else
      {
        if ( importFileType.equalsIgnoreCase( ImportFileTypeType.BUDGET ) || importFileType.equalsIgnoreCase( ImportFileTypeType.DEPOSIT )
            || importFileType.equalsIgnoreCase( ImportFileTypeType.GQ_PROGRESS_DATA_LOAD ) || importFileType.equalsIgnoreCase( ImportFileTypeType.GQ_VIN_LOAD )
            || importFileType.equalsIgnoreCase( ImportFileTypeType.CP_PROGRESS_DATA_LOAD ) || importFileType.equalsIgnoreCase( ImportFileTypeType.CP_BASE_DATA_LOAD )
            || importFileType.equalsIgnoreCase( ImportFileTypeType.CP_LEVEL_DATA_LOAD ) || importFileType.equalsIgnoreCase( ImportFileTypeType.AWARD_LEVEL )
            || importFileType.equalsIgnoreCase( ImportFileTypeType.NOMINATION_APPROVER ) )
        {
          // Validate this form's promotionId property.
          if ( promotionId == null || promotionId.length() == 0 )
          {
            actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "admin.fileload.errors.params", "PROMOTION" ) ) );
          }
        }
        else if ( importFileType.equalsIgnoreCase( ImportFileTypeType.HIERARCHY ) || importFileType.equalsIgnoreCase( ImportFileTypeType.PARTICIPANT ) )
        {
          // Validate this form's hierarchyId property.
          if ( hierarchyId == null || hierarchyId.length() == 0 )
          {
            actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "admin.fileload.errors.params", "HIERARCHY" ) ) );
          }
        }

        if ( importFileType.equalsIgnoreCase( ImportFileTypeType.TD_PROGRESS_DATA_LOAD ) )
        {
          if ( promotionId == null || promotionId.equals( "-1" ) )
          {
            actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "admin.fileload.errors.params", "PROMOTION" ) ) );
          }
          if ( this.roundNumber == null || this.roundNumber == -1 )
          {
            actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "promotion.payout.throwdown", "ROUND_NUMBER" ) ) );
          }
          if ( !StringUtils.isEmpty( progressEndDate ) )
          {
            formatDate = DateUtils.toDate( progressEndDate );
            if ( !DateUtils.toDisplayString( formatDate ).equals( progressEndDate ) )
            {
              actionErrors.add( "progressEndDate",
                                new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_DATE, CmsResourceBundle.getCmsBundle().getString( "admin.fileload.importFileDetails.PROGRESS_END_DATE" ) ) );
            }
            else if ( formatDate.after( DateUtils.getCurrentDate() ) )
            {
              actionErrors.add( "progressEndDate",
                                new ActionMessage( "system.errors.DATE_ON_BEFORE", CmsResourceBundle.getCmsBundle().getString( "admin.fileload.importFileDetails.PROGRESS_END_DATE" ) ) );
            }
          }
          else
          {
            actionErrors.add( ActionErrors.GLOBAL_MESSAGE,
                              new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "admin.fileload.importFileDetails.PROGRESS_END_DATE" ) ) );
          }
        }

        if ( importFileType.equalsIgnoreCase( ImportFileTypeType.BUDGET ) )
        {
          if ( promotionId != null && promotionId.length() != 0 )
          {
            if ( budgetSegmentId == null || budgetSegmentId == 0 )
            {
              actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "admin.fileload.budget", "BUDGET_SEGMENT" ) ) );
              request.setAttribute( "promotionId", promotionId );
            }
          }
          if ( countryId == null || countryId == 0 )
          {
            actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "admin.fileload.badgeId", "COUNTRY" ) ) );
          }
        }

        if ( importFileType.equalsIgnoreCase( ImportFileTypeType.BUDGET_DISTRIBUTION ) )
        {
          if ( budgetMasterId != null && budgetMasterId != 0 )
          {
            if ( budgetSegmentId == null || budgetSegmentId == 0 )
            {
              actionErrors.add( ActionErrors.GLOBAL_MESSAGE,
                                new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "admin.fileload.budgetdistribution", "BUDGET_TIME_PERIOD" ) ) );
            }
          }
          else
          {
            actionErrors.add( ActionErrors.GLOBAL_MESSAGE,
                              new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "admin.fileload.budgetdistribution", "BUDGET_MASTER_ID" ) ) );
          }
        }

        // leaderbaord import validations
        if ( importFileType.equalsIgnoreCase( ImportFileTypeType.LEADERBOARD ) )
        {
          if ( leaderboardId == null || leaderboardId == 0 )
          {
            actionErrors.add( ActionErrors.GLOBAL_MESSAGE,
                              new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "admin.fileload.leaderboard", "LEADERBOARD_NAME" ) ) );
          }

          if ( StringUtils.isEmpty( actionType ) )
          {
            actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "admin.fileload.leaderboard", "ACTION" ) ) );
          }

          if ( !StringUtils.isEmpty( asOfDate ) )
          {
            formatDate = DateUtils.toDate( asOfDate );
            if ( !DateUtils.toDisplayString( formatDate ).equals( asOfDate ) )
            {
              actionErrors.add( "asOfDate", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_DATE, CmsResourceBundle.getCmsBundle().getString( "admin.fileload.leaderboard.AS_OF_DATE" ) ) );
            }
          }
          else
          {
            actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "admin.fileload.leaderboard", "AS_OF_DATE" ) ) );
          }
        }

        // leaderbaord import validations
        if ( importFileType.equalsIgnoreCase( ImportFileTypeType.BADGE ) )
        {
          if ( theBadgeId == null || theBadgeId == 0 )
          {
            actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "admin.fileload.badge", "BADGE_NAME" ) ) );
          }

          if ( !StringUtils.isEmpty( earnedDate ) )
          {
            formatDate = DateUtils.toDate( earnedDate );
            if ( !DateUtils.toDisplayString( formatDate ).equals( earnedDate ) )
            {
              actionErrors.add( "earnedDate", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_DATE, CmsResourceBundle.getCmsBundle().getString( "admin.fileload.badge.BADGE_EARNED_DATE" ) ) );
            }
          }
          else
          {
            actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "admin.fileload.badge", "BADGE_EARNED_DATE" ) ) );
          }
        }

        if ( importFileType.equalsIgnoreCase( ImportFileTypeType.GQ_PROGRESS_DATA_LOAD ) || importFileType.equalsIgnoreCase( ImportFileTypeType.GQ_VIN_LOAD ) )
        {
          // Validate this form's progress end date property.
          if ( progressEndDate != null )
          {
            Date convertedProgressEndDate = DateUtils.toDate( progressEndDate );
            if ( convertedProgressEndDate == null )
            {
              actionErrors.add( ActionErrors.GLOBAL_MESSAGE,
                                new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "admin.fileload.importFileDetails", "PROGRESS_END_DATE" ) ) );

            }
          }
        }
        if ( importFileType.equalsIgnoreCase( ImportFileTypeType.DEPOSIT ) || importFileType.equalsIgnoreCase( ImportFileTypeType.AWARD_LEVEL ) )
        {
          if ( delayAward && ( delayAwardDate == null || delayAwardDate.isEmpty() ) )
          {
            actionErrors.add( ActionErrors.GLOBAL_MESSAGE,
                              new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "admin.fileload.importFileDetails", "DELAY_DATE" ) ) );
          }
          if ( promotionId != null && promotionId.length() != 0 )
          {
            Promotion promotion = getPromotionService().getPromotionById( new Long( promotionId ) );
            if ( promotion.isFileLoadEntry() && promotion.isRecognitionPromotion() && ( (RecognitionPromotion)promotion ).isAllowRecognitionSendDate() )
            {
              request.setAttribute( "showDelayRecognition", Boolean.TRUE );
            }
          }
          if ( delayAwardDate != null && !delayAwardDate.isEmpty() && DateUtils.toDate( delayAwardDate ).before( DateUtils.getCurrentDate() ) )
          {
            actionErrors.add( "delayAward", new ActionMessage( "admin.fileload.importFileDetails.DELAY_DATE_VALIDATION" ) );
          }
        }
      }
    }
    else if ( path.equalsIgnoreCase( "/importImportFile" ) )
    {
      // Validate this form's importFileType property.
      if ( importFileType == null )
      {
        actionErrors.add( ActionErrors.GLOBAL_MESSAGE,
                          new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "admin.fileload.errors.params", "IMPORT_FILE_TYPE" ) ) );
      }
      else
      {
        if ( importFileType.equalsIgnoreCase( ImportFileTypeType.DEPOSIT ) )
        {
          if ( StringUtils.isEmpty( transactionType ) )
          {
            actionErrors.add( ActionErrors.GLOBAL_MESSAGE,
                              new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "admin.fileload.errors.params", "TRANSACTION_TYPE" ) ) );
          }
          // Validate this form's messageId property for Deposit Only Load.
          /*
           * else if ( FileImportTransactionType.DEPOSIT_ONLY.equals( transactionType ) ) { if ( (
           * messageId == null ) || ( messageId.length() == 0 ) ) { actionErrors.add(
           * ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.REQUIRED",
           * CmsResourceBundle .getCmsBundle().getString( "admin.fileload.errors.params", "MESSAGE"
           * ) ) ); } }
           */
          // Validate this form's messageId property for Recognition Deposit Load.
          else if ( FileImportTransactionType.CREATE_RECOGNITION.equals( transactionType ) )
          {
            ImportFile recOptionsFile = (ImportFile)request.getSession().getAttribute( "recOptionsImportFile" );
            if ( recOptionsFile == null || recOptionsFile.getSubmitter() == null )
            {
              actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "admin.fileload.errors.params", "SUBMITTER" ) ) );
            }
          }
        }
        // Validate this form's messageId property for Recognition Award Level.
        /*
         * if ( importFileType.equalsIgnoreCase( ImportFileTypeType.AWARD_LEVEL )) { ImportFile
         * recAwardLevel = ( ImportFile ) request.getSession().getAttribute( "recAwardsImportFile" )
         * ; if( recAwardLevel == null || recAwardLevel.getSubmitter() == null ) { actionErrors.add(
         * ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.REQUIRED",
         * CmsResourceBundle .getCmsBundle().getString( "admin.fileload.errors.params", "SUBMITTER"
         * ) ) ); } }
         */
      }
    }

    // Validate this form's messageId property for Deposit Only Load.
    if ( !StringUtils.isEmpty( transactionType ) && FileImportTransactionType.DEPOSIT_ONLY.equals( transactionType ) )
    {
      if ( messageId == null || messageId.length() == 0 )
      {
        actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "admin.fileload.errors.params", "MESSAGE" ) ) );
      }
    }

    return actionErrors;
  }

  private static PromotionService getPromotionService()
  {
    return (PromotionService)BeanLocator.getBean( PromotionService.BEAN_NAME );
  }

  public String getProgressEndDate()
  {
    return progressEndDate;
  }

  public void setProgressEndDate( String progressEndDate )
  {
    this.progressEndDate = progressEndDate;
  }

  public boolean getEnableEmailCopy()
  {
    return ! ( !StringUtils.isEmpty( transactionType ) && FileImportTransactionType.CREATE_RECOGNITION.equals( transactionType ) );
  }

  public boolean getEnableRecoOptions()
  {
    return !StringUtils.isEmpty( transactionType ) && FileImportTransactionType.CREATE_RECOGNITION.equals( transactionType );
  }

  public String getTransactionType()
  {
    return transactionType;
  }

  public void setTransactionType( String transactionType )
  {
    this.transactionType = transactionType;
  }

  public Long getBudgetSegmentId()
  {
    return budgetSegmentId;
  }

  public void setBudgetSegmentId( Long budgetSegmentId )
  {
    this.budgetSegmentId = budgetSegmentId;
  }

  public boolean isDelayAward()
  {
    return delayAward;
  }

  public void setDelayAward( boolean delayAward )
  {
    this.delayAward = delayAward;
  }

  public String getDelayAwardDate()
  {
    return delayAwardDate;
  }

  public void setDelayAwardDate( String delayAwardDate )
  {
    this.delayAwardDate = delayAwardDate;
  }

  public Long getBudgetMasterId()
  {
    return budgetMasterId;
  }

  public void setBudgetMasterId( Long budgetMasterId )
  {
    this.budgetMasterId = budgetMasterId;
  }

  public Long getContestId()
  {
    return contestId;
  }

  public void setContestId( Long contestId )
  {
    this.contestId = contestId;
  }
}
