package uk.co.optimisticpanda.grules.runners;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import groovy.lang.GroovyClassLoader;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.ImportCustomizer;
import uk.co.optimisticpanda.grules.ScriptLoader;

import java.util.HashMap;
import java.util.Map;

public class GroovyClassRunner {

    public interface Rule<T> {
        T evaluate(Map<String, String> facts);
    }

    private final LoadingCache<String, Rule<String>> cache;
    private final ScriptLoader scriptLoader = new ScriptLoader();

    public GroovyClassRunner() {
        ImportCustomizer importCustomizer = new ImportCustomizer();
        importCustomizer.addImports(Rule.class.getCanonicalName());

        CompilerConfiguration configuration = new CompilerConfiguration()
                .addCompilationCustomizers(importCustomizer);

        GroovyClassLoader loader = new GroovyClassLoader(Thread.currentThread().getContextClassLoader(), configuration);

        this.cache = CacheBuilder.newBuilder()
                .maximumSize(5)
                .build(new CacheLoader<String, Rule<String>>() {
                    @Override
                    public Rule<String> load(String script) throws Exception {
                        return (Rule<String>) loader.parseClass(script).newInstance();
                    }
                });
    }

    public String execute() throws Exception {
        Map<String, String> facts = new HashMap<>();
        facts.put("name", "bob");
        return cache.get(scriptLoader.getGroovyClass()).evaluate(facts);
    }
}
