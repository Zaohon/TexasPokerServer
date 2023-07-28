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
	
	public static Option makeOption(OptionType type,String str) {
		if(type==OptionType.FOLD) {
			return new Option(type,0);
		}else {
			try {
				if(str==null) {
					return null;
				}
				int i = Integer.parseInt(str);
				return new Option(type,i);
			}catch(NumberFormatException e) {
				return null;
			}
		}
	}
}
