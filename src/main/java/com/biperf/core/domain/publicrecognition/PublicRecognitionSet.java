/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/publicrecognition/PublicRecognitionSet.java,v $
 */

package com.biperf.core.domain.publicrecognition;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.biperf.core.value.PublicRecognitionFormattedValueBean;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 *          
 */
public class PublicRecognitionSet implements Serializable
{

  private String nameId;
  private String name;
  private String desc;
  private long totalCount;
  // The following property only for Followed Tab
  private boolean hasFollowees;
  // to get Default Tab value
  private boolean isDefault;

  private List<PublicRecognitionFormattedValueBean> claims;

  private List<PublicRecognitionRecognitionView> recognitions = new ArrayList<PublicRecognitionRecognitionView>();
  private List list = new ArrayList();
  private String selectedListValue;
  
  public PublicRecognitionSet( String nameId, String name, String description, long count )
  {
    this.nameId = nameId;
    this.name = name;
    this.desc = description;
    this.claims = new ArrayList<PublicRecognitionFormattedValueBean>();
    this.totalCount = count;
    this.isDefault = false;
  }

  public PublicRecognitionSet( String nameId, String name, String description, boolean hasFollowees, long count, boolean isDefault )
  {
    this.nameId = nameId;
    this.name = name;
    this.desc = description;
    this.claims = new ArrayList<PublicRecognitionFormattedValueBean>();
    this.totalCount = count;
    this.hasFollowees = hasFollowees;
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

  @JsonIgnore
  public List<PublicRecognitionFormattedValueBean> getClaims()
  {
    return claims;
  }

  public void setClaims( List<PublicRecognitionFormattedValueBean> claims )
  {
    this.claims = claims;
  }

  public boolean getHasFollowees()
  {
    return hasFollowees;
  }

  public void setHasFollowees( boolean hasFollowees )
  {
    this.hasFollowees = hasFollowees;
  }

  public boolean getIsDefault()
  {
    return isDefault;
  }

  public void setIsDefault( boolean isDefault )
  {
    this.isDefault = isDefault;
  }

  public List<PublicRecognitionRecognitionView> getRecognitions()
  {
    return recognitions;
  }

  public void setRecognitions( List<PublicRecognitionRecognitionView> recognitions )
  {
    this.recognitions = recognitions;
  }

  public List getList()
  {
    return list;
  }

  public void setList( List list )
  {
    this.list = list;
  }

  public String getSelectedListValue()
  {
    return selectedListValue;
  }

  public void setSelectedListValue( String selectedListValue )
  {
    this.selectedListValue = selectedListValue;
  }

  /**
   * consolidate recognition list into 1 result for each team
   * 
   * @param recognitions
   * @return List<PublicRecognitionRecognitionView>
   */
  public void mergeRecognitions()
  {
    List<PublicRecognitionRecognitionView> mergedRecognitions = new ArrayList<PublicRecognitionRecognitionView>();

    for ( PublicRecognitionRecognitionView recognition : recognitions )
    {
      PublicRecognitionRecognitionView mergedRecognition = findRecognitionByTeamId( recognition.getTeamId(), recognition.getPromotionName(), mergedRecognitions );
      if ( mergedRecognition == null )
      {
        mergedRecognition = findRecognitionByTeamNameAndClaimId( recognition.getTeamName(), recognition.getId(), recognition.getPromotionName(), mergedRecognitions );
      }

      if ( mergedRecognition == null )
      {
        mergedRecognitions.add( recognition );
      }
      else
      {
        mergedRecognition.mergeTeamClaim( recognition );
      }
    }

    recognitions = mergedRecognitions;
  }

  private static PublicRecognitionRecognitionView findRecognitionByTeamId( Long teamId, String promotionName, List<PublicRecognitionRecognitionView> recognitions )
  {
    if ( teamId != null )
    {
      for ( PublicRecognitionRecognitionView recognition : recognitions )
      {
        if ( teamId.equals( recognition.getTeamId() ) )
        {
          return recognition;
        }
      }
    }

    return null;
  }

  private static PublicRecognitionRecognitionView findRecognitionByTeamNameAndClaimId( String teamName, Long claimId, String promotionName, List<PublicRecognitionRecognitionView> recognitions )
  {
    if ( teamName != null && claimId != null )
    {
      for ( PublicRecognitionRecognitionView recognition : recognitions )
      {
        if ( teamName.equals( recognition.getTeamName() ) && claimId.equals( recognition.getId() ) )
        {
          return recognition;
        }
      }
    }

    return null;
  }
}
