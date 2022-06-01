/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/promotion/PromotionECardFormBean.java,v $
 */

package com.biperf.core.ui.promotion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.multimedia.EcardFormBean;

/**
 * PromotionECardFormBean.
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
 * <td>sedey</td>
 * <td>Oct 07, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionECardFormBean extends PromotionECardCertificateAbstractFormBean implements Serializable
{
  private Long cardId;
  private String categoryId;
  private String categoryName;
  private boolean translatable;
  private List<EcardFormBean> localeList = new ArrayList<EcardFormBean>();
  private String orderNumber;
  private String flashName;
  private String displayName;
  private String locale;
  private boolean active;

  public PromotionECardFormBean()
  {
    // empty constructor
  }

  public String getCategoryId()
  {
    return categoryId;
  }

  public void setCategoryId( String categoryId )
  {
    this.categoryId = categoryId;
  }

  public String getCategoryName()
  {
    return categoryName;
  }

  public void setCategoryName( String categoryName )
  {
    this.categoryName = categoryName;
  }

  public Long getCardId()
  {
    return cardId;
  }

  public void setCardId( Long cardId )
  {
    this.cardId = cardId;
  }

  public void setTranslatable( boolean translatable )
  {
    this.translatable = translatable;
  }

  public boolean isTranslatable()
  {
    return translatable;
  }

  public void setLocaleListAsList( List<EcardFormBean> localeList )
  {
    this.localeList = localeList;
  }

  public List<EcardFormBean> getLocaleListAsList()
  {
    return localeList;
  }

  public EcardFormBean getLocaleList( int index )
  {
    try
    {
      return (EcardFormBean)localeList.get( index );
    }
    catch( Exception anException )
    {
      return null;
    }
  }
  
  public int getLocaleListCount()
  {
    if ( this.localeList == null )
    {
      return 0;
    }

    return this.localeList.size();
  }

  public String getOrderNumber()
  {
    return orderNumber;
  }

  public void setOrderNumber( String orderNumber )
  {
    this.orderNumber = orderNumber;
  }

  public String getFlashName()
  {
    return flashName;
  }

  public void setFlashName( String flashName )
  {
    this.flashName = flashName;
  }

  public String getDisplayName()
  {
    return displayName;
  }

  public void setDisplayName( String displayName )
  {
    this.displayName = displayName;
  }

  public String getLocale()
  {
    return locale;
  }

  public void setLocale( String locale )
  {
    this.locale = locale;
  }

  public boolean isActive()
  {
    return active;
  }

  public void setActive( boolean active )
  {
    this.active = active;
  }

}
