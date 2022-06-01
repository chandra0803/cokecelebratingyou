
package com.biperf.core.ui.ssi.view;

/**
 * 
 * SSIContestBillToTypeView.
 * 
 * @author chowdhur
 * @since Nov 7, 2014
 */
public class SSIApproverView implements Comparable<SSIApproverView>
{
  private Long id;
  private String firstName;
  private String lastName;
  private boolean selected;

  public SSIApproverView( Long id, String firstName, String lastName, boolean selected )
  {
    super();
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.selected = selected;
  }

  public Long getId()
  {
    return id;
  }

  public void setId( Long id )
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

  public boolean isSelected()
  {
    return selected;
  }

  public void setSelected( boolean selected )
  {
    this.selected = selected;
  }

  @Override
  public int compareTo( SSIApproverView ssiApproverView )
  {
    int lastNameCompare = this.lastName.compareToIgnoreCase( ( (SSIApproverView)ssiApproverView ).getLastName() );
    return lastNameCompare == 0 ? firstName.compareToIgnoreCase( ( (SSIApproverView)ssiApproverView ).getFirstName() ) : lastNameCompare;

  }

  public boolean equals( Object obj )
  {
    if ( ! ( obj instanceof SSIApproverView ) )
    {
      return false;
    }
    SSIApproverView approverView = (SSIApproverView)obj;
    return firstName.equals( approverView.getFirstName() ) && lastName.equals( approverView.getLastName() );
  }

}
