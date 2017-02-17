package sensor;

import sensor.representation.Cartesian2D;
import sensor.representation.Quaternion;
import sensor.representation.Vector3f;

/**
 * @author Abhisek Maiti
 * @author Sayantan Majumdar
 */
public class SensorDataHandler {
	//Sensitivity of Mouse Movement [Experimental Value]
	private float mSensitivity;
	private double mHcos;
    private double mHsin;
	private double mBli;

	public SensorDataHandler(Quaternion q, float sensitivity) {
		mSensitivity = sensitivity;
		initFix(q);
	}

	//Baseline Initialization
	private void initFix(Quaternion quaternion) {
		Vector3f xvec = quaternion.rotateVector(new Vector3f(1.0f,0.0f,0.0f));
		Vector3f zvec = quaternion.rotateVector(new Vector3f(0.0f,0.0f,1.0f));
		double blh = -1.0f * Math.atan2(xvec.getY(),xvec.getX());
		mHcos = Math.cos(blh);
		mHsin = Math.sin(blh);
		mBli = Math.asin(zvec.getY());
	}

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
