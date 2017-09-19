package uk.co.optimisticpanda.grules;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import uk.co.optimisticpanda.grules.runners.EvalGroovyScriptRunner;
import uk.co.optimisticpanda.grules.runners.GroovyClassRunner;
import uk.co.optimisticpanda.grules.runners.GroovyScriptRunner;
import uk.co.optimisticpanda.grules.runners.ThreadSafeGroovyScriptRunner;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import static java.lang.Runtime.getRuntime;

public class Runner {

    private static final Callable<String> evalRunner = new EvalGroovyScriptRunner()::execute;
    private static final Callable<String> scriptRunner = new GroovyScriptRunner()::execute;
    private static final Callable<String> scriptWithBindingRunner = new ThreadSafeGroovyScriptRunner()::execute;
    private static final Callable<String> classRunner = new GroovyClassRunner()::execute;

    public static void main(String[] args) throws Exception {

        MetricRegistry registry = new MetricRegistry();

        registry.register("memory", (Gauge<Long>) () ->
                getRuntime().totalMemory() - getRuntime().freeMemory());

        ConsoleReporter reporter = ConsoleReporter.forRegistry(registry)
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .build();
        reporter.start(5, TimeUnit.SECONDS);

        Timer timer = registry.timer("timer");

        while (true) {
            try (Timer.Context ctx = timer.time()) {
                classRunner.call();
            }
        }
    }
}

