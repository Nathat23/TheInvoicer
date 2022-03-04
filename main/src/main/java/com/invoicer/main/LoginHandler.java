package com.invoicer.main;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.security.SecureRandom;
import java.util.Random;

public class LoginHandler {

    private LoginConfig loginConfig;
    private Yaml yaml;
    private boolean newUser;

    private LoginConfig getLoginConfig() {
        return loginConfig;
    }

    public void init() throws IOException {
        File file = new File("login.yml");
        newUser = file.createNewFile() || loginConfig == null;
        yaml = new Yaml(new Constructor(LoginConfig.class));
        loginConfig = newUser ? new LoginConfig() : yaml.load(file.toURL().openStream());
        saveFile();
    }

    public boolean isNewUser() {
        return newUser || getLoginConfig().getUsername() == null;
    }

    public void saveFile() throws IOException {
        FileWriter writer = new FileWriter("login.yml");
        yaml.dump(loginConfig, writer);
    }

    public boolean isValid(String username, String input) {
        if (!username.equals(getLoginConfig().getUsername())) {
            return false;
        }
        return getLoginConfig().getHashedPassword().equals(hash(input + getLoginConfig().getSalt()));
    }

    public void hashAndStore(String username, String password) throws IOException {
        getLoginConfig().setUsername(username);
        getLoginConfig().setSalt(salt());
        getLoginConfig().setHashedPassword(hash(password + getLoginConfig().getSalt()));
        saveFile();
    }

    private String salt() {
        Random random = new SecureRandom();
        return String.valueOf(random.nextInt(999));
    }

    private String hash(String input) {
        StringBuilder hash = new StringBuilder();
        char[] chars = input.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            long nom = 0;
            for (int i1 = 1; i1 < chars.length; i1++) {
                nom += (chars[i] ^ 2) + (chars[i1] ^ 3);
            }
            int sumOfDigits = 0;
            for (char c : String.valueOf(nom).toCharArray()) {
                sumOfDigits += c;
            }
            hash.append(Integer.toHexString(sumOfDigits));
        }
        return hash.toString();
    }
}
