
package com.biperf.core.ui.nomination;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents settings for the draw tool for drawing your own ecard on a nomination submission
 */
public class NominationSubmitDataDrawSettingsViewBean
{

  private boolean canUpload;
  private boolean canDraw;
  private Integer[] sizes = new Integer[100];
  private List<NominationSubmitDataColorSettingViewBean> colors = new ArrayList<NominationSubmitDataColorSettingViewBean>();

  /**
   * Default constructor to initialize collection objects to empty default.
   */
  public NominationSubmitDataDrawSettingsViewBean()
  {
  }

  @JsonProperty( "canUpload" )
  public boolean isCanUpload()
  {
    return canUpload;
  }

  public void setCanUpload( boolean canUpload )
  {
    this.canUpload = canUpload;
  }

  @JsonProperty( "canDraw" )
  public boolean isCanDraw()
  {
    return canDraw;
  }

  public void setCanDraw( boolean canDraw )
  {
    this.canDraw = canDraw;
  }

  @JsonProperty( "colors" )
  public List<NominationSubmitDataColorSettingViewBean> getColors()
  {

    return colors;
  }

  public void setColors( List<NominationSubmitDataColorSettingViewBean> colors )
  {
    this.colors = colors;
  }

  public void addColor( NominationSubmitDataColorSettingViewBean color )
  {

    this.colors.add( color );
  }

  @JsonProperty( "sizes" )
  public Integer[] getSizes()
  {
    return sizes;
  }

  public void setSizes( Integer[] sizes )
  {
    this.sizes = sizes;
  }

}
