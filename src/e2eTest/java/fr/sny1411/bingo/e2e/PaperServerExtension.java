package fr.sny1411.bingo.e2e;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.util.List;

/**
 * Gives the tests a {@link PaperServer}, started once for all the tests of a run and stopped after them,
 * and fails a test when the plugin throws an exception during it.
 */
public class PaperServerExtension implements BeforeEachCallback, AfterEachCallback, ParameterResolver {
    private static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(PaperServerExtension.class);

    @Override
    public void beforeEach(ExtensionContext context) {
        context.getStore(NAMESPACE).put("logMark", server(context).logMark());
    }

    @Override
    public void afterEach(ExtensionContext context) {
        PaperServer server = server(context);
        int mark = context.getStore(NAMESPACE).get("logMark", Integer.class);
        server.waitForLog();
        List<String> exceptions = PluginExceptions.find(server.logSince(mark));
        if (!exceptions.isEmpty()) {
            throw new AssertionError("The plugin threw an exception during the test:\n" + String.join("\n\n", exceptions));
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        return parameterContext.getParameter().getType() == PaperServer.class;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        return server(extensionContext);
    }

    // Kept in the root store, which closes it at the end of the run
    private static PaperServer server(ExtensionContext context) {
        return context.getRoot().getStore(NAMESPACE).computeIfAbsent(PaperServer.class, key -> {
            try {
                return PaperServer.start();
            } catch (Exception e) {
                throw new IllegalStateException("Could not start the Paper server", e);
            }
        }, PaperServer.class);
    }
}
