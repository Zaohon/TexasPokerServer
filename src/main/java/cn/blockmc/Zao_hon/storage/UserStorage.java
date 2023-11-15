package cn.blockmc.Zao_hon.storage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;

public class UserStorage {

	public UserInfo loadUser(String name) {
		File file = new File(this.getUserFolder(), name + ".json");
		UserInfo info = null;
		if (!file.exists()) {
			info = new UserInfo(name, 1000, "127.0.0.1", "127.0.0.1");
			this.saveUser(info);

		} else {
			Gson gson = new Gson(); // 创建Gson对象
			try (Reader reader = new FileReader(file);) {
				info = gson.fromJson(reader, UserInfo.class); // 将json文件内容转换成Java对象
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return info;
	}

	public void saveUser(UserInfo info) {
		File file = new File(this.getUserFolder(), info.getName() + ".json");
		Gson gson = new Gson();
		try (FileOutputStream fos = new FileOutputStream(file);) {
			file.createNewFile();
			String jsonString = gson.toJson(info);
			fos.write(jsonString.getBytes());
		} catch (JsonIOException | IOException e) {
			e.printStackTrace();
		}
	}

	private File getUserFolder() {
		File file = new File("users");
		if (!file.exists()) {
			file.mkdir();
		}
		return file;
	}

	private static UserStorage instance = new UserStorage();

	public static UserStorage get() {
		return instance;
	}

}
