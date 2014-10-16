package com.dm.yx.webservice;

import com.lidroid.xutils.http.RequestParams;



public interface IWebServiceInterface {
	
	public RequestParams queryDoctorList(String expertType,String onLineType,String teamId);
	
	public RequestParams queryTeamList(String hospitalId,String expertType);
	
	public RequestParams queryOrderDoctorList(String hospitalId,String teamId,String doctorIdT);
	
	public RequestParams getOrderNormalNum(String teamId,String registerTime);
	
	public RequestParams queryUser(String telephone,String password);
	
	public RequestParams addUserQuestion(String userQuestion);
	
	public RequestParams addUser(String user);
	
	public RequestParams updateUser(String user);
	
	public RequestParams getUserQuestionsByDoctorId(String doctorId);
	
	public RequestParams getUserQuestionsByIds(String questionId);
	
	public RequestParams getUserQuestionsByUserId(String userId,String hospitalId);
	
	public RequestParams getHospitals(String hospitalId);
	
	public RequestParams getUserOrderById(String userId,String hospitalId);
	
	public RequestParams getTeamByHospitalId(String hospitalId);
	
	public RequestParams getNewsByHospitalId(String hospitalId,String type,String typeId);
	
	public RequestParams addUserRegisterOrder(String hospitalId, String userId, String registerId, String doctorId, String doctorName, String orderNum, String orderFee, String registerTime, String userName, String userNo, String userTelephone,String sex, String teamId, String teamName);
	
	public RequestParams checkNewVersion(String param);
	
	public RequestParams getNewsType(String hospitalId,String type);
	
	public RequestParams getAuthCode(String accNbr,String type);
	
	public RequestParams checkAuthCode(String accNbr,String authCode);

	public RequestParams queryOrderByDoctorIdList(String hospitalId,String userId,String orderTeamId,String doctorId, String weekStr,
			String dateStr);
	
	public RequestParams orderPay(String orderdId,String payState);
	
	public RequestParams getRsaSign(String orderdId);
	
}
