/*
 * Copyright 2006-2009 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, 
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.s2click.example.page;

import java.util.ArrayList;
import java.util.List;

import org.seasar.s2click.S2ClickPage;

/**
 *
 * @author Naoki Takezoe
 * @since 0.4.0
 */
public class GreyboxSelectPage extends S2ClickPage {

	public GreyboxSelectPage(){
		List<UserInfo> userList = new ArrayList<UserInfo>();
		userList.add(new UserInfo("00001", "山田 一郎"));
		userList.add(new UserInfo("00002", "山田 次郎"));
		userList.add(new UserInfo("00003", "山田 三郎"));
		userList.add(new UserInfo("00004", "山田 四郎"));
		userList.add(new UserInfo("00005", "山田 五郎"));

		addModel("userList", userList);
	}

	public static class UserInfo {

		public String userId;
		public String userName;

		public UserInfo(String userId, String userName){
			this.userId = userId;
			this.userName = userName;
		}
	}

}
