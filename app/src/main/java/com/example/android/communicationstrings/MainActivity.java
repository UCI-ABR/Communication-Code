package com.example.android.communicationstrings;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import static android.R.attr.logo;
import static android.R.attr.mode;
import static android.R.attr.value;

public class MainActivity extends AppCompatActivity {

    //for Master:
    String fromMinion = "", toMinion = "";
    enum Robots {
        DOC, MR, MRS, CARLITO, CARLOS, CARLY, CARLA, CARLETON,
    }
    Robots minion;
    Robot robot, doc, mr, mrs, carlito, carlos, carly, carla, carleton;
    double[] gps_coords;
    double[][] gpsList = new  double[50][2];
    int gpsListCounter = 0;

    //for Minion:
    String fromMaster = "", toMaster = "";

    double[] lidarGPS = {0,0};
    double[] destinationCoords = {0,0};
    boolean autoMode = false;

    //for both:
    boolean initialFieldScan = true,
            isMannequinFound = false,
            scanMode = true;

    byte hex = (byte)0x90;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        for (int i=0; i<8; i++) {
            Log.i("BIT","#" + i + " = " + bitRead(hex,i));
        }

        fromMinion = "NAME: CARLOS, " +
                "GPS[LAT:1.11, LON:1.11], " +
                "MANN: true" +
                "LGPS[LAT:1.55, LON:1.55];";

        fromMaster = "AUTOMODE: true" +
                     "SCANMODE: true" +
                     "DEST:[LAT:1.2555, LON:17.55]";

        receive_from_m();
        Log.i("TEST", "NAME: " + minion + ", " +
                "GPS" + "[" + gps_coords[0] + ", " + gps_coords[1] + "], " +
                "MANN: " + isMannequinFound + ", " +
                "LGPS: " + gpsList[0]);

//        send_to_M();
//        Log.i("TEST","" + toMaster);

//        fromMaster = "GRID: 123";
//        receive_from_M();
//        Log.i("TEST", "" + grid_number);
    }

    //read position pos from byte aByte
    private int bitRead(byte value, int bit) {
        return (((value) >> (bit)) & 0x01);
    }   /* bitRead */


    //receives info in form of string & parses to variables' appropriate types
    void receive_from_m (String data) {
        String string_name = data.substring(data.indexOf("NAME"),data.indexOf("GPS")),
                string_gps = data.substring(data.indexOf("GPS"),data.indexOf("MANN")),
                string_mann = data.substring(data.indexOf("MANN"),data.indexOf("LGPS")),
                string_lidarGPS = data.substring(data.indexOf("LGPS"),data.length());

        //get name of minion that sent string
        string_name = string_name.substring(string_name.indexOf(":")+2,string_name.indexOf(","));

        // Set robot name from message string
        switch (string_name) {
            case "DOC":
                robot = doc;
                break;
            case "MR":
                robot = mr;
                break;
            case "MRS":
                robot = mrs;
                break;
            case "CARLITO":
                robot = carlito;
                break;
            case "CARLOS":
                robot = carlos;
                break;
            case "CARLY":
                robot = carly;
                break;
            case "CARLA":
                robot = carla;
                break;
            case "CARLETON":
                robot = carleton;
                break;
            default:
                Log.i("ERROR","Invalid robot name. Refer to Robot name list in code.");
        }

        // Set robot location from message string
        robot.setLocation(getCoords(string_gps));

        // Set mannequin found status from message string
        robot.setStatus(string_mann.contains("true"));
        /*

        //add LIDAR gps calculation of victim to list of gps coordinates
        if (hasLIDAR) {
            if (gpsListCounter < gpsList[0].length) {
                gpsList[gpsListCounter] = getCoords(string_lidarGPS);
                gpsListCounter++;
            } else {
                Log.i(TAG1,"gpsList is full!");
            }
        }

        */
    }

    //sends instructions to m
    void send_to_m (Robots name, boolean mode, boolean scanMode, double[] dest) {
        String toMinion = "AUTOMODE: " + mode +
                "SCANMODE: " + scanMode +
                "DEST[LAT:" + dest[0] + ", LON:" + dest[1] + "], ";

        if (scanMode) {
            switch (name) {
                case DOC:
                    //ADD SEND CODE
                    break;
                case MR:
                    //ADD SEND CODE
                    break;
                case MRS:
                    //ADD SEND CODE
                case CARLITO:
                    //ADD SEND CODE
                    break;
                default:
                    Log.i("ERROR", "Invalid robot name. Robot not listed as enabled with LIDAR. Only robots with LIDAR can move right now.");
            }
        } else {
            switch (name) {
                case DOC:
                    //ADD SEND CODE
                    break;
                case MR:
                    //ADD SEND CODE
                    break;
                case MRS:
                    //ADD SEND CODE
                case CARLITO:
                    //ADD SEND CODE
                    break;
                case CARLOS:
                    //ADD SEND CODE
                    break;
                case CARLY:
                    //ADD SEND CODE
                    break;
                case CARLA:
                    //ADD SEND CODE
                    break;
                case CARLETON:
                    //ADD SEND CODE
                    break;
                default:
                    Log.i("ERROR", "Invalid robot name. Refer to Robot name list in code.");
            }
        }
    }

    // receives string from master and assigns data to variables
    void receive_from_M (String data) {
        String string_auto_mode = data.substring(data.indexOf("AUTOMODE"), data.indexOf("SCANMODE")),
                string_scan_mode= data.substring(data.indexOf("SCANMODE"), data.indexOf("DEST")),
                string_coords = data.substring(data.indexOf("DEST"), data.length());

        autoMode = string_auto_mode.contains("true");
        scanMode = string_scan_mode.contains("true");
        destinationCoords = getCoords(string_coords);
    }

    // sends minion's data to master
    void send_to_M (Robot name, double[] location, Boolean foundMann, double[] lgps) {

        // Add robot name
        toMaster = toMaster + "NAME: " + name;

        //REPLACE ## later w/ curr_loc.getLatitude & curr_loc.getLongitude
        toMaster = toMaster + "GPS[LAT:" + location[0] + ", LON:" + location[1] + "], ";

        // Add mannequin status
        toMaster = toMaster + "MAN: " + foundMann;

        // Add GPS coordinates of victim seen by LIDAR
        toMaster = toMaster + "LGPS[LAT:" + lgps[0] + ", LON:" + lgps[1] + "], ";
    }

    double[] getCoords (String str) {
        int find_comma, find_colon;

        //find lat
        find_colon = str.indexOf(':');
        find_comma = str.indexOf(',');
        String first_num = str.substring(find_colon+1, find_comma-1);

        //cut out lat
        str = str.substring(find_comma + 1, str.length());

        //find lon
        find_colon = str.indexOf(':');
        find_comma = str.indexOf(']');
        String sec_num = str.substring(find_colon+1, find_comma-1);


        double[] coords={0,0};
        double d = Double.parseDouble(first_num);
        double d2 = Double.parseDouble(sec_num);
        coords[0] = d;
        coords[1] = d2;
        return coords;
    }

    private class Robot {
        Robots name;
        double[] location = new double[2];
        boolean isMannequinFound = false;
        boolean isSearching = false;
        boolean hasLidar = false;
        double[] destination = new double[2];
        double[] lgps = new double[2];

        Robot(Robots name, double[] location, boolean hasLidar) {
            this.name = name;
            this.location = location;
            this.hasLidar = hasLidar;
        }

        Robots getName () {
            return name;
        }

        double[] getRobotLocation() {
            return location;
        }

        void setLocation (double[] location) {
            this.location = location;
        }

        void setDestination (double[] destination) {
            this.destination = destination;
            this.isSearching = true;
        }

        void setStatus(boolean isMannequinFound) {
            this.isMannequinFound = isMannequinFound;

            if (isMannequinFound) {
                this.isSearching = false;
            }
        }

        boolean getStatus() {
            return this.isMannequinFound;
        }

        boolean getIsSearching() {
            return this.isSearching;
        }

        void setObjectLocation (double[] lidarGPS) {
            this.lgps = lidarGPS;
        }

        double[] getObjectLocation () { return lgps; }
    }
}
