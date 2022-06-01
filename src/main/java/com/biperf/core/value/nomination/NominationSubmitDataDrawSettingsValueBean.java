
package com.biperf.core.value.nomination;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.Factory;
import org.apache.commons.collections4.ListUtils;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents settings for the draw tool for drawing your own ecard on a nomination submission
 */
public class NominationSubmitDataDrawSettingsValueBean
{

  private boolean canUpload;
  private boolean canDraw;
  private Integer[] sizes = new Integer[100];
  private List<NominationSubmitDataColorSettingValueBean> colors;

  /**
   * Default constructor to initialize collection objects to empty default.
   */
  public NominationSubmitDataDrawSettingsValueBean()
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

  @SuppressWarnings( "unchecked" )
  @JsonProperty( "colors" )
  public List<NominationSubmitDataColorSettingValueBean> getColors()
  {

    if ( colors == null )
    {
      Factory factory = new Factory()
      {
        public Object create()
        {
          return new NominationSubmitDataColorSettingValueBean();
        }
      };
      colors = ListUtils.lazyList( new ArrayList<>(), factory );
    }
    return colors;
  }

  public void setColors( List<NominationSubmitDataColorSettingValueBean> colors )
  {
    this.colors = colors;
  }

  @SuppressWarnings( "unchecked" )
  public void addColor( NominationSubmitDataColorSettingValueBean color )
  {

    if ( colors == null )
    {
      Factory factory = new Factory()
      {
        public Object create()
        {
          return new NominationSubmitDataColorSettingValueBean();
        }
      };
      colors = ListUtils.lazyList( new ArrayList<>(), factory );
    }

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
