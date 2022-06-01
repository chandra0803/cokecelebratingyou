
package com.biperf.core.domain.engageprogram;

import com.biperf.core.domain.BaseDomain;

public class ProgramAwardLevel extends BaseDomain
{

  private static final long serialVersionUID = 1L;
  private String awardLevel;
  private String celebLabel;
  private String celebLabelCmxAssetCode;
  private String celebMsg;
  private String celebMsgCmxAssetCode;
  private String celebImgUrl;
  private String celebImgDesc;
  private String celebImgDescCmxAssetCode;
  private String country;
  private EngageProgram engageProgram;

  public String getAwardLevel()
  {
    return awardLevel;
  }

  public void setAwardLevel( String awardLevel )
  {
    this.awardLevel = awardLevel;
  }

  public String getCelebLabel()
  {
    return celebLabel;
  }

  public void setCelebLabel( String celebLabel )
  {
    this.celebLabel = celebLabel;
  }

  public String getCelebLabelCmxAssetCode()
  {
    return celebLabelCmxAssetCode;
  }

  public void setCelebLabelCmxAssetCode( String celebLabelCmxAssetCode )
  {
    this.celebLabelCmxAssetCode = celebLabelCmxAssetCode;
  }

  public String getCelebMsg()
  {
    return celebMsg;
  }

  public void setCelebMsg( String celebMsg )
  {
    this.celebMsg = celebMsg;
  }

  public String getCelebMsgCmxAssetCode()
  {
    return celebMsgCmxAssetCode;
  }

  public void setCelebMsgCmxAssetCode( String celebMsgCmxAssetCode )
  {
    this.celebMsgCmxAssetCode = celebMsgCmxAssetCode;
  }

  public String getCelebImgUrl()
  {
    return celebImgUrl;
  }

  public void setCelebImgUrl( String celebImgUrl )
  {
    this.celebImgUrl = celebImgUrl;
  }

  public String getCelebImgDesc()
  {
    return celebImgDesc;
  }

  public void setCelebImgDesc( String celebImgDesc )
  {
    this.celebImgDesc = celebImgDesc;
  }

  public String getCelebImgDescCmxAssetCode()
  {
    return celebImgDescCmxAssetCode;
  }

  public void setCelebImgDescCmxAssetCode( String celebImgDescCmxAssetCode )
  {
    this.celebImgDescCmxAssetCode = celebImgDescCmxAssetCode;
  }

  public EngageProgram getEngageProgram()
  {
    return engageProgram;
  }

  public void setEngageProgram( EngageProgram engageProgram )
  {
    this.engageProgram = engageProgram;
  }

  public String getCountry()
  {
    return country;
  }

  public void setCountry( String country )
  {
    this.country = country;
  }

  @Override
  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }
    if ( object == null )
    {
      return false;
    }
    if ( ! ( object instanceof ProgramAwardLevel ) )
    {
      return false;
    }

    ProgramAwardLevel pgrm = (ProgramAwardLevel)object;
    if ( getEngageProgram() != null && !getEngageProgram().equals( pgrm.getEngageProgram() ) )
    {
      return false;
    }

    if ( getAwardLevel() != null && !getAwardLevel().equals( pgrm.getAwardLevel() ) )
    {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    if ( getEngageProgram() != null )
    {
      result = result + prime * getEngageProgram().hashCode();
    }
    if ( getAwardLevel() != null )
    {
      result = result + prime * getAwardLevel().hashCode();
    }

    return result;
  }

}
