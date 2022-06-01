/*
 * (c) 2019 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.service.rosterproxy;

import com.biperf.core.service.rosterproxy.V1.RosterProxyRequest;
import com.biperf.core.service.rosterproxy.V1.RosterProxyView;
import com.biperf.core.service.rosterproxy.V1.RosterPutRequest;

public interface RosterProxyRepository
{
  public static final String BEAN_NAME = "rosterProxyRepository";

  public RosterProxyView getRosterProxyInfo();

  public RosterProxyView postRosterProxyInfo( RosterProxyRequest request );

  public RosterProxyView putRosterProxyInfo( RosterPutRequest request );

  public RosterProxyView resideRosterProxyInfo();

}
