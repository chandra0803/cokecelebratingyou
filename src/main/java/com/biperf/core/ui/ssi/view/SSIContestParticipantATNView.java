
package com.biperf.core.ui.ssi.view;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Hibernate;

import com.biperf.core.domain.WebErrorMessage;
import com.biperf.core.domain.enums.DepartmentType;
import com.biperf.core.domain.enums.DynaPickListType;
import com.biperf.core.domain.enums.PositionType;
import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.domain.ssi.SSIContestParticipant;
import com.biperf.core.utils.NumberFormatUtil;
import com.biperf.core.value.ssi.SSIContestParticipantValueBean;

/**
 * @author patelp
 * @since April 17, 2015
 * @version 1.0
 */
public class SSIContestParticipantATNView
{
  private List<WebErrorMessage> messages;
  private int currentPage;
  private String sortedBy;
  private String sortedOn;
  private int recordsTotal;
  private int recordsPerPage;
  private List<SSIContestParticipantValueBean> participants;

  public SSIContestParticipantATNView()
  {
    super();
  }

  public SSIContestParticipantATNView( List<SSIContestParticipant> contestParticipants,
                                       Integer recordsPerPage,
                                       Integer totalCount,
                                       String sortedBy,
                                       String sortedOn,
                                       Integer currentPage,
                                       SSIContest ssiContest )
  {
    super();
    this.sortedOn = sortedOn;
    this.sortedBy = sortedBy;
    this.recordsPerPage = recordsPerPage;
    this.recordsTotal = totalCount;
    this.currentPage = currentPage;
    if ( contestParticipants != null )
    {
      participants = new ArrayList<SSIContestParticipantValueBean>();
      SSIContestParticipantValueBean contestParticipantValueBean = null;
      for ( SSIContestParticipant contestParticipant : contestParticipants )
      {
        contestParticipantValueBean = new SSIContestParticipantValueBean();
        if ( contestParticipant.getParticipant().getDepartmentType() != null )
        {
          DynaPickListType departmentItem = DynaPickListType.lookup( DepartmentType.PICKLIST_ASSET, contestParticipant.getParticipant().getDepartmentType() );
          contestParticipantValueBean.setDepartmentName( departmentItem != null ? departmentItem.getName() : null );
        }
        if ( contestParticipant.getParticipant().getPositionType() != null )
        {
          DynaPickListType jobPositionItem = DynaPickListType.lookup( PositionType.PICKLIST_ASSET, contestParticipant.getParticipant().getPositionType() );
          contestParticipantValueBean.setJobName( jobPositionItem != null ? jobPositionItem.getName() : null );
        }
        contestParticipantValueBean.setFirstName( contestParticipant.getParticipant().getFirstName() );
        contestParticipantValueBean.setLastName( contestParticipant.getParticipant().getLastName() );
        contestParticipantValueBean.setOrgName( contestParticipant.getParticipant().getPrimaryUserNode().getNode().getName() );
        contestParticipantValueBean.setId( contestParticipant.getId() );
        contestParticipantValueBean.setUserId( contestParticipant.getParticipant().getId() );
        contestParticipantValueBean.setActivityDescription( contestParticipant.getActivityDescription() );
        if ( Hibernate.isInitialized( contestParticipant.getParticipant().getUserAddresses() ) )
        {
          contestParticipantValueBean.setCountryCode( contestParticipant.getParticipant().getPrimaryCountryCode() );
          contestParticipantValueBean.setCountryName( contestParticipant.getParticipant().getPrimaryCountryName() );
        }
        contestParticipantValueBean.setObjectivePayoutDescription( contestParticipant.getObjectivePayoutDescription() );

        if ( ssiContest != null && ssiContest.getActivityMeasureType() != null && ssiContest.getActivityMeasureType().isCurrency() )
        {
          contestParticipantValueBean
              .setObjectiveAmount( contestParticipant.getObjectiveAmount() != null ? NumberFormatUtil.getLocaleBasedCurrencyFormat( contestParticipant.getObjectiveAmount(), 2 ) : null );
        }
        else
        {
          contestParticipantValueBean.setObjectiveAmount( NumberFormatUtil.getLocaleBasedDobleNumberFormat( contestParticipant.getObjectiveAmount() ) );
        }
        if ( ssiContest.getPayoutType() != null && ssiContest.getPayoutType().isOther() && ssiContest.getPayoutOtherCurrencyCode() != null )
        {
          contestParticipantValueBean
              .setObjectivePayout( contestParticipant.getObjectivePayout() != null ? NumberFormatUtil.getLocaleBasedCurrencyFormat( contestParticipant.getObjectivePayout(), 2 ) : null );
        }
        else
        {
          contestParticipantValueBean
              .setObjectivePayout( contestParticipant.getObjectivePayout() != null ? NumberFormatUtil.getLocaleBasedNumberFormat( contestParticipant.getObjectivePayout() ) : null );
        }

        contestParticipantValueBean.setBonusForEvery( contestParticipant.getObjectiveBonusIncrement() != null ? contestParticipant.getObjectiveBonusIncrement().toString() : null );
        contestParticipantValueBean.setBonusPayout( contestParticipant.getObjectiveBonusPayout() != null ? contestParticipant.getObjectiveBonusPayout().toString() : null );
        contestParticipantValueBean.setBonusPayoutCap( contestParticipant.getObjectiveBonusCap() != null ? contestParticipant.getObjectiveBonusCap().toString() : null );
        contestParticipantValueBean.setBaselineAmount( contestParticipant.getStepItUpBaselineAmount() != null ? contestParticipant.getStepItUpBaselineAmount().toString() : null );
        this.getParticipants().add( contestParticipantValueBean );
      }
    }
  }

  public List<WebErrorMessage> getMessages()
  {
    return messages;
  }

  public void setMessages( List<WebErrorMessage> messages )
  {
    this.messages = messages;
  }

  public int getCurrentPage()
  {
    return currentPage;
  }

  public void setCurrentPage( int currentPage )
  {
    this.currentPage = currentPage;
  }

  public String getSortedBy()
  {
    return sortedBy;
  }

  public void setSortedBy( String sortedBy )
  {
    this.sortedBy = sortedBy;
  }

  public String getSortedOn()
  {
    return sortedOn;
  }

  public void setSortedOn( String sortedOn )
  {
    this.sortedOn = sortedOn;
  }

  public int getRecordsTotal()
  {
    return recordsTotal;
  }

  public void setRecordsTotal( int recordsTotal )
  {
    this.recordsTotal = recordsTotal;
  }

  public int getRecordsPerPage()
  {
    return recordsPerPage;
  }

  public void setRecordsPerPage( int recordsPerPage )
  {
    this.recordsPerPage = recordsPerPage;
  }

  public List<SSIContestParticipantValueBean> getParticipants()
  {
    return participants;
  }

  public void setParticipants( List<SSIContestParticipantValueBean> participants )
  {
    this.participants = participants;
  }

}
