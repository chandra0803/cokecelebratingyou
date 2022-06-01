
package com.biperf.core.service.serviceanniversary;

import java.util.List;
import java.util.UUID;

import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.purl.PurlCelebration;
import com.biperf.core.domain.serviceanniversary.SACelebrationInfo;
import com.biperf.core.domain.serviceanniversary.SAInviteAndContributeInfo;
import com.biperf.core.exception.DataException;
import com.biperf.core.service.SAO;
import com.biperf.core.utils.CmxTranslateHelperBean;
import com.biperf.core.value.AlertsValueBean;
import com.biperf.core.value.serviceanniversary.SAValueBean;

public interface ServiceAnniversaryService extends SAO
{
  public static final String BEAN_NAME = "serviceAnniversaryService";

  boolean saveSACelebrationInfo( SACelebrationInfo serviceAwardDetails ) throws Exception;

  SACelebrationInfo getSACelebrationInfo( Long programId, UUID celebrationId, UUID companyId ) throws Exception;

  String getContributePageUrl( String invitationId, String uuid ) throws Exception;

  boolean saveSAInviteAndContributeInfo( SAInviteAndContributeInfo saInviteAndContributeInfo ) throws Exception;

  List<SAInviteAndContributeInfo> getSAInviteAndContributeInfoByContributorPersonId( Long contributorPersonId ) throws Exception;

  SAInviteAndContributeInfo getSAInviteAndContributeInfoByPersonIdAndCelebrationId( Long contributorPersonId, UUID CelebrationId ) throws Exception;

  List<AlertsValueBean> getPendingSAContributionsForAlerts( Participant contributor, CmxTranslateHelperBean cmxTranslateHelper ) throws Exception;

  List<SAValueBean> getAllPendingSAContributions( Long contributorPersonId, Long programId ) throws Exception;

  List<AlertsValueBean> getSACelebrationsByRecipient( Long recipientPersonId, int numOfDays, CmxTranslateHelperBean cmxTranslateHelper ) throws Exception;

  List<AlertsValueBean> getSAGiftCodeReminderListForAlerts( Long recipientPersonId, CmxTranslateHelperBean cmxTranslateHelper ) throws Exception;

  String getGiftCodePageUrl( String celebrationId ) throws Exception;

  String getCelebrationId( Long purlContributorId ) throws DataException;

  String getCelebrationIdByClaim( Long claimId, Long recipientId, int numOfDays ) throws DataException;

  List<PurlCelebration> getUpcomingCelebrationRecipients( int pageSize, String sortedBy, String sortedOn, int startIndex );

  int getUpcomingCelebrationCount();

  String getRecipientName( String celebrationId );
}
