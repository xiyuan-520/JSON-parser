package com.xiyuan.util.json.parser;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import com.xiyuan.util.json.JsonLexer;
import com.xiyuan.util.json.JsonParser;

/***
 * 基本类型适配器，8种基本类型&字符串
 * 
 * @version v1.0.0 @author lgz 2019-8-28 新建与整理
 */
public final class BaseParser extends JsonParser implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    public BaseParser(JsonLexer lexer, int level)
    {
        super(lexer, level);
    }
    
    public String toString(Object obj)
    {
        if (obj == null)
            return null;
        
        if (obj instanceof Long)
        {// 对long型进行判断，超过JS支持的最大值时行字符串处理，防止JS数值不准
            long l = (Long) obj;
            if (l > JS_MAX_LONG_VALUE)
                return new StringBuilder().append(JsonLexer.DB_QUOTE).append(l).append(JsonLexer.DB_QUOTE).toString();
        }
        else if (obj instanceof String)
        {// 要对引号进行增加转义
            String str = JsonLexer.addEscapeChar((String) obj, JsonLexer.DB_QUOTE);
            return new StringBuilder().append(JsonLexer.DB_QUOTE).append(str).append(JsonLexer.DB_QUOTE).toString();
        }
        
        return String.valueOf(obj);
    }
    
    @Override
    public Object toObject(Class<?> cls)
    {
        
        switch (cls.getName().hashCode())
        {
            case JsonLexer.CONCURRENT_ATOMIC_ATOMIC_BOOLEAN:
                return new AtomicBoolean(booleanValue(lexer));
            case JsonLexer.CONCURRENT_ATOMIC_ATOMIC_INTEGER:
                return new AtomicInteger(intValue(lexer));
            case JsonLexer.CONCURRENT_ATOMIC_ATOMIC_LONG:
                return new AtomicLong(longValue(lexer));
            case JsonLexer.BOOLEAN:
                return booleanValue(lexer);
            case JsonLexer.BYTE:
                return byteValue(lexer);
            case JsonLexer.CHAR:
                return charValue(lexer);
            case JsonLexer.SHORT:
                return shortValue(lexer);
            case JsonLexer.INT:
                return intValue(lexer);
            case JsonLexer.LONG:
                return longValue(lexer);
            case JsonLexer.FLOAT:
                return floatValue(lexer);
            case JsonLexer.DOUBLE:
                return doubleValue(lexer);
            case JsonLexer.BOOLEAN_O:
                return booleanValue(lexer) ? Boolean.TRUE : Boolean.FALSE;
            case JsonLexer.BYTE_O:
                return Byte.valueOf(byteValue(lexer));
            case JsonLexer.CHAR_O:
                return Character.valueOf(charValue(lexer));
            case JsonLexer.SHORT_O:
                return Short.valueOf(shortValue(lexer));
            case JsonLexer.INT_O:
                return Integer.valueOf(intValue(lexer));
            case JsonLexer.LONG_O:
                return Long.valueOf(longValue(lexer));
            case JsonLexer.FLOAT_O:
                return Float.valueOf(floatValue(lexer));
            case JsonLexer.DOUBLE_O:
                return Double.valueOf(doubleValue(lexer));
            case JsonLexer.STRING:
                return stringValue(lexer);
            default:
                return null;
        }
    }
    
    public static boolean booleanValue(JsonLexer lexer)
    {
        String value = JsonLexer.removeStartEndQuotation(lexer.value());
        return Boolean.parseBoolean(value) ? true : ("1".equals(value) ? true : false);
    }
    
    public static byte byteValue(JsonLexer lexer)
    {
        return Byte.parseByte((lexer.prevIsColon() && lexer.ch() == JsonLexer.COMMA) ? JsonLexer.COMMA_S : JsonLexer.removeStartEndQuotation(lexer.value()));
    }
    
    public static char charValue(JsonLexer lexer)
    {
        String value = JsonLexer.removeStartEndQuotation(lexer.value());
        return value == null ? (char) 0 : (((lexer.prevIsColon() && lexer.novalue()) ? (char) 0 : JsonLexer.removeStartEndQuotation(lexer.value()).charAt(0)));
    }
    
    public static short shortValue(JsonLexer lexer)
    {
        return Short.parseShort((lexer.prevIsColon() && lexer.novalue()) ? JsonLexer.COMMA_S : JsonLexer.removeStartEndQuotation(lexer.value()));
    }
    
    public static int intValue(JsonLexer lexer)
    {
        return Integer.parseInt((lexer.prevIsColon() && lexer.novalue()) ? JsonLexer.COMMA_S : JsonLexer.removeStartEndQuotation(lexer.value()));
    }
    
    public static long longValue(JsonLexer lexer)
    {
        return Long.parseLong((lexer.prevIsColon() && lexer.novalue()) ? JsonLexer.COMMA_S : JsonLexer.removeStartEndQuotation(lexer.value()));
    }
    
    public static float floatValue(JsonLexer lexer)
    {
        return Float.parseFloat((lexer.prevIsColon() && lexer.novalue()) ? JsonLexer.COMMA_S : JsonLexer.removeStartEndQuotation(lexer.value()));
    }
    
    public static double doubleValue(JsonLexer lexer)
    {
        return Double.parseDouble((lexer.prevIsColon() && lexer.novalue()) ? JsonLexer.COMMA_S : JsonLexer.removeStartEndQuotation((lexer.value() == null) ? null : lexer.value()));
    }
    
    public static String stringValue(JsonLexer lexer)
    {
        // 去掉前后可能的引号
        if (lexer.curType() == JsonLexer.T_BRACE_L || lexer.curType() == JsonLexer.T_BRACKET_L)
        {// 对象行 json字符串
            StringBuilder sb = new StringBuilder(lexer.value());
            int scope = lexer.scope();
            while (lexer.hasNext())
            {
                lexer.naxtToken();
                String v = lexer.value();
                if(lexer.curType() == JsonLexer.T_STRING)
                {//如果是String 值则需要剔除转义符
                    v = removeEscapeChar(v, true);
                }
                sb.append(v);
                if (lexer.scope() < scope || lexer.isEOF())
                    break;// 碰到结束符
            }
            
            return sb.toString();
        }
        else
        {
            // 去除引号&字符串要求删除转义 然后返回
            return removeEscapeChar(JsonLexer.removeStartEndQuotation((lexer.value() == null) ? null : lexer.value()), false);
        }
    }
    
    /***
     * 去除JSON中的转义字符
     * 
     * @param str 原字符串
     * @return 去除成对引号之后的字符串
     */
    public static String removeEscapeChar(String str, boolean isJsonString)
    {
        if(isJsonString)
            return str;
        
        if (str == null)
            return null;
        
        StringBuilder strb = new StringBuilder(str.length());
        boolean isEscape = false;// 是否前一字符是转义字符
        for (int i = 0; i < str.length(); i++)
        {
            char c = str.charAt(i);
            if (!isEscape)
            {// 未转义
                if (c == '\\')
                    isEscape = true;// 设为有转义
                else
                    strb.append(c);
            }
            else
            {// 有转义
                switch (c)
                {
                    case '\\':
                        strb.append('\\');
                        break;
                    case 'b':
                        strb.append('\b');
                        break;
                    case 'f':
                        strb.append('\f');
                        break;
                    case 'n':
                        strb.append('\n');
                        break;
                    case 'r':
                        strb.append('\r');
                        break;
                    case 't':
                        strb.append('\t');
                        break;
                    case '/':
                    case '\'':
                    case '\"':
                    {
                        strb.append(c);
                        break;
                    }
                    default:
                        strb.append("\\").append(c);
                        break;// 如果未找到匹配,则返原值
                }
                isEscape = false;// 重置转义为结束
            }
        }
        
        if (isEscape)
        {// 最后一个字符是\
            strb.append("\\");
        }
        
        return strb.toString();
    }
}
