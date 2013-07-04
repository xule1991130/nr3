create table N3_ACC_DIFF_DETAIL
(
  batchno   VARCHAR2(100)  not null,
  difftype  VARCHAR2(2)  not null,
  leftdata  VARCHAR2(2000) ,
  rightdata VARCHAR2(2000),
  diff      VARCHAR2(1000),
  leftkey   VARCHAR2(100),
  rightkey  VARCHAR2(100)
  accountDay DATE not null,
);

create index IDEX_N3_ACC_DIFF_DETAIL_BATCHNO on N3_ACC_DIFF_DETAIL (batchno);

create table N3_ACC_DIFF
(
  batchno   VARCHAR2(100) not null,
  los       INTEGER not null,
  ors       INTEGER not null,
  lrs       INTEGER not null,
  bal       INTEGER not null,
  totalline INTEGER not null,
  starttime DATE not null,
  endtime   DATE not null,
  costtime  INTEGER not null
  accountDay DATE not null,
);

alter table N3_ACC_DIFF
  add constraint PK_N3_ACC_DIFF primary key (batchno);

