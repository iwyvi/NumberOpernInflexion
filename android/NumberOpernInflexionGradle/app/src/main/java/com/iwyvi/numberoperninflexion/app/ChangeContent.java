package com.iwyvi.numberoperninflexion.app;

import java.util.Iterator;
import java.util.Stack;

/**
 * Created by aaa on 2016/1/29 0029.
 */

public class ChangeContent
{
    private final String[] indexMap = { "#", "1", "2", "3", "4", "5", "6", "7", "(", ")", "[", "]", "（", "）", "【", "】", " " };
    private final String[] toneMap = { "1", "#1", "2", "#2", "3", "4", "#4", "5", "#5", "6", "#6", "7" };
    private Stack<String> textSymbol;
    private int tempHeight;

    public ChangeContent()
    {
        textSymbol = new Stack<String>();
        tempHeight = 0;
    }

    public final String change(String text, int mode)
    {
        if (mode == 0)
        {
            return text;
        }
        textSymbol.clear();
        tempHeight = 0;
        String[] line = text.split("[\\n]", -1);
        String result = "";
        for (int i = 0; i < line.length; i++)
        {
            result += lineChange(line[i], mode);
            if (i != line.length - 1)
            {
                result += "\n";
            }
        }
        return result;
    }

    private String lineChange(String line, int mode)
    {
        String result = "";
        for (int i = 0; i < line.length(); i++)
        {
            result += charChange(line.substring(i, i + 1), mode);
        }
        result += bracketMatch();
        return result;
    }

    private String charChange(String charIndex, int mode)
    {
        if (!hasString(indexMap,charIndex))
        {
            charIndex = bracketMatch() + charIndex;
            return charIndex;
        }
        String[] textSymbolArr;
        if (isInteger(charIndex))
        {
            String result = "";
            int index = 0;
            int height = 0;
            if (textSymbol.size() !=0 && textSymbol.peek().equals("#"))
            {
                textSymbol.pop();
                charIndex = "#" + charIndex;
            }
            textSymbolArr = toArray(textSymbol);
            for (int i = 0; i < textSymbolArr.length; i++)
            {
                if(textSymbolArr[i].equals("("))
                {
                    height -= 1;
                }
                else if (textSymbolArr[i].equals("["))
                {
                    height += 1;
                }
            }
            if (charIndex.equals("#3"))
            {
                charIndex = "4";
            }
            else if (charIndex.equals("#7"))
            {
                height += 1;
                charIndex = "1";
            }
            for (int i = 0; i < toneMap.length; i++)
            {
                if (toneMap[i].equals(charIndex))
                {
                    index = i;
                    break;
                }
            }
            index = index + mode;
            if (index >= toneMap.length)
            {
                index = index % toneMap.length;
                height += 1;
            }
            else if (index < 0)
            {
                index = toneMap.length + index;
                height -= 1;
            }
            if (height != tempHeight)
            {
                if (height > tempHeight)
                {
                    for (int i = tempHeight + 1; i <= height; i++)
                    {
                        if (i < 0)
                        {
                            result += ")";
                        }
                        else if (i > 0)
                        {
                            result += "[";
                        }
                        else
                        {
                            if (tempHeight == -1)
                            {
                                result += ")";
                            }
                            else if(tempHeight == 1)
                            {
                                result += "[";
                            }
                        }
                    }
                }
                else if (height < tempHeight)
                {
                    for (int i = tempHeight - 1; i >= height; i--)
                    {
                        if (i < 0)
                        {
                            result += "(";
                        }
                        else if (i > 0)
                        {
                            result += "]";
                        }
                        else
                        {
                            if (tempHeight == -1)
                            {
                                result += "(";
                            }
                            else if(tempHeight == 1)
                            {
                                result += "]";
                            }
                        }
                    }
                }
                tempHeight = height;
            }
            result += toneMap[index];
            return result;
        }

//C# TO JAVA CONVERTER NOTE: The following 'switch' operated on a string member and was converted to Java 'if-else' logic:
//		switch (charIndex)
//ORIGINAL LINE: case "#":
        if (charIndex.equals("#"))
        {
            if (textSymbol.empty() || !textSymbol.peek().equals("#"))
            {
                textSymbol.push("#");
            }
        }
//ORIGINAL LINE: case "(":
        else if (charIndex.equals("(") || charIndex.equals("（"))
        {
            if (textSymbol.empty() || textSymbol.peek().equals("#"))
            {
                if (textSymbol.size() != 0)
                {
                    textSymbol.pop();
                }
            }
            textSymbol.push("(");
        }
//ORIGINAL LINE: case ")":
        else if (charIndex.equals(")") || charIndex.equals("）"))
        {
            textSymbolArr = toArray(textSymbol);
            for (int i = textSymbolArr.length - 1; i >= 0; i--)
            {
                if (textSymbolArr[i].equals("("))
                {
                    while (textSymbol.size() != i)
                    {
                        textSymbol.pop();
                    }
                    break;
                }
            }
        }
//ORIGINAL LINE: case "[":
        else if (charIndex.equals("[") || charIndex.equals("【"))
        {
            if (textSymbol.empty() || textSymbol.peek().equals("#"))
            {
                if (textSymbol.size() != 0)
                {
                    textSymbol.pop();
                }
            }
            textSymbol.push("[");
        }
//ORIGINAL LINE: case "]":
        else if (charIndex.equals("]") || charIndex.equals("】"))
        {
            textSymbolArr = toArray(textSymbol);
            for (int i = textSymbolArr.length - 1; i >= 0; i--)
            {
                if (textSymbolArr[i].equals("["))
                {
                    while (textSymbol.size() != i)
                    {
                        textSymbol.pop();
                    }
                    break;
                }
            }
        }
        else
        {
            return bracketMatch() + charIndex;
        }
        return "";
    }

    private String bracketMatch()
    {
        String result = "";
        while (tempHeight != 0)
        {
            if (tempHeight > 0)
            {
                result += "]";
                tempHeight--;
            }
            else if (tempHeight < 0)
            {
                result += ")";
                tempHeight++;
            }
        }
        return result;
    }

    private static boolean isInteger(String value)
    {
        try
        {
            Integer.parseInt(value);
            return true;
        }
        catch (RuntimeException e)
        {
            return false;
        }
    }

    private static boolean hasString(String[] a,String b){
        for (int i = 0; i < a.length; i++){
            if(a[i].equals(b)){
                return true;
            }
        }
        return false;
    }

    private static String[] toArray(Stack<String> s){
        String[] result = new String[s.size()];
        int i = 0;
        for (Iterator iter = s.iterator(); iter.hasNext();) {
            result[i] = (String)iter.next();
            i++;
        }
        return result;
    }



}
