package com.example.tenantcore.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.tenantcore.model.InviteCode;
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

  @Override
  public boolean hasInitialData() {
    return true;
  }

  @Override
  public void initialize(SQLiteDatabase database) throws DatabaseException {
    for (Landlord element : TenantCorePlaceholders.getLandlords()) {

      // Id of inserted element, -1 if error(?).
      long insertId = -1;

      // insert into DB
      try {
        ContentValues values = toContentValues(element);
        insertId = database.insertOrThrow(super.getName(), null, values);
      }
      catch (SQLException e) {
        throw new DatabaseException(e);
      }

      element.setId(insertId);
    }
  }
}
