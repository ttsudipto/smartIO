package project.pc.sensor;

import project.pc.sensor.representation.Cartesian2D;
import project.pc.sensor.representation.Quaternion;
import project.pc.sensor.representation.Vector3f;

/**
 * Class to transform filtered sensor data into mouse pointer movement data
 *
 */
public class SensorDataHandler {
	//Sensitivity of Mouse Movement [Experimental Value]
	/**
	 * @param mSensitivity Sensitivity co-efficient of the mouse pointer movement.
	 */
	private float mSensitivity;

	/**
	 * @param mHcos <code>cosine</code> of heading.
	 */
	private double mHcos;

	/**
	 * @param mHsin <code>sine</code> of heading.
	 */
    private double mHsin;

    /**
	 * @param mBli Baseline Index.
	 */
	private double mBli;

	/**
	 * Constructor.
	 *
	 * Creates an instance of <code>SensorDataHandler</code>.
	 *
	 * @param q Initial <code>Quaternion</code> object.
	 * @param sensitivity Sensitivity co-efficient of the mouse movement.
	 */
	public SensorDataHandler(Quaternion q, float sensitivity) {
		mSensitivity = sensitivity;
		initFix(q);
	}

	/**
	 *
	 * Initializes the baseline orientation.
	 *
	 * @param quaternion Initial <code>Quaternion</code> object.
	 */
	//Baseline Initialization
	private void initFix(Quaternion quaternion) {
		Vector3f xvec = quaternion.rotateVector(new Vector3f(1.0f,0.0f,0.0f));
		Vector3f zvec = quaternion.rotateVector(new Vector3f(0.0f,0.0f,1.0f));
		double blh = -1.0f * Math.atan2(xvec.getY(),xvec.getX());
		mHcos = Math.cos(blh);
		mHsin = Math.sin(blh);
		mBli = Math.asin(zvec.getY());
	}

	/**
	 *
	 * Determines pointer location from current <code>Quaternion</code> object.
	 * @param currentQuaternion Current <code>Quaternion</code> object acquired after filtering.
	 * @return an object of {@link project.pc.sensor.representation.Cartesian2D} containing current pointer location.
	 *
	 */
	//Get Pointer Location from Quaternion
	public Cartesian2D pointerUpdate(Quaternion currentQuaternion) {
		Vector3f cxv = currentQuaternion.rotateVector(new Vector3f(1.0f, 0.0f, 0.0f));
		Vector3f czv = currentQuaternion.rotateVector(new Vector3f(0.0f, 0.0f, 1.0f));
		double xeff = (mHcos * cxv.getX()) - (mHsin * cxv.getY());
		double yeff = (mHsin * cxv.getX()) + (mHcos * cxv.getY());
		double hdel = Math.atan2(yeff, xeff);
		double cli = Math.asin(czv.getY());
		double yEst = mSensitivity * Math.tan(mBli - cli);
		double xEst = mSensitivity * Math.tan(hdel);
        initFix(currentQuaternion);
		return new Cartesian2D(xEst,yEst);
	}
}