insert into authority (id, name, active_yn) values (1, 'ROLE_ADMIN', 1);
insert into authority (id, name, active_yn) values (2, 'ROLE_MEMBER', 1);

insert into member (login_id, password, name, mobile, is_enabled) values ('abcde', '$2a$10$ZnjZRn513vzMHzMG2DK6Du7Jq6lvC3mejgYOnssvMCPa1b8LEGmVG', '홍길동', '01012345678', 1);
insert into member_authority(member_id, authority_id) values (1, 2);