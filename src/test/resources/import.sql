insert into authority (id, name, active_yn) values (1, 'ROLE_ADMIN', 1);
insert into authority (id, name, active_yn) values (2, 'ROLE_MEMBER', 1);

insert into member (login_id, password, name, mobile, state) values ('abcde', '$2a$10$ZnjZRn513vzMHzMG2DK6Du7Jq6lvC3mejgYOnssvMCPa1b8LEGmVG', '홍길동', '01012345678', 'ACTIVATION');
insert into member (login_id, password, name, mobile, state) values ('admin', '$2a$10$ZnjZRn513vzMHzMG2DK6Du7Jq6lvC3mejgYOnssvMCPa1b8LEGmVG', '관리자', '01012345679', 'ACTIVATION');

insert into member_authority(member_id, authority_id) values (1, 2);
insert into member_authority(member_id, authority_id) values (2, 1);

insert into building(state, name, address1, address2, zip_no) values ('ACTIVATION', '현대빌라', '수원시 장안구', '정자1동', '123456');

insert into house_holder(member_id, name, mobile) values (1, '홍길동', '01012345678' );
insert into house_holder(member_id, name, mobile) values (2, '관리자', '01012345679' );

insert into house_hold(building_id, house_holder_id, room, house_hold_state) values (1, 1, '101호', 'LIVE' );
insert into house_hold(building_id, house_holder_id, room, house_hold_state) values (1, 2, '201호', 'LIVE' );

