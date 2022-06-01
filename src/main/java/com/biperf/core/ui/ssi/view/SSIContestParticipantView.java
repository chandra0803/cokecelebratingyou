
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
import com.biperf.core.domain.ssi.SSIContestParticipant;
import com.biperf.core.value.ssi.SSIContestParticipantValueBean;

/**
 * @author dudam
 * @since Nov 19, 2014
 * @version 1.0
 */
public class SSIContestParticipantView
{
  private List<WebErrorMessage> messages;
  private int currentPage;
  private String sortedBy;
  private String sortedOn;
  private int recordsTotal;
  private int recordsPerPage;
  private List<SSIContestParticipantValueBean> participants;

  public SSIContestParticipantView()
  {
    super();
  }

  public SSIContestParticipantView( WebErrorMessage message )
  {
    super();
    this.messages = new ArrayList<WebErrorMessage>();
    this.messages.add( message );
  }

  public SSIContestParticipantView( List<SSIContestParticipant> contestParticipants, Integer recordsPerPage, Integer totalCount, String sortedBy, String sortedOn, Integer currentPage )
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
        contestParticipantValueBean.setUserId( contestParticipant.getParticipant().getId() ); // Bugzilla
                                                                                              // Bug
                                                                                              // fix
                                                                                              // #59599
        contestParticipantValueBean.setActivityDescription( contestParticipant.getActivityDescription() );
        if ( Hibernate.isInitialized( contestParticipant.getParticipant().getUserAddresses() ) )
        {
          contestParticipantValueBean.setCountryCode( contestParticipant.getParticipant().getPrimaryCountryCode() );
          contestParticipantValueBean.setCountryName( contestParticipant.getParticipant().getPrimaryCountryName() );
        }
        contestParticipantValueBean.setObjectivePayoutDescription( contestParticipant.getObjectivePayoutDescription() );
        contestParticipantValueBean.setObjectiveAmount( contestParticipant.getObjectiveAmount() != null ? contestParticipant.getObjectiveAmount().toString() : null );
        contestParticipantValueBean.setObjectivePayout( contestParticipant.getObjectivePayout() != null ? contestParticipant.getObjectivePayout().toString() : null );
        contestParticipantValueBean.setBonusForEvery( contestParticipant.getObjectiveBonusIncrement() != null ? contestParticipant.getObjectiveBonusIncrement().toString() : null );
        contestParticipantValueBean.setBonusPayout( contestParticipant.getObjectiveBonusPayout() != null ? contestParticipant.getObjectiveBonusPayout().toString() : null );
        contestParticipantValueBean.setBonusPayoutCap( contestParticipant.getObjectiveBonusCap() != null ? contestParticipant.getObjectiveBonusCap().toString() : null );
        contestParticipantValueBean.setBaselineAmount( contestParticipant.getStepItUpBaselineAmount() != null ? contestParticipant.getStepItUpBaselineAmount().toString() : null );
        this.getParticipants().add( contestParticipantValueBean );
      }
      if ( sortedBy.equals( "asc" ) )
      {
        if ( sortedOn.equals( "objectiveAmount" ) )
        {
          Collections.sort( this.getParticipants(), new SSIContestParticipantObjectiveAmountComparator() );
        }
        else if ( sortedOn.equals( "objectivePayout" ) )
        {
          Collections.sort( this.getParticipants(), new SSIContestParticipantObjectivePayoutComparator() );
        }
        else if ( sortedOn.equals( "objectivePayoutDescription" ) )
        {
          Collections.sort( this.getParticipants(), new SSIContestParticipantObjectivePayoutDescriptionComparator() );
        }
        else if ( sortedOn.equals( "activityDescription" ) )
        {
          Collections.sort( this.getParticipants(), new SSIContestParticipantActivityDescriptionComparator() );
        }
        else if ( sortedOn.equals( "bonusForEvery" ) )
        {
          Collections.sort( this.getParticipants(), new SSIContestParticipantBonusForEveryComparator() );
        }
        else if ( sortedOn.equals( "bonusPayout" ) )
        {
          Collections.sort( this.getParticipants(), new SSIContestParticipantBonusPayoutComparator() );
        }
        else if ( sortedOn.equals( "bonusPayoutCap" ) )
        {
          Collections.sort( this.getParticipants(), new SSIContestParticipantBonusPayoutCapComparator() );
        }
        else
        {
          Collections.sort( this.getParticipants(), new SSIContestParticipantComparator() );
        }
      }
      else if ( sortedBy.equals( "desc" ) )
      {
        if ( sortedOn.equals( "objectiveAmount" ) )
        {
          Collections.sort( this.getParticipants(), new SSIContestParticipantObjectiveAmountComparatordesc() );
        }
        else if ( sortedOn.equals( "objectivePayout" ) )
        {
          Collections.sort( this.getParticipants(), new SSIContestParticipantObjectivePayoutComparatordesc() );
        }
        else if ( sortedOn.equals( "objectivePayoutDescription" ) )
        {
          Collections.sort( this.getParticipants(), new SSIContestParticipantObjectivePayoutDescriptionComparatordesc() );
        }
        else if ( sortedOn.equals( "activityDescription" ) )
        {
          Collections.sort( this.getParticipants(), new SSIContestParticipantActivityDescriptionComparatordesc() );
        }
        else if ( sortedOn.equals( "bonusForEvery" ) )
        {
          Collections.sort( this.getParticipants(), new SSIContestParticipantBonusForEveryComparatordesc() );
        }
        else if ( sortedOn.equals( "bonusPayout" ) )
        {
          Collections.sort( this.getParticipants(), new SSIContestParticipantBonusPayoutComparatordesc() );
        }
        else if ( sortedOn.equals( "bonusPayoutCap" ) )
        {
          Collections.sort( this.getParticipants(), new SSIContestParticipantBonusPayoutCapComparatordesc() );
        }
        else
        {
          Collections.sort( this.getParticipants(), new SSIContestParticipantComparatordesc() );
        }
      }
    }
  }

  public int compareAlphaNumeric( String firstObjToCompare, String secondObjToCompare )
  {
    String firstString = firstObjToCompare.toString();
    String secondString = secondObjToCompare.toString();

    if ( secondString == null || firstString == null )
    {
      return 0;
    }

    int lengthFirstStr = firstString.length();
    int lengthSecondStr = secondString.length();

    int index1 = 0;
    int index2 = 0;

    while ( index1 < lengthFirstStr && index2 < lengthSecondStr )
    {
      char ch1 = firstString.charAt( index1 );
      char ch2 = secondString.charAt( index2 );

      char[] space1 = new char[lengthFirstStr];
      char[] space2 = new char[lengthSecondStr];

      int loc1 = 0;
      int loc2 = 0;

      do
      {
        space1[loc1++] = ch1;
        index1++;

        if ( index1 < lengthFirstStr )
        {
          ch1 = firstString.charAt( index1 );
        }
        else
        {
          break;
        }
      }
      while ( Character.isDigit( ch1 ) == Character.isDigit( space1[0] ) );

      do
      {
        space2[loc2++] = ch2;
        index2++;

        if ( index2 < lengthSecondStr )
        {
          ch2 = secondString.charAt( index2 );
        }
        else
        {
          break;
        }
      }
      while ( Character.isDigit( ch2 ) == Character.isDigit( space2[0] ) );

      String str1 = new String( space1 );
      String str2 = new String( space2 );

      int result;

      if ( Character.isDigit( space1[0] ) && Character.isDigit( space2[0] ) )
      {
        Integer firstNumberToCompare = new Integer( Integer.parseInt( str1.trim() ) );
        Integer secondNumberToCompare = new Integer( Integer.parseInt( str2.trim() ) );
        result = firstNumberToCompare.compareTo( secondNumberToCompare );
      }
      else
      {
        result = str1.compareTo( str2 );
      }

      if ( result != 0 )
      {
        return result;
      }
    }
    return lengthFirstStr - lengthSecondStr;
  }

  class SSIContestParticipantComparator implements Comparator<SSIContestParticipantValueBean>
  {

    public int compare( SSIContestParticipantValueBean contestParticipantValueBeanOne, SSIContestParticipantValueBean contestParticipantValueBeanTwo )
    {
      int nameComp = contestParticipantValueBeanOne.getLastName().compareToIgnoreCase( contestParticipantValueBeanTwo.getLastName() );
      return nameComp == 0 ? contestParticipantValueBeanOne.getFirstName().compareToIgnoreCase( contestParticipantValueBeanTwo.getFirstName() ) : nameComp;
    }

  }

  class SSIContestParticipantObjectiveAmountComparator implements Comparator<SSIContestParticipantValueBean>
  {

    public int compare( SSIContestParticipantValueBean contestParticipantValueBeanOne, SSIContestParticipantValueBean contestParticipantValueBeanTwo )
    {
      int nameComp = Double.compare( Double.parseDouble( contestParticipantValueBeanOne.getObjectiveAmount() ), Double.parseDouble( contestParticipantValueBeanTwo.getObjectiveAmount() ) );
      return nameComp == 0
          ? Double.compare( Double.parseDouble( contestParticipantValueBeanOne.getObjectiveAmount() ), Double.parseDouble( contestParticipantValueBeanTwo.getObjectiveAmount() ) )
          : nameComp;
    }

  }

  class SSIContestParticipantObjectivePayoutDescriptionComparator implements Comparator<SSIContestParticipantValueBean>
  {

    public int compare( SSIContestParticipantValueBean contestParticipantValueBeanOne, SSIContestParticipantValueBean contestParticipantValueBeanTwo )
    {
      int nameComp = contestParticipantValueBeanOne.getObjectivePayoutDescription().compareToIgnoreCase( contestParticipantValueBeanTwo.getObjectivePayoutDescription() );
      return nameComp == 0 ? contestParticipantValueBeanOne.getObjectivePayoutDescription().compareToIgnoreCase( contestParticipantValueBeanTwo.getObjectivePayoutDescription() ) : nameComp;
    }

  }

  class SSIContestParticipantObjectivePayoutComparator implements Comparator<SSIContestParticipantValueBean>
  {

    public int compare( SSIContestParticipantValueBean contestParticipantValueBeanOne, SSIContestParticipantValueBean contestParticipantValueBeanTwo )
    {
      Long l1 = Long.parseLong( contestParticipantValueBeanOne.getObjectivePayout() );
      Long l2 = Long.parseLong( contestParticipantValueBeanTwo.getObjectivePayout() );
      int nameComp = l1.compareTo( l2 );
      return nameComp == 0 ? l1.compareTo( l2 ) : nameComp;
    }

  }

  class SSIContestParticipantActivityDescriptionComparator implements Comparator<SSIContestParticipantValueBean>
  {

    public int compare( SSIContestParticipantValueBean contestParticipantValueBeanOne, SSIContestParticipantValueBean contestParticipantValueBeanTwo )
    {
      int nameComp = compareAlphaNumeric( contestParticipantValueBeanOne.getActivityDescription(), contestParticipantValueBeanTwo.getActivityDescription() );
      return nameComp == 0 ? compareAlphaNumeric( contestParticipantValueBeanOne.getActivityDescription(), contestParticipantValueBeanTwo.getActivityDescription() ) : nameComp;
    }

  }

  class SSIContestParticipantBonusForEveryComparator implements Comparator<SSIContestParticipantValueBean>
  {
    public int compare( SSIContestParticipantValueBean contestParticipantValueBeanOne, SSIContestParticipantValueBean contestParticipantValueBeanTwo )
    {
      int nameComp = Double.compare( Double.parseDouble( contestParticipantValueBeanOne.getBonusForEvery() ), Double.parseDouble( contestParticipantValueBeanTwo.getBonusForEvery() ) );
      return nameComp == 0
          ? Double.compare( Double.parseDouble( contestParticipantValueBeanOne.getBonusForEvery() ), Double.parseDouble( contestParticipantValueBeanTwo.getBonusForEvery() ) )
          : nameComp;
    }
  }

  class SSIContestParticipantBonusPayoutComparator implements Comparator<SSIContestParticipantValueBean>
  {

    public int compare( SSIContestParticipantValueBean contestParticipantValueBeanOne, SSIContestParticipantValueBean contestParticipantValueBeanTwo )
    {
      Long l1 = Long.parseLong( contestParticipantValueBeanOne.getBonusPayout() );
      Long l2 = Long.parseLong( contestParticipantValueBeanTwo.getBonusPayout() );
      int nameComp = l1.compareTo( l2 );
      return nameComp == 0 ? l1.compareTo( l2 ) : nameComp;
    }

  }

  class SSIContestParticipantBonusPayoutCapComparator implements Comparator<SSIContestParticipantValueBean>
  {

    public int compare( SSIContestParticipantValueBean contestParticipantValueBeanOne, SSIContestParticipantValueBean contestParticipantValueBeanTwo )
    {
      Long l1 = Long.parseLong( contestParticipantValueBeanOne.getBonusPayoutCap() );
      Long l2 = Long.parseLong( contestParticipantValueBeanTwo.getBonusPayoutCap() );
      int nameComp = l1.compareTo( l2 );
      return nameComp == 0 ? l1.compareTo( l2 ) : nameComp;
    }

  }

  // Desending Order
  class SSIContestParticipantComparatordesc implements Comparator<SSIContestParticipantValueBean>
  {

    public int compare( SSIContestParticipantValueBean contestParticipantValueBeanOne, SSIContestParticipantValueBean contestParticipantValueBeanTwo )
    {
      int nameComp = contestParticipantValueBeanTwo.getLastName().compareToIgnoreCase( contestParticipantValueBeanOne.getLastName() );
      return nameComp == 0 ? contestParticipantValueBeanTwo.getFirstName().compareToIgnoreCase( contestParticipantValueBeanOne.getFirstName() ) : nameComp;
    }

  }

  class SSIContestParticipantObjectiveAmountComparatordesc implements Comparator<SSIContestParticipantValueBean>
  {

    public int compare( SSIContestParticipantValueBean contestParticipantValueBeanOne, SSIContestParticipantValueBean contestParticipantValueBeanTwo )
    {
      int nameComp = Double.compare( Double.parseDouble( contestParticipantValueBeanTwo.getObjectiveAmount() ), Double.parseDouble( contestParticipantValueBeanOne.getObjectiveAmount() ) );
      return nameComp == 0
          ? Double.compare( Double.parseDouble( contestParticipantValueBeanTwo.getObjectiveAmount() ), Double.parseDouble( contestParticipantValueBeanOne.getObjectiveAmount() ) )
          : nameComp;
    }

  }

  class SSIContestParticipantObjectivePayoutDescriptionComparatordesc implements Comparator<SSIContestParticipantValueBean>
  {

    public int compare( SSIContestParticipantValueBean contestParticipantValueBeanOne, SSIContestParticipantValueBean contestParticipantValueBeanTwo )
    {
      int nameComp = contestParticipantValueBeanTwo.getObjectivePayoutDescription().compareToIgnoreCase( contestParticipantValueBeanOne.getObjectivePayoutDescription() );
      return nameComp == 0 ? contestParticipantValueBeanTwo.getObjectivePayoutDescription().compareToIgnoreCase( contestParticipantValueBeanOne.getObjectivePayoutDescription() ) : nameComp;
    }

  }

  class SSIContestParticipantObjectivePayoutComparatordesc implements Comparator<SSIContestParticipantValueBean>
  {

    public int compare( SSIContestParticipantValueBean contestParticipantValueBeanOne, SSIContestParticipantValueBean contestParticipantValueBeanTwo )
    {
      Long l1 = Long.parseLong( contestParticipantValueBeanTwo.getObjectivePayout() );
      Long l2 = Long.parseLong( contestParticipantValueBeanOne.getObjectivePayout() );
      int nameComp = l1.compareTo( l2 );
      return nameComp == 0 ? l1.compareTo( l2 ) : nameComp;
    }

  }

  class SSIContestParticipantActivityDescriptionComparatordesc implements Comparator<SSIContestParticipantValueBean>
  {

    public int compare( SSIContestParticipantValueBean contestParticipantValueBeanOne, SSIContestParticipantValueBean contestParticipantValueBeanTwo )
    {
      int nameComp = compareAlphaNumeric( contestParticipantValueBeanTwo.getActivityDescription(), contestParticipantValueBeanOne.getActivityDescription() );
      return nameComp == 0 ? compareAlphaNumeric( contestParticipantValueBeanTwo.getActivityDescription(), contestParticipantValueBeanOne.getActivityDescription() ) : nameComp;
    }

  }

  class SSIContestParticipantBonusForEveryComparatordesc implements Comparator<SSIContestParticipantValueBean>
  {

    public int compare( SSIContestParticipantValueBean contestParticipantValueBeanOne, SSIContestParticipantValueBean contestParticipantValueBeanTwo )
    {
      int nameComp = Double.compare( Double.parseDouble( contestParticipantValueBeanTwo.getBonusForEvery() ), Double.parseDouble( contestParticipantValueBeanOne.getBonusForEvery() ) );
      return nameComp == 0
          ? Double.compare( Double.parseDouble( contestParticipantValueBeanTwo.getBonusForEvery() ), Double.parseDouble( contestParticipantValueBeanOne.getBonusForEvery() ) )
          : nameComp;
    }

  }

  class SSIContestParticipantBonusPayoutComparatordesc implements Comparator<SSIContestParticipantValueBean>
  {

    public int compare( SSIContestParticipantValueBean contestParticipantValueBeanOne, SSIContestParticipantValueBean contestParticipantValueBeanTwo )
    {
      Long l1 = Long.parseLong( contestParticipantValueBeanTwo.getBonusPayout() );
      Long l2 = Long.parseLong( contestParticipantValueBeanOne.getBonusPayout() );
      int nameComp = l1.compareTo( l2 );
      return nameComp == 0 ? l1.compareTo( l2 ) : nameComp;
    }

  }

  class SSIContestParticipantBonusPayoutCapComparatordesc implements Comparator<SSIContestParticipantValueBean>
  {

    public int compare( SSIContestParticipantValueBean contestParticipantValueBeanOne, SSIContestParticipantValueBean contestParticipantValueBeanTwo )
    {
      Long l1 = Long.parseLong( contestParticipantValueBeanTwo.getBonusPayoutCap() );
      Long l2 = Long.parseLong( contestParticipantValueBeanOne.getBonusPayoutCap() );
      int nameComp = l1.compareTo( l2 );
      return nameComp == 0 ? l1.compareTo( l2 ) : nameComp;
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
