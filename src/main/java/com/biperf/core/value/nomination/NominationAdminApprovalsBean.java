
package com.biperf.core.value.nomination;

public class NominationAdminApprovalsBean
{

  private Long id;
  private String promotionName;
  private String liveDate;

  public NominationAdminApprovalsBean()
  {
  }

  public NominationAdminApprovalsBean( Long id, String promotionName, String liveDate )
  {
    super();
    this.id = id;
    this.promotionName = promotionName;
    this.liveDate = liveDate;
  }

  public Long getId()
  {
    return id;
  }

  public void setId( Long id )
  {
    this.id = id;
  }

  public String getPromotionName()
  {
    return promotionName;
  }

  public void setPromotionName( String promotionName )
  {
    this.promotionName = promotionName;
  }

  public String getLiveDate()
  {
    return liveDate;
  }

  public void setLiveDate( String liveDate )
  {
    this.liveDate = liveDate;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( id == null ? 0 : id.hashCode() );
    result = prime * result + ( liveDate == null ? 0 : liveDate.hashCode() );
    result = prime * result + ( promotionName == null ? 0 : promotionName.hashCode() );
    return result;
  }

  @Override
  public boolean equals( Object obj )
  {
    if ( this == obj )
    {
      return true;
    }
    if ( obj == null )
    {
      return false;
    }
    if ( getClass() != obj.getClass() )
    {
      return false;
    }
    NominationAdminApprovalsBean other = (NominationAdminApprovalsBean)obj;
    if ( id == null )
    {
      if ( other.id != null )
      {
        return false;
      }
    }
    else if ( !id.equals( other.id ) )
    {
      return false;
    }
    if ( liveDate == null )
    {
      if ( other.liveDate != null )
      {
        return false;
      }
    }
    else if ( !liveDate.equals( other.liveDate ) )
    {
      return false;
    }
    if ( promotionName == null )
    {
      if ( other.promotionName != null )
      {
        return false;
      }
    }
    else if ( !promotionName.equals( other.promotionName ) )
    {
      return false;
    }
    return true;
  }

}
