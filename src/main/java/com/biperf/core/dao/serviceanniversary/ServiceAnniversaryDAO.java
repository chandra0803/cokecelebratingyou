/*
 * (c) 2017 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.dao.serviceanniversary;

import java.util.List;
import java.util.UUID;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.serviceanniversary.SACelebrationInfo;
import com.biperf.core.domain.serviceanniversary.SAInviteAndContributeInfo;
import com.biperf.core.value.serviceanniversary.SAValueBean;

/**
 * 
 * @author palaniss
 * @since Nov 01, 2018
 * 
 */
public interface ServiceAnniversaryDAO extends DAO
{
  public static final String BEAN_NAME = "serviceAnniversaryDAO";

  void saveSAInvitationInfo( SACelebrationInfo serviceAwardDetails );

  SACelebrationInfo getSAInvitationInfo( Long programId, UUID celebrationId, UUID companyId );

  void saveSAInviteAndContributeInfo( SAInviteAndContributeInfo saInviteAndContributeInfo );

  List<SAInviteAndContributeInfo> getSAInviteAndContributeInfoByContributorPersonId( Long contributorPersonId );

  SAInviteAndContributeInfo getSAInviteAndContributeInfoByPersonIdAndCelebrationId( Long contributorPersonId, UUID CelebrationId ) throws Exception;

  List<Long> getEligibleSAProgramsForContributor( Long contributorPersonId );

  List<SAValueBean> getAllPendingSAContributions( Long contributorPersonId, Long programId );

  List<SAValueBean> getSACelebrationsByRecipient( Long recipientPersonId, int numOfDays );

  List<SAValueBean> getContributionStatusCountByCelebrationId( String celebrationId );

  List<SAValueBean> getSAGiftCodeReminderListForAlerts( Long recipientPersonId );

  String getCelebrationId( Long purlContributorId );

  String getCelebrationIdByClaim( Long claimId, Long recipientId, int numOfDays );

  List<SACelebrationInfo> getUpcomingCelebrationRecipients( int rowNumStart, int rowNumEnd, String sortedBy, String sortedOn, int pageSize );

  int getUpcomingCelebrationCount();

  String getRecipientName( String celebrationId );

}
