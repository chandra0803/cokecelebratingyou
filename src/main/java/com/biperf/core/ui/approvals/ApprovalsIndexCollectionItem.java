
package com.biperf.core.ui.approvals;

public class ApprovalsIndexCollectionItem
{

  private Long id;
  private String name;
  private String type;
  private String alertMessage;
  private String url;
  private long numberOfApprovables;
  private long numberOfApproved;

  public ApprovalsIndexCollectionItem()
  {

  }

  public ApprovalsIndexCollectionItem( Long id, String name )
  {
    this.id = id;
    this.name = name;
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

  public String getType()
  {
    return type;
  }

  public void setType( String type )
  {
    this.type = type;
  }

  public String getAlertMessage()
  {
    return alertMessage;
  }

  public void setAlertMessage( String alertMessage )
  {
    this.alertMessage = alertMessage;
  }

  public String getUrl()
  {
    return url;
  }

  public void setUrl( String url )
  {
    this.url = url;
  }

  public void setNumberOfApprovables( long numberOfApprovables )
  {
    this.numberOfApprovables = numberOfApprovables;
  }

  public long getNumberOfApprovables()
  {
    return numberOfApprovables;
  }

  public long getNumberOfApproved()
  {
    return numberOfApproved;
  }

  public void setNumberOfApproved( long numberOfApproved )
  {
    this.numberOfApproved = numberOfApproved;
  }

}
