
package com.biperf.core.mobileapp.recognition.service;

import java.util.List;

import com.biperf.core.mobileapp.recognition.domain.DeviceType;
import com.biperf.core.mobileapp.recognition.domain.UserDevice;
import com.biperf.core.service.SAO;
import com.biperf.core.value.recognitionadvisor.RecognitionAdvisorValueBean;

public interface MobileNotificationService extends SAO
{
  public static final String BEAN_NAME = "mobileNotificationService";

  public UserDevice createUserDeviceFor( Long userId, DeviceType deviceType, String deviceId, boolean debug );

  public int deleteUserDevice( Long userId, String deviceId );

  public int deleteUserDevices( Long userId );

  public void onRecognitionClaimSent( Long recognitionClaimId );

  public void budgetEndNotification( List<BudgetEndNotification> budgetEndNotifications );

  public void inactivityRecognitionNotification( List<RecognitionInactivityNotification> recognitionInactivityNotifications );

  public void purlContributorInviteNotification( String localeCode, String firstName, String lastName, Long purlRecipientId, String milestone, Long contributorId, boolean reminder );

  public void raTeamOverDueNotification( Long managerId, RecognitionAdvisorValueBean teamMember );

  public void raNewHireNotification( Long managerId, RecognitionAdvisorValueBean newMember );

}
