package org.seasar.s2click.example.page;

import java.util.ArrayList;
import java.util.List;

import org.seasar.s2click.S2ClickPage;
import org.seasar.s2click.control.GreyboxResultLink;

/**
 *
 * @author Naoki Takezoe
 * @since 0.4.0
 */
public class GreyboxSelectLinkPage extends S2ClickPage {

	public GreyboxResultLink link = new GreyboxResultLink("link", "form_userId");

	public GreyboxSelectLinkPage(){
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
