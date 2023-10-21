insert into authority (id, name, active_yn) values (1, 'ROLE_ADMIN', 1);
insert into authority (id, name, active_yn) values (2, 'ROLE_MEMBER', 1);

insert into member (login_id, password, name, mobile, state) values ('abcde', '$2a$10$ZnjZRn513vzMHzMG2DK6Du7Jq6lvC3mejgYOnssvMCPa1b8LEGmVG', '홍길동', '01012345678', 'ACTIVATION');
insert into member (login_id, password, name, mobile, state) values ('admin', '$2a$10$ZnjZRn513vzMHzMG2DK6Du7Jq6lvC3mejgYOnssvMCPa1b8LEGmVG', '관리자', '01012345679', 'ACTIVATION');

insert into member_authority(member_id, authority_id) values (1, 2);
insert into member_authority(member_id, authority_id) values (2, 1);