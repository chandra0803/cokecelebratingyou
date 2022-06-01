/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/domain/claim/ClaimFormTest.java,v $
 */

package com.biperf.core.domain.claim;

import com.biperf.core.domain.enums.ClaimFormStatusType;
import com.biperf.core.domain.enums.MockPickListFactory;

import junit.framework.TestCase;

/**
 * ClaimFormTest <p/> <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>tennant</td>
 * <td>Jun 10, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */

public class ClaimFormTest extends TestCase
{

  public void testIsEditable()
  {
    ClaimForm form = new ClaimForm();
    form.setClaimFormStatusType( (ClaimFormStatusType)MockPickListFactory.getMockPickListItem( ClaimFormStatusType.class, ClaimFormStatusType.TEMPLATE ) );
    assertFalse( form.isEditable() );

    form.setClaimFormStatusType( (ClaimFormStatusType)MockPickListFactory.getMockPickListItem( ClaimFormStatusType.class, ClaimFormStatusType.UNDER_CONSTRUCTION ) );
    assertTrue( form.isEditable() );
    form.setClaimFormStatusType( (ClaimFormStatusType)MockPickListFactory.getMockPickListItem( ClaimFormStatusType.class, ClaimFormStatusType.COMPLETED ) );
    assertTrue( form.isEditable() );
    form.setClaimFormStatusType( (ClaimFormStatusType)MockPickListFactory.getMockPickListItem( ClaimFormStatusType.class, ClaimFormStatusType.ASSIGNED ) );
    assertFalse( form.isEditable() );

  }
}
