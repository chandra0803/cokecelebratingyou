/**
 * 
 */

package com.biperf.core.domain.purl;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author poddutur
 *
 */
@JsonInclude( value = Include.NON_NULL )
public class PurlCelebrationTableColumnsView
{
  private Long id;
  private String name;
  private String displayName;
  private boolean sortable;
  private String sortUrl;

  public Long getId()
  {
    return id;
  }

  public void setId( Long id )
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

  public String getSortUrl()
  {
    return sortUrl;
  }

  public void setSortUrl( String sortUrl )
  {
    this.sortUrl = sortUrl;
  }

}
