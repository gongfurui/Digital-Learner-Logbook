package e.gongfurui.digitallearnerlogbook.Helpers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.util.LogWriter;

import java.util.ArrayList;
import java.util.HashMap;

import e.gongfurui.digitallearnerlogbook.Roles.Instructor;
import e.gongfurui.digitallearnerlogbook.Roles.Learner;
import e.gongfurui.digitallearnerlogbook.Roles.Role;
import e.gongfurui.digitallearnerlogbook.Roles.Supervisor;

public class SQLQueryHelper {
    /**
     * Delete SQLite database
     * */
    public static void deleteDatabase(Context context, String dbname){
        System.out.println("Delete the database");
        MySQLiteOpenHelper dbHelper = new MySQLiteOpenHelper(context,
                dbname,2);
        // 调用getReadableDatabase()方法创建或打开一个可以读的数据库
        SQLiteDatabase sqliteDatabase = dbHelper.getReadableDatabase();
        //删除名为test.db数据库
        context.deleteDatabase(dbname);
    }


    /**
     * Insert data into SQLite database
     * */
    static public void insertDatabase(Context context, String query){
        System.out.println("Insert data");
        // 创建SQLiteOpenHelper子类对象
        MySQLiteOpenHelper dbHelper = new MySQLiteOpenHelper(context,"test_carson",2);
        // 调用getWritableDatabase()方法创建或打开一个可以读的数据库
        SQLiteDatabase  sqliteDatabase = dbHelper.getWritableDatabase();
        // 调用insert()方法将数据插入到数据库当中
        sqliteDatabase.execSQL(query);
        //close database
        sqliteDatabase.close();
    }

    /**
     * Update the data in the SQLite database
     * */
    static public void updateDatabase(Context context, String query){
        System.out.println("Update data");

        // 创建一个DatabaseHelper对象
        // 将数据库的版本升级为2
        // 传入版本号为2，大于旧版本（1），所以会调用onUpgrade()升级数据库
        MySQLiteOpenHelper dbHelper2 = new MySQLiteOpenHelper(context,"test_carson", 2);
        // 调用getWritableDatabase()得到一个可写的SQLiteDatabase对象
        SQLiteDatabase sqliteDatabase2 = dbHelper2.getWritableDatabase();
        sqliteDatabase2.execSQL(query);
        //关闭数据库
        sqliteDatabase2.close();
    }


    /**
     * Search the learner table through SQLite database
     * */
    public static Learner searchLearnerTable(Context context, String query){
        System.out.println("Search the database");
        // Create DatabaseHelper Object
        MySQLiteOpenHelper dbHelper = new MySQLiteOpenHelper(context,"test_carson",2);
        // 调用getWritableDatabase()方法创建或打开一个可以读的数据库
        SQLiteDatabase sqliteDatabase = dbHelper.getReadableDatabase();
        // 调用SQLiteDatabase对象的query方法进行查询
        // Return a Cursor object：由数据库查询返回的结果集对象
        Cursor cursor = sqliteDatabase.rawQuery(query,null);
        String id;
        String name;
        String email;
        String psw;
        String date_of_birth, time;

        ArrayList<Boolean> courseProgressList = new ArrayList<>();
        ArrayList<String> courseCommentList = new ArrayList<>();
        Learner learner = null;
        //Move the cursor to the next line and decide whether it has the next data
        while (cursor.moveToNext()) {
            id = cursor.getString(cursor.getColumnIndex("id"));
            name = cursor.getString(cursor.getColumnIndex("name"));
            email = cursor.getString(cursor.getColumnIndex("email"));
            psw = cursor.getString(cursor.getColumnIndex("psw"));
            date_of_birth = cursor.getString(cursor.getColumnIndex("date_of_birth"));
            time = cursor.getString(cursor.getColumnIndex("time"));
            for(int i = 1; i <= 23; i++){
                courseProgressList.add(cursor.getString(cursor.getColumnIndex("c" + i)).equals("0") ? false : true);
                courseCommentList.add(cursor.getString(cursor.getColumnIndex("c" + i+"c")));
            }
            learner = new Learner(new Role(name, email, psw), Integer.parseInt(id), date_of_birth,
                    Double.parseDouble(time), courseProgressList, courseCommentList);
        }
        //Close db
        sqliteDatabase.close();
        return learner;
    }

    /**
     * Search the lists of learner which instructor/supervisor in charge of
     * */
    static public HashMap<Integer, Learner> searchLearnersTable(Context context, String query){
        System.out.println("Search the database");
        // Create DatabaseHelper Object
        MySQLiteOpenHelper dbHelper = new MySQLiteOpenHelper(context,"test_carson",2);
        // 调用getWritableDatabase()方法创建或打开一个可以读的数据库
        SQLiteDatabase sqliteDatabase = dbHelper.getReadableDatabase();
        // 调用SQLiteDatabase对象的query方法进行查询
        // Return a Cursor object：由数据库查询返回的结果集对象
        Cursor cursor = sqliteDatabase.rawQuery(query,null);
        String id;
        String name;
        String email;
        String psw;
        String date_of_birth, time;
        ArrayList<Boolean> courseProgressList = new ArrayList<>();
        ArrayList<String> courseCommentList = new ArrayList<>();
        HashMap<Integer, Learner> learnerMap = null;
        //Move the cursor to the next line and decide whether it has the next data
        while (cursor.moveToNext()) {
            id = cursor.getString(cursor.getColumnIndex("id"));
            name = cursor.getString(cursor.getColumnIndex("name"));
            email = cursor.getString(cursor.getColumnIndex("email"));
            psw = cursor.getString(cursor.getColumnIndex("psw"));
            date_of_birth = cursor.getString(cursor.getColumnIndex("date_of_birth"));
            time = cursor.getString(cursor.getColumnIndex("time"));
            for(int i = 1; i <= 23; i++){
                courseProgressList.add(cursor.getString(cursor.getColumnIndex("c" + i)).equals("0") ? false : true);
                courseCommentList.add(cursor.getString(cursor.getColumnIndex("c" + i+"c")));
            }
            Learner learner = new Learner(new Role(name, email, psw), Integer.parseInt(id),
                    date_of_birth, Integer.parseInt(time), courseProgressList, courseCommentList);
            learnerMap.put(learner.driver_id, learner);
        }
        //Close db
        sqliteDatabase.close();
        return learnerMap;
    }

    /**
     * Search the instructor table through SQLite database
     * */
    static public Instructor searchInstructorTable(Context context, String query){
        System.out.println("Search the database");
        // Create DatabaseHelper Object
        MySQLiteOpenHelper dbHelper = new MySQLiteOpenHelper(context,"test_carson",2);
        // 调用getWritableDatabase()方法创建或打开一个可以读的数据库
        SQLiteDatabase sqliteDatabase = dbHelper.getReadableDatabase();
        // 调用SQLiteDatabase对象的query方法进行查询
        // Return a Cursor object：由数据库查询返回的结果集对象
        Cursor cursor = sqliteDatabase.rawQuery(query,null);
        String ADI;
        String name;
        String email;
        String psw;
        Instructor instructor = null;
        //Move the cursor to the next line and decide whether it has the next data
        while (cursor.moveToNext()) {
            ADI = cursor.getString(cursor.getColumnIndex("ADI"));
            name = cursor.getString(cursor.getColumnIndex("name"));
            email = cursor.getString(cursor.getColumnIndex("email"));
            psw = cursor.getString(cursor.getColumnIndex("psw"));
            instructor = new Instructor(new Role(name, email, psw),
                    Integer.parseInt(ADI));
        }
        //Close db
        sqliteDatabase.close();
        return instructor;
    }

    /**
     * Search the supervisor table through SQLite database
     * */
    static public Supervisor searchSupervisorTable(Context context, String query){
        System.out.println("Search the database");
        // Create DatabaseHelper Object
        MySQLiteOpenHelper dbHelper = new MySQLiteOpenHelper(context,"test_carson",2);
        // 调用getWritableDatabase()方法创建或打开一个可以读的数据库
        SQLiteDatabase sqliteDatabase = dbHelper.getReadableDatabase();
        // 调用SQLiteDatabase对象的query方法进行查询
        // Return a Cursor object：由数据库查询返回的结果集对象
        Cursor cursor = sqliteDatabase.rawQuery(query,null);
        String name;
        String email;
        String psw;
        Supervisor supervisor = null;
        //Move the cursor to the next line and decide whether it has the next data
        while (cursor.moveToNext()) {
            name = cursor.getString(cursor.getColumnIndex("name"));
            email = cursor.getString(cursor.getColumnIndex("email"));
            psw = cursor.getString(cursor.getColumnIndex("psw"));
            supervisor = new Supervisor(new Role(name, email, psw));
        }
        //Close db
        sqliteDatabase.close();
        return supervisor;
    }

    /**
     * Search throw the licence table
     * */
    static public boolean searchLicenceTable(Context context, String query){
        System.out.println("Search the database");
        // Create DatabaseHelper Object
        MySQLiteOpenHelper dbHelper = new MySQLiteOpenHelper(context,"test_carson",2);
        // 调用getWritableDatabase()方法创建或打开一个可以读的数据库
        SQLiteDatabase sqliteDatabase = dbHelper.getReadableDatabase();
        // 调用SQLiteDatabase对象的query方法进行查询
        // Return a Cursor object：由数据库查询返回的结果集对象
        Cursor cursor = sqliteDatabase.rawQuery(query,null);
        int id = 0;
        //Move the cursor to the next line and decide whether it has the next data
        while (cursor.moveToNext()) {
            id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("driver_id")));
        }
        //Close db
        sqliteDatabase.close();
        if(id == 0) return false;
        else return true;
    }

    /**
     * Search throw the ADIList table
     * */
    static public boolean searchADIListTable(Context context, String query){
        System.out.println("Search the database");
        // Create DatabaseHelper Object
        MySQLiteOpenHelper dbHelper = new MySQLiteOpenHelper(context,"test_carson",2);
        // 调用getWritableDatabase()方法创建或打开一个可以读的数据库
        SQLiteDatabase sqliteDatabase = dbHelper.getReadableDatabase();
        // 调用SQLiteDatabase对象的query方法进行查询
        // Return a Cursor object：由数据库查询返回的结果集对象
        Cursor cursor = sqliteDatabase.rawQuery(query,null);
        int adi = 0;
        //Move the cursor to the next line and decide whether it has the next data
        while (cursor.moveToNext()) {
            adi = Integer.parseInt(cursor.getString(cursor.getColumnIndex("adi")));
        }
        //Close db
        sqliteDatabase.close();
        if(adi == 0) return false;
        else return true;
    }

    /**
     * Search throw the courseFeedback table to get the instructor name who certify the progress
     * */
    static public ArrayList<String> getNameListFromCourseFeedbackTable(Context context, String query){
        System.out.println("Search the database");
        // Create DatabaseHelper Object
        MySQLiteOpenHelper dbHelper = new MySQLiteOpenHelper(context,"test_carson",2);
        // 调用getWritableDatabase()方法创建或打开一个可以读的数据库
        SQLiteDatabase sqliteDatabase = dbHelper.getReadableDatabase();
        // 调用SQLiteDatabase对象的query方法进行查询
        // Return a Cursor object：由数据库查询返回的结果集对象
        Cursor cursor = sqliteDatabase.rawQuery(query,null);
        ArrayList<String> nameList = new ArrayList<>();
        //Move the cursor to the next line and decide whether it has the next data
        while (cursor.moveToNext()) {
            String instructorName = "";
            instructorName = cursor.getString(cursor.getColumnIndex("instructor_name"));
            nameList.add(instructorName);
        }
        //Close db
        sqliteDatabase.close();
        return nameList;
    }
}
