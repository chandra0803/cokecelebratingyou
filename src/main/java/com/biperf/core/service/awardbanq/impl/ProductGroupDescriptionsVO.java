
package com.biperf.core.service.awardbanq.impl;

import java.util.ArrayList;
import java.util.List;

public class ProductGroupDescriptionsVO
{
  private List<ProductEntryVO> entry = new ArrayList<ProductEntryVO>();

  public ProductGroupDescriptionsVO()
  {

  }

  public List<ProductEntryVO> getEntry()
  {
    return entry;
  }

  public void setEntry( List<ProductEntryVO> entry )
  {
    this.entry = entry;
  }

}
