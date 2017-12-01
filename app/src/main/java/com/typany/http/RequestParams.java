package com.typany.http;

import android.text.TextUtils;

import org.apache.http.protocol.HTTP;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class RequestParams {

    private static final String DEFAULT_CHARSET = HTTP.UTF_8;
    
    private String charset = DEFAULT_CHARSET;

    private Map<String,StringContent> stringsParams;
    private Map<String,FileContent> filesParams;

    private Map<String, String> headersParams;


    public RequestParams() {
    }

    public RequestParams(String charset) {
        if (!TextUtils.isEmpty(charset)) {
            this.charset = charset;
        }
    }

    public void addHeaderParameter(String name,String value){
        if (headersParams == null) {
            headersParams = new HashMap<String, String>();
        }
        headersParams.put(name,value);
    }

    public Map<String,String> getHeadersParams(){
        return headersParams;
    }

    public void addBodyParameter(String name, String value) {
        if (stringsParams == null) {
            stringsParams = new HashMap<String, StringContent>();
        }
        StringContent content=new StringContent(value,null);
        stringsParams.put(name, content);
    }

    public void addBodyParameter(String name, String value,String charset) {
        if (stringsParams == null) {
            stringsParams = new HashMap<String, StringContent>();
        }
        StringContent content=new StringContent(value,charset);
        stringsParams.put(name, content);
    }

    public void addBodyParameter(String key, File file) {
        addBodyParameter(key, file, null);
    }

    public void addBodyParameter(String key, File file, String mimeType) {
        addBodyParameter(key,file,mimeType,null);
    }

    public void addBodyParameter(String key, File file, String mimeType, String charset) {
        if (filesParams == null) {
            filesParams = new HashMap<String, FileContent>();
        }
        filesParams.put(key, new FileContent(file, null, mimeType, charset));
    }

    Map<String,StringContent> getStringsParams(){
        return stringsParams;
    }

    Map<String,FileContent> getFilesParams(){
        return filesParams;
    }


    public static class StringContent{
        private final String value;
        private final String charset;

        public StringContent(String value,String charset){
            this.value=value;
            if(TextUtils.isEmpty(charset)){
                this.charset=DEFAULT_CHARSET;
            }else{
                this.charset = charset;
            }
        }

        public String getValue(){
            return value;
        }

        public String getCharset(){
            return charset;
        }
    }



    public static class FileContent{
        private final File file;
        private final String filename;
        private final String charset;
        private final String mimeType;

        public FileContent(final File file, String filename, String mimeType, String charset) {
            if (file == null) {
                throw new RuntimeException("file is null!");
            }
            this.file = file;
            if (filename != null) {
                this.filename = filename;
            } else {
                this.filename = file.getName();
            }
            if(TextUtils.isEmpty(charset)){
                this.charset=DEFAULT_CHARSET;
            }else{
                this.charset = charset;
            }


            this.mimeType=mimeType;
        }

        public File getFile() {
            return file;
        }

        public String getFilename() {
            return filename;
        }

        public String getCharset() {
            return charset;
        }

        public String getMimeType() {
            return mimeType;
        }
    }
}
