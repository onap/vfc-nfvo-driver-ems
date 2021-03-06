/*
 * Copyright 2017 BOCO Corporation.  CMCC Technologies Co., Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.onap.vfc.nfvo.emsdriver.commons.utils;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

    public  final class VarExprParser {
    private static Logger log = LoggerFactory.getLogger(VarExprParser.class);
    
    private static Pattern varPattern = Pattern.compile("(\\$\\{([^\\}]+)\\})",
            Pattern.CASE_INSENSITIVE);

    public static final String replaceVar(String str, long scanStartTime, long scanStopTime) {
        if (str.indexOf("${") == -1)
            return str;

        // support original system variable
        str = str.replace("${s_year}", "${SCAN_START_TIME,yyyy}");
        str = str.replace("${s_mon}", "${SCAN_START_TIME,MM}");
        str = str.replace("${s_day}", "${SCAN_START_TIME,dd}");
        str = str.replace("${s_hour}", "${SCAN_START_TIME,HH}");
        str = str.replace("${s_min}", "${SCAN_START_TIME,mm}");
        str = str.replace("${e_year}", "${SCAN_STOP_TIME,yyyy}");
        str = str.replace("${e_mon}", "${SCAN_STOP_TIME,MM}");
        str = str.replace("${e_day}", "${SCAN_STOP_TIME,dd}");
        str = str.replace("${e_hour}", "${SCAN_STOP_TIME,HH}");
        str = str.replace("${e_min}", "${SCAN_STOP_TIME,mm}");

        String expr;
	String varName;
	String value ;
        Matcher matcher = varPattern.matcher(str);
        while (matcher.find()) {
            value = null;
            expr = matcher.group(1);
            varName = matcher.group(2);
            if (expr.indexOf("${SCAN_START_TIME") != -1) {
                value = getTime(scanStartTime, varName, "yyyy-MM-dd HH:mm:ss");
            } else if (expr.indexOf("${SCAN_STOP_TIME") != -1) {
                value = getTime(scanStopTime, varName, "yyyy-MM-dd HH:mm:ss");
            }
            if (value == null) {
                log.warn(" expr [" + str + "] var["
                        + expr + "]is fail");
                continue;
            }
            str = str.replace(expr, value);
        }
        return str;
    }

    private static String getTime(long time, String value, String defaultParam) {
        String timeStr = null;
        String formatStr = null;
        String increaseTime = null;
        if (value.indexOf(",") == -1) {
            formatStr = defaultParam;
            timeStr = value;
        } else {
            timeStr = value.split(",")[0];
            formatStr = value.split(",")[1];
        }

        if (timeStr.indexOf("+") == -1 && timeStr.indexOf("-") == -1) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatStr, Locale.ENGLISH);
            return simpleDateFormat.format(time);
        } else {
            if (timeStr.indexOf("+") != -1) {
                increaseTime = timeStr.substring(timeStr.indexOf("+") + 1, timeStr.length() - 1);
            }
            if (timeStr.indexOf("-") != -1) {
                increaseTime = timeStr.substring(timeStr.indexOf("-"), timeStr.length() - 1);
            }
            if (timeStr.toLowerCase().endsWith("h")) {
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(time);
                cal.add(Calendar.HOUR, Integer.parseInt(increaseTime));
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatStr, Locale.ENGLISH);
                return simpleDateFormat.format(cal.getTimeInMillis());
            } else if (timeStr.toLowerCase().endsWith("m")) {
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(time);
                cal.add(Calendar.MINUTE, Integer.parseInt(increaseTime));
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatStr, Locale.ENGLISH);
                return simpleDateFormat.format(cal.getTimeInMillis());
            }
        }
        return null;
    }

    /**
     * Support two variable substitutions
     * @param result
     * @param scanStartTime
     * @param scanStopTime
     * @return
     */
    public static String replaceTimeVar(String result,String scanStartTime, String scanStopTime) {
        boolean isReplace = false;
        if (result.indexOf("${SCAN_ST") != -1) {
            isReplace = true;
        }
        if (isReplace) {
            if (result.indexOf("${SCAN_START_TIME}") != -1) {

                result = StringUtils.replace(result, "${SCAN_START_TIME}", scanStartTime);
            }
            if (result.indexOf("${SCAN_STOP_TIME") != -1) {

                result = StringUtils.replace(result, "${SCAN_STOP_TIME}", scanStopTime);
            }
        }
        return result;
    }
}
