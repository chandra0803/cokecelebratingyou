/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/participant/ParticipantPromotionsForm.java,v $
 */

package com.biperf.core.ui.participant;

import java.util.List;

import com.biperf.core.ui.BaseForm;

/*
 * ParticipantPromotionsForm <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th>
 * <th>Date</th> <th>Version</th> <th>Comments</th> </tr> <tr> <td>OPI Admin</td> <td>Nov
 * 16, 2005</td> <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 *
 */

public class ParticipantPromotionsForm extends BaseForm
{
  private String method;
  private String userId;
  private String moduleType;
  private List promotions;

  public void load( String userId, String moduleType, List promotionsList )
  {
    this.userId = userId;
    this.moduleType = moduleType;
    this.promotions = promotionsList;
  }

  public String getUserId()
  {
    return userId;
  }

  public void setUserId( String userId )
  {
    this.userId = userId;
  }

  public String getModuleType()
  {
    return moduleType;
  }

  public void setModuleType( String moduleType )
  {
    this.moduleType = moduleType;
  }

  public List getPromotions()
  {
    return promotions;
  }

  public void setPromotions( List promotions )
  {
    this.promotions = promotions;
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }
}
