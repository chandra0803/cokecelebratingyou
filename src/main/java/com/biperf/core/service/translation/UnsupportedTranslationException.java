
package com.biperf.core.service.translation;

import com.biperf.core.domain.enums.LanguageType;

public class UnsupportedTranslationException extends Exception
{
  private final LanguageType sourceLanguage;
  private final LanguageType targetLanguage;

  public UnsupportedTranslationException( LanguageType sourceLanguage, LanguageType targetLanguage )
  {
    this.sourceLanguage = sourceLanguage;
    this.targetLanguage = targetLanguage;
  }

  public LanguageType getSourceLanguage()
  {
    return sourceLanguage;
  }

  public LanguageType getTargetLanguage()
  {
    return targetLanguage;
  }
}
