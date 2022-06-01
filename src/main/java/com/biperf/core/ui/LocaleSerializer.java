/*
 * (c) 2019 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.ui;

import java.io.IOException;
import java.util.Locale;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

// en-US instead of en_US
// IETF BCP 47
public class LocaleSerializer extends ToStringSerializer
{

  private static final long serialVersionUID = 1L;

  @Override
  public boolean isEmpty( SerializerProvider prov, Object value )
  {
    return value == null || Locale.class.cast( value ).toLanguageTag().isEmpty();
  }

  @Override
  public void serialize( Object value, JsonGenerator gen, SerializerProvider provider ) throws IOException
  {
    gen.writeString( Locale.class.cast( value ).toLanguageTag() );
  }

}