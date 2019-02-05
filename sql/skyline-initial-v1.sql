-- drop table skyline_initial_queue

-- This queue table is going to be owned by Nucleus and is part of core DB unlike the result tables which are owned by datascope

create table skyline_initial_queue (
    id bigserial NOT NULL,
    user_id uuid NOT NULL,
    course_id uuid NOT NULL,
    class_id uuid,
    status int NOT NULL DEFAULT 0 CHECK (status::int = ANY (ARRAY[0::int, 1::int, 2::int])),
    category int NOT NULL DEFAULT 0 CHECK(category::int = ANY(ARRAY[0::int, 1::int])),
    payload text,
    created_at timestamp without time zone DEFAULT timezone('UTC'::text, now()) NOT NULL,
    updated_at timestamp without time zone DEFAULT timezone('UTC'::text, now()) NOT NULL,
    CONSTRAINT siq_pkey PRIMARY KEY (id),
    CONSTRAINT  siq_cat_pay_chk CHECK ((category = 0 AND payload is NOT NULL) OR (category = 1 AND payload is NULL))
);

ALTER TABLE skyline_initial_queue OWNER TO nucleus;

CREATE UNIQUE INDEX siq_ucc_unq_idx
    ON skyline_initial_queue (user_id, course_id, class_id)
    where class_id is not null;

CREATE UNIQUE INDEX siq_ucc_null_unq_idx
    ON skyline_initial_queue (user_id, course_id)
    where class_id is null;

COMMENT on TABLE skyline_initial_queue IS 'Persistent queue for initial baseline tasks';
COMMENT on COLUMN skyline_initial_queue.status IS '0 means queued, 1 means dispatched for processing, 2 means in process';
COMMENT on COLUMN skyline_initial_queue.category IS '0 diagnostic, 1 offline';
COMMENT on COLUMN skyline_initial_queue.payload IS 'JSON payload as received from log writer for diagnostic assessment';

-- Alter on class_member table

alter table class_member add column diag_asmt_assigned uuid;
alter table class_member add column diag_asmt_state int;
alter table class_member add column initial_lp_done boolean;

COMMENT on COLUMN class_member.diag_asmt_state IS '0 means not initialized, 1 means not needed, 2 means suggested, 3 means done, 4 means not available, 5 means class is offline';
ALTER TABLE class_member ADD CONSTRAINT cm_das_chk CHECK (diag_asmt_state = 0 OR diag_asmt_state = 1 OR diag_asmt_state = 2 OR diag_asmt_state = 3 OR diag_asmt_state = 4 OR diag_asmt_state = 5);

alter table grade_competency_bound add column average_tx_comp_code text;
alter table grade_competency_bound add constraint gcb_fk_dcm_sdca FOREIGN KEY (tx_subject_code, tx_domain_code, average_tx_comp_code)
  REFERENCES domain_competency_matrix(tx_subject_code, tx_domain_code, tx_comp_code);
