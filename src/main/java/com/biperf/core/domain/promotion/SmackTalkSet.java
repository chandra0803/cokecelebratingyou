
package com.biperf.core.domain.promotion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.biperf.core.value.SmackTalkCommentViewBean;
import com.biperf.core.value.SmackTalkView;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude( value = Include.NON_NULL )
public class SmackTalkSet implements Serializable
{
  private String nameId;
  private String name;
  private String desc;
  private long totalCount;

  // to get Default Tab value
  private boolean isDefault;
  private List<SmackTalkCommentViewBean> commentBeans;
  private List<SmackTalkView> smackTalks = new ArrayList<SmackTalkView>();

  public SmackTalkSet()
  {
  }

  public SmackTalkSet( String nameId, String name, String description, long count )
  {
    this.nameId = nameId;
    this.name = name;
    this.desc = description;
    this.commentBeans = new ArrayList<SmackTalkCommentViewBean>();
    this.totalCount = count;
    this.isDefault = false;
  }

  public SmackTalkSet( String nameId, String name, String description, long count, boolean isDefault )
  {
    this.nameId = nameId;
    this.name = name;
    this.desc = description;
    this.commentBeans = new ArrayList<SmackTalkCommentViewBean>();
    this.totalCount = count;
    this.isDefault = isDefault;
  }

  public String getNameId()
  {
    return nameId;
  }

  public void setNameId( String nameId )
  {
    this.nameId = nameId;
  }

  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  public String getDesc()
  {
    return desc;
  }

  public void setDesc( String desc )
  {
    this.desc = desc;
  }

  public long getTotalCount()
  {
    return totalCount;
  }

  public void setTotalCount( long totalCount )
  {
    this.totalCount = totalCount;
  }

  public boolean getIsDefault()
  {
    return isDefault;
  }

  public void setIsDefault( boolean isDefault )
  {
    this.isDefault = isDefault;
  }

  public void setCommentBeans( List<SmackTalkCommentViewBean> commentBeans )
  {
    this.commentBeans = commentBeans;
  }

  @JsonProperty( "commentBeans" )
  public List<SmackTalkCommentViewBean> getCommentBeans()
  {
    return commentBeans;
  }

  public void setSmackTalks( List<SmackTalkView> smackTalks )
  {
    this.smackTalks = smackTalks;
  }

  @JsonProperty( "smackTalks" )
  public List<SmackTalkView> getSmackTalks()
  {
    return smackTalks;
  }
}
