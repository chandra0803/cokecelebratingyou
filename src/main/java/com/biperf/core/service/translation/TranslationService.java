
package com.biperf.core.service.translation;

import com.biperf.core.domain.enums.LanguageType;
import com.biperf.core.service.TranslatedContent;

public interface TranslationService
{
  public TranslatedContent translate( LanguageType sourceLanguage, LanguageType targetLanguage, String content ) throws UnsupportedTranslationException, UnexpectedTranslationException;
}
