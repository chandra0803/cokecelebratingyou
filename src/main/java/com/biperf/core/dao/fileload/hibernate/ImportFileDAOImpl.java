/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/dao/fileload/hibernate/ImportFileDAOImpl.java,v $
 */

package com.biperf.core.dao.fileload.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.fileload.ImportFileDAO;
import com.biperf.core.domain.enums.ImportFileStatusType;
import com.biperf.core.domain.enums.ImportFileTypeType;
import com.biperf.core.domain.fileload.ImportFile;
import com.biperf.core.service.AssociationRequest;
import com.biperf.core.utils.HibernateSessionManager;

/**
 * CountryDAOImpl.
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
 * <td>Jun 2, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public class ImportFileDAOImpl extends BaseDAO implements ImportFileDAO
{
  /**
   * Deletes the given import file.
   * 
   * @param importFile the import file to delete.
   */
  public void deleteImportFile( ImportFile importFile )
  {
    getSession().delete( importFile );
  }

  public void deleteImportFile( Long importFileId )
  {

    Query query = getSession().getNamedQuery( "com.biperf.core.domain.fileload.DeleteStagedFile" );
    query.setParameter( "importFileId", importFileId );

    query.executeUpdate();
  }

  /**
   * Gets an import file given its import file ID.
   * 
   * @param id the import file ID of the import file to get.
   * @return the specified import file.
   */
  public ImportFile getImportFile( Long id )
  {
    return (ImportFile)getSession().get( ImportFile.class, id );
  }

  /**
   * Gets an import file given its ID.
   * 
   * @param id the ID of the import file to get.
   * @param associationRequest hydrates component collections of the specified import file.
   * @return the specified import file.
   */
  public ImportFile getImportFile( Long id, AssociationRequest associationRequest )
  {
    ImportFile importFile = (ImportFile)getSession().get( ImportFile.class, id );

    if ( associationRequest != null && importFile != null )
    {
      associationRequest.execute( importFile );
    }

    return importFile;
  }

  /**
   * Gets an import file given the associated promotion id.
   * 
   * @param id the promotion ID  of the import file to get.
   * @return the specified import file.
   */
  public List<ImportFile> getImportFileByPromotionId( Long promotionId )
  {
    Session session = HibernateSessionManager.getSession();
    Query query = session.getNamedQuery( "com.biperf.core.domain.importfile.getImportFileByPromotionId" );
    query.setLong( "promotionId", promotionId );

    return query.list();
  }

  /**
   * Returns the import files that meet the given criteria.
   * 
   * @param fileNamePrefix a prefix of the import file name or null if import file name is not a
   *          selection criteria.
   * @param fileType the import file status or null if file type is not a selection criteria.
   * @param status the import file status or null if import file status is not a selection criteria.
   * @param startDate the earliest staging date.
   * @param endDate the latest staging date.
   * @return the import files that meet the given criteria, as a <code>List</code> of
   *         {@link ImportFile} objects.
   */
  public List getImportFiles( String fileNamePrefix, ImportFileTypeType fileType, ImportFileStatusType status, Date startDate, Date endDate )
  {
    Criteria criteria = getSession().createCriteria( ImportFile.class );

    if ( fileNamePrefix != null && fileNamePrefix.length() > 0 )
    {
      // The method Restrictions.ilike implements a case-insensitive "like" operator.
      criteria.add( Restrictions.ilike( "fileName", fileNamePrefix + "%" ) );
    }

    if ( fileType != null )
    {
      criteria.add( Restrictions.eq( "fileType", fileType ) );
    }

    if ( status != null )
    {
      criteria.add( Restrictions.eq( "status", status ) );
    }

    if ( startDate != null )
    {
      if ( status != null )
      {
        if ( status.getCode().equalsIgnoreCase( ImportFileStatusType.STAGED ) )
        {
          criteria.add( Restrictions.ge( "dateStaged", startDate ) );
        }
        else if ( status.getCode().equalsIgnoreCase( ImportFileStatusType.VERIFIED ) )
        {
          criteria.add( Restrictions.ge( "dateVerified", startDate ) );
        }
        else if ( status.getCode().equalsIgnoreCase( ImportFileStatusType.IMPORTED ) )
        {
          criteria.add( Restrictions.ge( "dateImported", startDate ) );
        }
      }
      else
      // (status == null) => any status
      {
        // (date_created >= startDate)
        // OR ((date_verified IS NOT NULL) AND (date_verified >= startDate))
        // OR ((date_imported IS NOT NULL) AND (date_imported >= startDate))

        Criterion criterion1 = Restrictions.ge( "dateStaged", startDate );

        Criterion criterion2 = Restrictions.conjunction().add( Restrictions.isNotNull( "dateVerified" ) ).add( Restrictions.ge( "dateVerified", startDate ) );

        Criterion criterion3 = Restrictions.conjunction().add( Restrictions.isNotNull( "dateImported" ) ).add( Restrictions.ge( "dateImported", startDate ) );

        Criterion criterion4 = Restrictions.disjunction().add( criterion1 ).add( criterion2 ).add( criterion3 );

        criteria.add( criterion4 );
      }
    }

    if ( endDate != null )
    {
      if ( status != null )
      {
        if ( status.getCode().equalsIgnoreCase( ImportFileStatusType.STAGED ) )
        {
          criteria.add( Restrictions.le( "dateStaged", endDate ) );
        }
        else if ( status.getCode().equalsIgnoreCase( ImportFileStatusType.VERIFIED ) )
        {
          criteria.add( Restrictions.le( "dateVerified", endDate ) );
        }
        else if ( status.getCode().equalsIgnoreCase( ImportFileStatusType.IMPORTED ) )
        {
          criteria.add( Restrictions.le( "dateImported", endDate ) );
        }
      }
      else
      // (status == null) => any status
      {
        // (date_created <= endDate)
        // OR ((date_verified IS NOT NULL) AND (date_verified <= endDate))
        // OR ((date_imported IS NOT NULL) AND (date_imported <= endDate))

        Criterion criterion1 = Restrictions.le( "dateStaged", endDate );

        Criterion criterion2 = Restrictions.conjunction().add( Restrictions.isNotNull( "dateVerified" ) ).add( Restrictions.le( "dateVerified", endDate ) );

        Criterion criterion3 = Restrictions.conjunction().add( Restrictions.isNotNull( "dateImported" ) ).add( Restrictions.le( "dateImported", endDate ) );

        Criterion criterion4 = Restrictions.disjunction().add( criterion1 ).add( criterion2 ).add( criterion3 );

        criteria.add( criterion4 );
      }
    }

    return criteria.list();
  }

  public List getImportFileIdsByDateImported( Date dateImported )
  {
    Session session = HibernateSessionManager.getSession();
    Query query = session.getNamedQuery( "com.biperf.core.domain.fileload.getImportFileIdsByDateImported" );
    query.setParameter( "dateImported", dateImported );
    List results = query.list();
    return results;
  }

  public List getParticipantIdsToResendWelcomeEmailByImportFileId( Long importFileId )
  {
    Session session = HibernateSessionManager.getSession();
    Query query = session.getNamedQuery( "com.biperf.core.domain.fileload.getParticipantIdsToResendWelcomeEmailByImportFileId" );
    query.setParameter( "importFileId", importFileId );
    List results = query.list();
    return results;
  }

  /**
   * Saves the given import file.
   * 
   * @param importFile the import file to save.
   * @return the saved version of the import file.
   */
  public ImportFile saveImportFile( ImportFile importFile )
  {
    Session session = HibernateSessionManager.getSession();
    session.saveOrUpdate( importFile );
    session.flush();

    return importFile;
  }

  public Date getLastFileLoadDateForPromotionAndRound( Long promotionId, int roundNumber )
  {
    Session session = HibernateSessionManager.getSession();
    Query query = session.getNamedQuery( "com.biperf.core.domain.fileload.getLastFileLoadDateForPromotionAndRound" );
    query.setParameter( "promotionId", promotionId );
    query.setParameter( "roundNumber", roundNumber );
    return (Date)query.uniqueResult();
  }

  public Date getLastFileLoadDateForPromotion( Long promotionId )
  {
    Session session = HibernateSessionManager.getSession();
    Query query = session.getNamedQuery( "com.biperf.core.domain.fileload.getLastFileLoadDateForPromotion" );
    query.setParameter( "promotionId", promotionId );
    return (Date)query.uniqueResult();
  }

}
