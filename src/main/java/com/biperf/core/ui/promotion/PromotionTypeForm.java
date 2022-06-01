/**
 * 
 */

package com.biperf.core.ui.promotion;

import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.ui.BaseForm;

/**
 * PromotionTypeForm.
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
 * <td>sondgero</td>
 * <td>Jun 27, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionTypeForm extends BaseForm
{
  private String promotionType;
  private String method;
  private String returnActionUrl;
  private int screen;

  public int _getScreen()
  {
    return this.screen;
  }

  public void _setScreen( int screen )
  {
    this.screen = screen;
  }

  public String getMethod()
  {
    return this.method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public String getPromotionType()
  {
    return promotionType;
  }

  public void setPromotionType( String promotionType )
  {
    this.promotionType = promotionType;
  }

  public String getReturnActionUrl()
  {
    return returnActionUrl;
  }

  public void setReturnActionUrl( String returnActionUrl )
  {
    this.returnActionUrl = returnActionUrl;
  }

  public void load( PromotionType promotionType )
  {
    this.promotionType = promotionType.getCode();
  }

  public PromotionType toDomain()
  {
    return PromotionType.lookup( this.promotionType );
  }

}
