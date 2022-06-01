
package com.biperf.core.domain.plateauawards;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * This class object is used to generate communication json object for new G5 layout.
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>sharafud</td>
 * <td>dec 06, 2012</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */

// This annotation is used to not to serialize the null properties.
@JsonInclude( value = Include.NON_NULL )
public class PlateauMerchProductsView
{
  // This is for future use to keep additional messages
  private String[] messages = {};
  private List<PlateauMerchProducts> products;

  public String[] getMessages()
  {
    return messages;
  }

  public void setMessages( String[] messages )
  {
    this.messages = messages;
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
