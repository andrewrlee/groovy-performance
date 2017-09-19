package uk.co.optimisticpanda.grules.runners;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import org.codehaus.groovy.control.CompilerConfiguration;
import uk.co.optimisticpanda.grules.ScriptLoader;

/* Not threadsafe! No way to bind variables in a threadsafe way */
public class GroovyScriptRunner {

    private final LoadingCache<String, Script> cache;
    private final ScriptLoader scriptLoader = new ScriptLoader();

    public GroovyScriptRunner() {
        CompilerConfiguration compilerConfiguration = new CompilerConfiguration();
        GroovyShell shell = new GroovyShell(getClass().getClassLoader(), new Binding(), compilerConfiguration);

        this.cache = CacheBuilder.newBuilder()
                .maximumSize(5)
                .build(new CacheLoader<String, Script>() {
                    @Override
                    public Script load(String script) throws Exception {
                        return shell.parse(script);
                    }
                });
    }

    public String execute() throws Exception {
        return (String) cache.get(scriptLoader.getGroovyScript()).run();
    }
}
