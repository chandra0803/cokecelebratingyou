
<!--
/*
  
  Based on code from:
  Author: Justin Whitford
  Source: www.evolt.org
  
  */
  
  /*
  filtery(pattern, list)
  pattern: a string of zero or more characters by which to filter the list
  list: reference to a form object of type, select
  */
  function filtery(pattern, list)
  {
	  if (!list.bak){
	    list.bak = new Array();
	    for (n=0;n<list.length;n++){
	      list.bak[list.bak.length] = new Array(list[n].value, list[n].text, list[n].selected);
	    }
	  }
	  
	  match = new Array();
	  nomatch = new Array();
	  for (n=0;n<list.bak.length;n++){
	    if(list.bak[n][0].toLowerCase().indexOf(pattern.toLowerCase())!=-1){
	      match[match.length] = new Array(list.bak[n][0], list.bak[n][1], list.bak[n][2]);
	    }else{
	      nomatch[nomatch.length] = new Array(list.bak[n][0], list.bak[n][1], list.bak[n][2]);
	    }
	  }
	  
	  list.options.length = match.length;
	  for (n=0;n<match.length;n++){
	    list[n].value = match[n][0];
	    list[n].text = match[n][1];
	    list[n].selected = match[n][2];
	  }
	  
	  if(pattern == "claim_") {
          list.selectedIndex= 0;
	  }            

  }
  
 // -->