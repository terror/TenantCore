package com.example.tenantcore.db;

import com.example.tenantcore.model.InviteCode;
import com.example.tenantcore.model.Priority;
import com.example.tenantcore.model.Request;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TenantCorePlaceholders {

  private static List<InviteCode> inviteCodes;
  private static List<List<Request>> tenantRequests;
  public static final int NUM_DEFAULT_TENANTS = 3;
  public static final int NUM_DEFAULT_REQUESTS = 3;

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

  public static void loadTenantRequests(){
    tenantRequests = new ArrayList<>(NUM_DEFAULT_TENANTS);
    final SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd'T'hhmmss'Z'");
    try {
      tenantRequests.add(Arrays.asList(
        new Request("Mowing the lawn", "The grass in the frontyard is very tall and needs to be cut", fmt.parse("20220920T083000Z") ),
        new Request("Light problems", "The living room and kitchen lights suddenly stopped working, it looks like they need to be replaced", fmt.parse("20220122T083000Z")).setPriority(Priority.MEDIUM),
        new Request("Drain problems", "The bath tub water is overflowing and not draining. A plumber seems to be needed to fix issues", fmt.parse("20220112T083000Z")).setPriority(Priority.HIGH)
      ));

      tenantRequests.add(Arrays.asList(
        new Request("Issue with drain", "The water in the kitchen sink is not draining properly.", fmt.parse("20220120T083000Z")).setPriority(Priority.HIGH),
        new Request("Snow removing needed", "Snow needs to be cleared on the front driveway.", fmt.parse("20220115T083000Z")),
        new Request("Window repair", "Some kids broke one of the front window while playing soccer.", fmt.parse("20220102T083000Z")).setPriority(Priority.HIGH)
      ));

      tenantRequests.add(Arrays.asList(
        new Request("Front door issue", "The front door handle is broken and the door doesn't lock properly", fmt.parse("20221228T083000Z")).setPriority(Priority.HIGH),
        new Request("Roof leaking", "The roof on the second floor is leaking due the snow on the roof melting. The roof might also need to be replaced.", fmt.parse("20220120T083000Z")).setPriority(Priority.HIGH),
        new Request("Light not working", "Master bedroom light stopped working and the bulb might need to be replaced", fmt.parse("20220102T083000Z")).setPriority(Priority.MEDIUM)
      ));

    } catch (ParseException e) {
      e.printStackTrace();
    }
  }

  public static List<List<Request>> getTenantRequests(){
    return tenantRequests;
  }

}
