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
public class NominationsApprovalListView
{
  private NominationsApprovalListTabularData nomiApprovalListTabularData = new NominationsApprovalListTabularData();
  @JsonProperty( "sortedOn" )
  private String sortedOn;
  @JsonProperty( "sortedBy" )
  private String sortedBy;
  @JsonProperty( "total" )
  private int total;
  @JsonProperty( "nominationPerPage" )
  private int nominationPerPage;
  @JsonProperty( "currentPage" )
  private int currentPage;

  @JsonProperty( "tabularData" )
  public NominationsApprovalListTabularData getNomiApprovalListTabularData()
  {
    return nomiApprovalListTabularData;
  }

  public void setNomiApprovalListTabularData( NominationsApprovalListTabularData nomiApprovalListTabularData )
  {
    this.nomiApprovalListTabularData = nomiApprovalListTabularData;
  }

  public String getSortedOn()
  {
    return sortedOn;
  }

  public void setSortedOn( String sortedOn )
  {
    this.sortedOn = sortedOn;
  }

  public String getSortedBy()
  {
    return sortedBy;
  }

  public void setSortedBy( String sortedBy )
  {
    this.sortedBy = sortedBy;
  }

  public int getTotal()
  {
    return total;
  }

  public void setTotal( int total )
  {
    this.total = total;
  }

  public int getNominationPerPage()
  {
    return nominationPerPage;
  }

  public void setNominationPerPage( int nominationPerPage )
  {
    this.nominationPerPage = nominationPerPage;
  }

  public int getCurrentPage()
  {
    return currentPage;
  }

  public void setCurrentPage( int currentPage )
  {
    this.currentPage = currentPage;
  }

  public static class NominationsApprovalListTabularData
  {
    NominationsApprovalListMeta nomiApprovalListMeta = new NominationsApprovalListMeta();
    List<NominationsApprovalListResultsData> resultsDatalist = new ArrayList<NominationsApprovalListResultsData>();

    @JsonProperty( "meta" )
    public NominationsApprovalListMeta getNomiApprovalListMeta()
    {
      return nomiApprovalListMeta;
    }

    public void setNomiApprovalListMeta( NominationsApprovalListMeta nomiApprovalListMeta )
    {
      this.nomiApprovalListMeta = nomiApprovalListMeta;
    }

    @JsonProperty( "results" )
    public List<NominationsApprovalListResultsData> getResultsDatalist()
    {
      return resultsDatalist;
    }

    public void setResultsDatalist( List<NominationsApprovalListResultsData> resultsDatalist )
    {
      this.resultsDatalist = resultsDatalist;
    }

    public static class NominationsApprovalListMeta
    {
      List<Columns> columnslist = new ArrayList<Columns>();

      @JsonProperty( "columns" )
      public List<Columns> getColumnslist()
      {
        return columnslist;
      }

      public void setColumnslist( List<Columns> columnslist )
      {
        this.columnslist = columnslist;
      }

      public static class Columns
      {
        @JsonProperty( "id" )
        private int id;
        @JsonProperty( "name" )
        private String name;
        @JsonProperty( "displayName" )
        private String displayName;
        @JsonProperty( "sortable" )
        private boolean sortable;

        public int getId()
        {
          return id;
        }

        public void setId( int id )
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

        public String getDisplayName()
        {
          return displayName;
        }

        public void setDisplayName( String displayName )
        {
          this.displayName = displayName;
        }

        public boolean isSortable()
        {
          return sortable;
        }

        public void setSortable( boolean sortable )
        {
          this.sortable = sortable;
        }
      }
    }

    public static class NominationsApprovalListResultsData
    {
      @JsonProperty( "id" )
      private Long id;
      @JsonProperty( "index" )
      private int index;
      @JsonProperty( "nominationPromotionName" )
      private String nominationPromotionName;
      @JsonProperty( "levelName" )
      private String levelName;
      @JsonProperty( "levelNumber" )
      private int levelNumber;
      @JsonProperty( "tasks" )
      private String tasks;
      @JsonProperty( "status" )
      private String status;
      @JsonProperty( "url" )
      private String url;

      public Long getId()
      {
        return id;
      }

      public void setId( Long id )
      {
        this.id = id;
      }

      public int getIndex()
      {
        return index;
      }

      public void setIndex( int index )
      {
        this.index = index;
      }

      public String getNominationPromotionName()
      {
        return nominationPromotionName;
      }

      public void setNominationPromotionName( String nominationPromotionName )
      {
        this.nominationPromotionName = nominationPromotionName;
      }

      public String getLevelName()
      {
        return levelName;
      }

      public void setLevelName( String levelName )
      {
        this.levelName = levelName;
      }

      public int getLevelNumber()
      {
        return levelNumber;
      }

      public void setLevelNumber( int levelNumber )
      {
        this.levelNumber = levelNumber;
      }

      public String getTasks()
      {
        return tasks;
      }

      public void setTasks( String tasks )
      {
        this.tasks = tasks;
      }

      public String getStatus()
      {
        return status;
      }

      public void setStatus( String status )
      {
        this.status = status;
      }

      public String getUrl()
      {
        return url;
      }

      public void setUrl( String url )
      {
        this.url = url;
      }
    }
  }
}
