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
 *     {@code Key} and the <i>data</i> field contains the sequence of
 *     characters and keyboard strokes for input.
 * </p>
 * <p>
 *     For 3-D mouse movement operation, the <i>type of operation</i>
 *     field contains {@code Mouse_Move} and the <i>data</i> field
 *     contains a {@link Quaternion} object.
 * </p>
 * <p>
 *     For 2-D mouse movement operation, the <i>type of operation</i>
 *     field contains {@code Mouse_Touch} and the <i>data</i> field
 *     contains a pair of integers representing the cartesian coordinates
 *     of the mouse pointer location.
 * </p>
 * <p>
 *     For mouse button click operation, the <i>type of operation</i>
 *     field contains {@code Mouse_Button} and the <i>data</i> field
 *     contains any of the following :
 *     <ul>
 *         <li>{@code left} - representing left click.</li>
 *         <li>{@code right}  - representing right click.</li>
 *         <li>{@code middle} - representing middle button click.</li>
 *         <li>{@code upscroll} - representing scroll up.</li>
 *         <li>{@code downscroll} - representing scroll down.</li>
 *     </ul>
 * </p>
 *
 * @see Quaternion
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
     * Used to create a {@code DataWrapper} for keyboard and mouse
     * button click operations.
     * @param operationType the <i>type of operation field</i>.
     * @param data the <i>data</i> field.
     */
    DataWrapper(String operationType, String data) {
        mOperationType = operationType;
        mData = data;
    }

    /**
     * Constructor. <br/>
     * Used to create a {@code DataWrapper} for 2-D mouse movement
     * operation.
     * @param x x-coordinate data.
     * @param y y-coordinate data.
     */
    DataWrapper(int x, int y) {
        mMoveX = x;
        mMoveY = y;
        mOperationType = "Mouse_Touch";
    }

    /**
     * Constructor. <br/>
     * Used to create a {@code DataWrapper} for 3-D mouse movement
     * operation.
     * @param quaternionObject a {@link Quaternion} object.
     * @param isInitQuat {@code true}, if {@code quaternionObject}
     *                   is is the initial {@link Quaternion}, <br/>
     *                   {@code false}, otherwise.
     * @see Quaternion
     */
    DataWrapper(Quaternion quaternionObject, boolean isInitQuat) {
        mOperationType = "Mouse_Move";
        mIsInitQuat = isInitQuat;
        mQuaternion = quaternionObject;
    }

    /**
     * Returns a JSon-string of an object.
     *
     * @param object the input {@link Object}.
     * @return a JSon-string
     * @see Gson
     */
    static String getGsonString(Object object) {
        return new Gson().toJson(object);
    }

    /**
     * Returns the <i>type of operation</i> field of this
     * {@code DataWrapper}.
     *
     * @return the <i>type of operation</i> field.
     */
    String getOperationType() { return mOperationType; }

    /**
     * Returns the <i>data</i> field of this {@code DataWrapper}
     * for keyboard and mouse button click operations.
     *
     * @return the <i>data</i> field.
     */
    String getData() { return mData; }

    /**
     * Check if the stored {@link Quaternion} object is the initial
     * one.
     *
     * @return {@code true}, if it is initial, <br/>
     *         false, otherwise.
     */
    boolean isInitQuat() { return mIsInitQuat; }

    /**
     * Returns the {@link Quaternion} object stored in this
     * {@code DataWrapper}.
     *
     * @return the {@link Quaternion} object.
     */
    Quaternion getQuaternionObject() { return mQuaternion; }

    /**
     * Returns the x-coordinate value stored in this {@code DataWrapper}.
     *
     * @return the x-coordinate.
     */
    int getX() { return mMoveX; }

    /**
     * Returns the y-coordinate value stored in this {@code DataWrapper}.
     *
     * @return the y-coordinate.
     */
    int getY() { return mMoveY; }
}