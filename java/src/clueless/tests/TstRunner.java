package clueless.tests;

import org.junit.runner.JUnitCore;
import org.junit.runner.Request;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

/*
// Note: This is the easy way to run all tests, but when it fails
//       it is way to noisy.
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ByteBufferBackedStreamTest.class})
public class TstRunner {}
*/

public class TstRunner {
    public static void printResult(Result result, Class cls) {
        String status = "PASS";
        String name = cls.getSimpleName();
        int failed = result.getFailureCount();
        int ran = result.getRunCount();
        int passed = ran - failed;
        String summary;

        if (passed != ran) {
            status = "FAIL";
        }

        summary = status + " (" + passed + "/" + ran + "): " + name;
        System.out.println(summary);

        if (passed != ran) {
            for (Failure failure : result.getFailures()) {
                System.out.println("  " + failure.toString());
            }
        }
    }

    public static void main(String[] args) {
        JUnitCore jUnitCore = new JUnitCore();
        Class cls = ByteBufferBackedStreamTest.class;
        Request request = Request.aClass(cls);
        Result result = jUnitCore.run(request);
        printResult(result, cls);
    }
}
