
package com.biperf.core.dao.recognitionadvisor;

import java.util.List;

import com.biperf.core.dao.DAO;
import com.biperf.core.ui.recognitionadvisor.EligibleProgramBean;
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

public interface RecognitionAdvisorDao extends DAO
{
  public List<RecognitionAdvisorValueBean> showRAReminderPaxData( Long userId,
                                                                  Long rowStart,
                                                                  Long rowEnd,
                                                                  String sortColumnName,
                                                                  String sortBy,
                                                                  Long excludeUpcoming,
                                                                  String filterValue,
                                                                  Long pendingStatus );

  public List<RecognitionAdvisorValueBean> getRATeamMemberReminderData( Long paxId );

  public List<RecognitionAdvisorValueBean> getRANewHireForEmail( Long paxId );

  public Long getLongOverDueNewHireForManager( Long participantId );

  public List<AlertsValueBean> checkNewEmployeeAndTeamMemberExist( Long participantId );

  public List<EligibleProgramBean> getEligiblePromotions( Long userId );

  List<RecognitionAdvisorUnusedBudgetBean> getRAUnusedBudgetReminderData( Long participantId );

  public boolean isHavingMembers( Long participantId );

}
