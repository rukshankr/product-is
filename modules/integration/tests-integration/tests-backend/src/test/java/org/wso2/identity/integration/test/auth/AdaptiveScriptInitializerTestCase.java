/*
 *  Copyright (c) 2022 WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.identity.integration.test.auth;

import java.io.File;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.wso2.carbon.automation.extensions.servers.utils.ServerLogReader;
import org.wso2.carbon.integration.common.utils.exceptions.AutomationUtilException;
import org.wso2.carbon.integration.common.utils.mgt.ServerConfigurationManager;

/**
 * Initiation Test for adaptive authentication.
 */
public class AdaptiveScriptInitializerTestCase extends AbstractAdaptiveAuthenticationTestCase {

    private ServerConfigurationManager serverConfigurationManager;

    @BeforeTest(alwaysRun = true)
    public void testInit() throws Exception {

        super.init();
        serverConfigurationManager = new ServerConfigurationManager(isServer);
        runAdaptiveAuthenticationDependencyScript();
    }

    private void runAdaptiveAuthenticationDependencyScript() {

        ServerLogReader inputStreamHandler;
        ServerLogReader errorStreamHandler;
        String targetFolder = System.getProperty("carbon.home");
        String scriptFolder =  getTestArtifactLocation() + File.separator;
        Process tempProcess = null;
        File scriptFile = new File(scriptFolder);
        Runtime runtime = Runtime.getRuntime();

        try {
            if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                log.warn("Operating System is Windows, skipping execution");
            } else {
                log.info("Operating system is not windows. Executing shell script");
                tempProcess = runtime.getRuntime().exec(
                        new String[] { "/bin/bash", "enableadaptive.sh", targetFolder }, null, scriptFile);
                errorStreamHandler = new ServerLogReader("errorStream",
                        tempProcess.getErrorStream());
                inputStreamHandler = new ServerLogReader("inputStream",
                        tempProcess.getInputStream());
                inputStreamHandler.start();
                errorStreamHandler.start();
                boolean runStatus = waitForMessage(inputStreamHandler, "Enable Adaptive Script successfully finished.");
                log.info("Status Message : " + runStatus);
                restartServer();
            }
        } catch (Exception e) {
            log.error("Failed to execute adaptive authentication dependency script", e);
        } finally {
            if (tempProcess != null) {
                tempProcess.destroy();
            }
        }
    }

    private void restartServer() throws AutomationUtilException {

        serverConfigurationManager.restartGracefully();
    }

    private boolean waitForMessage(ServerLogReader inputStreamHandler,
                                  String message) {
        long time = System.currentTimeMillis() + 60 * 1000;
        while (System.currentTimeMillis() < time) {
            if (inputStreamHandler.getOutput().contains(message)) {
                return true;
            }
        }
        return false;
    }

    @AfterTest(alwaysRun = true)
    public void resetUserstoreConfig() throws Exception {

        super.init();
        // TODO: delete - downloaded jars
        serverConfigurationManager.restoreToLastConfiguration(false);
    }
}