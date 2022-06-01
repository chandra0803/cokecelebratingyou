ALTER TABLE import_file ADD ( 
	promo_badge_id  NUMBER(18,0),
	budget_segment_id 			NUMBER(18,0),
 	delay_award_date			DATE )
/
COMMENT ON COLUMN import_file.budget_segment_id IS 'Budget Segment Id is associated with the promotion in this import file.'
/