/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/enums/PickListItem.java,v $
 */

package com.biperf.core.domain.enums;

import java.io.Serializable;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;

/**
 * The PickListItem class represents a single type safe enum of a pick list from the content
 * manager. Subclass this class for each picklist defined in content manager. Subclasses shall look
 * like the following code sample:
 * 
 * <pre>
 * public class EmailAddressType extends PickListItem
 * {
 * 
 *   //This is the asset name from content manager
 *   private static final String PICKLIST_ASSET = &quot;picklist.emailtype&quot;
 * 
 *   //The default constructor shall be protected - we don't want users to create these.
 *   protected EmailAddressType()
 *   {
 *     super();
 *   }
 * 
 *   //Return a list of PickListItems that are marked as active
 *   public static List getList()
 *   {
 *     return PickListFactory.getPickList( EmailAddressType.class );
 *   }
 * 
 *   //Returns null if code is invalid
 *   public static EmailAddressType lookup( String code )
 *   {
 *     return (EmailAddressType)PickListFactory.getPickListItem( EmailAddressType.class, code );
 *   }
 * 
 *   //Returns null if the defaultItem is not defined - or invalid
 *   public static EmailAddressType getDefaultItem()
 *   {
 *     return (EmailAddressType)PickListFactory.getDefaultPickListItem( EmailAddressType.class );
 *   }
 * 
 *   public String getPickListAssetName()
 *   {
 *     return PICKLIST_ASSET;
 *   }
 * }
 * </pre>
 * 
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
 * <td>dunne</td>
 * <td>Apr 6, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public abstract class PickListItem implements PickListInterface, Serializable
{

  private String code;
  private String name;
  private String desc;
  private String abbr;
  private boolean active;
  private int sortOrder;
  private Map contentMap;

  // private static final String DEFAULT_ITEM = ":defaultItem";
  // private static final String LIST_TYPE = ":listType";
  public static final String ASSET_ITEM_SUFFIX = ".items";
  // private static final String ASSET_DESCRIPTION_SUFFIX = ".description";
  public static final String ITEM_NAME_KEY = "NAME";
  public static final String ITEM_ABBR_KEY = "ABBR";
  public static final String ITEM_DESC_KEY = "DESC";
  public static final String ITEM_CODE_KEY = "CODE";
  public static final String ITEM_STATUS_KEY = "STATUS";

  private static PickListFactory pickListFactory = new PickListFactoryImpl();

  protected PickListItem()
  {
    // empty
  }

  public String getCode()
  {
    return code;
  }

  public void setCode( String code )
  {
    this.code = code;
  }

  public boolean isActive()
  {
    return active;
  }

  protected void setActive( boolean active )
  {
    this.active = active;
  }

  /**
   * Get the Default PickList item.
   * 
   * @param PickListClazz
   * @return default picklist item
   */
  public static PickListItem getDefaultItem( Class PickListClazz )
  {
    return getPickListFactory().getDefaultPickListItem( PickListClazz );
  }

  public static PickListItem getInvalidItem( Class clazz )
  {
    PickListItem pickListItem = null;
    try
    {
      pickListItem = (PickListItem)clazz.newInstance();
    }
    catch( InstantiationException e )
    {

      e.printStackTrace();
    }
    catch( IllegalAccessException e )
    {

      e.printStackTrace();
    }
    pickListItem.setCode( "invalidEntry" );
    return pickListItem;
  }

  /**
   * Get the translated item name from content manager.
   * 
   * @return String PickListItem name
   */
  public String getName()
  {
    // Bug # 36018
    return StringEscapeUtils.unescapeHtml4( name ); // getPickListFactory().getItemDescription(
                                                    // getPickListDescriptionAssetName(),
    // code.toUpperCase() + ITEM_NAME );
  }

  /**
   * Get the translated item description from content manager.
   * 
   * @return String PickListItem description
   */
  public String getDescription()
  {
    return StringEscapeUtils.unescapeHtml4( desc ); // getPickListFactory().getItemDescription(
                                                    // getPickListDescriptionAssetName(),
    // code.toUpperCase() + ITEM_DESC );
  }

  /**
   * Get the translated item abbreviation from content manager.
   * 
   * @return String PickListItem abbreviation
   */
  public String getAbbreviation()
  {
    return abbr; // getPickListFactory().getItemDescription( getPickListDescriptionAssetName(),
    // code.toUpperCase() + ITEM_ABBR );
  }

  /**
   * @return PickList content manager Asset code.
   */
  public abstract String getPickListAssetCode();

  /**
   * Override this method if your PickListItems asset name does not equal getPickListAssetName() +
   * ".items".
   * 
   * @return String PickListItem Asset code
   */
  public String getPickListItemsAssetCode()
  {
    return getPickListAssetCode() + ASSET_ITEM_SUFFIX;
  }

  /**
   * Sets aPickListFactory for use when testing.
   * 
   * @param injectedPickListFactory
   */
  public static void setPickListFactory( PickListFactory injectedPickListFactory )
  {
    pickListFactory = injectedPickListFactory;
  }

  /**
   * Get the current PickListFactory
   * 
   * @return PickListFactory
   */
  public static PickListFactory getPickListFactory()
  {
    return pickListFactory;
  }

  protected Map getContentMap()
  {
    return contentMap;
  }

  protected void setContentMap( Map contentMap )
  {
    this.contentMap = contentMap;
  }

  /**
   * @return value of listName property
   */
  // public String getListName()
  // {
  // return getPickListFactory().getItemDescription( getPickListAssetName(), "NAME" );
  // }
  /**
   * Overridden from
   * 
   * @see java.lang.Object#toString()
   * @return <code>java.lang.String</code>
   */
  public String toString()
  {
    final StringBuffer buf = new StringBuffer();
    buf.append( this.getClass().getName() );
    buf.append( "{code=" ).append( getCode() );
    buf.append( ",active=" ).append( active );
    buf.append( '}' );
    return buf.toString();
  }

  /**
   * Overridden from
   * 
   * @see java.lang.Object#equals(java.lang.Object)
   * @param o
   * @return boolean
   */
  public boolean equals( Object o )
  {
    if ( this == o )
    {
      return true;
    }
    if ( ! ( o instanceof PickListItem ) )
    {
      return false;
    }

    final PickListItem pickListItem = (PickListItem)o;

    if ( !this.getClass().equals( pickListItem.getClass() ) )
    {
      return false;
    }
    if ( !getCode().equals( pickListItem.getCode() ) )
    {
      return false;
    }

    return true;
  }

  /**
   * Overridden from
   * 
   * @see java.lang.Object#hashCode()
   * @return int
   */
  public int hashCode()
  {
    int result;
    result = this.getClass().getName().hashCode();
    result = 29 * result + getCode().hashCode();
    return result;
  }

  public String getAbbr()
  {
    return abbr;
  }

  public void setAbbr( String abbr )
  {
    this.abbr = abbr;
  }

  public String getDesc()
  {
    return desc;
  }

  public void setDesc( String desc )
  {
    this.desc = desc;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  public int getSortOrder()
  {
    return sortOrder;
  }

  public void setSortOrder( int sortOrder )
  {
    this.sortOrder = sortOrder;
  }

  /**
   * clear the reference to the PickListFactory object
   */
  public static void clearFactory()
  {
    pickListFactory = null;
  }

  /**
   * initialize the PickListFactory object
   */
  public static void initFactory()
  {
    if ( null == pickListFactory )
    {
      pickListFactory = new PickListFactoryImpl();
    }
  }

}
