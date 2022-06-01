CREATE TABLE pick_list_item
  ( pick_list_item_id              NUMBER(12) NOT NULL, 
    list_table                     VARCHAR2(30) NOT NULL,
    list_field                     VARCHAR2(30) NOT NULL,
    list_item_id                   VARCHAR2(30) NOT NULL,
    description                    VARCHAR2(250) NOT NULL,
    display_order                  NUMBER(4),
    status                         VARCHAR2(9),
    created_by                     NUMBER (18) NOT NULL,
    date_created                   DATE NOT NULL,
    modified_by                    NUMBER (18),
    date_modified                  DATE,
	VERSION  				NUMBER (18) NOT NULL )
/
ALTER TABLE pick_list_item
ADD CONSTRAINT ckc_pick_list_item_status CHECK (status is null or (status in ('A','I')))
/
ALTER TABLE pick_list_item
ADD CONSTRAINT pick_list_item_pk PRIMARY KEY (pick_list_item_id)
USING INDEX
/
ALTER TABLE pick_list_item
ADD CONSTRAINT pick_list_item_uk UNIQUE (list_table, list_field,list_item_id)
/
COMMENT ON TABLE pick_list_item IS 'The PICK_LIST_ITEM table is the lookup table for all of the picklist fields.  Each picklist field has one or more records in the PICK_LIST_ITEM table; one for each data value that is available.  A data value must reside in this table before it is available in the application.'
/
COMMENT ON COLUMN pick_list_item.created_by IS 'created_by'
/
COMMENT ON COLUMN pick_list_item.date_created IS 'date_created'
/
COMMENT ON COLUMN pick_list_item.date_modified IS 'date_modified'
/
COMMENT ON COLUMN pick_list_item.display_order IS ' The order in which the item will appear in a drop-down list.'
/
COMMENT ON COLUMN pick_list_item.list_field IS ' The field to which the corresponding record refers.  Database key used to relate picklist item entries to the picklist.'
/
COMMENT ON COLUMN pick_list_item.list_item_id IS ' Identification code of a specific item in the picklist.  This is normally a user defined abbreviation of the picklist value.'
/
COMMENT ON COLUMN pick_list_item.list_table IS ' The PRIME database table where the field resides.  Database key used to relate picklist item entries to the picklist.'
/
COMMENT ON COLUMN pick_list_item.modified_by IS 'modified_by'
/
COMMENT ON COLUMN pick_list_item.status IS ' The current status of the data value.'
/
CREATE SEQUENCE PICK_LIST_ITEM_PK_SQ INCREMENT BY 1   START WITH 5000
/
