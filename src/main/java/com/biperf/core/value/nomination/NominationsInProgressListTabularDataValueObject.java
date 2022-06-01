
package com.biperf.core.value.nomination;

import java.util.ArrayList;
import java.util.List;

public class NominationsInProgressListTabularDataValueObject
{
  private NominationsInProgressListMetaValueObject meta = new NominationsInProgressListMetaValueObject();
  private List<NominationsInProgressValueObject> results = new ArrayList<NominationsInProgressValueObject>();

  public NominationsInProgressListMetaValueObject getMeta()
  {
    return meta;
  }

  public void setMeta( NominationsInProgressListMetaValueObject meta )
  {
    this.meta = meta;
  }

  public List<NominationsInProgressValueObject> getResults()
  {
    return results;
  }

  public void setResults( List<NominationsInProgressValueObject> results )
  {
    this.results = results;
  }
}
