/*
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/dao/participant/hibernate/AudienceDAOImpl.java,v $
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.dao.participant.hibernate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.participant.AudienceDAO;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.participant.Audience;
import com.biperf.core.domain.participant.AudienceRole;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.participant.impl.AudienceListValueBean;
import com.biperf.core.utils.hibernate.HibernateUtil;
import com.biperf.core.value.participant.AudienceDetail;

/**
 * AudienceDAOImpl.
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
 * <td>sharma</td>
 * <td>Jun 14, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public class AudienceDAOImpl extends BaseDAO implements AudienceDAO
{
  private static final Log logger = LogFactory.getLog( AudienceDAOImpl.class );
  /**
   * Return code returned from the stored procedure
   */
  public static final String OUTPUT_RETURN_CODE = "p_out_return_code";

  /**
    * Stored proc returns this code when the stored procedure executed without errors
    */
  public static final String GOOD = "00";

  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------
  private DataSource dataSource;

  // ---------------------------------------------------------------------------
  // Persistence Methods
  // ---------------------------------------------------------------------------

  public List getAll()
  {
    return getSession().getNamedQuery( "com.biperf.core.domain.participant.AllAudiences" ).list();
  }

  public List getAllCriteriaAudiences()
  {
    return getSession().getNamedQuery( "com.biperf.core.domain.participant.AllCriteriaAudiences" ).list();
  }

  public List getAllPaxAudiencesByParticipantId( Long participantId )
  {
    Query namedQuery = getSession().getNamedQuery( "com.biperf.core.domain.participant.AllPaxAudiencesForParticipant" );
    namedQuery.setLong( "participantId", participantId.longValue() );
    return namedQuery.list();
  }

  public Audience getAudienceById( Long audienceId )
  {
    return (Audience)getSession().get( Audience.class, audienceId );
  }

  /**
   * Get the audience from database by name
   * 
   * @param name
   * @return Audience
   */
  public Audience getAudienceByName( String name )
  {
    Query namedQuery = getSession().getNamedQuery( "com.biperf.core.domain.participant.GetAudienceByName" );
    namedQuery.setParameter( "name", name );

    return (Audience)namedQuery.uniqueResult();
  }

  public Audience save( Audience audience )
  {
    if ( !audience.isNew() )
    {
      // hack: evict original so merge doesn't occur. TODO: make sure object being saved came from a
      // detached object as in ClaimService.saveClaim() and associated action/form (or use an update
      // association)
      Audience savedAudience = getAudienceById( audience.getId() );
      getSession().evict( savedAudience );
    }
    return (Audience)HibernateUtil.saveOrUpdateOrDeepMerge( audience );
  }

  public boolean isAudienceNameUnique( String audienceName )
  {
    boolean isUnique = true;

    Query query = getSession().getNamedQuery( "com.biperf.core.domain.product.AudienceByNameCount" );

    query.setParameter( "name", audienceName.toLowerCase() );

    Integer count = (Integer)query.uniqueResult();
    isUnique = count.intValue() == 0;

    return isUnique;
  }

  /**
   * 
   * check if there is a PaxAudience for the supplied audienceId and
   * particpantId
   *
   * @param audienceId
   * @param participantId
   * @return List of PaxAudience objects
   */
  public List checkPaxAudiencesByAudienceIdParticipantId( Long audienceId, Long participantId )
  {
    Query namedQuery = getSession().getNamedQuery( "com.biperf.core.domain.participant.CheckPaxAudiencesByPaxAudienceIdParticipantId" );
    namedQuery.setLong( "audienceId", audienceId.longValue() );
    namedQuery.setLong( "participantId", participantId.longValue() );
    return namedQuery.list();
  }

  /**
   * 
   * check if there is a Audience for the supplied audienceId and
   * particpantId
   *
   * @param audienceId
   * @param participantId
   * @return int 0 indicating false and 1 indicating true
   */
  public int checkAudiencesByAudienceIdParticipantId( Long audienceId, Long participantId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.participant.checkAudiencesByAudienceIdParticipantId" );
    query.setLong( "audienceId", audienceId.longValue() );
    query.setLong( "participantId", participantId.longValue() );
    Integer count = (Integer)query.uniqueResult();
    return count.intValue();
  }

  public boolean isPaxInPromotionPartnerAudiences( Long participantId, Long promotionId )
  {
    Query query = getSession().createQuery( "select count(*) from ParticipantPartner paxPartner where paxPartner.promotion.id= :promotionId and paxPartner.partner.id= :partnerId " );
    query.setLong( "promotionId", promotionId.longValue() );
    query.setLong( "partnerId", participantId.longValue() );
    Long count = (Long)query.uniqueResult();
    if ( count.longValue() > 0 )
    {
      return true;
    }
    return false;
  }

  public List getAudienceList()
  {
    Query namedQuery = getSession().getNamedQuery( "com.biperf.core.domain.audience.getAudienceList" );

    return namedQuery.list();
  }

  public Map rematchParticipantForAllCriteriaAudiences( Long participantId ) throws ServiceErrorException
  {
    CallPrcRematchParticipantForAllCriteriaAudiences storedProc = new CallPrcRematchParticipantForAllCriteriaAudiences( dataSource );

    Map<String, Object> outParams = storedProc.executeProcedure( participantId );

    BigDecimal returnCode = (BigDecimal)outParams.get( "po_returncode" );

    if ( returnCode.intValueExact() != 0 )
    {
      throw new ServiceErrorException( "Stored procedure returned error. Procedure returned: " + outParams.get( "po_returncode" ) );
    }

    return outParams;
  }

  public Map rematchNodeForAllCriteriaAudiences( Long sourceNodeId, Long destinationPaxNodeId, Long destinationChildNodeId )
  {
    CallPrcRematchNodeForAllCriteriaAudiences storedProc = new CallPrcRematchNodeForAllCriteriaAudiences( dataSource );
    return storedProc.executeProcedure( sourceNodeId, destinationPaxNodeId, destinationChildNodeId );
  }

  public Map recreateCriteriaAudienceParticipants( Long audienceId )
  {
    CallPrcRecreateCriteriaAudienceParticipants storedProc = new CallPrcRecreateCriteriaAudienceParticipants( dataSource );
    return storedProc.executeProcedure( audienceId );
  }

  // This method will fetch the primary audience of a promotion who belong to the logged in manager
  // or owner node.
  public boolean isPromotionPrimaryAudienceInManagerNode( List<Node> nodes, Long promotionId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.audience.getPromotionPrimaryAduienceCountInManagerOrOwnersNode" );
    query.setLong( "promotionId", promotionId );

    for ( Node node : nodes )
    {
      query.setLong( "nodeId", node.getId() );
      Long count = (Long)query.uniqueResult();
      if ( count.longValue() > 0 )
      {
        return true;
      }
    }
    return false;
  }

  @Override
  public Map deleteAudience( Long audienceId )
  {
    CallPrcAudienceRemove procedure = new CallPrcAudienceRemove( dataSource );
    Map results = procedure.executeProcedure( audienceId );
    if ( GOOD.equals( results.get( OUTPUT_RETURN_CODE ) ) )
    {
      // TODO
      // In order to do this effectively, we will need to select all participants ids from the index
      // who are members of the audience ID being deleted.
      // Then, we bulk index those users. 2 seperate calls
      logger.error( "AudienceDAOImpl - TODO: deleteAudience apply asynchronous hook into Indexing System for AudienceId " + audienceId + "  HERE" );
    }
    return results;
  }

  public void setDataSource( DataSource dataSource )
  {
    this.dataSource = dataSource;
  }

  public AudienceRole updateAudienceRole( AudienceRole audienceRole )
  {
    return (AudienceRole)HibernateUtil.saveOrUpdateOrShallowMerge( audienceRole );
  }

  public List<Audience> getAudienceListFromAudienceRole( String roleCode )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.participant.getAudienceListFromAudienceRole" );
    query.setParameter( "code", roleCode );

    return query.list();
  }

  public List<AudienceRole> getAllAudienceRole()
  {
    return getSession().getNamedQuery( "com.biperf.core.domain.participant.getAllAudienceRole" ).list();
  }

  public void deleteAudienceRole( AudienceRole audienceRole )
  {
    getSession().delete( audienceRole );
  }

  public String getAudienceNameById( Long audienceId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.audience.getAudienceNameById" );
    query.setParameter( "audienceId", audienceId );

    return (String)query.uniqueResult();
  }

  public boolean isUserInDIYCommAudience( Long userId, String roleCode )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.audience.isUserInDIYCommAudience" );
    query.setParameter( "userId", userId );
    query.setParameter( "code", roleCode );
    int count = (Integer)query.uniqueResult();
    if ( count > 0 )
    {
      return true;
    }
    return false;
  }

  @Override
  public Map<String, Object> getCriteriaAudienceActiveandInactive( Map<String, Object> inputParameters )
  {
    CallPrcActiveandInactiveAudience procedure = new CallPrcActiveandInactiveAudience( dataSource );
    return procedure.executeProcedure( inputParameters );
  }

  // TODO : We will try to convert into criteria.
  @SuppressWarnings( "unchecked" )
  @Override
  public List<AudienceListValueBean> getAudiencesForRosterSearchGroups( Long audienceId, String audienceName, String audienceType )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.audience.getAudiencesForRosterSearchGroups" );
    query.setParameter( "audienceId", audienceId );
    query.setParameter( "audienceName", audienceName );
    query.setParameter( "audienceType", audienceType );

    List<Object> audienceList = query.list();

    List<AudienceListValueBean> audience = new ArrayList<AudienceListValueBean>();

    audienceList.forEach( val ->
    {
      Object[] audienceObject = (Object[])val;

      AudienceListValueBean audienceListValueBean = new AudienceListValueBean();
      audienceListValueBean.setAudienceId( (Long)audienceObject[0] );
      audienceListValueBean.setAudienceName( (String)audienceObject[1] );
      audienceListValueBean.setAudienceType( (String)audienceObject[2] );
      audienceListValueBean.setDateModified( (String)audienceObject[3] );
      audienceListValueBean.setPublicAudience( Boolean.valueOf( (Boolean)audienceObject[4] ) );
      audienceListValueBean.setRosterAudienceId( (UUID)audienceObject[5] );
      audience.add( audienceListValueBean );

    } );

    return audience;

  }

  @SuppressWarnings( "unchecked" )
  @Override
  public List<Object> getAllUsersByAudienceId( Long audienceId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.audience.getAllUsersByAudienceId" );
    query.setParameter( "audienceId", audienceId );
    return query.list();
  }

  @Override
  public Long getAudienceIdByRosterAudienceId( UUID rosterAudienceId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.audience.getAudienceIdByRosterAudienceId" );
    query.setParameter( "rosterAudienceId", rosterAudienceId.toString() );

    return (Long)query.uniqueResult();
  }

  @Override
  public UUID getRosterAudienceIdByAudienceId( Long audienceId )
  {

    Query query = getSession().getNamedQuery( "com.biperf.core.domain.audience.getRosterAudienceIdByAudienceId" );
    query.setParameter( "audienceId", audienceId );
    return (UUID)query.uniqueResult();
  }

  @Override
  public List<String> getRosterAudienceIdsByAudienceIds( List<Long> audienceIds )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.audience.getRosterAudienceIdsByAudienceIds" );
    query.setParameterList( "audienceIds", audienceIds );
    return (List<String>)query.list();
  }

  @Override
  public List<AudienceDetail> getAudienceDetailsByUserId( Long userId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.audience.getAudienceDetailsByUserId" );
    query.setParameter( "userId", userId );
    query.setResultTransformer( Transformers.aliasToBean( AudienceDetail.class ) );
    return (List<AudienceDetail>)query.list();
  }

  @Override
  public Audience getAudienceByRosterAudienceId( UUID rosterAudienceId )
  {
    Criteria searchCriteria = getSession().createCriteria( Audience.class );
    searchCriteria.add( Restrictions.eq( "rosterAudienceId", rosterAudienceId ) );
    return (Audience)searchCriteria.uniqueResult();
  }

}
