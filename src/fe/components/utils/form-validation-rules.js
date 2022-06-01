import * as ErrorMessages from './form-validation-errors.js';

export const required = ( text ) => {
  if ( text ) {
    return null;
  } else {
    return ErrorMessages.isRequired;
  }
};

export const mustMatch = ( field, fieldName ) => {
  return ( text, state ) => {
    return state[ field ] === text ? null : ErrorMessages.mustMatch( fieldName );
  };
};

export const minLength = ( length ) => {
  return ( text ) => {
    return text.length >= length ? null : ErrorMessages.minLength( length );
  };
};


export const validEmail = ( text ) => {
    // regex from http://stackoverflow.com/questions/46155/validate-email-address-in-javascript
    const re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    return re.test( text ) ? null : ErrorMessages.validEmail( );

};
