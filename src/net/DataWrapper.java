package net;

import com.google.gson.Gson;
import sensor.representation.Quaternion;

/**
 * Encapsulates the data received from client during TCP communication.
 *
 * <p>
 *     This data contains all the information required by the
 *     {@link device.MouseController} and the
 *     {@link device.KeyboardController} for doing the different input
 *     operations. It contains a <i>type of operation</i> field which
 *     helps to determine the operation requested by the client and
 *     a <i>data</i> field which acts as the operand for the input
 *     operations.
 * </p>
 * <p>
 *     For keyboard input, the <i>type of operation</i> field contains
 *     <code>Key</code> and the <i>data</i> field contains the sequence
 *     of characters and keyboard strokes for input.
 * </p>
 * <p>
 *     For 3-D mouse movement operation, the <i>type of operation</i>
 *     field contains <code>Mouse_Move</code> and the <i>data</i> field
 *     contains a {@link sensor.representation.Quaternion} object.
 * </p>
 * <p>
 *     For 2-D mouse movement operation, the <i>type of operation</i>
 *     field contains <code>Mouse_Touch</code> and the <i>data</i> field
 *     contains a pair of integers representing the displacement along the
 *     x-axis and y-axis relative to the current mouse pointer location.
 * </p>
 * <p>
 *     For mouse button click operation, the <i>type of operation</i>
 *     field contains <code>Mouse_Button</code> and the <i>data</i> field
 *     contains any of the following :
 *     <ul>
 *         <li><code>left</code> - representing left click.</li>
 *         <li><code>right</code>  - representing right click.</li>
 *         <li><code>middle</code> - representing middle button click.</li>
 *         <li><code>upscroll</code> - representing scroll up.</li>
 *         <li><code>downscroll</code> - representing scroll down.</li>
 *     </ul>
 * </p>
 *
 * @see sensor.representation.Quaternion
 * @see device.MouseController
 * @see device.KeyboardController
 */

class DataWrapper {

    private String mOperationType;
    private String mData;
    private Quaternion mQuaternion;

    private boolean mIsInitQuat;
    private int mMoveX;
    private int mMoveY;

    /**
     * Constructor. <br/>
     * Used to create a <code>DataWrapper</code> for keyboard and mouse
     * button click operations.
     *
     * @param operationType the <i>type of operation field</i>.
     * @param data the <i>data</i> field.
     */
    DataWrapper(String operationType, String data) {
        mOperationType = operationType;
        mData = data;
    }

    /**
     * Constructor. <br/>
     * Used to create a <code>DataWrapper</code> for 2-D mouse movement
     * operation. The <i>type of operation</i> field is implicit.
     *
     * @param x displacement data along x-axis.
     * @param y displacement data along y-axis.
     */
    DataWrapper(int x, int y) {
        mMoveX = x;
        mMoveY = y;
        mOperationType = "Mouse_Touch";
    }

    /**
     * Constructor. <br/>
     * Used to create a <code>DataWrapper</code> for 3-D mouse movement
     * operation. The <i>type of operation</i> field is implicit.
     *
     * @param quaternionObject a {@link sensor.representation.Quaternion}
     *                         object.
     * @param isInitQuat <code>true</code>, if <code>quaternionObject</code>
     *                   is the initial, <br/>
     *                   <code>false</code>, otherwise.
     * @see sensor.representation.Quaternion
     */
    DataWrapper(Quaternion quaternionObject, boolean isInitQuat) {
        mOperationType = "Mouse_Move";
        mIsInitQuat = isInitQuat;
        mQuaternion = quaternionObject;
    }

    /**
     * Returns a JSon-string of an object.
     *
     * @param object the input {@link java.lang.Object}.
     * @return a JSon-string
     * @see com.google.gson.Gson
     */
    static String getGsonString(Object object) {
        return new Gson().toJson(object);
    }

    /**
     * Returns the <i>type of operation</i> field of this
     * <code>DataWrapper</code>.
     *
     * @return the <i>type of operation</i> field.
     */
    String getOperationType() { return mOperationType; }

    /**
     * Returns the <i>data</i> field of this <code>DataWrapper</code>
     * for keyboard and mouse button click operations.
     *
     * @return the <i>data</i> field.
     */
    String getData() { return mData; }

    /**
     * Checks if the stored {@link sensor.representation.Quaternion}
     * object is the initial one.
     *
     * @return <code>true</code>, if it is initial, <br/>
     *         <code>false</code>, otherwise.
     */
    boolean isInitQuat() { return mIsInitQuat; }

    /**
     * Returns the {@link sensor.representation.Quaternion} object
     * stored in this <code>DataWrapper</code>.
     *
     * @return the {@link sensor.representation.Quaternion} object.
     */
    Quaternion getQuaternionObject() { return mQuaternion; }

    /**
     * Returns the displacement value along x-axis stored in this
     * <code>DataWrapper</code>.
     *
     * @return the displacement along x-axis.
     */
    int getX() { return mMoveX; }

    /**
     * Returns the displacement value along y-axis stored in this
     * <code>DataWrapper</code>.
     *
     * @return the displacement along y-axis.
     */
    int getY() { return mMoveY; }
}