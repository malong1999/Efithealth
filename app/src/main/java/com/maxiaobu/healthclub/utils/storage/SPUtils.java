package com.maxiaobu.healthclub.utils.storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.maxiaobu.healthclub.App;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by 马小布 on 2016/8/12.
 * sp工具类
 */
public class SPUtils {
    private SPUtils() {
        throw new UnsupportedOperationException("u can u up...");
    }

    /**
     * SP的name值
     * <p>可通过修改PREFERENCE_NAME变量修改SP的name值</p>
     */
    public static String PREFERENCE_NAME = "MA_XIAO_BU";


    /**
     * SP中写入Set
     */
    public static boolean putStringSet(String key, HashSet<String> value){
        return getSP(App.getInstance()).edit().putStringSet(key,value).commit();
    }

    /**
     * SP中取出set
     */
    public static Set<String> getStringSet(String key){

        return getSP(App.getInstance()).getStringSet(key,new HashSet<>());
    }


    /**
     * SP中写入String类型value
     *
     * @param key     键
     * @param value   值
     * @return true: 写入成功<br>false: 写入失败
     */
    public static boolean putString(String key, String value) {
        return getSP(App.getInstance()).edit().putString(key, value).commit();
    }


    /**
     * SP中读取String
     *
     * @param key     键
     * @return 存在返回对应值，不存在返回默认值null
     */
    public static String getString( String key) {
        return getString( key, null);
    }

    /**
     * SP中读取String
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 存在返回对应值，不存在返回默认值defaultValue
     */
    public static String getString( String key, String defaultValue) {
        return getSP(App.getInstance()).getString(key, defaultValue);
    }

    /**
     * SP中写入int类型value
     *
     * @param key     键
     * @param value   值
     * @return true: 写入成功<br>false: 写入失败
     */
    public static boolean putInt( String key, int value) {
        return getSP(App.getInstance()).edit().putInt(key, value).commit();
    }

    /**
     * SP中读取int
     *
     * @param key     键
     * @return 存在返回对应值，不存在返回默认值-1
     */
    public static int getInt( String key) {
        return getInt( key, -1);
    }

    /**
     * SP中读取int
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 存在返回对应值，不存在返回默认值defaultValue
     */
    public static int getInt( String key, int defaultValue) {
        return getSP(App.getInstance()).getInt(key, defaultValue);
    }

    /**
     * SP中写入long类型value
     *
     * @param key     键
     * @param value   值
     * @return true: 写入成功<br>false: 写入失败
     */
    public static boolean putLong( String key, long value) {
        return getSP(App.getInstance()).edit().putLong(key, value).commit();
    }

    /**
     * SP中读取long
     *
     * @param key     键
     * @return 存在返回对应值，不存在返回默认值-1
     */
    public static long getLong( String key) {
        return getLong( key, -1);
    }

    /**
     * SP中读取long
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 存在返回对应值，不存在返回默认值defaultValue
     */
    public static long getLong( String key, long defaultValue) {
        return getSP(App.getInstance()).getLong(key, defaultValue);
    }

    /**
     * SP中写入float类型value
     *
     * @param key     键
     * @param value   值
     * @return true: 写入成功<br>false: 写入失败
     */
    public static boolean putFloat(String key, float value) {
        return getSP(App.getInstance()).edit().putFloat(key, value).commit();
    }

    /**
     * SP中读取float
     *
     * @param key     键
     * @return 存在返回对应值，不存在返回默认值-1
     */
    public static float getFloat( String key) {
        return getFloat( key, -1);
    }

    /**
     * SP中读取float
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 存在返回对应值，不存在返回默认值defaultValue
     */
    public static float getFloat( String key, float defaultValue) {
        return getSP(App.getInstance()).getFloat(key, defaultValue);
    }

    /**
     * SP中写入boolean类型value
     *
     * @param key     键
     * @param value   值
     * @return true: 写入成功<br>false: 写入失败
     */
    public static boolean putBoolean( String key, boolean value) {
        return getSP(App.getInstance()).edit().putBoolean(key, value).commit();
    }

    /**
     * SP中读取boolean
     *
     * @param key     键
     * @return 存在返回对应值，不存在返回默认值false
     */
    public static boolean getBoolean( String key) {
        return getBoolean(key, false);
    }

    /**
     * SP中读取boolean
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 存在返回对应值，不存在返回默认值defaultValue
     */
    public static boolean getBoolean( String key, boolean defaultValue) {
        return getSP(App.getInstance()).getBoolean(key, defaultValue);
    }



    /**
     * 获取name为PREFERENCE_NAME的SP对象
     *
     * @param context 上下文
     * @return SP
     */
    private static SharedPreferences getSP(Context context) {
        return context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    /**
     * 根据键删除单条sp
     * @param context
     * @param key sp的键
     */
    public static boolean deleteSingleData(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        return editor.commit();
    }

    /**
     * 删除全部sp数据
     * @param context
     * @return
     */
    public static boolean clearAllData(Context context) {
        SharedPreferences sp = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        return editor.commit();
    }
}
