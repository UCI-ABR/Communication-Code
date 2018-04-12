package com.example.android.communicationstrings;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
            hasGPSCoords = false,
            isMannequinFound = false,
            hasLIDAR = false;

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
                "SEARCHING: true" +
                "MANN: true" +
                "LIDAR: true" +
                "LGPS[LAT:1.55, LON:1.55];";

        receive_from_m();
        Log.i("TEST", "NAME: " + minion + ", " +
                "GPS" + "[" + gps_coords[0] + ", " + gps_coords[1] + "], " +
                "SEARCHING: " + hasGPSCoords +
                "MANN: " + isMannequinFound + ", " +
                "LIDAR: " + hasLIDAR + ", " +
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
    void receive_from_m () {
        String string_name = fromMinion.substring(fromMinion.indexOf("NAME"),fromMinion.indexOf("GPS")),
                string_gps = fromMinion.substring(fromMinion.indexOf("GPS"),fromMinion.indexOf("SEARCH")),
                string_search = fromMinion.substring(fromMinion.indexOf("SEARCH"),fromMinion.indexOf("MANN")),
                string_mann = fromMinion.substring(fromMinion.indexOf("MANN"),fromMinion.indexOf("LIDAR")),
                string_lidar = fromMinion.substring(fromMinion.indexOf("LIDAR"),fromMinion.indexOf("LGPS")),
                string_lidarGPS = fromMinion.substring(fromMinion.indexOf("LGPS"),fromMinion.length());

        //get GPS coordinates from message string
        gps_coords = getCoords(string_gps);

        //get mannequin boolean
        isMannequinFound = string_mann.contains("true");

        //get name of minion that sent string
        string_name = string_name.substring(string_name.indexOf(":")+2,string_name.indexOf(","));

        switch (string_name) {
            case "DOC":
                minion = Robots.DOC;
                robot = doc;
                break;
            case "MR":
                minion = Robots.MR;
                robot = mr;
                break;
            case "MRS":
                minion = Robots.MRS;
                robot = mrs;
                break;
            case "CARLITO":
                minion = Robots.CARLITO;
                robot = carlito;
                break;
            case "CARLOS":
                minion = Robots.CARLOS;
                robot = carlos;
                break;
            case "CARLY":
                minion = Robots.CARLY;
                robot = carly;
                break;
            case "CARLA":
                minion = Robots.CARLA;
                robot = carla;
                break;
            case "CARLETON":
                minion = Robots.CARLETON;
                robot = carleton;
                break;
            default:
                Log.i("ERROR","Invalid robot name. Refer to Robot name list in code.");
        }

        robot.setLocation(gps_coords);
        robot.setStatus(isMannequinFound);

        /*
        //get LIDAR boolean
        hasLIDAR = string_lidar.contains("true");

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

    //send grid # for m to search
    void send_to_m (Robots name, boolean mode, boolean scanMode, double[] gps_coords) {
        toMinion = "AUTOMODE: " + mode + "GOTO[LAT:" + gps_coords[0] + ", LON:" + gps_coords[1] + "], ";

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

    //receives grid location as string & parses to appropriate type
    void receive_from_M () {
        String string_auto_mode = fromMaster.substring(fromMaster.indexOf("AUTOMODE"), fromMaster.indexOf("GOTO")),
                string_coords = fromMaster.substring(fromMaster.indexOf("GOTO"), fromMaster.length());

        autoMode = string_auto_mode.contains("true");
        destinationCoords = getCoords(string_coords);
    }

    //send info to GPS of current location + obstacles and mannequins founds + status of grid search
    void send_to_M () {
        //REPLACE ## later w/ curr_loc.getLatitude & curr_loc.getLongitude
        toMaster = toMaster + "GPS[LAT:" + gps_coords[0] + ", LON:" + gps_coords[1] + "], ";

        //add searching status
        toMaster = toMaster + "SEARCHING: " + hasGPSCoords;

        //add mannequin status
        toMaster = toMaster + "MAN: " + isMannequinFound;

        //add LIDAR status
        toMaster = toMaster + "LIDAR: " + hasLIDAR;

        //add GPS coordinates of victim seen by LIDAR
        toMaster = toMaster + "LGPS[LAT:" + lidarGPS[0] + ", LON:" + lidarGPS[1] + "], ";
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

    class Robot {
        Robots name;
        double[] location = new double[2];
        boolean isMannequinFound = false;
        boolean isSearching = false;
        boolean hasLidar = false;
        double[] destination = new double[2];

        public Robot(Robots name, double[] location, boolean hasLidar) {
            this.name = name;
            this.location = location;
            this.hasLidar = hasLidar;
        }

        public Robots getName () {
            return name;
        }

        public double[] getRobotLocation() {
            return location;
        }

        public void setLocation (double[] location) {
            this.location = location;
        }

        public void setDestination (double[] destination) {
            this.destination = destination;
            this.isSearching = true;
        }

        void setStatus(boolean isMannequinFound) {
            this.isMannequinFound = isMannequinFound;

            if (isMannequinFound) {
                this.isSearching = false;
            }
        }

        public boolean getStatus() {
            return this.isMannequinFound;
        }

        public boolean getIsSearching() {
            return this.isSearching;
        }
    }
}
