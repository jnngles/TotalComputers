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
import java.nio.file.Files;
import java.util.Vector;

public class Parser {

    private enum CharType {
        L_BRACKET('['),
        R_BRACKET(']'),
        L_PARENTHESIS('('),
        R_PARENTHESIS(')'),
        L_BRACE('{'),
        R_BRACE('}'),
        COMMA(','),
        DOT('.'),
        HYPHEN('-'),
        PLUS('+'),
        EQUAL('='),
        QUOTE('"'),
        SEMICOLON(';'),
        LESS_THEN('<'),
        GREATER_THEN('>'),
        COLON(':'),
        NUMBER(new char[] {'0','1','2','3','4','5','6','7','8','9'}),
        SEPARATOR(new char[] {' ','\n'}),
        SYMBOL;

        char[] c;
        CharType() { this.c = new char[] {}; }
        CharType(char c) { this.c = new char[]{c}; }
        CharType(char[] c) { this.c = c; }
    }

    private static CharType getCharType(char c) {
        for(CharType type : CharType.values()) {
            for(char probe : type.c) {
                if(probe == c) return type;
            }
        }
        return CharType.SYMBOL;
    }

    private static class AssignedString  {
        public String str;
        public CharType type;

        public AssignedString(String str, CharType type) {
            this.str = str;
            this.type = type;
        }

    }

    public static CodeTree generateCodeTree(File sourceFile) throws IOException {
        CodeTree tree = new CodeTree();

        String sourceCode = Files.readString(sourceFile.toPath()).replace("\r\n", "\n");
        char[] chars = sourceCode.toCharArray();

        Vector<AssignedString> parsed = new Vector<>();
        StringBuilder buff = new StringBuilder();
        CharType prevType = null;
        for(char c : chars) {
            CharType type = getCharType(c);
            if(prevType != null) {
                if(prevType == CharType.SYMBOL && type == CharType.NUMBER) type = CharType.SYMBOL;
            }
            if(prevType == null) {
                prevType = type;
            }
            if(prevType != CharType.SEPARATOR && prevType != type) {
                parsed.addElement(new AssignedString(buff.toString(), prevType));
                buff = new StringBuilder();
            }
            if(type != CharType.SEPARATOR) {
                buff.append(c);
            }
            prevType = type;
        }

        Component currentScope = tree;
        Vector<CharType> expected = new Vector<>();
        expected.addElement(CharType.SYMBOL);
        int iteration = 0, sub_iteration = 0;

        enum _type {
            Function,
            Variable,
            CodeBlock,
            Expression
        }

        _type task = null;
        _type sub_task = null;
        Object tmp = null;

        for (int i = 0; i < parsed.size(); i++) {
            AssignedString as = parsed.get(i);
            if (!expected.contains(as.type)) {
                System.out.println("Unexpected `"+as.str+"'.");
            }

            if(as.str.equals("function")) {
                expected.clear();
                expected.addElement(CharType.SYMBOL);
                iteration++;
                task = _type.Function;
                tmp = new Method();
                continue;
            }
            if(task == _type.Function) {
                if(iteration == 1) {
                    ((Method)tmp).name = as.str;
                    expected.clear();
                    expected.addElement(CharType.L_PARENTHESIS);
                    iteration++;
                    continue;
                }
                if(iteration == 2) {
                    expected.clear();
                    expected.addElement(CharType.SYMBOL);
                    expected.addElement(CharType.R_PARENTHESIS);
                    iteration++;
                    continue;
                }
                if(iteration == 3) {
                    if(as.type == CharType.R_PARENTHESIS) {
                        expected.clear();
                        expected.addElement(CharType.SYMBOL);
                        expected.addElement(CharType.L_BRACE);
                    } else {
                        expected.clear();
                        expected.addElement(CharType.SYMBOL);
                    }
                    iteration++;
                    continue;
                }
                if(iteration == 4) {
                    if(as.type == CharType.L_BRACE) {
                        currentScope.addChild((Method) tmp);
                        currentScope = (Method)tmp;
                        iteration = 0;
                        sub_iteration = 0;
                    }
                    else {
                        if(as.str.equals("returns")) {
                            expected.clear();
                            expected.addElement(CharType.SYMBOL);
                            iteration++;
                            continue;
                        }
                        else {
                            sub_iteration++;
                            if(sub_iteration % 2 == 0) {
                                Parameter param = new Parameter();
                                param.type = Type.fromString(parsed.get(i-1).str);
                                param.name = as.str;
                                ((Method)tmp).parameters.addElement(param);
                            }
                            continue;
                        }
                    }
                }
                if(iteration == 5) {
                    ((Method)tmp).returnType = Type.fromString(as.str);
                    expected.clear();
                    expected.addElement(CharType.L_BRACE);
                    iteration = 0;
                    sub_iteration = 0;
                }
            }

            if(as.type == CharType.L_BRACE) {
                tmp = new CodeBlock();
                currentScope.addChild((CodeBlock)tmp);
                currentScope = (CodeBlock)tmp;
                expected.clear();
                expected.addElement(CharType.SYMBOL);
                task = _type.CodeBlock;
                iteration = 0;
                sub_iteration = 0;
                continue;
            }
            if(as.type == CharType.R_BRACE) {
                if(task != _type.CodeBlock) System.out.println("Unexpected '}'. (Nothing to close)");
                currentScope = ((ChildComponent)currentScope).getParent();
                expected.clear();
                expected.addElement(CharType.SYMBOL);
                iteration = 0;
                sub_iteration = 0;
                continue;
            }

            if(as.str.equals("var")) {
                tmp = new Field();
                currentScope.addChild((Field)tmp);
                currentScope = (Field)tmp;
                expected.clear();
                expected.addElement(CharType.SYMBOL);
                task = _type.Variable;
                iteration++;
                continue;
            }
            if(task == _type.Variable) {
                if(iteration == 1) {
                    ((Field)tmp).type = Type.fromString(as.str);
                    expected.clear();
                    expected.addElement(CharType.SYMBOL);
                    iteration++;
                    continue;
                }
                if(iteration == 2) {
                    ((Field)tmp).name = as.str;
                    expected.clear();
                    expected.addElement(CharType.SEMICOLON);
                    expected.addElement(CharType.EQUAL);
                    iteration++;
                    continue;
                }
                if(iteration == 3) {
                    expected.clear();
                    if(as.type == CharType.SEMICOLON) {
                        currentScope.addChild(Type.getDefaultValueFor(((Field)tmp).type));
                        iteration = 5;
                    }
                    else {
                        expected.addElement(CharType.SYMBOL);
                        expected.addElement(CharType.NUMBER);
                        iteration++;
                        continue;
                    }
                }
                if(iteration == 4 && sub_task == null) {
                    sub_iteration = 0;
                    sub_task = _type.Expression;
                    continue;
                }
                if(iteration == 5) {
                    expected.addElement(CharType.SYMBOL);
                    currentScope = ((ChildComponent)currentScope).getParent();
                    sub_task = null;
                    iteration = 0;
                    sub_iteration = 0;
                }
            }
        }

        return tree;
    }

}
