/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/value/FormattedValueBean.java,v $
 */

package com.biperf.core.value;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.biperf.util.StringUtils;

/**
 * .
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
 * <td>Jun 13, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class FormattedValueBean implements Serializable, Comparable
{
  private String value;
  private List ids;
  private String fnameLname;

  private String externalId;
  private String locale;
  private String title;
  private String middleName;
  private String givenName;
  private String surname;
  private String suffix;
  private String country;
  private String userName;
  private Long userId;
  private String gender;

  public String getGender()
  {
    return gender;
  }

  public void setGender( String gender )
  {
    this.gender = gender;
  }

  public Long getUserId()
  {
    return userId;
  }

  public void setUserId( Long userId )
  {
    this.userId = userId;
  }

  public String getUserName()
  {
    return userName;
  }

  public void setUserName( String userName )
  {
    this.userName = userName;
  }

  public String getExternalId()
  {
    return externalId;
  }

  public void setExternalId( String externalId )
  {
    this.externalId = externalId;
  }

  public String getLocale()
  {
    return locale;
  }

  public void setLocale( String locale )
  {
    this.locale = locale;
  }

  public String getTitle()
  {
    return title;
  }

  public void setTitle( String title )
  {
    this.title = title;
  }

  public String getMiddleName()
  {
    return middleName;
  }

  public void setMiddleName( String middleName )
  {
    this.middleName = middleName;
  }

  public String getGivenName()
  {
    return givenName;
  }

  public void setGivenName( String givenName )
  {
    this.givenName = givenName;
  }

  public String getSurname()
  {
    return surname;
  }

  public void setSurname( String surname )
  {
    this.surname = surname;
  }

  public String getSuffix()
  {
    return suffix;
  }

  public void setSuffix( String suffix )
  {
    this.suffix = suffix;
  }

  public String getCountry()
  {
    return country;
  }

  public void setCountry( String country )
  {
    this.country = country;
  }

  /**
   * 
   */
  public FormattedValueBean()
  {
    super();
  }

  /**
   * @param value
   */
  public FormattedValueBean( String value )
  {
    super();
    this.value = value;
  }

  /**
   * @param id
   * @param value
   */
  public FormattedValueBean( Long id, String value )
  {
    super();
    this.setId( id );
    this.value = value;
  }

  /**
   * @param id
   * @param value
   */
  public FormattedValueBean( List ids, String value )
  {
    super();
    this.setIds( ids );
    this.value = value;
  }

  /**
   * Builds a String representation of this class. Overridden from
   * 
   * @see java.lang.Object#toString()
   * @return String
   */
  public String toString()
  {
    final StringBuffer buf = new StringBuffer();
    buf.append( "FormattedValueBean [" );
    buf.append( "{id=" + this.getId() + "}, " );
    buf.append( "{value=" + this.getValue() + "} " );
    buf.append( "]" );
    return buf.toString();
  }

  public int hashCode()
  {
    int hashCode = 0;
    if ( getIds() != null )
    {
      hashCode = getIds().hashCode();
    }
    if ( getValue() != null )
    {
      hashCode = hashCode * 31 + getValue().hashCode();
    }

    return hashCode;
  }

  /**
   * Checks equality of the object parameter to this. Overridden from
   * 
   * @see com.biperf.core.domain.BaseDomain#equals(java.lang.Object)
   * @param object
   * @return boolean
   */
  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }

    if ( ! ( object instanceof FormattedValueBean ) )
    {
      return false;
    }

    final FormattedValueBean valueBean = (FormattedValueBean)object;

    if ( getIds().equals( valueBean.getIds() ) )
    {
      return StringUtils.equals( getValue(), valueBean.getValue() );
    }
    return false;
  }

  public Long getId()
  {
    if ( getIds() != null && !getIds().isEmpty() )
    {
      return (Long)getIds().get( 0 );
    }
    return null;
  }

  public Long getCountryId()
  {
    if ( getIds() != null && !getIds().isEmpty() && getIds().size() > 2 )
    {
      return (Long)getIds().get( 2 );
    }
    return null;
  }

  public String getIdsAsString()
  {
    StringBuffer idStringBuffer = new StringBuffer();
    if ( getIds() != null )
    {
      for ( Iterator idIter = getIds().iterator(); idIter.hasNext(); )
      {
        if ( idStringBuffer.length() != 0 )
        {
          idStringBuffer.append( ':' );
        }
        Object paxId = idIter.next();
        if ( "np".equals( paxId.toString() ) )
        {
          idStringBuffer.append( paxId.toString() );
        }
        else
        {
          Long currentId = (Long)paxId;
          idStringBuffer.append( currentId.toString() );
        }
      }
    }
    return idStringBuffer.toString();
  }

  public void setIdsAsString( String idString )
  {
    String[] ids = idString.split( ":" );
    List newIds = new ArrayList();
    for ( int i = 0; i < ids.length; i++ )
    {
      newIds.add( new Long( ids[i] ) );
    }
    setIds( newIds );

  }

  public void setId( Long id )
  {
    if ( id != null )
    {
      if ( getIds().size() > 0 )
      {
        getIds().set( 0, id );
      }
      else
      {
        getIds().add( id );
      }
    }
  }

  public void setStringAsIds( String idString )
  {
    String[] ids = idString.split( ":" );
    List newIds = new ArrayList();
    for ( int i = 0; i < ids.length; i++ )
    {
      newIds.add( ids[i] );
    }
    setIds( newIds );

  }

  public String getValue()
  {
    return value;
  }

  public void setValue( String value )
  {
    this.value = value;
  }

  public String getFormattedId()
  {
    StringBuffer buffer = new StringBuffer();
    buffer.append( getIdsAsString() );
    if ( "np".equals( getIdsAsString() ) )
    {
      buffer.append( ':' );
    }
    else
    {
      buffer.append( '-' );
    }
    buffer.append( getValue() );
    return buffer.toString();
  }

  /**
   * This assumes value comes in as: "lastname, firstname space hyphen space some-text-or-none space
   * hyphen space some-text-or-none" e.g. Brown, Mary - - This method strips out the hyphenated text
   * to the right of the firstname and returns the firstname For Example: if value is returns
   * ------------------------------------------------------------------------------- Johnson,
   * Mary-Ann - Supervisor - Customer Department => Mary-Ann Sondgero, Andrew - - => Andrew Davis,
   * Doug E - Manager - => Doug Extra-Credit-Call, Elizabeth - - => Elizabeth D-B-Princ, Jessica - - =>
   * Jessica handling situtation when hyphens maybe present within the lastname or firstname.
   * 
   * For the recognition rewrite the values look like: "lastName, firstName; some-text-or-none; 
   * some-text-or-none; some-text-or-none" e.g. Brown Mary; ; ;
   * @return firstname
   */
  public String getFirstName()
  {
    String firstNamePortion = value.substring( value.indexOf( ", " ) + 2, value.length() );
    String[] split = firstNamePortion.split( "( -)|(; )" );
    if ( split.length > 0 )
    {
      return split[0];
    }
    return "";
  }

  public String getLastName()
  {
    return value.substring( 0, value.indexOf( ", " ) );
  }

  /**
   * Takes a String in the format of id:id2:id3-value and sets the ids to a
   * List of id,id2,id3... - and the value to what is after the -. 
   * Then returns a new FormattedValueBean.
   * 
   * @param formattedId
   * @return FormattedValueBean
   */
  public static FormattedValueBean parseFormattedId( String formattedId )
  {
    FormattedValueBean formattedValueBean = new FormattedValueBean();

    int delimeterIndex = formattedId.indexOf( "-" );
    String idString = formattedId.substring( 0, delimeterIndex );
    formattedValueBean.setIdsAsString( idString );
    formattedValueBean.setValue( formattedId.substring( delimeterIndex + 1 ) );

    return formattedValueBean;
  }

  public static FormattedValueBean parseContributorFormattedId( String formattedId )
  {
    FormattedValueBean valueBean = new FormattedValueBean();
    int delimeterIndex = 0;
    String idString = "";
    if ( formattedId.startsWith( "np" ) )
    {
      delimeterIndex = formattedId.indexOf( ":" );
      idString = formattedId.substring( 0, delimeterIndex );
      valueBean.setStringAsIds( idString );
    }
    else
    {
      delimeterIndex = formattedId.indexOf( "-" );
      idString = formattedId.substring( 0, delimeterIndex );
      valueBean.setIdsAsString( idString );
    }
    valueBean.setValue( formattedId.substring( delimeterIndex + 1 ) );
    return valueBean;
  }

  /**
   * Returns true if the formattedValueBeans collection of FormattedValueBean objects contains the
   * given id.
   */
  public static boolean containsId( Collection formattedValueBeans, Long id )
  {
    boolean idFound = false;
    for ( Iterator iter = formattedValueBeans.iterator(); iter.hasNext(); )
    {
      FormattedValueBean formattedValueBean = (FormattedValueBean)iter.next();
      if ( formattedValueBean.getId() != null && formattedValueBean.getId().equals( id ) )
      {
        idFound = true;
        break;
      }
    }

    return idFound;
  }

  /**
   * Returns the value of the FormattedValueBean object containing the given id found in the
   * formattedValueBeans collection.
   */
  public static String getValueFromCollection( Collection formattedValueBeans, Long id )
  {
    String value = null;
    for ( Iterator iter = formattedValueBeans.iterator(); iter.hasNext(); )
    {
      FormattedValueBean formattedValueBean = (FormattedValueBean)iter.next();
      if ( formattedValueBean.getId() != null && formattedValueBean.getId().equals( id ) )
      {
        value = formattedValueBean.getValue();
        break;
      }
    }

    return value;
  }

  public int compareTo( Object object ) throws ClassCastException
  {
    if ( ! ( object instanceof FormattedValueBean ) )
    {
      throw new ClassCastException( "A formattedValueBean was expected." );
    }
    FormattedValueBean formattedValueBean = (FormattedValueBean)object;
    return this.getValue().compareTo( formattedValueBean.getValue() );

  }

  public String getValueAtIndex( int index )
  {
    int beginIndex = value.indexOf( " -" );
    String remainingString = null;
    if ( beginIndex >= 0 )
    {
      remainingString = value.substring( beginIndex );
    }
    else
    {
      return "";
    }
    for ( int counter = 0; counter <= index; counter++ )
    {
      beginIndex = remainingString.indexOf( " -" );
      if ( beginIndex >= 0 )
      {
        remainingString = remainingString.substring( beginIndex );
      }

      if ( counter == index )
      {
        return remainingString;
      }
    }
    return "";

  }

  public List getIds()
  {
    if ( ids == null )
    {
      ids = new ArrayList();
    }
    return ids;

  }

  public void setIds( List ids )
  {
    this.ids = ids;
  }

  public String getFnameLname()
  {
    return fnameLname;
  }

  public void setFnameLname( String fnameLname )
  {
    this.fnameLname = fnameLname;
  }
}
