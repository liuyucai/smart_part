package cn.lps.utils;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
    static final Pattern floatNumericPattern = Pattern.compile("^[0-9\\-\\.]+$");
    static final Pattern abcPattern = Pattern.compile("^[a-z|A-Z]+$");
    static final Log LOG = LogFactory.getLog(cn.lps.utils.StringUtils.class);

    public StringUtils() {
    }

    public static boolean isNumeric(String src) {
        boolean result = true;

        try {
            Float.parseFloat(src);
        } catch (NumberFormatException var3) {
            result = false;
        }

        return result;
    }

    public static boolean isAlphabet(String src) {
        boolean result = false;
        if (src != null && src.length() > 0) {
            Matcher m = abcPattern.matcher(src);
            if (m.find()) {
                result = true;
            }
        }

        return result;
    }

    static boolean isFloatNumeric(String src) {
        boolean return_value = false;
        if (src != null && src.length() > 0) {
            Matcher m = floatNumericPattern.matcher(src);
            if (m.find()) {
                return_value = true;
            }
        }

        return return_value;
    }

    static String concatString(List<Object> array, String symbol) {
        String result = "";
        if (array != null) {
            for(int i = 0; i < array.size(); ++i) {
                String temp = array.get(i).toString();
                if (temp != null && temp.trim().length() > 0) {
                    result = result + temp + symbol;
                }
            }

            if (result.length() > 1) {
                result = result.substring(0, result.length() - 1);
            }
        }

        return result;
    }

    static String subStringNotEncode(String subject, int size) {
        if (subject != null && subject.length() > size) {
            subject = subject.substring(0, size) + "...";
        }

        return subject;
    }

    static String getLimitLengthString(String str, int len, String symbol) {
        int iLen = len * 2;
        int counterOfDoubleByte = 0;
        String strRet = "";

        String var9;
        try {
            if (str == null) {
                return "";
            }

            byte[] b = str.getBytes("GBK");
            if (b.length > iLen) {
                for(int i = 0; i < iLen; ++i) {
                    if (b[i] < 0) {
                        ++counterOfDoubleByte;
                    }
                }

                if (counterOfDoubleByte % 2 == 0) {
                    strRet = new String(b, 0, iLen, "GBK") + symbol;
                    var9 = strRet;
                    return var9;
                }

                strRet = new String(b, 0, iLen - 1, "GBK") + symbol;
                var9 = strRet;
                return var9;
            }

            var9 = str;
        } catch (Exception var12) {
            LOG.info("Error Msg", var12);
            var9 = str.substring(0, len);
            return var9;
        } finally {
            strRet = null;
        }

        return var9;
    }

    static String getLimitLengthString(String str, int len) {
        return getLimitLengthString(str, len, "...");
    }

    static String subStrNotEncode(String subject, int size) {
        if (subject.length() > size) {
            subject = subject.substring(0, size);
        }

        return subject;
    }

    static int getStringLen(String SrcStr) {
        int return_value = 0;
        if (SrcStr != null) {
            char[] theChars = SrcStr.toCharArray();

            for(int i = 0; i < theChars.length; ++i) {
                return_value += theChars[i] <= 255 ? 1 : 2;
            }
        }

        return return_value;
    }

    static boolean check(String str) {
        String sIllegal = "'\"";
        int len = sIllegal.length();
        if (str == null) {
            return false;
        } else {
            for(int i = 0; i < len; ++i) {
                if (str.indexOf(sIllegal.charAt(i)) != -1) {
                    return true;
                }
            }

            return false;
        }
    }

    static String getHideEmailPrefix(String email) {
        if (email != null) {
            int index = email.lastIndexOf(64);
            if (index > 0) {
                email = repeat("*", index).concat(email.substring(index));
            }
        }

        return email;
    }

    public static String repeat(String src, int num) {
        StringBuffer s = new StringBuffer();

        for(int i = 0; i < num; ++i) {
            s.append(src);
        }

        return s.toString();
    }

    static List<String> parseString2ListByCustomerPattern(String pattern, String src) {
        if (src == null) {
            return null;
        } else {
            List<String> list = new ArrayList();
            String[] result = src.split(pattern);

            for(int i = 0; i < result.length; ++i) {
                list.add(result[i]);
            }

            return list;
        }
    }

    static List<String> parseString2ListByPattern(String src) {
        String pattern = "，|,|、|。";
        return parseString2ListByCustomerPattern(pattern, src);
    }

    public static String formatFloat(float f, String format) {
        DecimalFormat df = new DecimalFormat(format);
        return df.format((double)f);
    }

    public static boolean isEmpty(String s) {
        return s == null || s.trim().length() == 0;
    }

    public static String lpad(String data, char padchar, int size) {
        if (data == null) {
            return null;
        } else if (data.length() >= size) {
            return data;
        } else {
            StringBuffer sb = new StringBuffer(data);

            while(sb.length() < size) {
                sb.insert(0, padchar);
            }

            return sb.toString();
        }
    }

    public static List<String> splitToList(String src, String splitSymbol, boolean skipBlankStr) {
        String sp = ",";
        if (splitSymbol != null) {
            sp = splitSymbol;
        }

        List<String> r = new ArrayList();
        String str = src;

        while(true) {
            String splitStr;
            do {
                int index = str.indexOf(sp);
                if (index == -1) {
                    if (!isEmpty(str) || !skipBlankStr) {
                        r.add(str);
                    }

                    return r;
                }

                splitStr = str.substring(0, index);
                str = str.substring(index + sp.length(), str.length());
            } while(isEmpty(splitStr) && skipBlankStr);

            r.add(splitStr);
        }
    }

    static String linkedHashMapToString(LinkedHashMap<String, String> map) {
        if (map != null && map.size() > 0) {
            String result = "";

            String name;
            String value;
            for(Iterator it = map.keySet().iterator(); it.hasNext(); result = result + String.format("{}={}", name, value)) {
                name = (String)it.next();
                value = (String)map.get(name);
                result = result + ("".equals(result) ? "" : "&");
            }

            return result;
        } else {
            return null;
        }
    }

    static LinkedHashMap<String, String> toLinkedHashMap(String str) {
        if (str != null && !"".equals(str) && str.indexOf("=") > 0) {
            LinkedHashMap<String, String> result = new LinkedHashMap();
            String name = null;
            String value = null;

            for(int i = 0; i < str.length(); ++i) {
                char c = str.charAt(i);
                switch(c) {
                case '&':
                    if (name != null && value != null && !"".equals(name)) {
                        result.put(name, value);
                    }

                    name = null;
                    value = null;
                    break;
                case '=':
                    value = "";
                    break;
                default:
                    if (value != null) {
                        value = value != null ? value + c : "" + c;
                    } else {
                        name = name != null ? name + c : "" + c;
                    }
                }
            }

            if (name != null && value != null && !"".equals(name)) {
                result.put(name, value);
            }

            return result;
        } else {
            return null;
        }
    }

    static String getCaption(String captions, int index) {
        if (index > 0 && captions != null && !"".equals(captions)) {
            String[] ss = captions.split(",");
            if (ss != null && ss.length > 0 && index < ss.length) {
                return ss[index];
            }
        }

        return null;
    }

    static String numberToString(Object num) {
        if (num == null) {
            return null;
        } else if (num instanceof Integer && ((Integer)num).intValue() > 0) {
            return Integer.toString(((Integer)num).intValue());
        } else if (num instanceof Long && ((Long)num).longValue() > 0L) {
            return Long.toString(((Long)num).longValue());
        } else if (num instanceof Float && ((Float)num).floatValue() > 0.0F) {
            return Float.toString(((Float)num).floatValue());
        } else {
            return num instanceof Double && ((Double)num).doubleValue() > 0.0D ? Double.toString(((Double)num).doubleValue()) : "";
        }
    }

    static String moneyToString(Object money, String style) {
        if (money == null || style == null || !(money instanceof Double) && !(money instanceof Float)) {
            return null;
        } else {
            Double num = (Double)money;
            if ("default".equalsIgnoreCase(style)) {
                if (num.doubleValue() == 0.0D) {
                    return "";
                } else {
                    return num.doubleValue() * 10.0D % 10.0D == 0.0D ? Integer.toString(num.intValue()) : num.toString();
                }
            } else {
                DecimalFormat df = new DecimalFormat(style);
                return df.format(num);
            }
        }
    }

    static boolean strPos(String sou, String... finds) {
        if (sou != null && finds != null && finds.length > 0) {
            for(int i = 0; i < finds.length; ++i) {
                if (sou.indexOf(finds[i]) > -1) {
                    return true;
                }
            }
        }

        return false;
    }

    static boolean strPos(String sou, List<String> finds) {
        if (sou != null && finds != null && finds.size() > 0) {
            Iterator var3 = finds.iterator();

            while(var3.hasNext()) {
                String s = (String)var3.next();
                if (sou.indexOf(s) > -1) {
                    return true;
                }
            }
        }

        return false;
    }

    static boolean equals(String s1, String s2) {
        if (isEmpty(s1) && isEmpty(s2)) {
            return true;
        } else {
            return !isEmpty(s1) && !isEmpty(s2) ? s1.equals(s2) : false;
        }
    }

    public static int toInt(String s) {
        if (s != null && !"".equals(s.trim())) {
            try {
                return Integer.parseInt(s);
            } catch (Exception var2) {
                LOG.info("Error Msg", var2);
                return 0;
            }
        } else {
            return 0;
        }
    }

    public static double toDouble(String s) {
        return s != null && !"".equals(s.trim()) ? Double.parseDouble(s) : 0.0D;
    }

    public static long toLong(String s) {
        try {
            if (s != null && !"".equals(s.trim())) {
                return Long.parseLong(s);
            }
        } catch (Exception var2) {
            LOG.info("Error Msg", var2);
        }

        return 0L;
    }

    static String removeURL(String str) {
        if (str != null) {
            str = str.toLowerCase().replaceAll("(http|www|com|cn|org|\\.)+", "");
        }

        return str;
    }

    static String numRandom(int bit) {
        if (bit == 0) {
            bit = 6;
        }

        String str = "";
        str = "0123456789";
        return RandomStringUtils.random(bit, str);
    }

    static String random(int bit) {
        if (bit == 0) {
            bit = 6;
        }

        String str = "";
        str = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijkmnpqrstuvwxyz";
        return RandomStringUtils.random(bit, str);
    }

    static String replaceWapStr(String str) {
        if (str != null) {
            str = str.replaceAll("<span class=\"keyword\">", "");
            str = str.replaceAll("</span>", "");
            str = str.replaceAll("<strong class=\"keyword\">", "");
            str = str.replaceAll("<strong>", "");
            str = str.replaceAll("</strong>", "");
            str = str.replace('$', '＄');
            str = str.replaceAll("&amp;", "＆");
            str = str.replace('&', '＆');
            str = str.replace('<', '＜');
            str = str.replace('>', '＞');
        }

        return str;
    }

    public static Float toFloat(String s) {
        try {
            return Float.parseFloat(s);
        } catch (NumberFormatException var2) {
            return new Float(0.0F);
        }
    }

    public static String replaceBlank(String str) {
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            str = m.replaceAll("");
        }

        return str;
    }

    public static String Q2B(String QJstr) {
        String outStr = "";
        String Tstr = "";
        byte[] b = null;

        for(int i = 0; i < QJstr.length(); ++i) {
            try {
                Tstr = QJstr.substring(i, i + 1);
                b = Tstr.getBytes("unicode");
            } catch (UnsupportedEncodingException var7) {
                if (LOG.isErrorEnabled()) {
                    LOG.error(var7);
                }
            }

            if (b[3] == -1) {
                b[2] = (byte)(b[2] + 32);
                b[3] = 0;

                try {
                    outStr = outStr + new String(b, "unicode");
                } catch (UnsupportedEncodingException var6) {
                    if (LOG.isErrorEnabled()) {
                        LOG.error(var6);
                    }
                }
            } else {
                outStr = outStr + Tstr;
            }
        }

        return outStr;
    }

    public static String changCoding(String s, String fencode, String bencode) {
        String str = null;

        try {
            if (isNotEmpty(s)) {
                str = new String(s.getBytes(fencode), bencode);
            }

            return str;
        } catch (UnsupportedEncodingException var5) {
            return s;
        }
    }

    static String removeHTMLLableExe(String str) {
        str = stringReplace(str, ">\\s*<", "><");
        str = stringReplace(str, "&nbsp;", " ");
        str = stringReplace(str, "<br ?/?>", "\n");
        str = stringReplace(str, "<([^<>]+)>", "");
        str = stringReplace(str, "\\s\\s\\s*", " ");
        str = stringReplace(str, "^\\s*", "");
        str = stringReplace(str, "\\s*$", "");
        str = stringReplace(str, " +", " ");
        return str;
    }

    static String removeHTMLLable(String str) {
        str = stringReplace(str, "\\s", "");
        str = stringReplace(str, "<br ?/?>", "\n");
        str = stringReplace(str, "<([^<>]+)>", "");
        str = stringReplace(str, "&nbsp;", " ");
        str = stringReplace(str, "&(\\S)(\\S?)(\\S?)(\\S?);", "");
        return str;
    }

    static String removeOutHTMLLable(String str) {
        str = stringReplace(str, ">([^<>]+)<", "><");
        str = stringReplace(str, "^([^<>]+)<", "<");
        str = stringReplace(str, ">([^<>]+)$", ">");
        return str;
    }

    static String stringReplace(String str, String sr, String sd) {
        Pattern p = Pattern.compile(sr, 2);
        Matcher m = p.matcher(str);
        str = m.replaceAll(sd);
        return str;
    }

    static String fomateToFullForm(String str, String pt) {
        String regEx = "<" + pt + "\\s+([\\S&&[^<>]]*)/>";
        Pattern p = Pattern.compile(regEx, 2);
        Matcher m = p.matcher(str);
        String[] sa = null;
        String sf = "";
        String sf2 = "";

        for(String sf3 = ""; m.find(); sa = null) {
            sa = p.split(str);
            if (sa == null) {
                break;
            }

            sf = str.substring(sa[0].length(), str.indexOf("/>", sa[0].length()));
            sf2 = sf + "></" + pt + ">";
            sf3 = str.substring(sa[0].length() + sf.length() + 2);
            str = sa[0] + sf2 + sf3;
        }

        return str;
    }

    static int[] getSubStringPos(String str, String sub, boolean b) {
        String[] sp = null;
        int l = sub.length();
        sp = splitString(str, sub);
        if (sp == null) {
            return null;
        } else {
            int[] ip = new int[sp.length - 1];

            int j;
            for(j = 0; j < sp.length - 1; ++j) {
                ip[j] = sp[j].length() + l;
                if (j != 0) {
                    ip[j] += ip[j - 1];
                }
            }

            if (b) {
                for(j = 0; j < ip.length; ++j) {
                    ip[j] -= l;
                }
            }

            return ip;
        }
    }

    static String[] splitString(String str, String ms) {
        Pattern p = Pattern.compile(ms, 2);
        String[] sp = p.split(str);
        return sp;
    }

    static String[] getStringArrayByPattern(String str, String pattern) {
        Pattern p = Pattern.compile(pattern, 2);
        Matcher matcher = p.matcher(str);
        HashSet result = new HashSet();

        while(matcher.find()) {
            for(int i = 0; i < matcher.groupCount(); ++i) {
                result.add(matcher.group(i));
            }
        }

        String[] resultStr = null;
        if (result.size() > 0) {
            resultStr = new String[result.size()];
            return (String[])result.toArray(resultStr);
        } else {
            return resultStr;
        }
    }

    static String[] midString(String s, String b, String e) {
        int i = s.indexOf(b) + b.length();
        int j = s.indexOf(e, i);
        String[] sa = new String[2];
        if (i >= b.length() && j >= i + 1 && i <= j) {
            sa[0] = s.substring(i, j);
            sa[1] = s.substring(j);
            return sa;
        } else {
            sa[1] = s;
            sa[0] = null;
            return sa;
        }
    }

    static boolean isMatch(String str, String pattern) {
        Pattern pattern_hand = Pattern.compile(pattern);
        Matcher matcher_hand = pattern_hand.matcher(str);
        boolean b = matcher_hand.matches();
        return b;
    }

    static String subStringExe(String s, String jmp, String sb, String se) {
        if (isEmpty(s)) {
            return "";
        } else {
            int i = s.indexOf(jmp);
            if (i >= 0 && i < s.length()) {
                s = s.substring(i + 1);
            }

            i = s.indexOf(sb);
            if (i >= 0 && i < s.length()) {
                s = s.substring(i + 1);
            }

            if (se == "") {
                return s;
            } else {
                i = s.indexOf(se);
                if (i >= 0 && i < s.length()) {
                    s = s.substring(i + 1);
                }

                return s;
            }
        }
    }

    static String URLEncode(String src) {
        String return_value = "";

        try {
            if (src != null) {
                return_value = URLEncoder.encode(src, "GBK");
            }
        } catch (UnsupportedEncodingException var3) {
            LOG.info("Error Msg", var3);
            return_value = src;
        }

        return return_value;
    }

    static String getGBK(String str) {
        return transfer(str);
    }

    static String transfer(String str) {
        Pattern p = Pattern.compile("&#\\d+;");

        String old;
        for(Matcher m = p.matcher(str); m.find(); str = str.replaceAll(old, getChar(old))) {
            old = m.group();
        }

        return str;
    }

    static String getChar(String str) {
        String dest = str.substring(2, str.length() - 1);
        char ch = (char)Integer.parseInt(dest);
        return "" + ch;
    }

    public static <T> String listTtoString(List<T> list) {
        if (list != null && !list.isEmpty()) {
            Iterator<T> i = list.iterator();
            StringBuilder sb = new StringBuilder();

            while(true) {
                T e = i.next();
                sb.append(e);
                if (!i.hasNext()) {
                    return sb.toString();
                }

                sb.append(",");
            }
        } else {
            return "";
        }
    }

    static boolean isNotEmpty(String str) {
        return str != null && !"".equals(str.trim());
    }

    static String full2Half(String str) {
        if (str != null && !"".equals(str)) {
            StringBuffer sb = new StringBuffer();

            for(int i = 0; i < str.length(); ++i) {
                char c = str.charAt(i);
                if (c >= '！' && c < '｝') {
                    sb.append((char)(c - 'ﻠ'));
                } else {
                    sb.append(str.charAt(i));
                }
            }

            return sb.toString();
        } else {
            return "";
        }
    }

    static Map<String, String> parseQuery(String query, char split1, char split2, String dupLink) {
        if (!isEmpty(query) && query.indexOf(split2) > 0) {
            Map<String, String> result = new HashMap();
            String name = null;
            String value = null;
            String tempValue = "";
            int len = query.length();

            for(int i = 0; i < len; ++i) {
                char c = query.charAt(i);
                if (c == split2) {
                    value = "";
                } else if (c == split1) {
                    if (!isEmpty(name) && value != null) {
                        if (dupLink != null) {
                            tempValue = (String)result.get(name);
                            if (tempValue != null) {
                                value = value + dupLink + tempValue;
                            }
                        }

                        result.put(name, value);
                    }

                    name = null;
                    value = null;
                } else if (value != null) {
                    value = value + c;
                } else {
                    name = name != null ? name + c : "" + c;
                }
            }

            if (!isEmpty(name) && value != null) {
                if (dupLink != null) {
                    tempValue = (String)result.get(name);
                    if (tempValue != null) {
                        value = value + dupLink + tempValue;
                    }
                }

                result.put(name, value);
            }

            return result;
        } else {
            return null;
        }
    }

    static String listToStringSlipStr(List<String> list, String slipStr) {
        StringBuffer returnStr = new StringBuffer();
        if (list != null && list.size() > 0) {
            for(int i = 0; i < list.size(); ++i) {
                returnStr.append((String)list.get(i)).append(slipStr);
            }
        }

        return returnStr.toString().length() > 0 ? returnStr.toString().substring(0, returnStr.toString().lastIndexOf(slipStr)) : "";
    }

    static String getMaskStr(String str, int start, int len) {
        if (isEmpty(str)) {
            return str;
        } else if (str.length() < start) {
            return str;
        } else {
            String ret = str.substring(0, start);
            int strLen = str.length();
            if (strLen < start + len) {
                len = strLen - start;
            }

            for(int i = 0; i < len; ++i) {
                ret = ret + "*";
            }

            if (strLen > start + len) {
                ret = ret + str.substring(start + len);
            }

            return ret;
        }
    }

    static String getHtmlSubString(String str, int len, String tail) {
        if (str != null && str.length() > len) {
            int length = str.length();
//            char c = true;
            String tag = null;
            String name = null;
//            int size = false;
            String result = "";
            boolean isTag = false;
            List<String> tags = new ArrayList();
            int i = 0;
            int end = 0;

            for(boolean var13 = false; i < length && len > 0; ++i) {
                char c = str.charAt(i);
                if (c == '<') {
                    end = str.indexOf(62, i);
                }

                if (end > 0) {
                    tag = str.substring(i, end + 1);
                    int n = tag.length();
                    if (tag.endsWith("/>")) {
                        isTag = true;
                    } else if (tag.startsWith("</")) {
                        name = tag.substring(2, end - i);
                        int size = tags.size() - 1;
                        if (size >= 0 && name.equals(tags.get(size))) {
                            isTag = true;
                            tags.remove(size);
                        }
                    } else {
                        int spanEnd = tag.indexOf(32, 0);
                        spanEnd = spanEnd > 0 ? spanEnd : n;
                        name = tag.substring(1, spanEnd);
                        if (name.trim().length() > 0) {
                            spanEnd = str.indexOf("</" + name + ">", end);
                            if (spanEnd > 0) {
                                isTag = true;
                                tags.add(name);
                            }
                        }
                    }

                    if (!isTag) {
                        if (n >= len) {
                            result = result + tag.substring(0, len);
                            break;
                        }

                        len -= n;
                    }

                    result = result + tag;
                    isTag = false;
                    i = end;
                    end = 0;
                } else {
                    --len;
                    result = result + c;
                }
            }

            String endTag;
            for(Iterator var19 = tags.iterator(); var19.hasNext(); result = result + "</" + endTag + ">") {
                endTag = (String)var19.next();
            }

            if (i < length) {
                result = result + tail;
            }

            return result;
        } else {
            return str;
        }
    }

    static String getProperty(String property) {
        return property.contains("_") ? property.replaceAll("_", "\\.") : property;
    }

    static String getEncodePra(String property) {
        String trem = "";
        if (isNotEmpty(property)) {
            try {
                trem = URLDecoder.decode(property, "UTF-8");
            } catch (UnsupportedEncodingException var3) {
                LOG.info("Error Msg", var3);
            }
        }

        return trem;
    }

    String getNumbers(String content) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(content);
        return matcher.find() ? matcher.group(0) : "";
    }

    String splitNotNumber(String content) {
        Pattern pattern = Pattern.compile("\\D+");
        Matcher matcher = pattern.matcher(content);
        return matcher.find() ? matcher.group(0) : "";
    }

    static boolean contains(String[] stringArray, String source) {
        List<String> tempList = Arrays.asList(stringArray);
        return tempList.contains(source);
    }
}