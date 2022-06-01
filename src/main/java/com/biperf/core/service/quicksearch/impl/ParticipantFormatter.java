/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.service.quicksearch.impl;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.enums.EmailAddressType;
import com.biperf.core.value.FormattedValueBean;
import com.biperf.core.value.FormatterBean;
import com.biperf.core.value.UserValueBean;

/**
 * ParticipantFormatter.
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
public class ParticipantFormatter implements QuickSearchFormatter
{

  private static final String KEY_PREFIX = "home.quicksearch.participant.headers.";

  /**
   * Overridden from
   * 
   * @return List
   */
  public List getHeaderFullKeys()
  {
    List headerFullKeys = new ArrayList();

    headerFullKeys.add( KEY_PREFIX + "NAME" );
    headerFullKeys.add( KEY_PREFIX + "EMAIL" );
    headerFullKeys.add( KEY_PREFIX + "USERNAME" );
    headerFullKeys.add( KEY_PREFIX + "BANK_ACCOUNT" );
    headerFullKeys.add( KEY_PREFIX + "STATUS" );
    headerFullKeys.add( KEY_PREFIX + "COUNTRY" );

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
    List<FormattedValueBean> formattedValueBeans = new ArrayList<>();

    UserValueBean participant = (UserValueBean)object;
    // First entry is link entry (i.e. has id set)
    formattedValueBeans.add( new FormattedValueBean( participant.getId(), participant.getNameLFMNoComma() ) );
    // Search may return recovery email - but we cannot display it, for security purposes.
    if ( EmailAddressType.RECOVERY.equals( participant.getEmailType() ) )
    {
      formattedValueBeans.add( new FormattedValueBean( "" ) );
    }
    else
    {
      formattedValueBeans.add( new FormattedValueBean( null == participant.getEmailAddress() ? "" : participant.getEmailAddress() ) );
    }
    formattedValueBeans.add( new FormattedValueBean( participant.getUserName() ) );
    formattedValueBeans.add( new FormattedValueBean( participant.getAwardBanqNumber() ) );
    formattedValueBeans.add( new FormattedValueBean( null == participant.getStatus() ? "null" : participant.getStatus() ) );
    formattedValueBeans.add( new FormattedValueBean( participant.getCountry().getI18nCountryName() ) );

    return new FormatterBean( formattedValueBeans );
  }

}
