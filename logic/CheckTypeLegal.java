package com.lux.trump.logic;

import java.util.ArrayList;


public class CheckTypeLegal extends RuleInfoDTO{
	SingleCardType singleCardType;
	CardComparator cardComparator;
	
	public CheckTypeLegal(RuleInfoDTO ruleInfoDTO){
		super(ruleInfoDTO);
		cardComparator = new CardComparator(ruleInfoDTO);
		singleCardType = new SingleCardType(primaryNumber, primaryColor);	
	}
	
	public boolean check(String[] basecard, String[] handcard, String[] playcard){
		CardType basetype = CardType.getType(
								this.getcardpriority(basecard), 
								this.cardtype);
		return this.check(basetype, basecard, handcard, playcard);
	}
	
	public boolean check(CardType basetype, String[] basecard, String[] handcard, String[] playcard){
		CardType[] cardtype = this.cardtype;
		CardType playtype = CardType.getType(this.getcardpriority(playcard), cardtype);
		
		//	System.out.println(playtype.name);
		if (basecard.length == 0){
			if (this.color_sensitive == true){
				for (int i=1; i<handcard.length; i++){
					if (this.singleCardType.typeequal(handcard[0],handcard[i]) == false){
						return false;
					}
				}
			}
			return true;
		}
		
		// maybe only for doudizhu
		if (this.order == true){
			if (playcard.length == 0)
				return true;
			if (playtype == null){
				System.out.println("no such card type");
				return false;	
			}
			if (playtype.isSuper == false){
				if (!basetype.name.equals(playtype.name) || basecard.length != playcard.length){
					System.out.println("type is different");
					return false;
				}
				if (cardComparator.isBigThan(basetype,basecard, playtype, playcard, basetype, basecard) == false){
					System.out.println("smaller than base card");
					return false;
				}
			}else {
				if (cardComparator.isBigThan(basetype,basecard, playtype, playcard, basetype, basecard) == false){
					System.out.println("smaller than base card");
					return false;
				}
			}
		}
		
		// only for shengji

		if (this.num_consistency == true && basecard.length != playcard.length){
			return false;
		}
		
		if (this.color_consistency == true){
			int handnum = 0;
			int playnum = 0;
			for (int i=0; i<playcard.length; i++){
				if (singleCardType.typeequal(playcard[i], basecard[0]) == true){
					playnum++;
				}
			}
			for (int i=0; i<handcard.length; i++){
				if (singleCardType.typeequal(handcard[i], basecard[0]) == true){
					handnum++;
				}
			}
		//	System.out.println(handnum+" "+playnum);
			if (playnum < basecard.length && handnum > playnum)
				return false;
		}
		
		if (this.strong_type_consistency == true ){
			ArrayList<String> samecolorcard =  new ArrayList<String>();
			for (String c:handcard){
				if (singleCardType.typeequal(c, basecard[0])){
					samecolorcard.add(c);
				}
			}
			String[] string = new String[samecolorcard.size()];
			samecolorcard.toArray(string);
			int[] samecard= getcardpriority(string);  

			ComplexType composeOfBasecard = new ComplexType(this.getcardpriority(basecard), cardtype);
			ComplexType composeOfHandcard = new ComplexType(samecard, cardtype);
			ComplexType composeOfPlaycard = new ComplexType(this.getcardpriority(playcard), cardtype);
			
			//check double_flush
			if (composeOfBasecard.flush_num > composeOfPlaycard.flush_num
			 && composeOfHandcard.flush_num > composeOfPlaycard.flush_num){
				System.out.println("need play more flush");
				return false;
			}
			//check double
			if (composeOfBasecard.double_num > composeOfPlaycard.double_num &&
				composeOfHandcard.double_num > composeOfPlaycard.double_num)
			{
				System.out.println("need play more double");
				return false;
			}
			//check single
			if (composeOfBasecard.single_num > composeOfPlaycard.single_num &&
				composeOfHandcard.single_num > composeOfPlaycard.single_num){
				System.out.println("need play more single");
				return false;
			}
		}
		return true;
	}

	boolean hastype(int[] handcard, CardType type, int length){
		if (handcard.length < length)
			return false;
		
		for (int i=0; i<handcard.length - length; i++){
			int []s1 = new int[length];
			for (int j=0; j<length; j++){
				s1[j] = handcard[i+j];
			}
			if (type.isMatch(s1))
				return true;
		}
			
		return false;
	}
}