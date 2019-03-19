package e.gongfurui.digitallearnerlogbook.Helpers;

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
   * context:object
   * name: db name
   * param:factory
   * version: current db ver
   * */
  public MySQLiteOpenHelper(Context context, String name, int version)
  {
    this(context,name,null,version);
  }


  public MySQLiteOpenHelper(Context context, String name)
  {
    this(context, name, Version);
  }

  /**
   * Create the database
   * */
  @Override
  public void onCreate(SQLiteDatabase db) {
    System.out.println("Creating database:");
    String sql_learner = "create table learner(" +
            "id int primary key NOT NULL, " +
            "email varchar(200), " +
            "name varchar(200), " +
            "psw varchar, " +
            "ADI int,"+
            "super_id int DEFAULT null, " +
            "date_of_birth TEXT," +
            "time int DEFAULT 0, " +
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

    String sql_instructor = "create table instructor(" +
            "id int, " +
            "ADI int primary key, "+
            "email varchar(200), " +
            "name varchar(200), " +
            "psw varchar, " +
            "date_of_birth TEXT)";

    String sql_supervisor = "create table supervisor(" +
            "id int primary key, " +
            "email varchar(200), " +
            "name varchar(200), " +
            "psw varchar, " +
            "date_of_birth TEXT)";

    String sql_instructor_learner = "create table instructor_learner(" +
            "ADI int, " +
            "learner_id int, "+
            "feedback varchar(200) DEFAULT ''," +
            "primary key (ADI, learner_id))";

    String sql_supervisor_learner = "create table supervisor_learner(" +
            "driver_id int, " +
            "learner_id int, "+
            "primary key (driver_id, learner_id))";

    String sql_licence = "create table licence(" +
            "driver_id int primary key, " +
            "type varchar(40))";

    db.execSQL(sql_learner);
    db.execSQL(sql_instructor);
    db.execSQL(sql_supervisor);
    db.execSQL(sql_instructor_learner);
    db.execSQL(sql_supervisor_learner);
    db.execSQL(sql_licence);

  }

  /**
   * This function will be called when system find the db's ver is different
   * */
  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    System.out.println("Update database ver to: " + newVersion);
  }




}