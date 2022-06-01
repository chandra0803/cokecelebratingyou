
package com.biperf.core.ui.promotion;

import java.io.Serializable;
import java.rmi.dgc.VMID;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings( "serial" )
public class PromotionStackStandingPayoutGroupFormBean implements Serializable, Cloneable
{
  private Long promoPayoutGroupId;
  private String guid;

  private String nodeTypeName;
  private String nodeTypeId;
  private String cmAssetCode;
  private String nameCmKey;

  private String createdBy = "";
  private long dateCreated;
  private Long version;
  private List<PromotionStackStandingPayoutFormBean> promoStackStandingPayoutValueList = new ArrayList<PromotionStackStandingPayoutFormBean>();
  private String rankingsPayoutType;

  public PromotionStackStandingPayoutGroupFormBean()
  {

    this.guid = new VMID().toString();
  }

  @SuppressWarnings( "unchecked" )
  public Object clone()
  {
    PromotionStackStandingPayoutGroupFormBean clonedObject = null;

    try
    {
      clonedObject = (PromotionStackStandingPayoutGroupFormBean)super.clone();
      clonedObject.setPromoStackStandingPayoutValueList( (List) ( (ArrayList)promoStackStandingPayoutValueList ).clone() );
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

  public PromotionStackStandingPayoutFormBean getPromoPayoutValue( int index )
  {
    try
    {
      return (PromotionStackStandingPayoutFormBean)promoStackStandingPayoutValueList.get( index );
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

  public int getPromoStackStandingPayoutValueListCount()
  {
    if ( promoStackStandingPayoutValueList == null )
    {
      return 0;
    }

    return promoStackStandingPayoutValueList.size();
  }

  public List<PromotionStackStandingPayoutFormBean> getPromoStackStandingPayoutValueList()
  {
    return promoStackStandingPayoutValueList;
  }

  public void setPromoStackStandingPayoutValueList( List<PromotionStackStandingPayoutFormBean> promoStackStandingPayoutValueList )
  {
    this.promoStackStandingPayoutValueList = promoStackStandingPayoutValueList;
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

  public String getRankingsPayoutType()
  {
    return rankingsPayoutType;
  }

  public void setRankingsPayoutType( String rankingsPayoutType )
  {
    this.rankingsPayoutType = rankingsPayoutType;
  }

}
