
package com.biperf.core.ui.ssi.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.hibernate.Hibernate;

import com.biperf.core.domain.WebErrorMessage;
import com.biperf.core.domain.enums.DepartmentType;
import com.biperf.core.domain.enums.DynaPickListType;
import com.biperf.core.domain.enums.PositionType;
import com.biperf.core.domain.ssi.SSIContestManager;
import com.biperf.core.value.ssi.SSIContestParticipantValueBean;

/**
 * 
 * SSIContestManagerView.
 * 
 * @author kandhi
 * @since Nov 19, 2014
 * @version 1.0
 */
public class SSIContestManagerView
{

  private List<WebErrorMessage> messages;
  private int currentPage;
  private String sortedBy;
  private String sortedOn;
  private int recordsTotal;
  private int recordsPerPage;

  private List<SSIContestParticipantValueBean> participants;

  public SSIContestManagerView()
  {
    super();
  }

  public SSIContestManagerView( WebErrorMessage message )
  {
    this.messages = new ArrayList<WebErrorMessage>();
    this.messages.add( message );
  }

  public SSIContestManagerView( List<SSIContestParticipantValueBean> participants, int recordsCount, String sortedOn, String sortedBy )
  {
    this.currentPage = 1;
    this.sortedOn = sortedOn;
    this.sortedBy = sortedBy;
    this.recordsPerPage = 20;
    this.participants = participants;
    this.recordsTotal = recordsCount;
  }

  public SSIContestManagerView( List<SSIContestManager> contestManagers, Integer managersCount, String sortedBy, String sortedOn, Integer currentPage, int recordsPerPage )
  {
    if ( contestManagers != null )
    {
      this.recordsTotal = contestManagers.size();
      participants = new ArrayList<SSIContestParticipantValueBean>();
      SSIContestParticipantValueBean participant = null;
      for ( SSIContestManager contestManager : contestManagers )
      {
        participant = new SSIContestParticipantValueBean();
        DynaPickListType departmentItem = DynaPickListType.lookup( DepartmentType.PICKLIST_ASSET, contestManager.getManager().getDepartmentType() );
        DynaPickListType jobPositionItem = DynaPickListType.lookup( PositionType.PICKLIST_ASSET, contestManager.getManager().getPositionType() );
        participant.setDepartmentName( departmentItem != null ? departmentItem.getName() : null );
        participant.setJobName( jobPositionItem != null ? jobPositionItem.getName() : null );
        participant.setFirstName( contestManager.getManager().getFirstName() );
        participant.setLastName( contestManager.getManager().getLastName() );
        participant.setOrgName( contestManager.getManager().getPrimaryUserNode().getNode().getName() );
        participant.setOrgType( contestManager.getManager().getPrimaryUserNode().getNode().getNodeType().getName() );
        participant.setId( contestManager.getManager().getId() );
        participant.setUserId( contestManager.getManager().getId() );
        if ( Hibernate.isInitialized( contestManager.getManager().getUserAddresses() ) )
        {
          participant.setCountryName( contestManager.getManager().getPrimaryCountryName() );
          participant.setCountryCode( contestManager.getManager().getPrimaryCountryCode() );
        }
        participants.add( participant );
      }

      Collections.sort( this.getParticipants(), new SSIContestParticipantComparator() );

    }
    this.sortedOn = sortedOn;
    this.sortedBy = sortedBy;
    this.recordsPerPage = recordsPerPage;
    this.recordsTotal = managersCount;
    this.currentPage = currentPage;
  }

  class SSIContestParticipantComparator implements Comparator<SSIContestParticipantValueBean>
  {

    public int compare( SSIContestParticipantValueBean contestParticipantValueBeanOne, SSIContestParticipantValueBean contestParticipantValueBeanTwo )
    {
      int nameComp = contestParticipantValueBeanOne.getLastName().compareToIgnoreCase( contestParticipantValueBeanTwo.getLastName() );
      return nameComp == 0 ? contestParticipantValueBeanOne.getFirstName().compareToIgnoreCase( contestParticipantValueBeanTwo.getFirstName() ) : nameComp;
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

  public List<SSIContestParticipantValueBean> getParticipants()
  {
    return participants;
  }

  public void setParticipants( List<SSIContestParticipantValueBean> participants )
  {
    this.participants = participants;
  }

  public int getRecordsPerPage()
  {
    return recordsPerPage;
  }

  public void setRecordsPerPage( int recordsPerPage )
  {
    this.recordsPerPage = recordsPerPage;
  }

}
