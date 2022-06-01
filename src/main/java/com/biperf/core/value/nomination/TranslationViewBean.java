
package com.biperf.core.value.nomination;

import java.util.ArrayList;
import java.util.List;

public class TranslationViewBean
{
  private List<TranslationFieldsViewBean> fields = new ArrayList<TranslationFieldsViewBean>();

  public List<TranslationFieldsViewBean> getFields()
  {
    return fields;
  }

  public void setFields( List<TranslationFieldsViewBean> fields )
  {
    this.fields = fields;
  }
}
