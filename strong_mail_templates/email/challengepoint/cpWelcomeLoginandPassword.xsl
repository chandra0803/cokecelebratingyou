<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
   <xsl:output method="xml"/>
   <xsl:template match="/">
   ##cp_header_css_image##
	<!-- start template -->
	
	
	Dear ##first_name## ##last_name##,

Welcome to Challengepoint!

Now is the time to challenge yourself with ##program_name## -- only you know what you can do!

In order to participate in ##program_name## you will need to visit the Challengepoint website, enter your unique Login ID and Password 
listed below and select your personal Challengepoint for this program.

Your Login ID is: ##user_nm##
Your Password is: ##password##

Please retain this information in a secure place for future use.

Click here to access the website

In order to participate in ##program_name## you must select your personal Challengepoint. If you do not select a Challengepoint 
you will not be eligible for any awards in this program. Selection of Challengepoints will begin ##registration_start_date## 
and you must submit your Challengepoint before ##registration_end_date##. The program contest period will begin on ##promotion_start_date## and 
will end on ##promotion_end_date##. All eligible activities must occur during these official program dates. 

Once at the challengepoint website you will be able to pick your personal Challengepoint from the three choices listed below.

	<xsl:if test="/StrongMail/BASE!= ''">
		Your base is:<strong> ##base##</strong>.
	</xsl:if>
	
Your challengepoint levels and available rewards are:

	<xsl:if test="/StrongMail/challengepoint1!= ''">			
	* Challengepoint Level 1 the performance required to achieve this Challengepoint is <strong>##challengepointLevel1##,</strong> which equals <strong>
	##challengepointValue1##</strong>
			<xsl:if test="/StrongMail/AWARD_PERQS!= ''">  
				and the reward is <strong>${challengepointReward1}</strong> Points.
			</xsl:if>
	<br/><br/>					
	</xsl:if>
						
	<xsl:if test="/StrongMail/challengepoint2!= ''">			
	* Challengepoint Level 2 the performance required to achieve this Challengepoint is <strong>##challengepointLevel2##,</strong> which equals <strong>
	##challengepointValue2##</strong>
			<xsl:if test="/StrongMail/AWARD_PERQS!= ''">  
				and the reward is <strong>${challengepointReward2}</strong> Points.
			</xsl:if>
	<br/><br/>					
	</xsl:if>
	
	
	<xsl:if test="/StrongMail/challengepoint3!= ''">			
	* Challengepoint Level 3 the performance required to achieve this Challengepoint is <strong>##challengepointLevel3##,</strong> which equals <strong>
	##challengepointValue3##</strong>
			<xsl:if test="/StrongMail/AWARD_PERQS!= ''">  
				and the reward is <strong>${challengepointReward3}</strong> Points.
			</xsl:if>
	<br/><br/>					
	</xsl:if>
	

The Challengepoint website features links to your program rules and progress updates.
	<xsl:if test="/StrongMail/AWARD_PERQS!= ''">  
		The website will also provide information about Points if you are not familiar with this popular award media.		
	</xsl:if> 
	
PLEASE READ the RULES at the website before you select your Challengepoint so that you make the best Challengepoint selection for YOU!

If you have any question or comments, please click here to contact the Program Administrator or email us at:
challengepoint@biworldwide.com

Best wishes and good luck!

Sincerely,
 
The ##program_name## Challengepoint Team!

 	<!-- end template -->	
   ##cp_footer1## 						
  </xsl:template>
</xsl:stylesheet>



