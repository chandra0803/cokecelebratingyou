
package com.biperf.core.value;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.biperf.core.dao.throwdown.Standing;
import com.biperf.core.domain.promotion.ThrowdownPromotion;
import com.biperf.core.utils.DateUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ThrowdownStandingBean implements Serializable
{

  private static final long serialVersionUID = 1L;
  public static final int PAGE_SIZE = 25;

  private ThrowdownPromotion promotion = null;
  private List<Standing> standings = new ArrayList<Standing>();
  private ThrowdownStandingsListTabularData tabularData = new ThrowdownStandingsListTabularData();
  private String rulesUrl;
  private int totalStandings;
  private int currentPage;
  private int sortedOn;
  private String sortedBy;
  private String progressEndDate;
  private boolean progressLoaded;

  @JsonProperty( "rulesUrl" )
  public String getRulesUrl()
  {
    return rulesUrl;
  }

  @JsonIgnore
  public void setRulesUrl( String rulesUrl )
  {
    this.rulesUrl = rulesUrl;
  }

  @JsonProperty( "promotionId" )
  public Long getPromotionId()
  {
    return promotion.getId();
  }

  @JsonProperty( "promotionStartDate" )
  public String getPromotionStartDate()
  {
    return DateUtils.toDisplayString( promotion.getSubmissionStartDate() );
  }

  @JsonProperty( "promotionEndDate" )
  public String getPromotionEndDate()
  {
    return DateUtils.toDisplayString( promotion.getSubmissionStartDate() );
  }

  @JsonIgnore
  public ThrowdownPromotion getPromotion()
  {
    return promotion;
  }

  @JsonIgnore
  public void setPromotion( ThrowdownPromotion promotion )
  {
    this.promotion = promotion;
  }

  @JsonProperty( "standings" )
  public List<Standing> getStandings()
  {
    return standings;
  }

  @JsonIgnore
  public void setStandings( List<Standing> standings )
  {
    this.standings = standings;
  }

  public void setTabularData( ThrowdownStandingsListTabularData tabularData )
  {
    this.tabularData = tabularData;
  }

  @JsonProperty( "tabularData" )
  public ThrowdownStandingsListTabularData getTabularData()
  {
    return tabularData;
  }

  @JsonProperty( "standingsPerPage" )
  public int getStandingsPerPage()
  {
    return PAGE_SIZE;
  }

  @JsonProperty( "totalStandings" )
  public int getTotalStandings()
  {
    return totalStandings;
  }

  @JsonIgnore
  public void setTotalStandings( int totalStandings )
  {
    this.totalStandings = totalStandings;
  }

  @JsonProperty( "currentPage" )
  public int getCurrentPage()
  {
    return currentPage;
  }

  @JsonIgnore
  public void setCurrentPage( int currentPage )
  {
    this.currentPage = currentPage;
  }

  @JsonProperty( "sortedOn" )
  public int getSortedOn()
  {
    return sortedOn;
  }

  @JsonIgnore
  public void setSortedOn( int sortedOn )
  {
    this.sortedOn = sortedOn;
  }

  @JsonProperty( "sortedBy" )
  public String getSortedBy()
  {
    return sortedBy;
  }

  @JsonIgnore
  public void setSortedBy( String sortedBy )
  {
    this.sortedBy = sortedBy;
  }

  @JsonProperty( "progressEndDate" )
  public String getProgressEndDate()
  {
    return progressEndDate;
  }

  public void setProgressEndDate( String progressEndDate )
  {
    this.progressEndDate = progressEndDate;
  }

  @JsonProperty( "isProgressLoaded" )
  public boolean getProgressLoaded()
  {
    return progressLoaded;
  }

  @JsonIgnore
  public void setProgressLoaded( boolean progressLoaded )
  {
    this.progressLoaded = progressLoaded;
  }

}
