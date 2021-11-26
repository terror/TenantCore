package com.example.tenantcore.viewmodel;

import android.content.Context;

import com.example.tenantcore.db.DatabaseException;
import com.example.tenantcore.db.TenantCoreDatabaseHandler;
import com.example.tenantcore.model.InviteCode;
import com.example.tenantcore.model.Landlord;
import com.example.tenantcore.model.PlaceholderContent;
import com.example.tenantcore.model.Request;
import com.example.tenantcore.model.Tenant;

import java.util.ArrayList;
import java.util.List;

public class TenantCoreViewModel {

  private TenantCoreDatabaseHandler dbHandler;

  private final List<Tenant> tenants;
  private final List<Landlord> landlords;
  private final List<Request> requests;
  private final List<InviteCode> inviteCodes;

  private PlaceholderContent.PlaceholderItem tenant;

  public TenantCoreViewModel(Context context) throws DatabaseException {
    this.dbHandler = new TenantCoreDatabaseHandler(context);
    this.tenants = dbHandler.getTenantTable().readAll();
    this.landlords = dbHandler.getLandlordTable().readAll();
    this.requests = dbHandler.getRequestTable().readAll();
    this.inviteCodes = dbHandler.getInviteCodeTable().readAll();
    this.tenant = null;
  }

  public PlaceholderContent.PlaceholderItem getTenant() {
    return tenant;
  }

  public void setTenant(PlaceholderContent.PlaceholderItem tenant) {
    this.tenant = tenant;
  }
}
