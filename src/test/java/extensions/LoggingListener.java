package extensions;

import lombok.extern.log4j.Log4j2;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;

@Log4j2
public class LoggingListener implements TestExecutionListener {

    @Override
    public void executionFinished(TestIdentifier testIdentifier, TestExecutionResult testExecutionResult) {
        if (testExecutionResult.getThrowable().isEmpty()) {
            return;
        }

        Throwable throwable = testExecutionResult.getThrowable().get();
        if (!(throwable instanceof AssertionError)) {
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(throwable.getMessage()).append("\n\nin:\n");

        for (StackTraceElement element : throwable.getStackTrace()) {
            sb.append("-> ").append(element.toString()).append("\n");
        }

        log.error(sb.toString());
    }

}
