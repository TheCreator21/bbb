import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.URL;



public class tes {

//    private static final String ACCESS_KEY = "eyJ4cC51Ijo5MiwieHAucCI6MSwieHAubSI6Ik1UVTNNekEwTWpBd01EazBNdyIsImFsZyI6IkhTMjU2In0.eyJleHAiOjE4ODg0MDIwMDAsImlzcyI6ImNvbS5leHBlcml0ZXN0In0.VF6gM49OxiYk3zhGwpMf8fO5OdGlFod-Ls_mKchxha0";
    private static final String ACCESS_KEY = "eyJ4cC51IjoyOTM0ODUzLCJ4cC5wIjoyOTM0ODUyLCJ4cC5tIjoiTVRVM01qZzNORE00TWpJd01BIiwiYWxnIjoiSFMyNTYifQ.eyJleHAiOjE4ODgyMzQzODIsImlzcyI6ImNvbS5leHBlcml0ZXN0In0.VJ82Hor4QI7qor53JhV5YxdxsPFC0I4T1oYyM21Ev64";//qacloud
    private RemoteWebDriver driver;
    private URL url;
    private DesiredCapabilities dc = new DesiredCapabilities();
    private long timeStamp;
    @Before
    public void setUp() throws Exception {

        url = new URL("https://qacloud.experitest.com/wd/hub");
        dc.setCapability(CapabilityType.BROWSER_NAME, BrowserType.FIREFOX);

        dc.setCapability(CapabilityType.PLATFORM, Platform.ANY);
        dc.setCapability("accessKey", ACCESS_KEY);
        dc.setCapability("testName", "Quick Start Firefox Browser Demo");
        driver = new RemoteWebDriver(url, dc);
    }


    @Test
    public void browserTestGoogleSearch() {
        timeStamp = System.currentTimeMillis();
        String filePath = createCSV();
        clickToTable();
        parseTable(filePath);

    }

    private void clickToTable(){
        driver.get("https://www.premierleague.com/tables");
        try{
            Thread.sleep(2000);
        }
        catch (InterruptedException iex){}
        WebElement cookie = driver.findElementByXPath("/html/body/section/div/div");
        cookie.click();
    }

    public String createCSV(){
        long milis = System.currentTimeMillis();
        String ret = milis +".csv";
        File f = new File("CSV/"+ret);
        System.out.println(f.getAbsolutePath());
        return ret;
    }

    private void parseTable(String filePath){
        WebElement headerRow = driver.findElementByXPath("/html/body/main/div[2]/div[1]/div[4]/div/div/div/table/thead/tr");
        String [] splited = headerRow.getText().split("\\r?\\n");
        String temp[] = splited[7].split("\\s+");
        String head = "";
        int j=0;
        for(int i=1;i<splited.length;i++){
            if(i==7){
                head+=temp[0]+","+temp[1]+","+temp[2]+",";
            }
            else{
                head+=splited[i]+",";
            }
        }
        System.out.println(head);
        try (PrintWriter output = new PrintWriter(new FileWriter(timeStamp+".csv", true))) {

            output.printf("%s\r\n", head);
        }
        catch(Exception e) {}
        //////

        for(int i=1;i<=40;i++){
            if(i%2==1) {
                WebElement rowele = driver.findElementByXPath("/html/body/main/div[2]/div[1]/div[4]/div/div/div/table/tbody/tr[" + i + "]");
                String s = rowElementToString(rowele);
                //modify the file
                try (PrintWriter output = new PrintWriter(new FileWriter(timeStamp+".csv", true))) {
                    output.printf("%s\r\n", s);
                }
                catch (Exception e) {}
            }
        }
    }
    private  String rowElementToString(WebElement row){

        String rowString="";
        String [] splited = row.getText().split("\\r?\\n");
        String [] temp = splited[2].split("\\s+");
//        for (String s:temp) System.out.println(s);
        String matches = temp[temp.length-1]+splited[3]+splited[4]+splited[5]+splited[6];

        String tempString="";
        for(int i=0;i<temp.length-1;i++) {
            if (i == temp.length - 2) {
                tempString += temp[i];
            }
            else {
                tempString += temp[i] + ",";
            }
        }
        for(int i=0;i<splited.length;i++){
            if(i==3) {
                rowString += tempString + ","+matches+",";

            }
            else{
                if(i<2||i>6) {
                    rowString += splited[i]+",";
                }
            }
        }
        System.out.println(rowString);
        return rowString;
    }

    @After
    public void tearDown() {
        driver.quit();
    }

}