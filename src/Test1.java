import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class Test1 {
    private static final String SHOPPE_URL = "https://fruitshoppe.firebaseapp.com/#/";
    private ChromeDriver driver;

    @BeforeTest
    public void beforeTest() {
        driver = DriverFactory.getChromeDriver();
    }

    @AfterTest
    public void afterTest() {
        driver.quit();
    }

    /**
     * Test to verify that when the user navigates from one page to another within FruitShoppe website,
     * query params like OrgId, UserId, SessionId etc are the same
     */
    @Test
    public void testQueryParamsAfterNavigatingToAnotherPage() throws Exception {
        driver.get(SHOPPE_URL);

        // Wait for some time since the events are sent to full story server after few seconds
        Thread.sleep(6000);

        // Get performance logs
        List<LogEntry> entries = driver.manage().logs().get(LogType.PERFORMANCE).getAll();

        // Get query params for the first call to full story server
        SequenceQueryParams seq1Params = getSequenceQueryParams(entries, "1");
        Assert.assertNotNull(seq1Params, "Sequence 1 not found");

        // Click on Mangocados link
        driver.findElement(By.xpath("//a[@href='#/market']")).click();

        // Wait for some more time for the events to be captured and sent to server
        Thread.sleep(6000);

        // Get performance logs again
        entries = driver.manage().logs().get(LogType.PERFORMANCE).getAll();

        // Get query params used for the first call after navigating to new page
        SequenceQueryParams seq3Params = getSequenceQueryParams(entries, "3");
        Assert.assertNotNull(seq3Params, "Sequence 3 not found");

        // Verify OrgId, UserId, SessionId, PageId and PageStart remain same
        Assert.assertEquals(seq1Params.getOrgId(), seq3Params.getOrgId(),
                "OrgId does not match after navigating to new page. Expected:" + seq1Params.getOrgId() + ", found:" + seq3Params.getOrgId() );
        Assert.assertEquals(seq1Params.getUserId(), seq3Params.getUserId(),
                "UserId does not match after navigating to new page. Expected:" + seq1Params.getUserId() + ", found:" + seq3Params.getUserId() );
        Assert.assertEquals(seq1Params.getSessionId(), seq3Params.getSessionId(),
                "SessionId does not match after navigating to new page. Expected:" + seq1Params.getSessionId() + ", found:" + seq3Params.getSessionId() );
        Assert.assertEquals(seq1Params.getPageId(), seq3Params.getPageId(),
                "PageId does not match after navigating to new page. Expected:" + seq1Params.getPageId() + ", found:" + seq3Params.getPageId() );
        Assert.assertEquals(seq1Params.getPageStart(), seq3Params.getPageStart(),
                "PageStart does not match after navigating to new page. Expected:" + seq1Params.getPageStart() + ", found:" + seq3Params.getPageStart() );
    }

    /**
     * Test to verify that when the user navigates from Fruit Shoppe website to google and back
     * query params like UserId, SessionId etc are the same but PageId and PageStart are different
     */
    @Test
    public void testQueryParamsAfterNavigatingFromFruitShoppeToGoogleAndBack() throws Exception {
        driver.get(SHOPPE_URL);

        // Wait for some time since the events are sent to full story server after few seconds
        Thread.sleep(6000);

        // Get performance logs
        List<LogEntry> entries = driver.manage().logs().get(LogType.PERFORMANCE).getAll();

        // Get query params for the first call to full story server
        SequenceQueryParams seq1Params = getSequenceQueryParams(entries, "1");
        Assert.assertNotNull(seq1Params, "Sequence 1 not found");

        // Navigate to google
        driver.get("http://google.com");
        Assert.assertTrue(driver.getCurrentUrl().contains("google.com"));

        // Navigate back to Fruit Shoppe
        driver.get(SHOPPE_URL);

        // Wait for some more time for the events to be captured and sent to server
        Thread.sleep(6000);

        // Get performance logs again
        entries = driver.manage().logs().get(LogType.PERFORMANCE).getAll();

        // Get query params used for the first call after navigating to new page
        SequenceQueryParams seq2Params = getSequenceQueryParams(entries, "*");
        Assert.assertNotNull(seq2Params, "No sequence found");

        // Verify OrgId, UserId, SessionId, remain same but not PageId, PageStart
        Assert.assertEquals(seq1Params.getOrgId(), seq2Params.getOrgId(),
                "OrgId does not match after navigating to new page. Expected:" + seq1Params.getOrgId() + ", found:" + seq2Params.getOrgId() );
        Assert.assertEquals(seq1Params.getUserId(), seq2Params.getUserId(),
                "UserId does not match after navigating to new page. Expected:" + seq1Params.getUserId() + ", found:" + seq2Params.getUserId() );
        Assert.assertEquals(seq1Params.getSessionId(), seq2Params.getSessionId(),
                "SessionId does not match after navigating to new page. Expected:" + seq1Params.getSessionId() + ", found:" + seq2Params.getSessionId() );
        Assert.assertNotEquals(seq1Params.getPageId(), seq2Params.getPageId(),
                "PageId match after navigating to new page. PageId1:" + seq1Params.getPageId() + ", PageId2:" + seq2Params.getPageId() );
        Assert.assertNotEquals(seq1Params.getPageStart(), seq2Params.getPageStart(),
                "PageStart match after navigating to new page. PageStart1:" + seq1Params.getPageStart() + ", PageStart2:" + seq2Params.getPageStart() );
    }

    /**
     * Test to verify that when the user close the browser and opens Fruit Shoppe in new window
     * new session is created but orgId remains same
     */
    @Test
    public void testToVerifyNewSessionIsCreatedAfterClosingAndReopeningFruitShoppe() throws Exception {
        driver.get(SHOPPE_URL);

        // Wait for some time since the events are sent to full story server after few seconds
        Thread.sleep(6000);

        // Get performance logs
        List<LogEntry> entries = driver.manage().logs().get(LogType.PERFORMANCE).getAll();

        // Get query params for the first call to full story server
        SequenceQueryParams seq1Params = getSequenceQueryParams(entries, "1");

        // Quit the browser
        driver.quit();

        // Create a new browser instance and go to FuiteShoppe again
        driver = DriverFactory.getChromeDriver();
        driver.get(SHOPPE_URL);

        // Wait for some more time for the events to be captured and sent to server
        Thread.sleep(10000);

        // Get performance logs again
        entries = driver.manage().logs().get(LogType.PERFORMANCE).getAll();

        // Get query params used for the first call after navigating to new page
        SequenceQueryParams seq2Params = getSequenceQueryParams(entries, "*");
        Assert.assertNotNull(seq2Params, "No sequence found");

        // Verify OrgId remains ame but new session is created i.e., SessionId, PageId, PageStart are different
        Assert.assertEquals(seq1Params.getOrgId(), seq2Params.getOrgId(),
                "OrgId does not match after navigating to new page. Expected:" + seq1Params.getOrgId() + ", found:" + seq2Params.getOrgId() );
        Assert.assertNotEquals(seq1Params.getUserId(), seq2Params.getUserId(),
                "UserId matches after closing and opening Fruit Shoppe again. Value:" + seq1Params.getUserId());
        Assert.assertNotEquals(seq1Params.getSessionId(), seq2Params.getSessionId(),
                "SessionId matches after closing and opening Fruit Shoppe again. Value:" + seq1Params.getSessionId());
        Assert.assertNotEquals(seq1Params.getPageId(), seq2Params.getPageId(),
                "PageId matches after closing and opening Fruit Shoppe again. Value:" + seq1Params.getPageId());
        Assert.assertNotEquals(seq1Params.getPageStart(), seq2Params.getPageStart(),
                "PageStart matches after closing and opening Fruit Shoppe again. Value:" + seq1Params.getPageStart());
    }

    /**
     *
     * @param entries List of LogEntry
     * @param seq Sequence number to be found. If set to *, params for the first seq found is returned
     * @return object of SequenceQueryParams
     * @throws ParseException throws ParseException when unable to parse the Json string
     * @throws MalformedURLException throws MalformedURLException when unable to create URL object out of a string
     */
    private SequenceQueryParams getSequenceQueryParams(List<LogEntry> entries, String seq) throws ParseException, MalformedURLException {
        SequenceQueryParams sequenceQueryParams = null;
        for (LogEntry entry : entries) {
            if (entry.getMessage().contains("bundle")) {
                JSONObject json = (JSONObject) new JSONParser().parse(entry.getMessage());
                JSONObject params = (JSONObject) ((JSONObject) json.get("message")).get("params");
                Object request = params.get("request");
                if (request != null) {
                    Object urlObject = ((JSONObject) request).get("url");
                    URL url = new URL((String) urlObject);

                    if (url.getQuery().contains("Seq=" + seq) || (seq.equals("*") && url.getQuery().contains("Seq=")))  {
                        sequenceQueryParams = new SequenceQueryParams();

                        String[] queryParams = url.getQuery().split("&");
                        for (String queryParam : queryParams) {
                            String name = queryParam.split("=")[0];
                            String value = queryParam.split("=")[1];
                            if (name.equals("OrgId")) {
                                sequenceQueryParams.setOrgId(value);
                            } else if (name.equals("UserId")) {
                                sequenceQueryParams.setUserId(value);
                            } else if (name.equals("SessionId")) {
                                sequenceQueryParams.setSessionId(value);
                            } else if (name.equals("PageId")) {
                                sequenceQueryParams.setPageId(value);
                            } else if (name.equals("Seq")) {
                                sequenceQueryParams.setSeq(value);
                            } else if (name.equals("PageStart")) {
                                sequenceQueryParams.setPageStart(value);
                            } else if (name.equals("PrevBundleTime")) {
                                sequenceQueryParams.setPrevBundleTime(value);
                            } else if (name.equals("LastActivity")) {
                                sequenceQueryParams.setLastActivity(value);
                            } else if (name.equals("IsNewSession")) {
                                sequenceQueryParams.setIsNewSession(value);
                            }
                        }
                    }
                }

            }
        }

        return sequenceQueryParams;
    }
}
