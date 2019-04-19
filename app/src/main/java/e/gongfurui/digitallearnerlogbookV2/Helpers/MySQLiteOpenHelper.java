package e.gongfurui.digitallearnerlogbookV2.Helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {


      //database ver.1
      private static Integer Version = 1;

      //constructor for SQLiteOpenHelper
      public MySQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                                int version) {
          //constructor from parent class
          super(context, name, factory, version);
      }
      /**
       * @param context context from the activty
       * @param name db name
       * @param version current version of db
       * @return
       * */
      public MySQLiteOpenHelper(Context context, String name, int version) {
          this(context,name,null,version);
      }


      public MySQLiteOpenHelper(Context context, String name)
      {
        this(context, name, Version);
      }

      /**
       * Create the tables in the database we will use in the digital learner logbook
       * */
      @Override
      public void onCreate(SQLiteDatabase db) {
          System.out.println("Creating database:");

          //The table for the learner
          String sql_learner = "create table learner(" +
                  "id int primary key NOT NULL, " +
                  "email varchar(200), " +
                  "name varchar(200), " +
                  "psw varchar, " +
                  "super_email varchar(200), " +
                  "date_of_birth TEXT," +
                  "time REAL DEFAULT 0.0, " +
                  "distance REAL DEFAULT 0.0, " +
                  "c1 int DEFAULT 0, " +
                  "c1c varchar(200) DEFAULT '', " +
                  "c2 int DEFAULT 0, " +
                  "c2c varchar(200) DEFAULT '', " +
                  "c3 int DEFAULT 0, " +
                  "c3c varchar(200) DEFAULT '', " +
                  "c4 int DEFAULT 0, " +
                  "c4c varchar(200) DEFAULT '', " +
                  "c5 int DEFAULT 0," +
                  "c5c varchar(200) DEFAULT '', " +
                  "c6 int DEFAULT 0," +
                  "c6c varchar(200) DEFAULT '', " +
                  "c7 int DEFAULT 0, " +
                  "c7c varchar(200) DEFAULT '', " +
                  "c8 int DEFAULT 0, "+
                  "c8c varchar(200) DEFAULT '', " +
                  "c9 int DEFAULT 0, " +
                  "c9c varchar(200) DEFAULT '', " +
                  "c10 int DEFAULT 0, " +
                  "c10c varchar(200) DEFAULT '', " +
                  "c11 int DEFAULT 0," +
                  "c11c varchar(200) DEFAULT '', " +
                  "c12 int DEFAULT 0," +
                  "c12c varchar(200) DEFAULT '', " +
                  "c13 int DEFAULT 0," +
                  "c13c varchar(200) DEFAULT '', " +
                  "c14 int DEFAULT 0, " +
                  "c14c varchar(200) DEFAULT '', " +
                  "c15 int DEFAULT 0, " +
                  "c15c varchar(200) DEFAULT '', " +
                  "c16 int DEFAULT 0, " +
                  "c16c varchar(200) DEFAULT '', " +
                  "c17 int DEFAULT 0," +
                  "c17c varchar(200) DEFAULT '', " +
                  "c18 int DEFAULT 0," +
                  "c18c varchar(200) DEFAULT '', " +
                  "c19 int DEFAULT 0," +
                  "c19c varchar(200) DEFAULT '', " +
                  "c20 int DEFAULT 0, " +
                  "c20c varchar(200) DEFAULT '', " +
                  "c21 int DEFAULT 0," +
                  "c21c varchar(200) DEFAULT '', " +
                  "c22 int DEFAULT 0, " +
                  "c22c varchar(200) DEFAULT '', " +
                  "c23 int DEFAULT 0," +
                  "c23c varchar(200) DEFAULT '')";

          //The table for the instructor
          String sql_instructor = "create table instructor(" +
                  "ADI int primary key, "+
                  "email varchar(200), " +
                  "name varchar(200), " +
                  "psw varchar)";

          //The table for the supervisor
          String sql_supervisor = "create table supervisor(" +
                  "email varchar(200) primary key, " +
                  "name varchar(200), " +
                  "psw varchar)";


          /*String sql_instructor_learner = "create table instructor_learner(" +
                "ADI int, " +
                "learner_id int, "+
                "feedback varchar(200) DEFAULT ''," +
                "primary key (ADI, learner_id))";*/

          //The table for the course feedback, which includes the relationship between learner and instructor
          String sql_courseFeedback = "create table courseFeedback(" +
                  "course_id int," +
                  "learner_id int," +
                  "instructor_name varchar(200)," +
                  "feedback varchar(200) DEFAULT ''," +
                  "primary key(course_id, learner_id))";

          //The table for building the relationship between learner and instructor
          String sql_supervisor_learner = "create table supervisor_learner(" +
                "email varchar(200), " +
                "learner_id int, "+
                "primary key (email, learner_id))";

          //the table for the current licence holder, including the types of learner and full
          String sql_licence = "create table licence(" +
                  "driver_id int primary key, " +
                  "type varchar(40))";

          //table for the current instructor list who has got the qualification to become an instructor
          String sql_adiList = "create table ADIList (adi int primary key)";

          //table for the route of the learner
          String sql_route = "create table route(" +
                  "id int primary key, " +
                  "distance REAL, " +
                  "time REAL, " +
                  "avgSpeed REAL, " +
                  "learnerID int, " +
                  "isApproved int DEFAULT 0)";

          //table for the route trace
          String sql_route_location = "create table route_location(" +
                  "id int, " +
                  "latitude REAL, " +
                  "longitude REAL," +
                  "primary key (id, latitude, longitude))";

          //execute the sql query to establish the schema of the database
          db.execSQL(sql_learner);
          db.execSQL(sql_instructor);
          db.execSQL(sql_supervisor);
          db.execSQL(sql_supervisor_learner);
          db.execSQL(sql_licence);
          db.execSQL(sql_adiList);
          db.execSQL(sql_courseFeedback);
          db.execSQL(sql_route);
          db.execSQL(sql_route_location);
      }

      /**
       * This function will be called when system find the db's ver is different
       * */
      @Override
      public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
          System.out.println("Update database ver to: " + newVersion);
      }
}