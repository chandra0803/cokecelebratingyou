/*
 * (c) 2017 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.dao.serviceanniversary.hibernate;

import java.util.List;
import java.util.UUID;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.serviceanniversary.ServiceAnniversaryDAO;
import com.biperf.core.domain.serviceanniversary.SACelebrationInfo;
import com.biperf.core.domain.serviceanniversary.SAInviteAndContributeInfo;
import com.biperf.core.utils.HibernateSessionManager;
import com.biperf.core.utils.SAConstants;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.serviceanniversary.SAValueBean;

/**
 * 
 * @author palaniss
 * @since Nov 01, 2018
 * 
 */
public class ServiceAnniversaryDAOImpl extends BaseDAO implements ServiceAnniversaryDAO
{

  @Override
  public void saveSAInvitationInfo( SACelebrationInfo serviceAwardDetails )
  {
    getSession().saveOrUpdate( serviceAwardDetails );
  }

  @Override
  public SACelebrationInfo getSAInvitationInfo( Long programId, UUID celebrationId, UUID companyId )
  {
    Criteria criteria = HibernateSessionManager.getSession().createCriteria( SACelebrationInfo.class );
    criteria.add( Restrictions.eq( "programId", programId ) );
    criteria.add( Restrictions.eq( "celebrationId", celebrationId ) );
    criteria.add( Restrictions.eq( "companyId", companyId ) );

    return (SACelebrationInfo)criteria.uniqueResult();
  }

  @Override
  public void saveSAInviteAndContributeInfo( SAInviteAndContributeInfo saInviteAndContributeInfo )
  {
    getSession().saveOrUpdate( saInviteAndContributeInfo );
  }

  @Override
  public List<SAInviteAndContributeInfo> getSAInviteAndContributeInfoByContributorPersonId( Long contributorPersonId )
  {
    Criteria criteria = HibernateSessionManager.getSession().createCriteria( SAInviteAndContributeInfo.class );
    criteria.add( Restrictions.eq( "contributorPersonId", contributorPersonId ) );

    return (List<SAInviteAndContributeInfo>)criteria.list();
  }

  @Override
  public SAInviteAndContributeInfo getSAInviteAndContributeInfoByPersonIdAndCelebrationId( Long contributorPersonId, UUID CelebrationId ) throws Exception
  {
    Criteria criteria = HibernateSessionManager.getSession().createCriteria( SAInviteAndContributeInfo.class );
    criteria.add( Restrictions.eq( "contributorPersonId", contributorPersonId ) );
    criteria.add( Restrictions.eq( "celebrationId", CelebrationId ) );
    return (SAInviteAndContributeInfo)criteria.uniqueResult();
  }

  @Override
  public List<Long> getEligibleSAProgramsForContributor( Long contributorPersonId )
  {
    String[] saContributionStates = new String[] { SAConstants.CONTRIBUTE_STATUS_INVITED };
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.serviceanniversary.SAInviteAndContributeInfo.getEligibleSAProgramsForContributor" );
    query.setParameterList( "contributionStates", saContributionStates );
    query.setParameter( "contributorPersonId", contributorPersonId );
    query.setParameter( "awardStatus", SAConstants.AWARD_STATUS_SCHEDULED );
    return query.list();
  }

  @Override
  public List<SAValueBean> getAllPendingSAContributions( Long contributorPersonId, Long programId )
  {
    String[] saContributionStates = new String[] { SAConstants.CONTRIBUTE_STATUS_INVITED };
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.serviceanniversary.SAInviteAndContributeInfo.getAllPendingSAContributions" );
    query.setParameterList( "contributionStates", saContributionStates );
    query.setParameter( "contributorPersonId", contributorPersonId );
    query.setParameter( "programId", programId );
    query.setParameter( "awardStatus", SAConstants.AWARD_STATUS_SCHEDULED );
    query.setResultTransformer( Transformers.aliasToBean( SAValueBean.class ) );
    return query.list();
  }

  @Override
  public List<SAValueBean> getSACelebrationsByRecipient( Long recipientPersonId, int numOfDays )
  {
    String[] saRecipientStates = new String[] { SAConstants.AWARD_STATUS_NOTIFIED, SAConstants.AWARD_STATUS_REWARDED };
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.serviceanniversary.SAInviteAndContributeInfo.getSACelebrationsByRecipient" );
    query.setParameter( "recipientPersonId", recipientPersonId );
    query.setParameter( "celebrationDisplayPeriod", numOfDays );
    query.setParameterList( "awardStatus", saRecipientStates );
    query.setResultTransformer( Transformers.aliasToBean( SAValueBean.class ) );
    return query.list();
  }

  @Override
  public List<SAValueBean> getContributionStatusCountByCelebrationId( String celebrationId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.serviceanniversary.SAInviteAndContributeInfo.getContributionStatusCountByCelebrationId" );
    query.setParameter( "celebrationId", celebrationId );
    query.setResultTransformer( Transformers.aliasToBean( SAValueBean.class ) );
    return query.list();

  }

  @Override
  public List<SAValueBean> getSAGiftCodeReminderListForAlerts( Long recipientPersonId )
  {
    String[] giftcodeStatus = new String[] { SAConstants.GIFTCODE_STATUS_ISSUED, SAConstants.GIFTCODE_STATUS_REISSUED };
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.serviceanniversary.SAInviteAndContributeInfo.getSAGiftCodeReminderList" );
    query.setParameter( "recipientPersonId", recipientPersonId );
    query.setParameterList( "giftcodeStatus", giftcodeStatus );
    query.setParameter( "awardStatus", SAConstants.AWARD_STATUS_NOTIFIED );
    query.setResultTransformer( Transformers.aliasToBean( SAValueBean.class ) );
    return query.list();
  }

  @Override
  public String getCelebrationId( Long purlContributorId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.serviceanniversary.SACelebrationInfo.getSACelebrationId" );
    query.setParameter( "purlContributorId", purlContributorId );
    query.setParameter( "contributionStates", SAConstants.CONTRIBUTE_STATUS_INVITED );
    query.setParameter( "awardStatus", SAConstants.AWARD_STATUS_SCHEDULED );
    return (String)query.uniqueResult();
  }

  @Override
  public String getCelebrationIdByClaim( Long claimId, Long recipientId, int numOfDays )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.serviceanniversary.SACelebrationInfo.getSACelebrationIdByClaim" );
    query.setParameter( "recipientPersonId", recipientId );
    query.setParameter( "celebrationDisplayPeriod", numOfDays );
    query.setParameter( "claimId", claimId );
    query.setParameter( "awardStatus", SAConstants.AWARD_STATUS_NOTIFIED );
    return (String)query.uniqueResult();
  }

  @Override
  public List<SACelebrationInfo> getUpcomingCelebrationRecipients( int rowNumStart, int rowNumEnd, String sortedBy, String sortedOn, int pageSize )
  {
    String queryString = "SELECT * FROM " + "(SELECT ROWNUM RANGE,A.* FROM " + "(select SC.* from SA_CELEBRATION_INFO SC,APPLICATION_USER AP,Program PG"
        + " WHERE PG.PROGRAM_ID = SC.PROGRAM_ID AND SC.RECIPIENT_ID = AP.USER_ID AND PG.ALLOW_CONTRIBUTION = 1 AND PG.PROGRAM_TYPE = 'recognition' AND SC.AWARD_STATUS = :awardStatus AND  SC.AWARD_DATE > sysdate AND"
        + " SC.RECIPIENT_ID <> :userId  ORDER BY :sortedOn :sortedBy) A) WHERE RANGE BETWEEN :rangeStart AND :rangeEnd";

    if ( "1".equals( sortedOn ) )
    {
      queryString = queryString.replace( ":sortedOn", "AP.LAST_NAME" + "," + "AP.FIRST_NAME" );
    }
    else
    {
      queryString = queryString.replace( ":sortedOn", "SC.AWARD_DATE" );
    }
    queryString = queryString.replace( ":sortedBy", sortedBy );

    SQLQuery query = getSession().createSQLQuery( queryString );

    query.setParameter( "userId", UserManager.getUser().getUserId() );
    query.setParameter( "awardStatus", SAConstants.AWARD_STATUS_SCHEDULED );
    query.setParameter( "rangeStart", rowNumStart + 1 );
    query.setParameter( "rangeEnd", rowNumEnd );
    query.addEntity( SACelebrationInfo.class );
    return query.list();
  }

  @Override
  public int getUpcomingCelebrationCount()
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.serviceanniversary.SACelebrationInfo.getUpcomingCelebrationCount" );
    query.setParameter( "userId", UserManager.getUser().getUserId() );
    query.setParameter( "awardStatus", SAConstants.AWARD_STATUS_SCHEDULED );
    return (Integer)query.uniqueResult();
  }

  @Override
  public String getRecipientName( String celebrationId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.serviceanniversary.SACelebrationInfo.getFNameLNameByRecipiantId" );
    query.setParameter( "celebrationId", celebrationId );
    String recipientName = (String)query.uniqueResult();
    return recipientName;
  }

}
