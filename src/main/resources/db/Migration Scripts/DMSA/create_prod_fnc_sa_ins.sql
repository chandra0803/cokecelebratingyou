CREATE OR REPLACE FUNCTION fnc_sa_ins() RETURNS VOID AS $$
BEGIN
  INSERT INTO persons select * from "DMSAPROD".stg_sa_persons;
  
  INSERT INTO programs
        SELECT nextval('programs_id_seq'), companyid, program_uuid, ,programstartdate, 
                programstate, awardtype, isbillprofileactive, billprofiles, giftcodeawardid,
				recipientemail,giftcodereminderemail,gcrememailsenddaysafter,giftcodeexpirationemail,
				gcexpemailsenddaysprior,contributionemail,createdat, updatedat,
				programsetup,gcrememailfrequency,gcrememailsendtime,gcrememailsendstartdate,gcrememailsendenddate,
				celebrationsettings,manageremail, '{}' branding,hierarchyid,signatureimageurl,emailheaderimageurl,
				programname,emaildisplayname,
				null milestonename, null messagelevellabel,
                '' awardintervaltype, awardintervalfield, awardintervalnumber, awardintervalpart, null taxable
           FROM "DMSAPROD".stg_sa_programs;
  
  INSERT INTO milestones 
        SELECT nextval('milestones_id_seq'), companyid, programid, milestoneid, milestonecode,
                milestonestate,createdat, updatedat, maxaward,webmessageimageurl, emailmessageimageurl,countrycode,
				milestoneaward,'' celebrationtype, '' contributiontype,ecard,
                null pointsfixed,null pointsmax,null pointsmin
	      FROM "DMSAPROD".stg_sa_milestones;
    
  INSERT INTO recipients
        SELECT nextval('recipients_id_seq'), companyid, recipientid, recipientstate, recipientstatus, programid, 
                milestonecode, awarddate, awardexpirationdate, giftcodeid, giftcodereference, 
				createdby, createdreference, createdat, updatedat, awarddatetimezone, celebrationid, 
                celebrationsettings, participantpersonid,celebrationyear, 
				emailsettings, awardeventsentat
		  FROM "DMSAPROD".stg_sa_recipients;
  
 INSERT INTO celebrationspersons 
     SELECT celebrationid,
			contributionid,
			personid,
			role,
			createdat,
			updatedat,
			invitationsentat,
			inviterpersonid,
			remindersentat,
			null thankyousentat
	   FROM "DMSAPROD".stg_sa_celebrationspersons;  
 
END
$$ LANGUAGE 'plpgsql';