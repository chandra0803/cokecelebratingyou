package com.biperf.core.domain.client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.purl.PurlCelebrationTableColumnsView;
import com.biperf.core.domain.purl.PurlCelebrationView;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class CareerMomentsSets implements Serializable
{
  /**
   * 
   */
  private static final long serialVersionUID = 6690726492899372775L;
  @JsonInclude( value = Include.NON_NULL )
  private String nameId;
  @JsonInclude( value = Include.NON_NULL )
  private String name;
  @JsonInclude( value = Include.NON_NULL )
  private long total;
  private int itemsPerPage;
  private int currentPage;
  private String sortedOn;
  private String sortedBy;
  private String description;
  @JsonProperty( "isDefault" )
  private boolean isDefault = false;

  private List<CareerMomentsTableColumnsView> tableColumns = new ArrayList<CareerMomentsTableColumnsView>();
  private List<CareerMomentsView> celebrations = new ArrayList<CareerMomentsView>();
  private List<CareerMomentsView> jobChanges = new ArrayList<CareerMomentsView>();

  private List list = new ArrayList();

  public CareerMomentsSets( String code, String name, long count, String description, boolean isDefaultTab )
  {
    this.nameId = code;
    this.name = name;
    this.total = count;
    this.description = description;
    this.isDefault = isDefaultTab;
  }

  public CareerMomentsSets()
  {
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

  public long getTotal()
  {
    return total;
  }

  public void setTotal( long total )
  {
    this.total = total;
  }

  public int getItemsPerPage()
  {
    return itemsPerPage;
  }

  public void setItemsPerPage( int itemsPerPage )
  {
    this.itemsPerPage = itemsPerPage;
  }

  public int getCurrentPage()
  {
    return currentPage;
  }

  public void setCurrentPage( int currentPage )
  {
    this.currentPage = currentPage;
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

  public String getDescription()
  {
    return description;
  }

  public void setDescription( String description )
  {
    this.description = description;
  }

  @JsonProperty( "tableColumns" )
  public List<CareerMomentsTableColumnsView> getTableColumns()
  {
    return tableColumns;
  }

  public void setTableColumns( List<CareerMomentsTableColumnsView> tableColumns )
  {
    this.tableColumns = tableColumns;
  }

  @JsonProperty( "celebrations" )
  public List<CareerMomentsView> getCelebrations()
  {
    return celebrations;
  }

  public void setCelebrations( List<CareerMomentsView> celebrations )
  {
    this.celebrations = celebrations;
  }

  public boolean isDefault()
  {
    return isDefault;
  }

  public void setDefault( boolean isDefault )
  {
    this.isDefault = isDefault;
  }

  public List getList()
  {
    return list;
  }

  public void setList( List list )
  {
    this.list = list;
  }

  public List<CareerMomentsView> getJobChanges()
  {
    return jobChanges;
  }

  public void setJobChanges( List<CareerMomentsView> jobChanges )
  {
    this.jobChanges = jobChanges;
  }
}
