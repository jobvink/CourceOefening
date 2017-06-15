package com.example.gebruiker.courseoefening;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.gebruiker.courseoefening.api.GsonRequest;
import com.example.gebruiker.courseoefening.api.VolleyHelper;
import com.example.gebruiker.courseoefening.database.DatabaseHelper;
import com.example.gebruiker.courseoefening.database.DatabaseInfo;
import com.example.gebruiker.courseoefening.model.CourseModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        leesJSON();

        haalJSON();

        DatabaseHelper dbHelper = DatabaseHelper.getHelper(this);


//        ContentValues values = new ContentValues();
//        values.put(DatabaseInfo.CourseColumn.NAME, "Inf Keuze Programming Mobile Devices");
//        values.put(DatabaseInfo.CourseColumn.ECTS, "3");
//        values.put(DatabaseInfo.CourseColumn.PERIOD, "IKPMD");
//        values.put(DatabaseInfo.CourseColumn.GRADE, "5.5");
//
//// INSERT dit values object in DE (ZELFGEMAAKTE) RIJ COURSE,
//        dbHelper.insert(DatabaseInfo.CourseTables.COURSE, null, values);

        Cursor rs = dbHelper.query(DatabaseInfo.CourseTables.COURSE, new String[]{"*"}, null, null, null, null, null);

        rs.moveToFirst();   // Skip : de lege elementen vooraan de rij.

        String msg = "";
        String name = "";
        String ects = "";
        String periode = "";
        String grade = "";

        do {
            try {
                name = rs.getString(rs.getColumnIndex(DatabaseInfo.CourseColumn.NAME));
                ects = rs.getString(rs.getColumnIndex(DatabaseInfo.CourseColumn.ECTS));
                periode = rs.getString(rs.getColumnIndex(DatabaseInfo.CourseColumn.PERIOD));
                grade = rs.getString(rs.getColumnIndex(DatabaseInfo.CourseColumn.GRADE));
            } catch (Exception e) {
                name = e.toString();
                ects = e.toString();
                periode = e.toString();
                grade = e.toString();
            }
            msg += (name + ": " + periode + ": " + ects + System.getProperty("line.separator"));
        } while (rs.moveToNext());

        TextView database = (TextView) findViewById(R.id.database);
        if (database != null)
            database.setText(msg);
    }


    private void leesJSON() {
        String json = "[{name: ' module1', ects: 3, grade:6}," +
                "{name: ' module2', ects: 3, grade:6}]";
        Gson gson = new Gson();   // Dependency in gradle â€" next slide
        CourseModel[] courses = gson.fromJson(json, CourseModel[].class);	// CONVERTS THE JSON String to an array of model objects
        for (CourseModel c : courses)  {
            Log.d("Found :"," this => "+c.getName()+" <=");
        }
    }

    private void haalJSON(){
        requestSubjects();
    }

    private void requestSubjects(){
        Type type = new TypeToken<List<CourseModel>>(){}.getType();

        GsonRequest<List<CourseModel>> request = new GsonRequest<List<CourseModel>>("http://fuujokan.nl/subject_lijst.json",
                type, null, new Response.Listener<List<CourseModel>>() {
            @Override
            public void onResponse(List<CourseModel> response) {
                processRequestSucces(response);
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                processRequestError(error);
            }
        });
        VolleyHelper.getInstance(this).addToRequestQueue(request);
    }


    private void processRequestSucces(List<CourseModel> subjects ){
        DatabaseHelper dbHelper = DatabaseHelper.getHelper(this);
        // putting all received classes in my database.
        for (CourseModel cm : subjects) {
            Log.d("Found :"," this => "+cm.getPeriod()+" <=");
            ContentValues cv = new ContentValues();
            cv.put(DatabaseInfo.CourseColumn.NAME, cm.getName());
            cv.put(DatabaseInfo.CourseColumn.GRADE, cm.getGrade());
            cv.put(DatabaseInfo.CourseColumn.ECTS, cm.getEcts());
            cv.put(DatabaseInfo.CourseColumn.PERIOD, cm.getPeriod());
            dbHelper.insert(DatabaseInfo.CourseTables.COURSE, null, cv);
        }

        Cursor rs = dbHelper.query(DatabaseInfo.CourseTables.COURSE, new String[]{"*"}, null, null, null, null, null);
        rs.moveToFirst();   // kan leeg zijn en faalt dan
        DatabaseUtils.dumpCursor(rs);

    }

    private void processRequestError(VolleyError error){
        // WAT ZULLEN WE HIERMEE DOEN ?? - niets..
    }

}
