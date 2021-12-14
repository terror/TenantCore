package com.example.tenantcore.viewmodel;

import android.content.Context;
import android.net.Uri;

import com.example.tenantcore.db.DatabaseException;
import com.example.tenantcore.db.TenantCoreDatabaseHandler;
import com.example.tenantcore.model.InviteCode;
import com.example.tenantcore.model.Landlord;
import com.example.tenantcore.model.Request;
import com.example.tenantcore.model.Tenant;
import java.util.List;
import java.util.stream.Collectors;

public class TenantCoreViewModel extends ObservableModel<TenantCoreViewModel> {
  private TenantCoreDatabaseHandler dbHandler;
  private String signedInUser;
  private List<Tenant> tenants;
  private List<Landlord> landlords;
  private List<Request> requests;
  private List<InviteCode> inviteCodes;
  private Tenant tenant;
  private Uri imageUri;

  @Override
  protected TenantCoreViewModel get() {
    return this;
  }

  public TenantCoreViewModel() {
    this.tenant = null;
    this.signedInUser = null;
  }

  public void initializeDatabase(Context context) throws DatabaseException {
    this.dbHandler = new TenantCoreDatabaseHandler(context);
    this.tenants = dbHandler.getTenantTable().readAll();
    this.landlords = dbHandler.getLandlordTable().readAll();
    this.requests = dbHandler.getRequestTable().readAll();
    this.inviteCodes = dbHandler.getInviteCodeTable().readAll();
  }

  public void setSignedInUser(String username){
    this.signedInUser = username;
  }

  public String getSignedInUser(){
    return this.signedInUser;
  }

  public Tenant getTenant() {
    return tenant;
  }

  public void setTenant(Tenant tenant) {
    this.tenant = tenant;
  }

  public Uri getImageUri() {
    return imageUri;
  }

  public void setImageUri(Uri imageUri) {
    this.imageUri = imageUri;
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
      if (landlord.getUsername().equalsIgnoreCase(username))
        return landlord;
    }
    return null;
  }

  public void addTenant(Tenant tenant) throws DatabaseException {
    dbHandler.getTenantTable().create(tenant);
  }

  public boolean updateTenant(Tenant tenant) throws DatabaseException {
    return dbHandler.getTenantTable().update(tenant);
  }

  public void addLandlord(Landlord landlord) throws DatabaseException {
    dbHandler.getLandlordTable().create(landlord);
  }

  public void addInviteCode(InviteCode inviteCode) throws DatabaseException {
    dbHandler.getInviteCodeTable().create(inviteCode);
  }

  public void addRequest(Request request) {
    try {
      dbHandler.getRequestTable().create(request);
    } catch (DatabaseException e) {
      e.printStackTrace();
    }
  }

  public boolean updateTenantRequest(Request request) throws DatabaseException {
    return dbHandler.getRequestTable().update(request);
  }

  public boolean remoteTenantRequest(Request request) throws DatabaseException {
    return dbHandler.getRequestTable().delete(request);
  }

  public List<Request> getRequestsByTenant(Tenant tenant) throws DatabaseException {
    List<Request> allRequests = dbHandler.getRequestTable().readAll();

    return allRequests
      .stream()
      .filter((request) -> request.getTenantId().equals(tenant.getId()))
      .collect(Collectors.toList());
  }

  public List<Tenant> getTenantsByLandlord(Landlord landlord) throws DatabaseException {
    List<Tenant> allTenants = dbHandler.getTenantTable().readAll();

    return allTenants
      .stream()
      .filter((tenant) -> tenant.getLandlordId().equals(landlord.getId()))
      .collect(Collectors.toList());
  }
  public void removeInviteCode(InviteCode inviteCode) throws DatabaseException {
    dbHandler.getInviteCodeTable().delete(inviteCode);
  }
}
