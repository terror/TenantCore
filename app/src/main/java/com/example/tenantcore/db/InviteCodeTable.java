package com.example.tenantcore.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.tenantcore.model.InviteCode;

public class InviteCodeTable extends Table<InviteCode> {
  private static final String TABLE_NAME = "InviteCode";
  private static final String COLUMN_LANDLORD_ID = "landlord_id";
  private static final String COLUMN_CODE = "code";
  private static final String COLUMN_EXPIRY = "expiry";

  public InviteCodeTable(SQLiteOpenHelper dbh) {
    super(dbh, TABLE_NAME);
    addColumn(new Column(COLUMN_CODE, Column.Type.INTEGER));
    addColumn(new Column(COLUMN_EXPIRY, Column.Type.TEXT));
    addColumn(new Column(COLUMN_LANDLORD_ID, Column.Type.INTEGER));
  }

  @Override
  protected ContentValues toContentValues(InviteCode element) throws DatabaseException {
    ContentValues values = new ContentValues();
    values.put(COLUMN_CODE, element.getCode());
    values.put(COLUMN_EXPIRY, Table.dateAsString(element.getExpiry()));
    values.put(COLUMN_LANDLORD_ID, element.getLandlordId());
    return values;
  }

  @Override
  protected InviteCode fromCursor(Cursor cursor) throws DatabaseException {
    return new InviteCode()
      .setId(cursor.getLong(0))
      .setCode(cursor.getInt(1))
      .setExpiry(Table.stringAsDate(cursor.getString(2)))
      .setLandlordId(cursor.getLong(3));
  }

  @Override
  public boolean hasInitialData() {
    return true;
  }

  @Override
  public void initialize(SQLiteDatabase database) throws DatabaseException {
    for (InviteCode element : TenantCorePlaceholders.getInviteCodes()) {

      // Id of inserted element, -1 if error(?).
      long insertId = -1;

      // insert into DB
      try {
        ContentValues values = toContentValues(element);
        insertId = database.insertOrThrow(super.getName(), null, values);
      } catch (SQLException e) {
        //database.close();
        throw new DatabaseException(e);
      }

      element.setId(insertId);
    }
  }
}
