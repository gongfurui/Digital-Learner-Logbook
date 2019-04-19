package e.gongfurui.digitallearnerlogbookV2.Helpers;


import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;

import e.gongfurui.digitallearnerlogbookV2.Roles.Instructor;
import e.gongfurui.digitallearnerlogbookV2.Roles.Learner;
import e.gongfurui.digitallearnerlogbookV2.Roles.Role;
import e.gongfurui.digitallearnerlogbookV2.Roles.Supervisor;
import e.gongfurui.digitallearnerlogbookV2.Utils.MyHTTPUtil;


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
}
