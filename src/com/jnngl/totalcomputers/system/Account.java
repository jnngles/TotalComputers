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

package com.jnngl.totalcomputers.system;

/**
 * Stores the information about user
 */
public class Account {

    /**
     * Account name
     */
    public final String name;

    /**
     * Encrypted password
     */
    public final String passwordHash;

    /**
     * Whether password is set
     */
    public final boolean usePassword;

    /**
     * Constructor
     * @param name Name of the computer account
     * @param passwordHash Encrypted password
     * @param usePassword Whether password is required to log in or not
     */
    public Account(String name, String passwordHash, boolean usePassword) {
        this.name = name;
        this.passwordHash = passwordHash;
        this.usePassword = usePassword;
    }

}
