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
		Vector3f xvec = rotateVector(new Vector3f(1.0f,0.0f,0.0f), quaternion);
		Vector3f zvec = rotateVector(new Vector3f(0.0f,0.0f,1.0f), quaternion);
		double blh = -1.0f * Math.atan2(xvec.getY(),xvec.getX());
		mHcos = Math.cos(blh);
		mHsin = Math.sin(blh);
		mBli = Math.asin(zvec.getY());
	}

	//Get Pointer Location from Quaternion
	public Cartesian2D pointerUpdate(Quaternion currentQuaternion) {
		Vector3f cxv = rotateVector(new Vector3f(1.0f, 0.0f, 0.0f), currentQuaternion);
		Vector3f czv = rotateVector(new Vector3f(0.0f, 0.0f, 1.0f), currentQuaternion);
		double xeff = (mHcos * cxv.getX()) - (mHsin * cxv.getY());
		double yeff = (mHsin * cxv.getX()) + (mHcos * cxv.getY());
		double hdel = Math.atan2(yeff, xeff);
		double cli = Math.asin(czv.getY());
		double yEst = mSensitivity * Math.tan(mBli - cli);
		double xEst = mSensitivity * Math.tan(hdel);
        initFix(currentQuaternion);
		return new Cartesian2D(xEst,yEst);
	}

	//Rotate a Vector
	/*
	private Vector3f rotateVector(Vector3f v, Quaternion q) {
		Quaternion qt = new Quaternion();
		qt.copyFromVec3(v,0.0f);
		Quaternion cqt = new Quaternion();
		cqt.setX(-1.0f*q.getX());
		cqt.setY(-1.0f*q.getY());
		cqt.setZ(-1.0f*q.getZ());
		cqt.setW(q.getW());
		q.multiplyByQuat(qt,qt);
		qt.multiplyByQuat(cqt,qt);
		return new Vector3f(qt.getX(),qt.getY(),qt.getZ());
	}
	*/
	private Vector3f rotateVector(Vector3f v, Quaternion q) {
		float q0 = q.getW();
		float q1 = q.getX();
		float q2 = q.getY();
		float q3 = q.getZ();
		float v1 = v.getX();
		float v2 = v.getY();
		float v3 = v.getZ();
		float x = (1-2*q2*q2-2*q3*q3)*v1 + 2*(q1*q2+q0*q3)*v2 + 2*(q1*q3-q0*q2)*v3;
		float y = 2*(q1*q2-q0*q3)*v1 + (1-2*q1*q1-2*q3*q3)*v2 + 2*(q2*q3+q0*q1)*v3;
		float z = 2*(q1*q3-q0*q2) + (q2*q3-q0*q1)*v2 + (1-2*q1*q1-2*q2*q2)*v3;
		return new Vector3f(x,y,z);
	}
}
