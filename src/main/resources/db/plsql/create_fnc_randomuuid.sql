CREATE OR REPLACE FUNCTION fnc_randomuuid
 /*******************************************************************************
   -- Purpose: Function used to generate random UUID using JAVA class
   --
   -- Person                          Date            Comments
   -- -----------                     --------         -----------------------------------------------------
   -- Gorantla                       11/28/2019   Initial creation                                                  
   *******************************************************************************/
   RETURN VARCHAR2
   AS LANGUAGE JAVA
   NAME 'RandomUUID.create() return java.lang.String';
/