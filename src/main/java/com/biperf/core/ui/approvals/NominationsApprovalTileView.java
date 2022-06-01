/*
 * (c) 2016 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.ui.approvals;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * @author poddutur
 * @since Mar 1, 2016
 */
// This annotation is used to not to serialize the null properties.
@JsonInclude( value = Include.NON_NULL )
public class NominationsApprovalTileView
{
  List<PendingNominationsApprovalView> pendingNominationsApprovalSets = new ArrayList<PendingNominationsApprovalView>();

  @JsonProperty( "pendingNominationsApprovalSets" )
  public List<PendingNominationsApprovalView> getPendingNominationsApprovalSets()
  {
    return pendingNominationsApprovalSets;
  }

  public void setPendingNominationsApprovalSets( List<PendingNominationsApprovalView> pendingNominationsApprovalSets )
  {
    this.pendingNominationsApprovalSets = pendingNominationsApprovalSets;
  }

  public static class PendingNominationsApprovalView
  {
    @JsonProperty( "totalPendingNominationsApproval" )
    int totalPendingNominationsApproval;
    List<NominationsApprovalView> nominationsApprovalList = new ArrayList<NominationsApprovalView>();

    public int getTotalPendingNominationsApproval()
    {
      return totalPendingNominationsApproval;
    }

    public void setTotalPendingNominationsApproval( int totalPendingNominationsApproval )
    {
      this.totalPendingNominationsApproval = totalPendingNominationsApproval;
    }

    @JsonProperty( "nominationsApproval" )
    public List<NominationsApprovalView> getNominationsApprovalList()
    {
      return nominationsApprovalList;
    }

    public void setNominationsApprovalList( List<NominationsApprovalView> nominationsApprovalList )
    {
      this.nominationsApprovalList = nominationsApprovalList;
    }

    public static class NominationsApprovalView
    {
      @JsonProperty( "id" )
      String id;
      @JsonProperty( "winnersName" )
      String winnersName;
      @JsonProperty( "url" )
      String url;
      @JsonProperty( "dateSubmitted" )
      String dateSubmitted;

      public String getId()
      {
        return id;
      }

      public void setId( String id )
      {
        this.id = id;
      }

      public String getWinnersName()
      {
        return winnersName;
      }

      public void setWinnersName( String winnersName )
      {
        this.winnersName = winnersName;
      }

      public String getUrl()
      {
        return url;
      }

      public void setUrl( String url )
      {
        this.url = url;
      }

      public String getDateSubmitted()
      {
        return dateSubmitted;
      }

      public void setDateSubmitted( String dateSubmitted )
      {
        this.dateSubmitted = dateSubmitted;
      }
    }
  }
}
