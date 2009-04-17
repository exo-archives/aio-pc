package com.sun.ts.tests.portlet.common.util;

public class ParameterCheck {
	
	private String _name;
	private boolean pass = true;
	ResultWriter _writer;
	
	private String[] expectedValues = {"One", "Two", "Three"};
	
	
	public ParameterCheck(String name) {
		_name = name;
	}
	
	public ParameterCheck(String name, ResultWriter writer) {
		_name = name;
		_writer = writer;
	}
	
	public void checkParameter(String[] values) {
		if (values != null) {
			if (values.length == expectedValues.length) {
				for (int i = 0; i < expectedValues.length; i++) {
					if (values[i] == null || !values[i].equals(expectedValues[i]))
						pass = false;
				}
			} 
			else
				pass = false;
		} 
		else
			pass = false;
		
		if (pass)
			_writer.setStatus(ResultWriter.PASS);
		else {
			_writer.setStatus(ResultWriter.FAIL);
			for (int i = 0; i < expectedValues.length; i++) {
				if (values!=null&&i < values.length && values[i] != null)
					_writer.addDetail("Actual result: " + values[i]);
				else 
					_writer.addDetail("No actual result for parameter " + (i+1));
				_writer.addDetail("Expected result: " + expectedValues[i]);
			}
		}
	}
}
