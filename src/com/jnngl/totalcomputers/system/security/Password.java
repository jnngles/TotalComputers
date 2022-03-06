/*
    Plugin for computers in vanilla minecraft!
    Copyright (C) 2022  JNNGL

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.jnngl.totalcomputers.system.security;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Objects;

/**
 * Class for encrypting passwords
 */
public class Password {

    /**
     * Encrypts password with PBKDF2WithHmacSHA512
     * @param password Password
     * @param _salt Salt. May be null
     * @return Encrypted Password
     */
    public static String hash(String password, byte[] _salt) {
        try {
            byte[] salt;
            if(_salt != null) salt = _salt;
            else {
                SecureRandom random = new SecureRandom();
                salt = new byte[512];
                random.nextBytes(salt);
            }

            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 1000, 512);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
            byte[] hash = skf.generateSecret(spec).getEncoded();
            return ""+toString(hash) +"$reg"+toString(salt);
        } catch(NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String toString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < bytes.length-1; i++) sb.append(bytes[i]).append("$");
        return sb.append(bytes[bytes.length-1]).toString();
    }

    private static byte[] fromString(String str) {
        String[] sBytes = str.split("\\$");
        byte[] bytes = new byte[sBytes.length];
        for(int i = 0; i < bytes.length; i++) bytes[i] = Byte.parseByte(sBytes[i]);
        return bytes;
    }

    /**
     * Encrypts password and compares it to hash
     * @param hash hash
     * @param password password
     * @return Whether password is correct or not
     */
    public static boolean equals(String hash, String password) {
        return Objects.equals(hash(password, fromString(hash.split("\\$reg")[1])), hash);
    }

}
