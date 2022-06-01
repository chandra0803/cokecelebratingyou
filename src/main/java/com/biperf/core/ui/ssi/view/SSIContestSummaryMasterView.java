
package com.biperf.core.ui.ssi.view;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.WebErrorMessage;
import com.biperf.core.value.ssi.SSIContestSummaryValueBean;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * 
 * SSIContestAdminDetailMasterView.
 * 
 * @author chowdhur
 * @since Jan 13, 2015
 */
@JsonInclude( value = Include.NON_NULL )
public class SSIContestSummaryMasterView
{
  private SSIContestSummaryParticipantView participants;
  private List<WebErrorMessage> messages;

  public SSIContestSummaryMasterView()
  {

  }

  public SSIContestSummaryMasterView( WebErrorMessage message )
  {
    this.messages = new ArrayList<WebErrorMessage>();
    this.messages.add( message );
  }

  public SSIContestSummaryMasterView( SSIContestSummaryValueBean detailValueBean )
  {
    super();

    SSIContestAdminDetailTabularDataView tabularData = new SSIContestAdminDetailTabularDataView( detailValueBean.getColumns(),
                                                                                                 detailValueBean.getSubColumns(),
                                                                                                 detailValueBean.getPaxResults(),
                                                                                                 detailValueBean.isFooterActive() );

    this.participants = new SSIContestSummaryParticipantView( detailValueBean.getContestType(),
                                                              detailValueBean.getPayoutType(),
                                                              detailValueBean.isIncludeBonus(),
                                                              detailValueBean.isIncludeBaseline(),
                                                              tabularData,
                                                              detailValueBean.getTotal(),
                                                              detailValueBean.getPerPage(),
                                                              detailValueBean.getCurrent(),
                                                              detailValueBean.getSortedBy(),
                                                              detailValueBean.getSortedOn() );

  }

  /**
   * @param multiOrgs
   * @param resultsType
   * @param orgUnits
   * @param participants
   */
  public SSIContestSummaryMasterView( SSIContestSummaryParticipantView participants )
  {
    super();
    this.participants = participants;
  }

  public void addPaginationParams( String sortedBy, String sortedOn, int current, int perPage )
  {
    this.participants.setSortedBy( sortedBy );
    this.participants.setSortedOn( sortedOn );
    this.participants.setCurrent( current );
    this.participants.setPerPage( perPage );
  }

  public SSIContestSummaryParticipantView getParticipants()
  {
    return participants;
  }

  public void setParticipants( SSIContestSummaryParticipantView participants )
  {
    this.participants = participants;
  }

  public List<WebErrorMessage> getMessages()
  {
    return messages;
  }

  public void setMessages( List<WebErrorMessage> messages )
  {
    this.messages = messages;
  }

}
