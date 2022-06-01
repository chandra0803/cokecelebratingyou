/*
 * (c) 2019 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.ui;

import java.util.Locale;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.module.SimpleModule;

@Service
public class JacksonConfig extends SimpleModule
{
  private static final long serialVersionUID = 1L;

  public JacksonConfig()
  {
    this.addSerializer( Locale.class, new LocaleSerializer() );
  }

}
