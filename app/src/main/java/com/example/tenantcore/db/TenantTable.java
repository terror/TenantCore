package com.example.tenantcore.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.tenantcore.model.Tenant;

public class TenantTable extends Table<Tenant> {

  private static final String TABLE_NAME = "Tenant";
  private static final String COLUMN_LANDLORD_ID = "landlord_id";
  private static final String COLUMN_USERNAME = "username";
  private static final String COLUMN_NAME = "name";
  private static final String COLUMN_LAST_LOGIN = "last_login";

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
}