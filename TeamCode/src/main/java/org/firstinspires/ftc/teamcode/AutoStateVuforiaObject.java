package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.RobotLog;
import com.vuforia.RectangleInt;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AutoStateVuforiaObject extends AutoState implements AutoStateIF {
    private static final String CLASS_NAME = "AutoStateVuforiaObject";

    private static final String TFOD_MODEL_ASSET = "FreightFrenzy_BCDM.tflite";

    private static final String[] LABELS = {
            "Ball",
            "Cube",
            "Duck",
            "Marker"
    };

    private static final String VUFORIA_KEY =
            "ARoEcQz/////AAABmbyfYFrgDUG3oErfL4T1xWKA4MoZFSrOSM5/M5tZtJixGWIrmS+N3fIXgbactAKwU6dEq0JOXEgiveG4tOtf2s4Z/iAgX1ksCTqxGS4msVEzJfiHey0JBDuxJfzGz0qNo0fSibtjuu5NSB5QvBT/UI8n6btQ9RlGBd9pYRTDlnw4bhBT/zELJAYrRzy8mDpNF9mybKCnXXMc1kjYj5LCU3NevROI9PJ4Fwx2GMY3DQL67w6TitpkouY3tVD9cWWKkJOUqL3d2UUZsjaGmLD1d8Qhx/J9lAIYA5ww/D7UBPR3/wmhCF3QZg5wgDOXqpIFG+pmQJlH9MLQ0rvQ2+MSdxdKRoooQFbpMUyoi46+iwW1";

    /**
     * {@link #_vuforia} is the variable we will use to store our instance of the Vuforia
     * localization engine.
     */
    private VuforiaLocalizer _vuforia;

    /**
     * {@link #_tfod} is the variable we will use to store our instance of the TensorFlow Object
     * Detection engine.
     */
    private TFObjectDetector _tfod;

    public double _zoom = 2;

    public double _aspectRatio = 16.0/9.0;

    public String _newState = null;

    public class ObjectRegion {
        String _label;
        double _minConfidence = 0;
        double _left = -Double.MAX_VALUE;
        double _right = Double.MAX_VALUE;
        double _top = Double.MAX_VALUE;
        double _bottom = -Double.MAX_VALUE;

        String _state;

        public void configure(JSONObject jsonObject) throws ConfigurationException {
            try {
                if (jsonObject.has("label")) {
                    _label = jsonObject.getString("label");
                }
                if (jsonObject.has("minConfidence")) {
                    _minConfidence = jsonObject.getDouble("minConfidence");
                }
                if (jsonObject.has("left")) {
                    _left = jsonObject.getDouble("left");
                }
                if (jsonObject.has("right")) {
                    _right = jsonObject.getDouble("right");
                }
                if (jsonObject.has("top")) {
                    _top = jsonObject.getDouble("top");
                }
                if (jsonObject.has("bottom")) {
                    _bottom = jsonObject.getDouble("bottom");
                }
                if (jsonObject.has("state")) {
                    _state = jsonObject.getString("state");
                }
            } catch (JSONException e) {
                throw new ConfigurationException(e.getMessage(), e);
            }
        }

        boolean matches(Recognition recognition) {
            if(!recognition.getLabel().equalsIgnoreCase(_label)) {
                return false;
            }

            if(recognition.getConfidence() < _minConfidence) {
                return false;
            }

            return
                recognition.getLeft() <= _right &&
                recognition.getRight() >= _left &&
                recognition.getTop() >= _bottom &&
                recognition.getBottom() <= _top;
        }

        String getState() {
            return _state;
        }
    }

    Map<String, ObjectRegion> _objectRegions = new HashMap<>();

    private void configureObjectRegions(JSONObject jsonObject,
                                      DriveTrainIF driveTrain,
                                      Map<String, DeviceIF> devices,
                                      Map<String, SensorIF> sensors)
            throws JSONException, ConfigurationException {
        JSONArray objectRegionNames = jsonObject.names();

        for(int i = 0; i < objectRegionNames.length(); ++i) {
            String objectRegionName = objectRegionNames.getString(i);

            JSONObject objectRegionConfig = jsonObject.getJSONObject(objectRegionName);

            ObjectRegion objectRegion = new ObjectRegion();

            objectRegion.configure(objectRegionConfig);

            _objectRegions.put(objectRegionName, objectRegion);
        }
    }

    public void configure(JSONObject jsonObject,
                          DriveTrainIF driveTrain,
                          Map<String, DeviceIF> devices,
                          Map<String, SensorIF> sensors)
            throws ConfigurationException {
        super.configure(jsonObject, driveTrain, devices, sensors);

        try {
            if (jsonObject.has("zoom")) {
                _zoom = jsonObject.getDouble("zoom");
            }
            if (jsonObject.has("aspectRatio")) {
                _aspectRatio = jsonObject.getDouble("aspectRatio");
            }
            if(jsonObject.has("objectRegions")) {
                configureObjectRegions(jsonObject.getJSONObject("objectRegions"), driveTrain, devices, sensors);
            } else {
                RobotLog.dd(CLASS_NAME, "No objectRegions specified");
            }
        } catch (JSONException e) {
            throw new ConfigurationException(e.getMessage(), e);
        }
    }


    public AutoStateVuforiaObject(OpMode opMode) {
        super(opMode);
    }

    /**
     * Initialize the Vuforia localization engine.
     */
    private void initVuforia(RobotBase robotBase) {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraName = robotBase.hardwareMap.get(WebcamName.class, "Webcam 1");

        //  Instantiate the Vuforia engine
        _vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the TensorFlow Object Detection engine.
    }

    /**
     * Initialize the TensorFlow Object Detection engine.
     */
    private void initTfod(RobotBase robotBase) {
        int tfodMonitorViewId = robotBase.hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", robotBase.hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minResultConfidence = 0.8f;
        tfodParameters.isModelTensorFlow2 = true;
        tfodParameters.inputSize = 320;
        _tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, _vuforia);
        _tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABELS);
    }

    public void init(RobotBase robotBase) {
        super.init(robotBase);

        // The TFObjectDetector uses the camera frames from the VuforiaLocalizer, so we create that
        // first.
        initVuforia(robotBase);
        initTfod(robotBase);

        if (_tfod != null) {
            _tfod.activate();

            // The TensorFlow software will scale the input images from the camera to a lower resolution.
            // This can result in lower detection accuracy at longer distances (> 55cm or 22").
            // If your target is at distance greater than 50 cm (20") you can adjust the magnification value
            // to artificially zoom in to the center of image.  For best results, the "aspectRatio" argument
            // should be set to the value of the images used to create the TensorFlow Object Detection model
            // (typically 16/9).

            _tfod.setZoom(_zoom, _aspectRatio);
        }
    }

    @Override
    protected boolean doAction(RobotBase robotBase) throws InterruptedException {
        boolean result = super.doAction(robotBase);

        _newState = null;

        if(result) {
            if (_tfod != null) {
                // getUpdatedRecognitions() will return null if no new information is available since
                // the last time that call was made.
                List<Recognition> updatedRecognitions = _tfod.getUpdatedRecognitions();
                if (updatedRecognitions != null) {
                    RobotLog.dd(CLASS_NAME, "# Object Detected: %d", updatedRecognitions.size());
                    // step through the list of recognitions and display boundary info.
                    for (Recognition recognition : updatedRecognitions) {
                        RobotLog.dd(CLASS_NAME, "label: %s",
                                recognition.getLabel()
                        );
                        RobotLog.dd(CLASS_NAME, "left,top %.03f , %.03f",
                                recognition.getLeft(),
                                recognition.getTop()
                        );
                        RobotLog.dd(CLASS_NAME,"right,bottom %.03f , %.03f",
                                recognition.getRight(),
                                recognition.getBottom()
                        );

                        for(String objectRegionID : _objectRegions.keySet()) {
                            ObjectRegion objectRegion = _objectRegions.get(objectRegionID);

                            if(objectRegion.matches(recognition)) {
                                // TODO: This should be changed to have the Object region set a property and then use the usual transition mechanism
                                _newState = objectRegion.getState();
                                return false;
                            }
                        }
                    }
                }
            }

            return true;
        } else {
            return false;
        }
    }

    String evaluateTransitions(Map<String, Object> propertyValues) {
        if(_newState != null) {
            return _newState;
        } else {
            return super.evaluateTransitions(propertyValues);
        }
    }
}
