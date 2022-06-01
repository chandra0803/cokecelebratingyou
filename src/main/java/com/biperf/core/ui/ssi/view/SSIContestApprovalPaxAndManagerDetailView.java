
package com.biperf.core.ui.ssi.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.biperf.core.domain.enums.DepartmentType;
import com.biperf.core.domain.enums.DynaPickListType;
import com.biperf.core.domain.enums.PositionType;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.ssi.SSIContestManager;
import com.biperf.core.domain.ssi.SSIContestParticipant;
import com.biperf.core.domain.ssi.SSIContestSuperViewer;

public class SSIContestApprovalPaxAndManagerDetailView
{

  private Participants participants;
  private Managers managers;
  private SuperViewers superViewers;

  public SSIContestApprovalPaxAndManagerDetailView()
  {

  }

  public SSIContestApprovalPaxAndManagerDetailView( Integer paxPerPage,
                                                    Integer currentPage,
                                                    Integer paxCount,
                                                    Integer managerCount,
                                                    Integer superviewerCount,
                                                    List<SSIContestParticipant> participants,
                                                    List<SSIContestManager> managers,
                                                    List<SSIContestSuperViewer> superViewers )
  {
    this.setParticipants( new Participants( paxPerPage, currentPage, paxCount, participants ) );
    this.setManagers( new Managers( paxPerPage, currentPage, managerCount, managers ) );
    this.setSuperViewers( new SuperViewers( paxPerPage, currentPage, superviewerCount, superViewers ) );
  }

  public Participants getParticipants()
  {
    return participants;
  }

  public void setParticipants( Participants participants )
  {
    this.participants = participants;
  }

  public Managers getManagers()
  {
    return managers;
  }

  public void setManagers( Managers managers )
  {
    this.managers = managers;
  }

  public SuperViewers getSuperViewers()
  {
    return superViewers;
  }

  public void setSuperViewers( SuperViewers superViewers )
  {
    this.superViewers = superViewers;
  }

  class Participants
  {
    private Meta meta;
    private List<Member> members;

    public Participants()
    {

    }

    public Participants( Integer paxPerPage, Integer currentPage, Integer paxCount, List<SSIContestParticipant> participants )
    {
      this.setMeta( new Meta( paxPerPage, currentPage, paxCount ) );
      if ( participants != null )
      {
        if ( members == null )
        {
          members = new ArrayList<Member>();
        }
        for ( SSIContestParticipant participant : participants )
        {
          this.members.add( new Member( participant.getParticipant() ) );
        }
        Collections.sort( this.getMembers(), new MemberComparator() );
      }
    }

    public Meta getMeta()
    {
      return meta;
    }

    public void setMeta( Meta meta )
    {
      this.meta = meta;
    }

    public List<Member> getMembers()
    {
      return members;
    }

    public void setMembers( List<Member> members )
    {
      this.members = members;
    }

  }

  class Managers
  {
    private Meta meta;
    private List<Member> members;

    public Managers()
    {

    }

    public Managers( Integer paxPerPage, Integer currentPage, Integer managerCount, List<SSIContestManager> managers )
    {
      this.setMeta( new Meta( paxPerPage, currentPage, managerCount ) );
      if ( managers != null )
      {
        if ( members == null )
        {
          members = new ArrayList<Member>();
        }
        for ( SSIContestManager manager : managers )
        {
          this.members.add( new Member( manager.getManager() ) );
        }
        Collections.sort( this.getMembers(), new MemberComparator() );
      }
    }

    public Meta getMeta()
    {
      return meta;
    }

    public void setMeta( Meta meta )
    {
      this.meta = meta;
    }

    public List<Member> getMembers()
    {
      return members;
    }

    public void setMembers( List<Member> members )
    {
      this.members = members;
    }

  }

  class SuperViewers
  {
    private Meta meta;
    private List<Member> members;

    public SuperViewers()
    {

    }

    public SuperViewers( Integer paxPerPage, Integer currentPage, Integer superViewersCount, List<SSIContestSuperViewer> superViewers )
    {
      this.setMeta( new Meta( paxPerPage, currentPage, superViewersCount ) );
      if ( superViewers != null )
      {
        if ( members == null )
        {
          members = new ArrayList<Member>();
        }
        for ( SSIContestSuperViewer superViewer : superViewers )
        {
          this.members.add( new Member( superViewer.getSuperViewer() ) );
        }
        Collections.sort( this.getMembers(), new MemberComparator() );
      }
    }

    public Meta getMeta()
    {
      return meta;
    }

    public void setMeta( Meta meta )
    {
      this.meta = meta;
    }

    public List<Member> getMembers()
    {
      return members;
    }

    public void setMembers( List<Member> members )
    {
      this.members = members;
    }

  }

  class MemberComparator implements Comparator<Member>
  {

    public int compare( Member memberOne, Member memberTwo )
    {
      int nameComp = memberOne.getLastName().compareToIgnoreCase( memberTwo.getLastName() );
      return nameComp == 0 ? memberOne.getFirstName().compareToIgnoreCase( memberTwo.getFirstName() ) : nameComp;
    }
  }

  class Meta
  {
    private int total;
    private int paxPerPage;
    private int currentPage;

    public Meta()
    {

    }

    public Meta( Integer paxPerPage, Integer currentPage, Integer count )
    {
      this.setTotal( count );
      this.setPaxPerPage( paxPerPage );
      this.setCurrentPage( currentPage );
    }

    public int getTotal()
    {
      return total;
    }

    public void setTotal( int total )
    {
      this.total = total;
    }

    public int getPaxPerPage()
    {
      return paxPerPage;
    }

    public void setPaxPerPage( int paxPerPage )
    {
      this.paxPerPage = paxPerPage;
    }

    public int getCurrentPage()
    {
      return currentPage;
    }

    public void setCurrentPage( int currentPage )
    {
      this.currentPage = currentPage;
    }

  }

  class Member
  {
    private long id;
    private String firstName;
    private String lastName;
    private String countryCode;
    private String countryName;
    private String orgName;
    private String departmentName;
    private String jobName;

    public Member()
    {

    }

    public Member( Participant participant )
    {
      this.setId( participant.getId() );
      this.setFirstName( participant.getFirstName() );
      this.setLastName( participant.getLastName() );
      this.setCountryCode( participant.getPrimaryCountryCode() );
      this.setCountryName( participant.getPrimaryCountryName() );

      if ( participant.getDepartmentType() != null )
      {
        DynaPickListType departmentItem = DynaPickListType.lookup( DepartmentType.PICKLIST_ASSET, participant.getDepartmentType() );
        this.setDepartmentName( departmentItem != null ? departmentItem.getName() : null );
      }
      if ( participant.getPositionType() != null )
      {
        DynaPickListType jobPositionItem = DynaPickListType.lookup( PositionType.PICKLIST_ASSET, participant.getPositionType() );
        this.setJobName( jobPositionItem != null ? jobPositionItem.getName() : null );
      }
      this.setOrgName( participant.getPrimaryUserNode().getNode().getName() );
    }

    public long getId()
    {
      return id;
    }

    public void setId( long id )
    {
      this.id = id;
    }

    public String getFirstName()
    {
      return firstName;
    }

    public void setFirstName( String firstName )
    {
      this.firstName = firstName;
    }

    public String getLastName()
    {
      return lastName;
    }

    public void setLastName( String lastName )
    {
      this.lastName = lastName;
    }

    public String getCountryCode()
    {
      return countryCode;
    }

    public void setCountryCode( String countryCode )
    {
      this.countryCode = countryCode;
    }

    public String getCountryName()
    {
      return countryName;
    }

    public void setCountryName( String countryName )
    {
      this.countryName = countryName;
    }

    public String getOrgName()
    {
      return orgName;
    }

    public void setOrgName( String orgName )
    {
      this.orgName = orgName;
    }

    public String getDepartmentName()
    {
      return departmentName;
    }

    public void setDepartmentName( String departmentName )
    {
      this.departmentName = departmentName;
    }

    public String getJobName()
    {
      return jobName;
    }

    public void setJobName( String jobName )
    {
      this.jobName = jobName;
    }

  }

}
