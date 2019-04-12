package com.core.lib_core.utils;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;

/**
 * TextView输入框的限制符
 *
 * @author coffee<br>
 * 2015-6-26下午6:05:05
 */
public class InputFilterExt implements android.text.InputFilter {

    private final int TYPE_APP = 0;

    /**
     * 过滤手机号(只允许输入手机号)
     */
    private final int TYPE_PHONE = 1;

    /**
     * 金钱类型(只允许输入合法的double类型)
     */
    private final int TYPE_MONEY = 2;

    /**
     * 数字+字母(只允许输入数字和字母)
     */
    private final int TYPE_NUM_AZ = 3;

    /**
     * 不允许emoji表情
     */
    private final int TYPE_NO_EMOJI = 4;

    /**
     * 只允许输入 0-9 大小写字母、汉字、逗号和分号
     */
    private final int TYPE_EDIT_TV = 5;
    /**
     * 禁止输入空格
     */
    private final int TYPE_NO_SPACE = 6;
    // ####
    private int type;

    /**
     *
     */
    public InputFilterExt() {
        this.type = 0;
    }

    /**
     * 从1开始
     *
     * @param type
     */
    public InputFilterExt(int type) {
        this.type = type;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        if (type == TYPE_APP) {
            // 只允许输入 0-9 大小写字母、汉字
            if (source.toString().matches("[^a-zA-Z0-9\u4E00-\u9FA5]")) {
                return "";
            }
        } else if (type == TYPE_PHONE) {
            // 如果输入的不是数字(0-9)或者符号(-#)则过滤掉
            if (source.toString().matches("[^\\d-#]*")) {
                return "";
            }
        } else if (type == TYPE_MONEY) {
            try {
                return filterMoney(source, start, end, dest, dstart, dend);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (type == TYPE_NUM_AZ) {
            // 如果输入的不是0-9 或者字母,则过滤掉
            if (source.toString().matches("[^a-zA-Z0-9]*")) {
                return "";
            }
        } else if (type == TYPE_NO_EMOJI) {
            sb.setLength(0);
            for (int i = 0; i < source.length(); i++) {
                // 只允许汉字、a-z、A-Z、0-9、点号 # -
                String charator = String.valueOf(source.charAt(i));
                if (charator.matches("[\u4E00-\u9FA5a-zA-Z0-9\\.#-—,。，;]")) {
                    sb.append(charator);
                }
            }
            if (source instanceof Spanned) {
                SpannableString sp = new SpannableString(sb.toString());
                if (source.toString().length() == sp.toString().length()) {
                    TextUtils.copySpansFrom((Spanned) source, start, end, null, sp, 0);
                }
                return sp;
            } else {
                return sb.toString();
            }
        } else if (type == TYPE_EDIT_TV) {
            // 只允许输入 0-9 大小写字母、汉字、逗号和分号
            if (source.toString().matches("[^(a-zA-Z0-9\u4E00-\u9FA5,;，；)]")) {
                return "";
            }
        } else if (type == TYPE_NO_SPACE) {
            if (source.equals(" ") || source.toString().contentEquals("\n")) {
                return "";
            }
        }
        return source;
    }

    private StringBuilder sb = new StringBuilder();

    public CharSequence filterMoney(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        // 如果输入的不是数字(0-9)或者点号(.)则过滤掉
        if (source.toString().matches("[\\d.]*?") == false) {
            return "";
        }
        // 如果已经有小数点了,则过滤掉(即只能输入一个小数点)
        if (source.equals(".") && dest.toString().contains(".")) {
            return "";
        }
        // 如果第一位是点号则过滤掉即不允许（.12）等的操作，只能是（0.12）等
        if ((source.equals(".") && dstart == 0)) {
            return "";
        }
        // 过滤掉 0000、000.05等格式
        if (source.equals("0") && dest.toString().startsWith("0") && dstart <= 1) {
            return "";
        }

        String valStr = dest.toString();
        String[] splitArray = valStr.split("\\.");
        // 如果当前输入的是带有小数点的实数,
        if (splitArray.length > 1) {
            // 12.00 当前插入的是小数位。则需要处理小数位是否合法
            if (dstart > valStr.indexOf(".")) {
                String dotValue = splitArray[1];
                int diff = dotValue.length() - 2;// 如果小数点位数超过2位.则去掉最后一位
                if (diff >= 0) {
                    return "";// 过滤掉
                } else {
                    return source;
                }
            }// 当前插入的是整数位
            else {
                // 过滤掉 00.12\01.12等格式
                if (source.toString().equals("0") && dstart == 0 && splitArray.length > 0) {
                    return "";
                }
            }
        }

        String newStr = new StringBuilder(dest).insert(dstart, source).toString();
        if (TextUtils.isEmpty(newStr)) {
            return "";
        } else {
            double value = Double.valueOf(newStr);
            // 最大金额不超过2000
            if (value > 999) {
                return "";
            } else {
                return source;
            }
        }
    }

}