package com.example.tenantcore.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.tenantcore.model.InviteCode;
import com.example.tenantcore.model.Landlord;
import com.example.tenantcore.model.Request;
import com.example.tenantcore.model.Tenant;

public class TenantCoreDatabaseHandler extends SQLiteOpenHelper {

  public static final String DATABASE_FILE_NAME = "tenantcore.db";
  public static final int DATABASE_VERSION = 1;

  private final Table<Tenant> tenantTable;
  private final Table<Landlord> landlordTable;
  private final Table<Request> requestTable;
  private final Table<InviteCode> inviteCodeTable;

  public TenantCoreDatabaseHandler(@Nullable Context context) {
    super(context, DATABASE_FILE_NAME, null, DATABASE_VERSION);
    tenantTable = new TenantTable(this);
    landlordTable = new LandlordTable(this);
    requestTable = new RequestTable(this);
    inviteCodeTable = new InviteCodeTable(this);
  }

  @Override
  public void onCreate(SQLiteDatabase database) {
    try {
      TenantCorePlaceholders.loadLandLord();
      TenantCorePlaceholders.loadTenantRequests();

      inviteCodeTable.createTable(database);
      landlordTable.createTable(database);
      tenantTable.createTable(database);
      requestTable.createTable(database);

    } catch (DatabaseException e) {
      System.err.println("Database table creation exception.");
    }
  }

  @Override
  public void onUpgrade(SQLiteDatabase database, int i, int i1) {

  }

  public Table<Tenant> getTenantTable() {
    return tenantTable;
  }

  public Table<Landlord> getLandlordTable() {
    return landlordTable;
  }

  public Table<Request> getRequestTable() {
    return requestTable;
  }

  public Table<InviteCode> getInviteCodeTable() {
    return inviteCodeTable;
  }
}
