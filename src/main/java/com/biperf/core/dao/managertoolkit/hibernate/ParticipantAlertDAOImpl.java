
package com.biperf.core.dao.managertoolkit.hibernate;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.managertoolkit.ParticipantAlertDAO;
import com.biperf.core.domain.managertoolkit.ParticipantAlert;
import com.biperf.core.utils.hibernate.HibernateUtil;

public class ParticipantAlertDAOImpl extends BaseDAO implements ParticipantAlertDAO
{

  public ParticipantAlert saveParticipantAlert( ParticipantAlert participantAlert )
  {
    return (ParticipantAlert)HibernateUtil.saveOrUpdateOrDeepMerge( participantAlert );
  }

  public ParticipantAlert getAlertByPaxIdContestIdAndType( Long paxId, Long contestId, String type )
  {
    Criteria criteria = getSession().createCriteria( ParticipantAlert.class, "participantAlert" );
    criteria.createAlias( "participantAlert.user", "user" );
    criteria.createAlias( "participantAlert.alertMessage", "alertMessage" );
    criteria.add( Restrictions.eq( "user.id", paxId ) );
    criteria.add( Restrictions.eq( "alertMessage.contestId", contestId ) );
    criteria.add( Restrictions.eq( "alertMessage.ssiAlertType", type ) );
    return (ParticipantAlert)criteria.uniqueResult();
  }

}
