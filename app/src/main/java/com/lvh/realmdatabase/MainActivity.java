package com.lvh.realmdatabase;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnAdd, btnRead, btnUpdate, btnEmployeeOver, btnDeleteByName, btnDeleteBySkill;
    private EditText edName, edAge, edSkill;
    private TextView tvRead, tvFillterSkill, tvFillterByAge;
    private Realm mRealm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
        mRealm = Realm.getDefaultInstance();

    }

    private void initData() {
        edName = findViewById(R.id.edName);
        edAge = findViewById(R.id.edAge);
        edSkill = findViewById(R.id.edSkill);

        tvRead = findViewById(R.id.tvResultRead);
        tvFillterByAge = findViewById(R.id.tvFilterByAge);
        btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);

        btnRead = findViewById(R.id.btnRead);
        btnRead.setOnClickListener(this);

        btnUpdate = findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(this);

        btnEmployeeOver = findViewById(R.id.btnFilterByAge);
        btnEmployeeOver.setOnClickListener(this);

        btnDeleteByName = findViewById(R.id.btnDeleteByName);
        btnDeleteByName.setOnClickListener(this);

//        btnDeleteBySkill = findViewById(R.id.btnDeleteAllWithSkill);
//        btnDeleteBySkill.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAdd:
                addEmployee();
                break;
            case R.id.btnRead:
                readEmployee();
                // Toast.makeText(this, "doc thanh cong", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btnUpdate:
                updateEmployee();
                break;
            case R.id.btnFilterByAge:
                fillterByAge();
                break;
            case R.id.btnDeleteByName:
                deleteEmployee();
                break;
//            case R.id.btnDeleteAllWithSkill:
//                break;
        }
    }

    private void fillterByAge() {
//        mRealm.executeTransaction(new Realm.Transaction() {
//            @Override
//            public void execute(Realm realm) {
//
//                RealmResults<Employee> results = realm.where(Employee.class).greaterThanOrEqualTo(Employee.PROPERTY_AGE, 25).findAllSortedAsync(Employee.PROPERTY_NAME);
//
//                tvFillterByAge.setText("");
//                for (Employee employee : results) {
//                    tvFillterByAge.append(employee.name + " age: " + employee.age + " skill: " + employee.skills);
//                }
//            }
//        });
    }


    private void deleteEmployee() {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Employee employee = realm.where(Employee.class).equalTo(Employee.PROPERTY_NAME, edName.getText().toString()).findFirst();
                if (employee != null) {
                    employee.deleteFromRealm();
                }
            }
        });
    }

    private void updateEmployee() {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {


                if (!edName.getText().toString().trim().isEmpty()) {


                    Employee employee = realm.where(Employee.class).equalTo(Employee.PROPERTY_NAME, edName.getText().toString()).findFirst();
                    if (employee == null) {
                        employee = realm.createObject(Employee.class, edName.getText().toString().trim());
                    }
                    if (!edAge.getText().toString().trim().isEmpty())
                        employee.age = Integer.parseInt(edAge.getText().toString().trim());

                    if (!edSkill.getText().toString().trim().isEmpty())
                        employee.skills = edSkill.getText().toString().trim();
//                    String languageKnown = inSkill.getText().toString().trim();
//                    Skill skill = realm.where(Skill.class).equalTo(Skill.PROPERTY_SKILL, languageKnown).findFirst();

//                    if (skill == null) {
//                        skill = realm.createObject(Skill.class, languageKnown);
//                        realm.copyToRealm(skill);
//                    }
//
//
//                    if (!employee.skills.contains(skill))
//                        employee.skills.add(skill);

                }
            }
        });
    }


    private void readEmployee() {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                RealmResults<Employee> results = realm.where(Employee.class).findAll();
                tvRead.setText("");
                for (Employee employee : results) {
                    tvRead.append(employee.name + " age: " + employee.age + " skill: " + employee.skills);

                }
            }
        });


    }

    private void addEmployee() {
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    try {


                        if (!edSkill.getText().toString().trim().isEmpty()) {
                            Employee employee = new Employee();
                            employee.name = edName.getText().toString().trim();

                            if (!edAge.getText().toString().trim().isEmpty())
                                employee.age = Integer.parseInt(edAge.getText().toString().trim());

                            if (!edSkill.getText().toString().trim().isEmpty())
                                employee.skills = edSkill.getText().toString().trim();


//                            String languageKnown = edSkill.getText().toString().trim();
//
//                            if (!languageKnown.isEmpty()) {
//                                Skill skill = realm.where(Skill.class).equalTo(Skill.PROPRERTY_SKILL, languageKnown).findFirst();
//
//                                if (skill == null) {
//                                    skill = realm.createObject(Skill.class, languageKnown);
//                                    realm.copyToRealm(skill);
//                                }
//
//                                employee.skills = new RealmList<>();
//                                employee.skills.add(skill);
//                            }

                            realm.copyToRealm(employee);
                        }

                    } catch (RealmPrimaryKeyConstraintException e) {
                        Toast.makeText(getApplicationContext(), "Primary Key exists, Press Update instead", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mRealm != null) {
            mRealm.close();
        }
    }
}
