
package com.biperf.core.ui.recognition;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.participant.NameIdBean;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude( Include.NON_NULL )
public class EligiblePromotionsListView
{

  @JsonProperty( "totalPromotionCount" )
  private int totalPromotionCount;
  @JsonProperty( "raEnable" )
  private boolean raEnable;
  @JsonProperty( "programs" )
  private List<EligiblePromotion> programs = new ArrayList<EligiblePromotion>();
  @JsonProperty( "nodes" )
  private List<NameIdBean> nodes = new ArrayList<NameIdBean>();

  public EligiblePromotionsListView( int totalPromotionCount, List<EligiblePromotion> programs, List<NameIdBean> nodes, boolean raEnable )
  {

    this.programs = programs;
    this.totalPromotionCount = totalPromotionCount;
    this.nodes = nodes;
    this.raEnable = raEnable;
  }

  public int getTotalPromotionCount()
  {
    return totalPromotionCount;
  }

  public List<EligiblePromotion> getPrograms()
  {
    return programs;
  }

  public boolean isRaEnable()
  {
    return raEnable;
  }

  public void setRaEnable( boolean raEnable )
  {
    this.raEnable = raEnable;
  }

  public List<NameIdBean> getNodes()
  {
    return nodes;
  }

  public void setNodes( List<NameIdBean> nodes )
  {
    this.nodes = nodes;
  }

}
