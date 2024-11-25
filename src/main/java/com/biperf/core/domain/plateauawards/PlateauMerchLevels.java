
package com.biperf.core.domain.plateauawards;

import java.util.List;

public class PlateauMerchLevels
{

  private String id;
  private String name;
  private String desc;
  private List<PlateauMerchProducts> products;

  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  public String getId()
  {
    return id;
  }

  public void setId( String id )
  {
    this.id = id;
  }

  public String getDesc()
  {
    return desc;
  }

  public void setDesc( String desc )
  {
    this.desc = desc;
  }

  public List<PlateauMerchProducts> getProducts()
  {
    return products;
  }

  public void setProducts( List<PlateauMerchProducts> products )
  {
    this.products = products;
  }

}
