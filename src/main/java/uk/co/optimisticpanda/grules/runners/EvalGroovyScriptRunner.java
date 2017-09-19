package uk.co.optimisticpanda.grules.runners;

import groovy.util.Eval;
import uk.co.optimisticpanda.grules.ScriptLoader;

public class EvalGroovyScriptRunner {

    private final ScriptLoader scriptLoader = new ScriptLoader();

    public EvalGroovyScriptRunner() {
    }

    public String execute() throws Exception {
        return (String) Eval.me(scriptLoader.getGroovyScript());
    }

}
