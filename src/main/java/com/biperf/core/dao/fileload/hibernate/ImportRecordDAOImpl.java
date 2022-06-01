/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/dao/fileload/hibernate/ImportRecordDAOImpl.java,v $
 */

package com.biperf.core.dao.fileload.hibernate;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.Query;
import org.hibernate.Session;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.fileload.ImportRecordDAO;
import com.biperf.core.domain.enums.ImportFileTypeType;
import com.biperf.core.domain.fileload.ImportRecord;
import com.biperf.core.service.AssociationRequest;
import com.biperf.core.utils.HibernateSessionManager;

public class ImportRecordDAOImpl extends BaseDAO implements ImportRecordDAO
{
  // ---------------------------------------------------------------------------
  // Public Methods
  // ---------------------------------------------------------------------------

  /**
   * Gets a set of import file records for a given import file ID.  Limits the
   * number of results to the pageSize passed.
   *
   * @param id the ID of the import file to get.
   * @param pageNumber the page number to retrieve data for.
   * @param pageSize the number of records to get.
   * @param importFileType the import file type of import file to get.
   * @return List of records for the specified import file.
   */
  public List getRecordsByPageNumber( Long id, int pageNumber, int pageSize, String importFileType )
  {
    Session session = HibernateSessionManager.getSession();
    String queryName = getQueryName( importFileType );

    Query query = session.getNamedQuery( queryName );
    // for hierarchy and quiz, get them all at once
    if ( !importFileType.equals( ImportFileTypeType.HIERARCHY ) && !importFileType.equals( ImportFileTypeType.QUIZ ) )
    {
      query.setMaxResults( pageSize );
      if ( pageNumber > 1 )
      {
        query.setFirstResult( pageSize * ( pageNumber - 1 ) );
      }
    }
    query.setParameter( "importFileId", id );

    return query.list();

  }

  public List<ImportRecord> getAllRecords( Long id, String importFileType )
  {
    Session session = HibernateSessionManager.getSession();
    String queryName = getQueryName( importFileType );

    Query query = session.getNamedQuery( queryName );
    query.setParameter( "importFileId", id );

    return query.list();

  }

  /**
   * Gets a set of import file records without errors for a given import file ID.  Limits the
   * number of results to the pageSize passed.
   *
   * @param id the ID of the import file to get.
   * @param pageNumber the page number to retrieve data for.
   * @param pageSize the number of records to get.
   * @param importFileType the import file type of import file to get.
   * @param associationRequest hydrates domain object associations
   * @return List of records for the specified import file.
   */

  public List getRecordsWithoutErrorByPageNumber( Long id, int pageNumber, int pageSize, String importFileType, AssociationRequest associationRequest )
  {
    Session session = HibernateSessionManager.getSession();
    String queryName = getQueryNameForRecordsWithoutError( importFileType );

    Query query = session.getNamedQuery( queryName );
    query.setMaxResults( pageSize );
    if ( pageNumber > 1 )
    {
      query.setFirstResult( pageSize * ( pageNumber - 1 ) );
    }
    query.setParameter( "importFileId", id );
    List queryList = query.list();
    if ( associationRequest != null && queryList != null )
    {
      associationRequest.execute( queryList );
    }

    return queryList;
  }

  /**
   * Gets a set of import file records with errors for a given import file ID.  Limits the
   * number of results to the pageSize passed.
   *
   * @param id the ID of the import file to get.
   * @param pageNumber the page number to retrieve data for.
   * @param pageSize the number of records to get.
   * @param importFileType the import file type of import file to get.
   * @param associationRequest hydrates domain object association
   * @return List of records for the specified import file.
   */
  public List getRecordsWithErrorByPageNumber( Long id, int pageNumber, int pageSize, String importFileType, AssociationRequest associationRequest )
  {
    Session session = HibernateSessionManager.getSession();
    String queryName = null;
    queryName = getQueryNameForRecordsWithError( importFileType );

    Query query = session.getNamedQuery( queryName );

    query.setMaxResults( pageSize );
    if ( pageNumber > 1 )
    {
      query.setFirstResult( pageSize * ( pageNumber - 1 ) );
    }
    query.setParameter( "importFileId", id );
    List queryList = query.list();
    if ( associationRequest != null && queryList != null )
    {
      associationRequest.execute( queryList );
    }

    return queryList;
  }

  // ---------------------------------------------------------------------------
  // Private Methods
  // ---------------------------------------------------------------------------

  private String getQueryName( String importFileType )
  {
    String name = "";

    if ( importFileType.equals( ImportFileTypeType.PARTICIPANT ) )
    {
      name = "com.biperf.core.domain.import.file.PaxRecordsByPage";
    }
    else if ( importFileType.equals( ImportFileTypeType.PRODUCT ) )
    {
      name = "com.biperf.core.domain.import.file.ProductRecordsByPage";
    }
    else if ( importFileType.equals( ImportFileTypeType.PRODUCT_CLAIM ) )
    {
      name = "com.biperf.core.domain.import.file.ProductClaimRecordsByPage";
    }
    else if ( importFileType.equals( ImportFileTypeType.BUDGET ) )
    {
      name = "com.biperf.core.domain.import.file.BudgetRecordsByPage";
    }
    else if ( importFileType.equals( ImportFileTypeType.DEPOSIT ) )
    {
      name = "com.biperf.core.domain.import.file.DepositRecordsByPage";
    }
    else if ( importFileType.equals( ImportFileTypeType.AWARD_LEVEL ) )
    {
      name = "com.biperf.core.domain.import.file.AwardLevelRecordsByPage";
    }
    else if ( importFileType.equals( ImportFileTypeType.HIERARCHY ) )
    {
      name = "com.biperf.core.domain.import.file.AllHierarchyRecords";
    }
    else if ( importFileType.equals( ImportFileTypeType.QUIZ ) )
    {
      name = "com.biperf.core.domain.import.file.QuizRecordsByPage";
    }
    else if ( importFileType.equals( ImportFileTypeType.GQ_PROGRESS_DATA_LOAD ) )
    {
      name = "com.biperf.core.domain.import.file.ProgressRecordsByPage";
    }
    else if ( importFileType.equals( ImportFileTypeType.GQ_BASE_DATA_LOAD ) )
    {
      name = "com.biperf.core.domain.import.file.PaxBaseRecordsByPage";
    }
    else if ( importFileType.equals( ImportFileTypeType.GQ_GOAL_DATA_LOAD ) )
    {
      name = "com.biperf.core.domain.import.file.PaxGoalRecordsByPage";
    }
    else if ( importFileType.equals( ImportFileTypeType.GQ_VIN_LOAD ) )
    {
      name = "com.biperf.core.domain.import.file.AutoVinRecordsByPage";
    }
    else if ( importFileType.equals( ImportFileTypeType.CP_PROGRESS_DATA_LOAD ) )
    {
      name = "com.biperf.core.domain.import.file.CPProgressRecordsByPage";
    }
    else if ( importFileType.equals( ImportFileTypeType.CP_BASE_DATA_LOAD ) )
    {
      name = "com.biperf.core.domain.import.file.PaxCPBaseRecordsByPage";
    }
    else if ( importFileType.equals( ImportFileTypeType.CP_LEVEL_DATA_LOAD ) )
    {
      name = "com.biperf.core.domain.import.file.PaxCPLevelRecordsByPage";
    }
    else if ( importFileType.equals( ImportFileTypeType.LEADERBOARD ) )
    {
      name = "com.biperf.core.domain.import.file.LeaderBoardImportRecordsByPage";
    }
    else if ( importFileType.equals( ImportFileTypeType.BADGE ) )
    {
      name = "com.biperf.core.domain.import.file.BadgeImportRecordsByPage";
    }
    else if ( importFileType.equals( ImportFileTypeType.TD_PROGRESS_DATA_LOAD ) )
    {
      name = "com.biperf.core.domain.import.file.TDProgressRecordsByPage";
    }
    else if ( importFileType.equals( ImportFileTypeType.BUDGET_DISTRIBUTION ) )
    {
      name = "com.biperf.core.domain.import.file.BudgetDistributionRecordsByPage";
    }
    else if ( importFileType.equals( ImportFileTypeType.NOMINATION_APPROVER ) )
    {
      name = "com.biperf.core.domain.import.file.NominationCustomApproverRecordsByPage";
    }
    else if ( importFileType.equals( ImportFileTypeType.SSI_CONTEST_OBJ ) )
    {
      name = "com.biperf.core.domain.fileload.SSIObjContestImportRecordsByPage";
    }
    else if ( importFileType.equals( ImportFileTypeType.SSI_CONTEST_DTGT ) )
    {
      name = "com.biperf.core.domain.fileload.SSIDtgtContestImportRecordsByPage";
    }
    else if ( importFileType.equals( ImportFileTypeType.SSI_CONTEST_SIU ) )
    {
      name = "com.biperf.core.domain.fileload.SSISiuContestImportRecordsByPage";
    }
    else if ( importFileType.equals( ImportFileTypeType.SSI_CONTEST_ATN ) )
    {
      name = "com.biperf.core.domain.fileload.SSIAtnContestImportRecordsByPage";
    }
    else if ( importFileType.equals( ImportFileTypeType.SSI_CONTEST_SR ) )
    {
      name = "com.biperf.core.domain.fileload.SSISrContestImportRecordsByPage";
    }
    return name;
  }

  private String getQueryNameForRecordsWithoutError( String importFileType )
  {
    String queryName = "";

    if ( importFileType.equals( ImportFileTypeType.BUDGET ) )
    {
      queryName = "com.biperf.core.domain.import.file.BudgetRecordsByPageWithoutError";
    }

    else if ( importFileType.equals( ImportFileTypeType.PARTICIPANT ) )
    {
      queryName = "com.biperf.core.domain.import.file.PaxRecordsByPageWithoutError";
    }

    else if ( importFileType.equals( ImportFileTypeType.PRODUCT ) )
    {
      queryName = "com.biperf.core.domain.import.file.ProductRecordsByPageWithoutError";
    }

    else if ( importFileType.equals( ImportFileTypeType.PRODUCT_CLAIM ) )
    {
      queryName = "com.biperf.core.domain.import.file.ProductClaimRecordsByPageWithoutError";
    }

    else if ( importFileType.equals( ImportFileTypeType.DEPOSIT ) )
    {
      queryName = "com.biperf.core.domain.import.file.DepositRecordsByPageWithoutError";
    }

    else if ( importFileType.equals( ImportFileTypeType.AWARD_LEVEL ) )
    {
      queryName = "com.biperf.core.domain.import.file.AwardLevelRecordsByPageWithoutError";
    }

    else if ( importFileType.equals( ImportFileTypeType.HIERARCHY ) )
    {
      queryName = "com.biperf.core.domain.import.file.HierarchyRecordsByPageWithoutError";
    }

    else if ( importFileType.equals( ImportFileTypeType.QUIZ ) )
    {
      queryName = "com.biperf.core.domain.import.file.QuizRecordsByPageWithoutError";
    }
    else if ( importFileType.equals( ImportFileTypeType.GQ_PROGRESS_DATA_LOAD ) )
    {
      queryName = "com.biperf.core.domain.import.file.ProgressRecordsByPageWithoutError";
    }
    else if ( importFileType.equals( ImportFileTypeType.GQ_BASE_DATA_LOAD ) )
    {
      queryName = "com.biperf.core.domain.import.file.PaxBaseRecordsByPageWithoutError";
    }
    else if ( importFileType.equals( ImportFileTypeType.GQ_GOAL_DATA_LOAD ) )
    {
      queryName = "com.biperf.core.domain.import.file.PaxGoalRecordsByPageWithoutError";
    }
    else if ( importFileType.equals( ImportFileTypeType.GQ_VIN_LOAD ) )
    {
      queryName = "com.biperf.core.domain.import.file.AutoVinRecordsByPageWithoutError";
    }
    else if ( importFileType.equals( ImportFileTypeType.CP_PROGRESS_DATA_LOAD ) )
    {
      queryName = "com.biperf.core.domain.import.file.CPProgressRecordsByPageWithoutError";
    }
    else if ( importFileType.equals( ImportFileTypeType.CP_BASE_DATA_LOAD ) )
    {
      queryName = "com.biperf.core.domain.import.file.PaxCPBaseRecordsByPageWithoutError";
    }
    else if ( importFileType.equals( ImportFileTypeType.CP_LEVEL_DATA_LOAD ) )
    {
      queryName = "com.biperf.core.domain.import.file.PaxCPLevelRecordsByPageWithoutError";
    }
    else if ( importFileType.equals( ImportFileTypeType.LEADERBOARD ) )
    {
      queryName = "com.biperf.core.domain.import.file.LeaderBoardImportRecordsByPageWithoutError";
    }
    else if ( importFileType.equals( ImportFileTypeType.BADGE ) )
    {
      queryName = "com.biperf.core.domain.import.file.BadgeImportRecordsByPageWithoutError";
    }
    else if ( importFileType.equals( ImportFileTypeType.TD_PROGRESS_DATA_LOAD ) )
    {
      queryName = "com.biperf.core.domain.import.file.TDProgressRecordsByPageWithoutError";
    }
    else if ( importFileType.equals( ImportFileTypeType.BUDGET_DISTRIBUTION ) )
    {
      queryName = "com.biperf.core.domain.import.file.BudgetDistributionRecordsByPageWithoutError";
    }
    else if ( importFileType.equals( ImportFileTypeType.NOMINATION_APPROVER ) )
    {
      queryName = "com.biperf.core.domain.import.file.NominationCustomApproverRecordsByPageWithoutError";
    }
    else if ( importFileType.equals( ImportFileTypeType.SSI_PROGRESS_DATA_LOAD ) )
    {
      queryName = "com.biperf.core.domain.import.file.SSIProgressRecordsByPageWithoutError";
    }
    else if ( importFileType.equals( ImportFileTypeType.SSI_CONTEST_OBJ ) )
    {
      queryName = "com.biperf.core.domain.fileload.SSIObjContestImportRecordsByPageWithoutError";
    }
    else if ( importFileType.equals( ImportFileTypeType.SSI_CONTEST_DTGT ) )
    {
      queryName = "com.biperf.core.domain.fileload.SSIDtgtContestImportRecordsByPageWithoutError";
    }
    else if ( importFileType.equals( ImportFileTypeType.SSI_CONTEST_SIU ) )
    {
      queryName = "com.biperf.core.domain.fileload.SSISiuContestImportRecordsByPageWithoutError";
    }
    else if ( importFileType.equals( ImportFileTypeType.SSI_CONTEST_ATN ) )
    {
      queryName = "com.biperf.core.domain.fileload.SSIAtnContestImportRecordsByPageWithoutError";
    }
    else if ( importFileType.equals( ImportFileTypeType.SSI_CONTEST_SR ) )
    {
      queryName = "com.biperf.core.domain.fileload.SSISrContestImportRecordsByPageWithoutError";
    }

    return queryName;
  }

  private String getQueryNameForRecordsWithError( String importFileType )
  {
    String queryName = "";

    if ( importFileType.equals( ImportFileTypeType.BUDGET ) )
    {
      queryName = "com.biperf.core.domain.import.file.BudgetRecordsByPageWithError";
    }
    else if ( importFileType.equals( ImportFileTypeType.PARTICIPANT ) )
    {
      queryName = "com.biperf.core.domain.import.file.PaxRecordsByPageWithError";
    }
    else if ( importFileType.equals( ImportFileTypeType.PRODUCT ) )
    {
      queryName = "com.biperf.core.domain.import.file.ProductRecordsByPageWithError";
    }
    else if ( importFileType.equals( ImportFileTypeType.PRODUCT_CLAIM ) )
    {
      queryName = "com.biperf.core.domain.import.file.ProductClaimRecordsByPageWithError";
    }
    else if ( importFileType.equals( ImportFileTypeType.DEPOSIT ) )
    {
      queryName = "com.biperf.core.domain.import.file.DepositRecordsByPageWithError";
    }
    else if ( importFileType.equals( ImportFileTypeType.AWARD_LEVEL ) )
    {
      queryName = "com.biperf.core.domain.import.file.AwardLevelRecordsByPageWithError";
    }
    else if ( importFileType.equals( ImportFileTypeType.HIERARCHY ) )
    {
      queryName = "com.biperf.core.domain.import.file.HierarchyRecordsByPageWithError";
    }
    else if ( importFileType.equals( ImportFileTypeType.QUIZ ) )
    {
      queryName = "com.biperf.core.domain.import.file.QuizRecordsByPageWithError";
    }
    else if ( importFileType.equals( ImportFileTypeType.GQ_PROGRESS_DATA_LOAD ) )
    {
      queryName = "com.biperf.core.domain.import.file.ProgressRecordsByPageWithError";
    }
    else if ( importFileType.equals( ImportFileTypeType.GQ_BASE_DATA_LOAD ) )
    {
      queryName = "com.biperf.core.domain.import.file.PaxBaseRecordsByPageWithError";
    }
    else if ( importFileType.equals( ImportFileTypeType.GQ_GOAL_DATA_LOAD ) )
    {
      queryName = "com.biperf.core.domain.import.file.PaxGoalRecordsByPageWithError";
    }
    else if ( importFileType.equals( ImportFileTypeType.GQ_VIN_LOAD ) )
    {
      queryName = "com.biperf.core.domain.import.file.AutoVinRecordsByPageWithError";
    }
    else if ( importFileType.equals( ImportFileTypeType.CP_PROGRESS_DATA_LOAD ) )
    {
      queryName = "com.biperf.core.domain.import.file.CPProgressRecordsByPageWithError";
    }
    else if ( importFileType.equals( ImportFileTypeType.CP_BASE_DATA_LOAD ) )
    {
      queryName = "com.biperf.core.domain.import.file.PaxCPBaseRecordsByPageWithError";
    }
    else if ( importFileType.equals( ImportFileTypeType.CP_LEVEL_DATA_LOAD ) )
    {
      queryName = "com.biperf.core.domain.import.file.PaxCPLevelRecordsByPageWithError";
    }
    else if ( importFileType.equals( ImportFileTypeType.LEADERBOARD ) )
    {
      queryName = "com.biperf.core.domain.import.file.LeaderBoardImportRecordsByPageWithError";
    }
    else if ( importFileType.equals( ImportFileTypeType.BADGE ) )
    {
      queryName = "com.biperf.core.domain.import.file.BadgeImportRecordsByPageWithError";
    }
    else if ( importFileType.equals( ImportFileTypeType.TD_PROGRESS_DATA_LOAD ) )
    {
      queryName = "com.biperf.core.domain.import.file.TDProgressRecordsByPageWithError";
    }
    else if ( importFileType.equals( ImportFileTypeType.BUDGET_DISTRIBUTION ) )
    {
      queryName = "com.biperf.core.domain.import.file.BudgetDistributionRecordsByPageWithError";
    }
    else if ( importFileType.equals( ImportFileTypeType.NOMINATION_APPROVER ) )
    {
      queryName = "com.biperf.core.domain.import.file.NominationCustomApproverRecordsByPageWithError";
    }
    else if ( importFileType.equals( ImportFileTypeType.SSI_PROGRESS_DATA_LOAD ) )
    {
      queryName = "com.biperf.core.domain.import.file.SSIProgressRecordsByPageWithError";
    }
    else if ( importFileType.equals( ImportFileTypeType.SSI_CONTEST_OBJ ) )
    {
      queryName = "com.biperf.core.domain.fileload.SSIObjContestImportRecordsByPageWithError";
    }
    else if ( importFileType.equals( ImportFileTypeType.SSI_CONTEST_DTGT ) )
    {
      queryName = "com.biperf.core.domain.fileload.SSIDtgtContestImportRecordsByPageWithError";
    }
    else if ( importFileType.equals( ImportFileTypeType.SSI_CONTEST_SIU ) )
    {
      queryName = "com.biperf.core.domain.fileload.SSISiuContestImportRecordsByPageWithError";
    }
    else if ( importFileType.equals( ImportFileTypeType.SSI_CONTEST_ATN ) )
    {
      queryName = "com.biperf.core.domain.fileload.SSIAtnContestImportRecordsByPageWithError";
    }
    else if ( importFileType.equals( ImportFileTypeType.SSI_CONTEST_SR ) )
    {
      queryName = "com.biperf.core.domain.fileload.SSISrContestImportRecordsByPageWithError";
    }

    return queryName;
  }

  // Code Fix for bug 18142
  private String getQueryNameForHierarchyRecordsWithError()
  {
    String queryName = "com.biperf.core.domain.fileload.GetHierarchyFileLevelErrorRecords";
    return queryName;
  }

  private String getQueryNameForDisplayComments( String importFileType )
  {
    String queryName = "";
    if ( importFileType.equals( ImportFileTypeType.DEPOSIT ) )
    {
      queryName = "com.biperf.core.domain.import.file.DepositRecordsWithoutMessage";
    }
    else if ( importFileType.equals( ImportFileTypeType.AWARD_LEVEL ) )
    {
      queryName = "com.biperf.core.domain.import.file.AwardLevelRecordsWithoutMessage";
    }

    return queryName;
  }

  public boolean verifyRecordsComments( Long id, String importFileType )
  {
    String queryName = getQueryNameForDisplayComments( importFileType );
    Query query = getSession().getNamedQuery( queryName );
    query.setParameter( "importFileId", id );
    return !CollectionUtils.isEmpty( query.list() );
  }

}
