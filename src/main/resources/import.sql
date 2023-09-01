insert into authority (id, name, active_yn) values (1, 'ROLE_ADMIN', 1);
insert into authority (id, name, active_yn) values (2, 'ROLE_MEMBER', 1);
insert into member (id, login_id, password, name, mobile) values (1, 'nostime', '$2a$10$1wJNLwBy9LDmjgRbQ1CDg.F86b1Aj8WKsr.fvtXwVBpJv8hCoGuhm', '홍길동', '01012345678');
insert into member_authority(id, member_id, authority_id) values (1, 1, 2);