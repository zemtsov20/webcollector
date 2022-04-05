create user renat;
create database wbdata;
alter user renat with encrypted password 'renat';
grant all privileges on database wbdata to renat;