
package com.biperf.core.domain.enums;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.process.AdminTestCelebMgrNonResponseEmailProcess;
import com.biperf.core.process.AdminTestCelebMgrNotificationEmailProcess;
import com.biperf.core.process.AdminTestCelebRecogReceivedEmailProcess;
import com.biperf.core.process.AdminTestCelebrationPurlRecipientEmailProcess;
import com.biperf.core.process.AdminTestPurlContributorsInvitationEmailProcess;
import com.biperf.core.process.AdminTestPurlMgrNotificationEmailProcess;
import com.biperf.core.process.AdminTestPurlRecipientEmailProcess;
import com.biperf.core.process.AdminTestRecogPurlContributorsNonResponseEmailProcess;
import com.biperf.core.process.AdminTestRecogPurlManagersNonResponseEmailProcess;
import com.biperf.core.process.PurlArchiveProcess;
import com.biperf.core.process.PurlAutoSendProcess;
import com.biperf.core.process.PurlAwardProcess;
import com.biperf.core.process.PurlInvitePostProcess;
import com.biperf.core.process.PurlMediaUploadProcess;
import com.biperf.core.process.PurlSubmissionProcess;

public enum SAProcessRemoveEnum
{
  ADMINTESTCELEBRATIONPURLRECIPIENTEMAILPROCESS( AdminTestCelebrationPurlRecipientEmailProcess.BEAN_NAME ), ADMINTESTCELEBMGRNONRESPONSEEMAILPROCESS(
      AdminTestCelebMgrNonResponseEmailProcess.BEAN_NAME ), ADMINTESTCELEBMGRNOTIFICATIONEMAILPROCESS( AdminTestCelebMgrNotificationEmailProcess.BEAN_NAME ), ADMINTESTCELEBRECOGRECEIVEDEMAILPROCESS(
          AdminTestCelebRecogReceivedEmailProcess.BEAN_NAME ), ADMINTESTPURLCONTRIBUTORSINVITATIONEMAILPROCESS(
              AdminTestPurlContributorsInvitationEmailProcess.BEAN_NAME ), ADMINTESTPURLMGRNOTIFICATIONEMAILPROCESS(
                  AdminTestPurlMgrNotificationEmailProcess.BEAN_NAME ), ADMINTESTPURLRECIPIENTEMAILPROCESS(
                      AdminTestPurlRecipientEmailProcess.BEAN_NAME ), ADMINTESTRECOGPURLCONTRIBUTORSNONRESPONSEEMAILPROCESS(
                          AdminTestRecogPurlContributorsNonResponseEmailProcess.BEAN_NAME ), ADMINTESTRECOGPURLMANAGERSNONRESPONSEEMAILPROCESS(
                              AdminTestRecogPurlManagersNonResponseEmailProcess.BEAN_NAME ), PURLARCHIVEPROCESS( PurlArchiveProcess.BEAN_NAME ), PURLAUTOSENDPROCESS(
                                  PurlAutoSendProcess.BEAN_NAME ), PURLAWARDPROCESS( PurlAwardProcess.BEAN_NAME ), PURLINVITEPOSTPROCESS(
                                      PurlInvitePostProcess.BEAN_NAME ), PURLMEDIAUPLOADPROCESS( PurlMediaUploadProcess.BEAN_NAME ), PURLSUBMISSIONPROCESS( PurlSubmissionProcess.BEAN_NAME );

  private final String processName;

  private SAProcessRemoveEnum( String processName )
  {
    this.processName = processName;
  }

  public static List<String> getProcessList()
  {
    List<String> process = new ArrayList<>();

    for ( SAProcessRemoveEnum value : SAProcessRemoveEnum.values() )
    {
      process.add( value.processName );
    }
    return process;
  }
}
