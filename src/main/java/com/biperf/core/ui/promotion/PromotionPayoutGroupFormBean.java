/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/promotion/PromotionPayoutGroupFormBean.java,v $
 */

package com.biperf.core.ui.promotion;

import java.io.Serializable;
import java.rmi.dgc.VMID;
import java.util.ArrayList;
import java.util.List;

/**
 * PromotionPayoutGroupFormBean.
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
 * <td>sedey</td>
 * <td>June 29, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionPayoutGroupFormBean implements Serializable, Cloneable
{
  private Long promoPayoutGroupId;
  private Long parentPromoPayoutGroupId;
  private String guid;

  private String submitterParentPayout;
  private String submitterChildPayout;
  private String teamMemberParentPayout;
  private String teamMemberChildPayout;
  private String minimumQualifier;
  private boolean retroPayout;
  private String createdBy = "";
  private long dateCreated;
  private Long version;
  private List promoPayoutValueList = new ArrayList();
  private String childQuantity;
  private String parentQuantity;

  public PromotionPayoutGroupFormBean()
  {
    submitterParentPayout = "";
    submitterChildPayout = "";
    teamMemberParentPayout = "";
    teamMemberChildPayout = "";
    parentQuantity = "1";
    childQuantity = "1";
    retroPayout = true;
    minimumQualifier = "1";
    this.guid = new VMID().toString();
  }

  public Object clone()
  {
    PromotionPayoutGroupFormBean clonedObject = null;

    try
    {
      clonedObject = (PromotionPayoutGroupFormBean)super.clone();
      clonedObject.setPromoPayoutValueList( (List) ( (ArrayList)promoPayoutValueList ).clone() );
    }
    catch( CloneNotSupportedException e )
    {
      // This exception will never be throw because this class implements the interface
      // "Cloneable."
    }

    return clonedObject;
  }

  public String getSubmitterChildPayout()
  {
    return submitterChildPayout;
  }

  public void setSubmitterChildPayout( String submitterChildPayout )
  {
    this.submitterChildPayout = submitterChildPayout;
  }

  public String getSubmitterParentPayout()
  {
    return submitterParentPayout;
  }

  public void setSubmitterParentPayout( String submitterParentPayout )
  {
    this.submitterParentPayout = submitterParentPayout;
  }

  public String getTeamMemberChildPayout()
  {
    return teamMemberChildPayout;
  }

  public void setTeamMemberChildPayout( String teamMemberChildPayout )
  {
    this.teamMemberChildPayout = teamMemberChildPayout;
  }

  public String getTeamMemberParentPayout()
  {
    return teamMemberParentPayout;
  }

  public void setTeamMemberParentPayout( String teamMemberParentPayout )
  {
    this.teamMemberParentPayout = teamMemberParentPayout;
  }

  public Long getVersion()
  {
    return version;
  }

  public void setVersion( Long version )
  {
    this.version = version;
  }

  public Long getPromoPayoutGroupId()
  {
    return promoPayoutGroupId;
  }

  public void setPromoPayoutGroupId( Long id )
  {
    this.promoPayoutGroupId = id;
  }

  public String getCreatedBy()
  {
    return createdBy;
  }

  public void setCreatedBy( String createdBy )
  {
    this.createdBy = createdBy;
  }

  public long getDateCreated()
  {
    return dateCreated;
  }

  public void setDateCreated( long dateCreated )
  {
    this.dateCreated = dateCreated;
  }

  /**
   * @return List of PromotionPayoutFormBean objects
   */
  public List getPromoPayoutValueList()
  {
    return promoPayoutValueList;
  }

  public void setPromoPayoutValueList( List promoPayoutValueList )
  {
    this.promoPayoutValueList = promoPayoutValueList;
  }

  /**
   * Accessor for the value list
   * 
   * @param index
   * @return Single instance of PromotionPayoutFormBean from the value list
   */
  public PromotionPayoutFormBean getPromoPayoutValueList( int index )
  {
    try
    {
      return (PromotionPayoutFormBean)promoPayoutValueList.get( index );
    }
    catch( Exception anException )
    {
      return null;
    }
  }

  /**
   * Accessor for the number of PromotionPayoutFormBean objects in the list.
   * 
   * @return int
   */
  public int getPromoPayoutValueListCount()
  {
    if ( promoPayoutValueList == null )
    {
      return 0;
    }

    return promoPayoutValueList.size();
  }

  public String getChildQuantity()
  {
    return childQuantity;
  }

  public void setChildQuantity( String childQuantity )
  {
    this.childQuantity = childQuantity;
  }

  public String getGuid()
  {
    return guid;
  }

  public void setGuid( String guid )
  {
    this.guid = guid;
  }

  public String getParentQuantity()
  {
    return parentQuantity;
  }

  public void setParentQuantity( String parentQuantity )
  {
    this.parentQuantity = parentQuantity;
  }

  public Long getParentPromoPayoutGroupId()
  {
    return parentPromoPayoutGroupId;
  }

  public void setParentPromoPayoutGroupId( Long parentPromoPayoutGroupId )
  {
    this.parentPromoPayoutGroupId = parentPromoPayoutGroupId;
  }

  public String getMinimumQualifier()
  {
    return minimumQualifier;
  }

  public void setMinimumQualifier( String minimumQualifier )
  {
    this.minimumQualifier = minimumQualifier;
  }

  public boolean getRetroPayout()
  {
    return retroPayout;
  }

  public void setRetroPayout( boolean retroPayout )
  {
    this.retroPayout = retroPayout;
  }

}
