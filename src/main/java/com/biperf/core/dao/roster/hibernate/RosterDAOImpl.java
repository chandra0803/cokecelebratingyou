
package com.biperf.core.dao.roster.hibernate;

import java.util.List;

import javax.sql.DataSource;

import org.hibernate.Query;
import org.hibernate.transform.Transformers;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.roster.RosterDAO;
import com.biperf.core.service.roster.value.AudienceDetails;
import com.biperf.core.service.roster.value.EmailDetails;
import com.biperf.core.service.roster.value.HierarchyDetails;
import com.biperf.core.service.roster.value.PhoneDetails;
import com.biperf.core.service.roster.value.UserAddressDetail;

public class RosterDAOImpl extends BaseDAO implements RosterDAO
{
  private DataSource dataSource;

  public void setDataSource( DataSource dataSource )
  {
    this.dataSource = dataSource;
  }

  @Override
  public Long getHierarchyIdByHierarchyUUId( String rosterHierarchyUUId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.roster.getHierarchyIdByHierarchyUUId" );
    query.setParameter( "rosterHierarchyUUId", rosterHierarchyUUId );

    return (Long)query.uniqueResult();
  }

  @Override
  public Long getUserIdByRosterPersonUUId( String rosterPersonUUId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.roster.getUserIdByRosterPersonUUId" );
    query.setParameter( "rosterPersonUUId", rosterPersonUUId );

    return (Long)query.uniqueResult();
  }

  @Override
  public Long getNodeIdByRosterNodeUUId( String rosterNodeUUId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.roster.getNodeIdByRosterNodeUUId" );
    query.setParameter( "rosterNodeUUId", rosterNodeUUId );

    return (Long)query.uniqueResult();
  }

  @Override
  public Long getAudienceIdByAudienceUUId( String rosterAudienceUUId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.roster.getAudienceIdByAudienceUUId" );
    query.setParameter( "rosterAudienceUUId", rosterAudienceUUId );

    return (Long)query.uniqueResult();
  }

  @Override
  public String getRosterPersonUUIdByUserId( Long userId )
  {

    Query query = getSession().getNamedQuery( "com.biperf.core.domain.roster.getRosterPersonUUIdByUserId" );
    query.setParameter( "userId", userId );
    return (String)query.uniqueResult();
  }

  @Override
  public String getAudienceUUIdByAudienceId( Long audienceId )
  {

    Query query = getSession().getNamedQuery( "com.biperf.core.domain.roster.getAudienceUUIdByAudienceId" );
    query.setParameter( "audienceId", audienceId );
    return (String)query.uniqueResult();
  }

  @Override
  public String getHierarchyUUIdByHierarchyId( Long hierarchyId )
  {

    Query query = getSession().getNamedQuery( "com.biperf.core.domain.roster.getHierarchyUUIdByHierarchyId" );
    query.setParameter( "hierarchyId", hierarchyId );
    return (String)query.uniqueResult();
  }

  @Override
  public String getNodeUUIdByNodeId( Long nodeId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.roster.getNodeUUIdByNodeId" );
    query.setParameter( "nodeId", nodeId );
    return (String)query.uniqueResult();
  }

  @Override
  public List<String> getAudienceUUIDsByAudienceIds( List<Long> audienceIds )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.roster.getAudienceUUIDsByAudienceIds" );
    query.setParameterList( "audienceIds", audienceIds );
    return (List<String>)query.list();
  }

  @Override
  public List<String> getNodeUUIDsByNodeIds( List<Long> allNodeIds )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.roster.getNodeUUIDsByNodeIds" );
    query.setParameterList( "allNodeIds", allNodeIds );
    return (List<String>)query.list();
  }

  @Override
  public List<AudienceDetails> getAudiencesDetailsByUserId( Long userId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.roster.getAudiencesDetailsByUserId" );
    query.setParameter( "userId", userId );
    query.setResultTransformer( Transformers.aliasToBean( AudienceDetails.class ) );
    return (List<AudienceDetails>)query.list();
  }

  @Override
  public List<HierarchyDetails> getHierarchyDetailsByHierarchyId( Long userId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.roster.getHierarchyDetailsByHierarchyId" );
    query.setParameter( "userId", userId );
    query.setResultTransformer( Transformers.aliasToBean( HierarchyDetails.class ) );
    return (List<HierarchyDetails>)query.list();
  }

  @Override
  public List<EmailDetails> getEmailAddressesByUserId( Long userId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.roster.getEmailAddressesByUserId" );
    query.setParameter( "userId", userId );
    query.setResultTransformer( Transformers.aliasToBean( EmailDetails.class ) );
    return (List<EmailDetails>)query.list();
  }

  @Override
  public List<PhoneDetails> getPhonesDetailsByUserId( Long userId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.roster.getPhonesDetailsByUserId" );
    query.setParameter( "userId", userId );
    query.setResultTransformer( Transformers.aliasToBean( PhoneDetails.class ) );
    return (List<PhoneDetails>)query.list();
  }

  @Override
  public List<UserAddressDetail> getAddressesDetailsByUserId( Long userId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.roster.getAddressesDetailsByUserId" );
    query.setParameter( "userId", userId );
    query.setResultTransformer( Transformers.aliasToBean( UserAddressDetail.class ) );
    return (List<UserAddressDetail>)query.list();
  }

}
