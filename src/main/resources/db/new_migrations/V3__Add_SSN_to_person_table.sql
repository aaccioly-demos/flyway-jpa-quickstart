-- Copy this file to the main migration folder and run mvn flyway:migrate to add SSNs
alter table PERSON
  add column SSN varchar(15);

update PERSON
set SSN = (select LPAD(ID, 15, '0'))
where SSN is null;

alter table PERSON
  alter column SSN varchar(15) not null;

alter table PERSON
  add constraint UK_PERSON_SSN unique (SSN);
