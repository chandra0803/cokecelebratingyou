
package com.biperf.core.ui.fileload;

import java.util.Date;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.biperf.core.domain.enums.ImportFileTypeType;
import com.biperf.core.domain.multimedia.Card;
import com.biperf.core.ui.BaseForm;
import com.biperf.core.utils.DateUtils;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * RecognitionOptionsForm.
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
 * <td>reddy</td>
 * <td>Dec 10, 2007</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class RecognitionOptionsForm extends BaseForm
{
  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  private String submitterId;
  private String submitterNodeId;

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
  // cards
  public static final int ECARDS_INITIAL_LOAD_INDEX = 9;
  private String method;
  private String returnActionUrl;
  private Boolean flashNeeded;
  private String cardId;
  private Long categoryId;
  private boolean noCardSelected;
  private String pagenationType;
  private Integer electronicCardsBeginIndex = new Integer( 0 );
  private Integer electronicCardsEndIndex = new Integer( 0 );
  private Integer electronicCardsMaxIndex = new Integer( 0 );
  private Card selectedCard;
  private Set electronicCards;
  private Set certificates;

  // ****Conments & Behaviours

  private boolean behaviorActive;
  private String cmPromotionTypeCode;
  private String teamName;
  private String comments;
  private boolean copyManager;

  private String message;
  private String submitterName;

  private String certificateId;
  private String selectedBehavior;

  // ---------------------------------------------------------------------------
  // Getter and Setter Methods
  // ---------------------------------------------------------------------------

  public String getCertificateId()
  {
    return certificateId;
  }

  public void setCertificateId( String certificateId )
  {
    this.certificateId = certificateId;
  }

  public String getSelectedBehavior()
  {
    return selectedBehavior;
  }

  public void setSelectedBehavior( String selectedBehavior )
  {
    this.selectedBehavior = selectedBehavior;
  }

  public String getComments()
  {
    return comments;
  }

  public void setComments( String comments )
  {
    this.comments = comments;
  }

  public String getTeamName()
  {
    return teamName;
  }

  public void setTeamName( String teamName )
  {
    this.teamName = teamName;
  }

  public String getCmPromotionTypeCode()
  {
    return cmPromotionTypeCode;
  }

  public void setCmPromotionTypeCode( String cmPromotionTypeCode )
  {
    this.cmPromotionTypeCode = cmPromotionTypeCode;
  }

  public boolean isBehaviorActive()
  {
    return behaviorActive;
  }

  public void setBehaviorActive( boolean behaviorActive )
  {
    this.behaviorActive = behaviorActive;
  }

  public String getCardId()
  {
    return cardId;
  }

  public void setCardId( String cardId )
  {
    this.cardId = cardId;
  }

  public Long getCategoryId()
  {
    return categoryId;
  }

  public void setCategoryId( Long categoryId )
  {
    this.categoryId = categoryId;
  }

  public Integer getElectronicCardsBeginIndex()
  {
    return electronicCardsBeginIndex;
  }

  public void setElectronicCardsBeginIndex( Integer electronicCardsBeginIndex )
  {
    this.electronicCardsBeginIndex = electronicCardsBeginIndex;
  }

  public Integer getElectronicCardsEndIndex()
  {
    return electronicCardsEndIndex;
  }

  public void setElectronicCardsEndIndex( Integer electronicCardsEndIndex )
  {
    this.electronicCardsEndIndex = electronicCardsEndIndex;
  }

  public Integer getElectronicCardsMaxIndex()
  {
    return electronicCardsMaxIndex;
  }

  public void setElectronicCardsMaxIndex( Integer electronicCardsMaxIndex )
  {
    this.electronicCardsMaxIndex = electronicCardsMaxIndex;
  }

  public Boolean getFlashNeeded()
  {
    return flashNeeded;
  }

  public void setFlashNeeded( Boolean flashNeeded )
  {
    this.flashNeeded = flashNeeded;
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public boolean isNoCardSelected()
  {
    return noCardSelected;
  }

  public void setNoCardSelected( boolean noCardSelected )
  {
    this.noCardSelected = noCardSelected;
  }

  public String getPagenationType()
  {
    return pagenationType;
  }

  public void setPagenationType( String pagenationType )
  {
    this.pagenationType = pagenationType;
  }

  public String getReturnActionUrl()
  {
    return returnActionUrl;
  }

  public void setReturnActionUrl( String returnActionUrl )
  {
    this.returnActionUrl = returnActionUrl;
  }

  public Card getSelectedCard()
  {
    return selectedCard;
  }

  public void setSelectedCard( Card selectedCard )
  {
    this.selectedCard = selectedCard;
  }

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

    if ( comments != null && comments.length() > 4000 )
    {
      actionErrors.add( "comments", new ActionMessage( "promotion.basics.errors.COMMENTS_ERROR" ) );
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
            || importFileType.equalsIgnoreCase( ImportFileTypeType.GQ_PROGRESS_DATA_LOAD ) || importFileType.equalsIgnoreCase( ImportFileTypeType.GQ_VIN_LOAD ) )
        {
          // Validate this form's promotionId property.
          if ( promotionId == null || promotionId.length() == 0 )
          {
            actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "admin.fileload.errors.params", "PROMOTION" ) ) );
          }
        }
        else if ( importFileType.equalsIgnoreCase( ImportFileTypeType.HIERARCHY ) )
        {
          // Validate this form's hierarchyId property.
          if ( hierarchyId == null || hierarchyId.length() == 0 )
          {
            actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "admin.fileload.errors.params", "HIERARCHY" ) ) );
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
          // Validate this form's messageId property.
          if ( messageId == null || messageId.length() == 0 )
          {
            actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "admin.fileload.errors.params", "MESSAGE" ) ) );
          }
        }
      }
    }

    return actionErrors;
  }

  public String getProgressEndDate()
  {
    return progressEndDate;
  }

  public void setProgressEndDate( String progressEndDate )
  {
    this.progressEndDate = progressEndDate;
  }

  public Set getCertificates()
  {
    return certificates;
  }

  public void setCertificates( Set certificates )
  {
    this.certificates = certificates;
  }

  public Set getElectronicCards()
  {
    return electronicCards;
  }

  public void setElectronicCards( Set electronicCards )
  {
    this.electronicCards = electronicCards;
  }

  public String getParticipantId()
  {
    return submitterId;
  }

  public void setParticipantId( String submitterId )
  {
    this.submitterId = submitterId;
  }

  public String getSubmitterId()
  {
    return submitterId;
  }

  public void setSubmitterId( String submitterId )
  {
    this.submitterId = submitterId;
  }

  public String getSubmitterNodeId()
  {
    return submitterNodeId;
  }

  public void setSubmitterNodeId( String submitterNodeId )
  {
    this.submitterNodeId = submitterNodeId;
  }

  public String getMessage()
  {
    return message;
  }

  public void setMessage( String message )
  {
    this.message = message;
  }

  public String getSubmitterName()
  {
    return submitterName;
  }

  public void setSubmitterName( String submitterName )
  {
    this.submitterName = submitterName;
  }

  public boolean isCopyManager()
  {
    return copyManager;
  }

  public void setCopyManager( boolean copyManager )
  {
    this.copyManager = copyManager;
  }
}
