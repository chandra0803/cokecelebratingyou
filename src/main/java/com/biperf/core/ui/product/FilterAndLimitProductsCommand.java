
package com.biperf.core.ui.product;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.biperf.core.domain.product.Product;
import com.biperf.core.value.FormattedValueBean;

public class FilterAndLimitProductsCommand implements Serializable
{

  private static final long serialVersionUID = 3256726164994536240L;

  private Collection productsToBeFiltered;
  private Collection productsToBeRetainedExclusively;

  public void setProductsToBeFiltered( Collection productsToBeFiltered )
  {
    this.productsToBeFiltered = productsToBeFiltered;
  }

  public Collection getProductsToBeFiltered()
  {
    return productsToBeFiltered;
  }

  public void setProductsToBeRetainedExclusively( Collection productsToBeRetainedExclusively )
  {
    this.productsToBeRetainedExclusively = productsToBeRetainedExclusively;
  }

  public Collection getProductsToBeRetainedExclusively()
  {
    return productsToBeRetainedExclusively;
  }

  public void execute( Collection products )
  {

    if ( productsToBeRetainedExclusively != null )
    {
      Set productIds = new HashSet();
      for ( Iterator iterator = productsToBeRetainedExclusively.iterator(); iterator.hasNext(); )
      {
        Product p = (Product)iterator.next();
        productIds.add( p.getId() );
      }
      for ( Iterator iterator = products.iterator(); iterator.hasNext(); )
      {
        FormattedValueBean prodValBean = (FormattedValueBean)iterator.next();
        if ( !productIds.contains( prodValBean.getId() ) )
        {
          iterator.remove();
        }
      }
    }
    if ( productsToBeFiltered != null )
    {
      for ( Iterator iterator = productsToBeFiltered.iterator(); iterator.hasNext(); )
      {
        Product product = (Product)iterator.next();
        for ( Iterator iterator2 = products.iterator(); iterator2.hasNext(); )
        {
          FormattedValueBean prodBean = (FormattedValueBean)iterator2.next();
          Long prodId = prodBean.getId();
          if ( product.getId().equals( prodId ) )
          {
            iterator2.remove();
          }
        }
      }
    }
  }
}
