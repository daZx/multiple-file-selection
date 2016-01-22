package org.cosenonjaviste.multiple_file_selection;

import hudson.Extension;
import hudson.Util;
import hudson.model.ParameterValue;
import hudson.model.ParameterDefinition;
import hudson.util.FormValidation;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.apache.commons.lang.StringUtils;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.export.Exported;

/**
 * @author dzambon
 *
 */
public class MultipleFileSelectionParameterDefinition extends
		ParameterDefinition {

	private static final String NO_FILE_FOUND = "NO FILE FOUND :(";

	private static final long serialVersionUID = 1531503644305270657L;

	private static final Logger LOGGER = Logger
			.getLogger(MultipleFileSelectionParameterDefinition.class.getName());

	private String paths;
	private String regexp;
	private String size ="4";
//	private List<String> filelist = new ArrayList<String>();
	private FilenameFilter filter;
	private boolean completePath =false;

	@Extension
	public static class DescriptorImpl extends ParameterDescriptor {

		@Override
		public String getDisplayName() {
			return "Multiple Files Selection Parameter";
		}

		public FormValidation doCheckName(@QueryParameter final String name)
				throws IOException {
			if (StringUtils.isBlank(name)) {
				return FormValidation
						.error("The name of the paramenter cannot be empty");
			}
			return FormValidation.ok();
		}

		public FormValidation doCheckPaths(@QueryParameter final String paths)
				throws IOException {
			if (StringUtils.isBlank(paths)) {
				return FormValidation
						.error("The plugin needs at least one valid path!");
			}

			String[] pathsTokens = paths.split(","); // allow multiple paths!
			for (String string : pathsTokens) {
				File dir = new File(string);
				if (!dir.exists()) {
					return FormValidation.error("Path does not exists", string);
				}

			}
			return FormValidation.ok();
		}

		public FormValidation doCheckSize(@QueryParameter final String size)
				throws IOException {
			if (size == null || size.isEmpty()) {
				return FormValidation.ok();
			}
			try {
				Integer.parseInt(size);
			} catch (NumberFormatException e) {
				return FormValidation.error("Size must be an integer number",
						size);
			}

			return FormValidation.ok();
		}

		public FormValidation doCheckRegexp(@QueryParameter final String regexp)
				throws IOException {
			if (regexp == null || regexp.isEmpty()) {
				// accept all extensions
				return FormValidation.ok();
			}
			try {
				new RegexFileFilter(regexp);
			} catch (IllegalArgumentException ie) {
				return FormValidation.error("Invalid RegExp!");
			}

			return FormValidation.ok();
		}

	}

	@DataBoundConstructor
	public MultipleFileSelectionParameterDefinition(String name,
			String description, String paths, String size, String regExp, boolean completePath) {
		super(name.toUpperCase(), description);
		this.paths = Util.fixNull(paths);
		this.completePath = completePath;
		this.size = size;
		this.regexp = regExp;
		if (regexp == null || regexp.trim().length() < 1) {
			filter = FileFilterUtils.trueFileFilter();
		} else {
			filter = new RegexFileFilter(regexp);
		}

	}

	@Override
	public ParameterValue createValue(StaplerRequest request) {
		return null;
	}

	@Override
	public ParameterValue createValue(StaplerRequest request, JSONObject jO) {
		Object value = jO.get("value");
		List<String> values = new ArrayList<String>();
		if (value instanceof String) {
			values.add((String) value);
		} else if (value instanceof JSONArray) {
			JSONArray jsonValues = (JSONArray) value;
			for (int i = 0; i < jsonValues.size(); i++) {
				String str = (String) jsonValues.getString(i); 
				if (!NO_FILE_FOUND.equals(str))
						values.add(str);

			}

		}

		return new MultipleFileSelectionParameterValue(getName(), values);
	}

	/*
	 * Creates list to display in config.jelly
	 */
	public List<String> getFilelist() throws IOException {
		List<String> res = new ArrayList<String>();

		String[] pathTokens = paths.split(",");
		for (String p : pathTokens) {
			File d = new File(p);
			File[] foundFiles = d.listFiles(filter);
			for (File file : foundFiles) {
				if (this.completePath)
					res.add(file.getAbsolutePath());
				else
					res.add(file.getName());
			}
		}

		if (res.size() < 1) {
			res.add(NO_FILE_FOUND);
		}
		return res;
	}

	
	
	public String getPaths() {
		return paths;
	}

	public void setPaths(String paths) {
		this.paths = paths;
	}

	public String getRegExp() {
		return regexp;
	}

	public void setRegExp(String regexp) {
		this.regexp = regexp;
	}

	public String getSelectSize() {
		return size;
	}

	public void setSelectSize(String size) {
		this.size = size;
	}

	public boolean isCompletePath() {
		return completePath;
	}

	public void setCompletePath(boolean completePath) {
		this.completePath = completePath;
	}

	
}
