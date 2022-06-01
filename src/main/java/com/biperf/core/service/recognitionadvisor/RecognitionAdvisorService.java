
package com.biperf.core.service.recognitionadvisor;

import java.util.List;

import com.biperf.core.service.SAO;
import com.biperf.core.ui.recognitionadvisor.RAEligibleProgramsView;
import com.biperf.core.ui.recognitionadvisor.RecognitionAdvisorView;
import com.biperf.core.value.AlertsValueBean;
import com.biperf.core.value.recognitionadvisor.RecognitionAdvisorUnusedBudgetBean;
import com.biperf.core.value.recognitionadvisor.RecognitionAdvisorValueBean;

/**
 * Used for the RecognitonAdvisor Algorithm screen(s).
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
 * <td>Ramesh J</td>
 * <td>Dec 11, 2017</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */

public interface RecognitionAdvisorService extends SAO
{
  public static final String BEAN_NAME = "recognitionAdvisorService";

  public RecognitionAdvisorView showRAReminderPaxData( Long userId,
                                                       Long rowStart,
                                                       Long rowEnd,
                                                       String sortColumnName,
                                                       String sortBy,
                                                       Long excludeUpcoming,
                                                       String filterValue,
                                                       String activePage,
                                                       Long pendingStatus );

  public List<RecognitionAdvisorValueBean> showRAReminderNewHireEmailPaxData( Long paxId );

  public List<RecognitionAdvisorValueBean> getRATeamMemberReminderData( Long participantId );

  public Long getLongOverDueNewHireForManager( Long participantId );

  public List<AlertsValueBean> checkNewEmployeeAndTeamMemberExist( Long participantId );

  public RAEligibleProgramsView getEligiblePromotions( Long userId, boolean isRAEnabled );

  List<RecognitionAdvisorUnusedBudgetBean> getRAUnusedBudgetReminderData( Long participantId );

  public boolean isHavingMembers( Long participantId );

}
