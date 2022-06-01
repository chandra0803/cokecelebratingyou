/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.ui.claim;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.biperf.core.domain.claim.Claim;
import com.biperf.core.domain.claim.ClaimElement;
import com.biperf.core.domain.enums.DynaPickListType;
import com.biperf.core.utils.ArrayUtil;

/**
 * Contains common code used by claim form classes.
 * </p>
 * <p/> <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>Thomas Eaton</td>
 * <td>Aug 2, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */

public class ClaimFormUtils
{

  public static void populateClaimElementPickLists( Claim claim )
  {
    // Put all the picklists in the request that we need.
    Iterator claimElementIterator = claim.getClaimElements().iterator();
    while ( claimElementIterator.hasNext() )
    {
      ClaimElement claimElement = (ClaimElement)claimElementIterator.next();
      if ( claimElement.getClaimFormStepElement().getClaimFormElementType().isSelectField() )
      {
        List pickListItems = new ArrayList();
        pickListItems.add( DynaPickListType.lookup( claimElement.getClaimFormStepElement().getSelectionPickListName(), claimElement.getValue() ) );
        claimElement.setPickListItems( pickListItems );
      }
      else if ( claimElement.getClaimFormStepElement().getClaimFormElementType().isMultiSelectField() )
      {
        List pickListItems = new ArrayList();
        // convert the comma delimited list of selected pickListItems to a list of strings
        Iterator pickListCodes = ArrayUtil.convertDelimitedStringToList( claimElement.getValue(), "," ).iterator();
        while ( pickListCodes.hasNext() )
        {
          String code = (String)pickListCodes.next();
          pickListItems.add( DynaPickListType.lookup( claimElement.getClaimFormStepElement().getSelectionPickListName(), code ) );
        }
        claimElement.setPickListItems( pickListItems );
      }
    }
  }

}
