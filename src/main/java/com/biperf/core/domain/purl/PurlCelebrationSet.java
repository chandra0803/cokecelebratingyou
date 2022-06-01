/**
 * 
 */

package com.biperf.core.domain.purl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author poddutur
 *
 */
@JsonInclude( value = Include.NON_NULL )
public class PurlCelebrationSet implements Serializable
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

  private List<PurlCelebrationTableColumnsView> tableColumns = new ArrayList<PurlCelebrationTableColumnsView>();
  private List<PurlCelebrationView> celebrations = new ArrayList<PurlCelebrationView>();

  private List list = new ArrayList();

  public PurlCelebrationSet( String code, String name, long count, String description, boolean isDefaultTab )
  {
    this.nameId = code;
    this.name = name;
    this.total = count;
    this.description = description;
    this.isDefault = isDefaultTab;
  }

  public PurlCelebrationSet()
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
  public List<PurlCelebrationTableColumnsView> getTableColumns()
  {
    return tableColumns;
  }

  public void setTableColumns( List<PurlCelebrationTableColumnsView> tableColumns )
  {
    this.tableColumns = tableColumns;
  }

  @JsonProperty( "celebrations" )
  public List<PurlCelebrationView> getCelebrations()
  {
    return celebrations;
  }

  public void setCelebrations( List<PurlCelebrationView> celebrations )
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

}
