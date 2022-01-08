/*
    Computers are now in minecraft!
    Copyright (C) 2021  JNNGL

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
 * Localization interface.
 */
public interface Localization {

    /**
     * Computer name
     * @return Localization
     */
    String computerName();

    /**
     * Create a Computer account
     * @return Localization
     */
    String createAComputerAccount();

    /**
     * Password
     * @return Localization
     */
    String password();

    /**
     * Require password
     * @return Localization
     */
    String requirePassword();

    /**
     * Do not require password
     * @return Localization
     */
    String doNotRequirePassword();

    /**
     * Next
     * @return Localization
     */
    String next();

    /**
     * Back
     * @return Localization
     */
    String back();

    /**
     * Computer name cannot be empty
     * @return Localization
     */
    String computerNameCannotBeEmpty();

    /**
     * Password field is empty
     * @return Localization
     */
    String passwordFieldIsEmpty();

    /**
     * Administrator rights are required to continue
     * @return Localization
     */
    String administratorRightsAreRequiredToContinue();

    /**
     * You do not have administrator rights
     * @return Localization
     */
    String youDoNotHaveAdministratorRights();

    /**
     * Setup complete
     * @return Localization
     */
    String setupComplete();

    /**
     * Restart required
     * @return Localization
     */
    String restartRequired();

    /**
     * Restart now
     * @return Localization
     */
    String restartNow();

    /**
     * Sign In
     * @return Localization
     */
    String signIn();

    /**
     * Wrong password
     * @return Localization
     */
    String wrongPassword();

    /**
     * English localization
     */
    class English implements Localization {

        @Override
        public String computerName() {
            return "Computer name";
        }

        @Override
        public String createAComputerAccount() {
            return "Create a Computer account";
        }

        @Override
        public String password() {
            return "Password";
        }

        @Override
        public String requirePassword() {
            return "Require password";
        }

        @Override
        public String doNotRequirePassword() {
            return "Do not require password";
        }

        @Override
        public String next() {
            return "Next";
        }

        @Override
        public String back() {
            return "Back";
        }

        @Override
        public String computerNameCannotBeEmpty() {
            return "Computer name cannot be empty!";
        }

        @Override
        public String passwordFieldIsEmpty() {
            return "Password field is empty!";
        }

        @Override
        public String administratorRightsAreRequiredToContinue() {
            return "Administrator rights are required to continue.";
        }

        @Override
        public String youDoNotHaveAdministratorRights() {
            return "You do not have administrator rights!";
        }

        @Override
        public String setupComplete() {
            return "Setup complete";
        }

        @Override
        public String restartRequired() {
            return "Restart required";
        }

        @Override
        public String restartNow() {
            return "Restart now";
        }

        @Override
        public String signIn() {
            return "Sign in";
        }

        @Override
        public String wrongPassword() {
            return "Wrong password!";
        }
    }

    /**
     * Russian localization
     */
    class Russian implements Localization {

        @Override
        public String computerName() {
            return "Название компьютера";
        }

        @Override
        public String createAComputerAccount() {
            return "Заполните некоторые данные";
        }

        @Override
        public String password() {
            return "Пароль";
        }

        @Override
        public String requirePassword() {
            return "Требовать пароль";
        }

        @Override
        public String doNotRequirePassword() {
            return "Не требовать пароль";
        }

        @Override
        public String next() {
            return "Далее";
        }

        @Override
        public String back() {
            return "Назад";
        }

        @Override
        public String computerNameCannotBeEmpty() {
            return "Название компьютера не может быть пустым.";
        }

        @Override
        public String passwordFieldIsEmpty() {
            return "Поле пароля пустое!";
        }

        @Override
        public String administratorRightsAreRequiredToContinue() {
            return "Требуются права администратора, чтобы продолжить.";
        }

        @Override
        public String youDoNotHaveAdministratorRights() {
            return "У вас нет прав администратора!";
        }

        @Override
        public String setupComplete() {
            return "Настройка завершена";
        }

        @Override
        public String restartRequired() {
            return "Требуется перезагрузка";
        }

        @Override
        public String restartNow() {
            return "Перезагрузить";
        }

        @Override
        public String signIn() {
            return "Войти";
        }

        @Override
        public String wrongPassword() {
            return "Неправильный пароль.";
        }
    }

}
