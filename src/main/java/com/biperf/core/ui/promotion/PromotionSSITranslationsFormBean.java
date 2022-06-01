
package com.biperf.core.ui.promotion;

import java.util.ArrayList;
import java.util.List;

public class PromotionSSITranslationsFormBean
{
  private String translationEN;
  private String description;
  private String translationCMKey;
  private List<PromotionSSITranslationsDetailBean> details = new ArrayList<PromotionSSITranslationsDetailBean>();

  public String getTranslationEN()
  {
    return translationEN;
  }

  public void setTranslationEN( String translationNameEN )
  {
    this.translationEN = translationNameEN;
  }

  public String getDescription()
  {
    return description;
  }

  public void setDescription( String description )
  {
    this.description = description;
  }

  public String getTranslationCMKey()
  {
    return translationCMKey;
  }

  public void setTranslationCMKey( String translationCMKey )
  {
    this.translationCMKey = translationCMKey;
  }

  public List<PromotionSSITranslationsDetailBean> getDetails()
  {
    return details;
  }

  public void setDetails( List<PromotionSSITranslationsDetailBean> details )
  {
    this.details = details;
  }

  public PromotionSSITranslationsDetailBean getDetail( int index )
  {

    // indexes to not come in order, populate empty spots
    while ( index >= this.details.size() )
    {
      this.details.add( new PromotionSSITranslationsDetailBean() );
    }

    // return the requested item
    return this.details.get( index );
  }

}
