/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/promotion/PromotionAudienceFormBean.java,v $
 */

package com.biperf.core.ui.promotion;

import java.io.Serializable;

/*
 * PromotionAudienceFormBean <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th>
 * <th>Date</th> <th>Version</th> <th>Comments</th> </tr> <tr> <td>sedey</td> <td>Sep 22,
 * 2005</td> <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 *
 */

/**
 * PromotionAudienceFormBean.
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
 * <td>Tammy Cheng</td>
 * <td>Oct 3, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionAudienceFormBean implements Serializable
{
  private Long id;
  private String name;
  private Long audienceId;
  private String audienceType;
  private String teamPositionCode;
  private Long version;
  private int size;
  private boolean required;
  private boolean removed;
  private boolean selected;

  /**
   * @return id
   */
  public Long getId()
  {
    return id;
  }

  /**
   * @param id
   */
  public void setId( Long id )
  {
    this.id = id;
  }

  /**
   * @return name
   */
  public String getName()
  {
    return name;
  }

  /**
   * @param name
   */
  public void setName( String name )
  {
    this.name = name;
  }

  /**
   * @return size
   */
  public int getSize()
  {
    return size;
  }

  /**
   * @param size
   */
  public void setSize( int size )
  {
    this.size = size;
  }

  /**
   * @return true if required; false if not required
   */
  public boolean isRequired()
  {
    return required;
  }

  /**
   * @param required
   */
  public void setRequired( boolean required )
  {
    this.required = required;
  }

  /**
   * @return true if removed; false if not removed
   */
  public boolean isRemoved()
  {
    return removed;
  }

  /**
   * @param removed
   */
  public void setRemoved( boolean removed )
  {
    this.removed = removed;
  }

  /**
   * @return true if selected; false if not selected
   */
  public boolean isSelected()
  {
    return selected;
  }

  /**
   * @param selected
   */
  public void setSelected( boolean selected )
  {
    this.selected = selected;
  }

  /**
   * @return audienceType
   */
  public String getAudienceType()
  {
    return audienceType;
  }

  /**
   * @param audienceType
   */
  public void setAudienceType( String audienceType )
  {
    this.audienceType = audienceType;
  }

  /**
   * @return audienceId
   */
  public Long getAudienceId()
  {
    return audienceId;
  }

  /**
   * @param audienceId
   */
  public void setAudienceId( Long audienceId )
  {
    this.audienceId = audienceId;
  }

  /**
   * @return teamPositionCode
   */
  public String getTeamPositionCode()
  {
    return teamPositionCode;
  }

  /**
   * @param teamPositionCode
   */
  public void setTeamPositionCode( String teamPositionCode )
  {
    this.teamPositionCode = teamPositionCode;
  }

  public Long getVersion()
  {
    return version;
  }

  public void setVersion( Long version )
  {
    this.version = version;
  }

}
