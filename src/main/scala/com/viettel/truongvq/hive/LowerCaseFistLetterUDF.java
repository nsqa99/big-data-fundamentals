package com.viettel.truongvq.hive;

import org.apache.hadoop.hive.ql.exec.UDFArgumentException;

import org.apache.hadoop.hive.ql.exec.UDFArgumentLengthException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDF;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDFUtils;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.PrimitiveObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorConverter;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorConverter.*;


public class LowerCaseFistLetterUDF extends GenericUDF {
    private transient StringConverter stringConverter;
    private final transient PrimitiveObjectInspector.PrimitiveCategory returnType = PrimitiveObjectInspector.PrimitiveCategory.STRING;
    private transient GenericUDFUtils.StringHelper returnHelper;

    @Override
    public ObjectInspector initialize(ObjectInspector[] arguments) throws UDFArgumentException {
        if (arguments.length != 1) {
            throw new UDFArgumentLengthException("Accept only one arg");
        }
        PrimitiveObjectInspector argumentOI = (PrimitiveObjectInspector) arguments[0];

        stringConverter = new PrimitiveObjectInspectorConverter.StringConverter(argumentOI);
        returnHelper = new GenericUDFUtils.StringHelper(returnType);
        return arguments[0];
    }

    @Override
    public Object evaluate(DeferredObject[] arguments) throws HiveException {
        try {
            String arg = (String) stringConverter.convert(arguments[0].get());
            if (arg != null && arg.length() > 0) {
                String result = arg.substring(0, 1).toLowerCase() + arg.substring(1);
                return returnHelper.setReturnValue(result);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return arguments[0];
    }

    @Override
    public String getDisplayString(String[] children) {
        return "";
    }
}
