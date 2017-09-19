package uk.co.optimisticpanda.grules.runners;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.runtime.GStringImpl;
import uk.co.optimisticpanda.grules.ScriptLoader;

import java.util.HashMap;
import java.util.Map;

public class ThreadSafeGroovyScriptRunner {

    private final ScriptLoader scriptLoader = new ScriptLoader();

    public ThreadSafeGroovyScriptRunner() {

    }

    public String execute() throws Exception {
        CompilerConfiguration compilerConfiguration = new CompilerConfiguration();

        Map<String, String> facts = new HashMap<>();
        facts.put("name", "bob");

        Binding binding = new Binding();
        binding.setVariable("facts", facts);

        GroovyShell shell = new GroovyShell(getClass().getClassLoader(), binding, compilerConfiguration);
        return ((GStringImpl) shell.evaluate(scriptLoader.getGroovyScriptWithBinding())).toString();
    }
}
