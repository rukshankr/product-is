/*
 * Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.iam.userportal.actionobject;

import org.openqa.selenium.WebDriver;
import org.wso2.carbon.iam.userportal.pageobject.LoginPageP;

/**
 * Login page action for generated page objects using chrome plugin.
 */
public class LoginPageActionP extends LoginPageP {

    public LoginPageActionP(WebDriver driver) {
        super(driver);
    }

    public boolean login(String username, String password) {
        boolean result = false;
        getUsername1().sendKeys(username);
        getPassword1().sendKeys(password);
        getSignIn().click();

        result = true;
//        try {
//            getUsername1().sendKeys(username);
//            getPassword1().sendKeys(password);
//            getSignIn().click();
//
//            result = true;
//
//        } catch (Exception e) {
//            System.out.print(e.getMessage());
//            result = false;
//        }
        return result;
    }

    public boolean clickPassword2Link() {
        getPassword2().click();
        return true;
    }


}
