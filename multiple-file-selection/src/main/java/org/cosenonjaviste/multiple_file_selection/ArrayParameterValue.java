package org.cosenonjaviste.multiple_file_selection;

import hudson.EnvVars;
import hudson.model.ParameterValue;
import hudson.model.AbstractBuild;
import hudson.model.Run;
import hudson.util.VariableResolver;

import java.util.List;
import java.util.logging.Logger;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.export.Exported;


/**
 * This class is used to export several different values into the shell.
 * 
 * Given a NAME of the parameter and a list of string like: "alice", "bob", "34",
 * they are exported as:
 * <ul>
 * <li>NAME_0 = alice </li>
 * <li>NAME_1 = bob</li>
 * <li>NAME_2 = 34</li>
 * <li>NAME_SIZE = 3</li>
 * </ul>
 * 
 * If the list is empty, NAME_SIZE = 0 
 * 
 * @author dzambon
 *
 */
public class ArrayParameterValue extends ParameterValue {
    
	
	private static final long serialVersionUID = -1300682400572110173L;

	private static final Logger LOGGER = Logger.getLogger(ArrayParameterValue.class.getName());

	private static final String SEPARATOR = "_";

	private static final String SIZE = "SIZE";
	
	@Exported(visibility=4)
    public final List<String> values;

    @DataBoundConstructor
    public ArrayParameterValue(String name, List<String> values) {
        this(name, values, null);
    }

    public ArrayParameterValue(String name, List<String> values, String description) {
        super(name, description);
        this.values = values;
    }

    @Override
    public void buildEnvironment(Run<?,?> build, EnvVars env) {
    	
    	for (int i =0; i<values.size();i++) {
    		env.put(name+SEPARATOR+i+"",values.get(i));
    		LOGGER.finer("Added paramter to env: "+name+SEPARATOR+i+" = "+values.get(i));
		}
    	env.put(name+SEPARATOR+SIZE,""+values.size());
		LOGGER.finer("Added paramter to env: "+name+SEPARATOR+SIZE+" = "+values.size());

    }

    @Override
    public VariableResolver<String> createVariableResolver(AbstractBuild<?, ?> build) {
        return new VariableResolver<String>() {
            public String resolve(String name) {
                return ArrayParameterValue.this.name.equals(name) ? values.toString() : null;
            }
        };
    }

    

    @Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((values == null) ? 0 : values.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ArrayParameterValue other = (ArrayParameterValue) obj;
		if (values == null) {
			if (other.values != null)
				return false;
		} else if (!values.equals(other.values))
			return false;
		return true;
	}

	@Override
    public String toString() {
    	return "(ArrayParameterValue) " + getName() + "='" + values + "'";
    }

    @Override public String getShortDescription() {
        return name + '=' + values;
    }

}
