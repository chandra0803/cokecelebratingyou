/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.service.quicksearch.impl;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.claim.Claim;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.value.FormattedValueBean;
import com.biperf.core.value.FormatterBean;

/**
 * ClaimFormatter.
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
 * <td>wadzinsk</td>
 * <td>Sep 19, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ClaimFormatter implements QuickSearchFormatter
{

  private static final String KEY_PREFIX = "home.quicksearch.claim.headers.";

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.quicksearch.impl.QuickSearchFormatter#getHeaderFullKeys()
   * @return List
   */
  public List getHeaderFullKeys()
  {
    List headerFullKeys = new ArrayList();

    headerFullKeys.add( KEY_PREFIX + "CLAIM_NUMBER" );
    headerFullKeys.add( KEY_PREFIX + "STATUS" );
    headerFullKeys.add( KEY_PREFIX + "SUBMIT_DATE" );
    headerFullKeys.add( KEY_PREFIX + "SUBMITTER_NAME" );

    return headerFullKeys;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.quicksearch.impl.QuickSearchFormatter#format(java.lang.Object)
   * @param object
   * @return a FormatterBean which is a wrapper for a List of FormattedValueBean objects. Wrapper
   *         Bean is needed for sorting.
   */
  public FormatterBean format( Object object )
  {
    List formattedValueBeans = new ArrayList();

    Claim claim = (Claim)object;

    // First entry is link entry (i.e. has id set)
    formattedValueBeans.add( new FormattedValueBean( claim.getId(), claim.getClaimNumber() ) );

    formattedValueBeans.add( new FormattedValueBean( claim.getOpen() ? "Open" : "Closed" ) );

    formattedValueBeans.add( new FormattedValueBean( DateUtils.toDisplayString( claim.getSubmissionDate() ) ) );

    formattedValueBeans.add( new FormattedValueBean( claim.getSubmitter().getNameLFMNoComma() ) );

    return new FormatterBean( formattedValueBeans );
  }

}
