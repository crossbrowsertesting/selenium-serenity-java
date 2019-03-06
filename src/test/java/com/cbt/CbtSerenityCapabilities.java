package com.cbt;

import java.util.Map;
import java.util.HashMap;
import java.util.Properties;
import java.util.Iterator;
import java.net.URL;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import net.thucydides.core.util.EnvironmentVariables;
import net.thucydides.core.util.SystemEnvironmentVariables;

public class CbtSerenityCapabilities{
    DesiredCapabilities capabilities = new DesiredCapabilities();

    public DesiredCapabilities newCaps() {
        EnvironmentVariables environmentVariables = SystemEnvironmentVariables.createEnvironmentVariables();


        String environment = System.getProperty("environment");
        
        Iterator it = environmentVariables.getKeys().iterator();
        while (it.hasNext()) {
            String key = (String) it.next();

            if (environment != null && key.startsWith("environment." + environment)) {
                capabilities.setCapability(key.replace("environment." + environment + ".", ""), environmentVariables.getProperty(key));
            }
        }

            System.out.println(capabilities);
            return capabilities;
        
    }

    public boolean takesScreenshots() {
        return true;
    }
}
