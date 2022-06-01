/*
 * (c) 2017 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biw.hc.core.service;

import org.springframework.http.ResponseEntity;

import com.biperf.core.exception.HoneycombException;
import com.biperf.core.value.hc.HCRequestDetails;

public interface HCRepository
{
  public <T, V> ResponseEntity<V> execute( HCRequestDetails hcRequestDetails, T data, Class<V> response ) throws HoneycombException;
}
