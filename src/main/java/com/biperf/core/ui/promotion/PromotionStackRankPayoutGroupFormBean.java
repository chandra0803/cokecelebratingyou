/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.ui.promotion;

import java.io.Serializable;
import java.rmi.dgc.VMID;
import java.util.ArrayList;
import java.util.List;

/**
 * PromotionStackRankPayoutGroupFormBean.
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
 * <td>attada</td>
 * <td>March 08, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionStackRankPayoutGroupFormBean implements Serializable, Cloneable
{
  private Long promoPayoutGroupId;
  private Long parentPromoPayoutGroupId;
  private String guid;

  private String nodeTypeName;
  private String nodeTypeId;
  private String cmAssetCode;
  private String nameCmKey;

  private String submittersRankName;
  private String submittersRankNameId;

  private String createdBy = "";
  private long dateCreated;
  private Long version;
  private List promoStackRankPayoutValueList = new ArrayList();

  public PromotionStackRankPayoutGroupFormBean()
  {

    this.guid = new VMID().toString();
  }

  public Object clone()
  {
    PromotionStackRankPayoutGroupFormBean clonedObject = null;

    try
    {
      clonedObject = (PromotionStackRankPayoutGroupFormBean)super.clone();
      clonedObject.setPromoStackRankPayoutValueList( (List) ( (ArrayList)promoStackRankPayoutValueList ).clone() );
    }
    catch( CloneNotSupportedException e )
    {
      // This exception will never be throw because this class implements the interface
      // "Cloneable."
    }

    return clonedObject;
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
   * Accessor for the value list
   * 
   * @param index
   * @return Single instance of PromotionStackRankPayoutFormBean from the value list
   */
  public PromotionStackRankPayoutFormBean getPromoPayoutValueList( int index )
  {
    try
    {
      return (PromotionStackRankPayoutFormBean)promoStackRankPayoutValueList.get( index );
    }
    catch( Exception anException )
    {
      return null;
    }
  }

  public String getGuid()
  {
    return guid;
  }

  public void setGuid( String guid )
  {
    this.guid = guid;
  }

  public Long getParentPromoPayoutGroupId()
  {
    return parentPromoPayoutGroupId;
  }

  public void setParentPromoPayoutGroupId( Long parentPromoPayoutGroupId )
  {
    this.parentPromoPayoutGroupId = parentPromoPayoutGroupId;
  }

  public String getNodeTypeId()
  {
    return nodeTypeId;
  }

  public void setNodeTypeId( String nodeTypeId )
  {
    this.nodeTypeId = nodeTypeId;
  }

  public String getNodeTypeName()
  {
    return nodeTypeName;
  }

  public void setNodeTypeName( String nodeTypeName )
  {
    this.nodeTypeName = nodeTypeName;
  }

  public String getSubmittersRankName()
  {
    return submittersRankName;
  }

  public void setSubmittersRankName( String submittersRankName )
  {
    this.submittersRankName = submittersRankName;
  }

  public String getSubmittersRankNameId()
  {
    return submittersRankNameId;
  }

  public void setSubmittersRankNameId( String submittersRankNameId )
  {
    this.submittersRankNameId = submittersRankNameId;
  }

  /**
   * Accessor for the number of PromotionPayoutFormBean objects in the list.
   * 
   * @return int
   */
  public int getPromoStackRankPayoutValueListCount()
  {
    if ( promoStackRankPayoutValueList == null )
    {
      return 0;
    }

    return promoStackRankPayoutValueList.size();
  }

  public List getPromoStackRankPayoutValueList()
  {
    return promoStackRankPayoutValueList;
  }

  public void setPromoStackRankPayoutValueList( List promoStackRankPayoutValueList )
  {
    this.promoStackRankPayoutValueList = promoStackRankPayoutValueList;
  }

  public String getCmAssetCode()
  {
    return cmAssetCode;
  }

  public void setCmAssetCode( String cmAssetCode )
  {
    this.cmAssetCode = cmAssetCode;
  }

  public String getNameCmKey()
  {
    return nameCmKey;
  }

  public void setNameCmKey( String nameCmKey )
  {
    this.nameCmKey = nameCmKey;
  }

}
