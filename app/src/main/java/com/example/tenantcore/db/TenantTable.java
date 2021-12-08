package com.example.tenantcore.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.tenantcore.model.InviteCode;
import com.example.tenantcore.model.Tenant;

import java.util.ArrayList;
import java.util.List;

public class TenantTable extends Table<Tenant> {

  public static final String TABLE_NAME = "Tenant";
  public static final String COLUMN_LANDLORD_ID = "landlord_id";
  public static final String COLUMN_USERNAME = "username";
  public static final String COLUMN_NAME = "name";
  public static final String COLUMN_LAST_LOGIN = "last_login";

  public TenantTable(SQLiteOpenHelper dbh) {
    super(dbh, TABLE_NAME);
    addColumn(new Column(COLUMN_USERNAME, Column.Type.TEXT));
    addColumn(new Column(COLUMN_NAME, Column.Type.TEXT));
    addColumn(new Column(COLUMN_LAST_LOGIN, Column.Type.TEXT));
    addColumn(new Column(COLUMN_LANDLORD_ID, Column.Type.INTEGER));
  }

  @Override
  protected ContentValues toContentValues(Tenant element) throws DatabaseException {
    ContentValues values = new ContentValues();
    values.put(COLUMN_USERNAME, element.getUsername());
    values.put(COLUMN_NAME, element.getName());
    values.put(COLUMN_LAST_LOGIN, Table.dateAsString(element.getLastLogin()));
    values.put(COLUMN_LANDLORD_ID, element.getLandlordId());
    return values;
  }

  @Override
  protected Tenant fromCursor(Cursor cursor) throws DatabaseException {
    return new Tenant()
      .setId(cursor.getLong(0))
      .setUsername(cursor.getString(1))
      .setName(cursor.getString(2))
      .setLastLogin(Table.stringAsDate(cursor.getString(3)))
      .setLandlordId(cursor.getLong(4));
  }

  @Override
  public boolean hasInitialData() {
    return true;
  }

  @Override
  public void initialize(SQLiteDatabase database) throws DatabaseException {
    for (Tenant tenant : getInitialTenants()) {

      // Id of inserted element, -1 if error(?).
      long insertId = -1;

      // insert into DB
      try {
        ContentValues values = toContentValues(tenant);
        insertId = database.insertOrThrow(super.getName(), null, values);
      }
      catch (SQLException e) {
        throw new DatabaseException(e.getMessage());
      }

      tenant.setId(insertId);
    }
  }

  private List<Tenant> getInitialTenants(){
    // The 3 tenants to seed
    List<Tenant> tenants = new ArrayList<>(TenantCorePlaceholders.NUM_DEFAULT_TENANTS);
    tenants.add(new Tenant("joe.doug", "Joe Doug", TenantCorePlaceholders.DEFAULT_LANDLORD_ID));
    tenants.add(new Tenant("walter-white62", "Walter White", TenantCorePlaceholders.DEFAULT_LANDLORD_ID));
    tenants.add(new Tenant("bobthebuilder", "Bob Builder", TenantCorePlaceholders.DEFAULT_LANDLORD_ID));

    return tenants;
  }
}
