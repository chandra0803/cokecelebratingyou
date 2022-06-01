
package com.biperf.core.ui.ssi.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.biperf.core.domain.participant.Participant;

/**
 * 
 * SSIContestBillToTypeView.
 * 
 * @author chowdhur
 * @since Nov 7, 2014
 */
public class SSIContestApproverView
{
  private Long id;
  private String name;
  private List<SSIApproverView> approvers = new ArrayList<SSIApproverView>();

  public SSIContestApproverView()
  {
    super();
  }

  public SSIContestApproverView( Long id, String name, List<SSIApproverView> approvers )
  {
    super();
    this.id = id;
    this.name = name;
    this.approvers = approvers;
  }

  public SSIContestApproverView( Long id, String name, Set<Participant> allowedApprovers, Set<Participant> selectedApprovers )
  {
    this.id = id;
    this.name = name;
    if ( allowedApprovers != null )
    {
      Iterator<Participant> iter = allowedApprovers.iterator();
      while ( iter.hasNext() )
      {
        Participant approver = iter.next();
        boolean isSelected = false;
        if ( selectedApprovers != null && selectedApprovers.contains( approver ) )
        {
          isSelected = true;
        }
        SSIApproverView approverView = new SSIApproverView( approver.getId(), approver.getFirstName(), approver.getLastName(), isSelected );
        approvers.add( approverView );
      }
      Collections.sort( approvers, new SSIApproverViewComparator() );
    }
  }

  class SSIApproverViewComparator implements Comparator<SSIApproverView>
  {

    public int compare( SSIApproverView ApproverViewInstanceOne, SSIApproverView ApproverViewInstancetwo )
    {
      int nameComp = ApproverViewInstanceOne.getLastName().compareToIgnoreCase( ApproverViewInstancetwo.getLastName() );
      return nameComp == 0 ? ApproverViewInstanceOne.getFirstName().compareToIgnoreCase( ApproverViewInstancetwo.getFirstName() ) : nameComp;
    }
  }

  public Long getId()
  {
    return id;
  }

  public void setId( Long id )
  {
    this.id = id;
  }

  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  public List<SSIApproverView> getApprovers()
  {
    return approvers;
  }

  public void setApprovers( List<SSIApproverView> approvers )
  {
    this.approvers = approvers;
  }

}
