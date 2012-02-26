/*
 * 
 * LegendShop 多用户商城系统
 * 
 *  版权所有,并保留所有权利。
 * 
 */
package com.legendshop.business.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.legendshop.business.common.CommonServiceUtil;
import com.legendshop.business.common.Constants;
import com.legendshop.business.common.PageLetEnum;
import com.legendshop.business.service.ShopDetailService;
import com.legendshop.business.service.UserDetailService;
import com.legendshop.central.license.LSResponse;
import com.legendshop.core.AttributeKeys;
import com.legendshop.core.base.BaseController;
import com.legendshop.core.constant.PathResolver;
import com.legendshop.core.dao.support.HqlQuery;
import com.legendshop.core.dao.support.PageSupport;
import com.legendshop.core.dao.support.SqlQuery;
import com.legendshop.model.entity.UserDetail;
import com.legendshop.model.entity.UserDetailView;
import com.legendshop.util.AppUtils;
import com.legendshop.util.sql.ConfigCode;

/**
 * 用户产品评论.
 */
@Controller
@RequestMapping("/system/userDetail")
public class UserDetailAdminController extends BaseController{ 
	
	/** The log. */
	private final Logger log = LoggerFactory.getLogger(UserDetailAdminController.class);

	/** The user detail service. */
	@Autowired
	private UserDetailService userDetailService;
	
	/** The shop detail service. */
	@Autowired
	private ShopDetailService shopDetailService;

	/**
	 * Query.
	 * 
	 * @param request
	 *            the request
	 * @param response
	 *            the response
	 * @param curPageNO
	 *            the cur page no
	 * @param userDetail
	 *            the user detail
	 * @return the string
	 */
	@Deprecated
	public String query_for_HQL(HttpServletRequest request, HttpServletResponse response, String curPageNO, UserDetail userDetail) {
		String search = request.getParameter("search") == null ? "" : request.getParameter("search");
		String enabled = request.getParameter("enabled") == null ? "" : request.getParameter("enabled");
		String haveShop = request.getParameter("haveShop") == null ? "" : request.getParameter("haveShop");
		String userMail = request.getParameter("userMail") == null ? "" : request.getParameter("userMail");
		
		log.debug("search = {},enabled = {}, haveShop = {}, userMail ={} ", new Object[]{search,enabled,haveShop ,userMail} );

			Map<String, String> map = new HashMap<String, String>();
			HqlQuery hqlQuery = new HqlQuery(60, curPageNO);

			if ("1".equals(haveShop)) {
				map.put("haveShop", "and u.shopId is not null");
			} else if ("0".equals(haveShop)) {
				map.put("haveShop", "and u.shopId is null");
			}

			if (!AppUtils.isBlank(search)) {
				map.put("userName", search);
				hqlQuery.addParams("%" + search + "%");
			}

			if (!AppUtils.isBlank(enabled)) {
				map.put("enabled", enabled);
				hqlQuery.addParams(enabled);
			}

			if (!AppUtils.isBlank(userMail)) {
				map.put("userMail", userMail);
				hqlQuery.addParams("%" + userMail + "%");
			}
			if (!CommonServiceUtil.isDataForExport(hqlQuery, request)) {// 非导出情况
			}
			if (!CommonServiceUtil.isDataSortByExternal(hqlQuery, request, map)) {
				map.put(Constants.ORDER_INDICATOR, "order by u.userRegtime desc");
			}
			String QueryNsortCount = ConfigCode.getInstance().getCode("biz.QueryUserDetailCount", map);
			String QueryNsort = ConfigCode.getInstance().getCode("biz.QueryUserDetail", map);
			hqlQuery.setAllCountString(QueryNsortCount);
			hqlQuery.setQueryString(QueryNsort);

			LSResponse lsResponse = (LSResponse) request.getSession().getServletContext().getAttribute(
					AttributeKeys.LEGENSHOP_LICENSE);
			request.setAttribute("supportOpenShop", shopDetailService.isSupportOpenShop(lsResponse));
			PageSupport ps = userDetailService.getUserDetailList(hqlQuery);
			request.setAttribute("search", search);
			request.setAttribute("userMail", userMail);
			request.setAttribute("enabled", enabled);
			request.setAttribute("haveShop", haveShop);
			savePage(ps, request);

		return PathResolver.getPath(request, PageLetEnum.USER_DETAIL_LIST_PAGE);
	}
	
	/**
	 * Query.
	 * 
	 * @param request
	 *            the request
	 * @param response
	 *            the response
	 * @param curPageNO
	 *            the cur page no
	 * @param userDetail
	 *            the user detail
	 * @return the string
	 */
	@RequestMapping("/query")
	public String query(HttpServletRequest request, HttpServletResponse response, String curPageNO, UserDetail userDetail) {
		String search = request.getParameter("search") == null ? "" : request.getParameter("search");
		String enabled = request.getParameter("enabled") == null ? "" : request.getParameter("enabled");
		String haveShop = request.getParameter("haveShop") == null ? "" : request.getParameter("haveShop");
		String userMail = request.getParameter("userMail") == null ? "" : request.getParameter("userMail");
		
		log.debug("search = {},enabled = {}, haveShop = {}, userMail ={} ", new Object[]{search,enabled,haveShop ,userMail} );

			Map<String, String> map = new HashMap<String, String>();
			SqlQuery hqlQuery = new SqlQuery(60, curPageNO);

			if ("1".equals(haveShop)) {
				map.put("haveShop", "and u.shop_id is not null");
			} else if ("0".equals(haveShop)) {
				map.put("haveShop", "and u.shop_id is null");
			}

			if (!AppUtils.isBlank(search)) {
				map.put("userName", search);
				hqlQuery.addParams("%" + search + "%");
			}

			if (!AppUtils.isBlank(enabled)) {
				map.put("enabled", enabled);
				hqlQuery.addParams(enabled);
			}

			if (!AppUtils.isBlank(userMail)) {
				map.put("userMail", userMail);
				hqlQuery.addParams("%" + userMail + "%");
			}
			if (!CommonServiceUtil.isDataForExport(hqlQuery, request)) {// 非导出情况
			}
			if (!CommonServiceUtil.isDataSortByExternal(hqlQuery, request, map)) {
				map.put(Constants.ORDER_INDICATOR, "order by u.user_regtime desc");
			}
			String QueryNsortCount = ConfigCode.getInstance().getCode("biz.QueryUserDetailCount", map);
			String QueryNsort = ConfigCode.getInstance().getCode("biz.QueryUserDetail", map);
			hqlQuery.setAllCountString(QueryNsortCount);
			hqlQuery.setQueryString(QueryNsort);
	
			LSResponse lsResponse = (LSResponse) request.getSession().getServletContext().getAttribute(
					AttributeKeys.LEGENSHOP_LICENSE);
			request.setAttribute("supportOpenShop", shopDetailService.isSupportOpenShop(lsResponse));
			PageSupport ps = userDetailService.getUserDetailList(hqlQuery);
			ps.setResultList(convert(ps.getResultList()));
			request.setAttribute("search", search);
			request.setAttribute("userMail", userMail);
			request.setAttribute("enabled", enabled);
			request.setAttribute("haveShop", haveShop);
			savePage(ps, request);

		return PathResolver.getPath(request, PageLetEnum.USER_DETAIL_LIST_PAGE);
	}

	/**
	 * Convert.
	 * 
	 * @param objs
	 *            the objs
	 * @return the list
	 */
	private List<UserDetailView> convert(List<Object> objs){
		if(AppUtils.isBlank(objs)){
			return null;
		}
		List<UserDetailView> list = new ArrayList<UserDetailView>();		
		for (Object obj : objs) {
			UserDetailView userDetail = new UserDetailView();
			Object[] arrayObj = (Object[])obj;
			if(arrayObj[0] != null){
				userDetail.setUserId((String)(arrayObj[0]));
			}
			if(arrayObj[1]!=null){
				userDetail.setUserName((String)(arrayObj[1]));
			}
			
			if(arrayObj[2]!=null){
				userDetail.setNickName((String)(arrayObj[2]));
			}
			
			if(arrayObj[3]!=null){
				userDetail.setUserMail((String)(arrayObj[3]));
			}
			
			if(arrayObj[4]!=null){
				userDetail.setUserRegip((String)(arrayObj[4]));
			}
			
			if(arrayObj[5]!=null){
				userDetail.setModifyTime((Date)(arrayObj[5]));
			}
			
			if(arrayObj[6]!=null){
				userDetail.setUserRegtime((Date)(arrayObj[6]));
			}
			
			if(arrayObj[7]!=null){
				userDetail.setEnabled((String)(arrayObj[7]));
			}
		
			if(arrayObj[8]!=null){
				userDetail.setShopId(((Integer)(arrayObj[8])).longValue());
			}
			list.add(userDetail);
		}
		return list;
	}

}
