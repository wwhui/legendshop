/*
 * 
 * LegendShop 多用户商城系统
 * 
 *  版权所有,并保留所有权利。
 * 
 */
package com.legendshop.business.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.legendshop.business.common.CommonServiceUtil;
import com.legendshop.business.common.PageLetEnum;
import com.legendshop.business.service.OrderService;
import com.legendshop.core.UserManager;
import com.legendshop.core.base.AdminController;
import com.legendshop.core.base.BaseController;
import com.legendshop.core.constant.ParameterEnum;
import com.legendshop.core.constant.PathResolver;
import com.legendshop.core.dao.support.CriteriaQuery;
import com.legendshop.core.dao.support.PageSupport;
import com.legendshop.core.helper.PropertiesUtil;
import com.legendshop.model.entity.Sub;
import com.legendshop.util.AppUtils;

/**
 * 订单控制器。 用于显示用户订单信息,管理员操作.
 */
@Controller
@RequestMapping("/admin/order")
public class OrderAdminController extends BaseController implements AdminController<Sub,Long>{
	
	/** The log. */
	private final Logger log = LoggerFactory.getLogger(OrderAdminController.class);
	
	/** The LIS t_ page. */
	public static String LIST_PAGE = "/order/orderList";
	
	/** The EDI t_ page. */
	public static String EDIT_PAGE="/order/order";
	
	/** The LIS t_ query. */
	public static String LIST_QUERY = "/admin/order/query";
	
	/** The order service. */
	@Autowired
	private OrderService orderService;

	/* (non-Javadoc)
	 * @see com.legendshop.core.base.AdminController#query(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.String, java.lang.Object)
	 */
	@Override
	@RequestMapping("/query")
	public String query(HttpServletRequest request, HttpServletResponse response, String curPageNO, Sub entity) {
		String subNumber = entity.getSubNumber();
		String loginName = UserManager.getUsername(request);
		if (!AppUtils.isBlank(subNumber)) {
			subNumber = subNumber.trim();
		}
			// Qbc查找方式
			CriteriaQuery cq = new CriteriaQuery(Sub.class,curPageNO);
			if (CommonServiceUtil.haveViewAllDataFunction(request)) {
				if (!AppUtils.isBlank(entity.getShopName())) {
					cq.eq("shopName", entity.getShopName());
				}
			} else {
				cq.eq("shopName", loginName);
			}

			if (AppUtils.isNotBlank(subNumber)) {
				cq.like("subNumber", subNumber + "%");
			}

			if (AppUtils.isNotBlank(entity.getUserName())) {
				cq.like("userName", entity.getUserName() + "%");
			}
			cq.eq("status", entity.getStatus());
			cq.eq("subCheck", entity.getSubCheck());
			if (!CommonServiceUtil.isDataForExport(cq, request)) {// 非导出情况
				cq.setPageSize(PropertiesUtil.getObject(ParameterEnum.FRONT_PAGE_SIZE, Integer.class));
			}
			if (!CommonServiceUtil.isDataSortByExternal(cq, request)) {
				cq.addOrder("desc", "subDate");
			}
			cq.add();
			PageSupport ps = orderService.getOrderList(cq);
			savePage(ps, request);
			request.setAttribute("subForm", entity);
			return PathResolver.getPath(request, PageLetEnum.ORDER_LIST_PAGE);

	}

	/* (non-Javadoc)
	 * @see com.legendshop.core.base.AdminController#save(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)
	 */
	@Override
	@RequestMapping("/save")
	public String save(HttpServletRequest request, HttpServletResponse response, Sub entity) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.legendshop.core.base.AdminController#delete(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)
	 */
	@Override
	@RequestMapping(value = "/delete/{id}")
	public String delete(HttpServletRequest request, HttpServletResponse response, Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.legendshop.core.base.AdminController#load(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	@RequestMapping(value = "/load/{id}")
	public String load(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.legendshop.core.base.AdminController#update(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)
	 */
	@Override
	@RequestMapping(value = "/update")
	public String update(HttpServletRequest request, HttpServletResponse response, Long id) {
		// TODO Auto-generated method stub
		return null;
	}

}
