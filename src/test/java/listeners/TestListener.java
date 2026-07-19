package listeners;

import com.codeborne.selenide.WebDriverRunner;
import io.qameta.allure.Attachment;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestListener;
import org.testng.ITestResult;


import java.util.concurrent.TimeUnit;

@Log4j2
public class TestListener implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        log.info("======== STARTING TEST: {} ========",
                result.getName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        log.info("======== PASSED TEST: {} | Duration: {}s ========",
                result.getName(),
                getExecutionTime(result));
    }

    @Override
    public void onTestFailure(ITestResult result) {
        log.error("======== FAILED TEST: {} | Duration: {}s ========",
                result.getName(),
                getExecutionTime(result));
        takeScreenshot();
    }

    @Attachment(value = "Screenshot on failure", type = "image/png")
    private byte[] takeScreenshot() {
        return ((TakesScreenshot) WebDriverRunner.getWebDriver())
                .getScreenshotAs(OutputType.BYTES);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        log.warn("======== SKIPPED TEST: {} ========",
                result.getName());
    }

    private long getExecutionTime(ITestResult result) {
        return TimeUnit.MILLISECONDS.toSeconds(
                result.getEndMillis() - result.getStartMillis()
        );
    }
}

