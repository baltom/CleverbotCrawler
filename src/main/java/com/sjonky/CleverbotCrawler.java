package com.sjonky;

import org.openqa.selenium.chrome.ChromeDriver;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CleverbotCrawler {

    private static CleverbotPageObject cleverbotPageObject1;
    private static CleverbotPageObject cleverbotPageObject2;

    private static boolean tryToSetGeckoIfExists(String property, Path path) {
        if (Files.exists(path)) {
            System.setProperty(property, path.toAbsolutePath().toString());
            return true;
        }
        return false;
    }

    private static void setupDriverExecutable(String executableName, String property) {
        String homeDir = System.getProperty("user.home");

        //first try Linux/Mac executable
        if (!tryToSetGeckoIfExists(property, Paths.get(homeDir, executableName))) {
            //then check if on Windows
            if (!tryToSetGeckoIfExists(property, Paths.get(homeDir, executableName + ".exe"))) {
                throw new RuntimeException("Cannot locate the " + executableName + " in your home directory " + homeDir);
            }
        }
    }

    public static void main(String[] args) {
        setupDriverExecutable("chromedriver", "webdriver.chrome.driver");
        cleverbotPageObject1 = new CleverbotPageObject(new ChromeDriver());
        cleverbotPageObject2 = new CleverbotPageObject(new ChromeDriver());

        try {
            crawl();
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        }
        cleverbotPageObject1.close();
        cleverbotPageObject2.close();
    }

    private static void crawl() {
        String answerFromBotOne = cleverbotPageObject1.askAndGetResponse("Hello, whats up?");
        for (int i = 0; i < 500; i++) {
            String answerFromBotTwo = cleverbotPageObject2.askAndGetResponse(answerFromBotOne);
            answerFromBotOne = cleverbotPageObject1.askAndGetResponse(answerFromBotTwo);
        }
    }
}
