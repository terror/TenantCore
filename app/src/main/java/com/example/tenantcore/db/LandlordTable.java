package com.example.tenantcore.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.tenantcore.model.Landlord;

public class LandlordTable extends Table<Landlord> {

  private static final String TABLE_NAME = "Landlord";
  private static final String COLUMN_USERNAME = "username";
  private static final String COLUMN_NAME = "name";
  private static final String COLUMN_LAST_LOGIN = "last_login";

  public LandlordTable(SQLiteOpenHelper dbh) {
    super(dbh, TABLE_NAME);
    addColumn(new Column(COLUMN_USERNAME, Column.Type.TEXT));
    addColumn(new Column(COLUMN_NAME, Column.Type.TEXT));
    addColumn(new Column(COLUMN_LAST_LOGIN, Column.Type.TEXT));
  }

  @Override
  protected ContentValues toContentValues(Landlord element) throws DatabaseException {
    ContentValues values = new ContentValues();
    values.put(COLUMN_USERNAME, element.getUsername());
    values.put(COLUMN_NAME, element.getName());
    values.put(COLUMN_LAST_LOGIN, Table.dateAsString(element.getLastLogin()));
    return values;
  }

  @Override
  protected Landlord fromCursor(Cursor cursor) throws DatabaseException {
    return new Landlord()
      .setId(cursor.getLong(0))
      .setUsername(cursor.getString(1))
      .setName(cursor.getString(2))
      .setLastLogin(Table.stringAsDate(cursor.getString(3)));
  }
}
