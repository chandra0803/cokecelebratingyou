
package com.biperf.core.value.translation;

public class TransResult
{
  private String resultCode;
  private String resultDesc;
  private String translatedText;

  public TransResult()
  {

  }

  public TransResult( String resultCode, String resultDesc, String translatedText )
  {
    this.resultCode = resultCode;
    this.resultDesc = resultDesc;
    this.translatedText = translatedText;
  }

  public String toString()
  {
    final StringBuffer buf = new StringBuffer();
    buf.append( "TransResult [" );
    buf.append( "{resultCode=" ).append( this.getResultCode() ).append( "}, " );
    buf.append( "{resultDesc=" ).append( this.getResultDesc() ).append( "}, " );
    buf.append( "{translatedText=" ).append( this.getTranslatedText() ).append( "} " );
    buf.append( "]" );
    return buf.toString();
  }

  public String getResultCode()
  {
    return resultCode;
  }

  public void setResultCode( String resultCode )
  {
    this.resultCode = resultCode;
  }

  public String getResultDesc()
  {
    return resultDesc;
  }

  public void setResultDesc( String resultDesc )
  {
    this.resultDesc = resultDesc;
  }

  public String getTranslatedText()
  {
    return translatedText;
  }

  public void setTranslatedText( String translatedText )
  {
    this.translatedText = translatedText;
  }

}
