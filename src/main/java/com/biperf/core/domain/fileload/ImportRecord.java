/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/fileload/ImportRecord.java,v $
 */

package com.biperf.core.domain.fileload;

import java.util.LinkedHashSet;
import java.util.Set;

import com.biperf.core.domain.BaseDomain;

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
public abstract class ImportRecord extends BaseDomain
{
  // ---------------------------------------------------------------------------
  // Constants
  // ---------------------------------------------------------------------------

  /* Action Types */
  public static final String ADD_ACTION_TYPE = "add";
  public static final String UPDATE_ACTION_TYPE = "upd";
  public static final String DELETE_ACTION_TYPE = "del";

  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  /**
   * The ID of the import file that contains this import record.
   */
  private Long importFileId;

  /**
   * Indicates how to process this import record.
   */
  private String actionType;

  private Integer anniversaryNumberOfDaysOrYears = new Integer( 0 );

  /**
   * Describes errors that occurred while processing this import record.
   */
  private Set importRecordErrors = new LinkedHashSet();

  // ---------------------------------------------------------------------------
  // Getter and Setter Methods
  // ---------------------------------------------------------------------------

  public String getActionType()
  {
    return actionType;
  }

  public Long getImportFileId()
  {
    return importFileId;
  }

  public Set getImportRecordErrors()
  {
    return importRecordErrors;
  }

  public void setActionType( String actionType )
  {
    this.actionType = actionType;
  }

  public void setImportFileId( Long importFileId )
  {
    this.importFileId = importFileId;
  }

  public Integer getAnniversaryNumberOfDaysOrYears()
  {
    return anniversaryNumberOfDaysOrYears;
  }

  public void setAnniversaryNumberOfDaysOrYears( Integer anniversaryNumberOfDaysOrYears )
  {
    this.anniversaryNumberOfDaysOrYears = anniversaryNumberOfDaysOrYears;
  }

  public void setImportRecordErrors( Set importRecordErrors )
  {
    this.importRecordErrors = importRecordErrors;
  }

  // ---------------------------------------------------------------------------
  // Predicate Methods
  // ---------------------------------------------------------------------------

  /**
   * Returns true if entity described by this import record should be inserted into the
   * application's database; returns false otherwise.
   * 
   * @return true if entity described by this import record should be inserted into the
   *         application's database; returns false otherwise.
   */
  public boolean isAdd()
  {
    return ADD_ACTION_TYPE.equalsIgnoreCase( actionType );
  }

  /**
   * Returns true if entity described by this import record should be updated in the application's
   * database; returns false otherwise.
   * 
   * @return true if entity described by this import record should be updated in the application's
   *         database; returns false otherwise.
   */
  public boolean isUpdate()
  {
    return UPDATE_ACTION_TYPE.equalsIgnoreCase( actionType );
  }

  /**
   * Returns true if entity described by this import record should be deleted from the application's
   * database; returns false otherwise.
   * 
   * @return true if entity described by this import record should be deleted from the application's
   *         database; returns false otherwise.
   */
  public boolean isDelete()
  {
    return DELETE_ACTION_TYPE.equalsIgnoreCase( actionType );
  }
}
