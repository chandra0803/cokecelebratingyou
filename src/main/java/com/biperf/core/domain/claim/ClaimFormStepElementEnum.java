/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/claim/ClaimFormStepElementEnum.java,v $
 */

package com.biperf.core.domain.claim;

import com.biperf.core.domain.enums.ClaimFormElementType;

/**
 * ClaimFormStepElementType is the type for the Elements of the ClaimFormStep that can be added as
 * fields to a step. If there are more static enums like this the static methods in this class can
 * be extracted.
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
 * <td>Adam</td>
 * <td>Jun 13, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public enum ClaimFormStepElementEnum
{

  NUMBER_FIELD( ClaimFormElementType.NUMBER_FIELD, "Number" ), TEXT_FIELD( ClaimFormElementType.TEXT_FIELD, "Text" ), TEXT_BOX_FIELD( ClaimFormElementType.TEXT_BOX_FIELD, "Text Box" ), SECTION_FIELD(
      ClaimFormElementType.SECTION_HEADING, "Section" ), COPY_FIELD( ClaimFormElementType.COPY_BLOCK, "Copy" ), LINK_FIELD( ClaimFormElementType.LINK,
          "Link" ), DATE_FIELD( ClaimFormElementType.DATE_FIELD, "Date" ), BOOLEAN_FIELD( ClaimFormElementType.BOOLEAN, "Boolean" ), SELECTION_FIELD( ClaimFormElementType.SELECTION,
              "Selection" ), MULTI_SELECTION_FIELD( ClaimFormElementType.MULTI_SELECTION, "MultiSelection" ), BUTTON_FIELD( ClaimFormElementType.BUTTON, "Button" );

  private final String name;
  private final String value;

  /**
   * @param name
   * @param value
   */
  private ClaimFormStepElementEnum( String name, String value )
  {
    this.name = name;
    this.value = value;
  }

  public String getName()
  {
    return name;
  }

  /**
   * <p>
   * Get value of enum item.
   * </p>
   * 
   * @return the enum item's value.
   */
  public String getValue()
  {
    return value;
  }

}
