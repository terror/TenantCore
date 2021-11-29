package com.example.tenantcore.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.tenantcore.model.InviteCode;
import com.example.tenantcore.model.Priority;
import com.example.tenantcore.model.Request;
import com.example.tenantcore.model.Status;

public class RequestTable extends Table<Request> {

  private static final String TABLE_NAME = "Request";
  private static final String COLUMN_TENANT_ID = "tenant_id";
  private static final String COLUMN_TITLE = "title";
  private static final String COLUMN_DESCRIPTION = "description";
  private static final String COLUMN_DATE = "date";
  private static final String COLUMN_STATUS = "status";
  private static final String COLUMN_PRIORITY = "priority";

  public RequestTable(SQLiteOpenHelper dbh) {
    super(dbh, TABLE_NAME);
    addColumn(new Column(COLUMN_TENANT_ID, Column.Type.INTEGER));
    addColumn(new Column(COLUMN_TITLE, Column.Type.TEXT));
    addColumn(new Column(COLUMN_DESCRIPTION, Column.Type.TEXT));
    addColumn(new Column(COLUMN_DATE, Column.Type.TEXT));
    addColumn(new Column(COLUMN_STATUS, Column.Type.INTEGER));
    addColumn(new Column(COLUMN_PRIORITY, Column.Type.INTEGER));
  }

  @Override
  protected ContentValues toContentValues(Request element) throws DatabaseException {
    ContentValues values = new ContentValues();
    values.put(COLUMN_TENANT_ID, element.getTenantId());
    values.put(COLUMN_TITLE, element.getTitle());
    values.put(COLUMN_DESCRIPTION, element.getDescription());
    values.put(COLUMN_DATE, Table.dateAsString(element.getDueDate()));
    values.put(COLUMN_STATUS, element.getStatus().ordinal());
    values.put(COLUMN_PRIORITY, element.getPriority().ordinal());
    return values;
  }

  @Override
  protected Request fromCursor(Cursor cursor) throws DatabaseException {
    return new Request()
      .setId(cursor.getLong(0))
      .setTenantId(cursor.getLong(1))
      .setTitle(cursor.getString(2))
      .setDescription(cursor.getString(3))
      .setDueDate(Table.stringAsDate(cursor.getString(4)))
      .setStatus(Status.values()[cursor.getInt(5)])
      .setPriority(Priority.values()[cursor.getInt(6)]);
  }
}
