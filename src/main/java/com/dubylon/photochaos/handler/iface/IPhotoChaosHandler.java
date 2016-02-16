package com.dubylon.photochaos.handler.iface;

import com.dubylon.photochaos.handler.PCResponseObject;

import javax.servlet.http.HttpServletRequest;

public interface IPhotoChaosHandler {

  PCResponseObject doGet(HttpServletRequest request);

  PCResponseObject doPost(HttpServletRequest request);

  PCResponseObject doPut(HttpServletRequest request);

  PCResponseObject doDelete(HttpServletRequest request);

}
