
package com.biperf.core.service.fileload.impl;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.biperf.core.domain.enums.BadgeType;
import com.biperf.core.domain.enums.ImportFileStatusType;
import com.biperf.core.domain.fileload.BadgeImportRecord;
import com.biperf.core.domain.fileload.ImportFile;
import com.biperf.core.domain.gamification.Badge;
import com.biperf.core.domain.gamification.BadgeRule;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.fileload.ImportStrategy;
import com.biperf.core.service.gamification.GamificationService;
import com.biperf.core.service.journal.JournalService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.UserManager;

/*
 * BadgeImportStrategy <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th>
 * <th>Date</th> <th>Version</th> <th>Comments</th> </tr> <tr> <td>Fazil Sharafudeen</td> <td>Oct
 * 1, 2012</td> <td>1.0</td> <td>created</td> </tr> </table> </p>
 */

public class BadgeImportStrategy extends ImportStrategy
{

  // participant
  private GamificationService gamificationService;
  private ParticipantService participantService;
  private JournalService journalService;

  /**
   * Imports the specified import file.
   * 
   * @param importFile the import file to import.
   * @param records the recordst to import.
   * @throws ServiceErrorException
   */
  public void importImportFile( ImportFile importFile, List records ) throws ServiceErrorException
  {
    if ( !importFile.getStatus().isImportInProcess() )
    {
      throw new ServiceErrorException( ServiceErrorMessageKeys.SYSTEM_ERRORS_INVALID_CALL );
    }

    Map result = getGamificationService().importImportFile( importFile.getId(), UserManager.getUserId(), importFile.getBadgepromotionId(), importFile.getEarnedDate() );

    int errorCount = ( (BigDecimal)result.get( "p_total_error_rec" ) ).intValue();
    importFile.setDateImported( DateUtils.getCurrentDate() );
    importFile.setImportedBy( UserManager.getUserName() );
    importFile.setImportRecordErrorCount( errorCount );

    int resultCode = ( (BigDecimal)result.get( "p_out_returncode" ) ).intValue();
    if ( resultCode == 0 || resultCode == 1 )
    {
      importFile.setStatus( ImportFileStatusType.lookup( ImportFileStatusType.IMPORTED ) );
      Badge badge = getGamificationService().getBadgeById( importFile.getBadgepromotionId() );
      Set badgeImportRecords = importFile.getBadgeImportRecords();
      Iterator badgePaxItr = badgeImportRecords.iterator();
      while ( badgePaxItr.hasNext() )
      {
        BadgeImportRecord badgeImportRecord = (BadgeImportRecord)badgePaxItr.next();
        if ( badgeImportRecord.getImportRecordErrors().isEmpty() )
        {
          BadgeRule rule = getGamificationService().getBadgeRuleByBadgeName( badgeImportRecord.getBadgeName(), importFile.getBadgepromotionId() );
          Participant participant = getParticipantService().getParticipantById( badgeImportRecord.getUserId() );
          if ( badge.getNotificationMessageId() != -1 )
          {
            if ( badgeImportRecord.getUserId() != null )
            {
              getGamificationService().sendSummaryMessage( participant, "", rule, 0 );
            }
          }
          if ( rule.getBadgePoints() != null && rule.getBadgePoints() > 0 )
          {
            int recordExist = gamificationService.canCreateJournal( participant.getId(), rule.getId(), badge.getId(), BadgeType.FILELOAD );

            if ( recordExist >= 1 )
            {
              gamificationService.createJournalEntry( (Promotion)badge, participant, rule.getBadgePoints() );
            }
          }
        }
      }
    }
    else
    {
      importFile.setStatus( ImportFileStatusType.lookup( ImportFileStatusType.IMPORT_FAILED ) );
    }
  }

  /**
   * Verifies the specified import file.
   * 
   * @param records the records to import.
   * @param importFile the import file to import.
   */
  public void verifyImportFile( ImportFile importFile, List records )
  {
    validateImportFilePrc( importFile, records );
  }

  /**
   * Verifies the specified import file.
   * 
   * @param records the records to import.
   * @param importFile the import file to import.
   * @param justForPaxRightNow
   * @throws ServiceErrorException
   */
  public void verifyImportFile( ImportFile importFile, List records, boolean justForPaxRightNow ) throws ServiceErrorException
  {
    // First, make sure we are in the correct state
    if ( !importFile.getStatus().isVerifyInProcess() )
    {
      throw new ServiceErrorException( ServiceErrorMessageKeys.SYSTEM_ERRORS_INVALID_CALL );
    }

    validateImportFilePrc( importFile, records );
  }

  public int validateImportFilePrc( ImportFile importFile, List records )
  {

    Map result = getGamificationService().verifyImportFile( importFile.getId(), UserManager.getUserId(), importFile.getBadgepromotionId(), importFile.getEarnedDate() );
    int errorCount = ( (BigDecimal)result.get( "p_total_error_rec" ) ).intValue();
    importFile.setDateVerified( DateUtils.getCurrentDate() );
    importFile.setVerifiedBy( UserManager.getUserName() );
    importFile.setImportRecordErrorCount( errorCount );

    int resultCode = ( (BigDecimal)result.get( "p_out_returncode" ) ).intValue();
    if ( resultCode == 0 || resultCode == 1 )
    {
      importFile.setStatus( ImportFileStatusType.lookup( ImportFileStatusType.VERIFIED ) );
    }
    else
    {
      importFile.setHierarchy( null );
      importFile.setStatus( ImportFileStatusType.lookup( ImportFileStatusType.VERIFY_FAILED ) );
    }

    return errorCount;
  }

  public GamificationService getGamificationService()
  {
    return gamificationService;
  }

  public void setGamificationService( GamificationService gamificationService )
  {
    this.gamificationService = gamificationService;
  }

  public ParticipantService getParticipantService()
  {
    return participantService;
  }

  public JournalService getJournalService()
  {
    return journalService;
  }

  public void setJournalService( JournalService journalService )
  {
    this.journalService = journalService;
  }

  public void setParticipantService( ParticipantService participantService )
  {
    this.participantService = participantService;
  }

}
