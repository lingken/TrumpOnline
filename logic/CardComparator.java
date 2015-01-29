package com.lux.trump.logic;


public class CardComparator extends RuleInfoDTO{
	SingleCardType singleCardType;
	public CardComparator(RuleInfoDTO ruleInfoDTO){
		super(ruleInfoDTO);
		singleCardType = new SingleCardType(this.primaryNumber, this.primaryColor);
	}
	
	public boolean isBigThan(String[] basecard, String[]s1, String[]s2){
		CardType basetype = CardType.getType(this.getcardpriority(basecard), this.cardtype);
		CardType type1 = CardType.getType(this.getcardpriority(s1), this.cardtype);
		CardType type2 = CardType.getType(this.getcardpriority(s2), this.cardtype);
		return this.isBigThan(basetype, basecard, type1, s1, type2, s2);
 	}
	
	public boolean isBigThan(CardType basetype, String[] basecard,  
			CardType type1, String[]s1, CardType type2, String[]s2)
	{
		if (this.color_consistency == true){
			boolean samecolor1 = true;
			boolean samecolor2 = true;
		
			for (String s:s1){
				if (!singleCardType.typeequal(s, s1[0])){
					samecolor1 = false;
					break;
				}
			}
			for (String s:s2){
				if (!singleCardType.typeequal(s, s2[0])){
					samecolor2 = false;
					break;
				}
			}
		//	System.out.println(samecolor1+" "+ samecolor2);
			/* check if two card groups have same color inside */
			if (samecolor1 == false){
				return false;
			}else if (samecolor2 == false){
				return true;
			}else {
				int a = getPriority(basetype, basecard, type1, s1);
				int b = getPriority(basetype, basecard, type2, s2);
			//	System.out.println(a);
			//	System.out.println(b);
				return a > b;
			}
		}else {
			int card1 = this.getPriority(basetype, basecard, type1, s1);
			int card2 = this.getPriority(basetype, basecard, type2, s2);
		//	System.out.println(card1+" "+card2);
			return card1 > card2;
		}
	}
	
	public int getPriority(CardType basetype, String[] basecard,
					CardType playtype, String[] playcard)
	{
		if (this.color_sensitive == false){
			if (playtype == null)
				return 0;
			if (playtype.isSuper == false && !playtype.name.equals(basetype.name)){
				return 0;
			}else{
				int [] card = this.getcardpriority(playcard);
				return card[0] + playtype.priority;
			}
		}
		else {
			if (basetype == null){
				ComplexType basecompose = new ComplexType(this.getcardpriority(basecard), this.cardtype);
				ComplexType playcompose = new ComplexType(this.getcardpriority(playcard), this.cardtype);
		//		basecompose.show();
		//		playcompose.show();
				if (basecompose.isSameType(playcompose)){
					if (singleCardType.type(playcard[0]).equals(this.primaryColor) ||
						singleCardType.typeequal(basecard[0], playcard[0]))
					{
						return playcompose.getPresent();
					}
					else {
						return 0;
					}
				}else{
					return 0;
				}
			}
			else {
				if (playtype !=null && basetype.name.equals(playtype.name) ){
					if (singleCardType.type(playcard[0]).equals(this.primaryColor) ||
							singleCardType.typeequal(basecard[0], playcard[0]))
					{
						int[] card = this.getcardpriority(playcard);
						return card[0];
						
					}
					else {
						return 0;
					}
				}else{
					return 0;
				}
			}
		}
	}
	//make sure that two card groups have same type
	public boolean isBigThan(String[]s1, String[] s2){
		int[] card1 = this.getcardpriority(s1);
		int[] card2 = this.getcardpriority(s2);
		
		return card1[0] > card2[0];
	}
}