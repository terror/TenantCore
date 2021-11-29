package com.example.tenantcore.viewmodel;

import android.content.Context;

import com.example.tenantcore.db.DatabaseException;
import com.example.tenantcore.db.TenantCoreDatabaseHandler;
import com.example.tenantcore.model.InviteCode;
import com.example.tenantcore.model.Landlord;
import com.example.tenantcore.model.PlaceholderContent;
import com.example.tenantcore.model.Tenant;

import java.util.List;

public class TenantCoreViewModel extends ObservableModel<TenantCoreViewModel> {

  private TenantCoreDatabaseHandler dbHandler;

//  private final List<Tenant> tenants;
//  private final List<Landlord> landlords;
//  private final List<Request> requests;
//  private final List<InviteCode> inviteCodes;

  private PlaceholderContent.PlaceholderItem tenant;

  @Override
  protected TenantCoreViewModel get() {
    return this;
  }

  public TenantCoreViewModel() {
    this.tenant = null;
  }

  public void initializeDatabase(Context context) throws DatabaseException {
    this.dbHandler = new TenantCoreDatabaseHandler(context);
//    this.tenants = dbHandler.getTenantTable().readAll();
//    this.landlords = dbHandler.getLandlordTable().readAll();
//    this.requests = dbHandler.getRequestTable().readAll();
//    this.inviteCodes = dbHandler.getInviteCodeTable().readAll();
  }

  public PlaceholderContent.PlaceholderItem getTenant() {
    return tenant;
  }

  public void setTenant(PlaceholderContent.PlaceholderItem tenant) {
    this.tenant = tenant;
  }

  // Returns whether or not an invite code exists with the provided code.
  public InviteCode findInviteCode(String code) {
    List<InviteCode> inviteCodes;
    try {
      inviteCodes = dbHandler.getInviteCodeTable().readAll();
    } catch (Exception e) {
      return null;
    }
    for (InviteCode inviteCode : inviteCodes) {
      if (String.valueOf(inviteCode.getCode()).equals(code))
        return inviteCode;
    }
    return null;
  }

  // Returns whether or not a tenant exists with the provided username.
  public Tenant findTenant(String username) {
    List<Tenant> tenants;
    try {
      tenants = dbHandler.getTenantTable().readAll();
    } catch (Exception e) {
      return null;
    }
    for (Tenant tenant : tenants) {
      if (String.valueOf(tenant.getUsername()).equals(username.toLowerCase()))
        return tenant;
    }
    return null;
  }

  // Returns whether or not a landlord exists with the provided username.
  public Landlord findLandlord(String username) {
    List<Landlord> landlords;
    try {
      landlords = dbHandler.getLandlordTable().readAll();
    } catch (Exception e) {
      return null;
    }
    for (Landlord landlord : landlords) {
      if (String.valueOf(landlord.getUsername()).equals(username.toLowerCase()))
        return landlord;
    }
    return null;
  }

  public void addTenant(Tenant tenant) throws DatabaseException {
    dbHandler.getTenantTable().create(tenant);
  }

  public void addLandlord(Landlord landlord) throws DatabaseException {
    dbHandler.getLandlordTable().create(landlord);
  }

  public void removeInviteCode(InviteCode inviteCode) throws DatabaseException {
    dbHandler.getInviteCodeTable().delete(inviteCode);
  }


}
