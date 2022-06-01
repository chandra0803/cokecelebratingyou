
package com.biperf.core.value.diycommunication;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.diycommunications.DIYCommunications;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.BaseJsonView;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CommunicationsTipsData extends BaseJsonView
{
  private static final long serialVersionUID = 1L;

  private TipsTableBean tipsTable;
  private String tipTitle;
  private String tipStartDate;
  private String tipEndDate;

  public CommunicationsTipsData()
  {
    tipsTable = new TipsTableBean();
  }

  public CommunicationsTipsData( DIYCommunications communications, List<ResourceList> tipsContentList )
  {
    this.tipTitle = communications.getContentTitle();
    this.tipStartDate = DateUtils.toDisplayDateString( communications.getStartDate(), UserManager.getLocale() );
    this.tipEndDate = DateUtils.toDisplayDateString( communications.getEndDate(), UserManager.getLocale() );
    this.tipsTable = new TipsTableBean( communications, tipsContentList );

  }

  @JsonProperty( "tipsTable" )
  public TipsTableBean getTipsTable()
  {
    return tipsTable;
  }

  public void setTipsTable( TipsTableBean tipsTable )
  {
    this.tipsTable = tipsTable;
  }

  @JsonProperty( "tipTitle" )
  public String getTipTitle()
  {
    return tipTitle;
  }

  public void setTipTitle( String tipTitle )
  {
    this.tipTitle = tipTitle;
  }

  @JsonProperty( "tipStartDate" )
  public String getTipStartDate()
  {
    return tipStartDate;
  }

  public void setTipStartDate( String tipStartDate )
  {
    this.tipStartDate = tipStartDate;
  }

  @JsonProperty( "tipEndDate" )
  public String getTipEndDate()
  {
    return tipEndDate;
  }

  public void setTipEndDate( String tipEndDate )
  {
    this.tipEndDate = tipEndDate;
  }

  public class TipsTableBean
  {
    private List<ResourceList> tips = new ArrayList<ResourceList>();

    public TipsTableBean()
    {

    }

    public TipsTableBean( DIYCommunications communications, List<ResourceList> tipsContentList )
    {
      this.tips.addAll( tipsContentList );
    }

    public List<ResourceList> getTips()
    {
      return tips;
    }

    public void setTips( List<ResourceList> tips )
    {
      this.tips = tips;
    }

  }

}
