
package com.biperf.core.service;

import com.biperf.core.domain.enums.LanguageType;

public class TranslatedContent
{
  private final LanguageType sourceLanguageType;
  private final String sourceContent;

  private final LanguageType translatedLanguageType;
  private final String translatedContent;

  public TranslatedContent( LanguageType sourceLanguageType, String sourceContent, LanguageType translatedLanguageType, String translatedContent )
  {
    this.sourceLanguageType = sourceLanguageType;
    this.sourceContent = sourceContent;
    this.translatedLanguageType = translatedLanguageType;
    this.translatedContent = translatedContent;
  }

  public String toString()
  {
    final StringBuffer buf = new StringBuffer();
    buf.append( "TranslatedContent [" );
    buf.append( "{sourceLanguageType=" ).append( this.getSourceLanguageType() ).append( "}, " );
    buf.append( "{sourceContent=" ).append( this.getSourceContent() ).append( "}, " );
    buf.append( "{translatedLanguageType=" ).append( this.getTranslatedLanguageType() ).append( "} " );
    buf.append( "{translatedContent=" ).append( this.getTranslatedContent() ).append( "} " );
    buf.append( "]" );
    return buf.toString();
  }

  public LanguageType getSourceLanguageType()
  {
    return sourceLanguageType;
  }

  public String getSourceContent()
  {
    return sourceContent;
  }

  public LanguageType getTranslatedLanguageType()
  {
    return translatedLanguageType;
  }

  public String getTranslatedContent()
  {
    return translatedContent;
  }
}
