function validateEmail(addr) 
{
	if (addr == '') 
	{
		return false;
	}
	// check for invalid characters
	var invalidChars = '\/\'\\ ";:?!()[]\{\}^|';
	for (i=0; i<invalidChars.length; i++) 
	{
		if (addr.indexOf(invalidChars.charAt(i),0) > -1) 
   		{
			return false;
   		}
	}	
	// check for nonascii characters
	for (i=0; i<addr.length; i++) 
	{
   		if (addr.charCodeAt(i)>127) 
   		{
			return false;
		}
	}
	var atPos = addr.indexOf('@',0);
	//address must contain an @
	if (atPos == -1) 
	{
		return false;
	}
	//must not start with @
	if (atPos == 0) 
	{
		return false;
	}
	//must contain only one @
	if (addr.indexOf('@', atPos + 1) > - 1) 
	{
   		return false;
	}
	//must contain a period in the domain name
	if (addr.indexOf('.', atPos) == -1) 
	{
		return false;
	}
	//period must not immediately follow @ 
	if (addr.indexOf('@.',0) != -1) 
	{
		return false;
	}
	//period must not immediately precede @
	if (addr.indexOf('.@',0) != -1)
	{
		return false;
	}
	//two periods must not be adjacent in email address
	if (addr.indexOf('..',0) != -1) 
	{
		return false;
	}
	// validate primary domain
	var suffix = addr.substring(addr.lastIndexOf('.')+1);
	if (suffix.length != 2 && suffix != 'com' && suffix != 'net' && suffix != 'org' && 
		suffix != 'edu' && suffix != 'int' && suffix != 'mil' && suffix != 'gov' 
		& suffix != 'arpa' && suffix != 'biz' && suffix != 'aero' && suffix != 'name' 
		&& suffix != 'coop' && suffix != 'info' && suffix != 'pro' && suffix != 'museum') 
	{
		return false;
	}
	return true;
}