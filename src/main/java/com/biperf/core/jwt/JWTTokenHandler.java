
package com.biperf.core.jwt;


public interface JWTTokenHandler
{

  String generateToken() throws JWTException;

  boolean validate( String token ) throws JWTException;

}
