/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/fileload/ImportRecordError.java,v $
 */

package com.biperf.core.domain.fileload;

import java.text.MessageFormat;

import com.biperf.core.domain.BaseDomain;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * Country domain object which represents a country within the Beacon application.
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
 * <td>June 2, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ImportRecordError extends BaseDomain
{
  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  /**
   * The item key for the Content Manager item that contains the message format pattern for this
   * error. This should be the full path
   */
  private String itemKey;
  private Long importRecordId;
  private Long importFileId;

  /**
   * Values for the parameters of the message format pattern for this error.
   */
  private String param1;
  private String param2;
  private String param3;
  private String param4;

  /**
   * BaseDomain Constuctor
   */
  public ImportRecordError()
  {
    super();
  }

  public ImportRecordError( String itemKey )
  {
    this( itemKey, null, null, null, null );
  }

  public ImportRecordError( String itemKey, String param1 )
  {
    this( itemKey, param1, null, null, null );
  }

  public ImportRecordError( String itemKey, String param1, String param2 )
  {
    this( itemKey, param1, param2, null, null );
  }

  public ImportRecordError( String itemKey, String param1, String param2, String param3 )
  {
    this( itemKey, param1, param2, param3, null );
  }

  /**
   * BaseDomain Constuctor
   * 
   * @param itemKey
   * @param param1
   * @param param2
   * @param param3
   * @param param4
   */
  public ImportRecordError( String itemKey, String param1, String param2, String param3, String param4 )
  {
    this.itemKey = itemKey;
    this.param1 = param1;
    this.param2 = param2;
    this.param3 = param3;
    this.param4 = param4;
  }

  // ---------------------------------------------------------------------------
  // Getter and Setter Methods
  // ---------------------------------------------------------------------------

  public String getItemKey()
  {
    return itemKey;
  }

  public String getParam1()
  {
    return param1;
  }

  public String getParam2()
  {
    return param2;
  }

  public String getParam3()
  {
    return param3;
  }

  public String getParam4()
  {
    return param4;
  }

  public void setItemKey( String itemKey )
  {
    this.itemKey = itemKey;
  }

  public void setParam1( String param1 )
  {
    this.param1 = param1;
  }

  public void setParam2( String param2 )
  {
    this.param2 = param2;
  }

  public void setParam3( String param3 )
  {
    this.param3 = param3;
  }

  public void setParam4( String param4 )
  {
    this.param4 = param4;
  }

  /**
   * Returns a message that describes this error.
   * 
   * @return a message that describes this error.
   */
  public String getErrorMessage()
  {
    String messageFormatPattern = CmsResourceBundle.getCmsBundle().getString( itemKey );
    return MessageFormat.format( messageFormatPattern, new Object[] { param1, param2, param3, param4 } );
  }

  // ---------------------------------------------------------------------------
  // Equals, Hashcode, and To String Methods
  // ---------------------------------------------------------------------------

  /**
   * Returns true if this object equals the given object; returns false otherwise.
   * 
   * @see com.biperf.core.domain.BaseDomain#equals(java.lang.Object)
   * @param object the object to compare to this object.
   * @return true if this object equals the given object; false otherwise.
   */
  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }

    if ( ! ( object instanceof ImportRecordError ) )
    {
      return false;
    }

    ImportRecordError importRecordError = (ImportRecordError)object;

    // This class does not have a good business key.
    if ( this.getId() != null && this.getId().equals( importRecordError.getId() ) )
    {
      return true;
    }
    return false;
  }

  /**
   * Returns the hashcode for this object.
   * 
   * @see com.biperf.core.domain.BaseDomain#hashCode()
   * @return the hashcode for this object.
   */
  public int hashCode()
  {
    return this.getId() != null ? this.getId().hashCode() : 0;
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
    buf.append( "ImportRecordError [" );
    buf.append( "{id=" ).append( super.getId() ).append( "}, " );
    buf.append( "{itemCode=" ).append( itemKey ).append( "}," );
    buf.append( "{param1=" ).append( param1 ).append( "}, " );
    buf.append( "{param2=" ).append( param2 ).append( "}, " );
    buf.append( "{param3=" ).append( param3 ).append( "}, " );
    buf.append( "{param4=" ).append( param4 ).append( "}, " );
    buf.append( "]" );
    return buf.toString();
  }

  public Long getImportRecordId()
  {
    return importRecordId;
  }

  public void setImportRecordId( Long importRecordId )
  {
    this.importRecordId = importRecordId;
  }

  public Long getImportFileId()
  {
    return importFileId;
  }

  public void setImportFileId( Long importFileId )
  {
    this.importFileId = importFileId;
  }
}
