<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<style>

div.artifactForm{
	border:1px solid black;
	width: 300px;
	height: 160px;
	text-align:left;
	margin:20px;
}
div.artifactForm div.caption{
	width:100%;
	height:20px;
	border-bottom: 1px solid black;
	background-color: #D4D4D4;
	text-align:center;
	vertical-align: middle;
}
div.artifactForm div.content{
	padding:10px;
}


div.data_field{
	margin-top:3px;
	margin-bottom:3px;
}
div.label{
	width: 60px;
}
div.label span{
	font-size:12px;
	font-family:Verdana;
}

div.value input{
	width:220px;
}

div.leftAlign{
	float: left;
}
</style>

<div class="artifactForm" align="left">
	<div class="caption">Add Artifact</div>
	<div class="content">
	<form name="AtrifactLoad" action="">
		<div class="data_field">
			<div class="label leftAlign"><span>ArtifactId</span></div>
			<div class="value"><input type="text" name="artifactId" /></div>
		</div>
		
		<div class="data_field">
			<div class="label leftAlign"><span>GroupId</span></div>
			<div class="value"><input type="text" name="groupId" /></div>
		</div>
		
		<div class="data_field">
			<div class="label leftAlign"><span>Version</span></div>
			<div class="value"><input type="text" name="version" /></div>
		</div>
		
		<div class="data_field">
			<div class="label leftAlign"><span>File</span></div>
			<div class="value"><input type="file" name="jar" /></div>
		</div>
		<div align="center">
			<input type="submit" value="Upload Artifact">
		</div>
	</form>
	</div>
</div>
