/*
 * 版权所有 (C) 2015 知启蒙(ZHIQIM) 保留所有权利。
 * 
 * 指定登记&发行网站： https://www.zhiqim.com/ 欢迎加盟知启蒙，[编程有你，知启蒙一路随行]。
 *
 * 本文采用《知启蒙许可证》，除非符合许可证，否则不可使用该文件！
 * 1、您可以免费使用、修改、合并、出版发行和分发，再授权软件、软件副本及衍生软件；
 * 2、您用于商业用途时，必须在原作者指定的登记网站，按原作者要求进行登记；
 * 3、您在使用、修改、合并、出版发行和分发时，必须包含版权声明、许可声明，及保留原作者的著作权、商标和专利等知识产权；
 * 4、您在互联网、移动互联网等大众网络下发行和分发再授权软件、软件副本及衍生软件时，必须在原作者指定的发行网站进行发行和分发；
 * 5、您可以在以下链接获取一个完整的许可证副本。
 * 
 * 许可证链接：http://zhiqim.org/licenses/zhiqim_register_publish_license.htm
 * 
 * 除非法律需要或书面同意，软件由原始码方式提供，无任何明示或暗示的保证和条件。详见完整许可证的权限和限制。
 */
package com.xiyuan.util.json;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.xiyuan.util.json.parser.ArrayParser;
import com.xiyuan.util.json.parser.BaseParser;
import com.xiyuan.util.json.parser.DateParser;
import com.xiyuan.util.json.parser.ListParser;
import com.xiyuan.util.json.parser.MapParser;
import com.xiyuan.util.json.parser.ObjectParser;

/**
 * 
 * json 分析器
 * 
 * @version v1.0.0 @author lgz 2019-9-22 新建与整理
 */
public class JsonLexer
{

    /** 未知类型 0 token */
    public final static byte T_ZERO = 0;
    /** 左大括号类型 1 = { */
    public final static byte T_BRACE_L = 1;// "{"
    /** 右大括号类型 2 = } */
    public final static byte T_BRACE_R = 2;// "}"
    /** 左中括号类型 3 = [ */
    public final static byte T_BRACKET_L = 3;// "["
    /** 右中括号类型 4 = ] */
    public final static byte T_BRACKET_R = 4;// "]"
    /** 冒号类型 5 = : */
    public final static byte T_COLON = 5;// ":"
    /** 逗号类型 6 = , */
    public final static byte T_COMMA = 6;// ","
    /** 字符类型 7 */
    public final static byte T_STRING = 7;// String值
    /** 结束类型 8 = EOF **/
    public final static byte T_EOF = 8;// 结束

    /** 左大括号 { */
    public final static char BRACE_L = '{';
    /** 右大括号 } */
    public final static char BRACE_R = '}';
    /** 左中括号 [ */
    public final static char BRACKET_L = '[';
    /** 右中括号 ] */
    public final static char BRACKET_R = ']';
    /** 冒号 : */
    public final static char COLON = ':';
    /** 逗号 , */
    public final static char COMMA = ',';
    /** 单引号 ' */
    public final static char QUOTE = '\'';
    /** 双引号 " */
    public final static char DB_QUOTE = '"';
    /** 单引号 ' */
    public final static String QUOTE_S = String.valueOf(QUOTE);
    /** 双引号 " */
    public final static String DB_QUOTE_S = String.valueOf(DB_QUOTE);

    /** null " */
    public final static String NULL = "null";
    /** "true" */
    public final static String TRUE = "true";
    /** "false" */
    public final static String FALSE = "false";
    /** 空对象 {} */
    public final static String EMPTY_OBJ = "{}";
    /** 空数组 [] */
    public final static String EMPTY_ARR = "[]";

    /**************************************************/
    // 八大基本类型&封装类&数组&封装数组的class的哈希吗
    /**************************************************/

    /**************************************************/
    // 基本类型
    /**************************************************/
    /**
     * boolean.class.getName().hashCode()<br>
     * = 64711720;
     */
    public final static int BOOL_CLS_HASH = 64711720;// boolean.class.getName().hashCode();
    /**
     * Boolean.class.getName().hashCode()<br>
     * = 344809556
     */
    public final static int BOOL_OBJ_CLS_HASH = 344809556;// Boolean.class.getName().hashCode();
    /**
     * byte.class.getName().hashCode()<br>
     * = 3039496
     */
    public final static int BYTE_CLS_HASH = 3039496;// byte.class.getName().hashCode();
    /**
     * Byte.class.getName().hashCode()<br>
     * = 398507100
     */
    public final static int BYTE_OBJ_CLS_HASH = 398507100;// Byte.class.getName().hashCode();
    /**
     * char.class.getName().hashCode()<br>
     * = 3052374
     */
    public final static int CHAR_CLS_HASH = 3052374;// char.class.getName().hashCode();
    /**
     * Character.class.getName().hashCode()<br>
     * = 155276373
     */
    public final static int CHAR_OBJ_CLS_HASH = 155276373;// Character.class.getName().hashCode();
    /**
     * short.class.getName().hashCode()<br>
     * = 109413500
     */
    public final static int SHORT_CLS_HASH = 109413500;// short.class.getName().hashCode();
    /**
     * Short.class.getName().hashCode()<br>
     * = -515992664
     */
    public final static int SHORT_OBJ_CLS_HASH = -515992664;// Short.class.getName().hashCode();
    /**
     * int.class.getName().hashCode()<br>
     * = 104431
     */
    public final static int INT_CLS_HASH = 104431;// int.class.getName().hashCode();
    /**
     * Integer.class.getName().hashCode()<br>
     * = -2056817302
     */
    public final static int INT_OBJ_CLS_HASH = -2056817302;// Integer.class.getName().hashCode();
    /**
     * long.class.getName().hashCode()<br>
     * = 3327612
     */
    public final static int LONG_CLS_HASH = 3327612;// long.class.getName().hashCode();
    /**
     * Long.class.getName().hashCode()<br>
     * = 398795216
     */
    public final static int LONG_OBJ_CLS_HASH = 398795216;// Long.class.getName().hashCode();
    /**
     * float.class.getName().hashCode()<br>
     * = 97526364
     */
    public final static int FLOAT_CLS_HASH = 97526364;// float.class.getName().hashCode();
    /**
     * Float.class.getName().hashCode()<br>
     * = -527879800
     */
    public final static int FLOAT_OBJ_CLS_HASH = -527879800;// Float.class.getName().hashCode();
    /**
     * double.class.getName().hashCode()<br>
     * = -1325958191
     */
    public final static int DOUBLE_CLS_HASH = -1325958191;// double.class.getName().hashCode();
    /**
     * Double.class.getName().hashCode()<br>
     * = 761287205
     */
    public final static int DOUBLE_OBJ_CLS_HASH = 761287205;// Double.class.getName().hashCode();

    /**************************************************/
    // 基本类型[]
    /**************************************************/
    /**
     * double[].class.getName().hashCode()<br>
     * = 2889
     */
    public final static int DOUBLE_ARR_CLS_HASH = 2889;// double[].class.getName().hashCode();
    /**
     * Double[].class.getName().hashCode()<br>
     * = 175516795
     */
    public final static int DOUBLE_OBJ_ARR_CLS_HASH = -175516795;// Double[].class.getName().hashCode();
    /**
     * float[].class.getName().hashCode()<br>
     * = 2891
     */
    public final static int FLOAT_ARR_CLS_HASH = 2891;// float[].class.getName().hashCode();
    /**
     * Float[].class.getName().hashCode()<br>
     * = 48646404
     */
    public final static int FLOAT_OBJ_ARR_CLS_HASH = 48646404;// Float[].class.getName().hashCode();
    /**
     * long[].class.getName().hashCode()<br>
     * = 2895
     **/
    public final static int LONG_ARR_CLS_HASH = 2895;// long[].class.getName().hashCode();
    /**
     * Long[].class.getName().hashCode()<br>
     * = -2071011078
     */
    public final static int LONG_OBJ_ARR_CLS_HASH = -2071011078;// Long[].class.getName().hashCode();
    /**
     * int[].class.getName().hashCode()<br>
     * = 2894
     */
    public final static int INT_ARR_CLS_HASH = 2894;// int[].class.getName().hashCode();
    /**
     * Integer[].class.getName().hashCode()<br>
     * = -1935445726
     */
    public final static int INT_OBJ_ARR_CLS_HASH = -1935445726;// Integer[].class.getName().hashCode();
    /**
     * short[].class.getName().hashCode()<br>
     * = 2904
     */
    public final static int SHORT_ARR_CLS_HASH = 2904;// short[].class.getName().hashCode();
    /**
     * Short[].class.getName().hashCode()<br>
     * = 417147620
     */
    public final static int SHORT_OBJ_ARR_CLS_HASH = 417147620;// Short[].class.getName().hashCode();
    /**
     * char[].class.getName().hashCode()<br>
     * = 2888
     */
    public final static int CHAR_ARR_CLS_HASH = 2888;// char[].class.getName().hashCode();
    /**
     * Character[].class.getName().hashCode()<br>
     * = -1378128041
     */
    public final static int CHAR_OBJ_ARR_CLS_HASH = -1378128041;// Character[].class.getName().hashCode();
    /**
     * byte[].class.getName().hashCode()<br>
     * = 2887
     */
    public final static int BYTE_ARR_CLS_HASH = 2887;// byte[].class.getName().hashCode();
    /**
     * Byte[].class.getName().hashCode()<br>
     * = 2079942674
     */
    public final static int BYTE_OBJ_ARR_CLS_HASH = -2079942674;// Byte[].class.getName().hashCode();
    /**
     * boolean[].class.getName().hashCode()<br>
     * = 2911
     */
    public final static int BOOL_ARR_CLS_HASH = 2911;// boolean[].class.getName().hashCode();
    /**
     * Boolean[].class.getName().hashCode()<br>
     * = -499457160
     */
    public final static int BOOL_OBJ_ARR_CLS_HASH = -499457160;// Boolean[].class.getName().hashCode();

    /**************************************************/
    // String值相关
    /**************************************************/
    /**
     * String.class.getName().hashCode()<br>
     * = 1195259493
     */
    public final static int STRING_CLS_HASH = 1195259493;// String.class.getName().hashCode();
    /**
     * String[].class.getName().hashCode()<br>
     * = 392722245
     */
    public final static int STRING_ARR_CLS_HASH = 392722245;// String[].class.getName().hashCode();

    /**************************************************/
    // 哈希表
    /**************************************************/
    /** Map.class.getName().hashCode() = 1383349348 */
    public final static int MAP_CLS_HASH = -1383349348;// Map.class.getName().hashCode();
    /**
     * HashMap.class.getName().hashCode()<br>
     * = -1402722386
     */
    public final static int HASHMAP_CLS_HASH = -1402722386;// HashMap.class.getName().hashCode();
    /**
     * ConcurrentMap.class.getName().hashCode()<br>
     * = -1419705938
     */
    public final static int CONCURRENTMAP_CLS_HASH = -1419705938;// ConcurrentMap.class.getName().hashCode();
    /**
     * ConcurrentHashMap.class.getName().hashCode()<br>
     * = 577244352
     */
    public final static int CONCURRENTHASHMAP_CLS_HASH = 577244352;// ConcurrentHashMap.class.getName().hashCode();
    /**
     * Hashtable.class.getName().hashCode()<br>
     * = 639525312
     */
    public final static int HASHTABLE_CLS_HASH = 639525312;// Hashtable.class.getName().hashCode();
    /**
     * LinkedHashMap.class.getName().hashCode()<br>
     * = 1258621781
     */
    public final static int LINKEDHASHMAP_CLS_HASH = 1258621781;// LinkedHashMap.class.getName().hashCode();
    /**
     * TreeMap.class.getName().hashCode()<br>
     * = 1131064094
     */
    public final static int TREEMAP_CLS_HASH = 1131064094;// TreeMap.class.getName().hashCode();

    /**************************************************/
    // 链表
    /**************************************************/
    /**
     * List.class.getName().hashCode()<br>
     * = 65821278
     */
    public final static int LIST_CLS_HASH = 65821278;// List.class.getName().hashCode();
    /**
     * ArrayList.class.getName().hashCode()<br>
     * = -1114099497
     */
    public final static int ARRAYLIST_CLS_HASH = -1114099497;// ArrayList.class.getName().hashCode();
    /**
     * LinkedList.class.getName().hashCode()<br>
     * = -1899270121
     */
    public final static int LINKEDLIST_CLS_HASH = -1899270121;// LinkedList.class.getName().hashCode();
    /**
     * Set.class.getName().hashCode()<br>
     * = -1383343454
     */
    public final static int SET_CLS_HASH = -1383343454;// Set.class.getName().hashCode();
    /**
     * HashSet.class.getName().hashCode()<br>
     * = -1402716492
     */
    public final static int HASHSET_CLS_HASH = -1402716492;// HashSet.class.getName().hashCode();

    /**************************************************/
    // Object值相关
    /**************************************************/
    /**
     * Object.class.getName().hashCode()<br>
     * = 1063877011
     */
    public final static int OBJECT_CLS_HASH = 1063877011;// Object.class.getName().hashCode();
    /**
     * Object[].class.getName().hashCode()<br>
     * = 614832599
     */
    public final static int OBJECT_ARR_CLS_HASH = 614832599;// Object[].class.getName().hashCode();

    // 时间
    /**
     * java.util.Calendar.class.getName().hashCode()<br>
     * = -861027074
     */
    public final static int CALENDAR_CLS_HASH = -861027074;// java.util.Calendar.class.getName().hashCode();
    /**
     * java.util.Date.class.getName().hashCode()<br>
     * = 65575278
     */
    public final static int DATE_CLS_HASH = 65575278;// java.util.Date.class.getName().hashCode();
    /**
     * java.sql.Date.class.getName().hashCode()<br>
     * = 1087757882
     */
    public final static int SQL_DATE_CLS_HASH = 1087757882;// java.sql.Date.class.getName().hashCode();
    /**
     * java.sql.Time.class.getName().hashCode()<br>
     * = 1088242009
     */
    public final static int SQL_TIME_CLS_HASH = 1088242009;// java.sql.Time.class.getName().hashCode();
    /**
     * java.sql.Timestamp.class.getName().hashCode()<br>
     * = 1252880906
     */
    public final static int SQL_TIMESTAMP_CLS_HASH = 1252880906;// java.sql.Timestamp.class.getName().hashCode();

    /**
     * 判断类结构，是否实现指定的接口（含本类）
     * 
     * @param cls 类结构
     * @param iface 接口
     * @return =true表示实现,=false表示未实现
     */
    public static boolean isImplement(Class<?> cls, Class<?> iface)
    {
        if (iface == null || cls == null || cls == Object.class || isPrimitiveBase(cls) || !iface.isInterface())
            return false;

        if (cls == iface)
            return true;

        // 先判断接口是否实现，或实现的接口是否继承
        for (Class<?> c : cls.getInterfaces())
        {
            while (c != iface)
            {// 如果未实现，再继承查找，直到没有继承
                Class<?>[] cs = c.getInterfaces();
                if (cs.length == 0)
                    break;

                c = cs[0];
            }

            // 最后判断是否实现
            if (c == iface)
                return true;
        }

        // 再判断继承是否实现接口，有可能cls本身是接口
        Class<?> c = cls.getSuperclass();
        while (c != null && c != Object.class)
        {
            if (isImplement(c, iface))
                return true;

            c = c.getSuperclass();
        }

        return false;
    }

    /**
     * 是否八种基本原型基本类型，仅支持byte/short/int/long/float/double/boolean/char
     * 
     * @param clazz 类结构
     * @return =true/=false
     */
    public static boolean isPrimitiveBase(Class<?> clazz)
    {
        if (clazz == null)
            return false;

        if (clazz == int.class)
            return true;
        else if (clazz == long.class)
            return true;
        else if (clazz == boolean.class)
            return true;
        else if (clazz == byte.class)
            return true;
        else if (clazz == short.class)
            return true;
        else if (clazz == char.class)
            return true;
        else if (clazz == float.class)
            return true;
        else if (clazz == double.class)
            return true;
        else
            return false;
    }

    /**
     * 判断是否为空
     * 
     * @param s
     * @return
     */
    public static boolean isEmpty(String s)
    {
        return (s == null || s.length() == 0);
    }

    public static boolean isEmptyBlank(String s)
    {
        if (s == null || s.length() == 0)
            return true;

        for (char ch : s.toCharArray())
        {
            if (!isWhitespace(ch))
                return false;
        }
        return true;
    }

    public static String trim(String s)
    {
        if (s == null)
            return null;

        StringBuilder sb = new StringBuilder(s.length());
        for (char ch : s.toCharArray())
        {
            if (isWhitespace(ch))
                continue;

            sb.append(ch);
        }
        return sb.toString();
    }

    /**
     * 生成从当前位置开始的 一个字符串token
     * 
     * @param json json 字符串
     * @param pos 读取位置
     * @param scope token所属范围类型
     * @param prevToken 上一个token的类型
     * @return 返回String token 的字符长度
     */
    private static int getStringTokenLength(String json, int pos, int scope, byte prevType)
    {
        int length = 0;
        char quote = 0;// 字符串开始的引号值
        for (; pos < json.length(); pos++, length++)
        {
            char ch = json.charAt(pos);
            if (quote == 0)
            {
                quote = ch;// 记录开始符
                continue;
            }

            // 查找结束符， 非引号字符串结束的字符
            if (quote > 0 && quote != DB_QUOTE && quote != QUOTE && (ch == COLON || ch == COMMA || ch == BRACE_R || ch == BRACKET_R))
            {
                if (scope == BRACKET_L && ch == COLON)// 如果是数组当前是冒号&所属范围是数组，则当前冒号为值
                    continue;

                // 当前为冒号上一个token的类型为冒号，则档前为值
                if (ch == COLON && prevType == Token.COLON)
                    continue;// {a::sss:sdcsdcs, b:wwww} 其中 sss:sdcsdcs 为值
                else
                    return length;// 非引号开始 并且有结束负号
            }

            // 查找结束符，引号开头&当前是引号 & 上一个字符不是不是转义符
            if ((ch == QUOTE || ch == DB_QUOTE) && ch == quote && json.charAt(pos - 1) != '\\')
                return ++length;// 包含当前字符
        }

        if (pos == json.length())
            return --length;// 遍历结束索引json最大索引
        else
            return length;
    }

    /**
     * 验证是否为空白
     * 
     * @param ch
     * @return
     */
    public static boolean isWhitespace(char ch)
    {// 中文空格 =12288=0x3000, BOM空格 =65279=0xFEFF，英文空格 = 32
        return ch <= 32 || ch == 12288 || ch == 65279;
    }

    /***********************************************************************/
    // 以下是类的定义及对象的调用方法
    /***********************************************************************/
    private JsonParser baseParser = new BaseParser();
    private JsonParser arrayParser = new ArrayParser();
    private JsonParser listParser = new ListParser();
    private JsonParser mapParser = new MapParser();
    private JsonParser dateParser = new DateParser();
    private JsonParser objParser = new ObjectParser();

    private int pos = -1;
    private String json = null;
    private List<Byte> contextLs = new ArrayList<Byte>();// 当前token 所在的作用范围类型
    private byte curType = T_ZERO;// 上一个token 的类型
    private char ch;// 循环的当前字符
    private int objNum = 0;
    private int arrNum = 0;
    private String value = NULL;
    private int scopeIndex = -1;

    public JsonParser BaseParser()
    {
        return this.baseParser;
    }
    
    public JsonParser ArrayParser()
    {
        return this.arrayParser;
    }
    
    public JsonParser ListParser()
    {
        return this.listParser;
    }
    
    public JsonParser MapParser()
    {
        return this.mapParser;
    }
    public JsonParser DateParser()
    {
        return this.dateParser;
    }
    public JsonParser ObjectParser()
    {
        return this.objParser;
    }
    public JsonLexer(String input)
    {
        this.json = input;
        this.value = null;
    }

    public String value()
    {
        return this.value;
    }

    public boolean hasNext()
    {
        if (json == null)
            return false;

        return this.curType != T_EOF && nextPos() < json.length();
    }

    private int nextPos()
    {
        return this.pos + 1;
    }

    public byte tokenType()
    {
        return this.curType;
    }

    public String naxtToken()
    {
        this.value = NULL;

        if (!hasNext())
        {
            curType = T_EOF;
            return null;
        }

        this.pos = nextPos();
        this.ch = json.charAt(this.pos);
        if (isWhitespace(ch))
        {
            naxtToken();
            return this.value;// 空白字符开头
        }

        if (pos == 0 && BRACE_L != ch && ch != BRACKET_L)
        {
            naxtToken();
            return this.value;// json 数据格式只有两种，1.对象使用{} 括起，2.数组[] 括起。所以非
                              // {或者[开头的数据
        }

        switch (ch)
        {
        case BRACE_L:
        {
            objNum++;
            setScope(T_BRACE_L);
            value = String.valueOf(ch);
            curType = T_BRACE_L;
            break;
        }
        case BRACE_R:
        {
            if (objNum > 0)
            {
                objNum--;
                if (!contextLs.isEmpty() && contextLs.get(scopeIndex) == T_BRACE_L)
                    removeScope();// 设置结束当前context

                value = String.valueOf(ch);
                curType = contextLs.isEmpty() ? T_ZERO : contextLs.get(scopeIndex);
            }
            else
            {
                naxtToken();
                return this.value;
            }

            break;
        }
        case BRACKET_L:
        {

            arrNum++;
            setScope(T_BRACKET_L);
            value = String.valueOf(ch);
            curType = T_BRACKET_L;
            break;
        }
        case BRACKET_R:
        {
            if (arrNum > 0)
            {
                arrNum--;
                if (!contextLs.isEmpty() && contextLs.get(scopeIndex) == T_BRACKET_L)
                    removeScope();// 设置结束当前context

                value = String.valueOf(ch);
                curType = contextLs.isEmpty() ? T_ZERO : contextLs.get(scopeIndex);
            }
            else
            {
                naxtToken();
                return this.value;
            }

            break;
        }
        case COMMA:// 上一个token 不能为,
        {// 前面token 必须是 对象结束 或者 数组结束 或者字符串

            if (curType == T_COMMA)
            {
                naxtToken();
                return this.value;// 上一个token类型是逗号
            }

            // 处理上一个是 冒号 当前为逗号，则当前值为null 列：{dd:,} 被整理 后为 {dd:null,}
            if (curType == T_COLON)
            {
                curType = T_STRING;
                pos--;// 设置当前为值
            }
            else
            {
                curType = T_COMMA;
                value = String.valueOf(ch);
            }
            break;
        }
        case COLON:
        {

            if (curType == Token.STRING)
            {// 上一个token是String 则当前是冒号
                curType = T_COLON;
                value = String.valueOf(ch);
            }
            else
            {
                // 如果上一个 不是String 型 则当前是 String键或者值
                int length = getStringTokenLength(json, pos, contextLs.get(scopeIndex), curType);
                if (length > 0)
                {
                    pos += (length - 1);
                    this.value = String.valueOf(json.subSequence(pos, pos + length));
                }

                curType = T_STRING;
            }

            break;
        }
        default:
        {

            int length = getStringTokenLength(json, pos, contextLs.get(scopeIndex), curType);
            if (length > 0)
            {
                this.value = String.valueOf(json.subSequence(pos, pos + length));
                pos += (length - 1);
            }

            curType = T_STRING;
            break;
        }
        }
        return this.value;
    }

    public int scope()
    {
        return scopeIndex;
    }

    /***
     * 添加context
     */
    public void setScope(Byte scope)
    {
        contextLs.add(scope);
        scopeIndex++;
    }

    public boolean isEmptyScope()
    {
        return this.contextLs.size() == 1;
    }

    /** 删除当前context */
    public int removeScope()
    {
        if (scopeIndex < 0)
            return scopeIndex;

        contextLs.remove(scopeIndex);
        return --scopeIndex;
    }

    /** 根据类获取解析器 */
    public JsonParser getParser(Class<?> clazz)
    {
        JsonParser parser = null;
        switch (clazz.getName().hashCode())
        {

        // 八大基本类型+封装类+String
        case BOOL_CLS_HASH:// = 64711720;//boolean.class.getName().hashCode();
        case BOOL_OBJ_CLS_HASH:// =
                               // 344809556;//Boolean.class.getName().hashCode();
        case BYTE_CLS_HASH:// = 3039496;//byte.class.getName().hashCode();
        case BYTE_OBJ_CLS_HASH:// = 398507100;//Byte.class.getName().hashCode();
        case CHAR_CLS_HASH:// :// = 3052374;// char.class.getName().hashCode();
        case CHAR_OBJ_CLS_HASH:// =
                               // 155276373;//Character.class.getName().hashCode();
        case SHORT_CLS_HASH:// = 109413500;//short.class.getName().hashCode();
        case SHORT_OBJ_CLS_HASH:// =
                                // -515992664;//Short.class.getName().hashCode();
        case INT_CLS_HASH:// = 104431;//int.class.getName().hashCode();
        case INT_OBJ_CLS_HASH:// =
                              // -2056817302;//Integer.class.getName().hashCode();
        case LONG_CLS_HASH:// = 3327612;//long.class.getName().hashCode();
        case LONG_OBJ_CLS_HASH:// = 398795216;//Long.class.getName().hashCode();
        case FLOAT_CLS_HASH:// = 97526364;//float.class.getName().hashCode();
        case FLOAT_OBJ_CLS_HASH:// =
                                // -527879800;//Float.class.getName().hashCode();
        case DOUBLE_CLS_HASH:// =
                             // -1325958191;//double.class.getName().hashCode();
        case DOUBLE_OBJ_CLS_HASH:// =
                                 // 761287205;//Double.class.getName().hashCode();
        case STRING_CLS_HASH:// =
                             // 1195259493;//String.class.getName().hashCode();
            parser = baseParser;
            break;

        // 八大基本类型+封装类+String 的数组
        case DOUBLE_ARR_CLS_HASH:// =
                                 // 2889;//double[].class.getName().hashCode();
        case DOUBLE_OBJ_ARR_CLS_HASH:// =
                                     // -175516795;//Double[].class.getName().hashCode();
        case FLOAT_ARR_CLS_HASH:// = 2891;//float[].class.getName().hashCode();
        case FLOAT_OBJ_ARR_CLS_HASH:// =
                                    // 48646404;//Float[].class.getName().hashCode();
        case LONG_ARR_CLS_HASH:// = 2895;//long[].class.getName().hashCode();
        case LONG_OBJ_ARR_CLS_HASH:// =
                                   // -2071011078;//Long[].class.getName().hashCode();
        case INT_ARR_CLS_HASH:// = 2894;//int[].class.getName().hashCode();
        case INT_OBJ_ARR_CLS_HASH:// =
                                  // -1935445726;//Integer[].class.getName().hashCode();
        case SHORT_ARR_CLS_HASH:// = 2904;//short[].class.getName().hashCode();
        case SHORT_OBJ_ARR_CLS_HASH:// =
                                    // 417147620;//Short[].class.getName().hashCode();
        case CHAR_ARR_CLS_HASH:// = 2888;//char[].class.getName().hashCode();
        case CHAR_OBJ_ARR_CLS_HASH:// =
                                   // -1378128041;//Character[].class.getName().hashCode();
        case BYTE_ARR_CLS_HASH:// = 2887;//byte[].class.getName().hashCode();
        case BYTE_OBJ_ARR_CLS_HASH:// =
                                   // -2079942674;//Byte[].class.getName().hashCode();
        case BOOL_ARR_CLS_HASH:// = 2911;//boolean[].class.getName().hashCode();
        case BOOL_OBJ_ARR_CLS_HASH:// =
                                   // -499457160;//Boolean[].class.getName().hashCode();
        case STRING_ARR_CLS_HASH:// =
                                 // 392722245;//String[].class.getName().hashCode();
        case OBJECT_ARR_CLS_HASH:// = 614832599;//
                                 // Object[].class.getName().hashCode();
            parser = arrarParser;
            break;

        // 哈希表
        case MAP_CLS_HASH:// = -1383349348;// Map.class.getName().hashCode();
        case HASHMAP_CLS_HASH:// = -1402722386;//
                              // HashMap.class.getName().hashCode();
        case CONCURRENTMAP_CLS_HASH:// =-1419705938;//
                                    // ConcurrentMap.class.getName().hashCode();
        case CONCURRENTHASHMAP_CLS_HASH:// = 577244352;//
                                        // ConcurrentHashMap.class.getName().hashCode();
        case HASHTABLE_CLS_HASH:// = 639525312;//
                                // Hashtable.class.getName().hashCode();
        case LINKEDHASHMAP_CLS_HASH:// = 1258621781;//
                                    // LinkedHashMap.class.getName().hashCode();
        case TREEMAP_CLS_HASH:// = 1131064094;//
                              // TreeMap.class.getName().hashCode();
            parser = mapParser;
            break;

        // 链表
        case LIST_CLS_HASH:// = 65821278;// List.class.getName().hashCode();
        case ARRAYLIST_CLS_HASH:// = -1114099497;//
                                // ArrayList.class.getName().hashCode();
        case LINKEDLIST_CLS_HASH:// = -1899270121;//
                                 // LinkedList.class.getName().hashCode();
        case SET_CLS_HASH:// =-1383343454;// Set.class.getName().hashCode();
        case HASHSET_CLS_HASH:// =-1402716492;//
                              // HashSet.class.getName().hashCode();
            parser = listParser;
            break;

        // 时间
        case CALENDAR_CLS_HASH:// = -861027074;//
                               // java.util.Calendar.class.getName().hashCode();
        case DATE_CLS_HASH:// = 65575278;//
                           // java.util.Date.class.getName().hashCode();
        case SQL_DATE_CLS_HASH:// = 1087757882;//
                               // java.sql.Date.class.getName().hashCode();
        case SQL_TIME_CLS_HASH:// = 1088242009;//
                               // java.sql.Time.class.getName().hashCode();
        case SQL_TIMESTAMP_CLS_HASH:// = 1252880906;//
                                    // java.sql.Timestamp.class.getName().hashCode();
            parser = dateParser;
            break;

        // 通用 object
        case OBJECT_CLS_HASH:// = 1063877011;//
                             // Object.class.getName().hashCode();
            parser = objParser;
            break;
        }

        if (parser == null)
        {
            if (isImplement(clazz, Collection.class))
                parser = listParser;
            else if (isImplement(clazz, Map.class))
                parser = mapParser;
            else if (clazz.isArray())
                parser = arrarParser;
            else
                parser = objParser;
        }

        return parser;
    }

}