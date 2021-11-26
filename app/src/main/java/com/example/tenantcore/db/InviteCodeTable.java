package com.example.tenantcore.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.tenantcore.model.InviteCode;

public class InviteCodeTable extends Table<InviteCode> {

  private static final String TABLE_NAME = "InviteCode";
  private static final String COLUMN_CODE = "code";
  private static final String COLUMN_EXPIRY = "expiry";

  public InviteCodeTable(SQLiteOpenHelper dbh) {
    super(dbh, TABLE_NAME);
    addColumn(new Column(COLUMN_CODE, Column.Type.INTEGER));
    addColumn(new Column(COLUMN_EXPIRY, Column.Type.TEXT));
  }

  @Override
  protected ContentValues toContentValues(InviteCode element) throws DatabaseException {
    ContentValues values = new ContentValues();
    values.put(COLUMN_CODE, element.getCode());
    values.put(COLUMN_EXPIRY, Table.dateAsString(element.getExpiry()));
    return values;
  }

  @Override
  protected InviteCode fromCursor(Cursor cursor) throws DatabaseException {
    return new InviteCode()
      .setId(cursor.getLong(0))
      .setCode(cursor.getInt(1))
      .setExpiry(Table.stringAsDate(cursor.getString(2)));
  }
}
