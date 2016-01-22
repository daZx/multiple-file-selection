package org.cosenonjaviste.multiple_file_selection;

import java.util.List;

import org.kohsuke.stapler.DataBoundConstructor;

public class MultipleFileSelectionParameterValue extends ArrayParameterValue {


	private static final long serialVersionUID = 308826876894940844L;

	@DataBoundConstructor
	public MultipleFileSelectionParameterValue(String name, List<String> values) {
		super(name.toUpperCase(), values);
	}

}
