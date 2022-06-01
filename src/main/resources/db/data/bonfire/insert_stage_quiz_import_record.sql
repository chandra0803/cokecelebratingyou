alter session set nls_date_format='DD-MON-RRRR HH24:MI:SS'
/
---
--- STAGE_QUIZ_IMPORT_RECORD data
---

--- import records for import file 800 -- Header record (quiz)
INSERT INTO stage_quiz_import_record ( 
	import_record_id, import_file_id, action_type, record_type,
	quiz_name, quiz_description, quiz_number_asked, quiz_passing_score, quiz_type, 
	question_status_type, question_is_required, question_text, 
	answer_is_correct, answer_choice_text, answer_explanation_text,
	created_by, date_created )
VALUES (1000, 800, 'add', 'H',
		'The Test Quiz', 'This quiz tests your knowledge of flowers and arts.', 3, 2, 'random', 
		null, null, null, 
		null, null, null,
		-1, sysdate)
/
--- import records for import file 800 -- Question record (quiz question 1)
INSERT INTO stage_quiz_import_record ( 
	import_record_id, import_file_id, action_type, record_type,
	quiz_name, quiz_description, quiz_number_asked, quiz_passing_score, quiz_type, 
	question_status_type, question_is_required, question_text, 
	answer_is_correct, answer_choice_text, answer_explanation_text,
	created_by, date_created )
VALUES (1001, 800, 'add', 'Q',
		null, null, null, null, null, 
		'active', 0, 'What is the name of Minnesota''s state flower?', 
		null, null, null,
		-1, sysdate)
/
--- import records for import file 800 -- Answer record (quiz question 1 answer 1)
INSERT INTO stage_quiz_import_record ( 
	import_record_id, import_file_id, action_type, record_type,
	quiz_name, quiz_description, quiz_number_asked, quiz_passing_score, quiz_type, 
	question_status_type, question_is_required, question_text, 
	answer_is_correct, answer_choice_text, answer_explanation_text,
	created_by, date_created )
VALUES (1002, 800, 'add', 'A',
		null, null, null, null, null, 
		null, null, null, 
		1, 'Lady''s slipper', 'The pink and white lady''s slipper (Cypripedium reginae), also known as the showy lady''s slipper or queen''s lady slipper, was adopted as the state flower in 1902. Found living in open fens, bogs, swamps, and damp woods where there is plenty of light, lady''s slippers grow slowly, taking up to 16 years to produce their first flowers. They bloom in late June or early July. The plants live for up to 50 years and grow four feet tall. A century ago, the showy lady''s slipper was a favorite adornment in rural church altars during the summer. Since 1925 this rare wildflower has been protected by state law (it is illegal to pick the flowers or to uproot or unearth the plants).',
		-1, sysdate)
/
--- import records for import file 800 -- Answer record (quiz question 1 answer 2)
INSERT INTO stage_quiz_import_record ( 
	import_record_id, import_file_id, action_type, record_type,
	quiz_name, quiz_description, quiz_number_asked, quiz_passing_score, quiz_type, 
	question_status_type, question_is_required, question_text, 
	answer_is_correct, answer_choice_text, answer_explanation_text,
	created_by, date_created )
VALUES (1003, 800, 'add', 'A', 
		null, null, null, null, null, 
		null, null, null, 
		0, 'Black-eyed Susan', null,
		-1, sysdate)
/
--- import records for import file 800 -- Answer record (quiz question 1 answer 3)
INSERT INTO stage_quiz_import_record ( 
	import_record_id, import_file_id, action_type, record_type,
	quiz_name, quiz_description, quiz_number_asked, quiz_passing_score, quiz_type, 
	question_status_type, question_is_required, question_text, 
	answer_is_correct, answer_choice_text, answer_explanation_text,
	created_by, date_created )
VALUES (1004, 800, 'add', 'A', 
		null, null, null, null, null, 
		null, null, null, 
		0, 'Balloon Flower', null,
		-1, sysdate)
/
--- import records for import file 800 -- Question record (quiz question 2)
INSERT INTO stage_quiz_import_record ( 
	import_record_id, import_file_id, action_type, record_type,
	quiz_name, quiz_description, quiz_number_asked, quiz_passing_score, quiz_type, 
	question_status_type, question_is_required, question_text, 
	answer_is_correct, answer_choice_text, answer_explanation_text,
	created_by, date_created )
VALUES (1005, 800, 'add', 'Q',
		null, null, null, null, null, 
		'active', 0, 'Who is the ''father'' of Surrealism?', 
		null, null, null,
		-1, sysdate)
/
--- import records for import file 800 -- Answer record (quiz question 2 answer 1)
INSERT INTO stage_quiz_import_record ( 
	import_record_id, import_file_id, action_type, record_type,
	quiz_name, quiz_description, quiz_number_asked, quiz_passing_score, quiz_type, 
	question_status_type, question_is_required, question_text, 
	answer_is_correct, answer_choice_text, answer_explanation_text,
	created_by, date_created )
VALUES (1006, 800, 'add', 'A',
		null, null, null, null, null, 
		null, null, null, 
		0, 'Pablo Picasso', null,
		-1, sysdate)
/
--- import records for import file 800 -- Answer record (quiz question 2 answer 2)
INSERT INTO stage_quiz_import_record ( 
	import_record_id, import_file_id, action_type, record_type,
	quiz_name, quiz_description, quiz_number_asked, quiz_passing_score, quiz_type, 
	question_status_type, question_is_required, question_text, 
	answer_is_correct, answer_choice_text, answer_explanation_text,
	created_by, date_created )
VALUES (1007, 800, 'add', 'A', 
		null, null, null, null, null, 
		null, null, null, 
		1, 'Salvador Dali', null,
		-1, sysdate)
/
--- import records for import file 800 -- Answer record (quiz question 2 answer 3)
INSERT INTO stage_quiz_import_record ( 
	import_record_id, import_file_id, action_type, record_type,
	quiz_name, quiz_description, quiz_number_asked, quiz_passing_score, quiz_type, 
	question_status_type, question_is_required, question_text, 
	answer_is_correct, answer_choice_text, answer_explanation_text,
	created_by, date_created )
VALUES (1008, 800, 'add', 'A', 
		null, null, null, null, null, 
		null, null, null, 
		0, 'Jackson Pollack', null,
		-1, sysdate)
/
--- import records for import file 800 -- Question record (quiz question 3)
INSERT INTO stage_quiz_import_record ( 
	import_record_id, import_file_id, action_type, record_type,
	quiz_name, quiz_description, quiz_number_asked, quiz_passing_score, quiz_type, 
	question_status_type, question_is_required, question_text, 
	answer_is_correct, answer_choice_text, answer_explanation_text,
	created_by, date_created )
VALUES (1009, 800, 'add', 'Q',
		null, null, null, null, null, 
		'active', 0, 'Who is John-Pierre Jeunet?', 
		null, null, null,
		-1, sysdate)
/
--- import records for import file 800 -- Answer record (quiz question 3 answer 1)
INSERT INTO stage_quiz_import_record ( 
	import_record_id, import_file_id, action_type, record_type,
	quiz_name, quiz_description, quiz_number_asked, quiz_passing_score, quiz_type, 
	question_status_type, question_is_required, question_text, 
	answer_is_correct, answer_choice_text, answer_explanation_text,
	created_by, date_created )
VALUES (1010, 800, 'add', 'A',
		null, null, null, null, null, 
		null, null, null, 
		0, 'He is a famous painter in the 18th century in France.', null,
		-1, sysdate)
/
--- import records for import file 800 -- Answer record (quiz question 3 answer 3)
INSERT INTO stage_quiz_import_record ( 
	import_record_id, import_file_id, action_type, record_type,
	quiz_name, quiz_description, quiz_number_asked, quiz_passing_score, quiz_type, 
	question_status_type, question_is_required, question_text, 
	answer_is_correct, answer_choice_text, answer_explanation_text,
	created_by, date_created )
VALUES (1011, 800, 'add', 'A', 
		null, null, null, null, null, 
		null, null, null, 
		1, 
		null, -- should error out, answer_choice_text is required
		null,
		-1, sysdate)
/
--- import records for import file 800 -- Answer record (quiz question 3 answer 2)
INSERT INTO stage_quiz_import_record ( 
	import_record_id, import_file_id, action_type, record_type,
	quiz_name, quiz_description, quiz_number_asked, quiz_passing_score, quiz_type, 
	question_status_type, question_is_required, question_text, 
	answer_is_correct, answer_choice_text, answer_explanation_text,
	created_by, date_created )
VALUES (1012, 800, 'add', 'A', 
		null, null, null, null, null, 
		null, null, null, 
		null, --- should error out, answer_is_correct is required
		'He is the chief chef in Paris''s famous hotel', null,
		-1, sysdate)
/
---
--- IMPORT_RECORD_ERROR data
---
INSERT INTO import_record_error (import_record_error_id, import_file_id, import_record_id,
                                 item_key, param1, param2, param3, param4, created_by,
                                 date_created)
VALUES (300, 800, 1011, 'admin.fileload.errors.MISSING_PROPERTY', 'answer_choice_text', NULL, NULL, NULL,
        0, SYSDATE)
/
INSERT INTO import_record_error (import_record_error_id, import_file_id, import_record_id,
                                 item_key, param1, param2, param3, param4, created_by,
                                 date_created)
VALUES (310, 800, 1012, 'admin.fileload.errors.MISSING_PROPERTY', 'answer_is_correct', NULL, NULL, NULL,
        0, SYSDATE)
/
---
---
--- import records for import file 820 -- Header record (quiz)
INSERT INTO stage_quiz_import_record ( 
	import_record_id, import_file_id, action_type, record_type,
	quiz_name, quiz_description, quiz_number_asked, quiz_passing_score, quiz_type, 
	question_status_type, question_is_required, question_text, 
	answer_is_correct, answer_choice_text, answer_explanation_text,
	created_by, date_created )
VALUES (2000, 820, 'add', 'H',
		'Common Sense Quiz (long)', 'See if you can get these challenging common sense questions.', 22, 11, 'fixed', 
		null, null, null, 
		null, null, null,
		-1, sysdate)
/
--- import records for import file 820 -- Question record (quiz question 1)
INSERT INTO stage_quiz_import_record ( 
	import_record_id, import_file_id, action_type, record_type,
	quiz_name, quiz_description, quiz_number_asked, quiz_passing_score, quiz_type, 
	question_status_type, question_is_required, question_text, 
	answer_is_correct, answer_choice_text, answer_explanation_text,
	created_by, date_created )
VALUES (2001, 820, 'add', 'Q',
		null, null, null, null, null, 
		'active', 0, 'Do they have a 4th of July in England?', 
		null, null, null,
		-1, sysdate)
/
--- import records for import file 820 -- Answer record (quiz question 1 answer 1)
INSERT INTO stage_quiz_import_record ( 
	import_record_id, import_file_id, action_type, record_type,
	quiz_name, quiz_description, quiz_number_asked, quiz_passing_score, quiz_type, 
	question_status_type, question_is_required, question_text, 
	answer_is_correct, answer_choice_text, answer_explanation_text,
	created_by, date_created )
VALUES (2002, 820, 'add', 'A',
		null, null, null, null, null, 
		null, null, null, 
		1, 'Yes', 'Everyone has a July 4th.',
		-1, sysdate)
/
--- import records for import file 820 -- Answer record (quiz question 1 answer 2)
INSERT INTO stage_quiz_import_record ( 
	import_record_id, import_file_id, action_type, record_type,
	quiz_name, quiz_description, quiz_number_asked, quiz_passing_score, quiz_type, 
	question_status_type, question_is_required, question_text, 
	answer_is_correct, answer_choice_text, answer_explanation_text,
	created_by, date_created )
VALUES (2003, 820, 'add', 'A',
		null, null, null, null, null, 
		null, null, null, 
		0, 'No', null,
		-1, sysdate)
/
--- import records for import file 820 -- Question record (quiz question 2)
INSERT INTO stage_quiz_import_record ( 
	import_record_id, import_file_id, action_type, record_type,
	quiz_name, quiz_description, quiz_number_asked, quiz_passing_score, quiz_type, 
	question_status_type, question_is_required, question_text, 
	answer_is_correct, answer_choice_text, answer_explanation_text,
	created_by, date_created )
VALUES (2004, 820, 'add', 'Q',
		null, null, null, null, null, 
		'active', 0, 'How many birthdays does the average man have?', 
		null, null, null,
		-1, sysdate)
/
--- import records for import file 820 -- Answer record (quiz question 2 answer 1)
INSERT INTO stage_quiz_import_record ( 
	import_record_id, import_file_id, action_type, record_type,
	quiz_name, quiz_description, quiz_number_asked, quiz_passing_score, quiz_type, 
	question_status_type, question_is_required, question_text, 
	answer_is_correct, answer_choice_text, answer_explanation_text,
	created_by, date_created )
VALUES (2005, 820, 'add', 'A',
		null, null, null, null, null, 
		null, null, null, 
		0, 'As many as he wants', null,
		-1, sysdate)
/
--- import records for import file 820 -- Answer record (quiz question 2 answer 2)
INSERT INTO stage_quiz_import_record ( 
	import_record_id, import_file_id, action_type, record_type,
	quiz_name, quiz_description, quiz_number_asked, quiz_passing_score, quiz_type, 
	question_status_type, question_is_required, question_text, 
	answer_is_correct, answer_choice_text, answer_explanation_text,
	created_by, date_created )
VALUES (2006, 820, 'add', 'A',
		null, null, null, null, null, 
		null, null, null, 
		1, 'One', 'You only get to be born once.',
		-1, sysdate)
/
--- import records for import file 820 -- Question record (quiz question 3)
INSERT INTO stage_quiz_import_record ( 
	import_record_id, import_file_id, action_type, record_type,
	quiz_name, quiz_description, quiz_number_asked, quiz_passing_score, quiz_type, 
	question_status_type, question_is_required, question_text, 
	answer_is_correct, answer_choice_text, answer_explanation_text,
	created_by, date_created )
VALUES (2007, 820, 'add', 'Q',
		null, null, null, null, null, 
		'active', 0, 'Some months have 31 days; how many have 28?', 
		null, null, null,
		-1, sysdate)
/
--- import records for import file 820 -- Answer record (quiz question 3 answer 1)
INSERT INTO stage_quiz_import_record ( 
	import_record_id, import_file_id, action_type, record_type,
	quiz_name, quiz_description, quiz_number_asked, quiz_passing_score, quiz_type, 
	question_status_type, question_is_required, question_text, 
	answer_is_correct, answer_choice_text, answer_explanation_text,
	created_by, date_created )
VALUES (2008, 820, 'add', 'A',
		null, null, null, null, null, 
		null, null, null, 
		0, 'One', null,
		-1, sysdate)
/
--- import records for import file 820 -- Answer record (quiz question 3 answer 2)
INSERT INTO stage_quiz_import_record ( 
	import_record_id, import_file_id, action_type, record_type,
	quiz_name, quiz_description, quiz_number_asked, quiz_passing_score, quiz_type, 
	question_status_type, question_is_required, question_text, 
	answer_is_correct, answer_choice_text, answer_explanation_text,
	created_by, date_created )
VALUES (2009, 820, 'add', 'A',
		null, null, null, null, null, 
		null, null, null, 
		1, 'All of them(12)', null,
		-1, sysdate)
/
--- import records for import file 820 -- Question record (quiz question 4)
INSERT INTO stage_quiz_import_record ( 
	import_record_id, import_file_id, action_type, record_type,
	quiz_name, quiz_description, quiz_number_asked, quiz_passing_score, quiz_type, 
	question_status_type, question_is_required, question_text, 
	answer_is_correct, answer_choice_text, answer_explanation_text,
	created_by, date_created )
VALUES (2010, 820, 'add', 'Q',
		null, null, null, null, null, 
		'active', 0, 'A woman gives a beggar 1 dollar and 32 cents; the woman is the beggar''s sister, but the beggar is not the woman''s brother. How come?', 
		null, null, null,
		-1, sysdate)
/
--- import records for import file 820 -- Answer record (quiz question 4 answer 1)
INSERT INTO stage_quiz_import_record ( 
	import_record_id, import_file_id, action_type, record_type,
	quiz_name, quiz_description, quiz_number_asked, quiz_passing_score, quiz_type, 
	question_status_type, question_is_required, question_text, 
	answer_is_correct, answer_choice_text, answer_explanation_text,
	created_by, date_created )
VALUES (2011, 820, 'add', 'A',
		null, null, null, null, null, 
		null, null, null, 
		0, 'Don''t know', null,
		-1, sysdate)
/
--- import records for import file 820 -- Answer record (quiz question 4 answer 2)
INSERT INTO stage_quiz_import_record ( 
	import_record_id, import_file_id, action_type, record_type,
	quiz_name, quiz_description, quiz_number_asked, quiz_passing_score, quiz_type, 
	question_status_type, question_is_required, question_text, 
	answer_is_correct, answer_choice_text, answer_explanation_text,
	created_by, date_created )
VALUES (2012, 820, 'add', 'A',
		null, null, null, null, null, 
		null, null, null, 
		1, 'The beggar is her sister. ', 'No all beggers are men.',
		-1, sysdate)
/
--- import records for import file 820 -- Question record (quiz question 5)
INSERT INTO stage_quiz_import_record ( 
	import_record_id, import_file_id, action_type, record_type,
	quiz_name, quiz_description, quiz_number_asked, quiz_passing_score, quiz_type, 
	question_status_type, question_is_required, question_text, 
	answer_is_correct, answer_choice_text, answer_explanation_text,
	created_by, date_created )
VALUES (2013, 820, 'add', 'Q',
		null, null, null, null, null, 
		'active', 0, 'Why can''t a man living in the U.S. be buried in Canada?', 
		null, null, null,
		-1, sysdate)
/
--- import records for import file 820 -- Answer record (quiz question 5 answer 1)
INSERT INTO stage_quiz_import_record ( 
	import_record_id, import_file_id, action_type, record_type,
	quiz_name, quiz_description, quiz_number_asked, quiz_passing_score, quiz_type, 
	question_status_type, question_is_required, question_text, 
	answer_is_correct, answer_choice_text, answer_explanation_text,
	created_by, date_created )
VALUES (2014, 820, 'add', 'A',
		null, null, null, null, null, 
		null, null, null, 
		0, 'Because he is not a Canadian citizen', null,
		-1, sysdate)
/
--- import records for import file 820 -- Answer record (quiz question 5 answer 2)
INSERT INTO stage_quiz_import_record ( 
	import_record_id, import_file_id, action_type, record_type,
	quiz_name, quiz_description, quiz_number_asked, quiz_passing_score, quiz_type, 
	question_status_type, question_is_required, question_text, 
	answer_is_correct, answer_choice_text, answer_explanation_text,
	created_by, date_created )
VALUES (2015, 820, 'add', 'A',
		null, null, null, null, null, 
		null, null, null, 
		1, 'He can''t be buried if he isn''t dead.', 'Duh!',
		-1, sysdate)
/
--- import records for import file 820 -- Question record (quiz question 6)
INSERT INTO stage_quiz_import_record ( 
	import_record_id, import_file_id, action_type, record_type,
	quiz_name, quiz_description, quiz_number_asked, quiz_passing_score, quiz_type, 
	question_status_type, question_is_required, question_text, 
	answer_is_correct, answer_choice_text, answer_explanation_text,
	created_by, date_created )
VALUES (2016, 820, 'add', 'Q',
		null, null, null, null, null, 
		'active', 0, 'How many outs are there in an inning?', 
		null, null, null,
		-1, sysdate)
/
--- import records for import file 820 -- Answer record (quiz question 6 answer 1)
INSERT INTO stage_quiz_import_record ( 
	import_record_id, import_file_id, action_type, record_type,
	quiz_name, quiz_description, quiz_number_asked, quiz_passing_score, quiz_type, 
	question_status_type, question_is_required, question_text, 
	answer_is_correct, answer_choice_text, answer_explanation_text,
	created_by, date_created )
VALUES (2017, 820, 'add', 'A',
		null, null, null, null, null, 
		null, null, null, 
		1, '6', null,
		-1, sysdate)
/
--- import records for import file 820 -- Answer record (quiz question 6 answer 2)
INSERT INTO stage_quiz_import_record ( 
	import_record_id, import_file_id, action_type, record_type,
	quiz_name, quiz_description, quiz_number_asked, quiz_passing_score, quiz_type, 
	question_status_type, question_is_required, question_text, 
	answer_is_correct, answer_choice_text, answer_explanation_text,
	created_by, date_created )
VALUES (2018, 820, 'add', 'A',
		null, null, null, null, null, 
		null, null, null, 
		0, '7', null,
		-1, sysdate)
/
--- import records for import file 820 -- Question record (quiz question 7)
INSERT INTO stage_quiz_import_record ( 
	import_record_id, import_file_id, action_type, record_type,
	quiz_name, quiz_description, quiz_number_asked, quiz_passing_score, quiz_type, 
	question_status_type, question_is_required, question_text, 
	answer_is_correct, answer_choice_text, answer_explanation_text,
	created_by, date_created )
VALUES (2019, 820, 'add', 'Q',
		null, null, null, null, null, 
		'active', 0, 'Is it legal for a man in California to marry his widow''s sister? Why?', 
		null, null, null,
		-1, sysdate)
/
--- import records for import file 820 -- Answer record (quiz question 7 answer 1)
INSERT INTO stage_quiz_import_record ( 
	import_record_id, import_file_id, action_type, record_type,
	quiz_name, quiz_description, quiz_number_asked, quiz_passing_score, quiz_type, 
	question_status_type, question_is_required, question_text, 
	answer_is_correct, answer_choice_text, answer_explanation_text,
	created_by, date_created )
VALUES (2020, 820, 'add', 'A',
		null, null, null, null, null, 
		null, null, null, 
		1, 'No', 'He''s dead.',
		-1, sysdate)
/
--- import records for import file 820 -- Answer record (quiz question 7 answer 2)
INSERT INTO stage_quiz_import_record ( 
	import_record_id, import_file_id, action_type, record_type,
	quiz_name, quiz_description, quiz_number_asked, quiz_passing_score, quiz_type, 
	question_status_type, question_is_required, question_text, 
	answer_is_correct, answer_choice_text, answer_explanation_text,
	created_by, date_created )
VALUES (2021, 820, 'add', 'A',
		null, null, null, null, null, 
		null, null, null, 
		0, 'Yes', null,
		-1, sysdate)
/
--- import records for import file 820 -- Question record (quiz question 8)
INSERT INTO stage_quiz_import_record ( 
	import_record_id, import_file_id, action_type, record_type,
	quiz_name, quiz_description, quiz_number_asked, quiz_passing_score, quiz_type, 
	question_status_type, question_is_required, question_text, 
	answer_is_correct, answer_choice_text, answer_explanation_text,
	created_by, date_created )
VALUES (2022, 820, 'add', 'Q',
		null, null, null, null, null, 
		'active', 0, 'Two men play five games of checkers. Each man wins the same number of games. There are no ties. Explain this.', 
		null, null, null,
		-1, sysdate)
/
--- import records for import file 820 -- Answer record (quiz question 8 answer 1)
INSERT INTO stage_quiz_import_record ( 
	import_record_id, import_file_id, action_type, record_type,
	quiz_name, quiz_description, quiz_number_asked, quiz_passing_score, quiz_type, 
	question_status_type, question_is_required, question_text, 
	answer_is_correct, answer_choice_text, answer_explanation_text,
	created_by, date_created )
VALUES (2023, 820, 'add', 'A',
		null, null, null, null, null, 
		null, null, null, 
		1, 'They were playing different people. ', null,
		-1, sysdate)
/
--- import records for import file 820 -- Answer record (quiz question 8 answer 2)
INSERT INTO stage_quiz_import_record ( 
	import_record_id, import_file_id, action_type, record_type,
	quiz_name, quiz_description, quiz_number_asked, quiz_passing_score, quiz_type, 
	question_status_type, question_is_required, question_text, 
	answer_is_correct, answer_choice_text, answer_explanation_text,
	created_by, date_created )
VALUES (2024, 820, 'add', 'A',
		null, null, null, null, null, 
		null, null, null, 
		0, 'One man cheated.', null,
		-1, sysdate)
/
--- import records for import file 820 -- Question record (quiz question 9)
INSERT INTO stage_quiz_import_record ( 
	import_record_id, import_file_id, action_type, record_type,
	quiz_name, quiz_description, quiz_number_asked, quiz_passing_score, quiz_type, 
	question_status_type, question_is_required, question_text, 
	answer_is_correct, answer_choice_text, answer_explanation_text,
	created_by, date_created )
VALUES (2025, 820, 'add', 'Q',
		null, null, null, null, null, 
		'active', 0, 'Divide 30 by 1/2 and add 10. What is the answer?', 
		null, null, null,
		-1, sysdate)
/
--- import records for import file 820 -- Answer record (quiz question 9 answer 1)
INSERT INTO stage_quiz_import_record ( 
	import_record_id, import_file_id, action_type, record_type,
	quiz_name, quiz_description, quiz_number_asked, quiz_passing_score, quiz_type, 
	question_status_type, question_is_required, question_text, 
	answer_is_correct, answer_choice_text, answer_explanation_text,
	created_by, date_created )
VALUES (2026, 820, 'add', 'A',
		null, null, null, null, null, 
		null, null, null, 
		1, '70', null,
		-1, sysdate)
/
--- import records for import file 820 -- Answer record (quiz question 9 answer 2)
INSERT INTO stage_quiz_import_record ( 
	import_record_id, import_file_id, action_type, record_type,
	quiz_name, quiz_description, quiz_number_asked, quiz_passing_score, quiz_type, 
	question_status_type, question_is_required, question_text, 
	answer_is_correct, answer_choice_text, answer_explanation_text,
	created_by, date_created )
VALUES (2027, 820, 'add', 'A',
		null, null, null, null, null, 
		null, null, null, 
		0, '12', null,
		-1, sysdate)
/
--- import records for import file 820 -- Question record (quiz question 10)
INSERT INTO stage_quiz_import_record ( 
	import_record_id, import_file_id, action_type, record_type,
	quiz_name, quiz_description, quiz_number_asked, quiz_passing_score, quiz_type, 
	question_status_type, question_is_required, question_text, 
	answer_is_correct, answer_choice_text, answer_explanation_text,
	created_by, date_created )
VALUES (2028, 820, 'add', 'Q',
		null, null, null, null, null, 
		'active', 0, 'A man builds a house rectangular in shape. All sides have southern exposure. A big bear walks by. What color is the bear? Why?', 
		null, null, null,
		-1, sysdate)
/
--- import records for import file 820 -- Answer record (quiz question 10 answer 1)
INSERT INTO stage_quiz_import_record ( 
	import_record_id, import_file_id, action_type, record_type,
	quiz_name, quiz_description, quiz_number_asked, quiz_passing_score, quiz_type, 
	question_status_type, question_is_required, question_text, 
	answer_is_correct, answer_choice_text, answer_explanation_text,
	created_by, date_created )
VALUES (2029, 820, 'add', 'A',
		null, null, null, null, null, 
		null, null, null, 
		0, 'brown', null,
		-1, sysdate)
/
--- import records for import file 820 -- Answer record (quiz question 10 answer 2)
INSERT INTO stage_quiz_import_record ( 
	import_record_id, import_file_id, action_type, record_type,
	quiz_name, quiz_description, quiz_number_asked, quiz_passing_score, quiz_type, 
	question_status_type, question_is_required, question_text, 
	answer_is_correct, answer_choice_text, answer_explanation_text,
	created_by, date_created )
VALUES (2030, 820, 'add', 'A',
		null, null, null, null, null, 
		null, null, null, 
		1, 'white', 'It is at the North Pole.',
		-1, sysdate)
/
--- import records for import file 820 -- Question record (quiz question 11)
INSERT INTO stage_quiz_import_record ( 
	import_record_id, import_file_id, action_type, record_type,
	quiz_name, quiz_description, quiz_number_asked, quiz_passing_score, quiz_type, 
	question_status_type, question_is_required, question_text, 
	answer_is_correct, answer_choice_text, answer_explanation_text,
	created_by, date_created )
VALUES (2031, 820, 'add', 'Q',
		null, null, null, null, null, 
		'active', 0, 'If there are 3 apples and you take away two, how many do you have?', 
		null, null, null,
		-1, sysdate)
/
--- import records for import file 820 -- Answer record (quiz question 11 answer 1)
INSERT INTO stage_quiz_import_record ( 
	import_record_id, import_file_id, action_type, record_type,
	quiz_name, quiz_description, quiz_number_asked, quiz_passing_score, quiz_type, 
	question_status_type, question_is_required, question_text, 
	answer_is_correct, answer_choice_text, answer_explanation_text,
	created_by, date_created )
VALUES (2032, 820, 'add', 'A',
		null, null, null, null, null, 
		null, null, null, 
		0, '1', null,
		-1, sysdate)
/
--- import records for import file 820 -- Answer record (quiz question 11 answer 2)
INSERT INTO stage_quiz_import_record ( 
	import_record_id, import_file_id, action_type, record_type,
	quiz_name, quiz_description, quiz_number_asked, quiz_passing_score, quiz_type, 
	question_status_type, question_is_required, question_text, 
	answer_is_correct, answer_choice_text, answer_explanation_text,
	created_by, date_created )
VALUES (2033, 820, 'add', 'A',
		null, null, null, null, null, 
		null, null, null, 
		1, '2', null,
		-1, sysdate)
/
--- import records for import file 820 -- Question record (quiz question 12)
INSERT INTO stage_quiz_import_record ( 
	import_record_id, import_file_id, action_type, record_type,
	quiz_name, quiz_description, quiz_number_asked, quiz_passing_score, quiz_type, 
	question_status_type, question_is_required, question_text, 
	answer_is_correct, answer_choice_text, answer_explanation_text,
	created_by, date_created )
VALUES (2034, 820, 'add', 'Q',
		null, null, null, null, null, 
		'active', 0, 'I have two U.S. coins totalling 55 cents. One is not a nickel. What are the coins?', 
		null, null, null,
		-1, sysdate)
/
--- import records for import file 820 -- Answer record (quiz question 12 answer 1)
INSERT INTO stage_quiz_import_record ( 
	import_record_id, import_file_id, action_type, record_type,
	quiz_name, quiz_description, quiz_number_asked, quiz_passing_score, quiz_type, 
	question_status_type, question_is_required, question_text, 
	answer_is_correct, answer_choice_text, answer_explanation_text,
	created_by, date_created )
VALUES (2035, 820, 'add', 'A',
		null, null, null, null, null, 
		null, null, null, 
		0, 'What a ridulous question. I refuse to answer.', null,
		-1, sysdate)
/
--- import records for import file 820 -- Answer record (quiz question 12 answer 2)
INSERT INTO stage_quiz_import_record ( 
	import_record_id, import_file_id, action_type, record_type,
	quiz_name, quiz_description, quiz_number_asked, quiz_passing_score, quiz_type, 
	question_status_type, question_is_required, question_text, 
	answer_is_correct, answer_choice_text, answer_explanation_text,
	created_by, date_created )
VALUES (2036, 820, 'add', 'A',
		null, null, null, null, null, 
		null, null, null, 
		1, 'A 50 cent piece and a nickel. Only one is not a nickel.', null,
		-1, sysdate)
/
--- import records for import file 820 -- Question record (quiz question 13)
INSERT INTO stage_quiz_import_record ( 
	import_record_id, import_file_id, action_type, record_type,
	quiz_name, quiz_description, quiz_number_asked, quiz_passing_score, quiz_type, 
	question_status_type, question_is_required, question_text, 
	answer_is_correct, answer_choice_text, answer_explanation_text,
	created_by, date_created )
VALUES (2037, 820, 'add', 'Q',
		null, null, null, null, null, 
		'active', 0, 'If you have only one match and you walked into a room where was an oil burner, a kerosene lamp, and a wood burning stove, which one would you light first? ', 
		null, null, null,
		-1, sysdate)
/
--- import records for import file 820 -- Answer record (quiz question 13 answer 1)
INSERT INTO stage_quiz_import_record ( 
	import_record_id, import_file_id, action_type, record_type,
	quiz_name, quiz_description, quiz_number_asked, quiz_passing_score, quiz_type, 
	question_status_type, question_is_required, question_text, 
	answer_is_correct, answer_choice_text, answer_explanation_text,
	created_by, date_created )
VALUES (2038, 820, 'add', 'A',
		null, null, null, null, null, 
		null, null, null, 
		0, 'A wood burning stove.', null,
		-1, sysdate)
/
--- import records for import file 820 -- Answer record (quiz question 13 answer 2)
INSERT INTO stage_quiz_import_record ( 
	import_record_id, import_file_id, action_type, record_type,
	quiz_name, quiz_description, quiz_number_asked, quiz_passing_score, quiz_type, 
	question_status_type, question_is_required, question_text, 
	answer_is_correct, answer_choice_text, answer_explanation_text,
	created_by, date_created )
VALUES (2039, 820, 'add', 'A',
		null, null, null, null, null, 
		null, null, null, 
		1, 'A match.', null,
		-1, sysdate)
/
--- import records for import file 820 -- Question record (quiz question 14)
INSERT INTO stage_quiz_import_record ( 
	import_record_id, import_file_id, action_type, record_type,
	quiz_name, quiz_description, quiz_number_asked, quiz_passing_score, quiz_type, 
	question_status_type, question_is_required, question_text, 
	answer_is_correct, answer_choice_text, answer_explanation_text,
	created_by, date_created )
VALUES (2040, 820, 'add', 'Q',
		null, null, null, null, null, 
		'active', 0, 'How far can a dog run into the woods?', 
		null, null, null,
		-1, sysdate)
/
--- import records for import file 820 -- Answer record (quiz question 14 answer 1)
INSERT INTO stage_quiz_import_record ( 
	import_record_id, import_file_id, action_type, record_type,
	quiz_name, quiz_description, quiz_number_asked, quiz_passing_score, quiz_type, 
	question_status_type, question_is_required, question_text, 
	answer_is_correct, answer_choice_text, answer_explanation_text,
	created_by, date_created )
VALUES (2041, 820, 'add', 'A',
		null, null, null, null, null, 
		null, null, null, 
		1, 'Halfway. Then it''s running out of the woods', null,
		-1, sysdate)
/
--- import records for import file 820 -- Answer record (quiz question 14 answer 2)
INSERT INTO stage_quiz_import_record ( 
	import_record_id, import_file_id, action_type, record_type,
	quiz_name, quiz_description, quiz_number_asked, quiz_passing_score, quiz_type, 
	question_status_type, question_is_required, question_text, 
	answer_is_correct, answer_choice_text, answer_explanation_text,
	created_by, date_created )
VALUES (2042, 820, 'add', 'A',
		null, null, null, null, null, 
		null, null, null, 
		0, 'However far it pleases.', null,
		-1, sysdate)
/
--- import records for import file 820 -- Question record (quiz question 15)
INSERT INTO stage_quiz_import_record ( 
	import_record_id, import_file_id, action_type, record_type,
	quiz_name, quiz_description, quiz_number_asked, quiz_passing_score, quiz_type, 
	question_status_type, question_is_required, question_text, 
	answer_is_correct, answer_choice_text, answer_explanation_text,
	created_by, date_created )
VALUES (2043, 820, 'add', 'Q',
		null, null, null, null, null, 
		'active', 0, 'A doctor gives you three pills telling you to take one every half hour. How long would the pills last?', 
		null, null, null,
		-1, sysdate)
/
--- import records for import file 820 -- Answer record (quiz question 15 answer 1)
INSERT INTO stage_quiz_import_record ( 
	import_record_id, import_file_id, action_type, record_type,
	quiz_name, quiz_description, quiz_number_asked, quiz_passing_score, quiz_type, 
	question_status_type, question_is_required, question_text, 
	answer_is_correct, answer_choice_text, answer_explanation_text,
	created_by, date_created )
VALUES (2044, 820, 'add', 'A',
		null, null, null, null, null, 
		null, null, null, 
		1, '1 hour.', null,
		-1, sysdate)
/
--- import records for import file 820 -- Answer record (quiz question 15 answer 2)
INSERT INTO stage_quiz_import_record ( 
	import_record_id, import_file_id, action_type, record_type,
	quiz_name, quiz_description, quiz_number_asked, quiz_passing_score, quiz_type, 
	question_status_type, question_is_required, question_text, 
	answer_is_correct, answer_choice_text, answer_explanation_text,
	created_by, date_created )
VALUES (2045, 820, 'add', 'A',
		null, null, null, null, null, 
		null, null, null, 
		0, '2 hours.', null,
		-1, sysdate)
/
--- import records for import file 820 -- Question record (quiz question 16)
INSERT INTO stage_quiz_import_record ( 
	import_record_id, import_file_id, action_type, record_type,
	quiz_name, quiz_description, quiz_number_asked, quiz_passing_score, quiz_type, 
	question_status_type, question_is_required, question_text, 
	answer_is_correct, answer_choice_text, answer_explanation_text,
	created_by, date_created )
VALUES (2046, 820, 'add', 'Q',
		null, null, null, null, null, 
		'active', 0, 'A farmer has 17 sheep; all but 9 die. How many are left?', 
		null, null, null,
		-1, sysdate)
/
--- import records for import file 820 -- Answer record (quiz question 16 answer 1)
INSERT INTO stage_quiz_import_record ( 
	import_record_id, import_file_id, action_type, record_type,
	quiz_name, quiz_description, quiz_number_asked, quiz_passing_score, quiz_type, 
	question_status_type, question_is_required, question_text, 
	answer_is_correct, answer_choice_text, answer_explanation_text,
	created_by, date_created )
VALUES (2047, 820, 'add', 'A',
		null, null, null, null, null, 
		null, null, null, 
		1, '9', null,
		-1, sysdate)
/
--- import records for import file 820 -- Answer record (quiz question 16 answer 2)
INSERT INTO stage_quiz_import_record ( 
	import_record_id, import_file_id, action_type, record_type,
	quiz_name, quiz_description, quiz_number_asked, quiz_passing_score, quiz_type, 
	question_status_type, question_is_required, question_text, 
	answer_is_correct, answer_choice_text, answer_explanation_text,
	created_by, date_created )
VALUES (2048, 820, 'add', 'A',
		null, null, null, null, null, 
		null, null, null, 
		0, '8', null,
		-1, sysdate)
/
--- import records for import file 820 -- Question record (quiz question 17)
INSERT INTO stage_quiz_import_record ( 
	import_record_id, import_file_id, action_type, record_type,
	quiz_name, quiz_description, quiz_number_asked, quiz_passing_score, quiz_type, 
	question_status_type, question_is_required, question_text, 
	answer_is_correct, answer_choice_text, answer_explanation_text,
	created_by, date_created )
VALUES (2049, 820, 'add', 'Q',
		null, null, null, null, null, 
		'active', 0, 'How many animals of each sex did Moses take on the ark?', 
		null, null, null,
		-1, sysdate)
/
--- import records for import file 820 -- Answer record (quiz question 17 answer 1)
INSERT INTO stage_quiz_import_record ( 
	import_record_id, import_file_id, action_type, record_type,
	quiz_name, quiz_description, quiz_number_asked, quiz_passing_score, quiz_type, 
	question_status_type, question_is_required, question_text, 
	answer_is_correct, answer_choice_text, answer_explanation_text,
	created_by, date_created )
VALUES (2050, 820, 'add', 'A',
		null, null, null, null, null, 
		null, null, null, 
		1, 'None.', 'Moses didn''t have an ark. Noah did.',
		-1, sysdate)
/
--- import records for import file 820 -- Answer record (quiz question 17 answer 2)
INSERT INTO stage_quiz_import_record ( 
	import_record_id, import_file_id, action_type, record_type,
	quiz_name, quiz_description, quiz_number_asked, quiz_passing_score, quiz_type, 
	question_status_type, question_is_required, question_text, 
	answer_is_correct, answer_choice_text, answer_explanation_text,
	created_by, date_created )
VALUES (2051, 820, 'add', 'A',
		null, null, null, null, null, 
		null, null, null, 
		0, '5', null,
		-1, sysdate)
/
--- import records for import file 820 -- Question record (quiz question 18)
INSERT INTO stage_quiz_import_record ( 
	import_record_id, import_file_id, action_type, record_type,
	quiz_name, quiz_description, quiz_number_asked, quiz_passing_score, quiz_type, 
	question_status_type, question_is_required, question_text, 
	answer_is_correct, answer_choice_text, answer_explanation_text,
	created_by, date_created )
VALUES (2052, 820, 'add', 'Q',
		null, null, null, null, null, 
		'active', 0, 'A clerk in the butcher shop is 5 feet 10 inches tall. What does he weigh?', 
		null, null, null,
		-1, sysdate)
/
--- import records for import file 820 -- Answer record (quiz question 18 answer 1)
INSERT INTO stage_quiz_import_record ( 
	import_record_id, import_file_id, action_type, record_type,
	quiz_name, quiz_description, quiz_number_asked, quiz_passing_score, quiz_type, 
	question_status_type, question_is_required, question_text, 
	answer_is_correct, answer_choice_text, answer_explanation_text,
	created_by, date_created )
VALUES (2053, 820, 'add', 'A',
		null, null, null, null, null, 
		null, null, null, 
		1, 'Meat.', 'He is a butcher after all.',
		-1, sysdate)
/
--- import records for import file 820 -- Answer record (quiz question 18 answer 2)
INSERT INTO stage_quiz_import_record ( 
	import_record_id, import_file_id, action_type, record_type,
	quiz_name, quiz_description, quiz_number_asked, quiz_passing_score, quiz_type, 
	question_status_type, question_is_required, question_text, 
	answer_is_correct, answer_choice_text, answer_explanation_text,
	created_by, date_created )
VALUES (2054, 820, 'add', 'A',
		null, null, null, null, null, 
		null, null, null, 
		0, '220 lbs.', null,
		-1, sysdate)
/
--- import records for import file 820 -- Question record (quiz question 19)
INSERT INTO stage_quiz_import_record ( 
	import_record_id, import_file_id, action_type, record_type,
	quiz_name, quiz_description, quiz_number_asked, quiz_passing_score, quiz_type, 
	question_status_type, question_is_required, question_text, 
	answer_is_correct, answer_choice_text, answer_explanation_text,
	created_by, date_created )
VALUES (2055, 820, 'add', 'Q',
		null, null, null, null, null, 
		'active', 0, 'How many two cent stamps are there in a dozen?', 
		null, null, null,
		-1, sysdate)
/
--- import records for import file 820 -- Answer record (quiz question 19 answer 1)
INSERT INTO stage_quiz_import_record ( 
	import_record_id, import_file_id, action_type, record_type,
	quiz_name, quiz_description, quiz_number_asked, quiz_passing_score, quiz_type, 
	question_status_type, question_is_required, question_text, 
	answer_is_correct, answer_choice_text, answer_explanation_text,
	created_by, date_created )
VALUES (2056, 820, 'add', 'A',
		null, null, null, null, null, 
		null, null, null, 
		1, '2', null,
		-1, sysdate)
/
--- import records for import file 820 -- Answer record (quiz question 19 answer 2)
INSERT INTO stage_quiz_import_record ( 
	import_record_id, import_file_id, action_type, record_type,
	quiz_name, quiz_description, quiz_number_asked, quiz_passing_score, quiz_type, 
	question_status_type, question_is_required, question_text, 
	answer_is_correct, answer_choice_text, answer_explanation_text,
	created_by, date_created )
VALUES (2057, 820, 'add', 'A',
		null, null, null, null, null, 
		null, null, null, 
		0, '6', null,
		-1, sysdate)
/
--- import records for import file 820 -- Question record (quiz question 20)
INSERT INTO stage_quiz_import_record ( 
	import_record_id, import_file_id, action_type, record_type,
	quiz_name, quiz_description, quiz_number_asked, quiz_passing_score, quiz_type, 
	question_status_type, question_is_required, question_text, 
	answer_is_correct, answer_choice_text, answer_explanation_text,
	created_by, date_created )
VALUES (2058, 820, 'add', 'Q',
		null, null, null, null, null, 
		'active', 0, 'What was the president''s name in 1950? ', 
		null, null, null,
		-1, sysdate)
/
--- import records for import file 820 -- Answer record (quiz question 20 answer 1)
INSERT INTO stage_quiz_import_record ( 
	import_record_id, import_file_id, action_type, record_type,
	quiz_name, quiz_description, quiz_number_asked, quiz_passing_score, quiz_type, 
	question_status_type, question_is_required, question_text, 
	answer_is_correct, answer_choice_text, answer_explanation_text,
	created_by, date_created )
VALUES (2059, 820, 'add', 'A',
		null, null, null, null, null, 
		null, null, null, 
		0, 'Bill Clinton', null,
		-1, sysdate)
/
--- import records for import file 820 -- Answer record (quiz question 20 answer 2)
INSERT INTO stage_quiz_import_record ( 
	import_record_id, import_file_id, action_type, record_type,
	quiz_name, quiz_description, quiz_number_asked, quiz_passing_score, quiz_type, 
	question_status_type, question_is_required, question_text, 
	answer_is_correct, answer_choice_text, answer_explanation_text,
	created_by, date_created )
VALUES (2060, 820, 'add', 'A',
		null, null, null, null, null, 
		null, null, null, 
		1, 'George W. Bush', 'Same as it is now.',
		-1, sysdate)
/
--- import records for import file 820 -- Question record (quiz question 21)
INSERT INTO stage_quiz_import_record ( 
	import_record_id, import_file_id, action_type, record_type,
	quiz_name, quiz_description, quiz_number_asked, quiz_passing_score, quiz_type, 
	question_status_type, question_is_required, question_text, 
	answer_is_correct, answer_choice_text, answer_explanation_text,
	created_by, date_created )
VALUES (2061, 820, 'add', 'Q',
		null, null, null, null, null, 
		'active', 0, 'There once was a lady who really liked pink. In her cozy, little one-story house, everything was pink. Even her dog was pink. Her hair, her carpet, everything. What color are her stairs?', 
		null, null, null,
		-1, sysdate)
/
--- import records for import file 820 -- Answer record (quiz question 21 answer 1)
INSERT INTO stage_quiz_import_record ( 
	import_record_id, import_file_id, action_type, record_type,
	quiz_name, quiz_description, quiz_number_asked, quiz_passing_score, quiz_type, 
	question_status_type, question_is_required, question_text, 
	answer_is_correct, answer_choice_text, answer_explanation_text,
	created_by, date_created )
VALUES (2062, 820, 'add', 'A',
		null, null, null, null, null, 
		null, null, null, 
		0, 'Pink!', null,
		-1, sysdate)
/
--- import records for import file 820 -- Answer record (quiz question 21 answer 2)
INSERT INTO stage_quiz_import_record ( 
	import_record_id, import_file_id, action_type, record_type,
	quiz_name, quiz_description, quiz_number_asked, quiz_passing_score, quiz_type, 
	question_status_type, question_is_required, question_text, 
	answer_is_correct, answer_choice_text, answer_explanation_text,
	created_by, date_created )
VALUES (2063, 820, 'add', 'A',
		null, null, null, null, null, 
		null, null, null, 
		1, 'She didn''t have stairs.', 'It was one story. ',
		-1, sysdate)
/
--- import records for import file 820 -- Question record (quiz question 22)
INSERT INTO stage_quiz_import_record ( 
	import_record_id, import_file_id, action_type, record_type,
	quiz_name, quiz_description, quiz_number_asked, quiz_passing_score, quiz_type, 
	question_status_type, question_is_required, question_text, 
	answer_is_correct, answer_choice_text, answer_explanation_text,
	created_by, date_created )
VALUES (2064, 820, 'add', 'Q',
		null, null, null, null, null, 
		'active', 0, 'There was an evergreen tree in the dark, ugly forest where ghosts, witches, and even Frankensteins lived. All of the sudden, a great gust of wind flew through the forest. Which way did the leaves on the tree fall? ', 
		null, null, null,
		-1, sysdate)
/
--- import records for import file 820 -- Answer record (quiz question 22 answer 1)
INSERT INTO stage_quiz_import_record ( 
	import_record_id, import_file_id, action_type, record_type,
	quiz_name, quiz_description, quiz_number_asked, quiz_passing_score, quiz_type, 
	question_status_type, question_is_required, question_text, 
	answer_is_correct, answer_choice_text, answer_explanation_text,
	created_by, date_created )
VALUES (2065, 820, 'add', 'A',
		null, null, null, null, null, 
		null, null, null, 
		1, 'None', 'It is a pine tree. It doesn''t have leaves.',
		-1, sysdate)
/
--- import records for import file 820 -- Answer record (quiz question 22 answer 2)
INSERT INTO stage_quiz_import_record ( 
	import_record_id, import_file_id, action_type, record_type,
	quiz_name, quiz_description, quiz_number_asked, quiz_passing_score, quiz_type, 
	question_status_type, question_is_required, question_text, 
	answer_is_correct, answer_choice_text, answer_explanation_text,
	created_by, date_created )
VALUES (2066, 820, 'add', 'A',
		null, null, null, null, null, 
		null, null, null, 
		0, 'The south side.',null,
		-1, sysdate)
/