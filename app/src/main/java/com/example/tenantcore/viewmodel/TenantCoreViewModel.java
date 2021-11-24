package com.example.tenantcore.viewmodel;

import com.example.tenantcore.model.PlaceholderContent;

public class TenantCoreViewModel {

  PlaceholderContent.PlaceholderItem tenant;

  public TenantCoreViewModel(){
    this.tenant = null;
  }

  public void setTenant(PlaceholderContent.PlaceholderItem tenant) {
    this.tenant = tenant;
  }

  public PlaceholderContent.PlaceholderItem getTenant() {
    return tenant;
  }
}
