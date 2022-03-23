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

package com.jnngl.tqlang;

import java.io.File;
import java.io.IOException;

public class Test {

    public static void main(String[] args) throws IOException {
        CodeTree tree = Parser.generateCodeTree(new File("src/test.tq"));
        printTree(tree, "");
    }

    public static void printTree(Component c, String prefix) {
        System.out.println(prefix+c.getClass().getSimpleName());
        for(ChildComponent child : c.getChildComponents()) {
            printTree(child, prefix+c.getClass().getSimpleName()+"->");
        }
    }

}
