package cn.blockmc.Zao_hon.storage;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserInfo {

	private String name;
	private int chip;
	private String firstIP;
	private String lastIP;

}
