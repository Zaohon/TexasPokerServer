package cn.blockmc.Zao_hon.game;

public final class Option {
	private final OptionType type;
	private final int pot;
	
	public Option(OptionType type,int pot) {
		this.type = type;
		this.pot = pot;
	}
	
	public Option(OptionType type) {
		this(type,0);
	}
	
	public OptionType getType() {
		return type;
	}
	public int getPot() {
		return pot;
	}

}
