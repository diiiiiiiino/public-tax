# public-tax
공공요금 계산

* 기능 목록
1. 관리자 신규 등록 시 건물과 세대도 같이 등록 (AdminCreateService) <br>
   -> 관리자가 건물을 등록할 때 세대주에 해당하는 Member가 없을 수도 있다. <br> ok
   -> 세대에 세대주로 등록 대상자에게 카톡을 보내서 등록 요청을 보낸다. (RequestCreateMemberSerice) <br> ok
   -> 관리자는 세대주가 될 수도 있다. ok
2. Member 신규 등록 시 관리자가 보낸 초대 코드가 필요하며 존재하는 세대에 세대주로 등록 해줘야 한다. (MemberCreateService) ok
3. 관리자가 수도 요금 정산 데이터 생성 (WaterBillCreateService) ok
4. 세대주가 수도 요금 입력 (WaterMeterCreateService) ok
5. 관리자가 수도요금 정산 기능 수행 (WaterBillCalculateAppService) <br> 
   -> 정산 시 거주하고 있지 않은 세대가 있을 수 있으며 정산대상에서 제외한다. (세대주가 등록 되지 않은 경우) ok <br>
   -> 정산 완료 시 세대주들에게 정산 완료 카카오톡 메세지를 보낸다.
   -> 수도요금 정산이 완료되면 관리자에게 정산 완료 카톡을 보낸다.
6. 관리자 변경 기능
7. 회원 정보 변경 기능 ok