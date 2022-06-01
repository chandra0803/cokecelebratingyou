CREATE OR REPLACE AND COMPILE
 /*******************************************************************************
   -- Purpose: JAVA class to generate random UUID
   --
   -- Person                         Date         Comments
   -- -----------                    --------     --------------------------------
   -- Gorantla                       11/28/2019   Initial creation                                                  
   *******************************************************************************/
   java source named "RandomUUID"
   as
   public class RandomUUID
   {
      public static String create()
      {
              return java.util.UUID.randomUUID().toString();
      }
   }
/