package e.gongfurui.digitallearnerlogbookV2.Helpers;


import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import e.gongfurui.digitallearnerlogbookV2.Roles.CourseFeedback;
import e.gongfurui.digitallearnerlogbookV2.Roles.Instructor;
import e.gongfurui.digitallearnerlogbookV2.Roles.Learner;
import e.gongfurui.digitallearnerlogbookV2.Roles.Role;
import e.gongfurui.digitallearnerlogbookV2.Roles.Route;
import e.gongfurui.digitallearnerlogbookV2.Roles.Supervisor;
import e.gongfurui.digitallearnerlogbookV2.Utils.MyHTTPUtil;

import static e.gongfurui.digitallearnerlogbookV2.Helpers.ValuesHelper.LOCAL_IP;


public class OnlineDBHelper {

    public static Learner searchLearnerTable(String url){
        final Learner[] learner = {null};
        Thread thread = new Thread(){
            @Override
            public void run() {
                MyHTTPUtil util = new MyHTTPUtil();
                String res = util.get(url);
                Log.v("l_msg", res);
                JSONArray jarr = JSON.parseArray(res.substring(9, res.length()-1));
                if(jarr.size() > 0){
                    JSONObject jsonObj = jarr.getJSONObject(0);
                    int id;
                    String email, name, psw, date_of_birth;
                    double time,distance;
                    ArrayList<Boolean> courseProgressList = new ArrayList<>();
                    ArrayList<String> courseCommentList = new ArrayList<>();
                    id = jsonObj.getInteger("id");
                    email = jsonObj.getString("email");
                    name = jsonObj.getString("name");
                    psw = jsonObj.getString("psw");
                    date_of_birth = jsonObj.getString("date_of_birth");
                    time = jsonObj.getDouble("time");
                    distance = jsonObj.getDouble("distance");
                    for(int i = 1; i < 24; i++){
                        courseProgressList.add(jsonObj.getBoolean("c" + i));
                        courseCommentList.add(jsonObj.getString("c" + i + "c"));
                    }
                    learner[0] = new Learner(new Role(name, email, psw), id, date_of_birth,
                            time, distance, courseProgressList, courseCommentList);
                }
            }
        };
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return learner[0];
    }

    public static Instructor searchInstructorTable(String url){
        final Instructor[] instructor = {null};
        Thread thread = new Thread(){
            @Override
            public void run() {
                MyHTTPUtil util = new MyHTTPUtil();
                String res = util.get(url);
                JSONArray jarr = JSON.parseArray(res.substring(9, res.length()-1));
                if(jarr.size() > 0){
                    JSONObject jsonObj = jarr.getJSONObject(0);
                    String email, name, psw;
                    int ADI;
                    ADI = jsonObj.getInteger("ADI");
                    email = jsonObj.getString("email");
                    name = jsonObj.getString("name");
                    psw = jsonObj.getString("psw");
                    instructor[0] = new Instructor(new Role(name, email, psw), ADI);
                }
            }
        };
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return instructor[0];
    }

    public static Supervisor searchSupervisorTable(String url){
        final Supervisor[] supervisor = {null};
        Thread thread = new Thread(){
            @Override
            public void run() {
                MyHTTPUtil util = new MyHTTPUtil();
                String res = util.get(url);
                JSONArray jarr = JSON.parseArray(res.substring(9, res.length()-1));
                if(jarr.size() > 0){
                    JSONObject jsonObj = jarr.getJSONObject(0);
                    String email, name, psw;
                    email = jsonObj.getString("email");
                    name = jsonObj.getString("name");
                    psw = jsonObj.getString("psw");
                    supervisor[0] = new Supervisor(new Role(name, email, psw));
                }
            }
        };
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return supervisor[0];
    }

    public static boolean isInLicenceTable(String url){
        final boolean[] isIn = {false};
        Thread thread = new Thread(){
            @Override
            public void run() {
                MyHTTPUtil util = new MyHTTPUtil();
                String res = util.get(url);
                JSONArray jarr = JSON.parseArray(res.substring(9, res.length()-1));
                if(jarr.size() > 0){
                    isIn[0] = true;
                }
            }
        };
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return isIn[0];
    }

    public static HashMap<Integer, CourseFeedback> searchCourseFeedbacksTable(String url){
        HashMap<Integer, CourseFeedback> courseFeebackMap = new HashMap<>();
        Thread thread = new Thread(){
            @Override
            public void run() {
                MyHTTPUtil util = new MyHTTPUtil();
                String res = util.get(url);
                JSONArray jarr = JSON.parseArray(res.substring(9, res.length()-1));
                if(jarr.size() > 0){
                    for(int i = 0; i < jarr.size(); i++){
                        JSONObject jsonObj = jarr.getJSONObject(i);
                        String learnerMail, instructorName, feedback;
                        int cID;
                        cID = jsonObj.getInteger("course_id");
                        learnerMail = jsonObj.getString("learnerMail");
                        instructorName = jsonObj.getString("instructor_name");
                        feedback = jsonObj.getString("feedback");
                        CourseFeedback courseFeedback = new CourseFeedback(cID, learnerMail,
                                instructorName, feedback);
                        courseFeebackMap.put(cID, courseFeedback);
                    }
                }
            }
        };
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return courseFeebackMap;
    }

    public static HashMap<Integer, Route> searchRoutesTable(String url){
        HashMap<Integer, Route> routeMap = new HashMap<>();
        Thread thread = new Thread(){
            @Override
            public void run() {
                MyHTTPUtil util = new MyHTTPUtil();
                String res = util.get(url);
                JSONArray jarr = JSON.parseArray(res.substring(9, res.length()-1));
                if(jarr.size() > 0){
                    for(int i = 0; i < jarr.size(); i++){
                        JSONObject jsonObj = jarr.getJSONObject(i);
                        String learnerMail;
                        double distance, time, avgSpeed;
                        boolean isApprove;
                        int rID;
                        HashSet<LatLng> traceSet;
                        rID = jsonObj.getInteger("rid");
                        learnerMail = jsonObj.getString("learnerMail");
                        distance = jsonObj.getDouble("distance");
                        time = jsonObj.getDouble("time");
                        avgSpeed = jsonObj.getDouble("avgSpeed");
                        isApprove = jsonObj.getBoolean("isApproved");
                        traceSet = searchRouteLocationTable(LOCAL_IP +
                                "/drive/searchRouteLocation/" + rID);
                        Route route = new Route(rID, traceSet, distance, time, avgSpeed,
                                learnerMail, isApprove);
                        routeMap.put(rID, route);
                    }
                }
            }
        };
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return routeMap;
    }

    public static HashSet<LatLng> searchRouteLocationTable(String url){
        HashSet<LatLng> traceSet = new HashSet<>();
        Thread thread = new Thread(){
            @Override
            public void run() {
                MyHTTPUtil util = new MyHTTPUtil();
                String res = util.get(url);
                JSONArray jarr = JSON.parseArray(res.substring(9, res.length()-1));
                if(jarr.size() > 0){
                    for(int i = 0; i < jarr.size(); i++){
                        JSONObject jsonObj = jarr.getJSONObject(i);
                        double latitude, longitude;
                        latitude = jsonObj.getDouble("latitude");
                        longitude = jsonObj.getDouble("longitude");
                        traceSet.add(new LatLng(latitude, longitude));
                    }
                }
            }
        };
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return traceSet;
    }

    public static boolean isInADIListTable(String url){
        final boolean[] isIn = {false};
        Thread thread = new Thread(){
            @Override
            public void run() {
                MyHTTPUtil util = new MyHTTPUtil();
                String res = util.get(url);
                JSONArray jarr = JSON.parseArray(res.substring(9, res.length()-1));
                if(jarr.size() > 0){
                    isIn[0] = true;
                }
            }
        };
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return isIn[0];
    }

    public static void updateTable(String url){
        Thread thread = new Thread(){
            @Override
            public void run() {
                MyHTTPUtil util = new MyHTTPUtil();
                util.get(url);
            }
        };
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void insertTable(String url){
        Thread thread = new Thread(){
            @Override
            public void run() {
                MyHTTPUtil util = new MyHTTPUtil();
                util.get(url);
            }
        };
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Integer> searchIDsFromSupervisorLearnerTable(String url) {
        ArrayList<Integer> IDList = new ArrayList<>();
        Thread thread = new Thread(){
            @Override
            public void run() {
                MyHTTPUtil util = new MyHTTPUtil();
                String res = util.get(url);
                JSONArray jarr = JSON.parseArray(res.substring(9, res.length()-1));
                if(jarr.size() > 0){
                    for(int i = 0; i < jarr.size(); i++){
                        JSONObject jsonObj = jarr.getJSONObject(i);
                        int id;
                        id = jsonObj.getInteger("learner_id");
                        IDList.add(id);
                    }
                }
            }
        };
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return IDList;
    }
}
