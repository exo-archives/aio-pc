<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@taglib uri="http://java.sun.com/portlet" prefix="portlet"%>
<%@taglib uri="http://java.sun.com/jstl/core" prefix="c"%>

<portlet:defineObjects/>


<div id="import">
	<div class="artifactForm" align="left">
		<div class="caption">Import zipped WAR application file.</div>
		<div class="content">
		<form name="import_zip" action="<portlet:actionURL/>" method="post" enctype="multipart/form-data">
			<input type="hidden" name="id" value="importzip_logic"/>
			<div class="data_field">
				<div class="label leftAlign"><span>WAR file</span></div>
				<div class="value"><input type="file" size="28" name="zip" /></div>
			</div>
			<div class="submit" align="center">
				<input type="submit" value="Import">
			</div>
		</form>
		</div>
	</div>
</div>

