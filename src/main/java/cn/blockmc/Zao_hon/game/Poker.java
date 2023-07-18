package cn.blockmc.Zao_hon.game;

import java.util.Collections;
import java.util.LinkedList;

import cn.blockmc.Zao_hon.game.PokerCard.Decor;
import cn.blockmc.Zao_hon.game.PokerCard.Num;

public class Poker {
	private LinkedList<PokerCard> cards = new LinkedList<PokerCard>();
	public Poker() {
		for(Decor decor:Decor.values()) {
			for(Num num:Num.values()) {
				PokerCard card = new PokerCard(decor,num);
				cards.add(card);
			}
		}
		this.shuffle();
	}
	
	public void shuffle() {
		Collections.shuffle(cards);
	}
	
	public PokerCard popCard() {
		return cards.pop();
	}
}
