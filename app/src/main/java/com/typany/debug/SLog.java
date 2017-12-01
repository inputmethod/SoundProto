package com.typany.debug;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Locale;

import static java.util.Calendar.MINUTE;


@SuppressWarnings("unused")
public final class SLog {
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    private static final int LENGTH = 5;
    private static boolean sLogEnabled = false;
    private static boolean sLogSaveFile = false;
    private static String tag = "";
    private static SLog ins = null;
    private static final ArrayList<String> sCache = new ArrayList<>(128);
    private static SimpleDateFormat sDateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ROOT);
    public static void initialize(boolean enabled, boolean saveFile) {
        final String packageName = SLog.class.getPackage().getName();

        Log.w(packageName, "SLog is " + Boolean.toString(enabled));
        sLogEnabled = enabled;
        sLogSaveFile = saveFile;
    }

    public static boolean isLogEnabled() {
        return sLogEnabled;
    }

    public static boolean isLogSaveFile() {
        return sLogSaveFile;
    }

    public static SLog tag() {
        if (!sLogEnabled) return new SLog();
        return tag(Thread.currentThread().getStackTrace()[3].getClass().getSimpleName());
    }

    public static SLog tag(String tag0) {
        if (ins == null) {
            ins = new SLog();
        }
        tag = tag0;
        return ins;
    }

    public static void p(String fmt, Object... arg) {
        Log.i(tag, String.format(fmt, arg));
    }

    public static int i(String TAG, String msg) {
        if (sLogEnabled) {
            return Log.i(TAG, msg);
        }
        return -1;
    }

    public static int i(String TAG, String msg, Throwable tr) {
        if (sLogEnabled) {
            return Log.i(TAG, msg, tr);
        }
        return -1;
    }

    public static int d(String TAG, String msg) {
        if (sLogEnabled) {
            saveLog("debug", TAG, msg);
            return Log.d(TAG, msg);
        }
        return -1;
    }

    public static int d(String TAG, String msg, Throwable tr) {
        if (sLogEnabled) {
            saveLog("debug", TAG, msg + LINE_SEPARATOR + Log.getStackTraceString(tr));
            return Log.d(TAG, msg, tr);
        }
        return -1;
    }

    public static int w(String TAG, String msg) {
        if (sLogEnabled) {
            saveLog("warning", TAG, msg);
            return Log.w(TAG, msg);
        }
        return -1;
    }

    public static int w(String TAG, String msg, Throwable tr) {
        if (sLogEnabled) {
            saveLog("warning", TAG, msg + LINE_SEPARATOR + Log.getStackTraceString(tr));
            return Log.w(TAG, msg, tr);
        }
        return -1;
    }

    public static int e(String TAG, String msg) {
        if (sLogEnabled) {
            saveLog("error", TAG, msg);
            return Log.e(TAG, msg);
        }
        return -1;
    }

    public static int e(String TAG, String msg, Throwable tr) {
        if (sLogEnabled) {
            saveLog("error", TAG, msg + LINE_SEPARATOR + Log.getStackTraceString(tr));
            return Log.e(TAG, msg, tr);
        }
        return -1;
    }

    public static int v(String TAG, String msg) {
        if (sLogEnabled) {
            saveLog("verbose", TAG, msg);
            return Log.v(TAG, msg);
        }
        return -1;
    }

    public static int v(String TAG, String msg, Throwable tr) {
        if (sLogEnabled) {
            saveLog("verbose", TAG, msg + LINE_SEPARATOR + Log.getStackTraceString(tr));
            return Log.v(TAG, msg, tr);
        }
        return -1;
    }

    public static int printMethodName(String TAG) {
        if (!sLogEnabled) {
            return -1;
        }
        String msg = "";
        StackTraceElement info = SLog.getInfoInternal(5);
        if (info != null) {
            msg = info.getMethodName();
        }
        return Log.i(TAG, msg);
    }

    public static String getUsageLocation() {
        StackTraceElement[] ste = Thread.currentThread().getStackTrace();
        if (ste != null && ste.length > 5) {
            StackTraceElement caller = ste[4];

            return caller.getFileName() + ":" + caller.getLineNumber();
        }
        return "UNKNOWN";
    }

    public static String getCallerName() {
        StackTraceElement[] ste = Thread.currentThread().getStackTrace();
        if (ste != null && ste.length > 5) {
            return ste[4].getMethodName();
        }
        return "UNKNOWN";
    }

    public static String getCurrentStack(int maxStack, String except) {
        final String thisExcept = SLog.class.getName();
        StringBuilder msg = new StringBuilder();
        StackTraceElement[] stackTraceElements = new Exception()
                .getStackTrace();
        if (stackTraceElements != null) {
            msg.append("PrintStackTrace:\n");
            for (int i = 1; i < (stackTraceElements.length > maxStack ? maxStack : stackTraceElements.length); i++) {
                final String className = stackTraceElements[i].getClassName();
                final int ret = thisExcept.compareTo(className);
                if (0 != ret) {
                    if (TextUtils.isEmpty(except) || 0 != except.compareTo(className)) {
                        msg.append("    ").append(stackTraceElements[i].toString()).append("\n");
                    }
                }
            }
        }

        return msg.toString();
    }

    public static int printCallerName(String TAG) {
        if (!sLogEnabled) {
            return -1;
        }
        String msg = "";
        StackTraceElement[] ste = Thread.currentThread().getStackTrace();
        if (ste != null && ste.length > 5) {
            msg = ste[4].getMethodName();
        }
        return Log.w(TAG, msg);
    }

    public static int printStackTrace(String TAG) {
        return printStackTrace(TAG, 20);
    }

    public static int printStackTrace(String TAG, int maxStack) {
        if (!sLogEnabled) {
            return -1;
        }
        Log.e(TAG, getCurrentStack(maxStack, ""));

        return 0;
    }

    public static int printStackTrace(String TAG, int maxStack, String exceptClassName) {
        if (!sLogEnabled) {
            return -1;
        }
        Log.e(TAG, getCurrentStack(maxStack, exceptClassName));

        return 0;
    }

    private static String getInfo(InfoType type) {
        String ret = "";

        StackTraceElement[] ste =
                Thread.currentThread().getStackTrace();
        if (ste != null && ste.length >= LENGTH) {
            StackTraceElement stackTraceElement = ste[LENGTH - 1];
            switch (type) {
                case FILE_NAME:
                    ret = stackTraceElement.getFileName();
                    break;
                case METHOD_NAME:
                    ret = stackTraceElement.getMethodName();
                    break;
                case CLASS_NAME:
                    ret = stackTraceElement.getClassName();
                    break;
                case LINE_NUM:
                    ret = Integer.toString(stackTraceElement.getLineNumber());
                    break;
                default:
                    break;
            }
        }
        return ret;
    }

    public static String getFileName() {
        return getInfo(InfoType.FILE_NAME);
    }

    public static String getMethodName() {
        return getInfo(InfoType.METHOD_NAME);
    }

    public static String getClassName() {
        return getInfo(InfoType.CLASS_NAME);
    }

    public static String getLineNumber() {
        return getInfo(InfoType.LINE_NUM);
    }

    private static StackTraceElement getInfoInternal(int length) {
        StackTraceElement ret = null;

        StackTraceElement[] ste = Thread.currentThread().getStackTrace();
        if (ste != null && ste.length >= length) {
            ret = ste[length - 1];
        }
        return ret;
    }

    public static String getStackTrace() {
        StringBuilder sb = new StringBuilder();
        StackTraceElement[] ste = Thread.currentThread().getStackTrace();
        for (StackTraceElement st : ste) {
            if (st != null) {
                sb.append("[ ")
                        .append(st.getFileName())
                        .append(":")
                        .append(st.getLineNumber())
                        .append(" ]")
                        .append(LINE_SEPARATOR);
            }
        }
        return sb.toString();
    }

    public static void forceFlushLog() {
        if (!sLogSaveFile) {
            return;
        }
        if (!sCache.isEmpty()) {
            try {
                synchronized (sCache) {
                    flushLog(sCache);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            sCache.clear();
        }
    }

    private static String formatMillis(long ms) {
        GregorianCalendar time = new GregorianCalendar();
        time.setTimeInMillis(ms);

        return String.format(Locale.US, "%04d%02d%02d-%02d:%02d:%02d", time.get(Calendar.YEAR), time.get(Calendar.MONTH), time.get(Calendar.DAY_OF_MONTH),
                time.get(Calendar.HOUR_OF_DAY), time.get(MINUTE), time.get(Calendar.SECOND));
    }

    public static String dumpCache() {
        StringBuilder text = new StringBuilder(1024 * 10);

        for (String line : sCache) {
            text.append(line);
        }

        return text.toString();
    }

    private static void saveLog(String level, String Tag, String msg) {
        if (!sLogSaveFile) {
            return;
        }

        synchronized (sCache) {
            if (sCache.size() < 128) {
                sCache.add("[" + formatMillis(System.currentTimeMillis()) + "]{" + level + "}" + "<" + Tag + ">" + msg
                        + LINE_SEPARATOR);
            } else {
                // flush and clear
                try {
                    flushLog(sCache);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                sCache.clear();
            }
        }
    }

    private static String convertFilename(long timestamp, String ext) {
        return sDateFormatter.format(new Date(timestamp)) + ext;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static File getLogDirectory() {
        String dirName = "SLog";

        File dir = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            dir = new File(Environment.getExternalStorageDirectory(), dirName);
            if (!dir.exists()) {
                dir.mkdirs();
            }
        }

        return dir;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static File getLogFileHandle(String filename) {
        File dir = getLogDirectory();
        if (dir == null) {
            return null;
        }
        if (!dir.exists()) {
            dir.mkdirs();
        }
        if (dir.exists()) {
            return new File(dir, filename);
        }
        return null;
    }

    private static void flushLog(ArrayList<String> cache) throws IOException {
        long timestamp = System.currentTimeMillis();
        String filename = convertFilename(timestamp, ".txt");
        File file = getLogFileHandle(filename);
        //for not change too much, need confirm later.
        if (file == null) {
            return;
        }
        Log.i(SLog.class.getPackage().getName(), "output file path = " + file.getAbsolutePath());

        FileOutputStream fos = new FileOutputStream(file, file.exists());
        PrintStream ps = new PrintStream(fos, true, "UTF-8");

        Iterator<String> it = cache.iterator();
        while (it.hasNext()) {
            String log = it.next();
            ps.append(log);
        }

        ps.flush();
        ps.close();
    }

    private enum InfoType {
        FILE_NAME,
        METHOD_NAME,
        CLASS_NAME,
        LINE_NUM
    }
}
