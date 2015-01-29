package com.lux.trump.shared;

import java.io.Serializable;

public class GameAction implements Serializable {
	/* 
	 **********************server -> client*********************
	 type = "setXcard" content = "card"
	 type = "new_procedure" content = "" 表示进入一个新的procedure

	 <Deal>
	 type = "audit_in_deal" content = "username card_num color"
	 type = "deal" content = "username card;card;...;card visible"
	 type = "set_primary_color" content = "color"
	 type = "set_role" content = "username role"

	 <Ending>
	 type = "restart" content = ""

	 <Audit>
	 type = "audit_turn" content = "username"
	 type = "audit" content = "username key"
	 type = "set_wager" content = "wager"
	 type = "set_role" content = "username role"

	 <Discard>
	 type = "discard_turn" content = "username card_num"
	 type = "discard" content = "username card;card;...;card deck visible"

	 <Play>
	 type = "play_turn" content = "username"
	 type = "play_discard" content = "username"

	 <Wager>
	 type = "wager" content = "wager;wager;wager;wager"

	 ********************client -> server*************************
	 <Deal>
	 type = "audit_in_deal" content = "username card_num color"
	 type = "deal_finish" content = "username"

	 <Ending>

	 <Audit>
	 type = "audit" content = "username key"

	 <Discard>
	 type = "discard" content = "username card;card;...;card"

	 <Play>
	 type = "play_discard" content = "username"
	 type = "play" content = "username card;card;...;card"
	 
	 */
	private static final long serialVersionUID = 63086100641335393L;
	public String type;
	public String content;
	public Boolean[] userKnow;
	
	public GameAction() {
			
	}
	
	public GameAction(String type, String content, int userNumber) {
		this.type = type;
		this.content = content;
		userKnow = new Boolean[userNumber];
		for (int i = 0;i < userNumber;i ++)
			userKnow[i] = false;
	}
}

