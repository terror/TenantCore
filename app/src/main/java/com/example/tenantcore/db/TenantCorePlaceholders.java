package com.example.tenantcore.db;

import com.example.tenantcore.model.InviteCode;

import java.util.ArrayList;
import java.util.List;

public class TenantCorePlaceholders {

  private static List<InviteCode> inviteCodes;

  private static void loadInviteCodes() {
    inviteCodes = new ArrayList<>();

    inviteCodes.add(new InviteCode().setLandlordId(1L).setCode(12345));
  }

  public static List<InviteCode> getInviteCodes() {
    if (inviteCodes == null) {
      try {
        loadInviteCodes();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return inviteCodes;
  }

}
