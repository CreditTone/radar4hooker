package gz.com.alibaba.fastjson.parser.deserializer;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

import gz.com.alibaba.fastjson.JSONException;
import gz.com.alibaba.fastjson.parser.DefaultJSONParser;
import gz.com.alibaba.fastjson.parser.JSONScanner;
import gz.com.alibaba.fastjson.parser.JSONToken;
import gz.com.alibaba.fastjson.parser.deserializer.AbstractDateDeserializer;
import gz.com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;

public class SqlDateDeserializer extends AbstractDateDeserializer implements ObjectDeserializer {

    public final static SqlDateDeserializer instance = new SqlDateDeserializer();

    @SuppressWarnings("unchecked")
    protected <T> T cast(DefaultJSONParser parser, Type clazz, Object fieldName, Object val) {
        if (val == null) {
            return null;
        }

        if (val instanceof Date) {
            val = new java.sql.Date(((Date) val).getTime());
        } else if (val instanceof Number) {
            val = (T) new java.sql.Date(((Number) val).longValue());
        } else if (val instanceof String) {
            String strVal = (String) val;
            if (strVal.length() == 0) {
                return null;
            }

            long longVal;

            JSONScanner dateLexer = new JSONScanner(strVal);
            try {
                if (dateLexer.scanISO8601DateIfMatch()) {
                    longVal = dateLexer.getCalendar().getTimeInMillis();
                } else {

                    DateFormat dateFormat = parser.getDateFormat();
                    try {
                        Date date = (Date) dateFormat.parse(strVal);
                        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                        return (T) sqlDate;
                    } catch (ParseException e) {
                        // skip
                    }

                    longVal = Long.parseLong(strVal);
                }
            } finally {
                dateLexer.close();
            }
            return (T) new java.sql.Date(longVal);
        } else {
            throw new JSONException("parse error : " + val);
        }

        return (T) val;
    }

    public int getFastMatchToken() {
        return JSONToken.LITERAL_INT;
    }
}
