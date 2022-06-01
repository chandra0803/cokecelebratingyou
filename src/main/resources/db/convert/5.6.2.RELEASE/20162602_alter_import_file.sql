alter table import_file add budget_master_id NUMBER(18,0)
/
COMMENT ON COLUMN import_file.budget_master_id IS 'Budget Master Id is associated with the import file.'
/