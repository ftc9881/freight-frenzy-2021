package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

public class AutoStateVuforiaObject extends AutoState implements AutoStateIF {
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
     * {@link #vuforia} is the variable we will use to store our instance of the Vuforia
     * localization engine.
     */
    private VuforiaLocalizer vuforia;

    /**
     * {@link #tfod} is the variable we will use to store our instance of the TensorFlow Object
     * Detection engine.
     */
    private TFObjectDetector tfod;

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
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

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
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABELS);
    }

    public void init(RobotBase robotBase) {
        // The TFObjectDetector uses the camera frames from the VuforiaLocalizer, so we create that
        // first.
        initVuforia(robotBase);
        initTfod(robotBase);

        /**
         * Activate TensorFlow Object Detection before we wait for the start command.
         * Do it here so that the Camera Stream window will have the TensorFlow annotations visible.
         **/
        if (tfod != null) {
            tfod.activate();

            // The TensorFlow software will scale the input images from the camera to a lower resolution.
            // This can result in lower detection accuracy at longer distances (> 55cm or 22").
            // If your target is at distance greater than 50 cm (20") you can adjust the magnification value
            // to artificially zoom in to the center of image.  For best results, the "aspectRatio" argument
            // should be set to the value of the images used to create the TensorFlow Object Detection model
            // (typically 16/9).
            tfod.setZoom(2, 16.0/9.0);
        }
    }
}
