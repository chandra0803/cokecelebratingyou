
package com.biperf.core.dao.diycommunications.hibernate;

import java.util.List;

import org.hibernate.Query;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.diycommunications.ParticipantDIYCommunicationsDAO;
import com.biperf.core.dao.reports.hibernate.BaseReportsResultTransformer;
import com.biperf.core.domain.diycommunications.DIYCommunications;
import com.biperf.core.domain.participant.Audience;
import com.biperf.core.utils.hibernate.HibernateUtil;
import com.biperf.core.value.FormattedValueBean;

public class ParticipantDIYCommunicationsDAOImpl extends BaseDAO implements ParticipantDIYCommunicationsDAO
{
  public List<DIYCommunications> getActiveByCommunicationType( Long managerId, String communicationType )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.diycommunications.getActiveByCommunicationType" );
    query.setParameter( "managerId", managerId );
    query.setParameter( "communicationType", communicationType );

    return query.list();
  }

  public List<DIYCommunications> getArchievedByCommunicationType( Long managerId, String communicationType )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.diycommunications.getArchievedByCommunicationType" );
    query.setParameter( "managerId", managerId );
    query.setParameter( "communicationType", communicationType );

    return query.list();
  }

  public DIYCommunications getDIYCommunicationsById( Long id )
  {
    return (DIYCommunications)getSession().get( DIYCommunications.class, id );
  }

  public DIYCommunications saveDIYCommunications( DIYCommunications diyCommunications )
  {
    return (DIYCommunications)HibernateUtil.saveOrUpdateOrShallowMerge( diyCommunications );
  }

  public List<Audience> getPublicAudienceList()
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.diycommunications.getPublicAudienceList" );

    return query.list();
  }

  public DIYCommunications getCommunicationsByTitleAndType( String communicationsTitle, String communicationsType )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.diycommunications.getCommunicationsByTitleAndType" );
    query.setParameter( "communicationsTitle", communicationsTitle.toLowerCase() );
    query.setParameter( "communicationsType", communicationsType );

    return (DIYCommunications)query.uniqueResult();
  }

  @Override
  public List<DIYCommunications> getActiveByCommunicationType( String communicationType )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.diycommunications.getAllActiveByCommunicationType" );
    query.setParameter( "communicationType", communicationType );

    return query.list();
  }

  @Override
  public List<FormattedValueBean> getAudienceParticipants( Long audienceId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.participant.getUsersByIds" );
    query.setParameter( "audienceId", audienceId );
    query.setResultTransformer( new ParticipantRowMapper() );
    return query.list();
  }

  private class ParticipantRowMapper extends BaseReportsResultTransformer
  {
    public FormattedValueBean transformTuple( Object[] tuple, String[] aliases )
    {
      FormattedValueBean valueBean = new FormattedValueBean();
      valueBean.setId( extractLong( tuple[0] ) );
      valueBean.setFnameLname( extractString( tuple[1] ) + " " + extractString( tuple[2] ) );
      return valueBean;
    }
  }
}
