/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/fileload/ImportFile.java,v $
 */

package com.biperf.core.domain.fileload;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.enums.FileImportApprovalType;
import com.biperf.core.domain.enums.ImportFileStatusType;
import com.biperf.core.domain.enums.ImportFileTypeType;
import com.biperf.core.domain.hierarchy.Hierarchy;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.message.Message;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.Promotion;

/**
 * ImportFile domain object which represents file within the Beacon application.
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
 * <td>sedey</td>
 * <td>June 2, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ImportFile implements Serializable
{

  private static final long serialVersionUID = 3256726164994536240L;
  public static final String UPDATE = "U";
  public static final String DELETE = "D";

  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  /**
   * Uniquely identifies this import file.
   */
  private Long id;

  /**
   * Uniquely identifies the current version of this import file.
   */
  private Long version;

  /**
   * The name of this import file.
   */
  private String fileName;

  /**
   * The type of this import file.
   */
  private ImportFileTypeType fileType;

  /**
   * The status of this import file.
   */
  private ImportFileStatusType status;

  /**
   * The budget import records contained by this import file.
   */
  private Set budgetImportRecords = new LinkedHashSet();

  /**
   * The leaderBoard import records contained by this import file.
   */
  private Set leaderBoardImportRecords = new LinkedHashSet();

  /**
   * The badge import records contained by this import file.
   */
  private Set badgeImportRecords = new LinkedHashSet();

  /**
   * The deposit import records contained by this import file.
   */
  private Set depositImportRecords = new LinkedHashSet();

  /**
   * The hierarchy import records contained by this import file.
   */
  private Set hierarchyImportRecords = new LinkedHashSet();

  /**
   * The participant import records contained by this import file.
   */
  private Set participantImportRecords = new LinkedHashSet();

  /**
   * The product import records contained by this import file.
   */
  private Set productImportRecords = new LinkedHashSet();

  /**
   * The product claim import records contained by this import file.
   */
  private Set productClaimImportRecords = new LinkedHashSet();

  /**
   * The quiz import records contained by this import file.
   */
  private Set quizImportRecords = new LinkedHashSet();

  /**
   * The Goal Quest Pax Base import records contained by this import file. 
   */
  private Set paxBaseImportRecords = new LinkedHashSet();

  /**
   * The Goal Quest Pax Goal import records contained by this import file. 
   */
  private Set paxGoalImportRecords = new LinkedHashSet();

  /**
   * The Goal Quest Progress import records contained by this import file. 
   */
  private Set progressImportRecords = new LinkedHashSet();

  /**
   * The Goal Quest Auto VINs import records contained by this import file. 
   */
  private Set vinImportRecords = new LinkedHashSet();

  /**
   * The Challengepoint Pax Base import records contained by this import file. 
   */
  private Set paxCPBaseImportRecords = new LinkedHashSet();

  /**
   * The Challengepoint Pax Level import records contained by this import file. 
   */
  private Set paxCPLevelImportRecords = new LinkedHashSet();

  /**
   * The Challengepoint Progress import records contained by this import file. 
   */
  private Set progressCPImportRecords = new LinkedHashSet();

  /**
   * The ThankQonline award level import records contained by this import file. 
   */
  private Set awardLevelImportRecords = new LinkedHashSet();

  /**
   * The Throwdown Progress import records contained by this import file. 
   */
  private Set progressTDImportRecords = new LinkedHashSet();

  /**
   * The budget distribution import records contained by this import file.
   */
  private Set budgetDistributionImportRecords = new LinkedHashSet();
  /**
   * The ssi contest progress import records contained by this import file.
   */
  private Set ssiProgressImportRecords = new LinkedHashSet();

  private Set nominationApproverImportRecords = new LinkedHashSet();

  /**
   * The number of import records contained by this import file.
   */
  private int importRecordCount;

  /**
   * Errors that occurred while verifying the import records contained by this import file.
   */
  private Set importRecordErrors = new LinkedHashSet();

  /**
   * The number of import records with errors contained by this import file.
   */
  private int importRecordErrorCount;

  /**
   * If this is an hierarchy import file, then the hierarchy field (this field) refers to the
   * hierarchy into which nodes based on this import file's import records will be inserted. If this
   * is not an hierarchy import file, then the hierarchy field is null.
   */
  private Hierarchy hierarchy;

  /**
   * If this is an LeaderBoard import file, then the LeaderBoard field (this field) refers to the 
   * LeaderBoard. If this is not an LeaderBoard import file, then the LeaderBoard field is null.
   */
  private Long leaderboardId;

  /**
  * If this is an Badge import file, then the LeaderBoard field (this field) refers to the 
  * Badge. If this is not an LeaderBoard import file, then the Badge field is null.
  */
  private Long badgepromotionId;

  /**
   * If this is an LeaderBoard import file, then the LeaderBoardName field (this field) refers to the 
   * LeaderBoard. If this is not an LeaderBoard import file, then the LeaderBoardName field is null.
   */
  private String leaderBoardName;

  /**
   * If this is an Badge import file, then the badgeName field (this field) refers to the 
   * Badge. If this is not an Badge import file, then the badgeName field is null.
   */
  private String badgeName;

  /**
   * If this is a deposit import file, then the message field (this field) refers to the email
   * message that is sent to participants when the contents of this import file are imported. If
   * this is not a deposit import file, then the message field is null.
   */
  private Message message;

  /**
   * If this is a budget import file, then the promotion field (this field) refers to the live
   * master promotion with budgets into which budgets based on this import file's import records will
   * be inserted.
   *
   * If this is a deposit import file, then the promotion field refers to the live promotion into
   * which deposits based on the import file's import records will be inserted.
   *
   * If this is a survey import file, then the promotion field refers to the live survey promotion
   * for which sweepstakes activities based on the import file's import records will be created.
   * 
   * If this is a Goal Quest import file (Base/Progress/Goal/VIN load), then the promotion field refers to
   * the live goalquest promotion for which base objective/pax performance progress/pax goal level or auto transactions
   * based on the import file's import records will be inserted/updated.
   *
   * If this is not a budget, deposit, survey or goalquest import file, then the promotion field is null.
   */
  private Promotion promotion;

  /**
   * If this is a budget import file, then the replaceValues field (this field) indicates
   * if the budget amount should be replaced (if true) or added to (if false).
   */
  private Boolean replaceValues;

  /**
   * If this is a budget import file, then the country field (this field) indicates
   * what country the budget amounts in the file originate from.
   */
  private Country country;

  /**
   * If this is a LeaderBoard import file, then the Action field indicates
   * if the leaderBoard score should be Updated(if true) or deleted (if false).
   */
  private String actionType;

  /**
   * As per asOfDate, LeaderBoard can be viewed.
   */
  private Date asOfDate;

  /**
   * As per badge earnedDate 
   */
  private Date earnedDate;

  /**
   * progressEndDate is the end date of the progress load
   */
  private Date progressEndDate;

  /**
   * Value true specifies a recognition deposit 
   */
  private Boolean recognitionDeposit;

  /**
   * The e-card to be sent to each claim recipient.
   */
  private Long card;

  /**
   * The submitter's comments.
   */
  private String submitterComments;

  /**
   * Recognition deposit submitter.
   */
  private Participant submitter;

  /**
   * Recognition deposit submitter node.
   */
  private Node submitterNode;

  /**
   * 
   */
  private FileImportApprovalType fileImportApprovalType;

  /**
   * The username of the user who staged this import file. All import files are staged.
   */
  private String stagedBy;

  /**
   * The time at which this import file was staged. All import files are staged.
   */
  private Date dateStaged;

  /**
   * The username of the user who initiated the import file verify process, or null if this import
   * file has not been verified.
   */
  private String verifiedBy;

  /**
   * The time at which this import file was verified, or null if this import file has not been
   * verified.
   */
  private Date dateVerified;

  /**
   * The username of the user who initiated the import file import process, or null if this import
   * file has not been imported.
   */
  private String importedBy;

  private Integer roundNumber;

  /**
   * The time at which this import file was imported, or null if this import file has not been
   * imported.
   */
  private Date dateImported;

  private List recordsWithoutErrorsByPage = new ArrayList();

  private List recordsWithErrorsByPage = new ArrayList();

  private Long certificateId;
  private String behavior;

  /**
   * The copy to manager to be sent to each claim recipient.
   */
  private boolean copyManager;

  /**
   * If this is a Budget import file, then the Budget Segment field (this field) refers to the 
   * Budget Segment for the budget master from the promotion. If this is not an Budget import file, 
   * then the Budget Segment field is null.
   */
  private Long budgetSegmentId;

  /**
   * If this is an Budget import file, then the Budget Segment Name field (this field) refers to the 
   * Budget Segment. If this is not an Budget import file, then the Budget Segment Name field is null.
   */
  private String budgetSegmentName;

  private Date delayAwardDate;

  private Long budgetMasterId;

  private String budgetMasterName;

  private Long contestId;

  // ---------------------------------------------------------------------------
  // Getter and Setter Methods
  // ---------------------------------------------------------------------------

  public Long getCertificateId()
  {
    return certificateId;
  }

  public void setCertificateId( Long certificateId )
  {
    this.certificateId = certificateId;
  }

  public String getBehavior()
  {
    return behavior;
  }

  public void setBehavior( String behavior )
  {
    this.behavior = behavior;
  }

  /**
   * @return List the Records with errors by page contained by this import file
   */
  public List getRecordsWithErrorsByPage()
  {
    return recordsWithErrorsByPage;
  }

  /** 
   * Sets the Records with errors by page contained by this import file
   * @param recordsWithErrorsByPage
   */
  public void setRecordsWithErrorsByPage( List recordsWithErrorsByPage )
  {
    this.recordsWithErrorsByPage = recordsWithErrorsByPage;
  }

  /**
   * @return List the Records without errors by page contained by this import file
   */
  public List getRecordsWithoutErrorsByPage()
  {
    return recordsWithoutErrorsByPage;
  }

  /** 
   * Sets the Records without errors by page contained by this import file
   * @param recordsWithoutErrorsByPage
   */
  public void setRecordsWithoutErrorsByPage( List recordsWithoutErrorsByPage )
  {
    this.recordsWithoutErrorsByPage = recordsWithoutErrorsByPage;
  }

  /**
   * Adds an import record error to the import import and the import record.
   * 
   * @param importRecordError
   * @param importRecord
   */
  public void addImportRecordError( ImportRecordError importRecordError, ImportRecord importRecord )
  {
    importRecordError.setImportFileId( id );
    importRecordError.setImportRecordId( importRecord.getId() );

    importRecordErrors.add( importRecordError );
    if ( importRecord.getImportRecordErrors().size() == 0 )
    {
      importRecordErrorCount++;
    }

    importRecord.getImportRecordErrors().add( importRecordError );
  }

  public void addBudgetImportRecord( BudgetImportRecord importRecord )
  {
    importRecord.setImportFileId( id );
    budgetImportRecords.add( importRecord );
    importRecordCount++;
  }

  public void addSSIProgressImportRecord( SSIProgressImportRecord importRecord )
  {
    importRecord.setImportFileId( id );
    ssiProgressImportRecords.add( importRecord );
    importRecordCount++;
  }

  public void addBudgetDistributionImportRecord( BudgetDistributionImportRecord importRecord )
  {
    importRecord.setImportFileId( id );
    budgetDistributionImportRecords.add( importRecord );
    importRecordCount++;
  }

  public void addLeaderBoardImportRecord( LeaderBoardImportRecord importRecord )
  {
    importRecord.setImportFileId( id );
    leaderBoardImportRecords.add( importRecord );
    importRecordCount++;
  }

  public void addBadgeImportRecord( BadgeImportRecord importRecord )
  {
    importRecord.setImportFileId( id );
    badgeImportRecords.add( importRecord );
    importRecordCount++;
  }

  public void addDepositImportRecord( DepositImportRecord importRecord )
  {
    importRecord.setImportFileId( id );
    depositImportRecords.add( importRecord );
    importRecordCount++;
  }

  public void addHierarchyImportRecord( HierarchyImportRecord importRecord )
  {
    importRecord.setImportFileId( id );
    hierarchyImportRecords.add( importRecord );
    importRecordCount++;
  }

  public void addParticipantImportRecord( ParticipantImportRecord importRecord )
  {
    importRecord.setImportFileId( id );
    participantImportRecords.add( importRecord );
    importRecordCount++;
  }

  public void addProductClaimImportRecord( ProductClaimImportRecord importRecord )
  {
    importRecord.setImportFileId( id );
    productClaimImportRecords.add( importRecord );
    importRecordCount++;
  }

  public void addProductImportRecord( ProductImportRecord importRecord )
  {
    importRecord.setImportFileId( id );
    productImportRecords.add( importRecord );
    importRecordCount++;
  }

  public void addQuizImportRecord( QuizImportRecord importRecord )
  {
    importRecord.setImportFileId( id );
    quizImportRecords.add( importRecord );
    importRecordCount++;
  }

  public void addPaxBaseImportRecord( PaxBaseImportRecord importRecord )
  {
    importRecord.setImportFileId( id );
    paxBaseImportRecords.add( importRecord );
    importRecordCount++;
  }

  public void addPaxGoalImportRecord( PaxGoalImportRecord importRecord )
  {
    importRecord.setImportFileId( id );
    paxGoalImportRecords.add( importRecord );
    importRecordCount++;
  }

  public void addPaxProgressImportRecord( GoalQuestProgressImportRecord importRecord )
  {
    importRecord.setImportFileId( id );
    progressImportRecords.add( importRecord );
    importRecordCount++;
  }

  public void addAutoVINImportRecord( AutoVinImportRecord importRecord )
  {
    importRecord.setImportFileId( id );
    vinImportRecords.add( importRecord );
    importRecordCount++;
  }

  public void addAwardLevelImportRecord( AwardLevelImportRecord importRecord )
  {
    importRecord.setImportFileId( id );
    awardLevelImportRecords.add( importRecord );
    importRecordCount++;
  }

  // ThrowdownProgressLoad
  public void addProgressTDImportRecords( ThrowdownProgressImportRecord importRecord )
  {
    importRecord.setImportFileId( id );
    progressTDImportRecords.add( importRecord );
    importRecordCount++;
  }

  /**
   * Returns the budget import records contained by this import file.
   * 
   * @return the budget import records contained by this import file.
   */
  public Set getBudgetImportRecords()
  {
    return budgetImportRecords;
  }

  /**
   * Returns the LeaderBoard import records contained by this import file.
   * 
   * @return the LeaderBoard import records contained by this import file.
   */
  public Set getLeaderBoardImportRecords()
  {
    return leaderBoardImportRecords;
  }

  /**
   * Returns the Badge import records contained by this import file.
   * 
   * @return the Badge import records contained by this import file.
   */

  public Set getBadgeImportRecords()
  {
    return badgeImportRecords;
  }

  /**
   * Returns the time at which this import file was imported, or null if this import file has not
   * been imported.
   * 
   * @return the time at which this import file was imported, or null if this import file has not
   *         been verified.
   */
  public Date getDateImported()
  {
    return dateImported;
  }

  /**
   * Returns the time at which this import file was staged.
   * 
   * @return the time at which this import file was staged.
   */
  public Date getDateStaged()
  {
    return dateStaged;
  }

  /**
   * Returns the date that the status of this import file last changed.
   * 
   * @return the date that the status of this import file last changed.
   */
  public Date getDateStatusChanged()
  {
    Date dateStatusChanged = null;

    if ( status.getCode().equalsIgnoreCase( ImportFileStatusType.STAGED ) )
    {
      dateStatusChanged = getDateStaged();
    }
    else if ( status.getCode().equalsIgnoreCase( ImportFileStatusType.STAGE_FAILED ) )
    {
      dateStatusChanged = getDateStaged();
    }
    else if ( status.getCode().equalsIgnoreCase( ImportFileStatusType.VERIFIED ) )
    {
      dateStatusChanged = getDateVerified();
    }
    else if ( status.getCode().equalsIgnoreCase( ImportFileStatusType.VERIFY_FAILED ) )
    {
      dateStatusChanged = getDateVerified();
    }
    else if ( status.getCode().equalsIgnoreCase( ImportFileStatusType.IMPORTED ) )
    {
      dateStatusChanged = getDateImported();
    }
    else if ( status.getCode().equalsIgnoreCase( ImportFileStatusType.IMPORT_FAILED ) )
    {
      dateStatusChanged = getDateImported();
    }

    return dateStatusChanged;
  }

  /**
   * Returns the time at which this import file was verified, or null if this import file has not
   * been verified.
   * 
   * @return the time at which this import file was verified, or null if this import file has not
   *         been verified.
   */
  public Date getDateVerified()
  {
    return dateVerified;
  }

  /**
   * Returns the deposit import records contained by this import file.
   * 
   * @return the deposit import records contained by this import file.
   */
  public Set getDepositImportRecords()
  {
    return depositImportRecords;
  }

  /**
   * Returns the name of this import file.
   * 
   * @return the name of this import file.
   */
  public String getFileName()
  {
    return fileName;
  }

  /**
   * Returns the type of this import file.
   * 
   * @return the type of this import file.
   */
  public ImportFileTypeType getFileType()
  {
    return fileType;
  }

  /**
   * Returns the hierarchy in which nodes based on this import file will be inserted.
   * 
   * @return the hierarchy associated with this import file.
   */
  public Hierarchy getHierarchy()
  {
    return hierarchy;
  }

  /**
   * Returns the hierarchy import records contained by this import file.
   * 
   * @return the hierarchy import records contained by this import file.
   */
  public Set getHierarchyImportRecords()
  {
    return hierarchyImportRecords;
  }

  /**
   * Returns this import file's ID.
   * 
   * @return this ID for this import file.
   */
  public Long getId()
  {
    return id;
  }

  /**
   * Returns username of the user who initiated the import file import process, or null if this
   * import file has not been imported.
   * 
   * @return username of the user who initiated the import file import process, or null if this
   *         import file has not been imported.
   */
  public String getImportedBy()
  {
    return importedBy;
  }

  public Integer getRoundNumber()
  {
    return roundNumber;
  }

  public void setRoundNumber( Integer roundNumber )
  {
    this.roundNumber = roundNumber;
  }

  /**
   * Returns the number of import records.
   * 
   * @return the number of import records.
   */
  public int getImportRecordCount()
  {
    return importRecordCount;
  }

  /**
   * Returns the number of import records with errors.
   * 
   * @return the number of import record with errors.
   */
  public int getImportRecordErrorCount()
  {
    return importRecordErrorCount;
  }

  /**
   * Returns the errors that occurred while verifying the import records contained by this import
   * file.
   * 
   * @return the errors that occurred while verifying the import records contained by this import
   *         file, as a <code>List</code> of {@link ImportRecordError} objects.
   */
  public Set getImportRecordErrors()
  {
    return importRecordErrors;
  }

  /**
   * Returns the import records contained by this import file - will return the correct collection
   * (participant, budget, etc) based on the fileType.
   * 
   * @return the import records contained by this import file, as a <code>Set</code> of
   *         {@link ImportRecord} objects, or null if the file type of the import file is not set.
   */
  public Set getImportRecords()
  {
    Set importRecords = null;

    if ( fileType.isBudget() )
    {
      importRecords = budgetImportRecords;
    }
    else if ( fileType.isDeposit() )
    {
      importRecords = depositImportRecords;
    }
    else if ( fileType.isLeaderBoard() )
    {
      importRecords = leaderBoardImportRecords;
    }
    else if ( fileType.isBadge() )
    {
      importRecords = badgeImportRecords;
    }
    else if ( fileType.isHierarchy() )
    {
      importRecords = hierarchyImportRecords;
    }
    else if ( fileType.isParticipant() )
    {
      importRecords = participantImportRecords;
    }
    else if ( fileType.isProduct() )
    {
      importRecords = productImportRecords;
    }
    else if ( fileType.isProductClaim() )
    {
      importRecords = productClaimImportRecords;
    }
    else if ( fileType.isQuiz() )
    {
      importRecords = quizImportRecords;
    }
    else if ( fileType.isGQPaxBase() )
    {
      importRecords = paxBaseImportRecords;
    }
    else if ( fileType.isGQPaxGoal() )
    {
      importRecords = paxGoalImportRecords;
    }
    else if ( fileType.isGQProgress() )
    {
      importRecords = progressImportRecords;
    }
    else if ( fileType.isGQVin() )
    {
      importRecords = vinImportRecords;
    }
    else if ( fileType.isAwardLevel() )
    {
      importRecords = awardLevelImportRecords;
    }
    else if ( fileType.isTDProgress() )
    {
      importRecords = progressTDImportRecords;
    }
    else if ( fileType.isBudgetRedistribution() )
    {
      importRecords = budgetDistributionImportRecords;
    }

    return importRecords;
  }

  public List getImportRecordsAsList()
  {
    return new ArrayList( getImportRecords() );
  }

  /**
   * Returns only the import records with errors contained by this import file.
   * 
   * @return a set of import records that contain errors, as a <code>Set</code> of
   *         {@link ImportRecord} objects, or null if the file type of the import file is not set.
   */
  public Set getImportRecordsWithErrors()
  {
    Set importRecordsWithErrors = null;

    Set importRecords = getImportRecords();
    if ( importRecords != null )
    {
      importRecordsWithErrors = new LinkedHashSet();

      Iterator iter = importRecords.iterator();
      while ( iter.hasNext() )
      {
        ImportRecord importRecord = (ImportRecord)iter.next();
        if ( importRecord.getImportRecordErrors().size() > 0 )
        {
          importRecordsWithErrors.add( importRecord );
        }
      }
    }

    return importRecordsWithErrors;
  }

  public List getImportRecordsWithErrorsAsList()
  {
    return new ArrayList( getImportRecordsWithErrors() );
  }

  /**
   * Returns only the import records with out errors contained by this import file.
   * 
   * @return a set of import records that contain no errors, as a <code>Set</code> of
   *         {@link ImportRecord} objects, or null if the file type of the import file is not set.
   */
  public Set getImportRecordsWithOutErrors()
  {
    Set importRecordsWithOutErrors = null;

    Set importRecords = getImportRecords();
    if ( importRecords != null )
    {
      importRecordsWithOutErrors = new LinkedHashSet();

      Iterator iter = importRecords.iterator();
      while ( iter.hasNext() )
      {
        ImportRecord importRecord = (ImportRecord)iter.next();
        if ( importRecord.getImportRecordErrors().isEmpty() )
        {
          importRecordsWithOutErrors.add( importRecord );
        }
      }
    }

    return importRecordsWithOutErrors;
  }

  public List getImportRecordsWithOutErrorsAsList()
  {
    return new ArrayList( getImportRecordsWithOutErrors() );
  }

  /**
   * Returns the email message sent to participants when a deposit import file is imported.
   * 
   * @return the email message sent to participants when a deposit import file is imported.
   */
  public Message getMessage()
  {
    return message;
  }

  /**
   * Returns the participant import records contained by this import file.
   * 
   * @return the participant import records contained by this import file.
   */
  public Set getParticipantImportRecords()
  {
    return participantImportRecords;
  }

  /**
   * Returns the product import records contained by this import file.
   * 
   * @return the product import records contained by this import file.
   */
  public Set getProductImportRecords()
  {
    return productImportRecords;
  }

  /**
   * If this is a budget import file, then this method returns the promotion into which budgets
   * based on this import file will be inserted. If this is a deposit import file, then this method
   * returns the promotion into which deposits based on the import file will be inserted. If this is
   * a pax base, pax goal, progress or VIN import file, then this method returns the goalquest
   * promotion into which the base objective, goal level, performance progress, and auto transactions
   * based on the import file will be inserted/updated. If this is any other type of import file, 
   * this method returns null.
   * 
   * @return the promotion associated with this import file.
   */
  public Promotion getPromotion()
  {
    return promotion;
  }

  /**
   * Returns username of the user who staged this import file.
   * 
   * @return username of the user who staged this import file.
   */
  public String getStagedBy()
  {
    return stagedBy;
  }

  /**
   * Returns the status of this import file.
   * 
   * @return the status of this import file.
   */
  public ImportFileStatusType getStatus()
  {
    return status;
  }

  /**
   * Returns the username of the user who last changed the status of this import file.
   * 
   * @return the username of the user who last changed the status of this import file.
   */
  public String getStatusChangedBy()
  {
    String statusChangedBy = null;

    if ( status.getCode().equalsIgnoreCase( ImportFileStatusType.STAGED ) )
    {
      statusChangedBy = stagedBy;
    }
    else if ( status.getCode().equalsIgnoreCase( ImportFileStatusType.VERIFIED ) )
    {
      statusChangedBy = verifiedBy;
    }
    else if ( status.getCode().equalsIgnoreCase( ImportFileStatusType.IMPORTED ) )
    {
      statusChangedBy = importedBy;
    }

    return statusChangedBy;
  }

  /**
   * Returns the total award amount on import records in this import file.
   * 
   * @return the total award amount on import records in this import file.
   */
  public int getTotalAwardAmount()
  {
    int totalAwardAmount = 0;

    if ( depositImportRecords != null )
    {
      Iterator iter = depositImportRecords.iterator();
      while ( iter.hasNext() )
      {
        DepositImportRecord depositImportRecord = (DepositImportRecord)iter.next();

        BigDecimal awardAmount = depositImportRecord.getAwardAmount();
        if ( awardAmount != null )
        {
          totalAwardAmount += awardAmount.intValue();
        }
      }
    }

    return totalAwardAmount;
  }

  /**
   * Returns the total budget amount on import records in this import file.
   * 
   * @return the total budget amount on import records in this import file.
   */
  public int getTotalBudgetAmount()
  {
    int totalBudgetAmount = 0;

    if ( budgetImportRecords != null )
    {
      Iterator iter = budgetImportRecords.iterator();
      while ( iter.hasNext() )
      {
        BudgetImportRecord budgetImportRecord = (BudgetImportRecord)iter.next();

        BigDecimal budgetAmount = budgetImportRecord.getBudgetAmount();
        if ( budgetAmount != null )
        {
          totalBudgetAmount += budgetAmount.intValue();
        }
      }
    }

    return totalBudgetAmount;
  }

  /**
   * Returns the total budget distribution amount on import records in this import file.
   * 
   * @return the total budget distribution amount on import records in this import file.
   */
  public int getTotalBudgetDistributionAmount()
  {
    int totalBudgetDistributionAmount = 0;

    if ( budgetDistributionImportRecords != null )
    {
      Iterator iter = budgetDistributionImportRecords.iterator();
      while ( iter.hasNext() )
      {
        BudgetDistributionImportRecord budgetDistributionImportRecord = (BudgetDistributionImportRecord)iter.next();

        BigDecimal budgetAmount = budgetDistributionImportRecord.getSrcBudgetAmount();
        if ( budgetAmount != null )
        {
          totalBudgetDistributionAmount += budgetAmount.intValue();
        }
      }
    }

    return totalBudgetDistributionAmount;
  }

  /**
   * Returns the number of import records associated with no import error records.
   * 
   * @return the number of import records associated with no import error records.
   */
  public int getValidImportRecordCount()
  {
    int invalidImportRecordCount = 0;

    Set importRecords = getImportRecords();
    if ( importRecords != null )
    {
      Iterator iter = importRecords.iterator();
      while ( iter.hasNext() )
      {
        ImportRecord importRecord = (ImportRecord)iter.next();
        if ( importRecord.getImportRecordErrors().size() > 0 )
        {
          invalidImportRecordCount++;
        }
      }
    }

    return getImportRecordCount() - invalidImportRecordCount;
  }

  /**
   * Returns the user name of the user who initiated the import file verify process, or null if this
   * import file has not been verified.
   * 
   * @return the user name of the user who initiated the import file verify process, or null if this
   *         import file has not been verified.
   */
  public String getVerifiedBy()
  {
    return verifiedBy;
  }

  /**
   * Returns the version number of this import file.
   * 
   * @return the version number of this import file.
   */
  public Long getVersion()
  {
    return version;
  }

  /**
   * Sets the budget import records contained by this import file.
   * 
   * @param budgetImportRecords the budget import records contained by this import file.
   */
  public void setBudgetImportRecords( Set budgetImportRecords )
  {
    this.budgetImportRecords = budgetImportRecords;
  }

  /**
   * Sets the leaderBoard import records contained by this import file.
   * 
   * @param leaderBoardImportRecords the leaderBoard import records contained by this import file.
   */
  public void setLeaderBoardImportRecords( Set leaderBoardImportRecords )
  {
    this.leaderBoardImportRecords = leaderBoardImportRecords;
  }

  /**
   * Sets the badge import records contained by this import file.
   * 
   * @param badgeImportRecords the badge import records contained by this import file.
   */

  public void setBadgeImportRecords( Set badgeImportRecords )
  {
    this.badgeImportRecords = badgeImportRecords;
  }

  /**
   * Sets the time at which this import file was imported, or null if this import file has not been
   * imported.
   * 
   * @param dateImported the time at which this import file was imported, or null if this import
   *          file has not been imported.
   */
  public void setDateImported( Date dateImported )
  {
    this.dateImported = dateImported;
  }

  /**
   * Returns the time at which this import file was staged.
   * 
   * @param dateStaged the time at which this import file was staged.
   */
  public void setDateStaged( Date dateStaged )
  {
    this.dateStaged = dateStaged;
  }

  /**
   * Sets the time at which this import file was verified, or null if this import file has not been
   * verified.
   * 
   * @param dateVerified the time at which this import file was verified, or null if this import
   *          file has not been verified.
   */
  public void setDateVerified( Date dateVerified )
  {
    this.dateVerified = dateVerified;
  }

  /**
   * Sets the deposit import records contained by this import file.
   * 
   * @param depositImportRecords the deposit import records contained by this import file.
   */
  public void setDepositImportRecords( Set depositImportRecords )
  {
    this.depositImportRecords = depositImportRecords;
  }

  /**
   * Sets the name of this import file.
   * 
   * @param fileName the name of this import file.
   */
  public void setFileName( String fileName )
  {
    this.fileName = fileName;
  }

  /**
   * Sets the type of this import file.
   * 
   * @param fileType the type of this import file.
   */
  public void setFileType( ImportFileTypeType fileType )
  {
    this.fileType = fileType;
  }

  /**
   * Sets the hierarchy in which nodes based on this import file will be inserted.
   * 
   * @param hierarchy the hierarchy associated with this import file.
   */
  public void setHierarchy( Hierarchy hierarchy )
  {
    this.hierarchy = hierarchy;
  }

  /**
   * Sets the hierarchy import records contained by this import file.
   * 
   * @param hierarchyImportRecords the hierarchy import records contained by this import file.
   */
  public void setHierarchyImportRecords( Set hierarchyImportRecords )
  {
    this.hierarchyImportRecords = hierarchyImportRecords;
  }

  /**
   * Sets the ID of this import file.
   * 
   * @param id the ID of this import file.
   */
  public void setId( Long id )
  {
    this.id = id;
  }

  /**
   * Sets the username of the user who initiated the import file import process, or null if this
   * import file has not been imported.
   * 
   * @param importedBy the username of the user who initiated the import file import process, or
   *          null if this import file has not been imported.
   */
  public void setImportedBy( String importedBy )
  {
    this.importedBy = importedBy;
  }

  /**
   * Sets the number of import records.
   * 
   * @param importRecordCount the number of import records.
   */
  public void setImportRecordCount( int importRecordCount )
  {
    this.importRecordCount = importRecordCount;
  }

  /**
   * Sets the number of import records with errors contained by this import file.
   * 
   * @param importRecordErrorCount the number of import records with errors contained by this import
   *          file.
   */
  public void setImportRecordErrorCount( int importRecordErrorCount )
  {
    this.importRecordErrorCount = importRecordErrorCount;
  }

  /**
   * Sets the errors that occurred while verifying the import records contained by this import file.
   * 
   * @param importRecordErrors the errors that occurred while verifying the import records contained
   *          by this import file, as a <code>List</code> of {@link ImportRecordError} objects.
   */
  public void setImportRecordErrors( Set importRecordErrors )
  {
    this.importRecordErrors = importRecordErrors;
  }

  /**
   * Sets the email message that is sent to participants when this import file is imported.
   * 
   * @param message the email message that is sent to participants when this import file is
   *          imported.
   */
  public void setMessage( Message message )
  {
    this.message = message;
  }

  /**
   * Sets the participant import records contained by this import file. This is a read-only
   * attribute.
   * 
   * @param participantImportRecords the participant import records contained by this import file.
   */
  public void setParticipantImportRecords( Set participantImportRecords )
  {
    this.participantImportRecords = participantImportRecords;
  }

  /**
   * Sets the product import records contained by this import file. This is a read-only attribute.
   * 
   * @param productImportRecords the product import records contained by this import file.
   */
  public void setProductImportRecords( Set productImportRecords )
  {
    this.productImportRecords = productImportRecords;
  }

  /** 
   * If this is a budget import file, then this method returns the promotion into which budgets
   * based on this import file will be inserted. If this is a deposit import file, then this method
   * returns the promotion into which deposits based on the import file will be inserted. If this is
   * a pax base, pax goal, progress or VIN import file, then this method returns the goalquest
   * promotion into which the base objective, goal level, performance progress, and auto transactions
   * based on the import file will be inserted/updated. If this is any other type of import file, 
   * this method returns null.
   * 
   * @param promotion the promotion associated with this import file.
   * 
   */
  public void setPromotion( Promotion promotion )
  {
    this.promotion = promotion;
  }

  /**
   * Sets the username of the user who staged this import file.
   * 
   * @param stagedBy the username of the user who staged this import file.
   */
  public void setStagedBy( String stagedBy )
  {
    this.stagedBy = stagedBy;
  }

  /**
   * Sets the status of this import file.
   * 
   * @param status the status of this import file.
   */
  public void setStatus( ImportFileStatusType status )
  {
    this.status = status;
  }

  /**
   * Sets the username of the user who initiated the import file verify process, or null if this
   * import file has not been verified.
   * 
   * @param verifiedBy the username of the user who initiated the import file verify process, or
   *          null if this import file has not been verified.
   */
  public void setVerifiedBy( String verifiedBy )
  {
    this.verifiedBy = verifiedBy;
  }

  /**
   * Sets the version number of this import file.
   * 
   * @param version the version number of this import file.
   */
  public void setVersion( Long version )
  {
    this.version = version;
  }

  // ---------------------------------------------------------------------------
  // Predicate Methods--Import File Status
  // ---------------------------------------------------------------------------

  /**
   * Returns true if the import file is imported; returns false otherwise.
   * 
   * @return true if the import file is imported; false otherwise.
   */
  public boolean getIsImported()
  {
    return status != null && status.getCode().equalsIgnoreCase( ImportFileStatusType.IMPORTED );
  }

  /**
   * Returns true if the import file is in import failed status; returns false otherwise.
   * 
   * @return true if the import file is in import failed status; false otherwise.
   */
  public boolean getIsImportFailed()
  {
    return status != null && status.getCode().equalsIgnoreCase( ImportFileStatusType.IMPORT_FAILED );
  }

  /**
   * Returns true if the import file is staged, but not verified; returns false otherwise.
   * 
   * @return true if the import file is staged, but not verified; false otherwise.
   */
  public boolean getIsStaged()
  {
    return status != null && status.getCode().equalsIgnoreCase( ImportFileStatusType.STAGED );
  }

  /**
   * Returns true if the import file is stage in process status; returns false otherwise.
   * 
   * @return true if the import file is stage in process status; returns false otherwise.
   */
  public boolean getIsStageInProcess()
  {
    return status != null && status.getCode().equalsIgnoreCase( ImportFileStatusType.STAGE_IN_PROCESS );
  }

  /**
   * Returns true if the import file is in stage failed status; returns false otherwise.
   * 
   * @return true if the import file is in stage failed status; false otherwise.
   */
  public boolean getIsStageFailed()
  {
    return status != null && status.getCode().equalsIgnoreCase( ImportFileStatusType.STAGE_FAILED );
  }

  /**
   * Returns true if the import file is verified, but not imported; returns false otherwise.
   * 
   * @return true if the import file is verified, but not imported; false otherwise.
   */
  public boolean getIsVerified()
  {
    return status != null && status.getCode().equalsIgnoreCase( ImportFileStatusType.VERIFIED );
  }

  /**
   * Returns true if the import file is verify in process; returns false otherwise.
   * 
   * @return true if the import file is verify in process; false otherwise.
   */
  public boolean getIsVerifyInProcess()
  {
    return status != null && status.getCode().equalsIgnoreCase( ImportFileStatusType.VERIFY_IN_PROCESS );
  }

  /**
   * Returns true if the import file is in verify failed status; returns false otherwise.
   * 
   * @return true if the import file is in verify failed status; false otherwise.
   */
  public boolean getIsVerifyFailed()
  {
    return status != null && status.getCode().equalsIgnoreCase( ImportFileStatusType.VERIFY_FAILED );
  }

  // ---------------------------------------------------------------------------
  // Predicate Methods--Import File Type
  // ---------------------------------------------------------------------------

  /**
   * Returns true if this is a budget import file; returns false otherwise.
   * 
   * @return true if this is a budget import file; false otherwise.
   */
  public boolean getIsBudgetImportFile()
  {
    return fileType != null && fileType.getCode().equalsIgnoreCase( ImportFileTypeType.BUDGET );
  }

  public boolean getIsNominationCustomApproverImportFile()
  {
    return fileType != null && fileType.getCode().equalsIgnoreCase( ImportFileTypeType.NOMINATION_APPROVER );
  }

  /**
   * Returns true if this is a budget distribution import file; returns false otherwise.
   * 
   * @return true if this is a budget distribution import file; false otherwise.
   */
  public boolean getIsBudgetDistributionImportFile()
  {
    return fileType != null && fileType.getCode().equalsIgnoreCase( ImportFileTypeType.BUDGET_DISTRIBUTION );
  }

  /**
   * Returns true if this is a leaderBoard import file; returns false otherwise.
   * 
   *  @return true if this is a leaderBoard import file: false otherwise.
   */
  public boolean getIsLeaderBoardImportFile()
  {
    return fileType != null && fileType.getCode().equalsIgnoreCase( ImportFileTypeType.LEADERBOARD );
  }

  /**
   * Returns true if this is a badge import file; returns false otherwise.
   * 
   *  @return true if this is a badge import file: false otherwise.
   */
  public boolean getIsBadgeImportFile()
  {
    return fileType != null && fileType.getCode().equalsIgnoreCase( ImportFileTypeType.BADGE );
  }

  /**
   * Returns true if this is a deposit import file; returns false otherwise.
   * 
   * @return true if this is a depost import file; false otherwise.
   */
  public boolean getIsDepositImportFile()
  {
    return fileType != null && fileType.getCode().equalsIgnoreCase( ImportFileTypeType.DEPOSIT );
  }

  /**
   * Returns true if this is a hierarchy import file; returns false otherwise.
   * 
   * @return true if this is a hierarchy import file; false otherwise.
   */
  public boolean getIsHierarchyImportFile()
  {
    return fileType != null && fileType.getCode().equalsIgnoreCase( ImportFileTypeType.HIERARCHY );
  }

  /**
   * Returns true if this is a participant import file; returns false otherwise.
   * 
   * @return true if this is a participant import file; false otherwise.
   */
  public boolean getIsParticipantImportFile()
  {
    return fileType != null && fileType.getCode().equalsIgnoreCase( ImportFileTypeType.PARTICIPANT );
  }

  /**
   * Returns true if this is a product import file; returns false otherwise.
   * 
   * @return true if this is a product import file; false otherwise.
   */
  public boolean getIsProductImportFile()
  {
    return fileType != null && fileType.getCode().equalsIgnoreCase( ImportFileTypeType.PRODUCT );
  }

  /**
   * Returns true if this is a product claim file; returns false otherwise.
   * 
   * @return true if this is a product claim file; false otherwise.
   */
  public boolean getIsProductClaimImportFile()
  {
    return fileType != null && fileType.getCode().equalsIgnoreCase( ImportFileTypeType.PRODUCT_CLAIM );
  }

  /**
   * Returns true if this is a quiz file; returns false otherwise.
   *
   * @return true if this is a quiz file; false otherwise.
   */
  public boolean getIsQuizImportFile()
  {
    return fileType != null && fileType.getCode().equalsIgnoreCase( ImportFileTypeType.QUIZ );
  }

  /**
   * Returns true if this is a goalquest base load file; returns false otherwise.
   *
   * @return true if this is a goalquest base load file; false otherwise.
   */
  public boolean getIsGqBaseLoadImportFile()
  {
    return fileType != null && fileType.getCode().equalsIgnoreCase( ImportFileTypeType.GQ_BASE_DATA_LOAD );
  }

  /**
   * Returns true if this is a goalquest progress load file; returns false otherwise.
   *
   * @return true if this is a goalquest progress load file; false otherwise.
   */
  public boolean getIsGqProgressLoadImportFile()
  {
    return fileType != null && fileType.getCode().equalsIgnoreCase( ImportFileTypeType.GQ_PROGRESS_DATA_LOAD );
  }

  /**
   * Returns true if this is a goalquest goal load file; returns false otherwise.
   *
   * @return true if this is a goalquest goal load file; false otherwise.
   */
  public boolean getIsGqGoalLoadImportFile()
  {
    return fileType != null && fileType.getCode().equalsIgnoreCase( ImportFileTypeType.GQ_GOAL_DATA_LOAD );
  }

  /**
   * Returns true if this is a goalquest vin file; returns false otherwise.
   *
   * @return true if this is a goalquest vin file; false otherwise.
   */
  public boolean getIsGqVinLoadImportFile()
  {
    return fileType != null && fileType.getCode().equalsIgnoreCase( ImportFileTypeType.GQ_VIN_LOAD );
  }

  /**
   * Returns true if this is a challengepoint base load file; returns false otherwise.
   *
   * @return true if this is a challengepoint base load file; false otherwise.
   */
  public boolean getIsCPBaseLoadImportFile()
  {
    return fileType != null && fileType.getCode().equalsIgnoreCase( ImportFileTypeType.CP_BASE_DATA_LOAD );
  }

  /**
   * Returns true if this is a challengepoint progress load file; returns false otherwise.
   *
   * @return true if this is a challengepoint progress load file; false otherwise.
   */
  public boolean getIsCPProgressLoadImportFile()
  {
    return fileType != null && fileType.getCode().equalsIgnoreCase( ImportFileTypeType.CP_PROGRESS_DATA_LOAD );
  }

  // ThrowdownProgressLoad
  /**
   * Returns true if this is a throwdown progress load file; returns false otherwise.
   *
   * @return true if this is a throwdown progress load file; false otherwise.
   */
  public boolean getIsTDProgressLoadImportFile()
  {
    return fileType != null && fileType.getCode().equalsIgnoreCase( ImportFileTypeType.TD_PROGRESS_DATA_LOAD );
  }

  /**
   * Returns true if this is a challengepoint level load file; returns false otherwise.
   *
   * @return true if this is a challengepoint level load file; false otherwise.
   */
  public boolean getIsCPLevelLoadImportFile()
  {
    return fileType != null && fileType.getCode().equalsIgnoreCase( ImportFileTypeType.CP_LEVEL_DATA_LOAD );
  }

  /**
   * Returns true if this is a award level load file; returns false otherwise.
   *
   * @return true if this is a award level load file; false otherwise.
   */
  public boolean getIsAwardLevelImportFile()
  {
    return fileType != null && fileType.getCode().equalsIgnoreCase( ImportFileTypeType.AWARD_LEVEL );
  }

  /**
   * @return true if this is a recognition promotion; false otherwise
   */
  public boolean getIsRecognitionPromotion()
  {
    return promotion != null && promotion.isRecognitionPromotion();
  }

  /**
   * Returns true if this is a ssi contest load file; returns false otherwise.
   *
   * @return true if this is a ssi contest load file; false otherwise.
   */
  public boolean getIsSSIContestOBJ()
  {
    return fileType != null && fileType.getCode().equalsIgnoreCase( ImportFileTypeType.SSI_CONTEST_OBJ );
  }

  public boolean getIsSSIContestSIU()
  {
    return fileType != null && fileType.getCode().equalsIgnoreCase( ImportFileTypeType.SSI_CONTEST_SIU );
  }

  public boolean getIsSSIContestDTGT()
  {
    return fileType != null && fileType.getCode().equalsIgnoreCase( ImportFileTypeType.SSI_CONTEST_DTGT );
  }

  public boolean getIsSSIContestSR()
  {
    return fileType != null && fileType.getCode().equalsIgnoreCase( ImportFileTypeType.SSI_CONTEST_SR );
  }

  public boolean getIsSSIContestATN()
  {
    return fileType != null && fileType.getCode().equalsIgnoreCase( ImportFileTypeType.SSI_CONTEST_ATN );
  }

  // ---------------------------------------------------------------------------
  // Equals, Hashcode, and To String Methods
  // ---------------------------------------------------------------------------

  /**
   * Returns true if this object and the given object are equal; returns false otherwise.
   * 
   * @see com.biperf.core.domain.BaseDomain#equals(java.lang.Object)
   * @param object the object to which this object is compared for equality.
   * @return true if this object and the given object are equal; false otherwise.
   */
  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }

    if ( ! ( object instanceof ImportFile ) )
    {
      return false;
    }

    ImportFile importFile = (ImportFile)object;

    if ( this.getFileName() != null ? !this.getFileName().equals( importFile.getFileName() ) : importFile.getFileName() != null )
    {
      return false;
    }

    return true;
  }

  /**
   * Returns the hashcode for this object.
   * 
   * @see com.biperf.core.domain.BaseDomain#hashCode()
   * @return the hashcode for this object.
   */
  public int hashCode()
  {
    return this.getFileName() != null ? this.getFileName().hashCode() : 0;
  }

  /**
   * Returns the string representation of this object.
   * 
   * @return the string representation of this object.
   */

  public String toString()
  {
    final StringBuffer buf = new StringBuffer();
    buf.append( "ImportFile [" );
    buf.append( "{id=" ).append( id ).append( "}, " );
    buf.append( "{filename=" ).append( this.getFileName() ).append( "}, " );
    buf.append( "]" );
    return buf.toString();
  }

  public ImportRecord getImportRecordById( Long importRecordId )
  {
    for ( Iterator iterator = getImportRecords().iterator(); iterator.hasNext(); )
    {
      ImportRecord record = (ImportRecord)iterator.next();
      if ( importRecordId.equals( record.getId() ) )
      {
        return record;
      }
    }

    return null;
  }

  /**
   * @return value of fileImportApprovalType property
   */
  public FileImportApprovalType getFileImportApprovalType()
  {
    return fileImportApprovalType;
  }

  /**
   * @param fileImportApprovalType value for fileImportApprovalType property
   */
  public void setFileImportApprovalType( FileImportApprovalType fileImportApprovalType )
  {
    this.fileImportApprovalType = fileImportApprovalType;
  }

  /**
   * @return value of productClaimImportRecords property
   */
  public Set getProductClaimImportRecords()
  {
    return productClaimImportRecords;
  }

  /**
   * @param productClaimImportRecords value for productClaimImportRecords property
   */
  public void setProductClaimImportRecords( Set productClaimImportRecords )
  {
    this.productClaimImportRecords = productClaimImportRecords;
  }

  public Set getQuizImportRecords()
  {
    return quizImportRecords;
  }

  public void setQuizImportRecords( Set quizImportRecords )
  {
    this.quizImportRecords = quizImportRecords;
  }

  /** 
   * @return int total count of records wihtout error 
   */
  public int getImportRecordWithoutErrorCount()
  {
    return importRecordCount - importRecordErrorCount;
  }

  /**
   * @return a Boolean indicating if Budget amounts should be replaced or added to
   */
  public Boolean getReplaceValues()
  {
    return replaceValues;
  }

  /**
   * @param replaceValues
   */
  public void setReplaceValues( Boolean replaceValues )
  {
    this.replaceValues = replaceValues;
  }

  public void setCountry( Country country )
  {
    this.country = country;
  }

  public Country getCountry()
  {
    return country;
  }

  public String getActionType()
  {
    return actionType;
  }

  public void setActionType( String actionType )
  {
    this.actionType = actionType;
  }

  public Date getAsOfDate()
  {
    return asOfDate;
  }

  public void setAsOfDate( Date asOfDate )
  {
    this.asOfDate = asOfDate;
  }

  public Set getPaxBaseImportRecords()
  {
    return paxBaseImportRecords;
  }

  public void setPaxBaseImportRecords( Set paxBaseImportRecords )
  {
    this.paxBaseImportRecords = paxBaseImportRecords;
  }

  public Set getPaxGoalImportRecords()
  {
    return paxGoalImportRecords;
  }

  public void setPaxGoalImportRecords( Set paxGoalImportRecords )
  {
    this.paxGoalImportRecords = paxGoalImportRecords;
  }

  public Set getVinImportRecords()
  {
    return vinImportRecords;
  }

  public void setVinImportRecords( Set vinImportRecords )
  {
    this.vinImportRecords = vinImportRecords;
  }

  public Date getProgressEndDate()
  {
    return progressEndDate;
  }

  public void setProgressEndDate( Date progressEndDate )
  {
    this.progressEndDate = progressEndDate;
  }

  public Set getProgressImportRecords()
  {
    return progressImportRecords;
  }

  public void setProgressImportRecords( Set progressImportRecords )
  {
    this.progressImportRecords = progressImportRecords;
  }

  public Set getSsiProgressImportRecords()
  {
    return ssiProgressImportRecords;
  }

  public void setSsiProgressImportRecords( Set ssiProgressImportRecords )
  {
    this.ssiProgressImportRecords = ssiProgressImportRecords;
  }

  public Long getCard()
  {
    return card;
  }

  public void setCard( Long card )
  {
    this.card = card;
  }

  public Boolean getRecognitionDeposit()
  {
    return recognitionDeposit;
  }

  public void setRecognitionDeposit( Boolean recognitionDeposit )
  {
    this.recognitionDeposit = recognitionDeposit;
  }

  public Participant getSubmitter()
  {
    return submitter;
  }

  public void setSubmitter( Participant submitter )
  {
    this.submitter = submitter;
  }

  public String getSubmitterComments()
  {
    return submitterComments;
  }

  public void setSubmitterComments( String submitterComments )
  {
    this.submitterComments = submitterComments;
  }

  public Node getSubmitterNode()
  {
    return submitterNode;
  }

  public void setSubmitterNode( Node submitterNode )
  {
    this.submitterNode = submitterNode;
  }

  public Set getPaxCPBaseImportRecords()
  {
    return paxCPBaseImportRecords;
  }

  public void setPaxCPBaseImportRecords( Set paxCPBaseImportRecords )
  {
    this.paxCPBaseImportRecords = paxCPBaseImportRecords;
  }

  public Set getPaxCPLevelImportRecords()
  {
    return paxCPLevelImportRecords;
  }

  public void setPaxCPLevelImportRecords( Set paxCPLevelImportRecords )
  {
    this.paxCPLevelImportRecords = paxCPLevelImportRecords;
  }

  public Set getProgressCPImportRecords()
  {
    return progressCPImportRecords;
  }

  public void setProgressCPImportRecords( Set progressCPImportRecords )
  {
    this.progressCPImportRecords = progressCPImportRecords;
  }

  // ThrowdownProgressLoad
  public Set getProgressTDImportRecords()
  {
    return progressTDImportRecords;
  }

  // ThrowdownProgressLoad
  public void setProgressTDImportRecords( Set progressTDImportRecords )
  {
    this.progressTDImportRecords = progressTDImportRecords;
  }

  public Set getAwardLevelImportRecords()
  {
    return awardLevelImportRecords;
  }

  public void setAwardLevelImportRecords( Set awardLevelImportRecords )
  {
    this.awardLevelImportRecords = awardLevelImportRecords;
  }

  public boolean getIsCountryChanged()
  {
    return status != null && status.getCode().equalsIgnoreCase( ImportFileStatusType.COUNTRY_CHANGE );
  }

  public String getLeaderBoardName()
  {
    return leaderBoardName;
  }

  public void setLeaderBoardName( String leaderBoardName )
  {
    this.leaderBoardName = leaderBoardName;
  }

  public Long getBadgepromotionId()
  {
    return badgepromotionId;
  }

  public void setBadgepromotionId( Long badgepromotionId )
  {
    this.badgepromotionId = badgepromotionId;
  }

  public Date getEarnedDate()
  {
    return earnedDate;
  }

  public void setEarnedDate( Date earnedDate )
  {
    this.earnedDate = earnedDate;
  }

  public String getBadgeName()
  {
    return badgeName;
  }

  public void setBadgeName( String badgeName )
  {
    this.badgeName = badgeName;
  }

  public boolean isCopyManager()
  {
    return copyManager;
  }

  public void setCopyManager( boolean copyManager )
  {
    this.copyManager = copyManager;
  }

  public Long getLeaderboardId()
  {
    return leaderboardId;
  }

  public void setLeaderboardId( Long leaderboardId )
  {
    this.leaderboardId = leaderboardId;
  }

  public Long getBudgetSegmentId()
  {
    return budgetSegmentId;
  }

  public void setBudgetSegmentId( Long budgetSegmentId )
  {
    this.budgetSegmentId = budgetSegmentId;
  }

  public String getBudgetSegmentName()
  {
    return budgetSegmentName;
  }

  public void setBudgetSegmentName( String budgetSegmentName )
  {
    this.budgetSegmentName = budgetSegmentName;
  }

  public Date getDelayAwardDate()
  {
    return delayAwardDate;
  }

  public void setDelayAwardDate( Date delayAwardDate )
  {
    this.delayAwardDate = delayAwardDate;
  }

  public Set getBudgetDistributionImportRecords()
  {
    return budgetDistributionImportRecords;
  }

  public void setBudgetDistributionImportRecords( Set budgetDistributionImportRecords )
  {
    this.budgetDistributionImportRecords = budgetDistributionImportRecords;
  }

  public Long getBudgetMasterId()
  {
    return budgetMasterId;
  }

  public void setBudgetMasterId( Long budgetMasterId )
  {
    this.budgetMasterId = budgetMasterId;
  }

  public String getBudgetMasterName()
  {
    return budgetMasterName;
  }

  public void setBudgetMasterName( String budgetMasterName )
  {
    this.budgetMasterName = budgetMasterName;
  }

  public Set getNominationApproverImportRecords()
  {
    return nominationApproverImportRecords;
  }

  public void setNominationApproverImportRecords( Set nominationApproverImportRecords )
  {
    this.nominationApproverImportRecords = nominationApproverImportRecords;
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
