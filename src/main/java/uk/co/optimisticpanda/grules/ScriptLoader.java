package uk.co.optimisticpanda.grules;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;

import java.util.concurrent.atomic.AtomicLong;

import static java.util.concurrent.TimeUnit.SECONDS;

public class ScriptLoader {

    private final Supplier<String> groovyClass;
    private final Supplier<String> groovyScript;
    private final Supplier<String> groovyScriptWithBinding;

    public ScriptLoader() {

        AtomicLong groovyCount = new AtomicLong();
        this.groovyClass = Suppliers.memoizeWithExpiration(() -> getClass(groovyCount), 10, SECONDS);

        AtomicLong scriptCount = new AtomicLong();
        this.groovyScript = Suppliers.memoizeWithExpiration(() -> getScript(scriptCount), 10, SECONDS);

        AtomicLong scriptWithBindingCount = new AtomicLong();
        this.groovyScriptWithBinding = Suppliers.memoizeWithExpiration(() -> getScriptWithBinding(scriptWithBindingCount), 10, SECONDS);
    }

    public String getGroovyClass() {
        return groovyClass.get();
    }

    public String getGroovyScript() {
        return groovyScript.get();
    }

    public String getGroovyScriptWithBinding() {
        return groovyScriptWithBinding.get();
    }


    private String getClass(AtomicLong counter) {
        return "class Foo implements Rule<String> { "
                + "   String evaluate(Map<String,String> facts) {"
                + "      return \"\"\"abc ${facts.name} " + counter.getAndIncrement() + "\"\"\""
                + "   }"
                + "}";
    }

    private String getScript(AtomicLong counter) {
        return  "return \"\"\"abc facts.name " + counter.getAndIncrement() + "\"\"\"";
    }

    private String getScriptWithBinding(AtomicLong counter) {
        return  "return \"\"\"abc ${facts.name} " + counter.getAndIncrement() + "\"\"\"";

    }
}
