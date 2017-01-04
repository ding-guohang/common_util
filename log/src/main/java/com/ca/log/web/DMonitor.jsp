/**
 * Created by guohang on 17-1-4.
 */
<%@ page session="false" %>
<%@page import="com.ca.log.monitor.DMonitor" %><%@
page import="java.util.Map.Entry"%><%@ page contentType="text/plain;charset=UTF-8" language="java" %><%

for (Entry<String, Long> entry : DMonitor.getValues().entrySet()) {
	String name = entry.getKey();
	Long value = entry.getValue();
	out.append(name + "=" + value + "\n");
}
%>