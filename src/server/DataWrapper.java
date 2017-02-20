package server;

import com.google.gson.Gson;
import sensor.representation.Quaternion;

/**
 * @author Sayantan Majumdar
 */

class DataWrapper {

    private String mOperationType;
    private String mData;
    private Quaternion mQuaternion;
    private boolean mIsInitQuat;

    DataWrapper(String operationType, String data) {
        mOperationType = operationType;
        mData = data;
    }

    DataWrapper(Quaternion quaternionObject, boolean isInitQuat) {
        mOperationType = "Mouse_Move";
        mIsInitQuat = isInitQuat;
        mQuaternion = quaternionObject;
    }

    static String getGsonString(Object object) {
        return new Gson().toJson(object);
    }

    String getOperationType() { return mOperationType; }
    String getData() { return mData; }
    boolean isInitQuat() { return mIsInitQuat; }
    Quaternion getQuaternionObject() { return mQuaternion; }
}
