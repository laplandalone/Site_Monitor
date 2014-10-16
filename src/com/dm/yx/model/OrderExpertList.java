package com.dm.yx.model;

import java.util.List;

import com.google.gson.annotations.Expose;

public class OrderExpertList
{
	@Expose
	List<OrderExpert> orders;

	public List<OrderExpert> getOrders()
	{
		return orders;
	}

	public void setOrders(List<OrderExpert> orders)
	{
		this.orders = orders;
	}
}
