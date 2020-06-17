package com.xiyuan.util.json;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * JSON解析器接口
 * 
 * @version v1.0.0 @author lgz 2016-3-21 新建与整理
 */
public abstract class JsonParser implements Serializable
{
    private static final long serialVersionUID = 1L;
    public static final long JS_MAX_LONG_VALUE = (long) Math.pow(2, 53);// 2^53//9007199254740992
    public static final String DATETIME_REG = "^((([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29))\\s(([0-1][0-9]|[2][0-3]):([0-5][0-9]):([0-5][0-9]))$";
    private static final int defaultCount = 120;//默认初始化空间大小
    /**用于保存临时的对象字段列表,用于toObject*/
    private static final Map<Class<?>, Map<String, Field>> fieldMap = new HashMap<Class<?>, Map<String, Field>>(defaultCount);//初始化defaultCount个类空间
    /**用于保存临时的对象字段列表,用于toString*/
    private static final Map<Class<?>, List<Field>> fieldList = new HashMap<Class<?>, List<Field>>(defaultCount);//初始化defaultCount个类空间
    
    protected int level;//检查日志级别0=忽略异常，1=严格检查，不匹配则抛异常
    protected JsonLexer lexer;
    public JsonParser(JsonLexer lexer, int level)
    {
        this.lexer = lexer;
        this.level = level;
    }
    
    /**
     * 解析对象成JSON字符串
     * 
     * @param obj 对象
     * @return JSON字符串
     */
    public abstract String toString(Object obj);
    
    /**
     * 解析JSON字符串成对象
     * @param <T>
     * 
     * @param json json字符串 // * @param token Json token流
     * @param cls 对象类结构
     * @return 返回对象
     */
    public abstract Object toObject(Class<?> cls);
    
    /***********************************************/
    // 以下提供子类调用方法
    /***********************************************/
    /**
     * 对一个未知的数组对象，转化为一个已知的数组对象，其中对基本类型进行转化
     * 
     * @param obj 未知的数组对象
     * @return 已知的数组对象
     */
    protected Object[] toArray(Object obj)
    {
        if (obj == null || !obj.getClass().isArray())
            throw new IllegalArgumentException("传入的参数不是数组类型");
        switch (obj.getClass().getName().hashCode())
        {
            case JsonLexer.BOOLEAN:
            {
                boolean[] os = (boolean[]) obj;
                Boolean[] arr = new Boolean[os.length];
                for (int i = 0; i < os.length; i++)
                    arr[i] = os[i];
                return arr;
            }
            case JsonLexer.ARR_BYTE:
            {
                byte[] os = (byte[]) obj;
                Byte[] arr = new Byte[os.length];
                for (int i = 0; i < os.length; i++)
                    arr[i] = os[i];
                return arr;
            }
            case JsonLexer.ARR_CHAR:
            {
                char[] os = (char[]) obj;
                Character[] arr = new Character[os.length];
                for (int i = 0; i < os.length; i++)
                    arr[i] = os[i];
                return arr;
            }
            case JsonLexer.ARR_SHORT:
            {
                short[] os = (short[]) obj;
                Short[] arr = new Short[os.length];
                for (int i = 0; i < os.length; i++)
                    arr[i] = os[i];
                return arr;
            }
            case JsonLexer.ARR_INT:
            {
                int[] os = (int[]) obj;
                Integer[] arr = new Integer[os.length];
                for (int i = 0; i < os.length; i++)
                    arr[i] = os[i];
                
                os = null;
                return arr;
            }
            case JsonLexer.ARR_LONG:
            {
                long[] os = (long[]) obj;
                Long[] arr = new Long[os.length];
                for (int i = 0; i < os.length; i++)
                    arr[i] = os[i];
                return arr;
            }
            case JsonLexer.ARR_FLOAT:
            {
                float[] os = (float[]) obj;
                Float[] arr = new Float[os.length];
                for (int i = 0; i < os.length; i++)
                    arr[i] = os[i];
                return arr;
            }
            case JsonLexer.ARR_DOUBLE:
            {
                double[] os = (double[]) obj;
                Double[] arr = new Double[os.length];
                for (int i = 0; i < os.length; i++)
                    arr[i] = os[i];
                return arr;
            }
            default:
                return (Object[]) obj;
        }
    }
    
    /**
     * 是否8种基本类型对象数组，支持byte/short/int/long/float/double/boolean/char
     * 
     * 这里只判断类int[],不去判断Integer[]，因为Integer[]可转化为Object[]，而int[]不行
     * 
     * @param clazz 类结构
     * @return =true/=false
     */
    protected boolean isPrimitiveArray(Class<?> clazz)
    {
        if (clazz == null)
            return false;
        
        return clazz.isArray() ? clazz.getComponentType().isPrimitive() : false;
    }
    
    /**
     * 是否8种基本类型对象封装类数组，支持byte/short/int/long/float/double/boolean/char
     * 
     * 这里只判断类int[],不去判断Integer[]，因为Integer[]可转化为Object[]，而int[]不行
     * 
     * @param clazz 类结构
     * @return =true/=false
     */
    protected boolean isPrimitiveObjArray(Class<?> clazz)
    {
        if (clazz == null)
            return false;
        
        switch (clazz.getName().hashCode())
        {
            case JsonLexer.ARR_BOOLEAN:
            case JsonLexer.ARR_BYTE_O:
            case JsonLexer.ARR_CHAR_O:
            case JsonLexer.ARR_SHORT_O:
            case JsonLexer.ARR_INT_O:
            case JsonLexer.ARR_LONG_O:
            case JsonLexer.ARR_FLOAT_O:
            case JsonLexer.ARR_DOUBLE_O:
                return true;
            default:
                return false;
        }
        
    }
    
    /**
     * String到java.sql.Date的转换 标准格式:yyyy-MM-dd
     * 
     * @param date 日期字符串
     * @return java.sql.Date
     */
    protected java.sql.Date toSqlDate(String date)
    {
        return java.sql.Date.valueOf(date);
    }
    
    /**
     * 转换String 到 Time,格式:"HH:mm:ss"
     * 
     * @param time 时间字符串
     * @return Time
     */
    protected Time toTime(String time)
    {
        return Time.valueOf(time);
    }
    
    /**
     * 转换String 到 Timestamp,格式:"yyyy-MM-dd HH:mm:ss"
     * 
     * @param datetime 格式:"yyyy-MM-dd HH:mm:ss"
     * @return Timestamp
     */
    protected Timestamp toTimestamp(String datetime)
    {
        return Timestamp.valueOf(datetime);
    }
    
    /**
     * 字符串转为日历,字符串符合标准格式:yyyy-MM-dd HH:mm:ss
     * 
     * @param datetime 标准时间格式 "yyyy-MM-dd HH:mm:ss"
     * @return Calendar
     */
    protected Calendar toCalendar(String datetime)
    {
        if (datetime == null)
            return null;
        
        if (!Pattern.matches(DATETIME_REG, datetime))
            throw new IllegalArgumentException("传入参数格式不正确");
        
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, Integer.parseInt(datetime.substring(0, 4)));
        c.set(Calendar.MONTH, Integer.parseInt(datetime.substring(5, 7)) - 1);// 月份的起始值为0而不是1
        c.set(Calendar.DATE, Integer.parseInt(datetime.substring(8, 10)));
        c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(datetime.substring(11, 13)));
        c.set(Calendar.MINUTE, Integer.parseInt(datetime.substring(14, 16)));
        c.set(Calendar.SECOND, Integer.parseInt(datetime.substring(17)));
        c.set(Calendar.MILLISECOND, 0);
        
        return c;
    }
    
    /**
     * 字符串转为时间,字符串符合标准格式yyyy-MM-dd HH:mm:ss
     * 
     * @param datetime 标准时间格式yyyy-MM-dd HH:mm:ss
     * @return 日期对象
     */
    protected java.util.Date toDate(String datetime)
    {
        return toCalendar(datetime).getTime();
    }
    
    /**
     * 生成标准日期，格式为yyyy-MM-dd
     * 
     * @param date 日期对象
     * @return 日期字符串，格式为yyyy-MM-dd
     */
    protected String toDateString(Date date)
    {
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }
    
    /**
     * 根据输入的时间,生成时间格式 HH:mm:ss
     * 
     * @param date Date对象
     * @return 生成时间格式为HH:mm:ss
     */
    protected String toTimeString(Date date)
    {
        return new SimpleDateFormat("HH:mm:ss").format(date);
    }
    
    /**
     * 生成标准格式的字符串 格式为yyyy-MM-dd HH:mm:ss
     * 
     * @param date 日期对象
     * @return 生成默认格式的字符串 格式为yyyy-MM-dd HH:mm:ss
     */
    protected String toDateTimeString(Date date)
    {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }
    
    /**
     * 日历转标准时间字符串
     * 
     * @param calendar 日历,给定一个日历
     * @return 取得默认的日期时间字符串yyyy-MM-dd HH:mm:ss
     */
    protected String toDateTimeString(Calendar calendar)
    {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime());
    }
    
    /*****************************************************************/
    // 以下是类相关操作
    /*****************************************************************/
    
    /**
     * 获取类中指定的属性列表，支持深度查找父类的字段列表(父类的循递归查找),不包括静态和临时两种属性
     * 注：字段已经设置为可访问
     * @param clazz 类
     * @param fieldList 用于存储的字段列表
     */
    public List<Field> getFieldListDeep(Class<?> clazz)
    {
        List<Field> ls = fieldList.get(clazz);
        if (ls == null)
        {
            ls = new ArrayList<Field>(defaultCount);//初始化 defaultCount个字段空间
            getFieldListDeep(clazz, ls);
            fieldList.put(clazz, ls);
        }
        
        return ls;
    }
    
    /**
     * 获取类中指定的属性列表，支持深度查找父类的字段列表(父类的循递归查找),不包括静态和临时两种属性
     * 注：字段已经设置为可访问
     * @param clazz
     * @param fieldList
     */
    public void getFieldListDeep(Class<?> clazz, List<Field> fieldList)
    {
        for (Field field : clazz.getDeclaredFields())
        {
            int mod = field.getModifiers();
            if (Modifier.isStatic(mod) || Modifier.isTransient(mod) || "this$0".equals(field.getName()))
                continue;// 静态和临时两种属性不拷贝，内部类指向外部类的引用不拷贝
            
            if (!field.isAccessible())//设置可访问
                field.setAccessible(true);
            
            fieldList.add(field);
        }
        
        Class<?> superSrcClass = clazz.getSuperclass();
        if (superSrcClass != null && superSrcClass != Object.class)
        {
            getFieldListDeep(superSrcClass, fieldList);
        }
    }
    
    /**
     * 获取类中指定的属性HashMap，支持深度查找父类的字段列表(父类的循递归查找),静态和临时两种属性不拷贝
     * 
     * @param clazz 类
     * @param fieldList 用于存储的字段列表
     * @return 
     */
    
    public Map<String, Field> getFieldMapDeep(Class<?> clazz)
    {
        Map<String, Field> map = fieldMap.get(clazz);
        if (map == null)
        {
            map = new HashMap<String, Field>(defaultCount);//初始化 defaultCount个字段空间
            fieldMap.put(clazz, map);
            for (Field field : getFieldListDeep(clazz))
                map.put(field.getName(), field);
        }
        return map;
    }
    
    /**
     * 初始化实例，忽略异常，异常时返回null
     * 
     * @param cls 类结构
     * @return 实例
     */
    protected <T> T newInstance(Class<T> cls)
    {
        try
        {
            return cls.newInstance();
        }
        catch (Exception e)
        {
            return null;
        }
    }
}