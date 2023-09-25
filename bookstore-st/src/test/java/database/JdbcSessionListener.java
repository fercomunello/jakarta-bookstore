package database;

import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestPlan;

public final class JdbcSessionListener implements TestExecutionListener {

    @Override
    public void testPlanExecutionStarted(final TestPlan testPlan) {
        JdbcSession.open();
    }

    @Override
    public void testPlanExecutionFinished(final TestPlan testPlan) {
        JdbcSession.close();
    }
}
